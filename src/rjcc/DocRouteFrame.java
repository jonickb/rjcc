/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static rjcc.MainFrame.error;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author User
 */
public final class DocRouteFrame extends javax.swing.JFrame {

    /**
     * Creates new form DocRouteFrame
     */
    DocRoute docRoute;
    SCars catalogCars;
    SCarDrivers catalogCarDrivers;
    SGSM catalogGSM;
    SDirections catalogDirection;
    SShift sShift;
    Connector connector;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm"); 
    String title;
    
    Object yesnoArray[] = { "ДА", "НЕТ" };
    boolean openNew = true;
    
    public DocRouteFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("docrouteico.png")));        
        initComponents();        
    }
    
    public DocRouteFrame(DocRoute dr, Connector con) {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("docrouteico.png")));        
        this.docRoute = dr;
        this.connector = con;
        catalogCars = new SCars();
        catalogCarDrivers = new SCarDrivers();
        catalogGSM = new SGSM();
        catalogDirection = new SDirections();
        sShift = new SShift();
        initComponents();
        
        if (dr.isSaved())
            fillDRWindow(); 
        else 
            initDocument();
        this.setTitle(docRoute.getTitle());
        openNew = false;
    }
    // ***************************** Инициализация пустого документа ***********************************************
    void initDocument() {
        
        docRoute.setDate(dateChooserCombo1.getSelectedDate());
            // установить значения даты в поля выезда и заезда
            dateChooserCombo2.setSelectedDate(dateChooserCombo1.getSelectedDate());
            dateChooserCombo3.setSelectedDate(dateChooserCombo1.getSelectedDate()); 
        
    }
    
    // ***************************** Заполнить интерфейс данными документа *****************************************
    void fillDRWindow(){
            jLabel35.setText(docRoute.getRouteId());            
            
            dateChooserCombo1.setSelectedDate(docRoute.getRDate());            
            jTextField2.setText(connector.getOne(catalogCars.getNumber(docRoute.getCarID())).toString());
            jLabel33.setText("[ "+connector.getOne(catalogCars.getCarModel(docRoute.getCarID()))+" ]");            
            jTextField3.setText(connector.getOne(catalogCarDrivers.getName(docRoute.getDriverId())).toString());
            jTextField4.setText(connector.getOne(catalogCarDrivers.getName(docRoute.getSopr1())).toString());
            jTextField5.setText(connector.getOne(catalogCarDrivers.getName(docRoute.getSopr2())).toString());
            jLabel9.setText(connector.getOne(catalogCarDrivers.getSeries(docRoute.getDriverId()))+" "+connector.getOne(catalogCarDrivers.getNumber(docRoute.getDriverId())));
            jLabel34.setText(connector.getOne(catalogCarDrivers.getCategory(docRoute.getDriverId())).toString());
            dateChooserCombo2.setSelectedDate(docRoute.getDateStart());
            dateChooserCombo3.setSelectedDate(docRoute.getDateEnd());

            if (!docRoute.getTimeStart().equals("  :  "))
                jFormattedTextField1.setText(docRoute.getTimeStart());
            if (!docRoute.getTimeEnd().equals("  :  "))
                jFormattedTextField2.setText(docRoute.getTimeEnd());
            jFormattedTextField5.setText(docRoute.getOdoStart().replace('.', ','));
            jFormattedTextField6.setText(docRoute.getOdoEnd().replace('.', ','));
            if (!docRoute.getTimefEnd().equals("  :  "))
                jFormattedTextField3.setText(docRoute.getTimefStart());            
            if (!docRoute.getTimefEnd().equals("  :  "))
                jFormattedTextField4.setText(docRoute.getTimefEnd());            
            String gsmid = connector.getOne(catalogCars.getGsm(docRoute.getCarID())).toString();
            jTextField14.setText(connector.getOne(catalogGSM.getMark(gsmid)).toString());
            jTextField15.setText(connector.getOne(catalogGSM.getCode(gsmid)).toString());
            jFormattedTextField7.setText(docRoute.getGSMGet().replace('.', ','));
            jFormattedTextField8.setText(docRoute.getGsmStart().replace('.', ','));
            jFormattedTextField10.setText(docRoute.getGsmEnd().replace('.', ','));
            jFormattedTextField9.setText(docRoute.getSpecStart().replace('.', ','));
            jFormattedTextField11.setText(docRoute.getSpecEnd().replace('.', ','));
            jTextField21.setText(connector.getOne(catalogDirection.getDesc(docRoute.getDirection())).toString());
            jTextField22.setText(docRoute.getGruz());
            jFormattedTextField12.setText(docRoute.getEzdki().replace('.', ','));
            jFormattedTextField13.setText(docRoute.getDist().replace('.', ','));
            jFormattedTextField14.setText(docRoute.getVolume().replace('.', ','));
            jTextField26.setText(docRoute.getComment());
            String tid= connector.getOne(catalogCars.getType(docRoute.getCarID())).toString();            
            jLabel40.setText(connector.getOne(SCarType.query2+tid).toString()); 
            jTextField13.setText(connector.getOne(sShift.getName(docRoute.getShift())).toString());
                
            if (docRoute.isClosed() || docRoute.isDeleted()){
                jComboBox1.setEnabled(false);
                dateChooserCombo1.setEnabled(false);
                jButton11.setEnabled(false);
                jButton12.setEnabled(false);
                jButton13.setEnabled(false);
                jButton14.setEnabled(false);
                dateChooserCombo2.setEnabled(false);
                dateChooserCombo3.setEnabled(false);
                jFormattedTextField1.setEditable(false);
                jFormattedTextField2.setEditable(false);
                jFormattedTextField3.setEditable(false);
                jFormattedTextField4.setEditable(false);
                jFormattedTextField5.setEditable(false);
                jFormattedTextField6.setEditable(false);
                jFormattedTextField7.setEditable(false);
                jFormattedTextField8.setEditable(false);
                jFormattedTextField9.setEditable(false);
                jFormattedTextField10.setEditable(false);
                jFormattedTextField11.setEditable(false);
                jFormattedTextField12.setEditable(false);
                jFormattedTextField13.setEditable(false);
                jFormattedTextField14.setEditable(false);
                jComboBox3.setEnabled(false);
                jButton15.setEnabled(false);
                jTextField22.setEditable(false);
            } else {
                jComboBox1.setEnabled(true);
                dateChooserCombo1.setEnabled(true);
                jButton11.setEnabled(true);
                jButton12.setEnabled(true);
                jButton13.setEnabled(true);
                jButton14.setEnabled(true);
                dateChooserCombo2.setEnabled(true);
                dateChooserCombo3.setEnabled(true);                
                jFormattedTextField1.setEditable(true);
                jFormattedTextField2.setEditable(true);
                jFormattedTextField3.setEditable(true);
                jFormattedTextField4.setEditable(true);
                jFormattedTextField5.setEditable(true);
                jFormattedTextField6.setEditable(true);
                jFormattedTextField7.setEditable(true);
                jFormattedTextField8.setEditable(true);
                jFormattedTextField9.setEditable(true);
                jFormattedTextField10.setEditable(true);
                jFormattedTextField11.setEditable(true);
                jFormattedTextField12.setEditable(true);
                jFormattedTextField13.setEditable(true);
                jFormattedTextField14.setEditable(true);
                jComboBox3.setEnabled(true);
                jButton15.setEnabled(true);
                jTextField22.setEditable(true);                
            }
        this.setProbeg();
        this.setSpecial();
        this.setGSM();
        this.setfGSM();
        this.setGSMspec();
        this.setTime();
        this.setfTime(); 
        this.setClimb();
        this.setAll();
    }    
 // ****************  заполгить документ данными из окна ***********************
    void fillDocRouteFromWindow(){
        System.out.println("FILL Document values");

            docRoute.setOrg("1");
            try {                
                  docRoute.setDate(dateChooserCombo1.getSelectedDate());
            } catch (Exception e) {docRoute.setDate(null);}
            try{
                docRoute.setDateStart(dateChooserCombo2.getSelectedDate());
            } catch (Exception e){docRoute.setDateStart(null);}
            try {
                docRoute.setDateEnd(dateChooserCombo3.getSelectedDate());
            } catch (Exception e) {docRoute.setDateEnd(null);}
                
                docRoute.setTimeStart(jFormattedTextField1.getText());
                docRoute.setTimeEnd(jFormattedTextField2.getText());
                docRoute.setOdoStart(jFormattedTextField5.getText());
                docRoute.setOdoEnd(jFormattedTextField6.getText());
                docRoute.setTimefStart(jFormattedTextField3.getText());
                docRoute.setTimefEnd(jFormattedTextField4.getText());
                docRoute.setGSMGet(jFormattedTextField7.getText());
                docRoute.setGSMStart(jFormattedTextField8.getText());
                docRoute.setGSMEnd(jFormattedTextField10.getText());
                docRoute.setSpecStart(jFormattedTextField9.getText());
                docRoute.setSpecEnd(jFormattedTextField11.getText());
                docRoute.setGruz(jTextField22.getText());
                docRoute.setEzdki(jFormattedTextField12.getText());
                docRoute.setDist(jFormattedTextField13.getText());
                docRoute.setVolume(jFormattedTextField14.getText());
                docRoute.setComment(jTextField26.getText());        
    }
