package org.juancatalan.edgepaircoverageplugin.toolsWindows;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.ToolWindow;
import org.juancatalan.edgepaircoverageplugin.DTO.MethodReportDTO;
import org.juancatalan.edgepaircoverageplugin.DTO.parsers.MethodReportJsonParser;
import org.juancatalan.edgepaircoverageplugin.ui.panels.ReportPanel;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class EdgePairCoverageReportJSONWindow {
    private JPanel contentPanel;
    private JPanel mainPanel;

    public EdgePairCoverageReportJSONWindow(Project project, ToolWindow toolWindow) {
        mainPanel = new JPanel(new BorderLayout()); // Panel principal con BorderLayout

        // Obtén el directorio base del proyecto
        String basePath = project.getBasePath();
        if (basePath == null) {
            throw new IllegalStateException("El directorio base del proyecto no está disponible.");
        }

        // Construye la ruta completa al archivo JSON
        String filePath = basePath + "/coverageReport/report.json";
        fillUi(project);
        //setupFileWatcher(project, filePath);
    }

    public void fillUi(Project project) {
        // Obtén el directorio base del proyecto
        String basePath = project.getBasePath();
        if (basePath == null) {
            throw new IllegalStateException("El directorio base del proyecto no está disponible.");
        }

        // Construye la ruta completa al archivo JSON
        String filePath = basePath + "/coverageReport/report.json";

        try {
            List<MethodReportDTO> methodReportDTOList = MethodReportJsonParser.parseJSON(filePath);

            // Crea un nuevo ReportPanel
            JPanel newContentPanel = new ReportPanel(project, methodReportDTOList);

            // Remueve el panel viejo y añade el nuevo
            if (contentPanel != null) {
                mainPanel.remove(contentPanel);
            }

            contentPanel = newContentPanel;
            mainPanel.add(contentPanel, BorderLayout.CENTER);

            // Actualiza la interfaz de usuario
            mainPanel.revalidate();
            mainPanel.repaint();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                    System.out.println("El archivo ha cambiado: " + event.getFile().getPath());

                    // Asegúrate de refrescar el estado del archivo
                    VirtualFileManager.getInstance().asyncRefresh(() -> {
                        SwingUtilities.invokeLater(() -> fillUi(project));
                    });
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
        return mainPanel;
    }
}