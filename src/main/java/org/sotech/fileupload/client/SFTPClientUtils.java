package org.sotech.fileupload.client;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import org.sotech.fileupload.bean.FileBean;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class SFTPClientUtils {

    private static JSch jsch = new JSch();
    private static Session session = null;

    public static void connectServer(String host, String user, String pwd) {
        try {
            session = jsch.getSession(user, host, 24);
            UserInfo ui = new SUserInfo(pwd, null);
            session.setUserInfo(ui);
            session.setPassword(pwd);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("sftp conected");
        } catch (JSchException e) {
            System.out.println("connectServer ftp " + e.getMessage());
        }
    }

    public static void uploadFile(String path, String id) throws Exception {
        ChannelSftp sftp = null;
        try {
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            sftp.put(path, "/" + id + ".bin");
        } catch (JSchException | SftpException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (sftp != null) {
                sftp.exit();
            }
        }
    }

    public static void downloadFile(String id, String filename, String root) {
        ChannelSftp sftp = null;
        try {
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            sftp.get("/" + id + ".bin", root + File.separator + filename);
        } catch (JSchException | SftpException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sftp != null) {
                sftp.exit();
            }
        }
    }

    public static void deleteFile(String filename, String from, String to) throws Exception {
        ChannelSftp sftp = null;
        try {
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            sftp.rm("/" + to + "/" + from + "/" + filename);
        } catch (JSchException | SftpException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sftp != null) {
                sftp.exit();
            }
        }
    }

    public static List<FileBean> listFile(String to) {
        List<FileBean> result = new ArrayList<>();
        ChannelSftp sftp = null;
        try {
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            sftp.cd("/" + to);
            Vector filelist = sftp.ls("/" + to);
            for (int i = 0; i < filelist.size(); i++) {
                ChannelSftp.LsEntry entry = (LsEntry) filelist.get(i);
                if (entry.getAttrs().isDir()) {
                    Vector child = sftp.ls("/" + to + "/" + entry.getFilename());
                    if (child.size() > 0) {
                        for (int j = 0; j < child.size(); j++) {
                            ChannelSftp.LsEntry item = (LsEntry) child.get(j);
                            if (!item.getAttrs().isDir()) {
                                FileBean bean = new FileBean();
                                bean.setName(item.getFilename());
                                bean.setOwner(entry.getFilename());
                                bean.setRoot(to);
                                int t = item.getAttrs().getMTime();
                                bean.setDate(new Date(t * 1000L));
                                bean.setPath(entry.getFilename() + File.separator + item.getFilename());
                                result.add(bean);
                            }
                        }
                    }
                }
            }
        } catch (JSchException | SftpException e) {

        } finally {
            if (sftp != null) {
                sftp.exit();
            }
        }
        return result;
    }

    public static void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    public static boolean isOpen() {
        return session.isConnected();
    }
}
