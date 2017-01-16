/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rjcc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static rjcc.MainFrame.error;

/**
 *
 * @author User
 */
public class Connector {
    
    Connection con = null;    
    Statement stmt;
    ResultSet rs;
    ResultSetMetaData rsmd;    
    
    ArrayList<Object[]> res = new ArrayList<>();
    ArrayList<Object> resOne = new ArrayList<>();
    
	public void connect(String driver, String srv, String dbName, String charset, String user, String pass) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://"+srv+"/"+dbName+"?useUnicode=true&characterEncoding="+charset, user, pass);
                stmt = con.createStatement();
	} 
        // универсальный запрос
        void uni_query(String query){            
        try {
            res.clear();
            rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
            int colNumber = rsmd.getColumnCount();
            Object[] obj = new Object[colNumber];
            while (rs.next()){
                for (int i = 1; i <= colNumber; i++){
                    obj[i - 1] = this.rs.getObject(i) != null ? this.rs.getObject(i) : "";
                }
                res.add(obj.clone());
            }
            rs.close();
        } catch (SQLException ex) {
            this.res.clear();
            JOptionPane.showMessageDialog(null, ex, "Ошибка!", JOptionPane.ERROR_MESSAGE, error);}
        }
        
        void getList(String query) throws SQLException{
            resOne.clear();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                resOne.add(rs.getObject(1));
            }
            rs.close();
        }
        
        Object getOne(String query){
            StringBuffer str = new StringBuffer();
            str.setLength(0);
            try {
                rs = stmt.executeQuery(query);            
                while (rs.next()){
                    if (rs.getObject(1) != null){
                        str.append(rs.getObject(1));
                        }
                    else{
                        str.append("");
                    }
                    }
                    rs.close();
                    return str;
            } catch (SQLException ex) {
                System.out.println("Exception getOne(String): " + ex + " \n"+query);
                return str.append("");}            
        }
        
        Object getOne(StringBuffer query){
            StringBuffer str = new StringBuffer();
            str.setLength(0);
            try {
                rs = stmt.executeQuery(query.toString());            
                while (rs.next()){
                    if (rs.getObject(1) != null){
                        str.append(rs.getObject(1));
                        }
                    else{
                        str.append("");
                    }
                    } 
                    rs.close();
                    return str;
            } catch (SQLException ex) {
                System.out.println("Exception getOne(StringBuffer): " + ex + " \n"+query);
                return str.append("");}            
        }        
        void getListWId(String query) throws SQLException{
            resOne.clear();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                resOne.add(rs.getObject(1)+"$"+rs.getObject(2));
            }
            rs.close();
        }
        String getLastID(){
        try {
            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            return rs.getObject(1).toString();
        } catch (SQLException ex) {return "0";}
        }
        void uni_execute(String query) throws SQLException{
            stmt.execute(query);
        }
        void closeConnection() throws SQLException{ 
            System.out.println("Close connection");
            stmt.close();
            con.close();
        }
        void closeSession(String id) throws SQLException {
            uni_execute("UPDATE session SET status=0, ulogin_date=NOW() WHERE id="+id);
        }
        boolean isAlive() {
        try {
            return con.isValid(0);
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
        }        

}
