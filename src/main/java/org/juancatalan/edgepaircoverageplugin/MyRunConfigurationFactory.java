package org.juancatalan.edgepaircoverageplugin;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MyRunConfigurationFactory extends ConfigurationFactory {
    protected MyRunConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new MyRunConfiguration(project, this, "MyRunConfiguration");
    }

    @Override
    public @NotNull String getId() {
        return "MY_RUN_CONFIGURATION";
    }
}
