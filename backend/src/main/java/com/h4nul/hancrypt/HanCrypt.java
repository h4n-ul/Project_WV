package com.h4nul.hancrypt;

import org.bouncycastle.crypto.digests.Blake3Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * HaמCrypt
 * @author Haמul
 */

public class HanCrypt {
    HanCryptUtil hanCryptUtil = new HanCryptUtil();

    public static final int PASS_LIMIT = 16777216;
    public static final int SALT_LIMIT = 1048576;
    public static final int PASS_PREFIX = 32768;
    public static final int SALT_PREFIX = 2048;

    /**
     * HaמCrypt Hash
     */
    public CryPackage hash(String uid, String input, int passes, int saltRes) {
        if (passes < 8 || passes > PASS_LIMIT) {
            System.out.println("Illegal pass count, Auto-adjusting pass count to " + PASS_PREFIX + "...");
            passes = PASS_PREFIX;
        }
        if (saltRes < 0 || saltRes > SALT_LIMIT) {
            System.out.println("Illegal salt resoluton, Auto-adjusting resolution to " + SALT_PREFIX + "...");
            saltRes = SALT_PREFIX;
        }

        String[] p = pr1(uid, input, passes, null);
        byte[] salt = hanCryptUtil.randGenBytes(saltRes);
        String hash = Base64.getEncoder().encodeToString(pr2(p, salt, passes));
        String salt64 = Base64.getEncoder().encodeToString(salt);

        MD5Digest md5 = new MD5Digest();
        String pf = "@hc4@"+passes+"@"+hash+"@"+salt64+"@"+p[1];
        md5.update(pf.getBytes(), 0, pf.length());
        byte[] v = new byte[16]; md5.doFinal(v, 0);

        return new CryPackage("@hc4@"+hash+"@"+p[1]+"@"+Base64.getEncoder().encodeToString(v), salt64, passes);
    }

    /**
     * HaמCrypt Hash: All-in-one
     */
    public String hash_allinone(String uid, String input, int passes, int saltRes) {
        if (passes < 0 || passes > PASS_LIMIT) {
            System.out.println("Illegal pass count, Auto-adjusting pass count to " + PASS_PREFIX + "...");
            passes = PASS_PREFIX;
        }
        if (saltRes < 0 || saltRes > SALT_LIMIT) {
            System.out.println("Illegal salt resoluton, Auto-adjusting resolution to " + SALT_PREFIX + "...");
            saltRes = SALT_PREFIX;
        }

        String[] p = pr1(uid, input, passes, null);
        byte[] salt = hanCryptUtil.randGenBytes(saltRes);
        String hash = Base64.getEncoder().encodeToString(pr2(p, salt, passes));
        String salt64 = Base64.getEncoder().encodeToString(salt);

        MD5Digest md5 = new MD5Digest();
        String pf = "@hc4@"+passes+"@"+hash+"@"+salt64+"@"+p[1];
        md5.update(pf.getBytes(), 0, pf.length());
        byte[] v = new byte[16]; md5.doFinal(v, 0);

        return "@hc4@"+passes+"@"+hash+"@"+salt64+"@"+p[1]+"@"+Base64.getEncoder().encodeToString(v);
    }

    /**
     * HaמCrypt Simple
     */
    public CryPackage hash(String uid, String input) {
        String[] p = pr1(uid, input, PASS_PREFIX, null);
        byte[] salt = hanCryptUtil.randGenBytes(SALT_PREFIX);
        String hash = Base64.getEncoder().encodeToString(pr2(p, salt, PASS_PREFIX));
        String salt64 = Base64.getEncoder().encodeToString(salt);

        MD5Digest md5 = new MD5Digest();
        String pf = "@hc4@"+PASS_PREFIX+"@"+hash+"@"+salt64+"@"+p[1];
        md5.update(pf.getBytes(), 0, pf.length());
        byte[] v = new byte[16]; md5.doFinal(v, 0);

        return new CryPackage("@hc4@"+hash+"@"+p[1]+"@"+Base64.getEncoder().encodeToString(v), salt64, PASS_PREFIX);
    }
    /**
     * HaמCrypt Simple: All-in-one
     */
    public String hash_allinone(String uid, String input) {
        String[] p = pr1(uid, input, PASS_PREFIX, null);
        byte[] salt = hanCryptUtil.randGenBytes(SALT_PREFIX);
        String hash = Base64.getEncoder().encodeToString(pr2(p, salt, PASS_PREFIX));
        String salt64 = Base64.getEncoder().encodeToString(salt);

        MD5Digest md5 = new MD5Digest();
        String pf = "@hc4@"+PASS_PREFIX+"@"+hash+"@"+salt64+"@"+p[1];
        md5.update(pf.getBytes(), 0, pf.length());
        byte[] v = new byte[16]; md5.doFinal(v, 0);

        return "@hc4@"+PASS_PREFIX+"@"+hash+"@"+salt64+"@"+p[1]+"@"+Base64.getEncoder().encodeToString(v);
    }

    /**
     * HaמCrypt Preppered
     */
    public CryPackage hash(String uid, String input, int passes, String pepper) {
        if (passes < 0 || passes > PASS_LIMIT) {
            System.out.println("Illegal pass count, Auto-adjusting pass count to " + PASS_LIMIT + "...");
            passes = PASS_LIMIT;
        }

        String[] p = pr1(uid, input, passes, null);
        byte[] pepper_bytes = Base64.getDecoder().decode(pepper);
        String hash = Base64.getEncoder().encodeToString(pr2(p, pepper_bytes, passes));
        String pepper64 = Base64.getEncoder().encodeToString(pepper_bytes);

        MD5Digest md5 = new MD5Digest();
        String pf = "@hc4@"+passes+"@"+hash+"@"+pepper64+"@"+p[1];
        md5.update(pf.getBytes(), 0, pf.length());
        byte[] v = new byte[16]; md5.doFinal(v, 0);

        return new CryPackage("@hc4@"+hash+"@"+p[1]+"@"+Base64.getEncoder().encodeToString(v), null, passes);
    }

