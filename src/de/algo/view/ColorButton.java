package de.algo.view;

import javax.swing.*;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorButton extends JButton {
        public static final int PRIMARY = 0;
        public static final int SECONDARY = 1;

        private static final Map<Integer, Color> COLORS = new HashMap<>();

        private int id;
        private Color color;

        public ColorButton(int id, Color initial, String tooltip) {
                super(View.getIcon("empty_icon&24"));

                this.id = id;
                this.color = initial;
                COLORS.put(id, initial);

                setToolTipText(tooltip);

                addActionListener(e -> {
                        JColorChooser picker = new JColorChooser(color);
                        picker.removeChooserPanel(picker.getChooserPanels()[0]);
                        picker.setSelectionModel(new DefaultColorSelectionModel());
                        picker.setPreviewPanel(new JPanel());
                        picker.setColor(color);

                        JDialog dialog = JColorChooser.createDialog(
                                ColorButton.this,
                                "Choose a color",
                                true,
                                picker,
                                actionEvent -> {
                                        color = picker.getColor();
                                        COLORS.put(id, color);
                                        ColorButton.this.repaint();
                                },
                                null);
                        dialog.setLocationRelativeTo(null);

                        dialog.setVisible(true);
                });
        }

        @Override
        public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);

                if (enabled) {
                        COLORS.put(id, color);
                } else {
                        COLORS.put(id, COLORS.get(PRIMARY));
                }
        }

        public static Color getColor(int id) {
                return COLORS.get(id);
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (isEnabled()) {
                        g.setColor(COLORS.get(id));
                        g.fillRect(5, 5, 25, 25);
                }
        }
}
