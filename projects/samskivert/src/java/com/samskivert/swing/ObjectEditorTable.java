//
// $Id: ObjectEditorTable.java,v 1.1 2004/06/14 01:08:22 ray Exp $

package com.samskivert.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.reflect.Field;

import java.util.BitSet;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.samskivert.swing.event.CommandEvent;

import com.samskivert.util.ClassUtil;
import com.samskivert.util.ListUtil;

import com.samskivert.Log;

/**
 * Allows simple displaying and editable of Objects in a table format.
 */
public class ObjectEditorTable extends JTable
{
    /**
     * The default FieldInterpreter, which can be used to customize the
     * name, values, and editing of a field in a DSet.Entry.
     *
     * There are a number of ways that the field editing can be customized.
     * A custom renderer (and editor) may be installed on the table, possibly
     * in conjunction with overriding getClass(). Or you may simply override
     * getValue (and setValue) to interpret between types, say for instance
     * turning an integer field that may be one of three constant values into
     * String names of the values.
     */
    public static class FieldInterpreter
    {
        /**
         * Get the name that she be used for the column header for the specified
         * field. By default it's merely the name of the field.
         */
        public String getName (Field field)
        {
            return field.getName();
        }

        /**
         * Get the class of the specified field. By default, the class of
         * the field is used, or its object equivalent if it is a primitive
         * class.
         */
        public Class getClass (Field field)
        {
            Class clazz = field.getType();
            return ClassUtil.objectEquivalentOf(clazz);
        }

        /**
         * Get the value of the specified field in the specified object.
         * By default, the field is used to directly access the value.
         */
        public Object getValue (Object obj, Field field)
        {
            try {
                return field.get(obj);
            } catch (IllegalAccessException iae) {
                Log.logStackTrace(iae);
                return null;
            }
        }

        /**
         * Set the value of the specified field in the specified object.
         * By default, the field is used to directly set the value.
         */
        public void setValue (Object obj, Object value, Field field)
        {
            try {
                field.set(obj, value);
            } catch (IllegalAccessException iae) {
                Log.logStackTrace(iae);
            }
        }
    }


    /**
     * Construct a table to display the specified class.
     *
     * @param protoClass the Class of the data that will be displayed.
     */
    public ObjectEditorTable (Class protoClass)
    {
        this(protoClass, null);
    }

    /**
     * Construct a table to display and edit the specified class.
     *
     * @param protoClass the Class of the data that will be displayed.
     * @param editableFields the names of the fields that are editable.
     */
    public ObjectEditorTable (Class protoClass, String[] editableFields)
    {
        this(protoClass, editableFields, null);
    }

    /**
     * Construct a table to display and edit the specified class.
     *
     * @param protoClass the Class of the data that will be displayed.
     * @param editableFields the names of the fields that are editable.
     * @param interp The {@link #FieldInterpreter} to use.
     */
    public ObjectEditorTable (Class protoClass, String[] editableFields,
                              FieldInterpreter interp)
    {
        _interp = (interp != null) ? interp : new FieldInterpreter();

        _fields = ClassUtil.getFields(protoClass);
        for (int ii=0, nn=_fields.length; ii < nn; ii++) {
            if (ListUtil.contains(editableFields, _fields[ii].getName())) {
                _editable.set(ii);
            }
        }

        setModel(_model);
    }

    /**
     * Set the data to be viewed or edited.
     * Note that the passed in data will be edited in place.
     */
    public void setData (Object[] data)
    {
        _data = data;
        _model.fireTableDataChanged();
    }

    /**
     * Set this table to merely display / edit one lousy Object.
     */
    public void setData (Object data)
    {
        setData(new Object[] { data });
    }

    /**
     * Get the edited data. Not really needed, since the the data passed
     * to set is 
     */
    public Object[] getData ()
    {
        return _data;
    }

    /**
     * Get the currently selected object, or null if none selected.
     */
    public Object getSelectedObject ()
    {
        int row = getSelectedRow();
        return (row == -1) ? null : _data[row];
    }

    /**
     * Add an action listener to this table. Will be notified when objects
     * are edited.
     */
    public void addActionListener (ActionListener listener)
    {
        listenerList.add(ActionListener.class, listener);
    }

    /**
     * Remove the specified action listener.
     */
    public void removeActionListener (ActionListener listener)
    {
        listenerList.remove(ActionListener.class, listener);
    }

    /**
     * A table model that uses the FieldInterpreter to muck with the data
     * objects.
     */
    protected AbstractTableModel _model = new AbstractTableModel() {
        /**
         * Get the object at the specified row. Useful for our subclass.
         */
        public Object getObjectAt (int row) {
            return _data[row];
        }

        // documentation inherited
        public int getColumnCount ()
        {
            return _fields.length;
        }

        // documentation inherited
        public int getRowCount() {
            return (_data == null) ? 0 : _data.length;
        }

        // documentation inherited
        public String getColumnName (int col)
        {
            return _interp.getName(_fields[col]);
        }

        // documentation inherited
        public boolean isCellEditable (int row, int col)
        {
            return _editable.get(col);
        }

        // documentation inherited
        public Class getColumnClass (int col)
        {
            return _interp.getClass(_fields[col]);
        }

        // documentation inherited
        public Object getValueAt (int row, int col)
        {
            Object o = getObjectAt(row);
            return _interp.getValue(o, _fields[col]);
        }

        // documentation inherited
        public void setValueAt (Object value, int row, int col)
        {
            Object o = getObjectAt(row);
            Object oldValue = _interp.getValue(o, _fields[col]);
            // we only set the value if it has changed
            if ((oldValue == null && value != null) ||
                !oldValue.equals(value)) {
                _interp.setValue(o, value, _fields[col]);

                // fire the event
                CommandEvent event = null;
                Object[] listeners = ObjectEditorTable.this.listenerList
                    .getListenerList();
                for (int ii=listeners.length-2; ii >= 0; ii -= 2) {
                    if (listeners[ii] == ActionListener.class) {
                        // lazy-create the event
                        if (event == null) {
                            event = new CommandEvent(ObjectEditorTable.this,
                                _fields[col].getName(), o);
                        }
                        ((ActionListener) listeners[ii+1]).actionPerformed(
                            event);
                    }
                }
            }
        }
    };

    /** The list of fields in the prototypical Entry object. */
    protected Field[] _fields;

    /** An interpreter that is used to massage values in and out of the
     * entries. */
    protected FieldInterpreter _interp;

    /** A list of flags corresponding to the _fields (and the table columns)
     * that indicate if the field is editable. */
    protected BitSet _editable = new BitSet();

    /** The data being edited. */
    protected Object[] _data;
}
