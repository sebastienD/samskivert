//
// $Id: DialogUtil.java,v 1.1 2002/07/09 17:48:13 ray Exp $

package com.samskivert.swing.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.samskivert.swing.JInternalDialog;

/**
 * Utility methods for dialogs.
 */
public class DialogUtil
{
    /**
     * Creates and shows an internal dialog with the specified panel.
     */
    public static JInternalDialog createDialog (JFrame frame, JPanel content)
    {
        JInternalDialog dialog = new JInternalDialog(frame);
        setContent(dialog, content);
        SwingUtil.centerComponent(frame, dialog);
        dialog.showDialog();
        return dialog;
    }

    /**
     * Change the content of the internal dailog.
     */
    public static void setContent (JInternalDialog dialog, JPanel content)
    {
        Container holder = dialog.getContentPane();
        holder.removeAll();
        holder.add(content, BorderLayout.CENTER);
        dialog.pack();
    }

    /**
     * Find the internal dialog that is a superparent of the specified
     * component.
     */
    public static JInternalDialog getInternalDialog (Component any)
    {
        Component parent = any;
        while (parent != null && !(parent instanceof JInternalDialog)) {
            parent = parent.getParent();
        }

        return (JInternalDialog) parent;
    }

    /**
     * Invalidate and resize the entire dialog, given any component in it.
     */
    public static void invalidateDialog (Component any)
    {
        JInternalDialog dialog = getInternalDialog(any);
        invalidateChildren(dialog);

        dialog.resize(dialog.getPreferredSize());
    }

    /**
     * Invalidate all the children of the specified container.
     */
    protected static void invalidateChildren (Container c)
    {
        int numkids = c.getComponentCount();
        for (int ii=0; ii < numkids; ii++) {
            Component child = c.getComponent(ii);
            if (child instanceof Container) {
                invalidateChildren((Container) child);
            }
            child.invalidate();
        }
    }
}