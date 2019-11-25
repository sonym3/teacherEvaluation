/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacherevaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import net.sf.json.JSONObject;

/**
 *
 * @author HP
 */
@Path("student")

public class Student {
     @Context
    private UriInfo context;
    
    @GET
    @Path("singleStudent&{id}")
    @Produces("text/plain")
    public String getText(@PathParam("id") int studentID) {
        //TODO return proper representation object
        Instant instant = Instant.now();
        long time = instant.getEpochSecond();
        JSONObject singleEmployee = new JSONObject();
        singleEmployee.accumulate("Status", "Error");
        
        singleEmployee.accumulate("Message", "Student id not exists");        
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
            Statement stm = con.createStatement();
            String sql = "Select student.PID,fname,lname,email,phone,address,postal,program from persons \n" +
"Join student on student.pid=persons.pid \n" +
"where student.pid =" + studentID;
            ResultSet rs = stm.executeQuery(sql);

            int id;
            String fname, lname, email, phone, address, postal, program;
           // double salary, commision;

            
              if(rs.next() == false){
                 singleEmployee.accumulate("Status", "ERROR");
                singleEmployee.accumulate("Timestamp", time);   
               
                }  
            
            else {
                do{
                  singleEmployee.accumulate("Status", "OK");
                    singleEmployee.accumulate("Timestamp :", time);
                     id = rs.getInt("pid");
                fname = rs.getString("fname");
                lname = rs.getString("lname");
                email = rs.getString("email");
                phone = rs.getString("phone");
                address = rs.getString("address");
                postal = rs.getString("postal");
                program = rs.getString("program");
                
                singleEmployee.accumulate("id", id);
                singleEmployee.accumulate("fname", fname);
                singleEmployee.accumulate("lname", lname);
                singleEmployee.accumulate("email", email);
                singleEmployee.accumulate("phone", phone);
                singleEmployee.accumulate("address", address);
                singleEmployee.accumulate("postal", postal);
                singleEmployee.accumulate("program", program);
                  }while(rs.next());

                rs.close();
                stm.close();
                con.close();
                }  
              //  singleEmployee.clear();
               
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }

        return singleEmployee.toString();
    }
        
    }
