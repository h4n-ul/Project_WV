package com.h4nul.hancrypt;

import java.security.SecureRandom;
import java.util.*;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.RIPEMD320Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.SHAKEDigest;

public class HanCryptUtil {

    BlenderTable tableShaker(byte[] input, String btStr) {
        SHAKEDigest shake = new SHAKEDigest(256);

        shake.update(input, 0, input.length);
        byte[] shaketable = new byte[shake.getDigestSize()];
        shake.doFinal(shaketable, 0);

        int slf = shaketable[0] & 0xff;

        byte[][] blenderTable;
        if (btStr == null) {
            blenderTable = createTable();
        }
        else {
            blenderTable = tableUnzipper(btStr);
        }

        for (int i = 0; i < blenderTable.length; i++) {
            for (int j = 0; j < blenderTable[i].length; j++) {
                if (blenderTable[i][j] == (byte) slf) {
                    return new BlenderTable(j, i, tableZipper(blenderTable));
                }
            }
        }

        throw new IntegrityUnguaranteedError("Detected tampering or corruption on Blender table coordinate selection logic.");
    }

    String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    byte[] shaCarousel(byte[] input, byte[] salt, int passes, BlenderTable blend) {
        String shiftDir;
        String[] alg;
        boolean not = false;
        switch (blend.getTClass()) {
            case 0:  String[] alg0x0 = {"SHAKE", "RIPE"}; alg = alg0x0; not = true; shiftDir = "right"; break;
            case 1:  String[] alg0x1 = {"SHA", "SHAKE"};  alg = alg0x1; not = true; shiftDir = "left";  break;
            case 2:  String[] alg0x2 = {"SHA", "SHAKE"};  alg = alg0x2; not = true; shiftDir = "right"; break;
            case 3:  String[] alg0x3 = {"SHAKE", "RIPE"}; alg = alg0x3;             shiftDir = "left";  break;
            case 4:  String[] alg0x4 = {"SHAKE", "SHA"};  alg = alg0x4;             shiftDir = "right"; break;
            case 5:  String[] alg0x5 = {"RIPE", "SHAKE"}; alg = alg0x5;             shiftDir = "left";  break;
            case 6:  String[] alg0x6 = {"RIPE", "SHAKE"}; alg = alg0x6; not = true; shiftDir = "right"; break;
            case 7:  String[] alg0x7 = {"SHA", "SHAKE"};  alg = alg0x7;             shiftDir = "right"; break;
            case 8:  String[] alg0x8 = {"SHA", "SHAKE"};  alg = alg0x8;             shiftDir = "left";  break;
            case 9:  String[] alg0x9 = {"RIPE", "SHAKE"}; alg = alg0x9; not = true; shiftDir = "left";  break;
            case 10: String[] alg0xa = {"RIPE", "SHAKE"}; alg = alg0xa;             shiftDir = "right"; break;
            case 11: String[] alg0xb = {"SHAKE", "SHA"};  alg = alg0xb;             shiftDir = "left";  break;
            case 12: String[] alg0xc = {"SHAKE", "RIPE"}; alg = alg0xc;             shiftDir = "right"; break;
            case 13: String[] alg0xd = {"SHAKE", "SHA"};  alg = alg0xd; not = true; shiftDir = "right"; break;
            case 14: String[] alg0xe = {"SHAKE", "SHA"};  alg = alg0xe; not = true; shiftDir = "left";  break;
            case 15: String[] alg0xf = {"SHAKE", "RIPE"}; alg = alg0xf; not = true; shiftDir = "left";  break;
            default: throw new IllegalArgumentException("Invalid Offset");
        }
        return digest(input, salt, alg, not, shiftDir, passes, blend);
    }

