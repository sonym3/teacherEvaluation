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


@Path("viewData&{sids}")
    @GET
    @Produces("text/plain")
    public String getText(@PathParam("sids") int sids) {
        //TODO return proper representation object
        Instant instant = Instant.now();
        long time = instant.getEpochSecond();
        JSONObject jsonObject = new JSONObject();
        PreparedStatement stmx=null;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
            
            Statement stm = con.createStatement();
            
            String sql = "select round(avg(rating),2) from RATING\n" +
"where sids=? order by pidt";
            stmx=con.prepareStatement(sql);
            stmx.setInt(1, sids);   
            ResultSet rs = stm.executeQuery(sql);

              if(rs.next() == false){
                 jsonObject.accumulate("Status", "ERROR");
                jsonObject.accumulate("Timestamp", time);   
              
                }  
            else {
                
                  jsonObject.accumulate("Status", "OK");
                  jsonObject.accumulate("Timestamp :", time);
                  jsonObject.accumulate("ratedb", rs.getString(1));
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
        return jsonObject.toString();
    }
}
