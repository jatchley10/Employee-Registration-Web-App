/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
public class Database {    
    private Connection getConnection() {        
        Connection conn = null;        
        try {            
            Context envContext = new InitialContext();            
            Context initContext  = (Context)envContext.lookup("java:/comp/env");            
            DataSource ds = (DataSource)initContext.lookup("jdbc/db_pool");            
            conn = ds.getConnection();
             
        }                
        catch (Exception e) { e.printStackTrace(); }
        return conn;    
    }
    
    protected Connection getDBConn(){
        Database db = new Database();
        return db.getConnection();
    }
    
    protected String insertEmployee(String fName, String lName, String disName, String sessionID){
        ResultSet keys;
        String key = "";
        Database db = new Database();
        Connection conn = db.getDBConn();
        String query = "INSERT INTO registration_db.registrations (firstname,lastname,displayname,sessionid) values (?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, disName);
            ps.setString(4, sessionID);
            int res = ps.executeUpdate();
            if(res == 1){
                keys = ps.getGeneratedKeys();
                if(keys.next()){
                    key = keys.getString(1);
                }
                
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }
    
    
}
