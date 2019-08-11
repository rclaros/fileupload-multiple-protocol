/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileupload.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;

public class FileUtils {

    public static String checksumSHA256(String path) {
        try {
            return DigestUtils.sha256Hex(new FileInputStream(new File(path)));
        } catch (FileNotFoundException ex) {
            return ex.getMessage();
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }
}
