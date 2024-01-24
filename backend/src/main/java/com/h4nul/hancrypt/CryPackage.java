package com.h4nul.hancrypt;

/**
 * CryPackage
 */
public class CryPackage {
    private String hash;
    private String salt;
    private int passes;

    public CryPackage(String hash, String salt, int passes) {
        this.hash = hash;
        this.salt = salt;
        this.passes = passes;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public int getPasses() {
        return passes;
    }
}