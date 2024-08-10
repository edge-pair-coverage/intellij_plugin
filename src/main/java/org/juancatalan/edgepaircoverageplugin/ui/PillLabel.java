package org.juancatalan.edgepaircoverageplugin.ui;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;

public class PillLabel extends JBLabel {

    public PillLabel(String text) {
            super(text);
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(JBColor.LIGHT_GRAY); // Usar JBColor para compatibilidad con temas
            setForeground(JBColor.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Habilitar el anti-aliasing para suavizar los bordes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Determinar el tamaño y las propiedades del borde redondeado
            int arcWidth = getHeight();
            int arcHeight = getHeight();
            int padding = 10; // Espacio extra alrededor del texto

            // Dibujar el fondo redondeado
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

            // Dibujar el texto del JLabel
            g2d.setColor(getForeground());
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int textX = (getWidth() - fontMetrics.stringWidth(getText())) / 2;
            int textY = (getHeight() - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
            g2d.drawString(getText(), textX, textY);

            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            int extraSpace = 20; // Espacio extra para el borde redondeado
            return new Dimension(size.width + extraSpace, size.height + extraSpace / 2);
        }
}