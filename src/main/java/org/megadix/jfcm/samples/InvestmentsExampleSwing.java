package org.megadix.jfcm.samples;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;

import javax.swing.*;

import org.megadix.jfcm.*;
import org.megadix.jfcm.act.SignumActivator;
import org.megadix.jfcm.conn.WeightedConnection;

public class InvestmentsExampleSwing extends JFrame {

    /*
     * inner classes
     */

    class ConceptSlider extends JSlider {
        private Concept concept;

        public ConceptSlider(Concept concept) {
            super(HORIZONTAL);
            this.concept = concept;
            setPaintTicks(true);
            setSnapToTicks(true);
            setMinorTickSpacing(100);
            updateValue();
        }

        public void updateValue() {
            setValue((int) (concept.getOutput() * 100.0));
            if (!SwingUtilities.isEventDispatchThread()) {
                repaint();
            }
        }
    }

    class LockButton extends JButton implements ActionListener {
        private Concept concept;
        private ImageIcon iconLocked;
        private ImageIcon iconUnlocked;

        public LockButton(Concept concept) {
            super();
            setPreferredSize(new Dimension(24, 24));
            this.concept = concept;
            URL lockedIconURL = getClass().getResource("/lock.png");
            URL unlockedIconURL = getClass().getResource("/lock_open.png");
            iconLocked = new ImageIcon(lockedIconURL);
            iconUnlocked = new ImageIcon(unlockedIconURL);
            addActionListener(this);
            update();
        }

        public void actionPerformed(ActionEvent event) {
            concept.setFixedOutput(!concept.isFixedOutput());
            update();
        }

        public void update() {
            setIcon(concept.isFixedOutput() ? iconLocked : iconUnlocked);
            if (! SwingUtilities.isEventDispatchThread()) {
                repaint();
            }
        }
    }

    class RunnerThread extends Thread {
        private boolean active;

        public RunnerThread() {
            this.active = false;
        }

        public void run() {
            while (isActive()) {
                try {
                    map.execute();
                    Iterator<Concept> iter = map.getConceptsIterator();
                    while (iter.hasNext()) {
                        Concept concept = iter.next();
                        ConceptSlider sl = sliders.get(concept.getName());
                        sl.updateValue();
                        JTextField txt = textfields.get(concept.getName());
                        txt.setText(concept.getOutput().toString());
                    }
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                }
            }
        }

        public synchronized boolean switchActive() {
            this.active = !active;
            return active;
        }

        public synchronized boolean isActive() {
            return active;
        }

        public synchronized void setActive(boolean active) {
            this.active = active;
        }
    }

    /*
     * actual class
     */
    private CognitiveMap map;
    private JButton bt_start;
    private Map<String, ConceptSlider> sliders = new HashMap<String, ConceptSlider>();
    private Map<String, JTextField> textfields = new HashMap<String, JTextField>();
    private RunnerThread runnerThread;

    public InvestmentsExampleSwing() {
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initMap();
        runnerThread = new RunnerThread();
        initGUI();
    }

    private void initGUI() {
        getContentPane().add(initMainPanel(), BorderLayout.CENTER);
        getContentPane().add(initControlButtons(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel initMainPanel() {
        JPanel panel = new JPanel(new GridLayout(map.getConcepts().size(), 1));

        Iterator<Concept> iter = map.getConceptsIterator();
        while (iter.hasNext()) {
            Concept concept = iter.next();
            panel.add(initConceptPanel(concept));
        }

        return panel;
    }

    private JPanel initConceptPanel(Concept concept) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // === first row ===
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.insets = new Insets(2, 2, 2, 2);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.0;
        c.weighty = 0.0;

        // lock button
        LockButton bt_lock = new LockButton(concept);
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.0;
        panel.add(bt_lock, c);

        // textfield
        c.gridx++;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.0;
        JTextField txt_value = new JTextField(concept.getOutput().toString());
        txt_value.setColumns(4);
        txt_value.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(txt_value, c);
        textfields.put(concept.getName(), txt_value);

        // label
        c.gridx++;
        c.weightx = 1.0;
        JLabel lbl_name = new JLabel(concept.getName() + " - " + concept.getDescription());
        panel.add(lbl_name, c);

        // === second row ===
        c.gridx = 0;
        c.gridy++;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.BOTH;

        ConceptSlider slider = new ConceptSlider(concept);
        slider.setMinimum(-100);
        slider.setMaximum(100);
        slider.updateValue();
        panel.add(slider, c);
        sliders.put(concept.getName(), slider);

        return panel;
    }

    private JPanel initControlButtons() {
        JPanel panel = new JPanel();
        bt_start = new JButton("Start");
        bt_start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                boolean running = runnerThread.switchActive();
                if (running) {
                    bt_start.setText("Stop");
                    runnerThread.start();
                } else {
                    bt_start.setText("Start");
                }
            }
        });
        panel.add(bt_start);

        return panel;
    }

    private void initMap() {
        map = new CognitiveMap("Investments");
        ConceptActivator af = new SignumActivator();

        Concept c1 = new Concept("c1", "Interest rate", af, 0.0, 1.0, false);
        map.addConcept(c1);

        Concept c2 = new Concept("c2", "Inflation", af, 0.0, -1.0, false);
        map.addConcept(c2);

        Concept c3 = new Concept("c3", "Occupation", af, 0.0, 0.0, false);
        map.addConcept(c3);

        Concept c4 = new Concept("c4", "Productive investments", af, 0.0, 0.0, false);
        map.addConcept(c4);

        FcmConnection conn_1 = new WeightedConnection("c1 -> c4", "Interest rate -> Productive investments", -0.8);
        conn_1.setFrom(c1);
        conn_1.connectOutputTo(c4);
        map.addConnection(conn_1);

        FcmConnection conn_2 = new WeightedConnection("c4 -> c3", "Productive investments -> Occupation", 1.0);
        conn_2.setFrom(c4);
        conn_2.connectOutputTo(c3);
        map.addConnection(conn_2);

        FcmConnection conn_3 = new WeightedConnection("c3 -> c2", "Occupation -> Inflation", 0.9);
        conn_3.setFrom(c3);
        conn_3.connectOutputTo(c2);
        map.addConnection(conn_3);

        FcmConnection conn_4 = new WeightedConnection("c2 -> c1", "Inflation -> Interest rate", 1.0);
        conn_4.setFrom(c2);
        conn_4.connectOutputTo(c1);
        map.addConnection(conn_4);
    }

    public static void main(String[] args) {
        final InvestmentsExampleSwing frame = new InvestmentsExampleSwing();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }
}