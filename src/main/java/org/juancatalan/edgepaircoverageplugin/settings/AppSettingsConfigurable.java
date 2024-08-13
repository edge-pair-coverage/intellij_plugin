package org.juancatalan.edgepaircoverageplugin.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.juancatalan.edgepaircoverageplugin.settings.ui.AppSettingsComponent;

import javax.swing.*;
import java.util.Objects;

/**
 * Provides controller functionality for application settings.
 */
final class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because
    // this implementation is registered as an applicationConfigurable

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Edge-Pair Coverage";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        return !mySettingsComponent.getMyReportType().equals(state.reportType) ||
                mySettingsComponent.getBooleanAssignmentAsPredicateNode() != state.booleanAssignmentsAsPredicateNode;
    }

    @Override
    public void apply() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        state.reportType = mySettingsComponent.getMyReportType();
        state.booleanAssignmentsAsPredicateNode = mySettingsComponent.getBooleanAssignmentAsPredicateNode();
    }

    @Override
    public void reset() {
        AppSettings.State state =
                Objects.requireNonNull(AppSettings.getInstance().getState());
        mySettingsComponent.setMyReportType(state.reportType);
        mySettingsComponent.setBooleanAssignmentAsPredicateNode(state.booleanAssignmentsAsPredicateNode);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}
