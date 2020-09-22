package interfaceui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import database.DatabaseConnetor;
import database.User;
import gamelogic.GameState;

public class DatabaseUI extends JFrame{
    JLabel jLabel;
    private GameState gameState;
    DatabaseConnetor dc = new DatabaseConnetor();
	public DatabaseUI(GameState gameState) {
		
	      this.setSize(500, 400);
	      this.setLocation(960,100);
	      this.getContentPane().setLayout(null);
	      this.setLayout(new BorderLayout());
	  
	      this.setTitle("DataBase Management");
	      this.gameState = gameState;
	      this.setAlwaysOnTop(!this.isAlwaysOnTop()); 

	}
 
    public void loadData() throws SQLException {
    	
    	if(gameState.getLoginState()) {
  	      String[] columnNames = new String[] { "id", "Name","Score","Time" };
  	      List<User> players =  dc.getPlayers();
  	    String[][] data = new String[players.size()][columnNames.length];
  	    
  	    for (int i = 0; i < players.size(); i++) {
  	    	data[i] = new String[] {i+1+"",players.get(i).getUserName(),players.get(i).getBestScore()+"",players.get(i).getLastUpdatetime()};
			
		}
  	     
	       
	        String[][] heros = new String[][] { { "1", "Tom","75" },
	                { "2", "Jerry","86" }, { "3", "Lucy","92"}};
	        JTable t = new JTable(data, columnNames);
	      
	        JScrollPane sp = new JScrollPane(t);
 
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        this.add(sp, null);
	        this.resize(501, 400);
	        
    		
    		
    		
    	}else {
 
    		JOptionPane.showMessageDialog(null, "please login first!");
    	}
    	
    }
}
