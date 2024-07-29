package org.juancatalan.edgepaircoverageplugin;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public class MyRunProfileState implements RunProfileState {
    private final ExecutionEnvironment environment;

    public MyRunProfileState(ExecutionEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner<?> runner) {
        Project project = environment.getProject();
        MyRunConfiguration configuration = (MyRunConfiguration) environment.getRunProfile();

        String myArgument = configuration.getMyArgument();

        // Aquí puedes usar el argumento para tu lógica de ejecución
        // Por ejemplo, podrías pasar este argumento a un proceso externo

        ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
        consoleView.print("Argument: " + myArgument, ConsoleViewContentType.NORMAL_OUTPUT);

        DefaultExecutionResult result = new DefaultExecutionResult(consoleView, new ProcessHandler() {
            @Override
            protected void destroyProcessImpl() {}

            @Override
            protected void detachProcessImpl() {}

            @Override
            public boolean detachIsDefault() {
                return false;
            }

            @Override
            public OutputStream getProcessInput() {
                return null;
            }
        });

        return result;
    }
}
