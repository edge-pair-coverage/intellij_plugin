package org.juancatalan.edgepaircoverageplugin.dialogs;


import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SituacionesImposiblesSelectorDialog extends DialogWrapper {
    private final List<PsiMethod> methods;
    private final List<Integer> selectedNumbers = new ArrayList<>();
    private final List<JSpinner> spinners = new ArrayList<>();

    public SituacionesImposiblesSelectorDialog(List<PsiMethod> methods) {
        super(true); // use current window as parent
        this.methods = methods;
        setTitle("Seleccionar número de caminos imposibles");
        init(); // Initialize the dialog components
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(methods.size(), 2));

        for (PsiMethod method : methods) {
            JLabel methodLabel = new JLabel(method.getName() + getMethodParameters(method));
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            spinners.add(spinner);

            panel.add(methodLabel);
            panel.add(spinner);
        }

        return panel;
    }


    @Override
    protected void doOKAction() {
        selectedNumbers.clear();
        for (JSpinner spinner : spinners) {
            selectedNumbers.add((Integer) spinner.getValue());
        }
        super.doOKAction();
    }

    private String getMethodParameters(PsiMethod method) {
        return Stream.of(method.getParameterList().getParameters())
                .map(param -> param.getType().getPresentableText() + " " + param.getName())
                .collect(Collectors.joining(", ", "(", ")"));
    }

    public List<Integer> getSelectedNumbers() {
        return selectedNumbers;
    }
}

