/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author JSU
 */
@WebServlet(name = "Registration", urlPatterns = {"/registration"})
public class Registration extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(postNewEmployee(request.getParameter("data")));
        }
    }
    
    protected void processGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(sendResultsAsTable(request.getParameter("id")));
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processGet(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processPost(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    protected String sendResultsAsTable(String code){
        //Building the table headers
        String result = "<table>";
        result += "<tr>";
        result += "<th>id</th>";
        result += "<th>firstname</th>";
        result += "<th>lastname</th>";
        result += "<th>displayname</th>";
        result += "<th>sessionid</th></tr>";
        //Iterating through the result sets to build table rows from each
        try {
            String query = "SELECT * FROM registrations r where sessionid like " + code;
            Database db = new Database();
            Connection conn = db.getDBConn();
            PreparedStatement p = conn.prepareStatement(query);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                String indRow = "<tr>";
                indRow += "<td>" + rs.getString("id") + "</td>";
                indRow += "<td>" + rs.getString("firstname") + "</td>";
                indRow += "<td>" + rs.getString("lastname") + "</td>";
                indRow += "<td>" + rs.getString("displayname") + "</td>";
                indRow += "<td>" + rs.getString("sessionid") + "</td>";
                indRow += "</tr>";
                result += indRow;
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        result +=  "</table>";
        
        return result;
    }
    
    protected String postNewEmployee(String data){
        String results = "";
        String[] info = data.split(";");
        String fName,lName,disName,session;
        fName = info[0];
        lName = info[1];
        disName = info[2];
        session = info[3];
        JSONObject json = new JSONObject();
        json.put("name",disName);
        Database update = new Database();
        String regNum = genRegID(update.insertEmployee(fName, lName, disName, session));
        json.put("regID", regNum);
        results = JSONObject.toJSONString(json);
        return results;
    }
    
    protected String genRegID(String primaryKey){
        int numZeros;
        String code = "R";
        int len = 6;
        
        numZeros = len - primaryKey.length();
        for(int i = 0 ; i < numZeros; ++i){
            code += "0";
        }
        code += primaryKey;
        return code;
    }
}
