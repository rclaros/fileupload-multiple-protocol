package org.sotech.fileupload.client;

import org.sotech.fileupload.bean.Documento;
import org.sotech.fileupload.config.ApplicationConfig;
import org.sotech.fileupload.util.FactoryMQ;
import org.sotech.fileupload.util.FileUtils;
import org.sotech.fileupload.util.ListUtils;
import org.sotech.fileupload.util.WSClientUpload;
import org.sotech.fileupload.ui.StartManager;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ClientManager {

    public static boolean isUploaded = false;
    public static WSClientUpload upload = null;

    public static void start() {
        FactoryMQ.start();
        FTPClientUtils.connectServer(ApplicationConfig.params.get("server"), Integer.parseInt(ApplicationConfig.params_protocol.get("ftp")), ApplicationConfig.params.get("user"), ApplicationConfig.params.get("password"));
        SFTPClientUtils.connectServer(ApplicationConfig.params.get("server"), ApplicationConfig.params.get("user"), ApplicationConfig.params.get("password"));
        try {
            upload = new WSClientUpload(new URI("ws://"+ApplicationConfig.server_ip+":8080/fileupload/postdata"));
            upload.start();
            System.out.println("ws upload conected");
        } catch (URISyntaxException e) {
            System.out.println("error start " + e.getMessage());
        }
    }

    public static void downloadAll(String to) {
        List<Documento> docs = HTTPClientUtils.getFilesPending(to);
        if (!docs.isEmpty()) {
            for (Documento doc : docs) {
                download(doc);
            }
        }
    }

    public static void download(Documento doc) {
        File input = new File(ApplicationConfig.root + File.separator + doc.getUserTo() + File.separator + doc.getUserFrom());
        if (!input.exists()) {
            input.mkdirs();
        }
        if (doc.getProtocol().equals("ftp")) {
            FTPClientUtils.downloadFile(doc.getId(), doc.getFilename(), input.getAbsolutePath());
            File ouput = new File(input.getAbsolutePath() + File.separator + doc.getFilename());
            if (ouput.exists() && ouput.length() > 0) {
                String status = "Normal";
                if (!doc.getChecksum().equals(FileUtils.checksumSHA256(input.getAbsolutePath() + File.separator + doc.getFilename()))) {
                    status = "Modificado";
                }
                String time = DateFormat.getDateTimeInstance().format(new Date());
                ListUtils.addReceive(doc.getUserFrom(), doc.getFilename(), time, status);
                StartManager.windows.setContadorReceive(1);
            }
        } else {
            if (doc.getProtocol().equals("sftp")) {
                SFTPClientUtils.downloadFile(doc.getId(), doc.getFilename(), input.getAbsolutePath());
                File ouput = new File(input.getAbsolutePath() + File.separator + doc.getFilename());
                if (ouput.exists() && ouput.length() > 0) {
                    String status = "Normal";
                    if (!doc.getChecksum().equals(FileUtils.checksumSHA256(input.getAbsolutePath() + File.separator + doc.getFilename()))) {
                        status = "Modificado";
                    }
                    String time = DateFormat.getDateTimeInstance().format(new Date());
                    ListUtils.addReceive(doc.getUserFrom(), doc.getFilename(), time, status);
                    StartManager.windows.setContadorReceive(1);
                }
            } else {
                HTTPClientUtils.downloadFile(doc.getId(), doc.getFilename(), input.getAbsolutePath());
                File ouput = new File(input.getAbsolutePath() + File.separator + doc.getFilename());
                if (ouput.exists() && ouput.length() > 0) {
                    String status = "Normal";
                    if (!doc.getChecksum().equals(FileUtils.checksumSHA256(input.getAbsolutePath() + File.separator + doc.getFilename()))) {
                        status = "Modificado";
                    }
                    String time = DateFormat.getDateTimeInstance().format(new Date());
                    ListUtils.addReceive(doc.getUserFrom(), doc.getFilename(), time, status);
                    StartManager.windows.setContadorReceive(1);
                }
            }
        }
    }

    public static void upload(String protocol, String from, String to, String path) throws Exception {
        isUploaded = false;
        Documento doc = new Documento();
        doc.setProtocol(protocol);
        doc.setUserFrom(from);
        doc.setUserTo(to);
        doc.setChecksum(FileUtils.checksumSHA256(path));
        File file = new File(path);
        doc.setFilename(file.getName());
        doc.setId(UUID.randomUUID().toString().replace("-", ""));
        switch (protocol) {
            case "ftp":
                if (FTPClientUtils.isOpen()) {
                    if (file.length() > 1024 * 1024 * 5) {
                        try {
                            System.out.println("sleepn");
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            System.out.println(e);
                        }
                    }
                    boolean upladed = FTPClientUtils.uploadFile(path, doc.getId());
                    System.out.println("uploaded " + upladed);
                    FactoryMQ.postData(doc);
                    FactoryMQ.sendMessage(doc);
                    ListUtils.addSending(to, file.getName());
                    StartManager.windows.setContadorSend(1);
                    ClientManager.isUploaded = true;
                } else {
                    throw new IOException("No se pudo conectar con el servidor");
                }
                break;
            case "sftp":
                if (SFTPClientUtils.isOpen()) {
                    SFTPClientUtils.uploadFile(path, doc.getId());
                    FactoryMQ.postData(doc);
                    FactoryMQ.sendMessage(doc);
                    ListUtils.addSending(to, file.getName());
                    StartManager.windows.setContadorSend(1);
                    ClientManager.isUploaded = true;
                } else {
                    throw new IOException("No se pudo conectar con el servidor");
                }
                break;
            case "http":
                doc.setHasContent(Boolean.TRUE);
                HTTPClientUtils.uploadFile(doc, path);
                FactoryMQ.sendMessage(doc);
                ListUtils.addSending(to, file.getName());
                StartManager.windows.setContadorSend(1);
                ClientManager.isUploaded = true;
                break;
            case "optimizado":
                doc.setProtocol("ftp");
                SocketClientUtils.uploadFile(path, doc.getId());
                FactoryMQ.postData(doc);
                FactoryMQ.sendMessage(doc);
                ListUtils.addSending(to, file.getName());
                StartManager.windows.setContadorSend(1);
                ClientManager.isUploaded = true;
                break;
            default:
                break;
        }
    }

    public static void close() {
        FTPClientUtils.disconnect();
        SFTPClientUtils.disconnect();
        FactoryMQ.close();
    }
}
