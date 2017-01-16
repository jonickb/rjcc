/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author User
 */
public class SCarDrivers {
            JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
    TableRowSorter<TableModel> rowSorter;
    DefaultTableModel tm = new DefaultTableModel(); 
    String titleDrivers = "Справочник [Водители]";
    String titleCargers = "Справочник [Сопровождающие]";
    String query = "SELECT id, name, phone FROM drivers";
    static String query1 = "SELECT id, name FROM drivers";
    String query2 = "SELECT name FROM drivers WHERE id=";
    String query3 = "Select doc_series FROM drivers WHERE id=";
    String query4 = "SELECT doc_number FROM drivers WHERE id=";
    String query5 = "SELECT category FROM drivers WHERE id=";
    
    int DRIVER = 1;
    int CARGER = 2;
    
    public SCarDrivers(){
        initTable();
    }

    JTable getTable(){
        return table;
    }
    
    String getTitle(int act) {
        switch (act) {
            case 1: 
                return titleDrivers;                
            case 2:
                return titleCargers;                
            default:
                return "Справочник";
        }
    }
    
    int getTitleId(String title) {
        if (title.equals(titleDrivers))
            return DRIVER;
        else
            return CARGER;
    }
    
    String getJournal(int act){        
        table.setRowSorter(rowSorter);
        if (act == 1) {            
            return this.query + " WHERE activity=1 AND status=1 ORDER BY name";
        }        
        return this.query + " WHERE activity=2 AND status=1 ORDER BY name";
    }
    String getNames(int act){
        if (act == 1) {
            return this.query + " WHERE activity=1 AND status=1 ORDER BY name";
        }
        return this.query + " WHERE activity=2 AND status=1 ORDER BY name";
    }
    
    String getName(String id){
        return query2+id;
    }
    String getSeries(String id){
        return query3+id;
    }
    String getNumber(String id){
        return query4+id;
    }
    String getCategory(String id){
        return query5+id;
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
        tm.addColumn("Ф.И.О.");     
        tm.addColumn("Телефон");
        
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
