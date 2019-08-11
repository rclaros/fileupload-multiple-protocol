package org.sotech.fileupload.config;

import org.sotech.fileupload.bean.Usuario;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class ApplicationConfig {

    public static String root = "C:";
    public static String protocol = null;
    /**
     * Server IP
     */
    public static final String server_ip = "your ip";
    /**
     * Servidor de Servicios de registro y autenticación de usuarios
     */
    public static String service_url = "http://" + server_ip + "/fileupload/api/user/";

    /**
     * Servidor de Colas para sincronización de mensajes
     */
    public static String param_queue_url = "tcp://"+server_ip+":61616";
    public static String param_queue_nombre = "APP.FTRANSFER";
    public static String param_queue_admin = "APP.FTRANSFER.ADMIN";
    /**
     * parametros de usuario y conectividad
     */
    //public static Boolean userConnectedReceive = Boolean.FALSE;
    public static Boolean status = Boolean.FALSE;

    /**
     * Objeto del usuario que ha iniciado sesión
     */
    public static Usuario me = null;
    /**
     * Objeto del usuario conectado
     */
    public static Usuario userConnect = null;
    /**
     * Lista de usuarios disponibles en el sistema
     */
    public static Map<String, Usuario> users = new HashMap<String, Usuario>();

    /**
     * Lista de parámetros de configuración
     */
    public static Map<String, String> params = new HashMap<>();
    /**
     * Lista de protocolos
     */
    public static Map<String, String> params_protocol = new HashMap<>();

    /**
     * Modelos para lista de documentos recibidos y modificados
     */
    public static DefaultTableModel model_sending = new DefaultTableModel();
    public static DefaultComboBoxModel<String> model_users = new DefaultComboBoxModel<>();

    public static Set<String> users_simple = new HashSet<>();

    static {
        params.put("server", server_ip);
        params.put("user", "your user");
        params.put("password", "your password");
        params_protocol.put("ftp", "21");
        params_protocol.put("sftp", "24");
        params_protocol.put("http", "8080");
        params_protocol.put("websocket", "8080");
        params_protocol.put("optimizado", "26");
        users_simple.add("Todos");
        ApplicationConfig.model_users.addElement("Todos");
    }

    public static DefaultTableModel model_receive = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            if (column == 4) {
                return JLabel.class;
            }
            return Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };
    public static DefaultTableModel model_editing = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            if (column == 4) {
                return JLabel.class;
            }
            return Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };

}