    private byte[] digest(byte[] s, byte[] salt, String[] algStr, boolean not, String shiftDir, int passes, BlenderTable blend) {
        int shakeSize = blend.getOffset();
        int dSize = blend.getOffset() % 4;
        Object algs[] = {new Object(), new Object()};
        int sz = 0;

        for (int i = 0; i < algStr.length; i++) {
            if (algStr[i].equals("SHA")) {
                switch(dSize) {
                    case 0: algs[i] = new SHA3Digest(224); sz = 28; break;
                    case 1: algs[i] = new SHA3Digest(256); sz = 32; break;
                    case 2: algs[i] = new SHA3Digest(384); sz = 48; break;
                    case 3: algs[i] = new SHA3Digest(512); sz = 64; break;
                }
            }
            else if (algStr[i].equals("SHAKE")) {
                algs[i] = new SHAKEDigest(256);
            }
            else if (algStr[i].equals("RIPE")) {
                switch(dSize) {
                    case 0: algs[i] = new RIPEMD128Digest(); sz = 16; break;
                    case 1: algs[i] = new RIPEMD160Digest(); sz = 20; break;
                    case 2: algs[i] = new RIPEMD256Digest(); sz = 32; break;
                    case 3: algs[i] = new RIPEMD320Digest(); sz = 40; break;
                }
            }
            else {
                throw new IntegrityUnguaranteedError("Detected tampering or corruption on Digest size.");
            }
        }

        boolean shiftDirBool;
        if (shiftDir == "right") {
            shiftDirBool = false;
        }
        else if (shiftDir == "left") {
            shiftDirBool = true;
        }
        else {
            throw new IntegrityUnguaranteedError("Detected tampering or corruption on Shift direction.");
        }

        int n = algs.length;
        for (int i = 0; i < passes; i++) {
            s = byteConcat(s, salt);
            Digest cAlg = (Digest)algs[i % n];
            cAlg.update(s, 0, s.length);
            if (cAlg instanceof SHAKEDigest) {
                byte[] out = new byte[64];
                cAlg.doFinal(out, 0);
                s = new byte[(32 + (int) Math.pow(shakeSize, 1.2798))];
                System.arraycopy(out, 0, s, 0, s.length);
            }
            else {
                byte[] out = new byte[64];
                cAlg.doFinal(out, 0);
                s = new byte[sz];
                System.arraycopy(out, 0, s, 0, s.length);
            }
            cAlg.reset();
            if (not) {
                for (int j = 0; j < s.length; j++) {
                    s[j] = (byte) ~s[j];
                }
                // System.out.print("NOT-");
            }
            // System.out.println("Pass"+i+".alg("+algStr[i%n]+"): "+bytesToHex(s));
            if (i % 16 == 15) {s = byteCycl(s, shiftDirBool);
                // System.out.println("Cycled: "+bytesToHex(s));
            }
        }
        return s;
    }

    // 0 = right, 1 = left
    byte[] byteCycl(byte[] input, Boolean dir) {
        if (input == null || input.length <= 1) return input;
        if (!dir) {
            if (input == null || input.length <= 1) return input;
            byte[] res = new byte[input.length];
            res[0] = input[input.length - 1];
            System.arraycopy(input, 0, res, 1, input.length - 1);
            return res;
        } else {
            byte[] res = new byte[input.length];
            res[res.length - 1] = input[0];
            System.arraycopy(input, 1, res, 0, input.length - 1);
            return res;
        }
    }

    byte[] randGenBytes(int length) {
        byte[] res = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(res);

        return res;
    }

    byte[] byteConcat(byte[] a, byte[] b) {
        if (a == null && b == null){return null;}else if (a == null){return b;}else if(b == null){return a;}
        byte[] con = new byte[a.length + b.length];
        System.arraycopy(a, 0, con, 0, a.length);
        System.arraycopy(b, 0, con, a.length, b.length);
        return con;
    }

    public static byte[][] createTable() {
        byte[][] b = new byte[16][16];
        int v = 0;
        List<Byte> list = new ArrayList<>();
        
        for (int i = 0; i < 256; ++i) {
            list.add((byte)v++);
        }

        Collections.shuffle(list);

        for (int i = 0; i < b.length; ++i) {
            for (int j = 0; j < b[i].length; ++j) {
                b[i][j] = list.get(i*16 + j);
            }
        }

        return b;
    }

    public static String tableZipper(byte[][] b) {
        byte[] f = new byte[256];
        for (int i = 0; i < 16; ++i) {
            System.arraycopy(b[i], 0, f, i * 16, b[i].length);
        }
        return Base64.getEncoder().encodeToString(f);
    }

    private static byte[][] tableUnzipper(String fa) {
        byte[] fab = Base64.getDecoder().decode(fa);
        byte[][] table = new byte[16][16];
        
        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table[i].length; j++){
                table[i][j] = fab[i * 16 + j];
            }
        }

       return table;
   }
}
