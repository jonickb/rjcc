/*
 * Decompiled with CFR 0_115.
 */
package rjcc;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OGSM {
    JTable table;
    DefaultTableModel model;
    String title;
    String query;
    JccTableCellRenderer renderer = new JccTableCellRenderer();

    public OGSM() {
        this.table = new JTable(){

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        this.model = new DefaultTableModel();
        this.title = "Отчет 2";
        this.query = "SELECT cars.number, drivers.name, DATE_FORMAT(date_start, '%d.%m.%Y'), time_start, DATE_FORMAT(date_end, '%d.%m.%Y'), time_end, odo_start, odo_end, (odo_end-odo_start), gsm_start, gsm_end, (gsm_start-gsm_end+gsm_get), gsm_get, special_start, special_end  FROM route  left join cars on route.car_id=cars.id   LEFT JOIN drivers ON route.driver_id=drivers.id WHERE car_id=";
        this.initTable();
    }

    String getJournal(String id, String ds, String de) {
        if (ds == null) {
            ds = "";
        }
        if (de == null) {
            de = "";
        }
        String q = this.query + id + " AND date_start>='" + ds + "' AND date_end<='" + de + "' AND route.status <> 3 ORDER BY route.date_start";
        System.out.println("GSM query > " + q);
        return q;
    }

    JTable getTable() {
        return this.table;
    }

    String getJournal() {
        return this.query;
    }

    DefaultTableModel getModel() {
        return this.model;
    }

    final void initTable() {
        this.model.addColumn("Гос. номар");
        this.model.addColumn("Водитель");
        this.model.addColumn("Дата выезда");
        this.model.addColumn("Время выезда");
        this.model.addColumn("Дата вызвращения");
        this.model.addColumn("Время возвращения");
        this.model.addColumn("Спидометр выезда");            
        this.model.addColumn("Спидометр возвращения");
        this.model.addColumn("Пробег");
        this.model.addColumn("ГСМ выезда");
        this.model.addColumn("ГСМ возвращения");
        this.model.addColumn("Расход");
        this.model.addColumn("Дозаправка");
        this.model.addColumn("Моточасы выезда");
        this.model.addColumn("Моточасы возвращения");
        this.table.setModel(this.model);
        this.table.setAutoCreateRowSorter(true);
        this.table.setFont(new Font("Tahoma", 0, 12));
        this.table.setGridColor(new Color(0, 0, 0));
        this.table.setRowHeight(20);
        this.table.setSelectionBackground(new Color(204, 204, 204));
        this.table.setSelectionForeground(new Color(0, 0, 0));
        this.table.setSelectionMode(0);
        this.table.setShowVerticalLines(false);
            table.getColumnModel().getColumn(6).setCellRenderer(renderer);
            table.getColumnModel().getColumn(7).setCellRenderer(renderer);   
            table.getColumnModel().getColumn(8).setCellRenderer(renderer);   
            table.getColumnModel().getColumn(9).setCellRenderer(renderer);   
            table.getColumnModel().getColumn(10).setCellRenderer(renderer);   
            table.getColumnModel().getColumn(11).setCellRenderer(renderer);   
            table.getColumnModel().getColumn(12).setCellRenderer(renderer);   
            table.getColumnModel().getColumn(13).setCellRenderer(renderer);   
            table.getColumnModel().getColumn(14).setCellRenderer(renderer);   
    }

}

