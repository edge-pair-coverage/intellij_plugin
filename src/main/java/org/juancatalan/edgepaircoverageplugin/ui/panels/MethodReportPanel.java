package org.juancatalan.edgepaircoverageplugin.ui.panels;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.juancatalan.edgepaircoverageplugin.DTO.MethodReportDTO;
import org.juancatalan.edgepaircoverageplugin.DTO.SituacionPruebaDTO;
import org.juancatalan.edgepaircoverageplugin.ui.PillLabel;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class MethodReportPanel extends JPanel {
    private MethodReportDTO methodReportDTO;

    public MethodReportPanel(MethodReportDTO methodReportDTO) {
        this.methodReportDTO = methodReportDTO;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        SwingUtilities.invokeLater(this::initializeUI);

    }

    private void initializeUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(JBUI.Borders.empty(10, 0, 10, 10));
        // Crear un panel de información con un fondo personalizado
        // Crear componentes
        /*
        JLabel nombreLabel = new JLabel(methodReportDTO.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nombreLabel.setIcon(AllIcons.Nodes.Method);
         */

        JBLabel tituloPorcentajeCobertura = new JBLabel(UIUtil.ComponentStyle.LARGE);
        tituloPorcentajeCobertura.setText("Coverage percentage");
        panel.add(tituloPorcentajeCobertura);


        if (methodReportDTO.getCaminosImposibles() > 0){
            JBLabel caminosImposiblesLabel = new JBLabel("With " + methodReportDTO.getCaminosImposibles() + " impossible test situations");
            caminosImposiblesLabel.setComponentStyle(UIUtil.ComponentStyle.SMALL);
            panel.add(caminosImposiblesLabel);
        }

        JBLabel porcentajeCoberturaLabel = new JBLabel(methodReportDTO.getPorcentajeCobertura() + "%");
        porcentajeCoberturaLabel.setBorder(JBUI.Borders.empty(10));
        panel.add(porcentajeCoberturaLabel);
        //porcentajeCoberturaLabel.setBackground(Color.GREEN);
        //porcentajeCoberturaLabel.setForeground(Color.WHITE);


        // Grafo
        JBLabel tituloGrafo = new JBLabel(UIUtil.ComponentStyle.LARGE);
        tituloGrafo.setText("Graph");
        panel.add(tituloGrafo);


        // Grafo Imagen
        JLabel grafoImagenLabel = new JBLabel();

        // Intenta cargar la imagen desde la URL
        try {
            String imageUrl = methodReportDTO.getGrafoImagen();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                URL url = new URI(imageUrl).toURL();
                BufferedImage img = ImageIO.read(url);
                grafoImagenLabel.setIcon(new ImageIcon(img));
            } else {
                grafoImagenLabel.setText("Image non available");
            }
        } catch (Exception e) {
            grafoImagenLabel.setText("<html>" + methodReportDTO.getGrafo().replace("\n", "<br/>").replace(" ", "&nbsp;") + "</html>");
        }
        grafoImagenLabel.setBorder(JBUI.Borders.empty(10));
        panel.add(grafoImagenLabel);

        // Situaciones de prueba

        JBLabel tituloSituacionesPrueba = new JBLabel(UIUtil.ComponentStyle.LARGE);
        tituloSituacionesPrueba.setText("Test situations");
        panel.add(tituloSituacionesPrueba);

        JBPanel situacionPruebaPanel = new JBPanel<>();
        situacionPruebaPanel.setLayout(new BoxLayout(situacionPruebaPanel, BoxLayout.Y_AXIS));
        for (SituacionPruebaDTO situacionPruebaDTO : methodReportDTO.getCaminos()) {
            JBLabel situacionPruebaLabel = getSituacionPruebaLabel(situacionPruebaDTO);
            situacionPruebaPanel.add(situacionPruebaLabel);
        }
        situacionPruebaPanel.setBorder(JBUI.Borders.empty(10));
        panel.add(situacionPruebaPanel);

        // Crear acordeones para caminos y caminos cubiertos
        JPanel childPanel = createAccordionPanel(methodReportDTO.getNombre(), panel);

        // Añadir acordeones al panel principal
        add(childPanel);
    }

    private @NotNull JBLabel getSituacionPruebaLabel(SituacionPruebaDTO situacionPruebaDTO) {
        JBLabel situacionPruebaLabel = new JBLabel(
                situacionPruebaDTO.getNodoInicio() + " -> " +
                        situacionPruebaDTO.getAristaInicioMedio() + " -> " +
                        situacionPruebaDTO.getNodoMedio() + " -> " +
                        situacionPruebaDTO.getAristaMedioFinal() + " -> " +
                        situacionPruebaDTO.getNodoFinal());
        situacionPruebaLabel.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        situacionPruebaLabel.setHorizontalTextPosition(SwingConstants.LEADING);
        if (methodReportDTO.getCaminosCubiertos().contains(situacionPruebaDTO)){
            situacionPruebaLabel.setIcon(AllIcons.RunConfigurations.TestPassed);
        }
        else {
            situacionPruebaLabel.setIcon(AllIcons.RunConfigurations.TestFailed);
        }
        return situacionPruebaLabel;
    }

    private JPanel createAccordionPanel(String title, JPanel childPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crear botón de toggle
        JToggleButton toggleButton = new JToggleButton(title);
        toggleButton.setIcon(AllIcons.Nodes.Method);
        toggleButton.setFont(new Font("Arial", Font.BOLD, 14));
        toggleButton.setFocusPainted(false);

        childPanel.setVisible(false);

        // Añadir listener al botón
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                childPanel.setVisible(toggleButton.isSelected());
                revalidate();
                repaint();
            }
        });

        // Añadir botón y contenido al panel
        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(childPanel, BorderLayout.CENTER);

        return panel;
    }
}


