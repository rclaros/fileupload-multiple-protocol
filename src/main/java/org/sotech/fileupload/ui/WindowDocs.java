package org.sotech.fileupload.ui;

import org.sotech.fileupload.client.HTTPClientUtils;
import org.sotech.fileupload.config.ApplicationConfig;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class WindowDocs extends javax.swing.JDialog {

    String[] columnRecive = {"Remitente", "Documento", "Fecha", "Integridad", "Ver"};
    String[] columnEditing = {"Remitente", "Documento", "Fecha", "Integridad", "Ver"};
    String[] columnSending = {"Destinatario", "Documento", "Fecha"};

    TableRowSorter<TableModel> sorterReceive = null;
    TableRowSorter<TableModel> sorterEditing = null;

    /**
     * Creates new form WindowDocs
     */
    public WindowDocs(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        select_users.setModel(ApplicationConfig.model_users);
        /**
         * Config
         */
        jtable_receive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JTable target = (JTable) e.getSource();
                    if (target.getSelectedColumn() == 4) {
                        String from = ApplicationConfig.model_receive.getValueAt(target.getSelectedRow(), 0).toString();
                        String filename = ApplicationConfig.model_receive.getValueAt(target.getSelectedRow(), 1).toString();
                        String openFilename = HTTPClientUtils.getPathUser() + File.separator + from + File.separator + filename;
                        try {
                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + openFilename);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                    }
                }
            }
        });
        jtable_editing.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JTable target = (JTable) e.getSource();
                    if (target.getSelectedColumn() == 4) {
                        String from = ApplicationConfig.model_receive.getValueAt(target.getSelectedRow(), 0).toString();
                        String filename = ApplicationConfig.model_receive.getValueAt(target.getSelectedRow(), 1).toString();
                        String openFilename = HTTPClientUtils.getPathUser() + File.separator + from + File.separator + filename;
                        try {
                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + openFilename);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                    }
                }
            }
        });
        jtable_receive.setModel(ApplicationConfig.model_receive);
        jtable_receive.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtable_receive.setFillsViewportHeight(true);
        jtable_receive.setDefaultRenderer(JLabel.class, new RendererReceive());
        ApplicationConfig.model_receive.setColumnIdentifiers(columnRecive);
        sorterReceive = new TableRowSorter<TableModel>(ApplicationConfig.model_receive);
        jtable_receive.setRowSorter(sorterReceive);
        jtable_receive.setRowHeight(30);

        jtable_editing.setModel(ApplicationConfig.model_editing);
        jtable_editing.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtable_editing.setFillsViewportHeight(true);
        jtable_editing.setDefaultRenderer(JLabel.class, new RendererReceive());
        ApplicationConfig.model_editing.setColumnIdentifiers(columnEditing);
        sorterEditing = new TableRowSorter<TableModel>(ApplicationConfig.model_editing);
        jtable_editing.setRowSorter(sorterEditing);
        jtable_editing.setRowHeight(30);

        jtable_sending.setModel(ApplicationConfig.model_sending);
        jtable_sending.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jtable_sending.setFillsViewportHeight(true);
        ApplicationConfig.model_sending.setColumnIdentifiers(columnSending);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtable_receive = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtable_editing = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtable_sending = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        select_users = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FTransfer - Mis Documentos");

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mis Documentos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtable_receive.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Remitente", "Documento", "Fecha", "Integridad", "Ver"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jtable_receive);

        jTabbedPane1.addTab("Documentos recibidos", jScrollPane2);

        jtable_editing.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Remitente", "Documento", "Fecha", "Integridad", "Ver"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtable_editing);

        jTabbedPane1.addTab("Documentos manipulados", jScrollPane1);

        jtable_sending.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Destinatario", "Documento", "Fecha", "Estado"
            }
        ));
        jScrollPane3.setViewportView(jtable_sending);

        jTabbedPane1.addTab("Documentos enviados", jScrollPane3);

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sotech/fileupload/resources/folder.png"))); // NOI18N
        jButton1.setText("Abrir carpeta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        select_users.setBackground(new java.awt.Color(204, 204, 204));
        select_users.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "email@gmail.com" }));
        select_users.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                select_usersItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Filtro Remitente/Destinatario");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(720, 720, 720))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(select_users, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(select_users, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String FolderName = HTTPClientUtils.getPathUser();//Write your complete path here
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + FolderName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void select_usersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_select_usersItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Object item = evt.getItem();
            System.out.println(item);
            if (item.toString().equals("Todos")) {
                sorterReceive.setRowFilter(RowFilter.regexFilter("", 0));
                sorterEditing.setRowFilter(RowFilter.regexFilter("", 0));
            } else {
                sorterReceive.setRowFilter(RowFilter.regexFilter(item.toString(), 0));
                sorterEditing.setRowFilter(RowFilter.regexFilter(item.toString(), 0));
            }
        }
    }//GEN-LAST:event_select_usersItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(WindowDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(WindowDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(WindowDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(WindowDocs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WindowDocs dialog = new WindowDocs(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jtable_editing;
    private javax.swing.JTable jtable_receive;
    private javax.swing.JTable jtable_sending;
    private javax.swing.JComboBox<String> select_users;
    // End of variables declaration//GEN-END:variables

    public class RendererReceive extends DefaultTableCellRenderer {

        public void fillColor(JTable t, JLabel l, boolean isSelected) {
            if (isSelected) {
                l.setBackground(t.getSelectionBackground());
                l.setForeground(t.getSelectionForeground());
            } else {
                l.setBackground(t.getBackground());
                l.setForeground(t.getForeground());
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                JLabel label = (JLabel) value;
                label.setOpaque(true);
                fillColor(table, label, isSelected);
                return label;
            } else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }
}
