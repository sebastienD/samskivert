//
// samskivert library - useful routines for java programs
// Copyright (C) 2001-2011 Michael Bayne, et al.
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package com.samskivert.swing.event;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A DocumentAdapter for focusing DocumentListener events into a pinpoint
 * of easy wonderosity.
 * Or you can override each of the DocumentListener methods as you wish.
 */
public class DocumentAdapter implements DocumentListener
{
    /**
     * A handy-dandy method you can override to just do *something* whenever
     * the document changes.
     */
    public void documentChanged ()
    {
    }

    // documentation inherited from interface DocumentListener
    public void changedUpdate (DocumentEvent e)
    {
        documentChanged();
    }

    // documentation inherited from interface DocumentListener
    public void insertUpdate (DocumentEvent e)
    {
        documentChanged();
    }

    // documentation inherited from interface DocumentListener
    public void removeUpdate (DocumentEvent e)
    {
        documentChanged();
    }
}
