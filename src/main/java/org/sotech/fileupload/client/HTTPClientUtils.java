package org.sotech.fileupload.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.sotech.fileupload.bean.Documento;
import org.sotech.fileupload.config.ApplicationConfig;
import org.sotech.fileupload.util.JsonDateDeserializer;
import org.sotech.fileupload.util.ListUtils;
import org.sotech.fileupload.ui.StartManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

public class HTTPClientUtils {

    private static Gson mapper = null;
    private static HttpClient webResource = null;

    static {
        webResource = getHttpClient();
        mapper = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    public static synchronized void uploadFile(Documento doc, String path) throws Exception {
        try {
            System.out.println("post data");
            HttpPost httpPost = new HttpPost(ApplicationConfig.service_url + "upload");
            MultipartEntity formEntity = new MultipartEntity();
            if (doc.getHasContent() != null && doc.getHasContent().equals(Boolean.TRUE)) {
                File file = new File(path);
                ContentBody body = new InputStreamBody(new FileInputStream(file), file.getName());
                formEntity.addPart("file", body);
            }
            formEntity.addPart("filename", new StringBody(doc.getFilename()));
            formEntity.addPart("from", new StringBody(doc.getUserFrom()));
            formEntity.addPart("to", new StringBody(doc.getUserTo()));
            formEntity.addPart("id", new StringBody(doc.getId()));
            formEntity.addPart("protocol", new StringBody(doc.getProtocol()));
            formEntity.addPart("checksum", new StringBody(doc.getChecksum()));
            httpPost.setEntity(formEntity);
            HttpResponse response = webResource.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("No se pudo enviar el archivo");
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    public static void listFile(String email) {
        try {
            HttpGet request = new HttpGet(ApplicationConfig.service_url + "documents?email=" + email);
            HttpResponse response = webResource.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("Se ha producido un error en el registro de usuario, contactar con a soporte@ftransfer.com");
            }
            Type listType = new TypeToken<List<Documento>>() {
            }.getType();
            List<Documento> docs = mapper.fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), listType);
            for (Documento doc : docs) {
                try {
                    File folder = new File(getPathUser());
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    folder = new File(getPathConected(doc.getUserFrom()));
                    if (!folder.exists()) {
                        folder.mkdir();
                    }
                    InputStream is = download(doc.getId());
                    File file = new File(getPathConected(doc.getUserFrom()) + File.separator + doc.getFilename());
                    FileOutputStream fos = new FileOutputStream(file);
                    IOUtils.copy(is, fos);
                    fos.flush();
                    fos.close();
                    IOUtils.closeQuietly(is);
                    StartManager.windows.setContadorReceive(1);
                    ListUtils.addUser(doc.getUserFrom());
                    String time = DateFormat.getDateTimeInstance().format(doc.getCreated());
                    ListUtils.addReceive(doc.getUserFrom(), doc.getFilename(), time, "Normal");
                } catch (Exception e) {
                    System.out.println("Error : " + e.getMessage());
                }
            }
        } catch (Exception e) {
        }
    }

    public static List<Documento> getFilesPending(String id) {
        List<Documento> list = new ArrayList<>();
        try {
            HttpGet request = new HttpGet(ApplicationConfig.service_url + "documents?email=" + id);
            HttpResponse response = webResource.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("Se ha producido un error en el registro de usuario, contactar con a soporte@fileupload.com");
            }
            Type listType = new TypeToken<List<Documento>>() {
            }.getType();
            list = mapper.fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), listType);
        } catch (Exception e) {
            System.out.println("getFilesPending " + e.getMessage());
        }
        return list;
    }

    private static InputStream download(String id) throws Exception {
        try {
            HttpGet getRequest = new HttpGet(ApplicationConfig.service_url + "download?id=" + id);
            HttpResponse response = webResource.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("No se pudo descargar");
            }
            return response.getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        } catch (IllegalStateException e) {
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static void downloadFile(String id, String filename, String root) {
        try {
            HttpGet getRequest = new HttpGet(ApplicationConfig.service_url + "download?id=" + id);
            HttpResponse response = webResource.execute(getRequest);
            InputStream is = response.getEntity().getContent();
            File file = new File(root + File.separator + filename);
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(is, fos);
            fos.flush();
            fos.close();
            IOUtils.closeQuietly(is);
        } catch (UnsupportedEncodingException e) {

        } catch (IllegalStateException e) {

        } catch (IOException e) {

        }
    }

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    public enum SSLCertificateTrustLevel {

        DEFAULT, SELFSIGNED, ANYCERTIFICATE
    }

    public static CloseableHttpClient getHttpClient(SSLCertificateTrustLevel trustLevel) {
        try {
            if (trustLevel == null) {
                return getHttpClient();
            }
            CloseableHttpClient client = null;
            SSLContextBuilder builder = null;
            SSLConnectionSocketFactory sslsf = null;
            switch (trustLevel) {
                case ANYCERTIFICATE:
                    builder = new SSLContextBuilder();
                    builder.loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            return true;
                        }
                    });
                    sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
                    break;
                case SELFSIGNED:
                    builder = new SSLContextBuilder();
                    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                    sslsf = new SSLConnectionSocketFactory(builder.build());
                    client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
                    break;
                case DEFAULT:
                    client = getHttpClient();
                    break;
            }
            return client;
        } catch (KeyManagementException e) {
        } catch (KeyStoreException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String getPathUser() {
        return ApplicationConfig.root + File.separator + ApplicationConfig.me.getId().toLowerCase();
    }

    public static String getPathConected(String from) {
        return ApplicationConfig.root + File.separator + ApplicationConfig.me.getId().toLowerCase() + File.separator + from.toLowerCase();
    }

}