// ***************************** ЗАпись в Exel **********************************
    void drToExel(File file, int form, boolean print){
        if (!file.getAbsolutePath().endsWith(".xls")) file = new File(file+".xls");
        
        Workbook originalworkbook;
        WritableWorkbook copywb;
        Label label;
        if (form == 0){                        
            try {
                originalworkbook = Workbook.getWorkbook(new File("sheet/putevoi-list-f4c.tplx"));
                copywb = Workbook.createWorkbook(file, originalworkbook);

                WritableFont font = new WritableFont(WritableFont.ARIAL, 8);                
                WritableCellFormat cf = new WritableCellFormat(font);
                WritableCellFormat cfc = new WritableCellFormat(font);
                cfc.setAlignment(Alignment.CENTRE);
                WritableCellFormat cblt = new WritableCellFormat(font);
                cblt.setAlignment(Alignment.CENTRE); 
                        cblt.setBorder(Border.LEFT, BorderLineStyle.MEDIUM, Colour.BLACK);
                        cblt.setBorder(Border.TOP, BorderLineStyle.MEDIUM, Colour.BLACK);
                        cblt.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
                        cblt.setBorder(Border.RIGHT, BorderLineStyle.THIN);   
                WritableCellFormat cblb = new WritableCellFormat(font);
                cblb.setAlignment(Alignment.CENTRE); 
                        cblb.setBorder(Border.LEFT, BorderLineStyle.MEDIUM, Colour.BLACK);
                        cblb.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                        cblb.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
                        cblb.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                WritableCellFormat cbtn = new WritableCellFormat(font);
                cbtn.setAlignment(Alignment.CENTRE); 
                        cbtn.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                        cbtn.setBorder(Border.TOP, BorderLineStyle.MEDIUM, Colour.BLACK);
                        cbtn.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
                        cbtn.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                WritableCellFormat cbbn = new WritableCellFormat(font);
                cbbn.setAlignment(Alignment.CENTRE); 
                        cbbn.setBorder(Border.LEFT, BorderLineStyle.THIN, Colour.BLACK);
                        cbbn.setBorder(Border.TOP, BorderLineStyle.THIN, Colour.BLACK);
                        cbbn.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
                        cbbn.setBorder(Border.RIGHT, BorderLineStyle.THIN);                         
                
                label = new Label(100, 2, jLabel35.getText());   //номер            
                copywb.getSheet(0).addCell(label);
                    calendar = dateChooserCombo1.getSelectedDate();
                    label = new Label(58, 4, ""+calendar.get(Calendar.DAY_OF_MONTH)); // день
                    copywb.getSheet(0).addCell(label);
                    label = new Label(67, 4, strMonth(calendar.get(Calendar.MONTH))); // месяц
                    copywb.getSheet(0).addCell(label);           
                    label = new Label(89, 4, ""+calendar.get(Calendar.YEAR)); // год
                    copywb.getSheet(0).addCell(label);
                
                label = new Label(16, 5, jComboBox1.getSelectedItem().toString()); // организация
                copywb.getSheet(0).addCell(label);
                label = new Label(16, 12, jLabel33.getText().substring(1, (jLabel33.getText().length()-1)), cf); // марка автомобиля
                copywb.getSheet(0).addCell(label);
                label = new Label(27, 13, jTextField2.getText(), cf); // госномер
                copywb.getSheet(0).addCell(label);
                label = new Label(8, 14, jTextField3.getText(), cf); // водитель
                copywb.getSheet(0).addCell(label);                
                label = new Label(15, 16, jLabel9.getText(), cf); // номер удостоверения
                copywb.getSheet(0).addCell(label);         
                label = new Label(44, 16, jLabel34.getText(), cf); // класс
                copywb.getSheet(0).addCell(label);          
                label = new Label(20, 29, jTextField4.getText(), cf); // сопровождающий 1
                copywb.getSheet(0).addCell(label);
                label = new Label(0, 30, jTextField5.getText() , cf); // сопровождающий 2
                copywb.getSheet(0).addCell(label);                          
                
                    calendar = dateChooserCombo2.getSelectedDate();
                    label = new Label(116, 13, ""+calendar.get(Calendar.DAY_OF_MONTH), cblt); // день выезда
                    copywb.getSheet(0).addCell(label);
                    label = new Label(123, 13, ""+(calendar.get(Calendar.MONTH)+1), cbbn); // месяц выезда
                    copywb.getSheet(0).addCell(label);
                    calendar = dateChooserCombo3.getSelectedDate();
                    label = new Label(116, 14, ""+calendar.get(Calendar.DAY_OF_MONTH), cblb); // день возвращения
                    copywb.getSheet(0).addCell(label);
                    label = new Label(123, 14, ""+(calendar.get(Calendar.MONTH)+1), cbbn); // месяц возвращения
                    copywb.getSheet(0).addCell(label);
                
                label = new Label(130, 13, getHour(jFormattedTextField1.getText()), cbtn); // час выезда
                copywb.getSheet(0).addCell(label);
                label = new Label(137, 13, getMinute(jFormattedTextField1.getText()), cbtn); // минута выезда
                copywb.getSheet(0).addCell(label);  
                label = new Label(151, 13, jFormattedTextField5.getText(), cbtn); // спидометр при выезде
                copywb.getSheet(0).addCell(label);                 
                label = new Label(163, 13, jFormattedTextField3.getText(), cfc); // время возвращения фактическое
                copywb.getSheet(0).addCell(label);                
                label = new Label(130, 14, getHour(jFormattedTextField2.getText()), cbbn); // час возвращения
                copywb.getSheet(0).addCell(label);                                
                label = new Label(137, 14, getMinute(jFormattedTextField2.getText()), cbbn); // минуты возвращения
                copywb.getSheet(0).addCell(label);                                
                label = new Label(151, 14, jFormattedTextField6.getText(), cbbn); // спидометр при возвращении
                copywb.getSheet(0).addCell(label);             
                label = new Label(163, 14, jFormattedTextField4.getText(), cfc); // время возвращения фактическое
                copywb.getSheet(0).addCell(label);                                
                label = new Label(101, 23, jTextField14.getText(), cblt); // марка топлива
                copywb.getSheet(0).addCell(label);                                
                label = new Label(110, 23, jTextField15.getText(), cbtn); // код марки
                copywb.getSheet(0).addCell(label);
                label = new Label(118, 23, jFormattedTextField7.getText(), cbtn); // выдано топлива
                copywb.getSheet(0).addCell(label);             
                label = new Label(127, 23, jFormattedTextField8.getText(), cbtn); // остаток топлива при выезде
                copywb.getSheet(0).addCell(label);                
                label = new Label(136, 23, jFormattedTextField10.getText(), cbtn); // остаток топлива привозвращении
                copywb.getSheet(0).addCell(label);                                
                label = new Label(166, 23, jFormattedTextField9.getText(), cbtn); // спецоборудование при выезде
                copywb.getSheet(0).addCell(label);                
                label = new Label(166, 25, jFormattedTextField11.getText(), cbbn); // спецоборудование при возвращении
                copywb.getSheet(0).addCell(label);                                
                label = new Label(0, 36, jComboBox3.getSelectedItem().toString(), cf); // заказчик
                copywb.getSheet(0).addCell(label);                              
                label = new Label(45, 36, jTextField21.getText(), cf); // маршрут
                copywb.getSheet(0).addCell(label);    
                label = new Label(120, 36, jTextField22.getText(), cfc); // груз
                copywb.getSheet(0).addCell(label);                                
                label = new Label(157, 36, jFormattedTextField12.getText(), cfc); // ездки
                copywb.getSheet(0).addCell(label);              
                label = new Label(167, 36, jFormattedTextField13.getText(), cbtn); // расстояние
                copywb.getSheet(0).addCell(label);              
                label = new Label(177, 36, jFormattedTextField14.getText(), cfc); // перевезено
                copywb.getSheet(0).addCell(label);                                
                label = new Label(21, 45, MainFrame.uname, cfc); // Диспетчер
                copywb.getSheet(0).addCell(label);                                                
                
                
                copywb.write();                
                originalworkbook.close();
                copywb.close();                 
            } catch (IOException | BiffException | WriteException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}           
        }
        if (form == 2){
            try {
                originalworkbook = Workbook.getWorkbook(new File("sheet/putevoi-list-f3.tplx"));
                copywb = Workbook.createWorkbook(file, originalworkbook);            
                label = new Label(75, 3, jLabel35.getText());   //номер            
                copywb.getSheet(0).addCell(label);               
                WritableFont font = new WritableFont(WritableFont.ARIAL, 8);                
                WritableCellFormat cf = new WritableCellFormat(font);
                WritableCellFormat cfc = new WritableCellFormat(font);
                cfc.setAlignment(Alignment.CENTRE);                
                //if (jDateChooser1.getDate() != null){
                    calendar = dateChooserCombo1.getSelectedDate();
                    //calendar.setTime(jDateChooser1.getDate());
                    label = new Label(29, 4, ""+calendar.get(Calendar.DAY_OF_MONTH), cfc); // день
                    copywb.getSheet(0).addCell(label);
                    label = new Label(34, 4, strMonth(calendar.get(Calendar.MONTH)), cf); // месяц
                    copywb.getSheet(0).addCell(label);           
                    label = new Label(46, 4, ""+calendar.get(Calendar.YEAR), cf); // год
                    copywb.getSheet(0).addCell(label);
                label = new Label(17, 7, jComboBox1.getSelectedItem().toString(), cf); // организация
                copywb.getSheet(0).addCell(label);
                label = new Label(21, 9, jLabel33.getText().substring(1, (jLabel33.getText().length()-1)), cf); // марка автомобиля
                copywb.getSheet(0).addCell(label);                
                label = new Label(34, 10, jTextField2.getText(), cf); // госномер
                copywb.getSheet(0).addCell(label);                
                label = new Label(12, 11, jTextField3.getText(), cf); // водитель
                copywb.getSheet(0).addCell(label);     
                label = new Label(67, 25, jTextField3.getText(), cf); // водитель
                copywb.getSheet(0).addCell(label);                
                label = new Label(18, 13, jLabel9.getText(), cf); // номер удостоверения
                copywb.getSheet(0).addCell(label);         
                label = new Label(66, 13, jLabel34.getText(), cf); // класс
                copywb.getSheet(0).addCell(label);     
                label = new Label(72, 18, jFormattedTextField5.getText(), cfc); // спидометр при выезде
                copywb.getSheet(0).addCell(label);   
                label = new Label(57, 28, jTextField14.getText(), cfc); // марка топлива
                copywb.getSheet(0).addCell(label);                                
                label = new Label(71, 28, jTextField15.getText(), cfc); // код марки
                copywb.getSheet(0).addCell(label);    
                label = new Label(71, 33, jFormattedTextField7.getText(), cfc); // выдано топлива
                copywb.getSheet(0).addCell(label);   
                label = new Label(71, 36, jFormattedTextField8.getText(), cfc); // остаток топлива при выезде
                copywb.getSheet(0).addCell(label);                
                label = new Label(71, 37, jFormattedTextField10.getText(), cfc); // остаток топлива привозвращении
                copywb.getSheet(0).addCell(label);   
                label = new Label(0, 21, jComboBox3.getSelectedItem().toString(), cf); // заказчик
                copywb.getSheet(0).addCell(label);           
                label = new Label(17, 25, jTextField21.getText(), cf); // маршрут
                copywb.getSheet(0).addCell(label); 
                label = new Label(30, 29, jFormattedTextField1.getText(), cfc); // время выезда
                copywb.getSheet(0).addCell(label);    
                label = new Label(32, 34, jFormattedTextField2.getText(), cfc); // время возвращения
                copywb.getSheet(0).addCell(label);                   
                label = new Label(71, 44, jFormattedTextField6.getText(), cfc); // спидометр при возвращении
                copywb.getSheet(0).addCell(label);    
                
                copywb.write();                
                originalworkbook.close();
                copywb.close();                  
                //}                
            } catch (IOException | BiffException | WriteException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
        }
//        if (print){
//            try {
//                Desktop.getDesktop().print(file);
//            } catch (IOException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
//        }
    }
    // *************************************************    
    void printFront(int form){
        File tmp = new File("tmp/tmp.xls");
        
        HSSFWorkbook book1 = null;      
        
        try {
            switch (form){
                case 0:
                    book1 = new HSSFWorkbook(new FileInputStream("sheet/putevoi-list-f4c.tplx"));
                    break;
                case 1:
                    book1 = new HSSFWorkbook(new FileInputStream("sheet/putevoi-list-f4p.tplx"));
                    break;
                case 2:
                    book1 = new HSSFWorkbook(new FileInputStream("sheet/putevoi-list-f3.tplx"));
                    break;                    
            }
            book1.removeSheetAt(1);
            try (FileOutputStream out = new FileOutputStream(tmp)) {
                book1.write(out);
            }
            book1.close();
            Desktop.getDesktop().print(tmp);
            tmp.delete();
        } catch (IOException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
    }
    void printBack(int form){
        File tmp = new File("tmp/tmp.xls");
        
        HSSFWorkbook book1 = null;      
        
        try {
            switch (form){
                case 0:
                    book1 = new HSSFWorkbook(new FileInputStream("sheet/putevoi-list-f4c.tplx"));
                    break;
                case 1:
                    book1 = new HSSFWorkbook(new FileInputStream("sheet/putevoi-list-f4p.tplx"));
                    break;
                case 2:
                    book1 = new HSSFWorkbook(new FileInputStream("sheet/putevoi-list-f3.tplx"));
                    break;                    
            }
            book1.removeSheetAt(0);
            try (FileOutputStream out = new FileOutputStream(tmp)) {
                book1.write(out);
            }
            book1.close();
            Desktop.getDesktop().print(tmp);
            tmp.delete();
        } catch (IOException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
    }    
    // *************************************************
    String strMonth(int i){
        switch(i){
            case 0:
                return "января";
            case 1:
                return "февраля";
            case 2:
                return "марта";
            case 3:
                return "апреля";
            case 4:
                return "мая";
            case 5:
                return "июня";
            case 6:
                return "июля";
            case 7:
                return "августа";
            case 8:
                return "сентября";
            case 9:
                return "октября";
            case 10:
                return "ноября";
            case 11:
                return "декабря";                
            default:
                return "";
        }
    }
    String getHour(String s){
        int i = s.indexOf(":");
        return s.substring(0, i);
    }
    String getMinute(String s){
        int i = s.indexOf(":");
        return s.substring(i+1);
    }   
    
    // *********************  заполнить расчетные данные
    
    // Установить пробег
    void setProbeg() {
        float p2, p1, probeg;
        if (this.jFormattedTextField6.getText().equals("")) 
            p2 = 0;
        else
            p2 = Float.parseFloat(this.jFormattedTextField6.getText().replace(",", "."));        
        if (this.jFormattedTextField5.getText().equals(""))
            p1 = 0;
        else
            p1 = Float.parseFloat(this.jFormattedTextField5.getText().replace(",", "."));        
        probeg = p2 - p1;
        
        if (probeg < 0.0f) {
            this.jTextField6.setForeground(Color.red);
        } else {
            this.jTextField6.setForeground(Color.black);
        }
        this.jTextField6.setText(new BigDecimal(probeg).setScale(2, RoundingMode.HALF_UP).toString());
        this.jFormattedTextField13.setText(new BigDecimal(probeg).setScale(2, RoundingMode.HALF_UP).toString());
        this.setAll();
    }
    // установить моточасы
    void setSpecial() {
        if (this.jFormattedTextField11.getText().equals("") || this.jFormattedTextField9.getText().equals("")) {
            return;
        }
        float s2, s1, spec;
        if (this.jFormattedTextField11.getText().equals(""))
            s2 = 0;
        else
            s2 = Float.parseFloat(this.jFormattedTextField11.getText().replace(",", "."));
        if (this.jFormattedTextField9.getText().equals(""))
            s1 = 0;
        else
            s1 = Float.parseFloat(this.jFormattedTextField9.getText().replace(",", "."));
        spec = s2 - s1;
        if (spec < 0.0f) {
            this.jTextField1.setForeground(Color.red);
        } else {
            this.jTextField1.setForeground(Color.black);
        }
        this.jTextField1.setText(new BigDecimal(spec).setScale(2, RoundingMode.HALF_UP).toString());
        this.setAll();
    }
    // Установить ГСМ по факту
    void setfGSM() {
        float add, r1, r2;
        
        if (!this.jFormattedTextField7.getText().equals("")) {
            add = Float.parseFloat(this.jFormattedTextField7.getText().replace(",", "."));
        } else
            add = 0;
        
        if (!this.jFormattedTextField8.getText().equals("")) {
            r1 = Float.parseFloat(this.jFormattedTextField8.getText().replace(",", "."));
        } else
            r1 = 0;
        
        if (!this.jFormattedTextField10.getText().equals("")) {
            r2 = Float.parseFloat(this.jFormattedTextField10.getText().replace(",", "."));
        } else
            r2 = 0;        
        
        float gsm = add + r1 - r2;
        
        if (gsm < 0.0f) {
            this.jTextField10.setForeground(Color.red);
        } else {
            this.jTextField10.setForeground(Color.black);
        }
        this.jTextField10.setText(new BigDecimal(gsm).setScale(2, RoundingMode.HALF_UP).toString());
        this.setAll();
    }
    // установить расход по норме
    void setGSM() {        
        if (!this.jTextField6.getText().equals("") && !docRoute.getRashod().equals("")) {                        
            float gsm = Float.parseFloat(this.jTextField6.getText()) * Float.parseFloat(docRoute.getRashod()) / 100;
            if (gsm < 0.0f) {
                    this.jTextField9.setForeground(Color.red);
                } else {
                    this.jTextField9.setForeground(Color.black);
                }            
            this.jTextField9.setText(new BigDecimal(gsm).setScale(2, RoundingMode.HALF_UP).toString());            
        } else jTextField9.setText("0.00");
        setAll();
    }
    // установить расход спецоборудования
    void setGSMspec() {
        if (!this.jTextField1.getText().equals("") && !docRoute.getRashodSpec().equals("")) {
            float spec = Float.parseFloat(this.jTextField1.getText()) * Float.parseFloat(docRoute.getRashodSpec());
            this.jTextField12.setText(new BigDecimal(spec).setScale(2, RoundingMode.HALF_UP).toString());
        } else jTextField12.setText("0.00");
        this.setAll();
    }
    // Установть подъем
    void setClimb() {
        if (!jFormattedTextField12.getText().equals("") && !docRoute.getClimb().equals("")) {
            float climb = Float.parseFloat(jFormattedTextField12.getText().replace(",", ".")) * Float.parseFloat(docRoute.getClimb());
            this.jTextField16.setText(new BigDecimal(climb).setScale(3, RoundingMode.HALF_UP).toString());
        } else jTextField16.setText("0.000");
        setAll();
    }
    
    void setTime() {
        Date dS = null;
        Date dE = null;
        Date tS = null;
        Date tE = null;
        if (this.dateChooserCombo2.getSelectedDate() != null && this.dateChooserCombo3.getSelectedDate() != null && !this.jFormattedTextField1.getText().equals("  :  ") && !this.jFormattedTextField2.getText().equals("  :  ")) {
            try {
                dS = this.sdf.parse(this.dateChooserCombo2.getText());
                dE = this.sdf.parse(this.dateChooserCombo3.getText());
                tS = this.sdf3.parse(this.jFormattedTextField1.getText());
                tE = this.sdf3.parse(this.jFormattedTextField2.getText());
            }
            catch (ParseException e) {
                System.err.println("Error: " + e.getMessage());
            }
            Calendar dayStart = Calendar.getInstance();
            Calendar dayEnd = Calendar.getInstance();            
            Calendar tmp1 = Calendar.getInstance();
            Calendar tmp2 = Calendar.getInstance();                
                
            
            dayStart.setTime(dS);
            dayEnd.setTime(dE);
            long days = dayEnd.getTimeInMillis() - dayStart.getTimeInMillis();                                               
            
            tmp1.setTimeInMillis(dayStart.getTimeInMillis());
            tmp2.setTimeInMillis(dayEnd.getTimeInMillis());
            
            dayStart.setTime(tS);
            dayEnd.setTime(tE);  
            
            dayStart.set(Calendar.DAY_OF_MONTH, tmp1.get(Calendar.DAY_OF_MONTH));
            dayStart.set(Calendar.MONTH, tmp1.get(Calendar.MONTH));
            dayStart.set(Calendar.YEAR, tmp1.get(Calendar.YEAR));
            
            dayEnd.set(Calendar.DAY_OF_MONTH, tmp2.get(Calendar.DAY_OF_MONTH));
            dayEnd.set(Calendar.MONTH, tmp2.get(Calendar.MONTH));
            dayEnd.set(Calendar.YEAR, tmp2.get(Calendar.YEAR));            
            
            
            long diff = dayEnd.getTimeInMillis() - dayStart.getTimeInMillis();
            long day = days / 86400000;
            long hours = diff / 3600000;
            long minutes = diff / 60000;
            if (hours < 0 || minutes < 0)
                jTextField8.setForeground(Color.red);
            else
                jTextField8.setForeground(Color.black);            
            this.jTextField8.setText("" + hours + ":" + minutes % 60);
            //this.jTextField8.setText("" + (24 * day + hours) + ":" + minutes % 60);
        }
    }
    
    void setAll() {
        float motor = !this.jTextField9.getText().equals("") ? Float.parseFloat(this.jTextField9.getText()) : 0.0f;
        
        float spec = !this.jTextField12.getText().equals("") ? Float.parseFloat(this.jTextField12.getText()) : 0.0f;

        float climb = !this.jTextField16.getText().equals("") ? Float.parseFloat(this.jTextField16.getText()) : 0.0f;

        float all = motor + spec + climb;
        this.jTextField11.setText(new BigDecimal(all).setScale(2, RoundingMode.HALF_UP).toString());
    }
    
    void setfTime() {
        Date dS = null;
        Date dE = null;
        Date tS = null;
        Date tE = null;
        if (this.dateChooserCombo2.getSelectedDate() != null && this.dateChooserCombo3.getSelectedDate() != null && !this.jFormattedTextField3.getText().equals("  :  ") && !this.jFormattedTextField4.getText().equals("  :  ")) {
            try {
                dS = this.sdf.parse(this.dateChooserCombo2.getText());
                dE = this.sdf.parse(this.dateChooserCombo3.getText());
                tS = this.sdf3.parse(this.jFormattedTextField3.getText());
                tE = this.sdf3.parse(this.jFormattedTextField4.getText());
            }
            catch (ParseException e) {
                System.err.println("Error: " + e.getMessage());
            }
            Calendar dayStart = Calendar.getInstance();
            Calendar dayEnd = Calendar.getInstance();
            Calendar tmp1 = Calendar.getInstance();
            Calendar tmp2 = Calendar.getInstance(); 
                        
            dayStart.setTime(dS);
            dayEnd.setTime(dE);
            long days = dayEnd.getTimeInMillis() - dayStart.getTimeInMillis();
            
            tmp1.setTimeInMillis(dayStart.getTimeInMillis());
            tmp2.setTimeInMillis(dayEnd.getTimeInMillis());
            
            dayStart.setTime(tS);
            dayEnd.setTime(tE);
            
            dayStart.set(Calendar.DAY_OF_MONTH, tmp1.get(Calendar.DAY_OF_MONTH));
            dayStart.set(Calendar.MONTH, tmp1.get(Calendar.MONTH));
            dayStart.set(Calendar.YEAR, tmp1.get(Calendar.YEAR));
            
            dayEnd.set(Calendar.DAY_OF_MONTH, tmp2.get(Calendar.DAY_OF_MONTH));
            dayEnd.set(Calendar.MONTH, tmp2.get(Calendar.MONTH));
            dayEnd.set(Calendar.YEAR, tmp2.get(Calendar.YEAR));             
            
            long diff = dayEnd.getTimeInMillis() - dayStart.getTimeInMillis();
            long day = days / 86400000;
            long hours = diff / 3600000;
            long minutes = diff / 60000;
            if (hours < 0 || minutes < 0)
                jTextField7.setForeground(Color.red);
            else
                jTextField7.setForeground(Color.black);
            this.jTextField7.setText("" + hours + ":" + minutes % 60);
        }
    }
    
    
    // *************************************************************************
    private void saveDocument() {
        fillDocRouteFromWindow();
        if (!docRoute.isSaved()){
            try {
                System.out.println("QUERY: " + docRoute.save(MainFrame.uid).toString());
                connector.uni_execute(docRoute.save(MainFrame.uid).toString());
                docRoute.setRouteId(connector.getLastID());
                jLabel35.setText(docRoute.getRouteId());                
            } catch (SQLException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, MainFrame.error);}
            docRoute.setSaved(true);
        } else {
            try {
                connector.uni_execute(docRoute.update(MainFrame.uid).toString());
            } catch (SQLException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, MainFrame.error);}
        }
        docRoute.setModified(false);
        this.setTitle(docRoute.getTitle());        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jToolBar8 = new javax.swing.JToolBar();
        jButton10 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo3 = new datechooser.beans.DateChooserCombo();
        jLabel36 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField13 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jFormattedTextField9 = new javax.swing.JFormattedTextField();
        jFormattedTextField10 = new javax.swing.JFormattedTextField();
        jFormattedTextField11 = new javax.swing.JFormattedTextField();
        jPanel8 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jFormattedTextField12 = new javax.swing.JFormattedTextField();
        jFormattedTextField13 = new javax.swing.JFormattedTextField();
        jFormattedTextField14 = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jPanel1 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();

        jPopupMenu1.setDoubleBuffered(true);

        jMenuItem1.setText("Лицевая сторона");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Оборотная сторона");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Документ [Путевой лист]");
        setPreferredSize(new java.awt.Dimension(1050, 768));
        setSize(new java.awt.Dimension(1050, 768));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar8.setFloatable(false);
        jToolBar8.setRollover(true);

        jButton10.setText("Записать");
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar8.add(jButton10);

        jButton16.setText("Сохранить на диск");
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar8.add(jButton16);

        jButton17.setText("Печать");
        jButton17.setFocusable(false);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jToolBar8.add(jButton17);

        jButton18.setText("Блок");
        jButton18.setFocusable(false);
        jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jToolBar8.add(jButton18);

        jButton19.setText("Закрыть");
        jButton19.setFocusable(false);
        jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jToolBar8.add(jButton19);

        getContentPane().add(jToolBar8, java.awt.BorderLayout.PAGE_START);

        jPanel5.setDoubleBuffered(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Путевой лист серия");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("№");

        jLabel3.setText("Организация");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "МУП \"Экоград\"" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Форма 4-С", "Форма 4-П", "Форма 3" }));

        jLabel4.setText("Форма");

        jLabel5.setText("Дата");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Автомобиль", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        jLabel6.setText("Автомобиль");

        jTextField2.setEditable(false);

        jLabel7.setText("Водитель");

        jTextField3.setEditable(false);

        jLabel8.setText("Удостоверение");

        jLabel9.setText("_____");

        jLabel10.setText("Класс");

        jLabel11.setText("Сопровождающий");

        jTextField4.setEditable(false);

        jLabel12.setText("Сопровождающий");

        jTextField5.setEditable(false);

        jButton11.setText("...");
        jButton11.setToolTipText("Выбрать Автомобиль");
        jButton11.setFocusPainted(false);
        jButton11.setPreferredSize(new java.awt.Dimension(45, 20));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("...");
        jButton12.setToolTipText("Выбрать Водителя");
        jButton12.setFocusPainted(false);
        jButton12.setPreferredSize(new java.awt.Dimension(45, 20));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("...");
        jButton13.setToolTipText("Выбрать 1-го Сопровождающего");
        jButton13.setFocusPainted(false);
        jButton13.setPreferredSize(new java.awt.Dimension(45, 20));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("...");
        jButton14.setToolTipText("Выбрать 2-го Сопровождающего");
        jButton14.setFocusPainted(false);
        jButton14.setPreferredSize(new java.awt.Dimension(45, 20));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jLabel33.setText("[Модель ТС]");

        jLabel34.setText("_____");

        jLabel40.setText("Тип ТС");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel9)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jTextField3))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField5))
                            .addComponent(jLabel33)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(276, 276, 276))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel40)
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel40)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel34))))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Работа водителя", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        jLabel13.setText("Выезд из гаража");

        jLabel14.setText("Возвращение в гараж");

        jLabel18.setText("Дата");

        jLabel19.setText("Время");

        jLabel20.setText("Спидометр");

        jLabel21.setText("Время факт.");

        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField1FocusLost(evt);
            }
        });
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyPressed(evt);
            }
        });

        try {
            jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField2FocusLost(evt);
            }
        });
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyPressed(evt);
            }
        });

        try {
            jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField3FocusLost(evt);
            }
        });
        jFormattedTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyPressed(evt);
            }
        });

        try {
            jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField4FocusLost(evt);
            }
        });
        jFormattedTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField4KeyPressed(evt);
            }
        });

        jFormattedTextField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField5FocusLost(evt);
            }
        });
        jFormattedTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField5KeyPressed(evt);
            }
        });

        jFormattedTextField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField6FocusLost(evt);
            }
        });
        jFormattedTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jFormattedTextField6KeyPressed(evt);
            }
        });

        dateChooserCombo2.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        dateChooserCombo2.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    try {
        dateChooserCombo2.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dateChooserCombo2.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dateChooserCombo2OnCommit(evt);
        }
    });

    dateChooserCombo3.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    try {
        dateChooserCombo3.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dateChooserCombo3.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dateChooserCombo3OnCommit(evt);
        }
    });

    jLabel36.setText("Смена");

    jButton1.setText("...");
    jButton1.setToolTipText("Выбрать Смену");
    jButton1.setFocusPainted(false);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jTextField13.setEditable(false);

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel14)
                .addComponent(jLabel13))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(jLabel18)
                    .addGap(127, 127, 127))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(23, 23, 23)))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel19)
                .addComponent(jFormattedTextField1)
                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(28, 28, 28)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel20)
                .addComponent(jFormattedTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addComponent(jFormattedTextField5))
            .addGap(18, 18, 18)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel21)
                    .addGap(0, 504, Short.MAX_VALUE))
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jFormattedTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel19)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(jLabel21))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36)
                        .addComponent(jButton1)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel18)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(jLabel13))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(jLabel14))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Движение горючего", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 11), new java.awt.Color(102, 102, 102))); // NOI18N

    jLabel15.setText("Выезд из гаража");

    jLabel16.setText("Возвращение в гараж");

    jTextField14.setEditable(false);

    jTextField15.setEditable(false);

    jLabel22.setText("Марка");

    jLabel23.setText("Код");

    jLabel24.setText("Выдано");

    jLabel25.setText("Остаток");

    jLabel26.setText("Спецоборудование");

    jFormattedTextField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
    jFormattedTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            jFormattedTextField7FocusLost(evt);
        }
    });
    jFormattedTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jFormattedTextField7KeyPressed(evt);
        }
    });

    jFormattedTextField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
    jFormattedTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            jFormattedTextField8FocusLost(evt);
        }
    });
    jFormattedTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jFormattedTextField8KeyPressed(evt);
        }
    });

    jFormattedTextField9.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
    jFormattedTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            jFormattedTextField9FocusLost(evt);
        }
    });
    jFormattedTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jFormattedTextField9KeyPressed(evt);
        }
    });

    jFormattedTextField10.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
    jFormattedTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            jFormattedTextField10FocusLost(evt);
        }
    });
    jFormattedTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jFormattedTextField10KeyPressed(evt);
        }
    });

    jFormattedTextField11.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
    jFormattedTextField11.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            jFormattedTextField11FocusLost(evt);
        }
    });
    jFormattedTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jFormattedTextField11KeyPressed(evt);
        }
    });

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(jLabel15)
                    .addGap(44, 44, 44)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField14))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel23)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(37, 37, 37)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel24)
                        .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jLabel16))
            .addGap(55, 55, 55)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel25)
                .addComponent(jFormattedTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addComponent(jFormattedTextField10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 100, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel26)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jFormattedTextField11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                    .addComponent(jFormattedTextField9, javax.swing.GroupLayout.Alignment.LEADING)))
            .addGap(401, 401, 401))
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel22)
                .addComponent(jLabel23)
                .addComponent(jLabel24)
                .addComponent(jLabel25)
                .addComponent(jLabel26))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel15)
                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jFormattedTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel16)
                .addComponent(jFormattedTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jFormattedTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(14, Short.MAX_VALUE))
    );

    jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Задание водителю", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 11), new java.awt.Color(102, 102, 102))); // NOI18N

    jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "МУП \"Экоград\"" }));
    jComboBox3.setBorder(null);

    jTextField21.setEditable(false);
    jTextField21.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTextField22.setText("ТКО");
    jTextField22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jTextField22.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jTextField22KeyPressed(evt);
        }
    });

    jLabel27.setText("Заказчик");

    jLabel28.setText("Маршрут");

    jLabel29.setText("Груз");

    jLabel30.setText("Ездки");

    jLabel31.setText("Расстояние");

    jLabel32.setText("Перевезено");

    jButton15.setText("...");
    jButton15.setToolTipText("Выбрать маршрут");
    jButton15.setFocusPainted(false);
    jButton15.setPreferredSize(new java.awt.Dimension(45, 20));
    jButton15.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton15ActionPerformed(evt);
        }
    });

    jFormattedTextField12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
    jFormattedTextField12.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            jFormattedTextField12FocusLost(evt);
        }
    });
    jFormattedTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jFormattedTextField12KeyPressed(evt);
        }
    });

    jFormattedTextField13.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
    jFormattedTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jFormattedTextField13KeyPressed(evt);
        }
    });

    jFormattedTextField14.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel27))
            .addGap(18, 18, 18)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel28)
                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel29))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel30)
                .addComponent(jFormattedTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jFormattedTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel31))
            .addGap(18, 18, 18)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jFormattedTextField14))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel27)
                .addComponent(jLabel28)
                .addComponent(jLabel29)
                .addComponent(jLabel30)
                .addComponent(jLabel31)
                .addComponent(jLabel32))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jFormattedTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jFormattedTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jFormattedTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jLabel17.setText("Комментарий");

    jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel35.setText("____");

    dateChooserCombo1.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    dateChooserCombo1.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dateChooserCombo1.setFormat(2);
