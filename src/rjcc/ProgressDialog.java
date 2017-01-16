/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class ProgressDialog {
  private final JFrame frame = new JFrame();
  private final JDialog dialog = new JDialog(frame, "Выполнение задачи", false);
  private final JProgressBar progressBar = new JProgressBar();
  /** Конструктор progreeBar и диалога, в котором он содержится */
  public ProgressDialog(){
	frame.setUndecorated(true);
    // Если мы не знаем, сколько времени займет операция, 
    // делаем progressBar в виде "катающегося объекта", а не "ползунка".
    progressBar.setIndeterminate(true);
    // При желании, задаем цвет.
    progressBar.setForeground( new Color(210,105,030));
    // При желании, задаем конкретную форму. В данном случае, progressBar имеет форму мячика,
    // которая описывается в классе ProgressUI.
    progressBar.setUI(new ProgressUI());
    // Убираем рамку из диалога, чтобы был виден только progressBar без кнопок управления окном.
    dialog.setUndecorated(true);
    // добавляем получившийся progressBar в диалог.
    dialog.getContentPane().add(progressBar);
    dialog.pack();
    dialog.setDefaultCloseOperation(0);
    // Задаем центровку диалога.
    Toolkit kit = dialog.getToolkit();
    GraphicsEnvironment ge = GraphicsEnvironment. getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
    Insets in = kit.getScreenInsets(gs[0].getDefaultConfiguration());
    Dimension d = kit.getScreenSize();
    int max_width = (d.width - in.left - in.right);
    int max_height = (d.height - in.top - in.bottom);
    dialog.setLocation((int) (max_width - dialog.getWidth()) / 2, (int) (max_height - dialog.getHeight() ) / 2);
    // Отображаем диалог и progressBar
    dialog.setVisible(true);
    progressBar.setVisible(true);
    dialog.setAlwaysOnTop(true);
  }
  /** Метод, отображающий диалог */
  public void showDialog(){
    dialog.setVisible(true);
    dialog.setAlwaysOnTop(true);
  }
  /** Метод, закрывающий диалог */
  public void closeDialog(){
    if (dialog.isVisible()){
      dialog.getContentPane().remove(progressBar);
      dialog.getContentPane().validate();
      dialog.setVisible(false);
    }
  }
  /** Класс, задающий внешний вид progressBar */
  public static class ProgressUI extends BasicProgressBarUI {
    private Rectangle r = new Rectangle();
    protected void paintIndeterminate(Graphics g, JComponent c) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      r = getBox(r);
      g.setColor(progressBar.getForeground());
      g.fillOval(r.x, r.y, r.height, r.height);
    }
  }
}