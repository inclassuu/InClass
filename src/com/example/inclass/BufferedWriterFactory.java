/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * Buffere Writer, file writer.
 */
package com.example.inclass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
 
public class BufferedWriterFactory {
    public static BufferedWriter create(String path, String textFileName) throws IOException {
        //path check.
        File filePath = new File(path);
        if (!filePath.exists())
            filePath.mkdirs();
 
        BufferedWriter bw = new BufferedWriter(new FileWriter(path + File.separator + textFileName));
        return bw;
    }
}