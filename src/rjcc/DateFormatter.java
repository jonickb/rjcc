/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.util.Calendar;

/**
 *
 * @author Администратор
 */
public class DateFormatter {
    
    protected String getDateF(Calendar calendar){
        int yy=calendar.get(Calendar.YEAR);
        int mm=calendar.get(Calendar.MONTH)+1;
        int dd=calendar.get(Calendar.DAY_OF_MONTH);
        
        if (calendar != null)
            return yy+"-"+mm+"-"+dd;
        else
            return null;        
    }
    
    String getDateString(Calendar calendar) {
        int yy=calendar.get(Calendar.YEAR);
        int mm=calendar.get(Calendar.MONTH)+1;
        int dd=calendar.get(Calendar.DAY_OF_MONTH);        
        
        switch(mm){
            case 1:
                return dd+" января " + yy + "г.";
            case 2:
                return dd+" февраля " + yy + "г.";                
            case 3:
                return dd+" марта " + yy + "г.";
            case 4:
                return dd+" апреля " + yy + "г.";
            case 5:
                return dd+" мая " + yy + "г.";
            case 6:
                return dd+" июня " + yy + "г.";
            case 7:
                return dd+" июля " + yy + "г.";
            case 8:
                return dd+" августа " + yy + "г.";
            case 9:
                return dd+" сентября " + yy + "г.";
            case 10:
                return dd+" октября " + yy + "г.";
            case 11:
                return dd+" ноября " + yy + "г.";
            case 12:
                return dd+" декабря " + yy + "г.";                
            default:
                return "неопределено";
        }
    }
}
