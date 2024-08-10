package org.juancatalan.edgepaircoverageplugin.toolsWindows;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.jcef.JBCefBrowser;
import org.juancatalan.edgepaircoverageplugin.DTO.MethodReportDTO;
import org.juancatalan.edgepaircoverageplugin.DTO.parsers.MethodReportJsonParser;
import org.juancatalan.edgepaircoverageplugin.dialogs.MethodReportPanel;
import org.juancatalan.edgepaircoverageplugin.dialogs.ReportPanel;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class EdgePairCoverageReportJSONWindow {
    private JPanel contentPanel;

    public EdgePairCoverageReportJSONWindow(Project project, ToolWindow toolWindow) {
        fillUi(project);
    }

    private void fillUi(Project project) {
        // Obtén el directorio base del proyecto
        String basePath = project.getBasePath();
        if (basePath == null) {
            throw new IllegalStateException("El directorio base del proyecto no está disponible.");
        }

        // Construye la ruta completa al archivo JSON
        String filePath = basePath + "/coverageReport/report.json";

        try {
            java.util.List<MethodReportDTO> methodReportDTOList = MethodReportJsonParser.parseJSON(filePath);

            /*
            if (contentPanel == null) {
                contentPanel = new JPanel();
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            }

            // Elimina todos los componentes anteriores
            contentPanel.removeAll();

            for (MethodReportDTO methodReportDTO : methodReportDTOList) {
                contentPanel.add(new MethodReportPanel(methodReportDTO));
            }

             */
            contentPanel = new ReportPanel(project, methodReportDTOList);

            // Actualiza la interfaz de usuario
            contentPanel.revalidate();
            contentPanel.repaint();

            setupFileWatcher(project, filePath);
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
                    SwingUtilities.invokeLater(() -> fillUi(project));
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
