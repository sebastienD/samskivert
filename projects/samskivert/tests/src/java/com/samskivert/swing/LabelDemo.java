//
// $Id: LabelDemo.java,v 1.5 2002/11/06 04:29:17 mdb Exp $

package com.samskivert.swing;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class LabelDemo extends JPanel
{
    public LabelDemo ()
    {
        // create our labels
        String text = "The quick brown fox jumped over the lazy dog. " +
            "He then popped into the butcher's and picked up some mutton.";
        Font font = new Font("Courier", Font.PLAIN, 10);

        _labelZero = new Label(text);
        _labelZero.setFont(font);

        _labelOne = new Label(text);
        _labelOne.setFont(font);
        _labelOne.setTargetWidth(100);
        _labelOne.setAlignment(Label.RIGHT);
        _labelOne.setAlternateColor(Color.white);
        _labelOne.setFont(new Font("Dialog", Font.PLAIN, 12));

        _labelTwo = new Label(text);
        _labelTwo.setFont(font);
        _labelTwo.setTargetHeight(30);
        _labelTwo.setAlignment(Label.CENTER);
        _labelTwo.setAlternateColor(Color.white);
        _labelTwo.setFont(new Font("Dialog", Font.PLAIN, 12));

//         try {
//             InputStream in = new FileInputStream("delarobb.TTF");
//             Font sfont = Font.createFont(Font.TRUETYPE_FONT, in);
//             in.close();
//             _labelThree = new Label(String.valueOf(30), Label.OUTLINE,
//                                     Color.pink, Color.black,
//                                     sfont.deriveFont(Font.PLAIN, 24));

//         } catch (Exception e) {
//             e.printStackTrace(System.err);
//         }
    }

    public void layout ()
    {
        super.layout();

        // layout our labels
        Graphics2D g = (Graphics2D)getGraphics();
        _labelZero.layout(g);
        System.out.println("l0: " + _labelZero.getSize());
        _labelOne.layout(g);
        System.out.println("l1: " + _labelOne.getSize());
        _labelTwo.layout(g);
        System.out.println("l2: " + _labelTwo.getSize());
//         _labelThree.layout(g);
//         System.out.println("l3: " + _labelThree.getSize());
    }

    public void paintComponent (Graphics g)
    {
        super.paintComponent(g);

        // render our labels
        Graphics2D g2 = (Graphics2D)g;
        Dimension size;
        int x = 10, y = 10;

        size = _labelZero.getSize();
        g2.setColor(Color.white);
        g2.fillRect(x, y, size.width, size.height);
        g2.setColor(Color.black);
        _labelZero.render(g2, x, y);

        y += 20;
        size = _labelTwo.getSize();
        g2.setColor(Color.white);
        g2.fillRect(x, y, size.width, 30);
        g2.setColor(Color.gray);
        g2.fillRect(x, y, size.width, size.height);
        g2.setColor(Color.black);
        _labelTwo.render(g2, x, y);

        y += 40;
        size = _labelOne.getSize();
        g2.setColor(Color.white);
        g2.fillRect(x, y, 100, size.height);
        g2.setColor(Color.gray);
        g2.fillRect(x, y, size.width, size.height);
        g2.setColor(Color.black);
        _labelOne.render(g2, x, y);

//         y += 100;
//         size = _labelThree.getSize();
//         g2.setColor(Color.white);
//         g2.fillRect(x, y, 100, size.height);
//         g2.setColor(Color.gray);
//         g2.fillRect(x, y, size.width, size.height);
//         g2.setColor(Color.black);
//         _labelThree.render(g2, x, y);
    }

    public Dimension getPreferredSize ()
    {
        // lay out label zero if necessary
        int width = _labelZero.getSize().width;
        if (width == 0) {
            Graphics2D g = (Graphics2D)getGraphics();
            _labelZero.layout(g);
            width = _labelZero.getSize().width;
        }

        return new Dimension(width + 20, 300);
    }

    public static void main (String[] args)
    {
        JFrame frame = new JFrame("Label Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        LabelDemo demo = new LabelDemo();
        frame.getContentPane().add(demo);
        frame.pack();
        frame.show();
    }

    protected Label _labelZero;
    protected Label _labelOne;
    protected Label _labelTwo;
//     protected Label _labelThree;
}
