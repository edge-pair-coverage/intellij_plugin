package org.juancatalan.edgepaircoverageplugin.ui.panels;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.UIUtil;
import org.juancatalan.edgepaircoverageplugin.DTO.MethodReportDTO;
import org.juancatalan.edgepaircoverageplugin.ui.PillLabel;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class MethodReportPanel extends JPanel {
    private MethodReportDTO methodReportDTO;

    public MethodReportPanel(MethodReportDTO methodReportDTO) {
        this.methodReportDTO = methodReportDTO;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Crear un panel de información con un fondo personalizado
        // Crear componentes
        /*
        JLabel nombreLabel = new JLabel(methodReportDTO.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nombreLabel.setIcon(AllIcons.Nodes.Method);
         */

        JBLabel tituloPorcentajeCobertura = new JBLabel(UIUtil.ComponentStyle.LARGE);
        tituloPorcentajeCobertura.setText("Porcentaje de cobertura");
        panel.add(tituloPorcentajeCobertura);


        if (methodReportDTO.getCaminosImposibles() > 0){
            JBLabel caminosImposiblesLabel = new JBLabel("Con " + methodReportDTO.getCaminosImposibles() + " caminos imposibles");
            caminosImposiblesLabel.setComponentStyle(UIUtil.ComponentStyle.SMALL);
            panel.add(caminosImposiblesLabel);
        }

        PillLabel porcentajeCoberturaLabel = new PillLabel(methodReportDTO.getPorcentajeCobertura() + "%");
        porcentajeCoberturaLabel.setBackground(Color.GREEN);
        porcentajeCoberturaLabel.setForeground(Color.WHITE);

        // Grafo como texto
        // JLabel grafoLabel = new JLabel("Grafo: " + methodReportDTO.getGrafo());
        // grafoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Grafo Imagen
        JLabel grafoImagenLabel = new JLabel();

        // Intenta cargar la imagen desde la URL
        try {
            String imageUrl = methodReportDTO.getGrafoImagen();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                URL url = new URL(imageUrl);
                BufferedImage img = ImageIO.read(url);
                grafoImagenLabel.setIcon(new ImageIcon(img));
            } else {
                System.err.println("URL de imagen vacía o nula.");
                grafoImagenLabel.setText("Imagen no disponible");
            }
        } catch (Exception e) {
            System.err.println("Error cargando la imagen desde la URL: " + e.getMessage());
            grafoImagenLabel.setText(methodReportDTO.getGrafo());
        }

        // Añadir componentes al panel de información
        // panel.add(nombreLabel);
        panel.add(porcentajeCoberturaLabel);
        // panel.add(grafoLabel);
        panel.add(grafoImagenLabel);



        // Crear acordeones para caminos y caminos cubiertos
        JPanel childPanel = createAccordionPanel(methodReportDTO.getNombre(), panel);

        // Añadir acordeones al panel principal
        add(childPanel);
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


