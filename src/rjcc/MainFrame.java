/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author User
 */
public class MainFrame extends javax.swing.JFrame {
    
static String version = "1.38.234";    

String charset = "UTF-8";
String dbDriver = "com.mysql.jdbc.Driver";
String dbPort = "3306";
String dbName = "city-clean";
String srvIP = "127.0.0.1";
String userName = "root";
String pass = "kalinka";
String sdate = "";
String edate = "";
String showDeleted = "yes";
String showClosed = "yes";
String font = "Arial";
String fontWeight = "0";
String fontSize = "14";
String ulogin = "admin";
String url="http://jmicode.ru";
String sColor = "R:204,G:204,B:204";
String tColor = "R:255,G:255,B:255";
String tgColor = "R:0,G:0,B:0";
String sfColor = "R:0,G:0,B:0";
String sessionId;




Icon help = new ImageIcon("image/help.png");
Icon app = new ImageIcon("image/approve.png");
static Icon warning = new ImageIcon("image/warning.png");
static Icon error = new ImageIcon("image/error.png");

Object yesnoArray[] = { "ДА", "НЕТ" };
ImageIcon successico = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("successico.png")));
ImageIcon warningico = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("warningico.png")));
//JProgressBar pbar;
//int percent;

    
    
JPL jpl;  
SCarType sct;
SUsers su;
SCarDrivers scd;
SCars sc;
SGSM sgsm;
SDirections sd;
DocRoute dr;
SShift ss;
DocRouteFrame drf;
OGSM reportGSM;
OGSM1 reportGSM1;
DocRouteFrame docRouteFrame;


static String RESULT = "";
static String RID = "";
static StringBuffer q = new StringBuffer();
static boolean exec = false;
static String uname = "";
static String uid = "0";
static Font tableFont;
static Color selectColor;
static Color selectForegroundColor;
static Color tableColor;
static Color tableGridColor;

DefaultListModel listModel = new DefaultListModel();
DefaultTableModel sessionModel = new DefaultTableModel();
DefaultTableModel sessionActiveModel = new DefaultTableModel();
DefaultTableModel deleteModel = new DefaultTableModel(){
          @Override
                    public Class<?> getColumnClass(int col) {
                        if (col == 0) {
                            return Boolean.class;
                        }
                    return super.getColumnClass(col);
                    }    
};

int DRIVERS = 1;
int CARGERS = 2;

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

int updateTimeMillis = 10000;
Thread connectionAlive;
boolean doUpdate = false;

SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
SimpleDateFormat sql_sdf = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat sql_sdf_wTime = new SimpleDateFormat("yyyy-MM-dd H:mm");
Calendar calendar = Calendar.getInstance();
    /**
     * Creates new form MainFrame
     */
    
    Connector connector = new Connector();
    
    public MainFrame(String mes) { 
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("truck.png")));
        readProp();
        preInit();
        initComponents();
        initTables();
        
        jPopupMenu1.add(new JMenuItem("Печать лицевой стороны"));
        jPopupMenu1.add(new JMenuItem("Печать обратной стороны"));
        jPopupMenu1.add(new JMenuItem("Печать две стороны"));
        
//        Calendar c = Calendar.getInstance();        
//        Calendar e = Calendar.getInstance();
//        e.set(2016, 9, 9);
//        if (c.after(e)) {
//            JOptionPane.showMessageDialog(this, "Срок действия весии истек! \nРабота программы будет прекращена.", "Ошибка!", JOptionPane.ERROR_MESSAGE, error);
//            System.exit(0); } else {jTextArea1.append("Действие версии истекает через "+(10-c.get(Calendar.DAY_OF_MONTH))+" дней.");}
        
        
        jTextArea1.append("Соединение "+dbName+"@"+srvIP);
        try {
            connector.connect(dbDriver, srvIP, dbName, charset, userName, pass);            
            jTextArea1.append("\nСоединение установлено.");
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            JOptionPane.showMessageDialog(this, "Не удалось установить соединение с базой. \nРабота приложения будет прекращена.\nОбратитесь к администратору для устранения неполадки.", "Ошибка!", JOptionPane.ERROR_MESSAGE, error); 
            jTextArea1.append("\nНе удалось установить соединение с базой!");
            System.exit(0);
        }  
        new DLogin(this, true, ulogin, connector).setVisible(true);
        ulogin = RESULT;
        uname = connector.getOne("SELECT name FROM users WHERE login='"+ulogin+"'").toString();
        uid = connector.getOne("SELECT id FROM users WHERE login='"+ulogin+"'").toString();
        jTextArea1.append("\nПользователь: "+ ulogin);
        this.setTitle(getTitle()+" ["+uname+"]");        
        connectionAlive.start();

        try {
            InetAddress addr = InetAddress.getLocalHost();
            connector.uni_execute("INSERT INTO session (username, host_ip, host_name, login_date, status) VALUES ('"+uname
                +"', '"+addr.getHostAddress()+"', '"+addr.getHostName()+"', NOW(), 1)");
            sessionId = connector.getLastID();
            } catch (SQLException | UnknownHostException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }        
        
