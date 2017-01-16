/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class SShift {
               
            JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
    DefaultTableModel tm = new DefaultTableModel(); 
    String title = "Справочник [Смены]";
    String query = "SELECT id, name, time_start, time_end FROM shift";
    String query1 = "SELECT name FROM shift WHERE id=";
    String query2 = "SELECT time_start FROM shift WHERE id=";
    String query3 = "SELECT time_end FROM shift WHERE id=";
    String query4 = "SELECT id, name FROM shift";    

    
    public SShift() {
        initTable();
    }

    JTable getTable() {
        return table;
    }
    
    String getJournal() {
        return query + " WHERE status = 1";
    }
    String getJournalSmall() {
        return query4;
    }
    String getName(String id) {
        return query1+id;
    }
    String getTimeStart(String id) {
        return query2+id;
    }
    String getTimeEnd(String id) {
        return query3+id;
    }    
    
    DefaultTableModel getModel() {
        return tm;
    } 
    
    void initTable(){        
        tm.addColumn("#");       
        tm.addColumn("Наименование");        
        tm.addColumn("Время начала");
        tm.addColumn("Время конца");
        
        table.setModel(tm);
        
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
