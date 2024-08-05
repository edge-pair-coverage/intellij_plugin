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
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class MyExecutionListener implements ExecutionListener {

    private final Project project;
    private static MessageBusConnection connection;

    public MyExecutionListener(Project project) {
        this.project = project;
    }

    @Override
    public void processTerminated(@NotNull String executorId, @NotNull ExecutionEnvironment environment, @NotNull ProcessHandler handler, int exitCode) {
        var runProfile = environment.getRunProfile();

        if (runProfile instanceof com.intellij.execution.configurations.RunConfiguration) {
            com.intellij.execution.configurations.RunConfiguration runConfiguration = (com.intellij.execution.configurations.RunConfiguration) runProfile;

            // Ejecutar la lógica de IU en el EDT
            ApplicationManager.getApplication().invokeLater(() -> {
                //Messages.showInfoMessage(project, "Ejecución terminada para: " + runConfiguration.getName(), "Información");
                ejecutarAccionPostEjecucion(runConfiguration, exitCode);
            });
        } else {
            // Ejecutar la lógica de IU en el EDT
            ApplicationManager.getApplication().invokeLater(() -> {
                // Messages.showInfoMessage(project, "Ejecución terminada para perfil no específico: " + runProfile.getName(), "Información");
            });
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
        openToolWindow(project);
    }

    private void openToolWindow(Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Edge Pair Coverage Report");
        if (toolWindow != null) {
            toolWindow.show();
        } else {
            System.out.println("ToolWindow no encontrado");
        }
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
