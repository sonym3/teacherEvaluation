/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacherevaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
@Path("studentsubject")

public class StudentSubject {
     @Context
    private UriInfo context;
   
    @GET
    @Path("getSubject&{pids}")
    @Produces("text/plain")
public String getSubject(@PathParam("pids") int pids)
       {
   
        Connection con=null;
JSONObject mainJSONObject = new JSONObject();        
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
            try (Statement stm = con.createStatement()) {
                String sql="select sids,subject.SNAME,teacher.PID,persons.fname,persons.lname from stusub \n" +
"join student on student.PID=stusub.PIDS\n" +
"join subject on stusub.SIDS=subject.SID\n" +
"join teacher on teacher.PID=subject.PIDT\n" +
"join persons on teacher.pid=persons.pid\n" +
"where student.PID="+pids;
                ResultSet result = stm.executeQuery(sql);
                
                Instant instant=Instant.now();
                long time=instant.getEpochSecond();
                
                
                JSONArray array = new JSONArray();
                JSONObject secondObject = new JSONObject();
                
                
                mainJSONObject.accumulate("Status", "ERROR");
                mainJSONObject.accumulate("Timestamp", time);
                
                
                    while(result.next()){
                       mainJSONObject.clear();
                        mainJSONObject.accumulate("Status", "OK");
                        mainJSONObject.accumulate("Timestamp", time);
                            secondObject.accumulate("subject_id", result.getInt(1));
                            secondObject.accumulate("subject_name", result.getString(2));
                            secondObject.accumulate("teacher_id", result.getInt(3));
                            secondObject.accumulate("teacher_fname", result.getString(4));
                            secondObject.accumulate("teacher_lname", result.getString(5));

                            array.add(secondObject);
                            secondObject.clear();
                    }
                    mainJSONObject.accumulate("Details of subject and teacher", array);
                    
            }
            
                con.close();           
         } catch (SQLException ex) {
            Logger.getLogger(Rating.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainJSONObject.toString();
    
    }

    
    
}