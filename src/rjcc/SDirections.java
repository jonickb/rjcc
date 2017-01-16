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
public final class SDirections {
           JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
    DefaultTableModel tm = new DefaultTableModel(); 
    String title = "Справочник [Маршруты]";
    String query = "SELECT id, description, distance, addres1, addres2 FROM directions";
    String query1 = "SELECT id, description FROM directions";
    String query2 = "SELECT description FROM directions WHERE id=";
    
    public SDirections() {
        initTable();
    }

    String getTitle() {
        return title;
    }
    
    String getDirections(){
        return query1;
    }
    String getDesc(String id){
        return query2+id;
    }
    
    JTable getTable(){
        return table;
    }
    
    String getJournal(){
        return query + " WHERE status = 1";
    }
    DefaultTableModel getModel(){
        return tm;
    } 
    void initTable(){        
        tm.addColumn("#");       
        tm.addColumn("Наименование");        
        tm.addColumn("Дистанция");
        tm.addColumn("Начальный адрес");
        tm.addColumn("Конечный адрес");
        
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
