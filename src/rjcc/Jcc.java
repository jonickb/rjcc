/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Jonick 2016
 * @version 1.2.1
 */
public class Jcc {

    /**
     * @param args the command line arguments
     */    
    
    static String ver = "";    
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{  
        
        try {
            URL url = new URL("http://city-clean.dev/version.txt");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();  
	  if (conn.getResponseCode() / 100 != 2) {
	    ver = "Ответ сервера. Код: "+conn.getResponseCode();
	  }            
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            DataInputStream dis = new DataInputStream(bis);
			while (dis.available() != 0) {				
                                ver = "Соединение установленно. Версия сервера: "+dis.readLine();
			}                        
        } catch (MalformedURLException ex) {
            ver = "MalformedURLException. Ошибка соединения с сервером обновлений.";
        } catch (IOException ex) {
            ver = "IOException. Ошибка чтения файлов обновлений.";
        }
        
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame(ver).setVisible(true);
        });         
    }
    
}
