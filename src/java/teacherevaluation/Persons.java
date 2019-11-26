/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacherevaluation;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import net.sf.json.JSONObject;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author HP
 */
@Path("persons")
public class Persons {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    
@GET
    @Path("updateStudent&{email}&{phone}&{address}&{postal}&{pid}")
    @Produces("text/plain")
public String udatePersos(@PathParam("email") String email,
        @PathParam("phone") String phone,
        @PathParam("address") String address,
        @PathParam("postal") String postal,
        @PathParam("pid") int pid){
   
        Connection con=null;
       PreparedStatement stm=null;
        JSONObject mainObject1=new JSONObject();
        String status=null;
        int result = 0;
        
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
           
            String sql="update persons set email=?, phone=?, address=?,postal=? where pid=?";
            stm=con.prepareStatement(sql);
             stm.setString(1, email);
             stm.setString(2, phone);
             stm.setString(3, address);
             stm.setString(4, postal);
             stm.setInt(5, pid);
             
             result=stm.executeUpdate();
             
            Instant instant=Instant.now();
            long time=instant.getEpochSecond();
             
   
           if(result>0){
               mainObject1.accumulate("Status", "OK");
                mainObject1.accumulate("Timestamp", time);         
                }  
            
            else {
                mainObject1.accumulate("Status", "ERROR");
                mainObject1.accumulate("Timestamp", time);    
           }  
             stm.close();
             con.close();

           
         } catch (SQLException ex) {
            Logger.getLogger(Persons.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainObject1.toString();
    
    }
}
