/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.util.Locale;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author User
 */

public class SCarType {
        JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
            
    DefaultTableModel tm = new DefaultTableModel(); 
    TableRowSorter<TableModel> rowSorter;
    String title = "Справочник [Типы ТС]";
    String query = "SELECT * FROM types";
    static String query1 = "SELECT id, type FROM types";
    static String query2 = "SELECT type FROM types WHERE id=";
    
    public SCarType() {
        initTable();
    }
    
    String getTitle() {
        return title;
    }
    
    String getTypes(){
        return query1;
    }
    String getType(String id){
        return query2+id;
    }
    JTable getTable(){
        return table;
    }
    
    String getJournal(){
        this.table.setRowSorter(rowSorter);
        return query + " WHERE status = 1";
    }
    DefaultTableModel getModel(){
        return tm;
    }   
   void setRowFilter(String text, int col) {              
       if (text != null) 
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
       else
        rowSorter.setRowFilter(null);       
   }    
    
void initTable(){        
        tm.addColumn("#");       
        tm.addColumn("Тип");        
        
        table.setModel(tm);       
        rowSorter = new TableRowSorter<>(this.table.getModel());        
        
        table.setAutoCreateRowSorter(true);
        table.setFont(MainFrame.tableFont);
        table.setBackground(MainFrame.tableColor);
        table.setGridColor(MainFrame.tableGridColor);
        table.setRowHeight(20);
        table.setSelectionBackground(MainFrame.selectColor);
        table.setSelectionForeground(MainFrame.selectForegroundColor);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);        

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(0).setMinWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(60);        
        table.getColumnModel().getColumn(0).setResizable(false);        
    }    
}
