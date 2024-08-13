package org.juancatalan.edgepaircoverageplugin.settings.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBComboBoxLabel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;
import org.juancatalan.edgepaircoverageplugin.settings.AppSettings;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final ComboBox myReportType = new ComboBox(AppSettings.reportType.values());
    private final JBCheckBox myBooleanAssignmentAsPredicateNode = new JBCheckBox("Asignaciones booleanas como nodos predicado");

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Tipo de informe"), myReportType, 1, false)
                .addComponent(myBooleanAssignmentAsPredicateNode, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myReportType;
    }

    @NotNull
    public AppSettings.reportType getMyReportType() {
        return (AppSettings.reportType) myReportType.getSelectedItem();
    }

    public void setMyReportType(@NotNull AppSettings.reportType newText) {
        myReportType.setSelectedItem(newText);
    }

    public boolean getBooleanAssignmentAsPredicateNode() {
        return myBooleanAssignmentAsPredicateNode.isSelected();
    }

    public void setBooleanAssignmentAsPredicateNode(boolean newStatus) {
        myBooleanAssignmentAsPredicateNode.setSelected(newStatus);
    }
}
