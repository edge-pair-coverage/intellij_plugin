package org.juancatalan.edgepaircoverageplugin.toolsWindows;

import com.intellij.openapi.project.Project;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;
import java.awt.*;

public class MyToolWindow {
    private JPanel contentPanel;
    private final JBCefBrowser browser;

    public MyToolWindow(Project project, ToolWindow toolWindow) {
        // Obtén el directorio base del proyecto
        String basePath = project.getBasePath();
        if (basePath == null) {
            throw new IllegalStateException("El directorio base del proyecto no está disponible.");
        }

        // Construye la ruta completa al archivo HTML
        String filePath = basePath + "/coverageReport/report.html";

        // Inicializa el navegador con la ruta del archivo
        browser = new JBCefBrowser("file://" + filePath);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(browser.getComponent(), BorderLayout.CENTER);
    }

    public JPanel getContent() {
        return contentPanel;
    }
}
