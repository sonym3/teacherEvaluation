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
    public Persons() {
    }

    /**
     * Retrieves representation of an instance of teacherevaluation.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getText() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

public JSONObject listCountries(){
   
        Connection con=null;
        Statement stmt=null;
        JSONObject mainObject1=new JSONObject();
        JSONObject jsonObject=new JSONObject();
        String status=null;
       
        
        try{
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con=DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "mad312team5", "anypw");
           
            String sql="select * from persons";
            stmt=con.createStatement();
            ResultSet result=stmt.executeQuery(sql);
            JSONArray jsonArray=new JSONArray();
            Instant instant=Instant.now();
            long time=instant.getEpochSecond();
             
   
           if(result.next() == false){
                status="Faild";
                mainObject1.accumulate("Status :", status);
                mainObject1.accumulate("Timestamp :", time);
                mainObject1.accumulate("Message :", " Fetching Failed");
               
                }  
            
            else {
                mainObject1.accumulate("Status :", "OK");
                mainObject1.accumulate("Timestamp :", time);
                do{
                    jsonObject.accumulate("PID : ", result.getInt(1));
                    jsonObject.accumulate("First Name : ", result.getString(2));
                    jsonObject.accumulate("Last Name : ", result.getString(3));
                    jsonObject.accumulate("email : ", result.getString(4));
                    jsonObject.accumulate("Phone : ", result.getBigDecimal(5));
                    jsonObject.accumulate("Address : ", result.getString(6));
                    jsonObject.accumulate("Postal : ", result.getString(7));
                    jsonArray.add(jsonObject);
                    jsonObject.clear();
                }while(result.next());
                mainObject1.accumulate("Details of Countries: ", jsonArray);
                }  
             
             
             
             stmt.close();
             con.close();

           
         } catch (SQLException ex) {
            Logger.getLogger(Persons.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainObject1;
    
    }
    
    @GET
     @Path("personslist")
    @Produces(MediaType.TEXT_PLAIN)
        public String getListCountries() {   
        JSONObject mainobject=new JSONObject();
        mainobject=listCountries();
        return mainobject.toString();
    }
}
