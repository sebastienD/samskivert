//
// $Id: CollapsibleList.java,v 1.3 2002/04/27 00:23:49 shaper Exp $

package com.samskivert.swing;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

/**
 * Displays a list of components in sections (with section headers) that
 * can be collapsed. Each section of a list uses a {@link JList} instance
 * to render the section elements.
 */
public class CollapsibleList extends JPanel
{
    /**
     * Constructs an empty collapsible list.
     */
    public CollapsibleList ()
    {
        // set up our layout manager
        VGroupLayout gl = new VGroupLayout(VGroupLayout.NONE);
        gl.setOffAxisPolicy(VGroupLayout.STRETCH);
        gl.setJustification(VGroupLayout.TOP);
        gl.setOffAxisJustification(VGroupLayout.LEFT);
        setLayout(gl);
    }

    /**
     * Constructs a collapsible list with the supplied section labels and
     * models.
     */
    public CollapsibleList (String[] sections, ListModel[] models)
    {
        this(); // set up our layout manager

        for (int i = 0; i < sections.length; i++) {
            addSection(sections[i], models[i]);
        }
    }

    /**
     * Returns the number of sections.
     */
    public int getSectionCount ()
    {
        return getComponentCount()/2;
    }

    /**
     * Adds a section to this collapsible list.
     *
     * @param label the title of the section.
     * @param model the list model to use for the new section.
     *
     * @return the index of the newly added section.
     */
    public int addSection (String label, ListModel model)
    {
        add(new JLabel(label));
        add(new JList(model));
        return getSectionCount()-1;
    }

    /**
     * Returns the label object associated with the title of the specified
     * section.
     */
    public JLabel getSectionLabel (int index)
    {
        return (JLabel)getComponent(index*2);
    }

    /**
     * Returns the list object associated with the specified section.
     */
    public JList getSectionList (int index)
    {
        return (JList)getComponent(index*2+1);
    }

    /**
     * Toggles the collapsed state of the specified section.
     */
    public void toggleCollapsed (int index)
    {
        JList list = getSectionList(index);
        list.setVisible(!list.isVisible());
    }
}
