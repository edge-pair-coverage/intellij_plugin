package org.juancatalan.edgepaircoverageplugin.dialogs;

import org.juancatalan.edgepaircoverageplugin.DTO.MethodReportDTO;
import org.juancatalan.edgepaircoverageplugin.DTO.SituacionPruebaDTO;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

public class MethodReportPanel extends JPanel {
    private MethodReportDTO methodReportDTO;

    public MethodReportPanel(MethodReportDTO methodReportDTO) {
        this.methodReportDTO = methodReportDTO;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeUI();
    }

    private void initializeUI() {
        // Crear un panel de información con un fondo personalizado
        // Crear componentes
        JLabel nombreLabel = new JLabel("Nombre: " + methodReportDTO.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel caminosImposiblesLabel = new JLabel("Caminos Imposibles: " + methodReportDTO.getCaminosImposibles());
        caminosImposiblesLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel porcentajeCoberturaLabel = new JLabel("Porcentaje de Cobertura: " + methodReportDTO.getPorcentajeCobertura() + "%");
        porcentajeCoberturaLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Grafo como texto
        JLabel grafoLabel = new JLabel("Grafo: " + methodReportDTO.getGrafo());
        grafoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

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
            grafoImagenLabel.setText("Error al cargar imagen");
        }

        // Añadir componentes al panel de información
        this.add(nombreLabel);
        this.add(caminosImposiblesLabel);
        this.add(porcentajeCoberturaLabel);
        this.add(grafoLabel);
        this.add(grafoImagenLabel);



        // Crear acordeones para caminos y caminos cubiertos
        JPanel caminosPanel = createAccordionPanel(methodReportDTO.getCaminos(), "Caminos");
        JPanel caminosCubiertosPanel = createAccordionPanel(methodReportDTO.getCaminosCubiertos(), "Caminos Cubiertos");

        // Añadir acordeones al panel principal
        add(caminosPanel);
        add(caminosCubiertosPanel);
    }

    private JPanel createAccordionPanel(List<SituacionPruebaDTO> caminos, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crear botón de toggle
        JToggleButton toggleButton = new JToggleButton(title);
        toggleButton.setFont(new Font("Arial", Font.BOLD, 14));
        toggleButton.setFocusPainted(false);

        // Crear contenido colapsable
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setVisible(false);

        // Crear la tabla para mostrar caminos
        String[] columnNames = {"Nodo Inicio", "Nodo Medio", "Nodo Final", "Arista Inicio-Medio", "Arista Medio-Final"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (SituacionPruebaDTO situacion : caminos) {
            Object[] rowData = {
                    situacion.getNodoInicio(),
                    situacion.getNodoMedio(),
                    situacion.getNodoFinal(),
                    situacion.getAristaInicioMedio(),
                    situacion.getAristaMedioFinal()
            };
            tableModel.addRow(rowData);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Añadir listener al botón
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.setVisible(toggleButton.isSelected());
                revalidate();
                repaint();
            }
        });

        // Añadir botón y contenido al panel
        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }
}