// ***************************  слушатели *******************************
        //поиск типы ТС
        this.jTextField2.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                sct.setRowFilter(jTextField2.getText(), 1);
            }        
            @Override
            public void removeUpdate(DocumentEvent e) {
                sct.setRowFilter(jTextField2.getText(), 1);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }                    
        });
        // поиск водители и слпровождающие
        this.jTextField8.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                scd.setRowFilter(jTextField8.getText(), 1);
            }        
            @Override
            public void removeUpdate(DocumentEvent e) {
                scd.setRowFilter(jTextField8.getText(), 1);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }                    
        });        
        // поиск автомобили
        this.jTextField9.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                sc.setRowFilter(jTextField9.getText(), 1);
            }        
            @Override
            public void removeUpdate(DocumentEvent e) {
                sc.setRowFilter(jTextField9.getText(), 1);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }                    
        });        

    sct.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    int row = sct.getTable().getRowSorter().convertRowIndexToModel(sct.getTable().getSelectedRow());
                    new DType(null, true, sct.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);                                        
                        removeAllRows(sct.getModel());
                        connector.uni_query(sct.getJournal());
                        insertJData(sct.getModel(), false, 1);                     
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}           
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
    });
    sct.getTable().addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    int row = sct.getTable().getRowSorter().convertRowIndexToModel(sct.getTable().getSelectedRow());
                    int rval = JOptionPane.showOptionDialog(null, "Вы действительно хотите удалить элемент?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, warning, yesnoArray, yesnoArray[0]);
                    if (rval == JOptionPane.YES_OPTION) {
                        try {
                            connector.uni_execute("UPDATE types SET status=0 WHERE id="+sct.getModel().getValueAt(row, 0));
                            jTextArea1.append("\nЭлемент справочника [Типы ТС] №"+sct.getModel().getValueAt(row, 0)+" помечен на удаление.");
                        } catch (SQLException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int row = sct.getTable().getRowSorter().convertRowIndexToModel(sct.getTable().getSelectedRow());
                    new DType(null, true, sct.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);                                        
                        removeAllRows(sct.getModel());
                        connector.uni_query(sct.getJournal());
                        insertJData(sct.getModel(), false, 1);                     
                    }                    
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
    });
    scd.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    int row = scd.getTable().getRowSorter().convertRowIndexToModel(scd.getTable().getSelectedRow());
                    if (scd.getTitleId(jInternalFrame4.getTitle()) == DRIVERS)
                        new DDRiver(null, true, scd.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    else
                        new DCarger(null, true, scd.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(scd.getModel());
                        connector.uni_query(scd.getJournal(scd.getTitleId(jInternalFrame4.getTitle())));
                        insertJData(scd.getModel(), false, 1);                         
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
    });
    scd.getTable().addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE){
                    int row = scd.getTable().getRowSorter().convertRowIndexToModel(scd.getTable().getSelectedRow());
                    int rval = JOptionPane.showOptionDialog(null, "Вы действительно хотите удалить элемент?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, warning, yesnoArray, yesnoArray[0]);
                    if (rval == JOptionPane.YES_OPTION) {
                        try {
                            connector.uni_execute("UPDATE drivers SET status=0 WHERE id="+scd.getModel().getValueAt(row, 0));
                            jTextArea1.append("\nЭлемент справочника [Водители] №"+scd.getModel().getValueAt(row, 0)+" помечен на удаление.");                                                    
                        } catch (SQLException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int row = scd.getTable().getRowSorter().convertRowIndexToModel(scd.getTable().getSelectedRow());
                    if (scd.getTitleId(jInternalFrame4.getTitle()) == DRIVERS)
                        new DDRiver(null, true, scd.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    else
                        new DCarger(null, true, scd.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(scd.getModel());
                        connector.uni_query(scd.getJournal(scd.getTitleId(jInternalFrame4.getTitle())));
                        insertJData(scd.getModel(), false, 1);                         
                    }                    
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
    });
    sgsm.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    new DGSMType(null, true, sgsm.getModel().getValueAt(sgsm.getTable().getSelectedRow(), 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(sgsm.getModel());
                        connector.uni_query(sgsm.getJournal());
                        insertJData(sgsm.getModel(), false, 1);                         
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
    });
    sgsm.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    int rval = JOptionPane.showOptionDialog(null, "Вы действительно хотите удалить элемент?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, warning, yesnoArray, yesnoArray[0]);
                    if (rval == JOptionPane.YES_OPTION) {  
                        try {
                            connector.uni_execute("UPDATE gsm SET status=0 WHERE id="+sgsm.getModel().getValueAt(sgsm.getTable().getSelectedRow(), 0));
                            jTextArea1.append("\nЭлемент справочника [Типы ГСМ] №"+sgsm.getModel().getValueAt(sgsm.getTable().getSelectedRow(), 0)+" помечен на удаление.");
                        } catch (SQLException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new DGSMType(null, true, sgsm.getModel().getValueAt(sgsm.getTable().getSelectedRow(), 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(sgsm.getModel());
                        connector.uni_query(sgsm.getJournal());
                        insertJData(sgsm.getModel(), false, 1);                         
                    }                    
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
    });
    sd.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    new DDirection(null, true, sd.getModel().getValueAt(sd.getTable().getSelectedRow(), 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(sd.getModel());
                        connector.uni_query(sd.getJournal());
                        insertJData(sd.getModel(), false, 1);                         
                    }
                }                    
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
    });
    sd.getTable().addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {                
            }

            @Override
            public void keyPressed(KeyEvent e) {                
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    int rval = JOptionPane.showOptionDialog(null, "Вы действительно хотите удалить элемент?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, warning, yesnoArray, yesnoArray[0]);
                    if (rval == JOptionPane.YES_OPTION) {
                        try {
                            connector.uni_execute("UPDATE directions SET status=0 WHERE id="+sd.getModel().getValueAt(sd.getTable().getSelectedRow(), 0));
                            jTextArea1.append("\nЭлемент справочника [Маршруты] №"+sd.getModel().getValueAt(sd.getTable().getSelectedRow(), 0)+" помечен на удаление.");                        
                        } catch (SQLException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new DDirection(null, true, sd.getModel().getValueAt(sd.getTable().getSelectedRow(), 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(sd.getModel());
                        connector.uni_query(sd.getJournal());
                        insertJData(sd.getModel(), false, 1);                         
                    }                    
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        
    });
    sc.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    int row = sc.getTable().getRowSorter().convertRowIndexToModel(sc.getTable().getSelectedRow());
                    new DCar(null, true, sc.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(sc.getModel());
                        connector.uni_query(sc.getJournal());
                        insertJData(sc.getModel(), false, 1);                         
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    sc.getTable().addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE){
                    int row = sc.getTable().getRowSorter().convertRowIndexToModel(sc.getTable().getSelectedRow());                    
                    int rval = JOptionPane.showOptionDialog(null, "Вы действительно хотите удалить элемент?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, warning, yesnoArray, yesnoArray[0]);
                    if (rval == JOptionPane.YES_OPTION) {
                        try {
                            connector.uni_execute("UPDATE cars SET status=0 WHERE id="+sc.getModel().getValueAt(row, 0));
                            jTextArea1.append("\nЭлемент справочника [Автомобили] №"+sc.getModel().getValueAt(row, 0)+" помечен на удаление.");
                        } catch (SQLException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int row = sc.getTable().getRowSorter().convertRowIndexToModel(sc.getTable().getSelectedRow());
                    new DCar(null, true, sc.getModel().getValueAt(row, 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(sc.getModel());
                        connector.uni_query(sc.getJournal());
                        insertJData(sc.getModel(), false, 1);                         
                    }                    
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
    });
    jpl.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    SwingUtilities.invokeLater(() -> {
                        fillDocRoute();
                        drf = new DocRouteFrame(dr, connector);
                        drf.setVisible(true);
                    });
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    jpl.getTable().addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    delDocument();
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(() -> {
                        fillDocRoute();
                        drf = new DocRouteFrame(dr, connector);
                        drf.setVisible(true);
                    });                    
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
    });
    su.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    new DManager(null, true, su.getModel().getValueAt(su.getTable().getSelectedRow(), 0).toString(), connector).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(su.getModel());
                        connector.uni_query(su.getJournal());
                        insertJData(su.getModel(), false, 1);                         
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
    });
    ss.getTable().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    new DShift(null, true, ss.getModel().getValueAt(ss.getTable().getSelectedRow(), 0).toString(), connector, ss).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(ss.getModel());
                        connector.uni_query(ss.getJournal());
                        insertJData(ss.getModel(), false, 1);                         
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
    });
    ss.getTable().addKeyListener(new KeyListener() {        
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    int rval = JOptionPane.showOptionDialog(null, "Вы действительно хотите удалить элемент?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, warning, yesnoArray, yesnoArray[0]);
                    if (rval == JOptionPane.YES_OPTION) {
                        try {
                            connector.uni_execute("UPDATE shift SET status=0 WHERE id="+ss.getModel().getValueAt(ss.getTable().getSelectedRow(), 0));
                            jTextArea1.append("\nЭлемент справочника [Смены] №"+ss.getModel().getValueAt(ss.getTable().getSelectedRow(), 0)+" помечен на удаление.");                        
                        } catch (SQLException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                    }                    
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new DShift(null, true, ss.getModel().getValueAt(ss.getTable().getSelectedRow(), 0).toString(), connector, ss).setVisible(true);
                    if (exec) {
                        ins(q);
                        removeAllRows(ss.getModel());
                        connector.uni_query(ss.getJournal());
                        insertJData(ss.getModel(), false, 1);                         
                    }                    
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
    });
    }    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void readProp(){
        Properties prop = new Properties();
    try {
        FileInputStream fpr = new FileInputStream("config.properties");
            prop.load(fpr);  
            charset = prop.getProperty("dbcharset");
            dbDriver = prop.getProperty("dbdriver");
            dbPort = prop.getProperty("dbport");
            dbName = prop.getProperty("dbname");
            srvIP = prop.getProperty("ip");
            userName = prop.getProperty("username");
            pass = prop.getProperty("pass");
            sdate = prop.getProperty("date_start");
            edate = prop.getProperty("date_end");
            showDeleted = prop.getProperty("show_deleted");
            showClosed = prop.getProperty("show_closed");
            font = prop.getProperty("font");
            fontSize = prop.getProperty("font_size");
            ulogin = prop.getProperty("last_login");
            sdate = prop.getProperty("date_start");
            edate = prop.getProperty("date_end");
            //version = prop.getProperty("version");
            url = prop.getProperty("url");   
            fontWeight = prop.getProperty("font_weight");
            sColor = prop.getProperty("select_color");
            sfColor = prop.getProperty("select_foreground");
            tColor = prop.getProperty("table_color");
            tgColor = prop.getProperty("table_grid_color");
            // -----------------------------------------------------------------

            auto = prop.getProperty("jnumber").equals("true");
            number = prop.getProperty("jnumber").equals("true");
            driver = prop.getProperty("jdriver").equals("true");
            shift = prop.getProperty("jshift").equals("true");
            user = prop.getProperty("juser").equals("true");
            des = prop.getProperty("jdes").equals("true");
            dateStart = prop.getProperty("jdate_start").equals("true");
            dateStop = prop.getProperty("jdate_stop").equals("true");
            odoStart = prop.getProperty("jodo_start").equals("true");
            odoStop = prop.getProperty("jodo_stop").equals("true");
            gsmStart = prop.getProperty("jgsm_start").equals("true");
            gsmStop = prop.getProperty("jgsm_stop").equals("true");
            gsmGet = prop.getProperty("jgsm_get").equals("true");
            specStart = prop.getProperty("jspec_start").equals("true");
            specStop = prop.getProperty("jspec_stop").equals("true");
            
        } catch (IOException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
    }
    private void storeProp(){
    try {
        Properties prop = new Properties();
        prop.setProperty("dbcharset", charset);
        prop.setProperty("dbdriver", dbDriver);
        prop.setProperty("dbport", dbPort);
        prop.setProperty("dbname", dbName);
        prop.setProperty("ip", srvIP);
        prop.setProperty("username", userName);
        prop.setProperty("pass", pass);
        prop.setProperty("date_start", sdate);
        prop.setProperty("date_end", edate);
        prop.setProperty("show_deleted", showDeleted);
        prop.setProperty("show_closed", showClosed);
        prop.setProperty("font", font);
        prop.setProperty("font_size", fontSize);
        prop.setProperty("last_login", ulogin);
        prop.setProperty("version", version);
        prop.setProperty("url", url);
        prop.setProperty("font_weight", fontWeight);
        prop.setProperty("select_color", sColor);
        prop.setProperty("select_foreground", sfColor);
        prop.setProperty("table_color", tColor);
        prop.setProperty("table_grid_color", tgColor);
        // ---------------------------------------------------------------------
        prop.setProperty("jauto", ""+jpl.getAuto());
        prop.setProperty("jnumber", ""+jpl.getNumber());
        prop.setProperty("jdriver", ""+jpl.getDriver());
        prop.setProperty("jshift", ""+jpl.getShift());
        prop.setProperty("juser", ""+jpl.getUser());
        prop.setProperty("jdes", ""+jpl.getDes());
        prop.setProperty("jdate_start", ""+jpl.getDateStart());
        prop.setProperty("jdate_stop", ""+jpl.getDateStop());
        prop.setProperty("jodo_start", ""+jpl.getOdoStart());
        prop.setProperty("jodo_stop", ""+jpl.getOdoStop());
        prop.setProperty("jgsm_start", ""+jpl.getGsmStart());
        prop.setProperty("jgsm_stop", ""+jpl.getGsmStop());
        prop.setProperty("jgsm_get", ""+jpl.getGsmGet());
        prop.setProperty("jspec_start", ""+jpl.getSpecStart());
        prop.setProperty("jspec_stop", ""+jpl.getSpecStop());
        prop.store(new FileOutputStream(new File("config.properties")), "SETTINGS");        
    } catch (IOException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
    }
    private void preInit(){
        
        tableFont = new Font(font, Integer.parseInt(fontWeight), Integer.parseInt(fontSize));
        int red, green, blue;
        red = Integer.parseInt(sColor.substring(2, sColor.indexOf("G")));
        green = Integer.parseInt(sColor.substring(sColor.indexOf("G")+2, sColor.indexOf("B")));
        blue = Integer.parseInt(sColor.substring(sColor.indexOf("B")+2));
        selectColor = new Color(red, green, blue);
        red = Integer.parseInt(sfColor.substring(2, sfColor.indexOf("G")));
        green = Integer.parseInt(sfColor.substring(sfColor.indexOf("G")+2, sfColor.indexOf("B")));
        blue = Integer.parseInt(sfColor.substring(sfColor.indexOf("B")+2));
        selectForegroundColor = new Color(red, green, blue);        
        red = Integer.parseInt(tColor.substring(2, tColor.indexOf("G")));
        green = Integer.parseInt(tColor.substring(tColor.indexOf("G")+2, tColor.indexOf("B")));
        blue = Integer.parseInt(tColor.substring(tColor.indexOf("B")+2));
        tableColor = new Color(red, green, blue);        
        red = Integer.parseInt(tgColor.substring(2, tgColor.indexOf("G")));
        green = Integer.parseInt(tgColor.substring(tgColor.indexOf("G")+2, tgColor.indexOf("B")));
        blue = Integer.parseInt(tgColor.substring(tgColor.indexOf("B")+2));
        tableGridColor = new Color(red, green, blue);        
        
        sessionModel.addColumn("ID сессии");       
        sessionModel.addColumn("Пользователь");         
        sessionModel.addColumn("IP адрес");        
        sessionModel.addColumn("Имя хоста");       
        sessionModel.addColumn("Время входа");         
        sessionModel.addColumn("Время выхода");                 
        sessionModel.addColumn("Статус");        
        
        sessionActiveModel.addColumn("ID");
        sessionActiveModel.addColumn("Пользователь");
        sessionActiveModel.addColumn("Хост");
        sessionActiveModel.addColumn("БД");
        sessionActiveModel.addColumn("Команда");
        sessionActiveModel.addColumn("Время");
        sessionActiveModel.addColumn("Статус");
        sessionActiveModel.addColumn("Инфо");
        sessionActiveModel.addColumn("Прогресс");
        
        deleteModel.addColumn("");
        deleteModel.addColumn("ID");
        deleteModel.addColumn("Наименование");
        deleteModel.addColumn("Родитель");
        
        jpl = new JPL();
            jpl.setAuto(auto);
            jpl.setNumber(number);
            jpl.setShift(shift);
            jpl.setUser(user);
            jpl.setDes(des);
            jpl.setDateStart(dateStart);
            jpl.setDateStop(dateStop);
            jpl.setDriver(driver);
            jpl.setOdoStart(odoStart);
            jpl.setOdoStop(odoStop);
            jpl.setGsmStart(gsmStart);
            jpl.setGsmStop(gsmStop);
            jpl.setGsmGet(gsmGet);
            jpl.setSpecStart(specStart);
            jpl.setSpecStop(specStop);
            jpl.initTable();
        dr = new DocRoute();
        sct = new SCarType();
        su = new SUsers();
        scd = new SCarDrivers();
        sc = new SCars();
        sgsm = new SGSM();
        ss = new SShift();
        sd = new SDirections();  
        reportGSM = new OGSM();
        reportGSM1 = new OGSM1();
        drf = new DocRouteFrame();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);        

        connectionAlive = new Thread(() -> {
            while(connector.isAlive()){
                try {
                    Thread.sleep(updateTimeMillis);                    
                } catch (InterruptedException e){JOptionPane.showMessageDialog(this, e, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
            }
            JOptionPane.showMessageDialog(this, "Утеряно соединение с базой данных!", "Ошибка!", JOptionPane.ERROR_MESSAGE, error);
        });
    }  
    private void initTables() {
            // -------- журнал регистрации -------------------------------------
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(80);
            jTable2.getColumnModel().getColumn(0).setMinWidth(80);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(80);        
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(220);
            jTable2.getColumnModel().getColumn(1).setMinWidth(220);
            jTable2.getColumnModel().getColumn(1).setMaxWidth(220);        
            jTable2.getColumnModel().getColumn(1).setResizable(false); 
            
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable2.getColumnModel().getColumn(2).setMinWidth(150);
            jTable2.getColumnModel().getColumn(2).setMaxWidth(150);        
            jTable2.getColumnModel().getColumn(2).setResizable(false); 
            
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(3).setMinWidth(200);
            jTable2.getColumnModel().getColumn(3).setMaxWidth(200);        
            jTable2.getColumnModel().getColumn(3).setResizable(false);  

            jTable2.getColumnModel().getColumn(4).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(4).setMinWidth(200);
            jTable2.getColumnModel().getColumn(4).setMaxWidth(200);        
            jTable2.getColumnModel().getColumn(4).setResizable(false);
            
            jTable2.getColumnModel().getColumn(5).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(5).setMinWidth(200);
            jTable2.getColumnModel().getColumn(5).setMaxWidth(200);        
            jTable2.getColumnModel().getColumn(5).setResizable(false); 

            jTable2.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTable2.getColumnModel().getColumn(6).setMinWidth(20);
            jTable2.getColumnModel().getColumn(6).setMaxWidth(20);        
            jTable2.getColumnModel().getColumn(6).setResizable(false);            
            
            jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            jTable2.setAutoCreateRowSorter(true);
            jTable2.setFont(MainFrame.tableFont);
            jTable2.setBackground(MainFrame.tableColor);
            jTable2.setGridColor(MainFrame.tableGridColor);
            jTable2.setRowHeight(20);
            jTable2.setSelectionBackground(MainFrame.selectColor);
            jTable2.setSelectionForeground(MainFrame.selectForegroundColor);
            jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jTable2.getColumnModel().getColumn(6).setCellRenderer(jTable2.getDefaultRenderer(ImageIcon.class));
            jTable2.setShowVerticalLines(false);  
            // -----------------------------------------------------------------
            
            // -------- Сессии -------------------------------------
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(80);
            jTable3.getColumnModel().getColumn(0).setMinWidth(80);
            jTable3.getColumnModel().getColumn(0).setMaxWidth(80);        
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            
            jTable3.getColumnModel().getColumn(1).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(1).setMinWidth(150);
            jTable3.getColumnModel().getColumn(1).setMaxWidth(150);        
            jTable3.getColumnModel().getColumn(1).setResizable(false); 
            
            jTable3.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTable3.getColumnModel().getColumn(2).setMinWidth(200);
            jTable3.getColumnModel().getColumn(2).setMaxWidth(200);        
            jTable3.getColumnModel().getColumn(2).setResizable(false); 
            
            jTable3.getColumnModel().getColumn(3).setPreferredWidth(120);
            jTable3.getColumnModel().getColumn(3).setMinWidth(120);
            jTable3.getColumnModel().getColumn(3).setMaxWidth(120);        
            jTable3.getColumnModel().getColumn(3).setResizable(false);  

            jTable3.getColumnModel().getColumn(4).setPreferredWidth(120);
            jTable3.getColumnModel().getColumn(4).setMinWidth(120);
            jTable3.getColumnModel().getColumn(4).setMaxWidth(120);        
            jTable3.getColumnModel().getColumn(4).setResizable(false);
            
            jTable3.getColumnModel().getColumn(5).setPreferredWidth(80);
            jTable3.getColumnModel().getColumn(5).setMinWidth(80);
            jTable3.getColumnModel().getColumn(5).setMaxWidth(80);        
            jTable3.getColumnModel().getColumn(5).setResizable(false); 
            
            jTable3.getColumnModel().getColumn(6).setPreferredWidth(80);
            jTable3.getColumnModel().getColumn(6).setMinWidth(80);
            jTable3.getColumnModel().getColumn(6).setMaxWidth(80);        
            jTable3.getColumnModel().getColumn(5).setResizable(false); 

            jTable3.getColumnModel().getColumn(7).setPreferredWidth(180);
            jTable3.getColumnModel().getColumn(7).setMinWidth(180);
            jTable3.getColumnModel().getColumn(7).setMaxWidth(180);        
            jTable3.getColumnModel().getColumn(7).setResizable(false);             
            
            jTable3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            jTable3.setAutoCreateRowSorter(true);
            jTable3.setFont(MainFrame.tableFont);
            jTable3.setBackground(MainFrame.tableColor);
            jTable3.setGridColor(MainFrame.tableGridColor);
            jTable3.setRowHeight(20);
            jTable3.setSelectionBackground(MainFrame.selectColor);
            jTable3.setSelectionForeground(MainFrame.selectForegroundColor);
            jTable3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jTable3.setShowVerticalLines(false);     
            
            // -------- удаление объектов --------------------------------------
            jTable4.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTable4.getColumnModel().getColumn(0).setMinWidth(20);
            jTable4.getColumnModel().getColumn(0).setMaxWidth(20);        
            jTable4.getColumnModel().getColumn(0).setResizable(false);
            
            jTable4.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTable4.getColumnModel().getColumn(1).setMinWidth(80);
            jTable4.getColumnModel().getColumn(1).setMaxWidth(80);        
            jTable4.getColumnModel().getColumn(1).setResizable(false); 
            
            jTable4.getColumnModel().getColumn(2).setPreferredWidth(250);
            jTable4.getColumnModel().getColumn(2).setMinWidth(250);
            jTable4.getColumnModel().getColumn(2).setMaxWidth(250);        
            jTable4.getColumnModel().getColumn(2).setResizable(false); 
                                      
            
            jTable4.setAutoCreateRowSorter(true);
            jTable4.setFont(MainFrame.tableFont);
            jTable4.setBackground(MainFrame.tableColor);
            jTable4.setGridColor(MainFrame.tableGridColor);
            jTable4.setRowHeight(20);
            jTable4.setSelectionBackground(MainFrame.selectColor);
            jTable4.setSelectionForeground(MainFrame.selectForegroundColor);
            jTable4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jTable4.setShowVerticalLines(false);            
    }
    ImageIcon getSessionImage(String s) {
        if (s.equals("1"))
            return warningico;        
        return successico;
    }
    void journalPreset(){
        if (showDeleted.equals("yes"))
            jCheckBox1.setSelected(true);
        else
            jCheckBox1.setSelected(false);
        if (showClosed.equals("yes"))
            jCheckBox2.setSelected(true);
        else
            jCheckBox2.setSelected(false);
                if (!sdate.equals("")){
                    try {
                        calendar.setTime(sdf.parse(sdate));
                        dateChooserCombo4.setSelectedDate(calendar);
                    } catch (ParseException ex) { dateChooserCombo4.setSelectedDate(null); }                    
                } else
                    dateChooserCombo4.setSelectedDate(null);
                if (!edate.equals("")){
                    try {
                        calendar.setTime(sdf.parse(edate));
                        dateChooserCombo5.setSelectedDate(calendar);
                    } catch (ParseException ex) { dateChooserCombo5.setSelectedDate(null); }                                                                                
                } else
                    dateChooserCombo5.setSelectedDate(null);                
                jDesktopPane1.getDesktopManager().maximizeFrame(jInternalFrame1);           
            
    }
    // ******************** проверка зависимостей ******************************
    private int getAddiction(String tab, String col, String ad) {
        int count = 0;                         
        connector.uni_query("SELECT id FROM "+tab+" WHERE "+col+"="+ad);
        System.out.println("SIZE" + connector.res.size());
            if (connector.res.size()>0) {
                for (int j=0; j<connector.res.size(); j++){
                    jTextArea2.append("\n  "+(j+1)+") Table["+tab+"] №"+connector.res.get(j)[0]);
                    count++;
                    }                        
                }            
        return count;
    }
    // ************   Удаление строк таблицы
    void removeAllRows(DefaultTableModel tm){
        for (int i=tm.getRowCount(); i>0; i--)
                tm.removeRow(i-1);        
    }
    // **********  Заполнить таблицу журнала данными из запроса
    void insertJData(DefaultTableModel tm, boolean isIm, int col){
        for (int i =0; i<connector.res.size(); i++){
            tm.addRow(connector.res.get(i));
                if (isIm) tm.setValueAt(jpl.getImage(connector.res.get(i)[col].toString()), i, col);
                if (col == 6) sessionModel.setValueAt(getSessionImage(connector.res.get(i)[col].toString()), i, col);
        }
        connector.res.clear();
    }   
    // *****************  Вставить данные из диалога
    void ins(StringBuffer qr){
            try {
                connector.uni_execute(qr.toString());
                } catch (SQLException ex) {
                    exec = false;
                    q.setLength(0);
                    JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}        
            exec = false;
            q.setLength(0);
    }
    // ********************* заполнить документ данными из базы **********************
    void fillDocRoute(){                
                    Date date;
                    calendar.clear();
                    int row = jpl.getTable().getRowSorter().convertRowIndexToModel(jpl.getTable().getSelectedRow());
                    //номер документа                    
                    dr.setRouteId(jpl.getModel().getValueAt(row, 1).toString());
                    connector.uni_query("SELECT DATE_FORMAT(date, '%d.%m.%Y'), organization_id, car_id, route.driver_id, DATE_FORMAT(date_start, '%d.%m.%Y'),"
                            + " odo_start, gsm_start, gsm_get, DATE_FORMAT(date_end, '%d.%m.%Y'), odo_end, gsm_end, manager_id, coment, route.status, special_start, "
                            + "special_end, soprovod_1, soprovod_2, volume_route, direction_id, ezdki, distance_route, gruz, fact_time_start, fact_time_end, time_start, "
                            + "time_end, shift "
                            + "FROM route WHERE route.id="+dr.getRouteId());                    
                    // Дата документа
                    if (!connector.res.get(0)[0].equals("")){                        
                        try {                                 
                            date = sdf.parse(connector.res.get(0)[0].toString());                             
                            calendar.setTime(date);
                            dr.setDate(calendar);                            
                    } catch (ParseException ex) {System.out.println(ex);}
                    }
                    else 
                        dr.rDateClear();
                    // Организация
                    dr.setOrg(connector.res.get(0)[1].toString());
                    // Автомобиль
                    dr.setCarID(connector.res.get(0)[2].toString());
                    // Водитель
                    dr.setDriverId(connector.res.get(0)[3].toString());
                    // Дата выезда
                    if (!connector.res.get(0)[4].equals("")){                        
                        try {
                            date = sdf.parse(connector.res.get(0)[4].toString());                            
                            calendar.setTime(date);
                            dr.setDateStart(calendar);                                                       
                    } catch (ParseException ex) {System.out.println(ex);}
                    }
                    else
                        dr.dateStartClear();
                    // Спидометр выезда
                    dr.setOdoStart(connector.res.get(0)[5].toString());
                    // ГСМ выезда
                    dr.setGSMStart(connector.res.get(0)[6].toString());
                    // ГСМ выдано
                    dr.setGSMGet(connector.res.get(0)[7].toString());
                    // Дата вызвращения
                    if (!connector.res.get(0)[8].equals(""))
                        try {                            
                            date = sdf.parse(connector.res.get(0)[8].toString());
                            calendar.setTime(date);
                            dr.setDateEnd(calendar);
                    } catch (ParseException ex) {System.out.println(ex);}
                    else
                        dr.dateEndClear();
                    // Спидометр возвращения
                    dr.setOdoEnd(connector.res.get(0)[9].toString());
                    // ГСМ возвращения
                    dr.setGSMEnd(connector.res.get(0)[10].toString());
                    // Диспетчер
                    dr.setManagerId(connector.res.get(0)[11].toString());
                    // Комментарий
                    dr.setComment(connector.res.get(0)[12].toString());
                    // Статус
                    dr.setStatus(connector.res.get(0)[13].toString());
                    // Моточасы выезда
                    dr.setSpecStart(connector.res.get(0)[14].toString());
                    // Моточасы Возврата
                    dr.setSpecEnd(connector.res.get(0)[15].toString());
                    // Сопровождающий 1
                    dr.setSopr1(connector.res.get(0)[16].toString());
                    // Сопровождающий 2
                    dr.setSopr2(connector.res.get(0)[17].toString());
                    // Объем
                    dr.setVolume(connector.res.get(0)[18].toString());
                    // Маршрут
                    dr.setDirection(connector.res.get(0)[19].toString());
                    // Ездки
                    dr.setEzdki(connector.res.get(0)[20].toString());
                    // Расстояние
                    dr.setDist(connector.res.get(0)[21].toString());
                    // Груз
                    dr.setGruz(connector.res.get(0)[22].toString());
                    // Факт время выезда
                    dr.setTimefStart(connector.res.get(0)[23].toString());
                    // Факт время возвращения
                    dr.setTimefEnd(connector.res.get(0)[24].toString());
                    // Время выезда
                    dr.setTimeStart(connector.res.get(0)[25].toString());
                    // Время возвращения
                    dr.setTimeEnd(connector.res.get(0)[26].toString()); 
                    // Смена
                    dr.setShift(connector.res.get(0)[27].toString());
                    connector.res.clear();
    } 
    void delDocument() {
        int row = jpl.getTable().getRowSorter().convertRowIndexToModel(jpl.getTable().getSelectedRow());
        if (!jpl.isDeleted(jpl.getModel().getValueAt(row, 0).toString())) {
            int rval = JOptionPane.showOptionDialog(this, "Вы действительно хотите удалить документ?", "Удаление", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, warning, yesnoArray, yesnoArray[0]);
            if (rval == JOptionPane.YES_OPTION) {
//                System.out.println("Удаляем документ №"+jpl.getModel().getValueAt(row, 1));
                try {
                    connector.uni_execute("UPDATE route SET status=3 WHERE id="+jpl.getModel().getValueAt(row, 1));
                    jpl.getModel().setValueAt(jpl.getImage("3"), row, 0);
                    jTextArea1.append("\nДокумент [Путевой лист] №"+jpl.getModel().getValueAt(row, 1)+" помечен на удаление.");
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jToolBar12 = new javax.swing.JToolBar();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        dateChooserCombo5 = new datechooser.beans.DateChooserCombo();
        jLabel38 = new javax.swing.JLabel();
        dateChooserCombo4 = new datechooser.beans.DateChooserCombo();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jToolBar2 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jInternalFrame3 = new javax.swing.JInternalFrame();
        jToolBar3 = new javax.swing.JToolBar();
        jButton3 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jInternalFrame4 = new javax.swing.JInternalFrame();
        jToolBar4 = new javax.swing.JToolBar();
        jButton4 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jInternalFrame5 = new javax.swing.JInternalFrame();
        jToolBar5 = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jTextField9 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jInternalFrame6 = new javax.swing.JInternalFrame();
        jToolBar6 = new javax.swing.JToolBar();
        jButton6 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jInternalFrame7 = new javax.swing.JInternalFrame();
        jToolBar7 = new javax.swing.JToolBar();
        jButton7 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jInternalFrame8 = new javax.swing.JInternalFrame();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jInternalFrame9 = new javax.swing.JInternalFrame();
        jToolBar8 = new javax.swing.JToolBar();
        jButton11 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        jInternalFrame10 = new javax.swing.JInternalFrame();
        jToolBar9 = new javax.swing.JToolBar();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jLabel4 = new javax.swing.JLabel();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        jInternalFrame11 = new javax.swing.JInternalFrame();
        jToolBar10 = new javax.swing.JToolBar();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        dateChooserCombo3 = new datechooser.beans.DateChooserCombo();
        jLabel10 = new javax.swing.JLabel();
        dateChooserCombo6 = new datechooser.beans.DateChooserCombo();
        jScrollPane12 = new javax.swing.JScrollPane();
        jInternalFrame12 = new javax.swing.JInternalFrame();
        jToolBar11 = new javax.swing.JToolBar();
        jButton29 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel14 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jTextField13 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField14 = new javax.swing.JTextField();
        jButton31 = new javax.swing.JButton();
        jTextField15 = new javax.swing.JTextField();
        jButton32 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jButton34 = new javax.swing.JButton();
        jInternalFrame13 = new javax.swing.JInternalFrame();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jInternalFrame14 = new javax.swing.JInternalFrame();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jInternalFrame15 = new javax.swing.JInternalFrame();
        jToolBar13 = new javax.swing.JToolBar();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();

        jMenuItem20.setText("jMenuItem20");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("JCC(r) ver" + version);
        setPreferredSize(new java.awt.Dimension(1024, 768));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane9.setViewportView(jTextArea1);

        jPanel1.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setDoubleBuffered(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/journal.png"))); // NOI18N
        jButton1.setToolTipText("Журнал \"Путевые листы\"");
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/docroute.png"))); // NOI18N
        jButton9.setToolTipText("Новый документ \"Путевой лист\"");
        jButton9.setFocusPainted(false);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jDesktopPane1.setBackground(new java.awt.Color(204, 204, 204));
        jDesktopPane1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jDesktopPane1.setDoubleBuffered(true);

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setResizable(true);
        jInternalFrame1.setDoubleBuffered(true);
        jInternalFrame1.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/journalico.png"))); // NOI18N
        jInternalFrame1.setMinimumSize(new java.awt.Dimension(1280, 768));
        jInternalFrame1.setName(""); // NOI18N
        jInternalFrame1.setPreferredSize(new java.awt.Dimension(1280, 768));
        jInternalFrame1.setVisible(false);
        jInternalFrame1.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                jInternalFrame1InternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setViewportView(jpl.getTable());
        jScrollPane2.setAutoscrolls(true);
        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jInternalFrame1.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel8.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanel8.setPreferredSize(new java.awt.Dimension(1936, 100));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jToolBar12.setRollover(true);

        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
        jButton27.setToolTipText("Обновить");
        jButton27.setFocusPainted(false);
        jButton27.setFocusable(false);
        jButton27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton27.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jToolBar12.add(jButton27);

        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/save.png"))); // NOI18N
        jButton28.setToolTipText("Сохранить");
        jButton28.setFocusPainted(false);
        jButton28.setFocusable(false);
        jButton28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton28.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jToolBar12.add(jButton28);

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/settingsbig.png"))); // NOI18N
        jButton26.setToolTipText("Настройки отображения");
        jButton26.setFocusPainted(false);
        jButton26.setFocusable(false);
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton26.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jToolBar12.add(jButton26);

        jPanel8.add(jToolBar12, java.awt.BorderLayout.PAGE_START);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel37.setText("Период с");

        dateChooserCombo5.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        dateChooserCombo5.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(187, 187, 187),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    try {
        dateChooserCombo5.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dateChooserCombo5.setFormat(2);
    dateChooserCombo5.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
        public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
            dateChooserCombo5OnSelectionChange(evt);
        }
    });
    dateChooserCombo5.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dateChooserCombo5OnCommit(evt);
        }
    });

    jLabel38.setText("по");

    dateChooserCombo4.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    try {
        dateChooserCombo4.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dateChooserCombo4.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
        public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
            dateChooserCombo4OnSelectionChange(evt);
        }
    });
    dateChooserCombo4.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dateChooserCombo4OnCommit(evt);
        }
    });

    jCheckBox1.setText("Показывать удаленные");
    jCheckBox1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxuncheck.png"))); // NOI18N
    jCheckBox1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxcheck.png"))); // NOI18N
    jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jCheckBox1ActionPerformed(evt);
        }
    });

    jCheckBox2.setText("Показывать закрытые");
    jCheckBox2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxuncheck.png"))); // NOI18N
    jCheckBox2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxcheck.png"))); // NOI18N
    jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jCheckBox2ActionPerformed(evt);
        }
    });

    jTextField1.setToolTipText("Введите текст поиска");
    jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            jTextField1KeyPressed(evt);
        }
    });

    jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/delete-text_.png"))); // NOI18N
    jButton8.setToolTipText("Очистить фильтр");
    jButton8.setFocusPainted(false);
    jButton8.setPreferredSize(new java.awt.Dimension(54, 20));
    jButton8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton8ActionPerformed(evt);
        }
    });

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/filter.png"))); // NOI18N

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel37)
                .addComponent(jLabel1))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(dateChooserCombo4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel38)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dateChooserCombo5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 713, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jCheckBox2)
                .addComponent(jCheckBox1))
            .addContainerGap())
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(jCheckBox1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jCheckBox2))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateChooserCombo4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateChooserCombo5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel8.add(jPanel9, java.awt.BorderLayout.CENTER);

    jInternalFrame1.getContentPane().add(jPanel8, java.awt.BorderLayout.PAGE_START);

    jDesktopPane1.add(jInternalFrame1);
    jInternalFrame1.setBounds(50, 50, 1280, 768);
    jInternalFrame1.getAccessibleContext().setAccessibleDescription("Журнал пут листы");
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame1);

    jInternalFrame2.setClosable(true);
    jInternalFrame2.setDoubleBuffered(true);
    jInternalFrame2.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/listico.png"))); // NOI18N
    jInternalFrame2.setPreferredSize(new java.awt.Dimension(600, 400));
    jInternalFrame2.setVisible(false);

    jToolBar2.setRollover(true);

    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/add.png"))); // NOI18N
    jButton2.setToolTipText("Новый");
    jButton2.setFocusPainted(false);
    jButton2.setFocusable(false);
    jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });
    jToolBar2.add(jButton2);

    jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
    jButton19.setToolTipText("Обновить");
    jButton19.setFocusPainted(false);
    jButton19.setFocusable(false);
    jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton19.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton19ActionPerformed(evt);
        }
    });
    jToolBar2.add(jButton19);

    jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/filter.png"))); // NOI18N
    jButton10.setBorderPainted(false);
    jButton10.setEnabled(false);
    jButton10.setFocusPainted(false);
    jButton10.setFocusable(false);
    jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar2.add(jButton10);

    jTextField2.setToolTipText("Введите текст поиска");
    jToolBar2.add(jTextField2);

    jInternalFrame2.getContentPane().add(jToolBar2, java.awt.BorderLayout.PAGE_START);

    jScrollPane1.setViewportView(sct.getTable());
    jInternalFrame2.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame2);
    jInternalFrame2.setBounds(55, 55, 600, 400);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame2);
    jInternalFrame2.getAccessibleContext().setAccessibleDescription("Типы ТС");

    jInternalFrame3.setClosable(true);
    jInternalFrame3.setDoubleBuffered(true);
    jInternalFrame3.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/listico.png"))); // NOI18N
    jInternalFrame3.setPreferredSize(new java.awt.Dimension(800, 600));
    jInternalFrame3.setVisible(false);

    jToolBar3.setRollover(true);

    jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/add.png"))); // NOI18N
    jButton3.setToolTipText("Новый");
    jButton3.setFocusPainted(false);
    jButton3.setFocusable(false);
    jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
        }
    });
    jToolBar3.add(jButton3);

    jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
    jButton20.setToolTipText("Обновить");
    jButton20.setFocusPainted(false);
    jButton20.setFocusable(false);
    jButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton20.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton20ActionPerformed(evt);
        }
    });
    jToolBar3.add(jButton20);

    jInternalFrame3.getContentPane().add(jToolBar3, java.awt.BorderLayout.PAGE_START);

    jScrollPane3.setViewportView(su.getTable());
    jInternalFrame3.getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame3);
    jInternalFrame3.setBounds(60, 60, 800, 600);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame3);
    jInternalFrame3.getAccessibleContext().setAccessibleDescription("справочник пользователи");

    jInternalFrame4.setClosable(true);
    jInternalFrame4.setDoubleBuffered(true);
    jInternalFrame4.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/listico.png"))); // NOI18N
    jInternalFrame4.setPreferredSize(new java.awt.Dimension(800, 600));
    try {
        jInternalFrame4.setSelected(true);
    } catch (java.beans.PropertyVetoException e1) {
        e1.printStackTrace();
    }
    jInternalFrame4.setVisible(false);

    jToolBar4.setRollover(true);

    jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/add.png"))); // NOI18N
    jButton4.setToolTipText("Новый");
    jButton4.setFocusPainted(false);
    jButton4.setFocusable(false);
    jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton4ActionPerformed(evt);
        }
    });
    jToolBar4.add(jButton4);

    jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
    jButton21.setToolTipText("Обновить");
    jButton21.setFocusPainted(false);
    jButton21.setFocusable(false);
    jButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton21.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton21.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton21ActionPerformed(evt);
        }
    });
    jToolBar4.add(jButton21);

    jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/filter.png"))); // NOI18N
    jButton14.setEnabled(false);
    jButton14.setFocusPainted(false);
    jButton14.setFocusable(false);
    jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar4.add(jButton14);
    jToolBar4.add(jTextField8);

    jInternalFrame4.getContentPane().add(jToolBar4, java.awt.BorderLayout.PAGE_START);

    jScrollPane4.setViewportView(scd.getTable());
    jInternalFrame4.getContentPane().add(jScrollPane4, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame4);
    jInternalFrame4.setBounds(65, 65, 800, 600);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame4);
    jInternalFrame4.getAccessibleContext().setAccessibleDescription("Водители и сопровождающие");

    jInternalFrame5.setClosable(true);
    jInternalFrame5.setDoubleBuffered(true);
    jInternalFrame5.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/listico.png"))); // NOI18N
    jInternalFrame5.setMinimumSize(new java.awt.Dimension(800, 600));
    jInternalFrame5.setPreferredSize(new java.awt.Dimension(1024, 768));
    jInternalFrame5.setVisible(false);

    jToolBar5.setRollover(true);

    jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/add.png"))); // NOI18N
    jButton5.setToolTipText("Новый");
    jButton5.setFocusPainted(false);
    jButton5.setFocusable(false);
    jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton5.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton5ActionPerformed(evt);
        }
    });
    jToolBar5.add(jButton5);

    jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
    jButton22.setToolTipText("Обновить");
    jButton22.setFocusPainted(false);
    jButton22.setFocusable(false);
    jButton22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton22.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton22.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton22ActionPerformed(evt);
        }
    });
    jToolBar5.add(jButton22);

    jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/filter.png"))); // NOI18N
    jButton18.setEnabled(false);
    jButton18.setFocusPainted(false);
    jButton18.setFocusable(false);
    jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToolBar5.add(jButton18);
    jToolBar5.add(jTextField9);

    jInternalFrame5.getContentPane().add(jToolBar5, java.awt.BorderLayout.PAGE_START);

    jScrollPane5.setViewportView(sc.getTable());
    jInternalFrame5.getContentPane().add(jScrollPane5, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame5);
    jInternalFrame5.setBounds(70, 70, 1024, 768);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame5);
    jInternalFrame5.getAccessibleContext().setAccessibleDescription("Справочник автомобили");

    jInternalFrame6.setClosable(true);
    jInternalFrame6.setDoubleBuffered(true);
    jInternalFrame6.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/listico.png"))); // NOI18N
    jInternalFrame6.setPreferredSize(new java.awt.Dimension(800, 600));
    jInternalFrame6.setRequestFocusEnabled(false);
    jInternalFrame6.setVisible(false);

    jToolBar6.setRollover(true);

    jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/add.png"))); // NOI18N
    jButton6.setToolTipText("Новый");
    jButton6.setFocusPainted(false);
    jButton6.setFocusable(false);
    jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton6.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton6ActionPerformed(evt);
        }
    });
    jToolBar6.add(jButton6);

    jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
    jButton23.setToolTipText("Обновить");
    jButton23.setFocusPainted(false);
    jButton23.setFocusable(false);
    jButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton23.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton23.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton23ActionPerformed(evt);
        }
    });
    jToolBar6.add(jButton23);

    jInternalFrame6.getContentPane().add(jToolBar6, java.awt.BorderLayout.PAGE_START);

    jScrollPane6.setViewportView(sgsm.getTable());
    jInternalFrame6.getContentPane().add(jScrollPane6, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame6);
    jInternalFrame6.setBounds(75, 75, 800, 600);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame6);
    jInternalFrame6.getAccessibleContext().setAccessibleDescription("Справочник типы топлива");

    jInternalFrame7.setClosable(true);
    jInternalFrame7.setDoubleBuffered(true);
    jInternalFrame7.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/listico.png"))); // NOI18N
    jInternalFrame7.setPreferredSize(new java.awt.Dimension(800, 600));
    jInternalFrame7.setVisible(false);

    jToolBar7.setRollover(true);

    jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/add.png"))); // NOI18N
    jButton7.setToolTipText("Новый");
    jButton7.setFocusPainted(false);
    jButton7.setFocusable(false);
    jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton7ActionPerformed(evt);
        }
    });
    jToolBar7.add(jButton7);

    jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
    jButton24.setToolTipText("Обновить");
    jButton24.setFocusPainted(false);
    jButton24.setFocusable(false);
    jButton24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton24.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton24.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton24ActionPerformed(evt);
        }
    });
    jToolBar7.add(jButton24);

    jInternalFrame7.getContentPane().add(jToolBar7, java.awt.BorderLayout.PAGE_START);

    jScrollPane7.setViewportView(sd.getTable());
    jInternalFrame7.getContentPane().add(jScrollPane7, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame7);
    jInternalFrame7.setBounds(75, 75, 800, 600);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame7);
    jInternalFrame7.getAccessibleContext().setAccessibleDescription("Справочник маршруты");

    jInternalFrame8.setClosable(true);
    jInternalFrame8.setTitle("Конфигурация");
    jInternalFrame8.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/settings.png"))); // NOI18N
    jInternalFrame8.setVisible(false);

    jTable1.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null},
            {null, null}
        },
        new String [] {
            "Ключ", "Значение"
        }
    ));
    jScrollPane8.setViewportView(jTable1);

    jInternalFrame8.getContentPane().add(jScrollPane8, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame8);
    jInternalFrame8.setBounds(180, 10, 440, 380);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame8);
    jInternalFrame8.getAccessibleContext().setAccessibleName("");
    jInternalFrame8.getAccessibleContext().setAccessibleDescription("Конфигурация");

    jInternalFrame9.setClosable(true);
    jInternalFrame9.setToolTipText("");
    jInternalFrame9.setDoubleBuffered(true);
    jInternalFrame9.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/listico.png"))); // NOI18N
    jInternalFrame9.setPreferredSize(new java.awt.Dimension(800, 400));
    jInternalFrame9.setVisible(false);

    jToolBar8.setRollover(true);

    jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/add.png"))); // NOI18N
    jButton11.setToolTipText("Новый");
    jButton11.setFocusPainted(false);
    jButton11.setFocusable(false);
    jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton11.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton11ActionPerformed(evt);
        }
    });
    jToolBar8.add(jButton11);

    jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/refresh.png"))); // NOI18N
    jButton25.setToolTipText("Обновить");
    jButton25.setFocusPainted(false);
    jButton25.setFocusable(false);
    jButton25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton25.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton25.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton25ActionPerformed(evt);
        }
    });
    jToolBar8.add(jButton25);

    jInternalFrame9.getContentPane().add(jToolBar8, java.awt.BorderLayout.PAGE_START);

    jScrollPane10.setViewportView(ss.getTable());
    jInternalFrame9.getContentPane().add(jScrollPane10, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame9);
    jInternalFrame9.setBounds(160, 50, 800, 400);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame9);
    jInternalFrame9.getAccessibleContext().setAccessibleDescription("Смены");

    jInternalFrame10.setClosable(true);
    jInternalFrame10.setIconifiable(true);
    jInternalFrame10.setMaximizable(true);
    jInternalFrame10.setResizable(true);
    jInternalFrame10.setTitle("Отчет 2");
    jInternalFrame10.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/report.png"))); // NOI18N
    jInternalFrame10.setVisible(false);

    jToolBar9.setRollover(true);

    jButton12.setText("Сформировать");
    jButton12.setFocusable(false);
    jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton12.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton12ActionPerformed(evt);
        }
    });
    jToolBar9.add(jButton12);

    jButton13.setText("Сохранить");
    jButton13.setFocusable(false);
    jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton13.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton13ActionPerformed(evt);
        }
    });
    jToolBar9.add(jButton13);

    jInternalFrame10.getContentPane().add(jToolBar9, java.awt.BorderLayout.PAGE_START);

    jPanel4.setLayout(new java.awt.BorderLayout());

    jLabel3.setText("С");

    dateChooserCombo1.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    try {
        dateChooserCombo1.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }

    jLabel4.setText("по");

    dateChooserCombo2.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    try {
        dateChooserCombo2.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }

    jLabel5.setText("Автомобиль");

    jTextField4.setEditable(false);

    jButton15.setText("...");
    jButton15.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton15ActionPerformed(evt);
        }
    });

    jLabel6.setText("Пробег");

    jTextField5.setEditable(false);

    jLabel7.setText("Расход");

    jTextField6.setEditable(false);

    jLabel8.setText("Дозапрвка");

    jTextField7.setEditable(false);

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel8)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(154, Short.MAX_VALUE))
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15))
                .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel4)
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel3))
            .addGap(18, 18, 18)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(43, Short.MAX_VALUE))
    );

    jPanel4.add(jPanel5, java.awt.BorderLayout.PAGE_START);

    jScrollPane11.setViewportView(reportGSM.getTable());

    jPanel4.add(jScrollPane11, java.awt.BorderLayout.CENTER);

    jInternalFrame10.getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame10);
    jInternalFrame10.setBounds(60, 30, 730, 360);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame10);
    jInternalFrame10.getAccessibleContext().setAccessibleName("");
    jInternalFrame10.getAccessibleContext().setAccessibleDescription("Отчет 2");

    jInternalFrame11.setClosable(true);
    jInternalFrame11.setIconifiable(true);
    jInternalFrame11.setMaximizable(true);
    jInternalFrame11.setResizable(true);
    jInternalFrame11.setTitle("Отчет 1");
    jInternalFrame11.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/report.png"))); // NOI18N
    jInternalFrame11.setVisible(false);

    jToolBar10.setRollover(true);

    jButton16.setText("Сформировать");
    jButton16.setFocusable(false);
    jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton16.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton16ActionPerformed(evt);
        }
    });
    jToolBar10.add(jButton16);

    jButton17.setText("Сохранить");
    jButton17.setFocusable(false);
    jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton17.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton17ActionPerformed(evt);
        }
    });
    jToolBar10.add(jButton17);

    jInternalFrame11.getContentPane().add(jToolBar10, java.awt.BorderLayout.PAGE_START);

    jPanel6.setLayout(new java.awt.BorderLayout());

    jPanel7.setPreferredSize(new java.awt.Dimension(694, 60));

    jLabel9.setText("Период с");

    dateChooserCombo3.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    try {
        dateChooserCombo3.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }

    jLabel10.setText("по");

    dateChooserCombo6.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    try {
        dateChooserCombo6.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel9)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel10)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(dateChooserCombo6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(368, Short.MAX_VALUE))
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(dateChooserCombo6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel10)
                .addComponent(dateChooserCombo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel9))
            .addContainerGap(29, Short.MAX_VALUE))
    );

    jPanel6.add(jPanel7, java.awt.BorderLayout.PAGE_START);

    jScrollPane12.setViewportView(reportGSM1.getTable());

    jPanel6.add(jScrollPane12, java.awt.BorderLayout.CENTER);

    jInternalFrame11.getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame11);
    jInternalFrame11.setBounds(70, 30, 710, 350);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame11);
    jInternalFrame11.getAccessibleContext().setAccessibleName("");
    jInternalFrame11.getAccessibleContext().setAccessibleDescription("Отчет 1");

    jInternalFrame12.setClosable(true);
    jInternalFrame12.setTitle("Настроки");
    jInternalFrame12.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/settings.png"))); // NOI18N
    jInternalFrame12.setVisible(false);

    jToolBar11.setRollover(true);

    jButton29.setText("Сохранить");
    jButton29.setFocusable(false);
    jButton29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton29.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton29.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton29ActionPerformed(evt);
        }
    });
    jToolBar11.add(jButton29);

    jInternalFrame12.getContentPane().add(jToolBar11, java.awt.BorderLayout.PAGE_START);

    jLabel2.setText("База данных");

    jLabel11.setText("Сервер");

    jLabel12.setText("Порт");

    jLabel13.setText("Пароль");

    jLabel14.setText("Драйвер");

    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel2)
                .addComponent(jLabel12)
                .addComponent(jLabel11)
                .addComponent(jLabel13)
                .addComponent(jLabel14))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPasswordField1)
                .addComponent(jTextField3)
                .addComponent(jTextField11)
                .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(244, Short.MAX_VALUE))
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel11)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel12)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel13)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel14)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(84, Short.MAX_VALUE))
    );

    jTabbedPane2.addTab("База данных", jPanel10);

    jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jList1.setModel(listModel);
    jScrollPane13.setViewportView(jList1);

    jCheckBox3.setText("Жирный");
    jCheckBox3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxuncheck.png"))); // NOI18N
    jCheckBox3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxcheck.png"))); // NOI18N

    jCheckBox4.setText("Курсив");
    jCheckBox4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxuncheck.png"))); // NOI18N
    jCheckBox4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/checkboxcheck.png"))); // NOI18N

    jLabel15.setText("Размер");

    javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
    jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane13)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField13)
                        .addComponent(jCheckBox3))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel15)
                        .addComponent(jCheckBox4))
                    .addGap(62, 62, 62)))
            .addContainerGap())
    );
    jPanel12Layout.setVerticalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel15))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jCheckBox3)
                .addComponent(jCheckBox4))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jButton31.setText("...");
    jButton31.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton31ActionPerformed(evt);
        }
    });

    jButton32.setText("...");
    jButton32.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton32ActionPerformed(evt);
        }
    });

    jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel16.setText("O");

    jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel17.setText("O");

    jLabel18.setText("Цвет выделения");

    jLabel19.setText("Цвет выделенного текста");

    jLabel20.setText("Цвет таблиц");

    jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel21.setText("O");

    jButton33.setText("...");
    jButton33.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton33ActionPerformed(evt);
        }
    });

    jLabel22.setText("Цвет сетки");

    jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel23.setText("O");

    jButton34.setText("...");
    jButton34.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton34ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                .addComponent(jTextField14))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton32)
                                .addComponent(jButton31)))
                        .addComponent(jLabel18)
                        .addComponent(jLabel19)
                        .addComponent(jLabel20)
                        .addComponent(jLabel22))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jTextField16, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton33))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton34)))
                    .addGap(16, 16, 16))))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel18)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton31))
                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel19)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton32)
                .addComponent(jLabel17))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel20)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton33)
                .addComponent(jLabel21))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel22)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton34)
                .addComponent(jLabel23))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel11Layout.setVerticalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    jTabbedPane2.addTab("Шрифты и Цвета", jPanel11);

    jInternalFrame12.getContentPane().add(jTabbedPane2, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame12);
    jInternalFrame12.setBounds(150, 30, 530, 320);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame12);
    jInternalFrame12.getAccessibleContext().setAccessibleDescription("");

    jInternalFrame13.setClosable(true);
    jInternalFrame13.setIconifiable(true);
    jInternalFrame13.setTitle("Журнал [Регистрации]");
    jInternalFrame13.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/registered.png"))); // NOI18N
    jInternalFrame13.setPreferredSize(new java.awt.Dimension(1110, 600));
    jInternalFrame13.setVisible(false);

    jPanel13.setLayout(new java.awt.BorderLayout());

    jTable2.setModel(sessionModel);
    jScrollPane14.setViewportView(jTable2);

    jPanel13.add(jScrollPane14, java.awt.BorderLayout.CENTER);

    jInternalFrame13.getContentPane().add(jPanel13, java.awt.BorderLayout.CENTER);

    jDesktopPane1.add(jInternalFrame13);
    jInternalFrame13.setBounds(90, 30, 1110, 600);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame13);
    jInternalFrame13.getAccessibleContext().setAccessibleDescription("");

    jInternalFrame14.setClosable(true);
    jInternalFrame14.setIconifiable(true);
    jInternalFrame14.setTitle("Активные сеансы");
    jInternalFrame14.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/targetico.png"))); // NOI18N
    jInternalFrame14.setPreferredSize(new java.awt.Dimension(800, 600));
    jInternalFrame14.setVisible(false);

    jTable3.setModel(sessionActiveModel);
    jScrollPane15.setViewportView(jTable3);

    jInternalFrame14.getContentPane().add(jScrollPane15, java.awt.BorderLayout.CENTER);

    jPanel14.setPreferredSize(new java.awt.Dimension(574, 40));

    jLabel24.setText("ID процесса:");

    jButton30.setText("Удалить");
    jButton30.setToolTipText("Убить процесс");
    jButton30.setFocusPainted(false);
    jButton30.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton30ActionPerformed(evt);
        }
    });

    jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

    javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
    jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap(564, Short.MAX_VALUE)
            .addComponent(jLabel24)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton30)
            .addContainerGap())
    );
    jPanel14Layout.setVerticalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton30)
                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel24))
            .addContainerGap())
    );

    jInternalFrame14.getContentPane().add(jPanel14, java.awt.BorderLayout.PAGE_END);

    jDesktopPane1.add(jInternalFrame14);
    jInternalFrame14.setBounds(150, 30, 800, 320);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame14);
    jInternalFrame14.getAccessibleContext().setAccessibleDescription("");

    jInternalFrame15.setClosable(true);
    jInternalFrame15.setTitle("Удаление объектов");
    jInternalFrame15.setVisible(false);

    jToolBar13.setRollover(true);

    jButton35.setBackground(new java.awt.Color(255, 102, 102));
    jButton35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton35.setText(" Удалить ");
    jButton35.setToolTipText("Удалить выделенные");
    jButton35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    jButton35.setFocusable(false);
    jButton35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton35.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton35.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton35ActionPerformed(evt);
        }
    });
    jToolBar13.add(jButton35);

    jButton36.setBackground(new java.awt.Color(153, 255, 153));
    jButton36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton36.setText(" Восстановить ");
    jButton36.setToolTipText("Восстановить выделенные");
    jButton36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    jButton36.setFocusable(false);
    jButton36.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton36.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton36.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton36ActionPerformed(evt);
        }
    });
    jToolBar13.add(jButton36);

    jButton37.setBackground(new java.awt.Color(153, 204, 255));
    jButton37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton37.setText(" Выделить все ");
    jButton37.setToolTipText("Пометить все");
    jButton37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    jButton37.setFocusPainted(false);
    jButton37.setFocusable(false);
    jButton37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton37.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton37.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton37ActionPerformed(evt);
        }
    });
    jToolBar13.add(jButton37);

    jButton38.setBackground(new java.awt.Color(153, 204, 255));
    jButton38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton38.setText(" Снять выделение ");
    jButton38.setToolTipText("Снять выделение");
    jButton38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    jButton38.setFocusable(false);
    jButton38.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton38.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton38.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton38ActionPerformed(evt);
        }
    });
    jToolBar13.add(jButton38);

    jInternalFrame15.getContentPane().add(jToolBar13, java.awt.BorderLayout.PAGE_START);

    jPanel15.setLayout(new java.awt.BorderLayout());

    jTable4.setModel(deleteModel);
    jScrollPane17.setViewportView(jTable4);

    jPanel15.add(jScrollPane17, java.awt.BorderLayout.CENTER);

    jInternalFrame15.getContentPane().add(jPanel15, java.awt.BorderLayout.CENTER);

    jTextArea2.setEditable(false);
    jTextArea2.setColumns(20);
    jTextArea2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jTextArea2.setRows(10);
    jTextArea2.setDoubleBuffered(true);
    jTextArea2.setFocusable(false);
    jTextArea2.setRequestFocusEnabled(false);
    jScrollPane16.setViewportView(jTextArea2);

    jInternalFrame15.getContentPane().add(jScrollPane16, java.awt.BorderLayout.PAGE_END);

    jDesktopPane1.add(jInternalFrame15);
    jInternalFrame15.setBounds(160, 50, 680, 410);
    jDesktopPane1.getDesktopManager().closeFrame(jInternalFrame15);
    jInternalFrame15.getAccessibleContext().setAccessibleDescription("");

    getContentPane().add(jDesktopPane1, java.awt.BorderLayout.CENTER);

    jMenu1.setText("Файл");
    jMenu1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

    jMenuItem1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/exitico.png"))); // NOI18N
    jMenuItem1.setText("Выход");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem1ActionPerformed(evt);
        }
    });
    jMenu1.add(jMenuItem1);

    jMenuBar1.add(jMenu1);

    jMenu2.setText("Журналы");
    jMenu2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

    jMenuItem2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/journalico.png"))); // NOI18N
    jMenuItem2.setText("Путевые листы");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem2ActionPerformed(evt);
        }
    });
    jMenu2.add(jMenuItem2);

    jMenuItem21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/registered.png"))); // NOI18N
    jMenuItem21.setText("Регистрации");
    jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem21ActionPerformed(evt);
        }
    });
    jMenu2.add(jMenuItem21);

    jMenuBar1.add(jMenu2);

    jMenu7.setText("Документы");
    jMenu7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

    jMenuItem17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/docrouteico.png"))); // NOI18N
    jMenuItem17.setText("Путевой лист новый");
    jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem17ActionPerformed(evt);
        }
    });
    jMenu7.add(jMenuItem17);

    jMenuBar1.add(jMenu7);

    jMenu3.setText("Справочники");
    jMenu3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

    jMenuItem5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem5.setText("Автомобили");
    jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem5ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem5);

    jMenuItem7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem7.setText("Типы ТС");
    jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem7ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem7);

    jMenuItem6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem6.setText("Типы ГСМ");
    jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem6ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem6);
    jMenu3.add(jSeparator2);

    jMenuItem4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem4.setText("Водители");
    jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem4ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem4);

    jMenuItem12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem12.setText("Сопровождающие");
    jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem12ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem12);
    jMenu3.add(jSeparator3);

    jMenuItem8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem8.setText("Маршруты");
    jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem8ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem8);

    jMenuItem9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem9.setText("Организации");
    jMenu3.add(jMenuItem9);

    jMenuItem11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem11.setText("Смены");
    jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem11ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem11);
    jMenu3.add(jSeparator4);

    jMenuItem3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem3.setText("Пользователи");
    jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem3ActionPerformed(evt);
        }
    });
    jMenu3.add(jMenuItem3);

    jMenuBar1.add(jMenu3);

    jMenu5.setText("Отчеты");
    jMenu5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

    jMenuItem14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem14.setText("Движение топлива за период");
    jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem14ActionPerformed(evt);
        }
    });
    jMenu5.add(jMenuItem14);

    jMenuItem15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem15.setText("Расход топлива автомобиля");
    jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem15ActionPerformed(evt);
        }
    });
    jMenu5.add(jMenuItem15);

    jMenuBar1.add(jMenu5);

    jMenu4.setText("Сервис");
    jMenu4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

    jMenuItem13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/backup.png"))); // NOI18N
    jMenuItem13.setText("Выгрузить базу");
    jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem13ActionPerformed(evt);
        }
    });
    jMenu4.add(jMenuItem13);

    jMenuItem23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/deleteico.png"))); // NOI18N
    jMenuItem23.setText("Безопасное удаление");
    jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem23ActionPerformed(evt);
        }
    });
    jMenu4.add(jMenuItem23);
    jMenu4.add(jSeparator1);

    jMenuItem18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/settings.png"))); // NOI18N
    jMenuItem18.setText("Настройка");
    jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem18ActionPerformed(evt);
        }
    });
    jMenu4.add(jMenuItem18);

    jMenuItem10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem10.setText("Конфигурация");
    jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem10ActionPerformed(evt);
        }
    });
    jMenu4.add(jMenuItem10);
    jMenu4.add(jSeparator5);

    jMenuItem19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rjcc/targetico.png"))); // NOI18N
    jMenuItem19.setText("Активные сеансы");
    jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem19ActionPerformed(evt);
        }
    });
    jMenu4.add(jMenuItem19);

    jMenuBar1.add(jMenu4);

    jMenu6.setText("Справка");
    jMenu6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

    jMenuItem16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem16.setText("О программе");
    jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem16ActionPerformed(evt);
        }
    });
    jMenu6.add(jMenuItem16);

    jMenuItem22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jMenuItem22.setText("Проверить обновления");
    jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem22ActionPerformed(evt);
        }
    });
    jMenu6.add(jMenuItem22);

    jMenuBar1.add(jMenu6);

    setJMenuBar(jMenuBar1);

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Журнал путевые листы на тулбаре
        if (!jInternalFrame1.isValid()){
            jDesktopPane1.add(jInternalFrame1);
        jInternalFrame1.setTitle(jpl.getTitle());
        SwingUtilities.invokeLater(() ->{
        if (!jInternalFrame1.isVisible()){
            journalPreset();
            removeAllRows(jpl.getModel());
            connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
            insertJData(jpl.getModel(), true, 0);
            jInternalFrame1.setVisible(true);
            // timer
            }
        });
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // Справочник типы ТС
        if (!jInternalFrame2.isValid()){
            jDesktopPane1.add(jInternalFrame2);
        jInternalFrame2.setTitle(sct.getTitle());
        SwingUtilities.invokeLater(() ->{
        if (!jInternalFrame2.isVisible()){
            removeAllRows(sct.getModel());
            connector.uni_query(sct.getJournal());
            insertJData(sct.getModel(), false, 1);
            jInternalFrame2.setVisible(true);
            }  
            });
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Справочник Пользователи        
        if (!jInternalFrame3.isValid()){
            jDesktopPane1.add(jInternalFrame3);
        jInternalFrame3.setTitle(su.getTitle());
        SwingUtilities.invokeLater(() -> {
        if (!jInternalFrame3.isVisible()){
            removeAllRows(su.getModel());
            connector.uni_query(su.getJournal());
                insertJData(su.getModel(), false, 1);        
            jInternalFrame3.setVisible(true);
            }                    
        });
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // Справочник водители        
        if (!jInternalFrame4.isValid() && !jInternalFrame4.isVisible()){
            jDesktopPane1.add(jInternalFrame4);
        jInternalFrame4.setTitle(scd.getTitle(DRIVERS));
        SwingUtilities.invokeLater(() -> { 
        if (!jInternalFrame4.isVisible()){
            removeAllRows(scd.getModel());
            connector.uni_query(scd.getJournal(DRIVERS));
                insertJData(scd.getModel(), false, 1);        
            jInternalFrame4.setVisible(true);
            }         
        });
        } else {
            jTextArea1.append("\nНе возможно открыть еще одно окно 'Водители' или 'Сопровождающие'. Для этого закройте активное окно.");
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // Справочник автомобили
        if (!jInternalFrame5.isValid()){
            jDesktopPane1.add(jInternalFrame5);
        jInternalFrame5.setTitle(sc.getTitle());
        SwingUtilities.invokeLater(() ->{
        if (!jInternalFrame5.isVisible()){
            removeAllRows(sc.getModel());
            connector.uni_query(sc.getJournal());
                insertJData(sc.getModel(), false, 1);        
            jInternalFrame5.setVisible(true);
            }                 
            });
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // справочник гсм
        if (!jInternalFrame6.isValid()){
            jDesktopPane1.add(jInternalFrame6);
        jInternalFrame6.setTitle(sgsm.getTitle());
        SwingUtilities.invokeLater(() -> {
        if (!jInternalFrame6.isVisible()){
            removeAllRows(sgsm.getModel());
            connector.uni_query(sgsm.getJournal());
                insertJData(sgsm.getModel(), false, 1);        
            jInternalFrame6.setVisible(true);
            }         
        });
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // справочник маршруты
        if (!jInternalFrame7.isValid()){
            jDesktopPane1.add(jInternalFrame7);
        jInternalFrame7.setTitle(sd.getTitle());
        SwingUtilities.invokeLater(() -> {
        if (!jInternalFrame7.isVisible()){
            removeAllRows(sd.getModel());
            connector.uni_query(sd.getJournal());
                insertJData(sd.getModel(), false, 1);        
            jInternalFrame7.setVisible(true);
            }                 
        });
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // Документ путевой лист новый    
            //System.out.println(DocRouteFrame.getFrames().length);            
            SwingUtilities.invokeLater(() -> {
                if (!drf.isVisible()) {                    
                    dr.setDefaultValues();
                    drf = new DocRouteFrame(dr, connector);
                    drf.setVisible(true);
                } else {drf.setState(JFrame.NORMAL);}
            });
//            jButton9.setEnabled(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Новый автомобиль
        new DCar(this, true, connector).setVisible(true);  
        if (exec) {
            ins(q);       
            removeAllRows(sc.getModel());
            connector.uni_query(sc.getJournal());
            insertJData(sc.getModel(), false, 1);        
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Новый тип автомобиля
        new DType(this, true).setVisible(true);
        if (exec) {
            ins(q);
            removeAllRows(sct.getModel());
            connector.uni_query(sct.getJournal());
            insertJData(sct.getModel(), false, 1);        
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // новый тип топлива
        new DGSMType(this, true).setVisible(true);
        if (exec) {
            ins(q);        
            removeAllRows(sgsm.getModel());
            connector.uni_query(sgsm.getJournal());
            insertJData(sgsm.getModel(), false, 1);}
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // новый водитель
        if (scd.getTitleId(jInternalFrame4.getTitle()) == DRIVERS)
            new DDRiver(this, true).setVisible(true);
        else
            new DCarger(this, true).setVisible(true);
        if (exec) {
            ins(q);
            removeAllRows(scd.getModel());
            connector.uni_query(scd.getJournal(scd.getTitleId(jInternalFrame4.getTitle())));
            insertJData(scd.getModel(), false, 1);        
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Новый маршрут
        new DDirection(this, true).setVisible(true);        
        if (exec) {
            ins(q);
            removeAllRows(sd.getModel());
            connector.uni_query(sd.getJournal());
            insertJData(sd.getModel(), false, 1);        
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Закрытие окна
        int close = JOptionPane.showOptionDialog(this, "Выйти из программы?", "Выход", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, help, yesnoArray, yesnoArray[0]);
        if (close == JOptionPane.YES_OPTION){
            try {
                connector.closeSession(sessionId);
                connector.con.close();
                storeProp();
                System.exit(0);
            } catch (SQLException ex) {
                storeProp();
                System.exit(0);                
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Новый пользователь
        new DManager(this, true).setVisible(true);        
        if (exec) {
            ins(q);        
            removeAllRows(su.getModel());
            connector.uni_query(su.getJournal());
            insertJData(su.getModel(), false, 1);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Выход
        int close = JOptionPane.showOptionDialog(this, "Выйти из программы?", "Выход", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, help, yesnoArray, yesnoArray[0]);
        if (close == JOptionPane.YES_OPTION){
                try {
                    connector.closeSession(sessionId);
                    connector.con.close();
                    storeProp();
                    System.exit(0);
                } catch (SQLException ex) {                    
                    storeProp();
                    System.exit(0);}
        }        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Журнал путевые листы меню
        if (!jInternalFrame1.isValid()){
            jDesktopPane1.add(jInternalFrame1);
        jInternalFrame1.setTitle(jpl.getTitle());
        SwingUtilities.invokeLater(() ->{
        if (!jInternalFrame1.isVisible()){
            journalPreset();
            removeAllRows(jpl.getModel());
            connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
            insertJData(jpl.getModel(), true, 0);
            jInternalFrame1.setVisible(true);
            // timer
            }
        });
        }        
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // Показывать удаленные
        if (jCheckBox1.isSelected())
            showDeleted = "yes";
        else
            showDeleted = "no";
        removeAllRows(jpl.getModel());
        connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
        insertJData(jpl.getModel(), true, 0);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // Показывать закрытые
        if (jCheckBox2.isSelected())
            showClosed = "yes";
        else
            showClosed = "no";        
        removeAllRows(jpl.getModel());
        connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
        insertJData(jpl.getModel(), true, 0);        
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void dateChooserCombo4OnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo4OnSelectionChange
        // TODO add your handling code here:
        System.out.println("EVENT");
//        if (dateChooserCombo4.getSelectedDate() != null)
//            sdate = sdf.format(dateChooserCombo4.getSelectedDate().getTime());
//        else
//            sdate = "";
//        removeAllRows(jpl.getModel());
//        connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
//        insertJData(jpl.getModel(), true, 0);        
    }//GEN-LAST:event_dateChooserCombo4OnSelectionChange

    private void dateChooserCombo5OnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo5OnSelectionChange
        // TODO add your handling code here:
        System.out.println("EVENT");
//        if (dateChooserCombo5.getSelectedDate() != null)
//            edate = sdf.format(dateChooserCombo5.getSelectedDate().getTime());
//        else
//            edate = "";
//        removeAllRows(jpl.getModel());
//        connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
//        insertJData(jpl.getModel(), true, 0);        
    }//GEN-LAST:event_dateChooserCombo5OnSelectionChange

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // backup
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Сохранить файл...");        
        fc.setFileFilter(new ExtFileFilter("sql", "*.sql DataBase Dump File"));
        int rv = fc.showSaveDialog(this);
        if (rv == JFileChooser.CANCEL_OPTION)
            return;
        File file = fc.getSelectedFile();        
        if (!file.getAbsolutePath().endsWith(".sql")) file = new File(file+".sql");
            String path = file.getAbsolutePath().replace("\\", "/");
        jTextArea1.append("\nВыгрузка базы данных в "+file.getAbsolutePath());
        Process p = null;
        try{
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec("utils/mysqldump.exe -u"+userName+" -h"+srvIP+" -p"+pass+" -B "+dbName+" -r"+path);
            int pComplete = p.waitFor();
            if (pComplete == 0)
                JOptionPane.showMessageDialog(this, "Выгрузка базы данных завершена!", "Сообщение!", JOptionPane.ERROR_MESSAGE, app);
            else
                JOptionPane.showMessageDialog(this, "Не удалось выгрузить базу данных!", "Ошибка!", JOptionPane.ERROR_MESSAGE, error);
        } catch (IOException | InterruptedException | HeadlessException e){JOptionPane.showMessageDialog(this, e, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        if (!jInternalFrame8.isValid()){
            jDesktopPane1.add(jInternalFrame8);
        SwingUtilities.invokeLater(() ->{
        if (!jInternalFrame8.isVisible()){
            jTable1.setValueAt("ip", 0, 0);
            jTable1.setValueAt(srvIP, 0, 1);
            jTable1.setValueAt("dbport", 1, 0);
            jTable1.setValueAt(dbPort, 1, 1);                        
            jTable1.setValueAt("dbname", 2, 0);
            jTable1.setValueAt(dbName, 2, 1);            
            jTable1.setValueAt("dbcharset", 3, 0);
            jTable1.setValueAt(charset, 3, 1);
            jTable1.setValueAt("dbdriver", 4, 0);
            jTable1.setValueAt(dbDriver, 4, 1);
            jTable1.setValueAt("username", 5, 0);
            jTable1.setValueAt(userName, 5, 1);            
            jTable1.setValueAt("pass", 6, 0);
            jTable1.setValueAt(pass, 6, 1);                  
            jTable1.setValueAt("font", 7, 0);
            jTable1.setValueAt(font, 7, 1);
            jTable1.setValueAt("font_size", 8, 0);
            jTable1.setValueAt(fontSize, 8, 1);      
            jTable1.setValueAt("date_start", 9, 0);
            jTable1.setValueAt(sdate, 9, 1);
            jTable1.setValueAt("date_end", 10, 0);
            jTable1.setValueAt(edate, 10, 1);
            jTable1.setValueAt("show_closed", 11, 0);
            jTable1.setValueAt(showClosed, 11, 1);
            jTable1.setValueAt("show_deleted", 12, 0);
            jTable1.setValueAt(showDeleted, 12, 1);            
            jTable1.setValueAt("last_login", 13, 0);
            jTable1.setValueAt(ulogin, 13, 1);            
            jTable1.setValueAt("URL", 14, 0);
            jTable1.setValueAt(url, 14, 1);
            jTable1.setValueAt("version", 15, 0);
            jTable1.setValueAt(version, 15, 1);            
            jInternalFrame8.setVisible(true);
            }
        });
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // Журнал смены    
        if (!jInternalFrame9.isValid()){
            jDesktopPane1.add(jInternalFrame9);
            
        SwingUtilities.invokeLater(() ->{
        if (!jInternalFrame9.isVisible()){
            removeAllRows(ss.getModel());
            connector.uni_query(ss.getJournal());
            insertJData(ss.getModel(), false, 1);              
            jInternalFrame9.setVisible(true);
        }
        });
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // новая смена
        new DShift(this, true).setVisible(true);
        if (exec) ins(q);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // отчет 2 
        if (!jInternalFrame10.isValid()) {
            jDesktopPane1.add(jInternalFrame10);
            
            SwingUtilities.invokeLater(() -> {
                if (!jInternalFrame10.isVisible()) {
                    removeAllRows(reportGSM.getModel());
                    jInternalFrame10.setVisible(true);
                }
            }
            );
        }        
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // Сформировать отчет 2
        String id = "";
        try {
            if (!this.jTextField4.getText().equals("")) {
                id = this.connector.getOne(this.sc.getID(this.jTextField4.getText())).toString();
            }
        }
        catch (UnsupportedEncodingException ex) {
            JOptionPane.showMessageDialog(this, "Не выбран автомобиль!", "Ошибка!", 0, error);
        }
        if (id.equals("")) {
            id = "0";
        }
        this.removeAllRows(this.reportGSM.getModel());
        if (this.dateChooserCombo1.getSelectedDate() != null && this.dateChooserCombo2.getSelectedDate() != null) {
            this.connector.uni_query(this.reportGSM.getJournal(id, this.sql_sdf.format(this.dateChooserCombo1.getSelectedDate().getTime()), this.sql_sdf.format(this.dateChooserCombo2.getSelectedDate().getTime())));
            this.insertJData(this.reportGSM.getModel(), false, 1);
        } else {
            this.jTextArea1.append("\nПроизошла какая-то херня!");
        }
        float probeg = 0.0f;
        float rashod = 0.0f;
        float doz = 0.0f;
        for (int i = 0; i < this.reportGSM.getModel().getRowCount(); ++i) {
            if (!this.reportGSM.getModel().getValueAt(i, 8).toString().equals(""))
                probeg += Float.parseFloat(this.reportGSM.getModel().getValueAt(i, 8).toString());
            if (!this.reportGSM.getModel().getValueAt(i, 11).toString().equals(""))
                rashod += Float.parseFloat(this.reportGSM.getModel().getValueAt(i, 11).toString());
            if (!this.reportGSM.getModel().getValueAt(i, 12).toString().equals(""))
                doz += Float.parseFloat(this.reportGSM.getModel().getValueAt(i, 12).toString());
        }
        this.jTextField5.setText("" + Math.round(probeg*100)/100);
        this.jTextField6.setText("" + rashod);
        this.jTextField7.setText("" + doz);        
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // Выбор автомобиля для отчета 2
        this.connector.uni_query("SELECT id, number FROM cars");
        new DSearch1(this, true, this.connector.res).setVisible(true);
        this.jTextField4.setText(RESULT);        
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // Отчет 1
        if (!jInternalFrame11.isValid()) {
            jDesktopPane1.add(jInternalFrame11);
            
            SwingUtilities.invokeLater(() -> {
                if (!jInternalFrame11.isVisible()) {
                    removeAllRows(reportGSM1.getModel());
                    jInternalFrame11.setVisible(true);
                }
            }
            );
        }        
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // Сформировать отчет 1
        this.removeAllRows(this.reportGSM1.getModel());
        if (this.dateChooserCombo3.getSelectedDate() != null && this.dateChooserCombo6.getSelectedDate() != null) {
            this.connector.uni_query(this.reportGSM1.getJournal(this.sql_sdf.format(this.dateChooserCombo3.getSelectedDate().getTime()), this.sql_sdf.format(this.dateChooserCombo6.getSelectedDate().getTime())));
            ArrayList<Object> row = new ArrayList<>();
            for (int i = 0; i < this.connector.res.size(); ++i) {
                row.clear();
                row.add(i + 1);
                row.add(this.connector.res.get(i)[2]);
                row.add(this.connector.res.get(i)[3]);
                row.add(this.connector.res.get(i)[4]);
                switch (this.connector.res.get(i)[1].toString()) {
                    case "1": {
                        row.add(this.connector.res.get(i)[5]);
                        row.add(this.connector.res.get(i)[6]);
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        break;
                    }
                    case "3": {
                        row.add("");
                        row.add("");
                        row.add(this.connector.res.get(i)[5]);
                        row.add(this.connector.res.get(i)[6]);
                        row.add("");
                        row.add("");
                        break;
                    }
                    case "4": {
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add(this.connector.res.get(i)[5]);
                        row.add(this.connector.res.get(i)[6]);
                        break;
                    }
                    default: {
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");
                        row.add("");                        
                    }
                }
                if (this.dateChooserCombo3.getSelectedDate() != null) {
                    row.add(this.connector.getOne(this.reportGSM1.getGsmEnd(this.connector.res.get(i)[0].toString(), this.sql_sdf.format(this.dateChooserCombo6.getSelectedDate().getTime()))));
                } else {
                    row.add(this.connector.getOne(this.reportGSM1.getGsmEnd(this.connector.res.get(i)[0].toString(), null)));
                }
                this.reportGSM1.getModel().addRow(row.toArray());
            }
        } else {
            this.jTextArea1.append("\nПроизошла херня в отчете 1.");
        }        
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // Сохрпнить отчет 2
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Сохранить файл...");
        fc.setFileFilter(new ExtFileFilter("xls", "*.xls MS Office Exel(2003)"));
        int rv = fc.showSaveDialog(this);
        if (rv == 1) {
            return;
        }
        File file = fc.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) {
            file = new File(file + ".xls");
        }
        try {
            Label label;
            Workbook originalworkbook = Workbook.getWorkbook((File)new File("sheet/ogsm2.tplx"));
            WritableWorkbook copywb = Workbook.createWorkbook((File)file, (Workbook)originalworkbook);
            WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
            WritableCellFormat cf = new WritableCellFormat(font);
            cf.setAlignment(Alignment.LEFT);
            cf.setBorder(Border.ALL, BorderLineStyle.THIN);
            int row = 0;
            System.out.println(this.reportGSM.getModel().getRowCount());
            for (int i = 0; i < this.reportGSM.getModel().getRowCount(); ++i) {
                row = i + 4;
                label = new Label(0, row, this.reportGSM.getModel().getValueAt(i, 0).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(1, row, this.reportGSM.getModel().getValueAt(i, 1).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(2, row, this.reportGSM.getModel().getValueAt(i, 2).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(3, row, this.reportGSM.getModel().getValueAt(i, 3).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(4, row, this.reportGSM.getModel().getValueAt(i, 4).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(5, row, this.reportGSM.getModel().getValueAt(i, 5).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(6, row, this.reportGSM.getModel().getValueAt(i, 6).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(7, row, this.reportGSM.getModel().getValueAt(i, 7).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(8, row, this.reportGSM.getModel().getValueAt(i, 8).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(9, row, this.reportGSM.getModel().getValueAt(i, 9).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(10, row, this.reportGSM.getModel().getValueAt(i, 10).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(11, row, this.reportGSM.getModel().getValueAt(i, 11).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(12, row, this.reportGSM.getModel().getValueAt(i, 12).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(13, row, this.reportGSM.getModel().getValueAt(i, 13).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                label = new Label(14, row, this.reportGSM.getModel().getValueAt(i, 14).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
            }
            label = new Label(8, ++row, this.jTextField5.getText().replace(".", ","), (CellFormat)cf);
            copywb.getSheet(0).addCell((WritableCell)label);
            label = new Label(11, row, this.jTextField6.getText().replace(".", ","), (CellFormat)cf);
            copywb.getSheet(0).addCell((WritableCell)label);
            label = new Label(12, row, this.jTextField7.getText().replace(".", ","), (CellFormat)cf);
            copywb.getSheet(0).addCell((WritableCell)label);
            String id = this.connector.getOne(this.sc.getID(this.jTextField4.getText())).toString();
            label = new Label(12, 0, this.connector.getOne(this.sc.getCarModel(id)).toString(), (CellFormat)cf);
            copywb.getSheet(0).addCell((WritableCell)label);
            label = new Label(12, 1, this.dateChooserCombo1.getText() + "-" + this.dateChooserCombo2.getText(), (CellFormat)cf);
            copywb.getSheet(0).addCell((WritableCell)label);
            label = new Label(0, ++row, "Диспетчер");
            copywb.getSheet(0).addCell((WritableCell)label);
            label = new Label(2, row, "Коротецкая И.А.");
            copywb.getSheet(0).addCell((WritableCell)label);
            
            label = new Label(0, row + 2, "Диспетчер");
            copywb.getSheet(0).addCell((WritableCell)label);            
            label = new Label(2, row + 2, "Погожева Н.В.");
            copywb.getSheet(0).addCell((WritableCell)label);            
            
            label = new Label(0, row + 4, "Диспетчер");
            copywb.getSheet(0).addCell((WritableCell)label);            
            label = new Label(2, row + 4, "Султанова К.С.");
            copywb.getSheet(0).addCell((WritableCell)label);            
            
            label = new Label(0, row + 6, "Начальник ТЭО");
            copywb.getSheet(0).addCell((WritableCell)label);
            label = new Label(2, row + 6, "Кузьменко А.В.");
            copywb.getSheet(0).addCell((WritableCell)label);            
            
            copywb.write();
            originalworkbook.close();
            copywb.close();
            jTextArea1.append("\nОтчет сохранен в файл: "+file.getAbsolutePath());
        }
        catch (IOException | IndexOutOfBoundsException | BiffException | WriteException ex) {
            JOptionPane.showMessageDialog(this, ex, "Ошибка!", 0, error);
        }        
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // Сохранить отчет 1
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Сохранить файл...");
        fc.setFileFilter(new ExtFileFilter("xls", "*.xls MS Office Exel(2003)"));
        int rv = fc.showSaveDialog(this);
        if (rv == 1) {
            return;
        }
        File file = fc.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) {
            file = new File(file + ".xls");
        }
        try {
            Workbook originalworkbook = Workbook.getWorkbook((File)new File("sheet/ogsm.tplx"));
            WritableWorkbook copywb = Workbook.createWorkbook((File)file, (Workbook)originalworkbook);
            WritableFont font = new WritableFont(WritableFont.ARIAL, 8);
            WritableCellFormat cf = new WritableCellFormat(font);
            cf.setAlignment(Alignment.LEFT);
            cf.setBorder(Border.ALL, BorderLineStyle.THIN);
            int row;
            Label label;
            for (int i = 0; i < this.reportGSM1.getModel().getRowCount(); i++) {
                System.out.println(i);
                row = i + 6;
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 0).toString());
                label = new Label(0, row, this.reportGSM1.getModel().getValueAt(i, 0).toString(), (CellFormat)cf);                
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 1).toString());
                label = new Label(1, row, this.reportGSM1.getModel().getValueAt(i, 1).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 2).toString());
                label = new Label(2, row, this.reportGSM1.getModel().getValueAt(i, 2).toString(), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 3).toString());
                label = new Label(3, row, this.reportGSM1.getModel().getValueAt(i, 3).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 4).toString());
                label = new Label(4, row, this.reportGSM1.getModel().getValueAt(i, 4).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 5).toString());
                label = new Label(5, row, this.reportGSM1.getModel().getValueAt(i, 5).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 6).toString());
                label = new Label(6, row, this.reportGSM1.getModel().getValueAt(i, 6).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 7).toString());
                label = new Label(7, row, this.reportGSM1.getModel().getValueAt(i, 7).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 8).toString());
                label = new Label(8, row, this.reportGSM1.getModel().getValueAt(i, 8).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 9).toString());
                label = new Label(9, row, this.reportGSM1.getModel().getValueAt(i, 9).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
                
                System.out.println(this.reportGSM1.getModel().getValueAt(i, 10).toString());
                label = new Label(10, row, this.reportGSM1.getModel().getValueAt(i, 10).toString().replace(".", ","), (CellFormat)cf);
                copywb.getSheet(0).addCell((WritableCell)label);
            }
            copywb.write();
            originalworkbook.close();
            copywb.close();
            jTextArea1.append("\nОтчет сохранен в файл: "+file.getAbsolutePath());
        }
        catch (IOException | IndexOutOfBoundsException | BiffException | WriteException ex) {
            JOptionPane.showMessageDialog(this, ex, "Ошибка!", 0, error);
        }        
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // Справочник сопровождающие лица        
        if (!jInternalFrame4.isValid() && !jInternalFrame4.isVisible()){
            jDesktopPane1.add(jInternalFrame4);
            jInternalFrame4.setTitle(scd.getTitle(CARGERS));
        SwingUtilities.invokeLater(() -> { 
        if (!jInternalFrame4.isVisible()){
            removeAllRows(scd.getModel());
            connector.uni_query(scd.getJournal(CARGERS));
                insertJData(scd.getModel(), false, 1);        
            jInternalFrame4.setVisible(true);
            }         
        });
        } else {
            jTextArea1.append("\nНе возможно открыть еще одно окно 'Водители' или 'Сопровождающие'. Для этого закройте активное окно.");
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Фильтр удалить
        jTextField1.setText("");        
        jpl.setRowFilter(null, 4);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // Фильтр установить
        if (jTextField1.getText().trim().length() != 0 && evt.getKeyCode() == KeyEvent.VK_ENTER)
            jpl.setRowFilter(jTextField1.getText(), 4);        
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // Обновить типы ТС
            removeAllRows(sct.getModel());
            connector.uni_query(sct.getJournal());
            insertJData(sct.getModel(), false, 1);        
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // Обновить Пользователи
            removeAllRows(su.getModel());
            connector.uni_query(su.getJournal());
            insertJData(su.getModel(), false, 1);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // Обновить водители и сопровождающие
            removeAllRows(scd.getModel());
            int activity = scd.getTitleId(jInternalFrame4.getTitle());
            connector.uni_query(scd.getJournal(activity));
            insertJData(scd.getModel(), false, 1);        
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // Обновить автомобили
            removeAllRows(sc.getModel());
            connector.uni_query(sc.getJournal());
            insertJData(sc.getModel(), false, 1);        
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // Обновить типы топлива
            removeAllRows(sgsm.getModel());
            connector.uni_query(sgsm.getJournal());
            insertJData(sgsm.getModel(), false, 1);        
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // Обновить маршруты
            removeAllRows(sd.getModel());
            connector.uni_query(sd.getJournal());
            insertJData(sd.getModel(), false, 1);        
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // Обновить смены
            removeAllRows(ss.getModel());
            connector.uni_query(ss.getJournal());
            insertJData(ss.getModel(), false, 1);        
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jInternalFrame1InternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_jInternalFrame1InternalFrameClosing
        // TODO add your handling code here:
        doUpdate = false;
    }//GEN-LAST:event_jInternalFrame1InternalFrameClosing

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // Обновить журнал
        removeAllRows(jpl.getModel());
        connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
        insertJData(jpl.getModel(), true, 0);        
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // О программе
        new DAbout(this, true, version).setVisible(true);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // настройки журнала
        new DJournalSettings(this, true, jpl).setVisible(true);        
        if (exec) {
            removeAllRows(jpl.getModel());
            jpl.initTable();
            connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
            insertJData(jpl.getModel(), true, 0);                    
            exec = false;
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // Сохранить журнал
        jTextArea1.append("\nСохранение файла...");
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Сохранить файл...");
        fc.setFileFilter(new ExtFileFilter("xls", "*.xls MS Office Exel(2003)"));
        int rv = fc.showSaveDialog(this);
        if (rv == 1) {
            return;
        }
        File file = fc.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".xls")) {
            file = new File(file + ".xls");
        }        
        jInternalFrame1.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));       
        
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Журнал");            
        Row row;
        Cell cell; 

        row = sheet.createRow(0);        
        HSSFCellStyle cs = wb.createCellStyle();              
        cs.setBorderBottom((short)1);
        
        
        for (int i = 1; i < jpl.getTable().getColumnCount(); i++) {
            cell = row.createCell(i-1);
            cell.setCellStyle(cs);
            cell.setCellValue(jpl.getTable().getColumnName(i));
        }
        int r;
        
        for (int i = 0; i < jpl.getTable().getRowSorter().getViewRowCount(); i++) {            
            row = sheet.createRow(i+1);
            for (int col = 1; col < jpl.getModel().getColumnCount(); col++){
                cell = row.createCell(col-1);
                r = jpl.getTable().getRowSorter().convertRowIndexToModel(i);                
                cell.setCellValue(jpl.getModel().getValueAt(r, col).toString()); 
                sheet.autoSizeColumn(col);
                }                                    
               } 
                
        try {
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);            
            wb.close();   
            fos.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex, "Ошибка!", 0, error);
        }       

        jTextArea1.append("\nЖурнал сохранен в файл: "+file.getAbsolutePath());
        jInternalFrame1.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jButton28ActionPerformed

    private void dateChooserCombo4OnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dateChooserCombo4OnCommit
        // Дата начала в журнале
        if (dateChooserCombo4.getSelectedDate() != null)
            sdate = sdf.format(dateChooserCombo4.getSelectedDate().getTime());
        else
            sdate = "";
        removeAllRows(jpl.getModel());
        connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
        insertJData(jpl.getModel(), true, 0);          
    }//GEN-LAST:event_dateChooserCombo4OnCommit

    private void dateChooserCombo5OnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dateChooserCombo5OnCommit
        // Дата окончания в журнале
        if (dateChooserCombo5.getSelectedDate() != null)
            edate = sdf.format(dateChooserCombo5.getSelectedDate().getTime());
        else
            edate = "";
        removeAllRows(jpl.getModel());
        connector.uni_query(jpl.getJournal(showClosed, showDeleted, dateChooserCombo4.getSelectedDate(), dateChooserCombo5.getSelectedDate()));
        insertJData(jpl.getModel(), true, 0);         
    }//GEN-LAST:event_dateChooserCombo5OnCommit

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // Новый путевой лист из меню
            SwingUtilities.invokeLater(() -> {
                if (!drf.isVisible()) {                    
                    dr.setDefaultValues();
                    drf = new DocRouteFrame(dr, connector);
                    drf.setVisible(true);
                } else {drf.setState(JFrame.NORMAL);}
            });        
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // Настройки
        if (!jInternalFrame12.isValid()){
            jDesktopPane1.add(jInternalFrame12);            
        SwingUtilities.invokeLater(() -> { 
        if (!jInternalFrame12.isVisible()){
            
            jTextField11.setText(srvIP);
            jTextField10.setText(dbPort);
            jTextField3.setText(dbName);
            jTextField12.setText(dbDriver);
            jPasswordField1.setText(pass);
            
            String[] allFontFamilies=java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            for (int i = 0; i < allFontFamilies.length; i++)
                listModel.addElement(allFontFamilies[i]);
            jTextField13.setText(fontSize);
            switch(fontWeight){
                case "1":
                    jCheckBox3.setSelected(true);
                    break;
                case "2":
                    jCheckBox4.setSelected(true);
                    break;
                case "3":
                    jCheckBox3.setSelected(true);
                    jCheckBox4.setSelected(true);
                    break;
            }
            jTextField14.setText(sColor);
            jLabel16.setForeground(selectColor);
            jTextField15.setText(sfColor);
            jLabel7.setForeground(selectForegroundColor);
            jTextField16.setText(tColor);
            jLabel21.setForeground(tableColor);
            jTextField17.setText(tgColor);
            jLabel23.setForeground(tableGridColor);
            
            jInternalFrame12.setVisible(true);
            }         
        });
        }         
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // Сохранить настройки        
        if (jList1.getSelectedValue() != null)
            font = jList1.getSelectedValue();
        fontSize = jTextField13.getText();        
        int w = 0;
        if (jCheckBox3.isSelected()) w+=1;
        if (jCheckBox4.isSelected()) w+=2;
        fontWeight = ""+w;
        sColor = jTextField14.getText();
        sfColor = jTextField15.getText();
        tColor = jTextField16.getText();
        tgColor = jTextField17.getText();
        jTextArea1.append("\nНастройки будут применены при следующем запуске!");
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        // Выбор цвета выделения
        Color c = JColorChooser.showDialog(null, "Выберите цвет", jLabel16.getBackground());
        if (c != null)
            jLabel16.setForeground(c);
        jTextField14.setText("R:"+c.getRed()+"G:"+c.getGreen()+"B:"+c.getBlue());
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // Цвет выделенного текста
        Color c = JColorChooser.showDialog(null, "Выберите цвет", jLabel17.getBackground());
        if (c != null)
            jLabel17.setForeground(c);
        jTextField15.setText("R:"+c.getRed()+"G:"+c.getGreen()+"B:"+c.getBlue());        
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // Цвет таблицы
        Color c = JColorChooser.showDialog(null, "Выберите цвет", jLabel21.getBackground());
        if (c != null)
            jLabel21.setForeground(c);
        jTextField16.setText("R:"+c.getRed()+"G:"+c.getGreen()+"B:"+c.getBlue());          
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // Цвет сетки
        Color c = JColorChooser.showDialog(null, "Выберите цвет", jLabel23.getBackground());
        if (c != null)
            jLabel23.setForeground(c);
        jTextField12.setText("R:"+c.getRed()+"G:"+c.getGreen()+"B:"+c.getBlue());          
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // Журнал регистрации
        if (!jInternalFrame13.isValid()){
            jDesktopPane1.add(jInternalFrame13);
        SwingUtilities.invokeLater(() -> { 
        if (!jInternalFrame13.isVisible()){
            removeAllRows(sessionModel);
            connector.uni_query("SELECT * FROM session");
                insertJData(sessionModel, false, 6);        
            jInternalFrame13.setVisible(true);
            }         
        });
        }        
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // Активные сеансы
        if (!jInternalFrame14.isValid()){
            jDesktopPane1.add(jInternalFrame14);
        SwingUtilities.invokeLater(() -> { 
        if (!jInternalFrame14.isVisible()){
            removeAllRows(sessionActiveModel);
            connector.uni_query("SHOW PROCESSLIST");
                insertJData(sessionActiveModel, false, 1);        
            jInternalFrame14.setVisible(true);
            }         
        });
        }        
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
    try {
        // Kill
        connector.uni_execute("KILL "+jFormattedTextField1.getText());
        removeAllRows(sessionActiveModel);
        connector.uni_query("SHOW PROCESSLIST");
        insertJData(sessionActiveModel, false, 1); 
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, ex, "Ошибка!", 0, error);
    }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // Проверить обновления
        new DUpdate(this, true).setVisible(true);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        // Удаление объектов
        if (!jInternalFrame15.isValid()){
            jDesktopPane1.add(jInternalFrame15);
        SwingUtilities.invokeLater(() -> { 
        if (!jInternalFrame15.isVisible()){
            removeAllRows(deleteModel);
            jTextArea2.setText("");
            connector.uni_query("SELECT id FROM route WHERE status=3");            
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], "Путевой лист", "Документы: Путевые листы"});
            }
            connector.uni_query("SELECT id, name FROM shift WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Смены"});
            }
            connector.uni_query("SELECT id, description FROM directions WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Маршруты"});
            }            
            connector.uni_query("SELECT id, number FROM cars WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Автомобили"});
            }         
            connector.uni_query("SELECT id, type FROM types WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Типы ТС"});
            }        
            connector.uni_query("SELECT id, type FROM gsm WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Типы ГСМ"});
            }       
            connector.uni_query("SELECT id, name FROM drivers WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Водители"});
            }            
            jInternalFrame15.setVisible(true);
            jTextArea2.append("Элементов для удаления "+jTable4.getRowCount());
            }                     
        });
        }         
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        // Выделить все        
        for (int i = 0; i < jTable4.getRowCount(); i++) {
            if (deleteModel.getValueAt(i, 0).toString().equals("false"))
                deleteModel.setValueAt(true, i, 0);
        }
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        // Снять выделение
        for (int i = 0; i < jTable4.getRowCount(); i++) {
            if (deleteModel.getValueAt(i, 0).toString().equals("true"))
                deleteModel.setValueAt(false, i, 0);
        }        
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // Восснановить
        for (int i = 0; i < jTable4.getRowCount(); i++) {
            if (deleteModel.getValueAt(i, 0).toString().equals("true")) {
                switch (deleteModel.getValueAt(i, 3).toString()) {
                    case "Документы: Путевые листы":
                    {
                        try {
                            connector.uni_execute("UPDATE route SET status=0 WHERE id="+deleteModel.getValueAt(i, 1));
                            jTextArea2.append("\nВосстановлен Документ [Путевой лист] №"+deleteModel.getValueAt(i, 1));
                        } catch (SQLException ex) {
                            jTextArea2.append("!!!Ошибка!!! Не удалось восстановить Документ [Путевой лист] №"+deleteModel.getValueAt(i, 1));
                        }
                    }                        
                        break;
                    case "Справочник: Смены":
                    {
                        try {
                            connector.uni_execute("UPDATE shift SET status=1 WHERE id="+deleteModel.getValueAt(i, 1));
                            jTextArea2.append("\nВосстановлен объект [Справочник: Смены] №"+deleteModel.getValueAt(i, 1));
                        } catch (SQLException ex) {
                            jTextArea2.append("!!!Ошибка!!! Не удалось восстановить объект [Справочник: Смены] №"+deleteModel.getValueAt(i, 1));
                        }
                    }                        
                        break;  
                    case "Справочник: Маршруты":
                    {
                        try {
                            connector.uni_execute("UPDATE directions SET status=1 WHERE id="+deleteModel.getValueAt(i, 1));
                            jTextArea2.append("\nВосстановлен объект [Справочник: Маршруты] №"+deleteModel.getValueAt(i, 1));
                        } catch (SQLException ex) {
                            jTextArea2.append("!!!Ошибка!!! Не удалось восстановить объект [Справочник: Маршруты] №"+deleteModel.getValueAt(i, 1));
                        }
                    }                        
                        break;                                                  
                    case "Справочник: Автомобили":
                    {
                        try {
                            connector.uni_execute("UPDATE cars SET status=1 WHERE id="+deleteModel.getValueAt(i, 1));
                            jTextArea2.append("\nВосстановлен объект [Справочник: Автомобили] №"+deleteModel.getValueAt(i, 1));
                        } catch (SQLException ex) {
                            jTextArea2.append("!!!Ошибка!!! Не удалось восстановить объект [Справочник: Автомобили] №"+deleteModel.getValueAt(i, 1));
                        }
                    }                        
                        break;
                    case "Справочник: Типы ТС":
                    {
                        try {
                            connector.uni_execute("UPDATE types SET status=1 WHERE id="+deleteModel.getValueAt(i, 1));
                            jTextArea2.append("\nВосстановлен объект [Справочник: Типы ТС] №"+deleteModel.getValueAt(i, 1));
                        } catch (SQLException ex) {
                            jTextArea2.append("!!!Ошибка!!! Не удалось восстановить объект [Справочник: Типы ТС] №"+deleteModel.getValueAt(i, 1));
                        }
                    }                        
                        break;                                                
                    case "Справочник: Типы ГСМ":
                    {
                        try {
                            connector.uni_execute("UPDATE gsm SET status=1 WHERE id="+deleteModel.getValueAt(i, 1));
                            jTextArea2.append("\nВосстановлен объект [Справочник: Типы ГСМ] №"+deleteModel.getValueAt(i, 1));
                        } catch (SQLException ex) {
                            jTextArea2.append("!!!Ошибка!!! Не удалось восстановить объект [Справочник: Типы ГСМ] №"+deleteModel.getValueAt(i, 1));
                        }
                    }                        
                        break;                                                  
                    case "Справочник: Водители":
                    {
                        try {
                            connector.uni_execute("UPDATE drivers SET status=1 WHERE id="+deleteModel.getValueAt(i, 1));
                            jTextArea2.append("\nВосстановлен объект [Справочник: Водители] №"+deleteModel.getValueAt(i, 1));
                        } catch (SQLException ex) {
                            jTextArea2.append("!!!Ошибка!!! Не удалось восстановить объект [Справочник: Водители] №"+deleteModel.getValueAt(i, 1));
                        }
                    }                        
                        break;                                                    
                }
            }
        }
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        // Удалить        
        for (int i = 0; i < jTable4.getRowCount(); i++) {
            if (deleteModel.getValueAt(i, 0).toString().equals("true")) {
                
                switch (deleteModel.getValueAt(i, 3).toString()) {        
                    case "Документы: Путевые листы": {
                        jTextArea2.append("\nУдаление объекта [Документы: Путевые листы] №"+deleteModel.getValueAt(i, 1));
                    try {
                        connector.uni_execute("DELETE FROM route WHERE id="+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\nОбъект удален.");
                    } catch (SQLException ex) {
                        jTextArea2.append("\n!!!Ошибка!!! Не удалось удалить объект [Документы: Путевые листы] №"+deleteModel.getValueAt(i, 1));
                    }
                    break;
                    }
                    case "Справочник: Смены": {
                        jTextArea2.append("\nУдаление объекта [Справочник: Смены] №"+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\n  Проверка зависимостей...");
                        int count = getAddiction("route", "shift", deleteModel.getValueAt(i, 1).toString()); 
                        if (count >0) {
                             jTextArea2.append("\n  Зависимостей "+count+". Удаление невозможно");
                             break;
                        } else                         
                    try {
                        connector.uni_execute("DELETE FROM shift WHERE id="+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\nОбъект удален.");
                        break;
                    } catch (SQLException ex) {
                        jTextArea2.append("\n!!!Ошибка!!! Не удалось удалить объект.");
                        }
                    }
                    case "Справочник: Маршруты": {
                        jTextArea2.append("\nУдаление объекта [Справочник: Маршруты] №"+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\n  Проверка зависимостей...");
                        int count = getAddiction("route", "direction_id", deleteModel.getValueAt(i, 1).toString()); 
                        if (count > 0) {
                             jTextArea2.append("\n  Зависимостей "+count+". Удаление невозможно");
                             break;
                        } else                       
                    try {
                        connector.uni_execute("DELETE FROM directions WHERE id="+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\nОбъект удален.");
                        break;
                    } catch (SQLException ex) {
                        jTextArea2.append("\n!!!Ошибка!!! Не удалось удалить объект.");
                        }
                    }  
                    case "Справочник: Автомобили": {
                        jTextArea2.append("\nУдаление объекта [Справочник: Автомобили] №"+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\n  Проверка зависимостей...");
                        int count = getAddiction("route", "car_id", deleteModel.getValueAt(i, 1).toString()); 
                        if (count > 0) { 
                             jTextArea2.append("\n  Зависимостей "+count+". Удаление невозможно");
                             break;
                         } else
                    try {
                        connector.uni_execute("DELETE FROM cars WHERE id="+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\nОбъект удален.");
                        break;
                    } catch (SQLException ex) {
                        jTextArea2.append("\n!!!Ошибка!!! Не удалось удалить объект.");
                        }
                    } 
                    case "Справочник: Типы ТС": {
                        jTextArea2.append("\nУдаление объекта [Справочник: Типы ТС] №"+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\n  Проверка зависимостей...");
                        int count = getAddiction("cars", "type_id", deleteModel.getValueAt(i, 1).toString()); 
                        if (count > 0) { 
                             jTextArea2.append("\n  Зависимостей "+count+". Удаление невозможно");
                             break;
                         } else
                    try {
                        connector.uni_execute("DELETE FROM types WHERE id="+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\nОбъект удален.");
                        break;
                    } catch (SQLException ex) {
                        jTextArea2.append("\n!!!Ошибка!!! Не удалось удалить объект.");
                        }
                    }
                    case "Справочник: Типы ГСМ": {
                        jTextArea2.append("\nУдаление объекта [Справочник: Типы ГСМ] №"+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\n  Проверка зависимостей...");
                        int count = getAddiction("cars", "gsm_id", deleteModel.getValueAt(i, 1).toString()); 
                        if (count > 0) { 
                             jTextArea2.append("\n  Зависимостей "+count+". Удаление невозможно");
                             break;
                         } else
                    try {
                        connector.uni_execute("DELETE FROM gsm WHERE id="+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\nОбъект удален.");
                        break;
                    } catch (SQLException ex) {
                        jTextArea2.append("\n!!!Ошибка!!! Не удалось удалить объект.");
                        }
                    }
                    case "Справочник: Водители": {
                        jTextArea2.append("\nУдаление объекта [Справочник: Водители] №"+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\n  Проверка зависимостей...");
                        int count = getAddiction("cars", "driver_id", deleteModel.getValueAt(i, 1).toString());
                        //count += getAddiction("route", "driver_id", deleteModel.getValueAt(i, 1).toString());
                        if (count > 0) { 
                             jTextArea2.append("\n  Зависимостей "+count+". Удаление невозможно");
                             break;
                         } else
                    try {
                        connector.uni_execute("DELETE FROM drivers WHERE id="+deleteModel.getValueAt(i, 1));
                        jTextArea2.append("\nОбъект удален.");
                        break;
                    } catch (SQLException ex) {
                        jTextArea2.append("\n!!!Ошибка!!! Не удалось удалить объект.");
                        } 
                    } break;
                                        
                }
        }
        }
            removeAllRows(deleteModel);
            //jTextArea12.setText("");
            connector.uni_query("SELECT id FROM route WHERE status=3");            
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], "Путевой лист", "Документы: Путевые листы"});
            }
            connector.uni_query("SELECT id, name FROM shift WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Смены"});
            }
            connector.uni_query("SELECT id, description FROM directions WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Маршруты"});
            }            
            connector.uni_query("SELECT id, number FROM cars WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Автомобили"});
            }         
            connector.uni_query("SELECT id, type FROM types WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Типы ТС"});
            }        
            connector.uni_query("SELECT id, type FROM gsm WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Типы ГСМ"});
            }       
            connector.uni_query("SELECT id, name FROM drivers WHERE status=0");
            for (int i=0; i<connector.res.size(); i++) {                
                deleteModel.addRow(new Object[]{false, connector.res.get(i)[0], connector.res.get(i)[1], "Справочник: Водители"});
            }            
            //jInternalFrame15.setVisible(true);            
            jTextArea2.append("\n________________________________________________");            
            jTextArea2.append("\nЭлементов для удаления "+jTable4.getRowCount());        
            jTextArea2.append("\n________________________________________________");            
    }//GEN-LAST:event_jButton35ActionPerformed
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private datechooser.beans.DateChooserCombo dateChooserCombo3;
    private datechooser.beans.DateChooserCombo dateChooserCombo4;
    private datechooser.beans.DateChooserCombo dateChooserCombo5;
    private datechooser.beans.DateChooserCombo dateChooserCombo6;
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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame10;
    private javax.swing.JInternalFrame jInternalFrame11;
    private javax.swing.JInternalFrame jInternalFrame12;
    private javax.swing.JInternalFrame jInternalFrame13;
    private javax.swing.JInternalFrame jInternalFrame14;
    private javax.swing.JInternalFrame jInternalFrame15;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JInternalFrame jInternalFrame3;
    private javax.swing.JInternalFrame jInternalFrame4;
    private javax.swing.JInternalFrame jInternalFrame5;
    private javax.swing.JInternalFrame jInternalFrame6;
    private javax.swing.JInternalFrame jInternalFrame7;
    private javax.swing.JInternalFrame jInternalFrame8;
    private javax.swing.JInternalFrame jInternalFrame9;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar10;
    private javax.swing.JToolBar jToolBar11;
    private javax.swing.JToolBar jToolBar12;
    private javax.swing.JToolBar jToolBar13;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JToolBar jToolBar9;
    // End of variables declaration//GEN-END:variables
}
