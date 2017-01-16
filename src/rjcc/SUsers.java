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
public class SUsers {
            JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
    DefaultTableModel tm = new DefaultTableModel(); 
    String title = "Справочник [Пользователи]";
    String query = "SELECT id, login, name FROM users";
    static String query1 = "SELECT login FROM users WHERE id=";
    static String query2 = "SELECT name FROM users WHERE id=";
    static String query3 = "SELECT status FROM users WHERE id=";
    String query4 = "SELECT id FROM users WHERE login=";
    
    public SUsers() {
        initTable();
    }

    JTable getTable() {
        return table;
    }
    String getTitle() {
        return title;
    }
    
    String getID(String l) {
        return query4+l;
    }
    String getLogin(String id) {
        return query1+id;
    }
    String getName(String id) {
        return query2+id;
    }
    String getStatus(String id) {
        return query3+id;
    }
    
    String getJournal() {
        return query;
    }
    DefaultTableModel getModel(){
        return tm;
    } 
    void initTable(){        
        tm.addColumn("#");       
        tm.addColumn("Логин");        
        tm.addColumn("Ф.И.О.");        
        
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
