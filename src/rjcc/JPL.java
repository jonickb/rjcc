/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author User
 */


public class JPL {
    ImageIcon doc = new ImageIcon("image/doc.png");
    ImageIcon check = new ImageIcon("image/doccheck.png");     
    ImageIcon del = new ImageIcon("image/docdelete.png"); 
    ImageIcon hlp = new ImageIcon("image/dochelp.png"); 
    StringBuffer query = new StringBuffer();
    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
    TableRowSorter<TableModel> rowSorter;
    
    JccTableCellRenderer renderer = new JccTableCellRenderer();
    
    // settings
    boolean auto = true;
    boolean number = true;
    boolean shift = true;
    boolean driver = true;
    boolean user = true;
    boolean des = true;
    boolean dateStart = true;
    boolean dateStop = true;
    boolean odoStart = true;
    boolean odoStop = true;
    boolean gsmStart = true;
    boolean gsmStop = true;
    boolean gsmGet = true;
    boolean specStart = true;
    boolean specStop = true;
    
    JTable table = new JTable(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };    
    DefaultTableModel tm;
    String title = "Журнал [Путевые листы]";
    
    JPL(){
    query.append("SELECT route.status, route.id, DATE_FORMAT(date, '%d.%m.%Y'), cars.model, cars.number, shift.name, drivers.name, managers.name, coment FROM route ")
        .append(" left JOIN cars ON route.car_id=cars.id")
        .append(" LEFT JOIN drivers ON route.driver_id=drivers.id")
        .append(" LEFT JOIN managers ON route.manager_id=managers.id")
        .append(" LEFT JOIN shift ON route.shift=shift.id ORDER BY date");
        //initTable();
    }
        
    JTable getTable() {
        return table;
    }    
    
   String getTitle() {
       return title;
   }
   
   TableRowSorter getSorter() {
       return rowSorter;
   }
   
