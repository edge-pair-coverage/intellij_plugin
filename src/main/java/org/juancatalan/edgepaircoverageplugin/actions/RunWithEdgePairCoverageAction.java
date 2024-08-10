package org.juancatalan.edgepaircoverageplugin.actions;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.juancatalan.edgepaircoverageplugin.MyExecutionListener;
import org.juancatalan.edgepaircoverageplugin.settings.AppSettings;
import org.juancatalan.edgepaircoverageplugin.ui.dialogs.SeleccionarMetodosWizardDialog;
import org.juancatalan.edgepaircoverageplugin.utils.PsiMethodToFullMethodName;

import java.io.*;
import java.util.Map;

public class RunWithEdgePairCoverageAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e){
        Project project = e.getProject();
        if (project == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }

        RunManager runManager = RunManager.getInstance(project);
        RunnerAndConfigurationSettings settings = runManager.getSelectedConfiguration();

        if (settings != null) {
            RunConfiguration configuration = settings.getConfiguration();
            // Aquí configuramos el texto de la acción con el nombre de la configuración
            String originalText = e.getPresentation().getText();
            e.getPresentation().setText(originalText.replaceAll("\\$", configuration.getName()));
            e.getPresentation().setEnabledAndVisible(true);
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }

    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            MyExecutionListener.registerListener(project);
            // Mostrar diálogo para selección de métodos
            SeleccionarMetodosWizardDialog dialog = new SeleccionarMetodosWizardDialog(project);
            //MetodoSelectorDialog dialog = new MetodoSelectorDialog(project);
            if (dialog.showAndGet()) {
                System.out.println(dialog.methodsSituacionesImposiblesMap());
                System.out.println(dialog.methodsSituacionesImposiblesMap().keySet().stream()
                        .map(m -> PsiMethodToFullMethodName.transform(m)).toList());
                // Obtener métodos seleccionados y ejecutar con -javaagent
                ejecutarConJavaAgent(project, dialog.methodsSituacionesImposiblesMap());
            }
        }
    }

    private void ejecutarConJavaAgent(Project project, Map<PsiMethod, Integer> metodosSeleccionados) {
        // Obtener la configuración de ejecución actual
        RunManager runManager = RunManager.getInstance(project);
        RunnerAndConfigurationSettings currentSettings = runManager.getSelectedConfiguration();

        if (currentSettings != null) {
            RunnerAndConfigurationSettings tempSettings = runManager.createConfiguration(currentSettings.getConfiguration().clone(), currentSettings.getFactory());
            RunConfiguration configuration = tempSettings.getConfiguration();

            // Verificar si la configuración permite modificar VM options
            if (configuration instanceof RunProfile) {
                String currentVmOptions = getVmOptions(configuration);
                // Obtener el Path del agente
                InputStream inputStreamAgente = getClass().getClassLoader().getResourceAsStream("agent.jar");
                // Convierte la URL en un archivo
                File tempFile = null;
                try {
                    tempFile = File.createTempFile("agent", ".jar");
                    tempFile.deleteOnExit(); // Asegura que el archivo temporal se elimine al salir
                    // Copia el recurso al archivo temporal
                    OutputStream out = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStreamAgente.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Obtén la ruta del archivo temporal
                //String pathAgente = tempFile.getAbsolutePath();
                String pathAgente = "/home/juan/.m2/repository/org/juancatalan/edgepaircoverage/0.9-SNAPSHOT/edgepaircoverage-0.9-SNAPSHOT.jar"; // Ruta al agente

                AppSettings.State appSettings = ApplicationManager.getApplication().getService(AppSettings.class).getState();

                String javaAgentParameter = "-javaagent:".concat(pathAgente); // Ruta al agente

                StringBuilder stringBuilder = new StringBuilder();
                metodosSeleccionados.forEach((k, v) -> stringBuilder.append(PsiMethodToFullMethodName.transform(k)+":"+v+";"));
                javaAgentParameter = javaAgentParameter.concat("=methods={"+stringBuilder.toString()+"}");

                if (currentVmOptions == null || !currentVmOptions.contains(javaAgentParameter)) {
                    setVmOptions(configuration, (currentVmOptions != null ? currentVmOptions + " " : "") + javaAgentParameter);
                }

                // Crear el entorno de ejecución
                ExecutionEnvironmentBuilder builder = null;
                try {
                    builder = ExecutionEnvironmentBuilder.create(
                            DefaultRunExecutor.getRunExecutorInstance(), tempSettings);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                ExecutionEnvironment environment = builder.build();

                // Ejecutar la configuración modificada
                ProgramRunner<?> runner = ProgramRunner.getRunner(DefaultRunExecutor.EXECUTOR_ID, configuration);
                if (runner != null) {
                    try {
                        runner.execute(environment);
                        //Messages.showInfoMessage("Ejecución iniciada con -javaagent", "Información");
                    } catch (Exception ex) {
                        Messages.showErrorDialog("Error al iniciar la ejecución: " + ex.getMessage(), "Error");
                    }
                } else {
                    Messages.showErrorDialog("No se pudo encontrar un runner para ejecutar la configuración.", "Error");
                }
            } else {
                Messages.showErrorDialog("La configuración seleccionada no permite modificar los parámetros de VM.", "Error");
            }
        } else {
            Messages.showErrorDialog("No hay una configuración de ejecución seleccionada.", "Error");
        }
    }

    private String getVmOptions(RunConfiguration configuration) {
        try {
            // Usar reflexión para obtener los VM options si es posible
            return (String) configuration.getClass().getMethod("getVMParameters").invoke(configuration);
        } catch (Exception e) {
            return null; // No se pueden obtener los VM options
        }
    }

    private void setVmOptions(RunConfiguration configuration, String vmOptions) {
        try {
            // Usar reflexión para establecer los VM options si es posible
            configuration.getClass().getMethod("setVMParameters", String.class).invoke(configuration, vmOptions);
        } catch (Exception e) {
            // Manejar errores al intentar establecer los VM options
            Messages.showErrorDialog("No se pueden modificar los VM options para esta configuración.", "Error");
        }
    }
}
