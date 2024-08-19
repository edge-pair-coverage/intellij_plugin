package org.juancatalan.edgepaircoverageplugin.ui.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nullable;
import org.juancatalan.edgepaircoverageplugin.ui.panels.SituacionesImposiblesSelectorPanel;
import org.juancatalan.edgepaircoverageplugin.ui.panels.MetodoSelectorPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SeleccionarMetodosWizardDialog extends DialogWrapper {
    private final String TITLE_METODO_SELECTOR_PANEL = "Select the methods to measure Edge-Pair coverage";
    private final String TITLE_SITUACIONES_IMPOSIBLES_SELECTOR_PANEL = "Indicate the number of impossible situations per method";
    private final Project project;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private MetodoSelectorPanel metodoSelectorPanel;
    private SituacionesImposiblesSelectorPanel situacionesImposiblesSelectorPanel;

    private final JButton backButton = new JButton("Previous");
    private final JButton nextButton = new JButton("Next");

    private JPanel currentPanel;

    public SeleccionarMetodosWizardDialog(@Nullable Project project) {
        super(true); // use current window as parent
        this.project = project;

        setTitle(TITLE_METODO_SELECTOR_PANEL);

        // Inicializar el primer panel
        metodoSelectorPanel = new MetodoSelectorPanel(project);
        situacionesImposiblesSelectorPanel = new SituacionesImposiblesSelectorPanel(new ArrayList<>());

        cardPanel.add(metodoSelectorPanel, "MetodoSelector");
        cardPanel.add(situacionesImposiblesSelectorPanel, "SituacionesImposiblesSelector");

        currentPanel = metodoSelectorPanel;

        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return cardPanel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        backButton.setEnabled(false); // Al iniciar, no se puede ir hacia atrás
        backButton.addActionListener(e -> {
            if (currentPanel == situacionesImposiblesSelectorPanel) {
                cardLayout.previous(cardPanel);
                currentPanel = metodoSelectorPanel;
                backButton.setEnabled(false);
                nextButton.setEnabled(true);
                nextButton.setText("Next");
                setTitle(TITLE_METODO_SELECTOR_PANEL);
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPanel == metodoSelectorPanel) {
                // Obtener los métodos seleccionados y actualizar el panel de situaciones imposibles
                List<PsiMethod> selectedMethods = metodoSelectorPanel.getSelectedMethodsList();
                if (selectedMethods.size() > 0){
                    situacionesImposiblesSelectorPanel.updateMethods(selectedMethods);
                    cardLayout.next(cardPanel);
                    currentPanel = situacionesImposiblesSelectorPanel;
                    backButton.setEnabled(true);
                    nextButton.setText("Run");
                    setTitle(TITLE_SITUACIONES_IMPOSIBLES_SELECTOR_PANEL);
                }
                else {
                    // Mostrar mensaje de advertencia si no se ha seleccionado ningún método
                    JOptionPane.showMessageDialog(this.cardPanel,
                            "You must select at least one method to continue.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else if (currentPanel == situacionesImposiblesSelectorPanel) {
                doOKAction(); // Finalizar el wizard
            }
        });

        panel.add(backButton);
        panel.add(nextButton);

        return panel;
    }

    public Map<PsiMethod, Integer> methodsSituacionesImposiblesMap(){
        return situacionesImposiblesSelectorPanel.getMetodosSeleccionadosConSituacionesImposibles();
    }
}



