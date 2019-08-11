package org.sotech.fileupload.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.sotech.fileupload.bean.Usuario;
import org.sotech.fileupload.config.ApplicationConfig;
import org.sotech.fileupload.util.JsonDateDeserializer;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class DocumentoService {

    private static Gson mapper = null;

    static {
        mapper = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
    }

    public static Usuario autenticar(String email, String clave) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(ApplicationConfig.service_url + "login?email=" + email + "&clave=" + clave);
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception("Credenciales incorrectas");
        }
        return mapper.fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), Usuario.class);
    }

    public static Usuario registrar(String name, String email, String phone) throws Exception {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("id", email);
        data.put("phone", phone);
        StringEntity entity = new StringEntity(data.toString(), ContentType.APPLICATION_JSON);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(ApplicationConfig.service_url + "register");
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            System.out.println(response.getStatusLine());
            throw new Exception("Se ha producido un error en el registro de usuario, contactar con a soporte@ftransfer.com");
        }
        return mapper.fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), Usuario.class);
    }

    public static List<Usuario> listar_usuarios_conectados(String email) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(ApplicationConfig.service_url + "users_conected?email=" + email);
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception("Se ha producido un error en el registro de usuario, contactar con a soporte@fileupload.com");
        }
        Type listType = new TypeToken<List<Usuario>>() {
        }.getType();
        List<Usuario> tmp = mapper.fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), listType);
        List<Usuario> users = new ArrayList<Usuario>();
        for (Usuario usuario : tmp) {
            usuario.setSearch(getText(usuario));
            users.add(usuario);
            ApplicationConfig.users.put(usuario.getSearch(), usuario);
        }
        return users;
    }

    public static boolean validar_registro(String email, String sms) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(ApplicationConfig.service_url + "valid_register?email=" + email + "&sms=" + sms);
        HttpResponse response = httpClient.execute(request);
        return response.getStatusLine().getStatusCode() == 200;
    }

    public static String getText(Usuario usuario) {
        return usuario.getName() + "(" + usuario.getId() + ")(" + usuario.getOriginIp() + ")";
    }

}