dateChooserCombo1.addCommitListener(new datechooser.events.CommitListener() {
    public void onCommit(datechooser.events.CommitEvent evt) {
        dateChooserCombo1OnCommit(evt);
    }
    });

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Расчет", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 11), new java.awt.Color(102, 102, 102))); // NOI18N

    jLabel37.setText("Пробег");

    jLabel38.setText("Моточасов");

    jTextField1.setEditable(false);

    jTextField6.setEditable(false);

    jLabel39.setText("Часов");

    jLabel41.setText("Часов (факт)");

    jTextField7.setEditable(false);

    jTextField8.setEditable(false);
    jTextField8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField8ActionPerformed(evt);
        }
    });

    jLabel42.setText("ГСМ (норма)");

    jLabel43.setText("ГСМ (факт)");

    jTextField9.setEditable(false);

    jTextField10.setEditable(false);

    jLabel44.setText("Расход (спец)");

    jLabel45.setText("Расход (общий)");

    jTextField11.setEditable(false);

    jTextField12.setEditable(false);

    jLabel46.setText("Подъем");

    jTextField16.setEditable(false);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel38)
                .addComponent(jLabel37))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jTextField6)
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel41)
                .addComponent(jLabel39))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jTextField8)
                .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel42)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel43)
                    .addGap(8, 8, 8)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addComponent(jTextField9))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel44)
                .addComponent(jLabel46))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addComponent(jTextField16))
            .addGap(18, 18, 18)
            .addComponent(jLabel45)
            .addGap(8, 8, 8)
            .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
            .addGap(46, 46, 46))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel45)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel42)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44)
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel38)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel41)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel43)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel46)
                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(17, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(30, 30, 30))
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jLabel17)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField26))
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(29, 29, 29)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel17)
                .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(44, Short.MAX_VALUE))
    );

    getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // Записать путевой лист
        saveDocument();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // Сохранить на диск
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Сохранить файл...");
        fc.setFileFilter(new ExtFileFilter("xls", "*.xls MS Office Exel(2003)"));
        int rv = fc.showSaveDialog(this);
        if (rv == JFileChooser.CANCEL_OPTION)
            return;

        File file = fc.getSelectedFile();
        // ********************  4-C ************************************************
        if (jComboBox2.getSelectedIndex() == 0) drToExel(file, 0, false);
        if (jComboBox2.getSelectedIndex() == 1) JOptionPane.showMessageDialog(this, "Форма 4-П недостурна!", "Ошибка!", JOptionPane.ERROR_MESSAGE, MainFrame.error);
        if (jComboBox2.getSelectedIndex() == 2) drToExel(file, 2, false);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        int x = jButton17.getX()+10;
        int y = jButton17.getY() + jButton17.getHeight()+30;
        jPopupMenu1.show(this, x, y);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // Закрыть путевой лист
        if (!docRoute.isClosed()){
        try {
            connector.uni_execute(docRoute.close());
        } catch (SQLException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, MainFrame.error);}
            docRoute.setClosed(true);
        } else
            {JOptionPane.showMessageDialog(this, "Документ уже закрыт!", "Ошибка!", JOptionPane.ERROR_MESSAGE, MainFrame.error);}
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // Путевой лист отмена        
        if (docRoute.isModified()) {
            int rval = JOptionPane.showOptionDialog(null, "Документ был изменен! \nСохранить документ перед закрытием?", "Сохранение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, MainFrame.warning, yesnoArray, yesnoArray[0]);
            if (rval == JOptionPane.YES_OPTION) {
                saveDocument();
            }
        }
        this.dispose();
    }//GEN-LAST:event_jButton19ActionPerformed

    // путевой лист Выбор автомобиля   
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
     
        this.connector.uni_query("SELECT id, number FROM cars");
        new DSearch1(this, true, connector.res).setVisible(true);
        jTextField2.setText(MainFrame.RESULT);        
            docRoute.setCarID(MainFrame.RID);
            if (!docRoute.getCarID().equals("")) {
                docRoute.setTypeId(connector.getOne(catalogCars.getType(docRoute.getCarID())).toString());
                docRoute.setGsmId(connector.getOne(catalogCars.getGsm(docRoute.getCarID())).toString());
                docRoute.setDriverId(connector.getOne(catalogCars.getDriver(docRoute.getCarID())).toString());
                docRoute.setClimb(connector.getOne(catalogCars.getClimb(this.docRoute.getCarID())).toString());
                docRoute.setRashod(connector.getOne(catalogCars.getRashod(this.docRoute.getCarID())).toString());
                docRoute.setRashodSpec(connector.getOne(catalogCars.getRashodSpec(this.docRoute.getCarID())).toString());
                docRoute.setVolume(connector.getOne(catalogCars.getVolume(this.docRoute.getCarID())).toString());
            }
            
            //установить предыдущие значения 
            if (!docRoute.getCarID().equals("")) {
                String lastDocId = connector.getOne(catalogCars.getMaxID(docRoute.getCarID())).toString();
                
                this.jFormattedTextField5.setText(this.connector.getOne(this.catalogCars.getLastProbeg(lastDocId)).toString().replace(".", ","));
                this.jFormattedTextField8.setText(this.connector.getOne(this.catalogCars.getLastGsm(lastDocId)).toString().replace(".", ","));
                this.jFormattedTextField9.setText(this.connector.getOne(this.catalogCars.getLastSpec(lastDocId)).toString().replace(".", ","));
            }
            
            // установить водителя если существует
            if (!docRoute.getDriverId().equals(0)) {
                jTextField3.setText(connector.getOne(catalogCarDrivers.getName(docRoute.getDriverId())).toString()); 
                String str = "№ "+connector.getOne(catalogCarDrivers.getSeries(docRoute.getDriverId()))+" "+connector.getOne(catalogCarDrivers.getNumber(docRoute.getDriverId()));
                jLabel9.setText(str);                
                jLabel34.setText(connector.getOne(catalogCarDrivers.getCategory(docRoute.getDriverId())).toString());              
            }
            
            // Установить параметры автомобиля
        if (!docRoute.getCarID().equals("")) {            
            jLabel33.setText("[ "+ connector.getOne(catalogCars.getCarModel(docRoute.getCarID())).toString()+" ]");
            jTextField14.setText(connector.getOne(catalogGSM.getMark(docRoute.getGsmId())).toString());
            jTextField15.setText(connector.getOne(catalogGSM.getCode(docRoute.getGsmId())).toString());            
            String tid= connector.getOne(catalogCars.getType(docRoute.getCarID())).toString();            
            jLabel40.setText(connector.getOne(SCarType.query2+tid).toString());
            }
            
