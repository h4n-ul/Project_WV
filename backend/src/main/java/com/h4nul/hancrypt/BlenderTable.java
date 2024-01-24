package com.h4nul.hancrypt;

import lombok.Getter;
import lombok.Setter;

/**
 * BlenderTable
 */
@Getter
@Setter
public class BlenderTable {
    private int offset;
    private int tClass;
    private String blenderTable;

    public BlenderTable(int offset, int tClass, String blenderTable) {
        this.offset = offset;
        this.tClass = tClass;
        this.blenderTable = blenderTable;
    }
}
