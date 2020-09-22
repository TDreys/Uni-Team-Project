package database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* It provides MySQL database connection and the function of adding, deleting, modifying and querying database.
*/
public class DatabaseConnetor{
	
	 // under MySQL 8.0  
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost:3306/tankwar?characterEncoding=utf8&autoReconnect=true&useSSL=false";
   // userName and passWord
   static final String USER = "root";
   static final String PASS = "root";
   
   
   Connection conn = null;
   PreparedStatement  stmt = null;
   /**
   *Create a databaseconnector instance, complete the connection of the database, and initialize the connection.
   */
   public DatabaseConnetor() {
		super();
		try{
           // jdbc driver
           Class.forName(JDBC_DRIVER);
       
           // connect  to database
           System.out.println("connect  to database...");
           this.conn = DriverManager.getConnection(DB_URL,USER,PASS);
       
           // execute query
           System.out.println(" initialization...");
          
           }catch (Exception e) {
				// TODO: handle exception
			}
		 
	}
   /**
    *Returns the query results of all players and stores them in the list collection
    */
	public  List<User> getPlayers() throws SQLException{
		
		List<User> users = new ArrayList<User>();
		
	    String sql = "SELECT * FROM player";
	     stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
    
        // iteration
        while(rs.next()){
          
            String userName = rs.getString("userName");
            String passWord = rs.getString("passWord");
            String lastUpdatetime = rs.getString("time");
         
            int bestScore = rs.getInt("bestScore");
           
            

            // save data to list
            User user = new User(userName, passWord, bestScore,lastUpdatetime);
            users.add(user);
        
        }
		
		
		
		return users;
		
	}
	
	   /**
	    *Check if the player exists
	    */
	public  boolean checkPlayer(User user) throws SQLException{
		
		 
	    String sql = "SELECT * FROM player where userName =? and passWord = ?";
	     stmt = conn.prepareStatement(sql);
	     stmt.setString(1, user.getUserName());
		stmt.setString(2, user.getPassWord());
        ResultSet rs = stmt.executeQuery();
     
        while(rs.next()){
          
            if(!rs.getString("userName").equals(null)) {
           	 return true;
            }; }
		return false;
		
	}

	

	 /**
	    *Add a new palyer
	    */
	public void insertUser(User user) throws SQLException {
		String sql = "insert into player(userName,passWord,bestScore) values (?,?,?)";
		  stmt = conn.prepareStatement(sql);
		stmt.setString(1, user.getUserName());
		stmt.setString(2, user.getPassWord());
		stmt.setInt(3, 0);
 
		stmt.executeUpdate();
		System.out.println("insert player successfully");
		
	}
	 /**
	    *Delete a new palyer
	    */
	public void deletePlayer(String userName,String passWord) throws SQLException {
		
		 String sql = "delete from player where userName=? and passWord=?";
		 stmt = conn.prepareStatement(sql);
		 stmt.setString(1, userName);
		 stmt.setString(2, passWord);

		 stmt.execute();
		
	}
	/**
	    *Update a new palyer
	    */
	public void updatePlayer(String userName, String passWord, User user) throws SQLException {
		 String sql="update player set userName=? ,passWord=?  where userName=?  and passWord=?";
		  stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassWord());
			stmt.setString(3, userName);
			stmt.setString(4, passWord);
	 
	 

		 stmt.execute();
		 
	}
	
	/**
	    *Update a new palyer
	    */
	public void updateScore(String userName,int score) throws SQLException {
		 SimpleDateFormat sdf = new SimpleDateFormat();
	        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
	        Date date = new Date();
		 String sql="update player set bestScore=? , time=? where userName=?";
		  stmt = conn.prepareStatement(sql);
			stmt.setInt(1,score);
			stmt.setString(2, sdf.format(date));
			stmt.setString(3, userName);
 
	 

		 stmt.execute();
		 
	}
	 
		

}

