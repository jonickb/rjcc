/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.io.UnsupportedEncodingException;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author User
 */
public class SCars {
               
            JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
    DefaultTableModel tm = new DefaultTableModel(); 
    TableRowSorter<TableModel> rowSorter;
    String title = "Справочник [Транспортные средства]";
    String query = "SELECT cars.id, model, number, type FROM cars LEFT JOIN types ON cars.type_id = types.id";
    String query1 = "SELECT number FROM cars";
    String query2 = "SELECT id FROM cars WHERE number=";
    String query3 = "SELECT model FROM cars WHERE id=";
    String query4 = "SELECT type_id FROM cars WHERE id=";
    String query5 = "SELECT gsm_id FROM cars WHERE id=";
    String query6 = "SELECT driver_id FROM cars WHERE id=";
    String query7 = "SELECT number FROM cars WHERE id=";
    String query8 = "SELECT rashod FROM cars WHERE id=";
    String query9 = "SELECT rashod_special FROM cars WHERE id=";
    String query10 = "SELECT odo_end FROM route WHERE car_id=";
    String query101 = "SELECT special_end FROM route WHERE car_id=";
    String query102 = "SELECT gsm_end FROM route WHERE car_id=";
    String query11 = "SELECT climb FROM cars WHERE id=";  
    String query12 = "SELECT volume FROM cars WHERE id=";
    
    public SCars(){
        initTable();
    }

    JTable getTable() {
        return table;
    }
    
    String getTitle() {
        return title;
    }
    
    String getJournal(){
        this.table.setRowSorter(rowSorter);
        return query + " WHERE cars.status = 1";
    }
    String getNumbers(){
        return query1;
    }
    
    String getCarModel(String id){
        return query3+id;
    }
    
    String getNumber(String id){
        return query7+id;
    }
    
    String getGsm(String id){
        return query5+id;
    }
    String getType(String id){
        return query4+id;
    }
    String getDriver(String id){
        return query6+id;
    }
    
    String getID(String num) throws UnsupportedEncodingException{
        return query2+"'"+num+"'";
    }
    
    String getRashod(String id) {
        return this.query8 + id;
    }
    
    String getRashodSpec(String id) {
        return this.query9 + id;
    }

    // Вот такой костыль пока не придумаю что нить другое (наверное нужно добавить регистры накопления)
    String getMaxID(String id) {
        return "SELECT id FROM route WHERE car_id = "+id+" AND status <> 3 AND odo_end = (SELECT MAX(odo_end) FROM route WHERE car_id="+id+")";
    }
    
    String getLastProbeg(String id) {
        return "SELECT odo_end FROM route WHERE id="+id;
    }
    
    String getLastSpec(String id) {
        return "SELECT special_end FROM route WHERE id="+id;
    }
    
    String getLastGsm(String id) {
        return "SELECT gsm_end FROM route WHERE id="+id;
    }    
    
    
//    String getLastProbeg(String id) {
//        return this.query10 + id + " AND status <> 3 ORDER BY date_end DESC LIMIT 1";
//    }
//    
//    String getLastSpec(String id) {
//        return query101 + id + " AND status <> 3 ORDER BY date_end DESC LIMIT 1";
//    }
//    
//    String getLastGsm(String id) {
//        return query102 + id + " AND status <> 3 ORDER BY date_end DESC LIMIT 1";
//    }
    
    String getClimb(String id) {
        return this.query11 + id;
    }
    
    String getVolume(String id) {
        return this.query12+id;
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
        tm.addColumn("ТС");        
        tm.addColumn("№");
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