    /**
     * Testcode
     */
    public CryPackage hashTest() {
        String input = "H@nCr1pt";
        int passes = 96000;
        String uid = "9ae62da7-8753-7857-a2c4-fa23492ab5d6";

        String[] p = pr1(uid, input, passes, "Jr2gybviBsWLE7ioFUxSZ0dxXq7WM37Izrl6kmAM601/qzSqFiphFOEgBTFULqTcA10ydw2yWsck55MwZgfST4rKlQLwnMDpN9vPAbM205CXsd4pgjjaK4/3GUNTUbqEaVj9xO3qI0bLDpgQC+T0CaYsa+9Fcryt8UB2h1lO5am05lWNai3NaGT+So5Bwlf4OTwdYrWF7B+B9UR7Oz2dX2WfxvvfQtVzG3Ucw7Do7hHy86zZ46UIoWzRHobdNZRuJcw/Er6M+nxjULYaBADUwRjXt1avb6Mv2HSICniASZb8Pkj5F555fVvQkSLgOieZIUunD4Oi9pttXImav/9wKA==");
        byte[] salt = "Unus pro omnibus, Omnes pro uno.".getBytes();
        byte[] hash = pr2(p, salt, passes);

        return new CryPackage("@hc4@"+Base64.getEncoder().encodeToString(hash), Base64.getEncoder().encodeToString(salt), passes);
    }

    /**
     * Confirm
     */
    public Boolean confirm(String uid, String input, int passes, String hashDB, String salt) {
        String[] h = hashDB.split("\\@");
        if (!h[1].equals("hc4")){
            throw new IncompatibleVersionException("Incompatible password version: The password should have been hashed with HaמCrypt Ver.4 AuthPad.");
        }
        if (h.length == 5) {
            MD5Digest md5 = new MD5Digest();
            String p = "@hc4@"+passes+"@"+h[2]+"@"+salt+"@"+h[3];
            md5.update(p.getBytes(), 0, p.length());
            byte[] v = new byte[16]; md5.doFinal(v, 0);
            
            if (!h[4].equals(Base64.getEncoder().encodeToString(v))) {
                throw new IntegrityUnguaranteedException("Password has been corrupted.");
            }
        }

        byte[] hashDB_bytes = Base64.getDecoder().decode(h[2]);
        byte[] salt_bytes = Base64.getDecoder().decode(salt);

        String[] p = pr1(uid, input, passes, h[3]);
        byte[] hash = pr2(p, salt_bytes, passes);

        return MessageDigest.isEqual(hash, hashDB_bytes);
    }

    /**
     * Confirm: All in one
     */
    public Boolean confirm_allinone(String uid, String input, String db) {
        String[] parts = db.split("\\@");
        if (!parts[1].equals("hc4")){
            throw new IncompatibleVersionException("Incompatible password version: The password should have been hashed with HaמCrypt Ver.4 AuthPad.");
        }

        int passes = Integer.parseInt(parts[2]);
        String dbHashBase64 = parts[3];
        String dbSaltBase64 = parts[4];

        if (parts.length == 7) {
            MD5Digest md5 = new MD5Digest();
            String p = "@hc4@"+passes+"@"+dbHashBase64+"@"+dbSaltBase64+"@"+parts[5];
            md5.update(p.getBytes(), 0, p.length());
            byte[] v = new byte[16]; md5.doFinal(v, 0);
            
            if (!parts[6].equals(Base64.getEncoder().encodeToString(v))) {
                throw new IntegrityUnguaranteedException("Password has been corrupted.");
            }
        }

        byte[] dbHashBytes = Base64.getDecoder().decode(dbHashBase64);
        byte[] saltBytes = Base64.getDecoder().decode(dbSaltBase64);

        String[] p = pr1(uid, input, passes, parts[5]);
        byte[] hash = pr2(p, saltBytes, passes);

        return MessageDigest.isEqual(hash, dbHashBytes);
    }

    private String[] pr1(String uid, String input, int passes, String btStr) {
        Blake3Digest blake = new Blake3Digest(512);

        BlenderTable blend = hanCryptUtil.tableShaker(input.getBytes(), btStr);
        byte[] uidBytes = ((String)uid).getBytes();
        blake.update(uidBytes, 0, uidBytes.length); byte[] uidf = new byte[blake.getDigestSize()]; blake.doFinal(uidf, 0);

        byte[] stretchedUID = hanCryptUtil.shaCarousel(uidf, null, passes, blend);
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] stretchedInput = hanCryptUtil.shaCarousel(inputBytes, null, passes, blend);

        String processingPassword = Hex.toHexString(stretchedUID) + Hex.toHexString(stretchedInput);

        String[] prc = {processingPassword, blend.getBlenderTable()};
        return prc;
    }

    private byte[] pr2(String[] prc, byte[] salt, int passes) {
        byte[] prcBytes = hanCryptUtil.hexToBytes(prc[0]);
        BlenderTable blend = hanCryptUtil.tableShaker(prcBytes, prc[1]);
        byte[] hash = hanCryptUtil.shaCarousel(prcBytes, salt, passes, blend);

        return hash;
    }
}
