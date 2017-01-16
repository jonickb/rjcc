/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  datechooser.beans.DateChooserCombo
 *  datechooser.model.multiple.MultyModelBehavior
 */
package rjcc;

import datechooser.beans.DateChooserCombo;
import datechooser.model.multiple.MultyModelBehavior;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class DCar1 extends JDialog {
    
    ArrayList<String> types = new ArrayList();
    String drv = "0";
    String gsm = "0";
    String type = "0";
    Connector con;
    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
    String id = "";
    boolean edit = false;
    Calendar cal = Calendar.getInstance();
    private DateChooserCombo dateChooserCombo1;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JComboBox<String> jComboBox1;
    private JFormattedTextField jFormattedTextField1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private JToolBar jToolBar1;

    public DCar1(Frame parent, boolean modal, Connector con) {
        super(parent, modal);
        this.initComponents();
        this.con = con;
        this.jComboBox1.removeAllItems();
        try {
            con.getListWId(SCarType.query1);
            int index = 0;
            String str = "";
            for (int i = 0; i < con.resOne.size(); ++i) {
                index = con.resOne.get(i).toString().indexOf(36);
                this.types.add(con.resOne.get(i).toString().substring(0, index));
                str = con.resOne.get(i).toString().substring(index + 1, con.resOne.get(i).toString().length());
                this.jComboBox1.addItem(str);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DCar.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.edit = false;
    }

    public DCar1(Frame parent, boolean modal, String id, Connector con) {
        int i;
        //super(parent, modal);
        this.initComponents();
        this.con = con;
        this.jComboBox1.removeAllItems();
        try {
            con.getListWId(SCarType.query1);
            int index = 0;
            String str = "";
            for (i = 0; i < con.resOne.size(); ++i) {
                index = con.resOne.get(i).toString().indexOf(36);
                this.types.add(con.resOne.get(i).toString().substring(0, index));
                str = con.resOne.get(i).toString().substring(index + 1, con.resOne.get(i).toString().length());
                this.jComboBox1.addItem(str);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DCar.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.id = id;
        this.jTextField2.setText(con.getOne("SELECT model FROM cars WHERE id=" + id).toString());
        this.jTextField1.setText(con.getOne("SELECT number FROM cars WHERE id=" + id).toString());
        try {
            String obj = con.getOne("SELECT DATE_FORMAT(manufactured, '%d.%m.%Y') FROM cars WHERE id=" + id).toString();
            if (!"".equals(obj)) {
                this.cal.setTime(this.sdf1.parse(obj));
                this.dateChooserCombo1.setSelectedDate(this.cal);
            } else {
                this.dateChooserCombo1.setSelectedDate(null);
            }
        }
        catch (ParseException ex) {
            System.out.println(ex);
        }
        this.drv = con.getOne("SELECT driver_id FROM cars WHERE id=" + id).toString();
        this.gsm = con.getOne("SELECT gsm_id FROM cars WHERE id=" + id).toString();
        this.type = con.getOne("SELECT type_id FROM cars WHERE id=" + id).toString();
        this.jTextField3.setText(con.getOne("SELECT name FROM drivers WHERE id=" + this.drv).toString());
        this.jTextField4.setText(con.getOne("SELECT type FROM gsm WHERE id=" + this.gsm).toString());
        int index = 0;
        for (i = 0; i < this.types.size(); ++i) {
            if (Integer.parseInt(this.types.get(i).toString()) != Integer.parseInt(this.type)) continue;
            index = i;
        }
        this.jComboBox1.setSelectedIndex(index);
        this.jFormattedTextField1.setText(con.getOne("SELECT probeg FROM cars WHERE id=" + id).toString());
        this.jTable1.setValueAt(con.getOne("SELECT rashod FROM cars WHERE id=" + id).toString().replace(".", ","), 0, 1);
        this.jTable1.setValueAt(con.getOne("SELECT rashod_special FROM cars WHERE id=" + id).toString().replace(".", ","), 1, 1);
        this.jTable1.setValueAt(con.getOne("SELECT climb FROM cars WHERE id=" + id).toString().replace(".", ","), 2, 1);
        this.jTable1.setValueAt(con.getOne("SELECT volume FROM cars WHERE id=" + id).toString().replace(".", ","), 3, 1);
        this.edit = true;
    }

    String dateF(Calendar date) {
        if (date != null) {
            return "date('" + this.sdf.format(date.getTime()) + "')";
        }
        return null;
    }

    String toNull(String s) {
        if (s.equals("")) {
            return "NULL";
        }
        return s;
    }

    private void initComponents() {
        this.jToolBar1 = new JToolBar();
        this.jButton1 = new JButton();
        this.jButton2 = new JButton();
        this.jPanel1 = new JPanel();
        this.jLabel1 = new JLabel();
        this.jTextField2 = new JTextField();
        this.jLabel2 = new JLabel();
        this.jTextField1 = new JTextField();
        this.jLabel3 = new JLabel();
        this.dateChooserCombo1 = new DateChooserCombo();
        this.jLabel4 = new JLabel();
        this.jTextField3 = new JTextField();
        this.jLabel5 = new JLabel();
        this.jTextField4 = new JTextField();
        this.jButton3 = new JButton();
        this.jButton4 = new JButton();
        this.jScrollPane1 = new JScrollPane();
        this.jTable1 = new JTable();
        this.jLabel7 = new JLabel();
        this.jFormattedTextField1 = new JFormattedTextField();
        this.jComboBox1 = new JComboBox();
        this.jLabel6 = new JLabel();
        this.setDefaultCloseOperation(2);
        this.jToolBar1.setRollover(true);
        this.jButton1.setText("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c");
        this.jButton1.setFocusable(false);
        this.jButton1.setHorizontalTextPosition(0);
        this.jButton1.setVerticalTextPosition(3);
        this.jButton1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                DCar1.this.jButton1ActionPerformed(evt);
            }
        });
        this.jToolBar1.add(this.jButton1);
        this.jButton2.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
        this.jButton2.setFocusable(false);
        this.jButton2.setHorizontalTextPosition(0);
        this.jButton2.setVerticalTextPosition(3);
        this.jButton2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                DCar1.this.jButton2ActionPerformed(evt);
            }
        });
        this.jToolBar1.add(this.jButton2);
        this.getContentPane().add((Component)this.jToolBar1, "First");
        this.jLabel1.setText("\u041c\u043e\u0434\u0435\u043b\u044c");
        this.jLabel2.setText("\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u043e\u043d\u043d\u044b\u0439 \u043d\u043e\u043c\u0435\u0440");
        this.jLabel3.setText("\u0414\u0430\u0442\u0430 \u0432\u044b\u043f\u0443\u0441\u043a\u0430");
        this.dateChooserCombo1.setBehavior(MultyModelBehavior.SELECT_SINGLE);
        this.jLabel4.setText("\u0412\u043e\u0434\u0438\u0442\u0435\u043b\u044c");
        this.jTextField3.setEditable(false);
        this.jLabel5.setText("\u0422\u043e\u043f\u043b\u0438\u0432\u043e");
        this.jTextField4.setEditable(false);
        this.jButton3.setText("...");
        this.jButton3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                DCar1.this.jButton3ActionPerformed(evt);
            }
        });
        this.jButton4.setText("...");
        this.jButton4.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                DCar1.this.jButton4ActionPerformed(evt);
            }
        });
        this.jTable1.setModel(new DefaultTableModel(new Object[][]{{"\u0420\u0430\u0441\u0445\u043e\u0434 (\u043b. \u043d\u0430 100\u043a\u043c.)", "0,00"}, {"\u041c\u043e\u0442\u043e\u0447\u0430\u0441\u044b (\u043b. \u043d\u0430 1\u0447.)", "0,000"}, {"\u041f\u043e\u0434\u044a\u0435\u043c (\u043b. \u043d\u0430 1 \u0435\u0437\u0434\u043a\u0443)", "0,00"}, {"\u041e\u0431\u044a\u0435\u043c (\u043c3)", "0,00"}}, new String[]{"\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435", "\u0417\u043d\u0430\u0447\u0435\u043d\u0438\u0435"}));
        this.jScrollPane1.setViewportView(this.jTable1);
        this.jLabel7.setText("\u041d\u0430\u0447\u0430\u043b\u044c\u043d\u044b\u0439 \u043f\u0440\u043e\u0431\u0435\u0433");
        this.jFormattedTextField1.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        this.jComboBox1.setModel(new DefaultComboBoxModel<String>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        this.jLabel6.setText("\u0422\u0438\u043f");
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1, -1, 463, 32767).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.jLabel1).addComponent(this.jLabel6)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTextField2).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jTextField1, -1, 188, 32767).addComponent(this.jComboBox1, 0, -1, 32767)).addGap(0, 0, 32767)))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel3).addComponent(this.jLabel7)).addGap(38, 38, 38).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent((Component)this.dateChooserCombo1, -1, 188, 32767).addComponent(this.jFormattedTextField1)).addGap(0, 0, 32767)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel4).addComponent(this.jLabel5)).addGap(84, 84, 84).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTextField4).addComponent(this.jTextField3)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jButton3, GroupLayout.Alignment.TRAILING, -2, 25, -2).addComponent(this.jButton4, GroupLayout.Alignment.TRAILING, -2, 25, -2)))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jTextField2, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel2).addComponent(this.jTextField1, -2, -1, -2)).addGap(8, 8, 8).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(3, 3, 3).addComponent(this.jLabel6)).addComponent(this.jComboBox1, -2, 21, -2)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.dateChooserCombo1, -2, -1, -2).addComponent(this.jLabel3)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jFormattedTextField1, -2, 21, -2).addComponent(this.jLabel7)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 13, 32767).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel4).addComponent(this.jTextField3, -2, -1, -2).addComponent(this.jButton3)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.jTextField4, -2, -1, -2).addComponent(this.jButton4)).addGap(18, 18, 18).addComponent(this.jScrollPane1, -2, 93, -2).addContainerGap()));
        this.getContentPane().add((Component)this.jPanel1, "Center");
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void jButton3ActionPerformed(ActionEvent evt) {
        this.con.uni_query(SCarDrivers.query1);
        new DSearch1(null, true, this.con.res).setVisible(true);
        this.jTextField3.setText(MainFrame.RESULT);
        this.drv = MainFrame.RID;
    }

    private void jButton4ActionPerformed(ActionEvent evt) {
        this.con.uni_query(SGSM.query3);
        new DSearch1(null, true, this.con.res).setVisible(true);
        this.jTextField4.setText(MainFrame.RESULT);
        this.gsm = MainFrame.RID;
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        this.dispose();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        if (!this.edit) {
            MainFrame.q.append("INSERT INTO cars (model, number, type_id, gsm_id, manufactured, rashod, probeg, driver_id, volume, rashod_special, climb)").append(" VALUES ('").append(this.jTextField2.getText()).append("', '").append(this.jTextField1.getText()).append("', ").append(this.types.get(this.jComboBox1.getSelectedIndex())).append(", ").append(this.gsm).append(", ").append(this.dateF(this.dateChooserCombo1.getSelectedDate())).append(", ").append(this.jTable1.getValueAt(0, 1).toString().replace(',', '.')).append(", ").append(this.toNull(this.jFormattedTextField1.getText().replace(',', '.'))).append(", ").append(this.drv).append(", ").append(this.jTable1.getValueAt(3, 1).toString().replace(',', '.')).append(", ").append(this.jTable1.getValueAt(1, 1).toString().replace(',', '.')).append(", ").append(this.jTable1.getValueAt(2, 1).toString().replace(",", ".")).append(")");
            MainFrame.exec = true;
        } else {
            MainFrame.q.append("UPDATE cars ").append(" SET model='").append(this.jTextField2.getText()).append("', number='").append(this.jTextField1.getText()).append("', type_id=").append(this.types.get(this.jComboBox1.getSelectedIndex())).append(", gsm_id=").append(this.gsm).append(", manufactured=").append(this.dateF(this.dateChooserCombo1.getSelectedDate())).append(", rashod=").append(this.jTable1.getValueAt(0, 1).toString().replace(',', '.')).append(", probeg=").append(this.toNull(this.jFormattedTextField1.getText().replace(',', '.'))).append(", driver_id=").append(this.drv).append(", volume=").append(this.toNull(this.jTable1.getValueAt(3, 1).toString().replace(',', '.'))).append(", rashod_special=").append(this.jTable1.getValueAt(1, 1).toString().replace(',', '.')).append(", climb=").append(this.jTable1.getValueAt(2, 1).toString().replace(",", ".")).append(" WHERE id=").append(this.id);
            MainFrame.exec = true;
        }
        this.dispose();
    }

}

