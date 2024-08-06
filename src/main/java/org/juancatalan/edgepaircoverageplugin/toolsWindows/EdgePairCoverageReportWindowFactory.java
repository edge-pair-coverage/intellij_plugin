package org.juancatalan.edgepaircoverageplugin.toolsWindows;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class EdgePairCoverageReportWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // Inicializa el contenido del ToolWindow
        EdgePairCoverageReportWindow edgePairCoverageReportWindow = new EdgePairCoverageReportWindow(project, toolWindow);

        // Usa el nuevo método para obtener ContentFactory
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(edgePairCoverageReportWindow.getContent(), "", false);

        toolWindow.getContentManager().addContent(content);
    }
}