package org.juancatalan.edgepaircoverageplugin;

import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

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

                ProgramRunnerUtil.executeConfiguration(project, runManager.getSelectedConfiguration(), DefaultRunExecutor.getRunExecutorInstance());
        }
    }
}
