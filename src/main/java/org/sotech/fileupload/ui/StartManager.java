package org.sotech.fileupload.ui;

import java.awt.Color;
import javax.swing.UIManager;

public class StartManager {

    public static WelcomeWindow windows;

    public static void start() {
        UIManager.put("InternalFrame.activeTitleBackground", Color.RED);
        windows = new WelcomeWindow();
        windows.setVisible(true);
    }
}
