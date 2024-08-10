package org.juancatalan.edgepaircoverageplugin.settings;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

@State(
        name = "org.juancatalan.edgepaircoverageplugin.settings.AppSettings",
        storages = @Storage("EdgePairCoverageSettingsPlugin.xml")
)
final public class AppSettings
        implements PersistentStateComponent<AppSettings.State> {

    static public class State {
        @NonNls
        public String userId = "John Smith";
        public boolean ideaStatus = false;
    }

    private State myState = new State();

    static AppSettings getInstance() {
        return ApplicationManager.getApplication()
                .getService(AppSettings.class);
    }

    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }
}
