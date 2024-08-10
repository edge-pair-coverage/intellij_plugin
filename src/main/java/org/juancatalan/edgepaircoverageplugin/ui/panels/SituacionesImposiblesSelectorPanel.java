package org.juancatalan.edgepaircoverageplugin.ui.panels;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiMethod;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SituacionesImposiblesSelectorPanel extends JPanel {
    private final List<PsiMethod> methods;
    private final List<JSpinner> spinners = new ArrayList<>();

    public SituacionesImposiblesSelectorPanel(List<PsiMethod> methods) {
        this.methods = methods;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (PsiMethod method : methods) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));

            JLabel methodLabel = new JLabel(method.getName() + getMethodParameters(method));
            methodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            rowPanel.add(methodLabel);

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            spinner.setPreferredSize(new Dimension(100, spinner.getPreferredSize().height));
            spinner.setMaximumSize(new Dimension(100, spinner.getPreferredSize().height));
            spinner.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rowPanel.add(Box.createHorizontalGlue());
            rowPanel.add(spinner);

            add(rowPanel);
        }
    }

    private String getMethodParameters(PsiMethod method) {
        return Stream.of(method.getParameterList().getParameters())
                .map(param -> param.getType().getPresentableText() + " " + param.getName())
                .collect(Collectors.joining(", ", "(", ")"));
    }

    public void updateMethods(List<PsiMethod> methods) {
        this.removeAll();
        this.spinners.clear();
        this.methods.clear();
        this.methods.addAll(methods);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (PsiMethod method : methods) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));

            JLabel methodLabel = new JLabel(method.getName() + getMethodParameters(method));
            methodLabel.setIcon(AllIcons.Nodes.Method);
            methodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            rowPanel.add(methodLabel);

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            spinner.setPreferredSize(new Dimension(100, spinner.getPreferredSize().height));
            spinner.setMaximumSize(new Dimension(100, spinner.getPreferredSize().height));
            spinner.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rowPanel.add(Box.createHorizontalGlue());
            rowPanel.add(spinner);
            spinners.add(spinner);

            add(rowPanel);
        }

        revalidate();
        repaint();
    }

    public Map<PsiMethod, Integer> getMetodosSeleccionadosConSituacionesImposibles() {
        Map<PsiMethod, Integer> map = new HashMap<>();
        List<Integer> selectedNumbers = this.getSelectedNumbers();
        for (int i = 0; i < methods.size(); i++) {
            map.put(methods.get(i), selectedNumbers.get(i));
        }
        return map;
    }

    private List<Integer> getSelectedNumbers() {
        List<Integer> selectedNumbers = new ArrayList<>();
        for (JSpinner spinner : spinners) {
            selectedNumbers.add((Integer) spinner.getValue());
        }
        return selectedNumbers;
    }
}


