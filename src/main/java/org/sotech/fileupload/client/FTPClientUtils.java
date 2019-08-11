package org.sotech.fileupload.client;

import org.sotech.fileupload.bean.FileBean;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPClientUtils {

    private static FTPClient ftp = null;

    public static void connectServer(String host,int port, String user, String pwd) {
        try {
            FTPClientUtils.ftp = new FTPClient();
            int reply;
            FTPClientUtils.ftp.connect(host,port);
            reply = FTPClientUtils.ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                FTPClientUtils.ftp.disconnect();
                throw new Exception("Exception in connecting to FTP Server");
            }
            FTPClientUtils.ftp.login(user, pwd);
            FTPClientUtils.ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            FTPClientUtils.ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            FTPClientUtils.ftp.enterLocalPassiveMode();
            FTPClientUtils.ftp.changeWorkingDirectory("/");
            System.out.println("ftp conected");
        } catch (Exception e) {
            System.out.println("connectServer ftp " + e.getMessage());
        }
    }

    public static boolean uploadFile(String path, String id) throws Exception {
        try (InputStream input = new FileInputStream(new File(path))) {
            return  FTPClientUtils.ftp.storeFile(id + ".bin", input);
        }
    }

    public static void downloadFile(String id, String filename, String root) {
        try {
            File file = new File(root + File.separator + filename);
            OutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            FTPClientUtils.ftp.retrieveFile(id + ".bin", stream);
            stream.close();
        } catch (IOException e) {
            File output = new File(root + File.separator + filename);
            if (output.exists()) {
                output.delete();
            }
        }
    }

    public static void deleteFile(String path, String to) {
        try {
            FTPClientUtils.ftp.deleteFile("/" + to + "/" + path);
        } catch (IOException e) {
        }
    }

    public static List<FileBean> listFile(String to) {
        List<FileBean> result = new ArrayList<>();
        try {
            FTPFile[] files = FTPClientUtils.ftp.listFiles("/" + to);
            for (FTPFile file : files) {
                if (file.isDirectory()) {
                    FTPFile[] childs = FTPClientUtils.ftp.listFiles("/" + to + "/" + file.getName());
                    for (FTPFile child : childs) {
                        FileBean bean = new FileBean();
                        bean.setName(child.getName());
                        bean.setOwner(file.getName());
                        bean.setRoot(to);
                        bean.setDate(child.getTimestamp().getTime());
                        bean.setPath(to + File.separator + file.getName() + File.separator + child.getName());
                        result.add(bean);
                    }
                }
            }
        } catch (IOException e) {
        }
        return result;
    }

    public static void disconnect() {
        if (FTPClientUtils.ftp != null && FTPClientUtils.ftp.isConnected()) {
            try {
                FTPClientUtils.ftp.logout();
                FTPClientUtils.ftp.disconnect();
            } catch (IOException f) {
                System.out.println("disconnect " + f.getMessage());
            }
        }
    }

    public static boolean isOpen() {
        return FTPClientUtils.ftp.isConnected();
    }
}
