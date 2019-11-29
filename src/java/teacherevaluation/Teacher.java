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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author HP
 */
@Path("teacher")
public class Teacher {
     @Context
    private UriInfo context;
    @Path("listTeacher")
    @GET
    @Produces("text/plain")
    public String getText() {
        //TODO return proper representation object
        Instant instant = Instant.now();
        long time = instant.getEpochSecond();
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
            
            Statement stm = con.createStatement();
            
            String sql = "Select teacher.PID,fname,lname,email,phone,address,postal,teacher.designation from persons\n" +
"Join teacher on teacher.pid=persons.pid";
            ResultSet rs = stm.executeQuery(sql);

            int id;
            String fname, lname, email, phone, address, postal, designation;
           // double salary, commision;

            
              if(rs.next() == false){
                 jsonObject1.accumulate("Status", "ERROR");
                jsonObject1.accumulate("Timestamp", time);   
              
                }  
            
            else {
                
                  jsonObject1.accumulate("Status", "OK");
                    jsonObject1.accumulate("Timestamp :", time);
                    do{
                     id = rs.getInt("pid");
                fname = rs.getString("fname");
                lname = rs.getString("lname");
                email = rs.getString("email");
                phone = rs.getString("phone");
                address = rs.getString("address");
                postal = rs.getString("postal");
                designation = rs.getString("designation");
                
                jsonObject.accumulate("id", id);
                jsonObject.accumulate("fname", fname);
                jsonObject.accumulate("lname", lname);
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("phone", phone);
                jsonObject.accumulate("address", address);
                jsonObject.accumulate("postal", postal);
                jsonObject.accumulate("designation", designation);
                                    jsonArray.add(jsonObject);
                    jsonObject.clear();

                  }while(rs.next());
                   jsonObject1.accumulate("Details of teachers ", jsonArray);

              }
                rs.close();
                stm.close();
                con.close();
                
              //  singleEmployee.clear();
               
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonObject1.toString();
    }
        
    
}
