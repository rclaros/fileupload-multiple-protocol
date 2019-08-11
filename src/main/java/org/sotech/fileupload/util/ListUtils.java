package org.sotech.fileupload.util;

import org.sotech.fileupload.config.ApplicationConfig;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ListUtils {

    public static void addUser(String email) {
        if (!ApplicationConfig.users_simple.contains(email)) {
            ApplicationConfig.users_simple.add(email);
            ApplicationConfig.model_users.addElement(email);
        }
    }

    public static void addReceive(String from, String filename, String date, String status) {
        ImageIcon icon = null;
        if (status.equals("Normal")) {
            icon = new ImageIcon(ListUtils.class.getResource("/org/sotech/fileupload/resources/icon_file.png"));
        } else {
            icon = new ImageIcon(ListUtils.class.getResource("/org/sotech/fileupload/resources/icon_edit.png"));
        }
        JLabel label = new JLabel();
        label.setText("<html><FONT COLOR=GREEN>Ver documento</FONT></html>");
        label.setIcon(icon);
        label.setOpaque(true);
        ApplicationConfig.model_receive.addRow(new Object[]{from, filename, date, status, label});
        if (!status.equals("Normal")) {
            ApplicationConfig.model_editing.addRow(new Object[]{from, filename, date, status, label});
        }
    }

    public static void addSending(String to, String filename) {
        ApplicationConfig.model_sending.addRow(new Object[]{to, filename, DateFormat.getDateTimeInstance().format(new Date())});
    }
}
