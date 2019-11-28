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
import net.sf.json.JSONObject;

/**
 *
 * @author HP
 */
@Path("credentials")
public class Crendentials {
     @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    
    @GET
    @Path("validate&{username}")
    @Produces("application/json")
public String getCrentials(@PathParam("username") int username){
   
        Connection con=null;
        JSONObject mainObject1=new JSONObject();
        String status=null;
        
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
             Statement stm = con.createStatement();
            String sql="select username,password from credentials where username="+username;
            ResultSet result = stm.executeQuery(sql);
            Instant instant=Instant.now();
            long time=instant.getEpochSecond();
           if(result.next() == false){
   
                   status="ERROR";
                mainObject1.accumulate("Status", status);
                mainObject1.accumulate("Timestamp", time);      
                }  
            
            else {
               do{
                  status="OK";   
                    mainObject1.accumulate("Status", status);
                    mainObject1.accumulate("Timestamp", time);
                    mainObject1.accumulate("username", result.getString("username"));
                    mainObject1.accumulate("password",result.getString("password"));
                  }while(result.next());     
                
                }  
             stm.close();
             con.close();

           
         } catch (SQLException ex) {
            Logger.getLogger(Crendentials.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainObject1.toString();
    
    }

@GET
    @Path("updatePassword&{username}&{password}")
    @Produces("application/json")
public String updatePassword(@PathParam("username") int username,
        @PathParam("password") String password){
   
        Connection con=null;
       PreparedStatement stmx=null;
        JSONObject singleObject=new JSONObject();
        int result = 0;
        
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
           
            String sql="update CREDENTIALS set password=? where username=?";
            stmx=con.prepareStatement(sql);
             stmx.setString(1, password);
             stmx.setInt(2, username);
             result=stmx.executeUpdate();
             
            Instant instant=Instant.now();
            long time=instant.getEpochSecond();
             
   
           if(result>0){
               singleObject.accumulate("Status", "OK");
                singleObject.accumulate("Timestamp", time);         
                }  
            
            else {
                singleObject.accumulate("Status", "ERROR");
                singleObject.accumulate("Timestamp", time);    
           }  
             stmx.close();
             con.close();

           
         } catch (SQLException ex) {
            Logger.getLogger(Crendentials.class.getName()).log(Level.SEVERE, null, ex);
        }
        return singleObject.toString();
    
    }
}

