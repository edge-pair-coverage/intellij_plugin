package org.juancatalan.edgepaircoverageplugin;

import com.intellij.execution.*;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.JavaRunConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.execution.configuration.RunConfigurationExtensionBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RunWithEdgePairCoverageAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            // Encuentra la configuración de ejecución
            RunManager runManager = RunManager.getInstance(project);
            RunConfiguration runConfiguration = runManager.getSelectedConfiguration().getConfiguration();

            System.out.println(runConfiguration.getBeforeRunTasks());
            // Verifica si es una instancia de MyRunConfiguration
            System.out.println("Click");

            /*
            if (runConfiguration instanceof MyRunConfiguration) {
                System.out.println("Instancia propia");
                MyRunConfiguration myRunConfiguration = (MyRunConfiguration) runConfiguration;

                // Establece un argumento personalizado
                myRunConfiguration.setMyArgument("customArgument");

                // Ejecuta la configuración
                ProgramRunnerUtil.executeConfiguration(project, runManager.getSelectedConfiguration(), DefaultRunExecutor.getRunExecutorInstance());
            }

             */

            ejecutarConJavaAgent(project, new ArrayList<>(){});

            // ProgramRunnerUtil.executeConfiguration(project, runManager.getSelectedConfiguration(), DefaultRunExecutor.getRunExecutorInstance());
        }
    }

    private void ejecutarConJavaAgent(Project project, List<String> metodosSeleccionados) {
        // Obtener la configuración de ejecución actual
        RunManager runManager = RunManager.getInstance(project);
        RunnerAndConfigurationSettings currentSettings = runManager.getSelectedConfiguration();

        if (currentSettings != null) {
            RunConfiguration configuration = currentSettings.getConfiguration();

            // Modificar las opciones de VM para añadir -javaagent
            if (configuration instanceof JUnitConfiguration) {
                JUnitConfiguration junitConfig = (JUnitConfiguration) configuration;

                String currentVmOptions = junitConfig.getVMParameters();
                String javaAgentParameter = "-javaagent:/ruta/al/agente.jar"; // Ruta al agente

                if (currentVmOptions == null || !currentVmOptions.contains(javaAgentParameter)) {
                    junitConfig.setVMParameters((currentVmOptions != null ? currentVmOptions + " " : "") + javaAgentParameter);
                }

                // Opcional: añadir argumentos para los métodos seleccionados
                // junitConfig.setProgramParameters(String.join(" ", metodosSeleccionados));

                // Crear el entorno de ejecución
                ExecutionEnvironmentBuilder builder = null;
                try {
                    builder = ExecutionEnvironmentBuilder.create(
                            DefaultRunExecutor.getRunExecutorInstance(), currentSettings);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                ExecutionEnvironment environment = builder.build();

                // Ejecutar la configuración modificada
                ProgramRunner<?> runner = ProgramRunner.getRunner(DefaultRunExecutor.EXECUTOR_ID, junitConfig);
                if (runner != null) {
                    try {
                        runner.execute(environment);
                        Messages.showInfoMessage("Ejecución de pruebas JUnit con -javaagent iniciada", "Información");
                    } catch (Exception ex) {
                        Messages.showErrorDialog("Error al iniciar la ejecución: " + ex.getMessage(), "Error");
                    }
                } else {
                    Messages.showErrorDialog("No se pudo encontrar un runner para ejecutar la configuración.", "Error");
                }
            } else {
                Messages.showErrorDialog("La configuración seleccionada no es una configuración de prueba JUnit.", "Error");
            }
        } else {
            Messages.showErrorDialog("No hay una configuración de ejecución seleccionada.", "Error");
        }
    }
 /*
    private void ejecutarConJavaAgent(Project project, List<String> metodosSeleccionados) {
        // Obtener la configuración de ejecución actual
        RunManager runManager = RunManager.getInstance(project);
        RunnerAndConfigurationSettings currentSettings = runManager.getSelectedConfiguration();

        if (currentSettings != null) {
            RunConfiguration configuration = currentSettings.getConfiguration();

            if (configuration instanceof )

            if (configuration instanceof ApplicationConfiguration){
                Messages.showErrorDialog("Va, ejecutado", "Ejecutado");
            }

            // Modificar las opciones de VM para añadir -javaagent
            /*
            if (configuration instanceof JavaRunConfigurationBase) {
                JavaRunConfigurationBase javaConfig = (JavaRunConfigurationBase) configuration;

                String currentVmOptions = javaConfig.getVMParameters();
                // String javaAgentParameter = "-javaagent:/ruta/al/agente.jar"; // Ruta al agente
                String javaAgentParameter = ""; // Ruta al agente

                if (currentVmOptions == null || !currentVmOptions.contains(javaAgentParameter)) {
                    javaConfig.setVMParameters((currentVmOptions != null ? currentVmOptions + " " : "") + javaAgentParameter);
                }

                // Opcional: añadir argumentos para los métodos seleccionados
                // javaConfig.setProgramParameters(String.join(" ", metodosSeleccionados));

                // Crear el entorno de ejecución
                ExecutionEnvironmentBuilder builder = null;
                try {
                    builder = ExecutionEnvironmentBuilder.create(
                            DefaultRunExecutor.getRunExecutorInstance(), currentSettings);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                ExecutionEnvironment environment = builder.build();

                // Ejecutar la configuración modificada
                ProgramRunner<?> runner = ProgramRunner.getRunner(DefaultRunExecutor.EXECUTOR_ID, javaConfig);
                if (runner != null) {
                    try {
                        runner.execute(environment);
                        Messages.showInfoMessage("Ejecución con -javaagent iniciada", "Información");
                    } catch (Exception ex) {
                        Messages.showErrorDialog("Error al iniciar la ejecución: " + ex.getMessage(), "Error");
                    }
                } else {
                    Messages.showErrorDialog("No se pudo encontrar un runner para ejecutar la configuración.", "Error");
                }
            } else {
                Messages.showErrorDialog("La configuración seleccionada no es una configuración de Java.", "Error");
            }
        } else {
            Messages.showErrorDialog("No hay una configuración de ejecución seleccionada.", "Error");
        }
    }*/
}
