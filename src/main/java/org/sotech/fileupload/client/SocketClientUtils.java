/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileupload.client;

import org.sotech.fileupload.config.ApplicationConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketClientUtils {

    private static Socket s;

    public static void connectServer(String host, int port) {
//        try {
//            s = new Socket(host, port);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void uploadFilex(String path, String id) throws IOException {
        System.out.println("conected " + isOpen());
        if (!isOpen()) {
            SocketClientUtils.connectServer(ApplicationConfig.params.get("server"), 26);
        }
        File input = new File(path);
        try (DataOutputStream dos = new DataOutputStream(s.getOutputStream()); FileInputStream fis = new FileInputStream(input)) {
            dos.writeUTF(id + ".bin");
            dos.writeLong(input.length());
            byte[] buffer = new byte[4096];
            while (fis.read(buffer) > 0) {
                dos.write(buffer);
            }
        }
    }

    public static void uploadFile(String path, String id) throws IOException {
        File file = new File(path);
        Socket socket = new Socket(ApplicationConfig.params.get("server"), 21);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter.write("USER " + ApplicationConfig.params.get("user") + "\r\n");
        bufferedWriter.flush();
        bufferedWriter.write("PASS " + ApplicationConfig.params.get("password") + "\r\n");
        bufferedWriter.flush();
        bufferedWriter.write("CWD " + "" + "\r\n");
        bufferedWriter.flush();
        bufferedWriter.write("TYPE A\r\n");
        bufferedWriter.flush();
        bufferedWriter.write("PASV\r\n");
        bufferedWriter.flush();
        String response = null;
        while ((response = bufferedReader.readLine()) != null) {
            if (response.startsWith("530")) {
                System.err.println("Login aunthentication failed");
                break;
            }
            if (response.startsWith("227")) {
                String address = null;
                int port = -1;
                int opening = response.indexOf('(');
                int closing = response.indexOf(')', opening + 1);
                if (closing > 0) {
                    String dataLink = response.substring(opening + 1, closing);
                    StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
                    try {
                        address = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                        port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Socket transfer = new Socket(address, port);
                        bufferedWriter.write("STOR " + id + ".bin" + "\r\n");
                        bufferedWriter.flush();
                        response = bufferedReader.readLine();
                        if (response.startsWith("150")) {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            final int BUFFER_SIZE = 1024;
                            byte buffer[] = new byte[BUFFER_SIZE];
                            int len = 0, off = 0;
                            while ((len = fileInputStream.read(buffer)) != -1) {
                                transfer.getOutputStream().write(buffer, off, len);
                            }
                            transfer.getOutputStream().flush();
                            transfer.close();
                            bufferedWriter.write("QUIT\r\n");
                            bufferedWriter.flush();
                            bufferedReader.close();
                            socket.close();
                            break;
                        }
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            }
        }
    }

    public static void disconnect() {
        if (s != null && s.isConnected()) {
            try {
                s.close();
            } catch (IOException e) {
            }
        }
    }

    public static boolean isOpen() {
        return s.isConnected();
    }
}