   void setRowFilter(String text, int col) {              
       if (text != null)
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));       
       else
        rowSorter.setRowFilter(null);       
   }
    
    String getJournal(String sc, String sd, Calendar sdate, Calendar edate){
        table.setRowSorter(rowSorter);
        String date1;
        String date2;
        String closed;
        String deleted;
        query.setLength(0);
        
        if (sdate != null)
            date1 = "'"+sdf.format(sdate.getTime())+"'";
        else
            date1 = "''";
        if (edate != null)
            date2 = "'"+sdf.format(edate.getTime())+"'";
        else
            date2 = "'"+sdf.format(new Date())+"'";
        if (sc.equals("no"))
            closed = " AND route.status<>1 ";
        else
            closed = "";
        if (sd.equals("no"))
            deleted = " AND route.status<>3";
        else
            deleted = "";
        query.append("SELECT route.status, route.id, DATE_FORMAT(date, '%d.%m.%Y')");
        if (getAuto())
            query.append(", cars.model");
        if (getNumber())
            query.append(", cars.number");
        if (getDriver())
            query.append(", drivers.name");
        if (getDateStart())
            query.append(", DATE_FORMAT(date_start, '%d.%m.%Y')");
        if (getDateStop())
            query.append(", DATE_FORMAT(date_end, '%d.%m.%Y')");
        if (getOdoStart())
            query.append(", odo_start");
        if (getOdoStop())
            query.append(", odo_end");        
        if (getGsmStart())
            query.append(", gsm_start");
        if (getGsmStop())
            query.append(", gsm_end");
        if (getGsmGet())
            query.append(", gsm_get");        
        if (getSpecStart())
            query.append(", special_start");
        if (getSpecStop())
            query.append(", special_end");
        if (getShift())
            query.append(", shift.name");        
        if (getUser())
            query.append(", managers.name");
        if (getDes())
            query.append(", coment");        
        
        query.append(" FROM route")                             
            .append(" left JOIN cars ON route.car_id=cars.id")
            .append(" LEFT JOIN drivers ON route.driver_id=drivers.id")
            .append(" LEFT JOIN managers ON route.manager_id=managers.id")
            .append(" LEFT JOIN shift ON route.shift=shift.id")
            .append(" WHERE route.date>=").append(date1)
            .append(" AND route.date<=").append(date2)
            .append(closed).append(deleted).append(" ORDER BY date");
        return query.toString();
    }
    DefaultTableModel getModel(){
        return tm;
    }
    ImageIcon getImage(String i){
        switch (i){
            case "0":
                return doc;
            case "1":
                return check;
            case "3":
                return del;
            default:
                return hlp;
            }
    }
    boolean isDeleted(String image) {                
            return image.equals(del.getDescription());        
    }
    // ********************* GETTER ********************************************
    boolean getAuto() {
        return auto;
    }
    boolean getNumber() {
        return number;
    }    
    boolean getShift() {
        return shift;
    }    
    boolean getDriver() {
        return driver;
    }
    boolean getUser() {
        return user;
    }
    boolean getDes() {
        return des;
    }
    boolean getDateStart() {
        return dateStart;
    }
    boolean getDateStop() {
        return dateStop;
    }
    boolean getOdoStart() {
        return odoStart;
    }
    boolean getOdoStop() {
        return odoStop;
    }
    boolean getGsmStart() {
        return gsmStart;
    }
    boolean getGsmStop() {
        return gsmStop;
    }
    boolean getGsmGet() {
        return gsmGet;
    }
    boolean getSpecStart() {
        return specStart;
    }
    boolean getSpecStop() {
        return specStop;
    } 
    
    // ****************  SETTER ************************************************
    void setAuto(boolean val) {
        auto = val;
    }    
    void setNumber(boolean val) {
        number = val;
    }
    void setShift(boolean val) {
        shift = val;
    }
    void setDriver(boolean val) {
        driver = val;
    }
    void setUser(boolean val) {
        user = val;
    }
    void setDes(boolean val) {
        des = val;
    }
    void setDateStart(boolean val) {
        dateStart = val;
    }
    void setDateStop(boolean val) {
        dateStop = val;
    }
    void setOdoStart(boolean val) {
        odoStart = val;
    }
    void setOdoStop(boolean val) {
        odoStop = val;
    }
    void setGsmStart(boolean val) {
        gsmStart = val;
    }
    void setGsmStop(boolean val) {
        gsmStop = val;
    }
    void setGsmGet(boolean val) {
        gsmGet = val;
    }
    void setSpecStart(boolean val) {
        specStart = val;
    }
    void setSpecStop(boolean val) {
        specStop = val;
    }    
    
        
    void initTable(){
        
        Vector cols = new Vector();
            cols.addElement("");        
            cols.addElement("#");       
            cols.addElement("Дата");
            if (getAuto())
                cols.addElement("Автомобиль");
            if (getNumber())
                cols.addElement("Гос.номер");
            if (getDriver())
                cols.addElement("Водитель");
            if (getDateStart())
                cols.addElement("Дата выезда");
            if (getDateStop())
                cols.addElement("Дата возвращения");
            if (getOdoStart())
                cols.addElement("Спидометр выезда");
            if (getOdoStop())
                cols.addElement("Спидометр возврата");
            if (getGsmStart())
                cols.addElement("ГСМ выезда");
            if (getGsmStop())
                cols.addElement("ГСМ возврата");
            if (getGsmGet())
                cols.addElement("Дозаправка");
            if (getSpecStart())
                cols.addElement("Моточасы выезда");
            if (getSpecStop())
                cols.addElement("Моточасы возврата");                
            if (getShift())
                cols.addElement("Смена");
            if (getUser())
                cols.addElement("Диспетчер");
            if (getDes())
                cols.addElement("Примечание");              
            
            tm = new DefaultTableModel(cols, 0);
              
        
        table.setModel(tm);
        rowSorter = new TableRowSorter<>(tm);
        table.setRowSorter(rowSorter);                
        
        table.getColumnModel().getColumn(0).setPreferredWidth(18);
        table.getColumnModel().getColumn(0).setMinWidth(18);
        table.getColumnModel().getColumn(0).setMaxWidth(18);        
        table.getColumnModel().getColumn(0).setResizable(false);
        
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setMinWidth(80);
        table.getColumnModel().getColumn(1).setMaxWidth(80);        
        table.getColumnModel().getColumn(1).setResizable(false);

        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMaxWidth(100);        
        table.getColumnModel().getColumn(2).setResizable(false);
        
        for (int i = 0; i < table.getColumnCount(); i++) {            
            switch (table.getColumnName(i)){
                case "Автомобиль":
                    table.getColumnModel().getColumn(i).setPreferredWidth(280);
                    table.getColumnModel().getColumn(i).setMinWidth(280);
                    table.getColumnModel().getColumn(i).setMaxWidth(280);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;
                case "Гос.номер":
                    table.getColumnModel().getColumn(i).setPreferredWidth(120);
                    table.getColumnModel().getColumn(i).setMinWidth(120);
                    table.getColumnModel().getColumn(i).setMaxWidth(120);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;
                case "Водитель":
                    table.getColumnModel().getColumn(i).setPreferredWidth(280);
                    table.getColumnModel().getColumn(i).setMinWidth(280);
                    table.getColumnModel().getColumn(i).setMaxWidth(280);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;                    
                case "Дата выезда":
                    table.getColumnModel().getColumn(i).setPreferredWidth(120);
                    table.getColumnModel().getColumn(i).setMinWidth(120);
                    table.getColumnModel().getColumn(i).setMaxWidth(120);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;  
                case "Дата возвращения":
                    table.getColumnModel().getColumn(i).setPreferredWidth(120);
                    table.getColumnModel().getColumn(i).setMinWidth(120);
                    table.getColumnModel().getColumn(i).setMaxWidth(120);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;    
                case "Спидометр выезда":
                    table.getColumnModel().getColumn(i).setPreferredWidth(100);
                    table.getColumnModel().getColumn(i).setMinWidth(100);
                    table.getColumnModel().getColumn(i).setMaxWidth(100);        
                    table.getColumnModel().getColumn(i).setResizable(false); 
                    table.getColumnModel().getColumn(i).setCellRenderer(renderer);                    
                    break;  
                case "Спидометр возврата":
                    table.getColumnModel().getColumn(i).setPreferredWidth(100);
                    table.getColumnModel().getColumn(i).setMinWidth(100);
                    table.getColumnModel().getColumn(i).setMaxWidth(100);        
                    table.getColumnModel().getColumn(i).setResizable(false);
                    table.getColumnModel().getColumn(i).setCellRenderer(renderer);
                    break;    
                case "ГСМ выезда":
                    table.getColumnModel().getColumn(i).setPreferredWidth(80);
                    table.getColumnModel().getColumn(i).setMinWidth(80);
                    table.getColumnModel().getColumn(i).setMaxWidth(80);        
                    table.getColumnModel().getColumn(i).setResizable(false);
                    table.getColumnModel().getColumn(i).setCellRenderer(renderer);                    
                    break;  
                case "ГСМ возврата":
                    table.getColumnModel().getColumn(i).setPreferredWidth(80);
                    table.getColumnModel().getColumn(i).setMinWidth(80);
                    table.getColumnModel().getColumn(i).setMaxWidth(80);        
                    table.getColumnModel().getColumn(i).setResizable(false);
                    table.getColumnModel().getColumn(i).setCellRenderer(renderer);                    
                    break;     
                case "Дозаправка":
                    table.getColumnModel().getColumn(i).setPreferredWidth(80);
                    table.getColumnModel().getColumn(i).setMinWidth(80);
                    table.getColumnModel().getColumn(i).setMaxWidth(80);        
                    table.getColumnModel().getColumn(i).setResizable(false);
                    table.getColumnModel().getColumn(i).setCellRenderer(renderer);                    
                    break;    
                case "Моточасы выезда":
                    table.getColumnModel().getColumn(i).setPreferredWidth(80);
                    table.getColumnModel().getColumn(i).setMinWidth(80);
                    table.getColumnModel().getColumn(i).setMaxWidth(80);        
                    table.getColumnModel().getColumn(i).setResizable(false);
                    table.getColumnModel().getColumn(i).setCellRenderer(renderer);                    
                    break;  
                case "Моточасы возврата":
                    table.getColumnModel().getColumn(i).setPreferredWidth(80);
                    table.getColumnModel().getColumn(i).setMinWidth(80);
                    table.getColumnModel().getColumn(i).setMaxWidth(80);        
                    table.getColumnModel().getColumn(i).setResizable(false);
                    table.getColumnModel().getColumn(i).setCellRenderer(renderer);                    
                    break;                      
                case "Смена":
                    table.getColumnModel().getColumn(i).setPreferredWidth(150);
                    table.getColumnModel().getColumn(i).setMinWidth(150);
                    table.getColumnModel().getColumn(i).setMaxWidth(150);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;
                case "Диспетчер":
                    table.getColumnModel().getColumn(i).setPreferredWidth(220);
                    table.getColumnModel().getColumn(i).setMinWidth(220);
                    table.getColumnModel().getColumn(i).setMaxWidth(220);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;                                
                case "Примечание":
                    table.getColumnModel().getColumn(i).setPreferredWidth(400);
                    table.getColumnModel().getColumn(i).setMinWidth(400);
                    table.getColumnModel().getColumn(i).setMaxWidth(400);        
                    table.getColumnModel().getColumn(i).setResizable(false);                    
                    break;                                                    
                }                    
            }     
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        
        table.setAutoCreateRowSorter(true);
        table.setFont(MainFrame.tableFont);
        table.setBackground(MainFrame.tableColor);
        table.setGridColor(MainFrame.tableGridColor);
        table.setRowHeight(20);
        table.setSelectionBackground(MainFrame.selectColor);
        table.setSelectionForeground(MainFrame.selectForegroundColor);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);                
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(ImageIcon.class));
        tm.fireTableDataChanged();
    }    
}
