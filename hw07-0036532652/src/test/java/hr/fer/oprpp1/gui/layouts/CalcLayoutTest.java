package hr.fer.oprpp1.gui.layouts;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CalcLayoutTest {

    @Test
    public void testRowNum(){
        CalcLayout cl = new CalcLayout();
        JLabel label = new JLabel();
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(label, new RCPosition(6, 5)));
    }

    @Test
    public void testColumnNum(){
        CalcLayout cl = new CalcLayout();
        JLabel label = new JLabel();
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(label, new RCPosition(1, 8)));
    }

    @Test
    public void testIllegalColumnRowNum(){
        CalcLayout cl = new CalcLayout();
        JLabel label = new JLabel();
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(label, new RCPosition(1, 5)));
    }

    @Test
    public void testComponentAlreadyExists(){
        CalcLayout cl = new CalcLayout();
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        cl.addLayoutComponent(label1, new RCPosition(1, 7));
        assertThrows(CalcLayoutException.class, () -> cl.addLayoutComponent(label2, new RCPosition(1, 7)));
    }

    @Test
    public void testPrefferedSize1(){
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();
        assertEquals(new Dimension(152, 158), dim);
    }


    @Test
    public void testPrefferedSize2(){
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();
        assertEquals(new Dimension(152, 158), dim);
    }

}
