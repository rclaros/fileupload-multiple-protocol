/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileupload.util;

import org.sotech.fileupload.client.ClientManager;

public class ThreadStart extends Thread {

    private String email;

    @Override
    public void run() {
        try {
            ClientManager.start();
            ClientManager.downloadAll(email);
        } catch (Exception e) {
            System.out.println("Error thread : " + e.getMessage());
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
