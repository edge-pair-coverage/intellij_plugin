package org.juancatalan.edgepaircoverageplugin;

import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MyRunSettingsEditor extends SettingsEditor<MyRunConfiguration> {
    private JPanel myPanel;
    private LabeledComponent<JBTextField> myTextField;

    @Override
    protected void resetEditorFrom(@NotNull MyRunConfiguration s) {
        myTextField.getComponent().setText(s.getMyArgument());
    }

    @Override
    protected void applyEditorTo(@NotNull MyRunConfiguration s) {
        s.setMyArgument(myTextField.getComponent().getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        myPanel = new JPanel();
        myTextField = LabeledComponent.create(new JBTextField(), "Argument:");
        myPanel.add(myTextField);
        return myPanel;
    }
}

