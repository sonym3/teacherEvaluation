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
import java.sql.SQLIntegrityConstraintViolationException;
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

 
@Path("ratingTable")
  
public class Rating {
    
    @Context
    private UriInfo context;
    
    
    
    public Rating(){
        
    }
    
    @GET
    @Path("insertRating&{pids}&{sids}&{pidt}&{feedback}&{rating}")
    @Produces("text/plain")
public String insertRating(@PathParam("pids") int pids,@PathParam("sids") int sids,
        @PathParam("pidt") int pidt,@PathParam("feedback") String feedback,
        @PathParam("rating") float rating)
       {
    Instant instant=Instant.now();
            long time=instant.getEpochSecond();
        Connection con=null;
       PreparedStatement stmx=null;
        JSONObject mainx=new JSONObject();
        
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
           
            String sql="insert into rating(pids,sids,pidt,feedback,rating) values (?,?,?,?,?)";
            stmx=con.prepareStatement(sql);
            stmx.setInt(1, pids);
            stmx.setInt(2, sids);
            stmx.setInt(3, pidt);
            stmx.setString(4, feedback); 
            stmx.setFloat(5, rating);
            int resultx=stmx.executeUpdate();
             
           
   
           if(resultx>0){
               mainx.accumulate("Status", "OK");
                mainx.accumulate("Timestamp", time);         
                }  
            else {
                mainx.accumulate("Status", "ERROR");
                mainx.accumulate("Timestamp", time);    
           }  
             stmx.close();
             con.close();

           
         } catch(SQLIntegrityConstraintViolationException e) {
             mainx.accumulate("Status", "ERROR");
             mainx.accumulate("Timestamp", time);
        }catch (SQLException ex) {
             Logger.getLogger(Rating.class.getName()).log(Level.SEVERE, null, ex);

         }
        return mainx.toString();
    
    }


@Path("viewData&{sids1}&{sids2}")
    @GET
    @Produces("text/plain")
    public String getText4(@PathParam("sids1") int sids1,
            @PathParam("sids2") int sids2) {
        JSONObject firstOne = new JSONObject();
            JSONObject secondOne = new JSONObject();
                        JSONArray jsonArray=new JSONArray();
try{
    
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
           
            String sql="select sids,round(avg(rating),2) from RATING\n" +
"where sids between ? and ?\n" +
"group by sids";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, sids1);
            stmt.setInt(2, sids2);

            ResultSet result=stmt.executeQuery();
            
            Instant instant=Instant.now();
            long time=instant.getEpochSecond();
            
   
           if(result.next() == false){
                firstOne.accumulate("Status", "ERROR");
                firstOne.accumulate("Timestamp", time);               
                }  
            
            else {
                firstOne.accumulate("Status", "OK");
                firstOne.accumulate("Timestamp", time);
                do{
                    secondOne.accumulate("subject: ", result.getInt(1));
                    secondOne.accumulate("rating", result.getFloat(2));
                    jsonArray.add(secondOne);
                    secondOne.clear();
                }while(result.next());
                firstOne.accumulate("Details", jsonArray);
                }  
             
             
             
             stmt.close();
             con.close();

         } catch (SQLException ex) {
            Logger.getLogger(Rating.class.getName()).log(Level.SEVERE, null, ex);
        }
           return firstOne.toString();

    }
}
      