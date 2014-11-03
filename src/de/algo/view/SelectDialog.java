package de.algo.view;

import de.algo.model.MyImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

public class SelectDialog extends JDialog {
        public final ImageSelectorPanel SELECTORPANEL;
        private JButton accept;
        private JButton cancel;
        private ActionListener actionListener;

        public SelectDialog(JFrame owner, ImageSelectorPanel selectorPanel) {
                super(owner, "Select images..", true);

                this.SELECTORPANEL = selectorPanel;
                SELECTORPANEL.addChangeListener(e -> {
                        if (!SELECTORPANEL.getSelected().isEmpty()) {
                                accept.setEnabled(true);
                        } else {
                                accept.setEnabled(false);
                        }
                });

                JScrollPane scrollpane = new JScrollPane(selectorPanel);
                scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollpane.getVerticalScrollBar().setUnitIncrement(16);

                setLayout(new BorderLayout());
                add(scrollpane, BorderLayout.CENTER);

                JPanel buttonsPanel = new JPanel();

                accept = new JButton("Accept");
                accept.setEnabled(false);
                accept.addActionListener(e -> submit(true));
                buttonsPanel.add(accept);

                cancel = new JButton("Cancel");
                cancel.addActionListener(e -> submit(false));
                buttonsPanel.add(cancel);

                add(buttonsPanel, BorderLayout.SOUTH);

                addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                submit(false);
                        }
                });
        }

        public void addActionListener(ActionListener actionListener) {
                this.actionListener = actionListener;
        }

        private void submit(boolean success) {
                if (success) {
                        if (actionListener != null) {
                                actionListener.actionPerformed(new ActionEvent(this, 0, ""));
                        }
                }

                setVisible(false);
        }

        public void setImages(Collection<MyImage> images) {
                SELECTORPANEL.clear();
                SELECTORPANEL.addTiles(images);
                accept.setEnabled(false);
        }
}
