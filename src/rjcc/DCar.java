/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public final class DCar extends javax.swing.JDialog {

    /**
     * Creates new form DCar
     */
    ArrayList<String> types = new ArrayList<>();
    String drv = "0";
    String gsm = "0";
    String type = "0";
    Connector con;
    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
    String id = "";
    boolean edit = false;
    Calendar cal = Calendar.getInstance();
    DefaultTableModel tm = new DefaultTableModel();
    
    public DCar(java.awt.Frame parent, boolean modal, Connector con) {
        super(parent, modal);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("edit.png")));
        initTable();
        initComponents();
        this.con = con;
        jComboBox1.removeAllItems();
        try {
            con.getListWId(SCarType.query1);
            int index = 0;
            String str;
            for (int i = 0; i < con.resOne.size(); i++){
                index = con.resOne.get(i).toString().indexOf('$');
                types.add(con.resOne.get(i).toString().substring(0, index));
                str = con.resOne.get(i).toString().substring(index+1, con.resOne.get(i).toString().length());
                jComboBox1.addItem(str);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DCar.class.getName()).log(Level.SEVERE, null, ex);
        }
        edit = false;
    }
    public DCar(java.awt.Frame parent, boolean modal, String id, Connector con){
        super(parent, modal);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("edit.png")));
        initTable();
        initComponents();
        this.con = con;
        jComboBox1.removeAllItems();
        try {
            con.getListWId(SCarType.query1);
            int index = 0;
            String str;
            for (int i = 0; i < con.resOne.size(); i++){
                index = con.resOne.get(i).toString().indexOf('$');
                types.add(con.resOne.get(i).toString().substring(0, index));
                str = con.resOne.get(i).toString().substring(index+1, con.resOne.get(i).toString().length());
                jComboBox1.addItem(str);
            }
        } catch (SQLException ex) {Logger.getLogger(DCar.class.getName()).log(Level.SEVERE, null, ex);}
        this.id = id;
        jTextField2.setText(con.getOne("SELECT model FROM cars WHERE id="+id).toString());
        jTextField1.setText(con.getOne("SELECT number FROM cars WHERE id="+id).toString());
        String obj;
        try {
            obj =con.getOne("SELECT DATE_FORMAT(manufactured, '%d.%m.%Y') FROM cars WHERE id="+id).toString();
            if (!"".equals(obj)){
                cal.setTime(sdf1.parse(obj));
                dateChooserCombo1.setSelectedDate(cal);
                } else dateChooserCombo1.setSelectedDate(null);
                //jDateChooser1.setDate(sdf1.parse(obj));
        } catch (ParseException ex) {System.out.println(ex);}
        drv = con.getOne("SELECT driver_id FROM cars WHERE id="+id).toString();
        gsm = con.getOne("SELECT gsm_id FROM cars WHERE id="+id).toString();
        type = con.getOne("SELECT type_id FROM cars WHERE id="+id).toString();
        jTextField3.setText(con.getOne("SELECT name FROM drivers WHERE id="+drv).toString());
        jTextField4.setText(con.getOne("SELECT type FROM gsm WHERE id="+gsm).toString());
        int index = 0;
        for (int i = 0; i<types.size(); i++){
            if (Integer.parseInt(types.get(i)) == Integer.parseInt(type))
                index = i;
        }
        jComboBox1.setSelectedIndex(index);
        jFormattedTextField1.setText(con.getOne("SELECT probeg FROM cars WHERE id="+id).toString());
        tm.setValueAt(con.getOne("SELECT rashod FROM cars WHERE id="+id).toString().replace(".", ","), 0, 1);        
        tm.setValueAt(con.getOne("SELECT rashod_special FROM cars WHERE id="+id).toString().replace(".", ","), 1, 1);
        tm.setValueAt(con.getOne("SELECT climb FROM cars WHERE id="+id).toString().replace(".", ","), 2, 1);
        tm.setValueAt(con.getOne("SELECT volume FROM cars WHERE id="+id).toString().replace(".", ","), 3, 1);
        edit = true;        
    }
    
    void initTable() {
        tm.addColumn("Параметр");
        tm.addColumn("Значение");        
        tm.addRow(new Object[]{"Расход (л. на 100км.)", "0,00"});
        tm.addRow(new Object[]{"Моточасы (л. на 1ч.)", "0,00"});
        tm.addRow(new Object[]{"Подъем (л. на 1 ездку)", "0,000"});
        tm.addRow(new Object[]{"Объем (м3)", "0,00"});
    }

    String dateF(Calendar date){
        if (date != null)
            return "date('"+sdf.format(date.getTime())+"')";
        else
            return null;
    }
    
    String toNull(String s){
        if (s.equals(""))
            return "0";
        else
            return s;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Автомобиль");
        setModal(true);
        setResizable(false);

        jToolBar1.setRollover(true);

        jButton1.setText("Сохранить");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton4.setText("Отмена");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jLabel1.setText("Модель");

        jLabel2.setText("Регистрационный номер");

        jLabel3.setText("Дата выпуска");

        jLabel4.setText("Водитель");

        jLabel5.setText("Топливо");

        jLabel6.setText("Тип");

        jLabel7.setText("Начальный пробег");

        jTextField3.setEditable(false);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField4.setEditable(false);

        jButton3.setText("...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        dateChooserCombo1.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        dateChooserCombo1.setFormat(2);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Нормы расхода"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable1.setModel(tm);
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(84, 84, 84)
                                .addComponent(jTextField3))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addGap(61, 61, 61)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jFormattedTextField1)
                                        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                                    .addComponent(jTextField4))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Водитель
