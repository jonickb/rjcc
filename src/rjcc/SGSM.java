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
public class SGSM {
           JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
    DefaultTableModel tm = new DefaultTableModel(); 
    String title = "Справочник [Топливо]";
    String query = "SELECT id, type, mark, code FROM gsm";
    String query1 = "SELECT mark FROM gsm WHERE id=";
    String query2 = "SELECT code FROM gsm WHERE id=";
    static String query3 = "SELECT id, type FROM gsm";
    
    public SGSM() {
        initTable();
    }
    
    String getTitle() {
        return title;
    }
    
    String getMark(String id){
        return query1+id;
    }
    String getCode(String id){
        return query2+id;
    }
    String getGsm(){
        return query3;
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
        tm.addColumn("Название");        
        tm.addColumn("Марка");    
        tm.addColumn("Код марки");    
        
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
