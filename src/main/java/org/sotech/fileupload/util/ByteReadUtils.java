/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileupload.util;

import org.sotech.fileupload.config.ApplicationConfig;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author LaboratorioFISI
 */
public class ByteReadUtils {

    public ArrayList<String> readAndFragment(String SourceFileName, int CHUNK_SIZE) throws IOException {
        File root = new File(ApplicationConfig.root + File.separator + "tmp");
        if (!root.exists()) {
            root.mkdir();
        }
        File willBeRead = new File(SourceFileName);
        int FILE_SIZE = (int) willBeRead.length();
        ArrayList<String> nameList = new ArrayList<String>();
        int NUMBER_OF_CHUNKS = 0;
        byte[] temporary = null;
        try {
            InputStream inStream = null;
            int totalBytesRead = 0;
            try {
                inStream = new BufferedInputStream(new FileInputStream(willBeRead));
                while (totalBytesRead < FILE_SIZE) {
                    String PART_NAME = "data" + NUMBER_OF_CHUNKS + ".bin";
                    int bytesRemaining = FILE_SIZE - totalBytesRead;
                    if (bytesRemaining < CHUNK_SIZE) {
                        CHUNK_SIZE = bytesRemaining;
                    }
                    temporary = new byte[CHUNK_SIZE]; //Temporary Byte Array
                    int bytesRead = inStream.read(temporary, 0, CHUNK_SIZE);
                    if (bytesRead > 0) // If bytes read is not empty
                    {
                        totalBytesRead += bytesRead;
                        NUMBER_OF_CHUNKS++;
                    }
                    write(temporary, root + File.separator + PART_NAME);
                    nameList.add(root + File.separator + PART_NAME);
                }
            } finally {
                inStream.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return nameList;
    }

    void write(byte[] DataByteArray, String DestinationFileName) {
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(DestinationFileName));
                output.write(DataByteArray);
            } finally {
                output.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