//        try {
//            con.getListWId(SCarDrivers.query1);
//        } catch (SQLException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE);}
//        new DSearch(null, true, con.resOne, true).setVisible(true);
        con.uni_query("SELECT id, name FROM drivers WHERE activity=1 ORDER BY name");
        new DSearch1(null, true, con.res).setVisible(true);
    jTextField3.setText(MainFrame.RESULT);    
    drv = MainFrame.RID;        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // топливо
//        try {
//            con.getListWId(SGSM.query3);
//        } catch (SQLException ex) {JOptionPane.showMessageDialog(this, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE);}
//        new DSearch(null, true, con.resOne, true).setVisible(true);
        con.uni_query("SELECT id, type FROM gsm");
        new DSearch1(null, true, con.res).setVisible(true);
        jTextField4.setText(MainFrame.RESULT);    
        gsm = MainFrame.RID;               
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (!edit){
            MainFrame.q.append("INSERT INTO cars (model, number, type_id, gsm_id, manufactured, rashod, probeg, driver_id, volume, rashod_special, climb)").append(" VALUES ('")
                .append(jTextField2.getText()).append("', '").append(jTextField1.getText()).append("', ")
                .append(types.get(jComboBox1.getSelectedIndex())).append(", ").append(gsm).append(", ").append(dateF(dateChooserCombo1.getSelectedDate()))
                .append(", ")
                .append(toNull(tm.getValueAt(0, 1).toString().replace(',', '.'))).append(", ").append(toNull(jFormattedTextField1.getText().replace(',', '.'))).append(", ")
                .append(drv)
                .append(", ")
                .append(toNull(tm.getValueAt(3, 1).toString().replace(',', '.')))
                .append(", ")
                .append(toNull(tm.getValueAt(1, 1).toString().replace(',', '.')))
                .append(", ")
                .append(toNull(tm.getValueAt(2, 1).toString().replace(',', '.')))
                .append(")");
            MainFrame.exec = true;
        } else {
            MainFrame.q.append("UPDATE cars ").append(" SET model='").append(jTextField2.getText()).append("', number='").append(jTextField1.getText()).append("', type_id=")
                .append(types.get(jComboBox1.getSelectedIndex())).append(", gsm_id=").append(gsm).append(", manufactured=").append(dateF(dateChooserCombo1.getSelectedDate()))
                .append(", rashod=")
                .append(toNull(tm.getValueAt(0, 1).toString().replace(',', '.'))).append(", probeg=").append(toNull(jFormattedTextField1.getText().replace(',', '.'))).append(", driver_id=")
                .append(drv)
                .append(", volume=")
                .append(toNull(tm.getValueAt(3, 1).toString().replace(',', '.'))).append(", rashod_special=").append(toNull(tm.getValueAt(1, 1).toString().replace(',', '.')))
                .append(", climb=")
                .append(toNull(tm.getValueAt(2, 1).toString().replace(',', '.')))
                .append(" WHERE id=")
                .append(id);
            MainFrame.exec = true;
        }
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
