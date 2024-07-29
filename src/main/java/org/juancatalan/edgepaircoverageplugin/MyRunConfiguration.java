package org.juancatalan.edgepaircoverageplugin;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyRunConfiguration extends RunConfigurationBase {
    private String myArgument;

    protected MyRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        myArgument = "";
    }

    public String getMyArgument() {
        return myArgument;
    }

    public void setMyArgument(String myArgument) {
        this.myArgument = myArgument;
    }

    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MyRunSettingsEditor();
    }

    @Override
    public void checkConfiguration() {
        // Validate the configuration settings
    }

    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        return new MyRunProfileState(environment);
    }
}