//        if (!this.jFormattedTextField12.getText().equals("")) {
//            float cl = Float.parseFloat(this.docRoute.getClimb()) * Float.parseFloat(this.jFormattedTextField12.getText());
//            this.jTextField16.setText(new BigDecimal(cl).setScale(2, RoundingMode.HALF_UP).toString());
//        }
        this.setAll();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());
        this.jButton12.requestFocusInWindow();                        

    }//GEN-LAST:event_jButton11ActionPerformed

    // путевой лист выбор водителя
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        
        this.connector.uni_query(this.catalogCarDrivers.getNames(1));
        new DSearch1(this, true, connector.res).setVisible(true);
        jTextField3.setText(MainFrame.RESULT);
        docRoute.setDriverId(MainFrame.RID);
        
        //Если не нажата отмена установить данные водителя
        if (!docRoute.getDriverId().equals("") || !docRoute.getDriverId().equals("0")){
            String str;
            str = "№ "+connector.getOne(catalogCarDrivers.getSeries(docRoute.getDriverId()))+" "+connector.getOne(catalogCarDrivers.getNumber(docRoute.getDriverId()));
            jLabel9.setText(str);
            jLabel34.setText(connector.getOne(catalogCarDrivers.getCategory(docRoute.getDriverId())).toString());
        }
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
        this.jButton13.requestFocusInWindow();
    }//GEN-LAST:event_jButton12ActionPerformed

    // Выбор сопровождающий 1
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        this.connector.uni_query(this.catalogCarDrivers.getNames(2));
        new DSearch1(this, true, connector.res).setVisible(true);
        jTextField4.setText(MainFrame.RESULT);
        docRoute.setSopr1(MainFrame.RID);
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
        this.jButton14.requestFocusInWindow();
    }//GEN-LAST:event_jButton13ActionPerformed

    // Выбор сопровождающий 2
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        
        this.connector.uni_query(this.catalogCarDrivers.getNames(2));
        new DSearch1(this, true, connector.res).setVisible(true);
        jTextField5.setText(MainFrame.RESULT);
        docRoute.setSopr2(MainFrame.RID);
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
        this.jButton1.requestFocusInWindow();
    }//GEN-LAST:event_jButton14ActionPerformed

    // выбор маршрута
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        
        this.connector.uni_query(this.catalogDirection.getDirections());
        new DSearch1(this, true, connector.res).setVisible(true);
        jTextField21.setText(MainFrame.RESULT);
        docRoute.setDirection(MainFrame.RID);
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
        this.jTextField22.requestFocusInWindow();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    // Выбор Смена
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.connector.uni_query(sShift.getJournalSmall());
        new DSearch1(this, true, this.connector.res).setVisible(true);
        jTextField13.setText(MainFrame.RESULT);
        docRoute.setShift(MainFrame.RID);
        if (!docRoute.getShift().equals("")) {
            docRoute.setTimeStart(connector.getOne(sShift.getTimeStart(docRoute.getShift())).toString());
            docRoute.setTimeEnd(connector.getOne(sShift.getTimeEnd(docRoute.getShift())).toString());
            docRoute.setTimefStart(connector.getOne(sShift.getTimeStart(docRoute.getShift())).toString());
            docRoute.setTimefEnd(connector.getOne(sShift.getTimeEnd(docRoute.getShift())).toString());        
            jFormattedTextField1.setText(docRoute.getTimeStart());
            jFormattedTextField2.setText(docRoute.getTimeEnd());
            //jFormattedTextField3.setText(docRoute.getTimefStart());
            //jFormattedTextField4.setText(docRoute.getTimefEnd()); 
            }
        setTime();
        setfTime();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
        this.jFormattedTextField5.requestFocusInWindow();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
            // Печать лицевой стороны
            int form = jComboBox2.getSelectedIndex();
            printFront(form);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
            int form = jComboBox2.getSelectedIndex();
            printBack(form);        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    // Спидометр выезда
    private void jFormattedTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField5FocusLost
        this.setProbeg();
        this.setGSM();
        this.setAll(); 
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField5FocusLost

    // Спидометр вызвращения
    private void jFormattedTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField6FocusLost
        this.setProbeg();
        this.setGSM();
        this.setAll();  
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField6FocusLost

    // Фактическое время выезда
    private void jFormattedTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField3FocusLost
        this.setfTime();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField3FocusLost

    // фактическое время возвращения
    private void jFormattedTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField4FocusLost
        this.setfTime();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField4FocusLost

    // выдано топлива
    private void jFormattedTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField7FocusLost
        this.setfGSM();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField7FocusLost

    // Остаток топлива при выезде
    private void jFormattedTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField8FocusLost
        this.setfGSM();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField8FocusLost

    // остаток топлива при возвращении
    private void jFormattedTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField10FocusLost
        this.setfGSM();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());
    }//GEN-LAST:event_jFormattedTextField10FocusLost

    // спецоборудование при выезде
    private void jFormattedTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField9FocusLost
        this.setSpecial();
        this.setGSMspec();
        this.setAll();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField9FocusLost

    // спецоборудование при возвращении
    private void jFormattedTextField11FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField11FocusLost
        this.setSpecial();
        this.setGSMspec();
        this.setAll();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField11FocusLost

    // дата документа
    private void dateChooserCombo1OnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dateChooserCombo1OnCommit
                
        docRoute.setDate(dateChooserCombo1.getSelectedDate());
        this.dateChooserCombo2.setSelectedDate(this.dateChooserCombo1.getSelectedDate());
        this.dateChooserCombo3.setSelectedDate(this.dateChooserCombo1.getSelectedDate());  
        if (!openNew) {
            docRoute.setModified(true);
            this.setTitle(docRoute.getTitle());        
        }        
        jButton11.requestFocus();
    }//GEN-LAST:event_dateChooserCombo1OnCommit

    // время выезда
    private void jFormattedTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField2.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField1KeyPressed

    // время возвращения
    private void jFormattedTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField5.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField2KeyPressed

    // спидометр выезда
    private void jFormattedTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField6.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField5KeyPressed
    // спидометр возвращения
    private void jFormattedTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField6KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField3.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField6KeyPressed

    // фактическое время выезда
    private void jFormattedTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField4.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField3KeyPressed

    // фактическое время возвращения
    private void jFormattedTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField7.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField4KeyPressed

    // выдано топлива
    private void jFormattedTextField7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField7KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField8.requestFocus();
        }        
    }//GEN-LAST:event_jFormattedTextField7KeyPressed
    
    // остаток топлива при выезде
    private void jFormattedTextField8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField8KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){         
            jFormattedTextField10.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField8KeyPressed

    // остаток топлива при возвращении
    private void jFormattedTextField10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField10KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){            
            jFormattedTextField9.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField10KeyPressed

    // моточасы при выезде
    private void jFormattedTextField9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField9KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField11.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField9KeyPressed

    // моточасы при возвращении
    private void jFormattedTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField11KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jButton15.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField11KeyPressed
    
    // груз
    private void jTextField22KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField22KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField12.requestFocus();
        }
    }//GEN-LAST:event_jTextField22KeyPressed

    // ездки
    private void jFormattedTextField12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField12KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField13.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField12KeyPressed

    // расстояние
    private void jFormattedTextField13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField13KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            jFormattedTextField14.requestFocus();
        }
    }//GEN-LAST:event_jFormattedTextField13KeyPressed

    // ездки
    private void jFormattedTextField12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField12FocusLost
        setClimb();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField12FocusLost

    // дата выезда
    private void dateChooserCombo2OnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dateChooserCombo2OnCommit
        setTime();
        setfTime();        
        if (!openNew) {
            docRoute.setModified(true);
            this.setTitle(docRoute.getTitle());        
        }
    }//GEN-LAST:event_dateChooserCombo2OnCommit
    
    // дата возвращения
    private void dateChooserCombo3OnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dateChooserCombo3OnCommit
        setTime();
        setfTime();         
        if (!openNew) {
            docRoute.setModified(true);
            this.setTitle(docRoute.getTitle());        
        }
    }//GEN-LAST:event_dateChooserCombo3OnCommit

    private void jFormattedTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField1FocusLost
        // Время выезда
        setTime();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField1FocusLost

    private void jFormattedTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField2FocusLost
        // Время возвращения
        setTime();
        docRoute.setModified(true);
        this.setTitle(docRoute.getTitle());        
    }//GEN-LAST:event_jFormattedTextField2FocusLost

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Закрытие окна
        if (docRoute.isModified()) {
            int rval = JOptionPane.showOptionDialog(null, "Документ был изменен! \nСохранить документ перед закрытием?", "Сохранение", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, MainFrame.warning, yesnoArray, yesnoArray[0]);
            if (rval == JOptionPane.YES_OPTION) {
                saveDocument();                
            }
        }
        this.dispose();        
    }//GEN-LAST:event_formWindowClosing



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private datechooser.beans.DateChooserCombo dateChooserCombo3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField10;
    private javax.swing.JFormattedTextField jFormattedTextField11;
    private javax.swing.JFormattedTextField jFormattedTextField12;
    private javax.swing.JFormattedTextField jFormattedTextField13;
    private javax.swing.JFormattedTextField jFormattedTextField14;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
    private javax.swing.JFormattedTextField jFormattedTextField9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToolBar jToolBar8;
    // End of variables declaration//GEN-END:variables
}
