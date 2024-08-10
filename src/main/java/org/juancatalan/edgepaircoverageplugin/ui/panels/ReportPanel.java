package org.juancatalan.edgepaircoverageplugin.ui.panels;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import javax.swing.*;
import java.awt.*;

import org.juancatalan.edgepaircoverageplugin.DTO.MethodReportDTO;

public class ReportPanel extends JPanel {
    private final Project project;

    public ReportPanel(@Nullable Project project, List<MethodReportDTO> methodReport) {
        this.project = project;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (MethodReportDTO methodReportDTO: methodReport){
            panel.add(new MethodReportPanel(methodReportDTO));
        }

        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    public static void populateMethods(Project project) {

    }
}


