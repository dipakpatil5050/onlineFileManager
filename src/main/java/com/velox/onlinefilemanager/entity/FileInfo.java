package com.velox.onlinefilemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfo {

    private String name;
    private long size; // in KB
}