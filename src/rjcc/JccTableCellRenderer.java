/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author jonick
 */
public class JccTableCellRenderer extends DefaultTableCellRenderer{
    public int x;
    public int y;
    public JccTableCellRenderer()
    {
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value.toString().equals("0.00")) {
            cell.setForeground(Color.lightGray);             
        } else
            cell.setForeground(Color.black);
        return cell;
    }    
    
}
