package de.algo.view;

import javax.swing.*;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ColorButton extends JButton {
        public static final int PRIMARY = 0;
        public static final int SECONDARY = 1;

        private static final Map<Integer, Color> COLORS = new HashMap<>();

        private int id;

        public ColorButton(int id, Color initial, String tooltip) {
                super(View.getIcon("empty_icon&24"));

                this.id = id;
                COLORS.put(id, initial);

                setToolTipText(tooltip);

                addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                                JColorChooser picker = new JColorChooser(COLORS.get(id));
                                picker.removeChooserPanel(picker.getChooserPanels()[0]);
                                picker.setSelectionModel(new DefaultColorSelectionModel());
                                picker.setPreviewPanel(new JPanel());
                                picker.setColor(COLORS.get(id));

                                JDialog dialog = JColorChooser.createDialog(
                                        ColorButton.this,
                                        "Choose a color",
                                        true,
                                        picker,
                                        actionEvent -> {
                                                COLORS.put(id, picker.getColor());
                                                ColorButton.this.repaint();
                                        },
                                        null);
                                dialog.setLocationRelativeTo(null);

                                dialog.setVisible(true);
                        }
                });
        }

        public static Color getColor(int id) {
                return COLORS.get(id);
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(COLORS.get(id));
                g.fillRect(5, 5, 25, 25);
        }
}
