/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileupload.util;

import org.sotech.fileupload.bean.Documento;
import org.sotech.fileupload.client.HTTPClientUtils;

public class ThreadOperation extends Thread {

    private Documento doc;
    private String path;

    public ThreadOperation() {

    }

    public static ThreadOperation build() {
        return new ThreadOperation();
    }

    @Override
    public void run() {
        try {
            HTTPClientUtils.uploadFile(doc, path);
        } catch (Exception e) {
            System.out.println("Error thread : " + e.getMessage());
        }
    }

    public Documento getDoc() {
        return doc;
    }

    public ThreadOperation setDoc(Documento doc) {
        this.doc = doc;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ThreadOperation setPath(String path) {
        this.path = path;
        return this;
    }

}
