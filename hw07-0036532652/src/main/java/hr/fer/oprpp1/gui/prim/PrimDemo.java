package hr.fer.oprpp1.gui.prim;

import javax.swing.*;
import java.awt.*;

public class PrimDemo extends JFrame {


    public PrimDemo(){
        super();
        setLocation(50,50);
        initGUI();
        pack();
    }

    /**
     * initializes GUI
     */
    private void initGUI() {
        Container container = this.getContentPane();
        PrimListModel listModel = new PrimListModel();
        JList<Integer> listA = new JList<>(listModel);
        JList<Integer> listB = new JList<>(listModel);
        JScrollPane p1 = new JScrollPane(listA);
        JScrollPane p2 = new JScrollPane(listB);

        container.setLayout(new BorderLayout());

        JButton button = new JButton("Sljedeći");
        button.addActionListener(event -> listModel.next());
        container.add(button, BorderLayout.PAGE_END);

        JPanel listContainer = new JPanel(new GridLayout(0,2));
        listContainer.add(p1);
        listContainer.add(p2);

        container.add(listContainer, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrimDemo().setVisible(true);
        });
    }
}
