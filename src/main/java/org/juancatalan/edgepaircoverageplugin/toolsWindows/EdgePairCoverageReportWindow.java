package org.juancatalan.edgepaircoverageplugin.toolsWindows;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;
import java.awt.*;

public class EdgePairCoverageReportWindow {
    private JPanel contentPanel;
    private final JBCefBrowser browser;

    public EdgePairCoverageReportWindow(Project project, ToolWindow toolWindow) {
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

        //setupFileWatcher(project, filePath);
    }

    private void setupFileWatcher(Project project, String filePath) {
        VirtualFileManager vfm = VirtualFileManager.getInstance();
        VirtualFile vf = vfm.findFileByUrl("file://" + filePath);

        if (vf == null) {
            throw new IllegalStateException("El archivo especificado no existe: " + filePath);
        }

        // Agrega un listener para observar los cambios
        VirtualFileListener listener = new VirtualFileListener() {
            @Override
            public void contentsChanged(VirtualFileEvent event) {
                if (event.getFile().equals(vf)) {
                    // Recarga el navegador cuando el archivo cambia
                    browser.loadURL("file://" + filePath);
                }
            }
        };

        // Registra el listener para el VirtualFile
        vf.getFileSystem().addVirtualFileListener(listener);

        // Asegúrate de eliminar el listener cuando ya no sea necesario
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            vf.getFileSystem().removeVirtualFileListener(listener);
        }));
    }

    public JPanel getContent() {
        return contentPanel;
    }
}
