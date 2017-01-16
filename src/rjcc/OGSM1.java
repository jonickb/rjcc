/*
 * Decompiled with CFR 0_115.
 */
package rjcc;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OGSM1 {
    JTable table;
    DefaultTableModel tm;
    String title;
    String query;
    String query1;

    public OGSM1() {
        this.table = new JTable(){

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        this.tm = new DefaultTableModel();
        this.title = "Отчет 1";
        this.query = "SELECT cars.id, cars.gsm_id, cars.model, cars.number, route.gsm_start, SUM(route.gsm_get), SUM(route.gsm_get)+SUM(route.gsm_start)-SUM(route.gsm_end) FROM route LEFT JOIN cars ON route.car_id = cars.id";
        this.query1 = "SELECT route.gsm_end FROM route WHERE car_id=";
        this.initTable();
    }

    String getJournal(String ds, String de) {
        if (ds == null) {
            ds = "";
        }
        if (de == null) {
            de = "";
        }
        String q = this.query + " WHERE route.date>='" + ds + "' AND route.date<='" + de + "' " + "AND cars.id <> 'null' AND route.status <> 3 " + "GROUP BY route.car_id";
        return q;
    }

    String getGsmEnd(String id, String de) {
        if (de == null) {
            return this.query1 + id + " ORDER BY route.date DESC LIMIT 1";
        }
        return this.query1 + id + " AND route.date<='" + de + "' ORDER BY route.date DESC LIMIT 1";
    }

    JTable getTable() {
        return this.table;
    }

    String getJournal() {
        return this.query;
    }

    DefaultTableModel getModel() {
        return this.tm;
    }

    final void initTable() {
        this.tm.addColumn("№");
        this.tm.addColumn("Модель ТС");
        this.tm.addColumn("Гос.номер");
        this.tm.addColumn("Остаток пред.смены");
        this.tm.addColumn("ДТ выдано");
        this.tm.addColumn("ДТ использовано");
        this.tm.addColumn("АИ92 выдано");
        this.tm.addColumn("АИ92 использовано");
        this.tm.addColumn("АИ95 выдано");
        this.tm.addColumn("АИ95 использовано");
        this.tm.addColumn("Остаток");
        this.table.setModel(this.tm);
        this.table.setAutoCreateRowSorter(true);
        this.table.setFont(new Font("Tahoma", 0, 12));
        this.table.setGridColor(new Color(0, 0, 0));
        this.table.setRowHeight(20);
        this.table.setSelectionBackground(new Color(204, 204, 204));
        this.table.setSelectionForeground(new Color(0, 0, 0));
        this.table.setSelectionMode(0);
        this.table.setShowVerticalLines(false);
    }

}

