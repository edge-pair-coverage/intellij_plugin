package org.juancatalan.edgepaircoverageplugin;

import com.intellij.execution.ExecutionListener;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.ExecutionTarget;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.*;
import com.intellij.util.messages.MessageBusConnection;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.juancatalan.edgepaircoverageplugin.settings.AppSettings;
import org.juancatalan.edgepaircoverageplugin.toolsWindows.EdgePairCoverageReportJSONWindow;
import org.juancatalan.edgepaircoverageplugin.toolsWindows.EdgePairCoverageReportJSONWindowFactory;
import org.juancatalan.edgepaircoverageplugin.toolsWindows.EdgePairCoverageReportWindowFactory;

import javax.swing.*;

public class MyExecutionListener implements ExecutionListener {

    private final Project project;
    private static MessageBusConnection connection;

    public MyExecutionListener(Project project) {
        this.project = project;
    }

    @Override
    public void processTerminated(@NotNull String executorId, @NotNull ExecutionEnvironment environment, @NotNull ProcessHandler handler, int exitCode) {
        var runProfile = environment.getRunProfile();

        if (runProfile instanceof RunConfiguration runConfiguration) {

            // Ejecutar la lógica de IU en el EDT
            //ApplicationManager.getApplication().invokeLater(() -> {
                //Messages.showInfoMessage(project, "Ejecución terminada para: " + runConfiguration.getName(), "Información");
                ejecutarAccionPostEjecucion(runConfiguration, exitCode);
            //});
        }
        MyExecutionListener.unregisterListener();
    }

    private void ejecutarAccionPostEjecucion(com.intellij.execution.configurations.RunConfiguration runConfiguration, int exitCode) {
        // Esta lógica también se ejecuta en el EDT
        if (exitCode == 0) {
            // Messages.showInfoMessage(project, "La ejecución fue exitosa para: " + runConfiguration.getName(), "Resultado de Ejecución");
        } else {
            // Messages.showErrorDialog(project, "La ejecución falló para: " + runConfiguration.getName() + " con código de salida: " + exitCode, "Resultado de Ejecución");
        }
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        toolWindowManager.invokeLater(() -> {
            if (toolWindowManager.getToolWindow("Edge Pair Coverage Report") != null){
                ToolWindow toolWindow = toolWindowManager.getToolWindow("Edge Pair Coverage Report");
                assert toolWindow != null;
                toolWindow.getContentManager().removeAllContents(true);
                Icon icon = IconLoader.getIcon("/icons/coverageReport.svg", getClass());
                toolWindow.setIcon(icon);
                AppSettings.State appSettings = AppSettings.getInstance().getState();
                assert appSettings != null;
                if (appSettings.reportType.equals(AppSettings.reportType.HTML)){
                    EdgePairCoverageReportWindowFactory factory = new EdgePairCoverageReportWindowFactory();
                    factory.createToolWindowContent(project, toolWindow);
                }
                else {
                    EdgePairCoverageReportJSONWindowFactory factory = new EdgePairCoverageReportJSONWindowFactory();
                    factory.createToolWindowContent(project, toolWindow);
                }
            }
            else {
                AppSettings.State appSettings = AppSettings.getInstance().getState();
                if (appSettings.reportType.equals(AppSettings.reportType.HTML)) {
                    toolWindowManager.registerToolWindow("Edge Pair Coverage Report", new Function1<RegisterToolWindowTaskBuilder, Unit>() {
                        @Override
                        public Unit invoke(RegisterToolWindowTaskBuilder registerToolWindowTaskBuilder) {
                            registerToolWindowTaskBuilder.contentFactory = new EdgePairCoverageReportWindowFactory();
                            registerToolWindowTaskBuilder.icon = IconLoader.getIcon("/icons/coverageReport.svg", getClass());
                            registerToolWindowTaskBuilder.anchor = ToolWindowAnchor.RIGHT;
                            return null;
                        }
                    });
                }
                else {
                    toolWindowManager.registerToolWindow("Edge Pair Coverage Report", new Function1<RegisterToolWindowTaskBuilder, Unit>() {
                        @Override
                        public Unit invoke(RegisterToolWindowTaskBuilder registerToolWindowTaskBuilder) {
                            registerToolWindowTaskBuilder.contentFactory = new EdgePairCoverageReportJSONWindowFactory();
                            registerToolWindowTaskBuilder.icon = IconLoader.getIcon("/icons/coverageReport.svg", getClass());
                            registerToolWindowTaskBuilder.anchor = ToolWindowAnchor.RIGHT;
                            return null;
                        }
                    });
                }
            }

        });
        openToolWindow(project);
    }

    private void openToolWindow(Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

        toolWindowManager.invokeLater(() -> {
            ToolWindow toolWindow = toolWindowManager.getToolWindow("Edge Pair Coverage Report");
            if (toolWindow != null) {
                toolWindow.show();
            } else {
                System.out.println("ToolWindow no encontrado");
            }
        });
    }

    // Método para registrar el listener en el proyecto
    public static void registerListener(@NotNull Project project) {
        connection = project.getMessageBus().connect();
        connection.subscribe(ExecutionManager.EXECUTION_TOPIC, new MyExecutionListener(project));
    }

    // Método para registrar el listener en el proyecto
    public static void unregisterListener() {
        connection.disconnect();
    }
}
