package interfaceui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import database.DatabaseConnetor;
import database.User;
import gamelogic.GameState;

public class LoginUI extends JFrame{

  
   private Container c;
   private JLabel a1 = new JLabel("User Name:");
   private JTextField username = new JTextField();
   private JLabel a2 = new JLabel("PassWord");
   private JPasswordField password = new JPasswordField();
   private JButton okbtn = new JButton("OK");
   private JButton cancelbtn = new JButton("CANCEL");
   private GameState gameState;
	public LoginUI(GameState gameState) {
		c=this.getContentPane();
	      this.setSize(300, 220);
	      this.setLocation(960,100);
	      this.getContentPane().setLayout(null);
	      this.setLayout(new BorderLayout());
	      this.gameState = gameState;
	      
	 
	      
	      /*title*/
	     JPanel titlePanel = new JPanel();
	     titlePanel.setLayout(new FlowLayout());
	     titlePanel.add(new JLabel("Tank War Login "));
	      c.add(titlePanel, "North");
	     
	      /*input*/
	       JPanel fieldPanel = new JPanel();
	       fieldPanel.setLayout(null);
	       a1.setBounds(50, 20, 50, 20);
	       a2.setBounds(50, 60, 50, 20);
	       fieldPanel.add(a1);
	       fieldPanel.add(a2);
	      username.setBounds(110, 20, 120, 20);
	        password.setBounds(110, 60, 120, 20);
	       fieldPanel.add(username);
	       fieldPanel.add(password);
	      c.add(fieldPanel, "Center");
	      
	        /*button*/
	       JPanel buttonPanel = new JPanel();
	        buttonPanel.setLayout(new FlowLayout());
	       buttonPanel.add(okbtn);
	        buttonPanel.add(cancelbtn);
	       c.add(buttonPanel, "South");
 
	      this.setTitle("Login");
	      
	      this.setAlwaysOnTop(!this.isAlwaysOnTop());
	      
	      okbtn.addActionListener(new ActionListener(){		//login 
	  		
	  		public void actionPerformed(ActionEvent arg0) {
	  		    DatabaseConnetor dc = new DatabaseConnetor();
	  			User user = new User(new String(username.getText()).trim(),new String(password.getPassword()).trim(),0,"");
	  			
	  			if(username.getText().trim().length()==0||new String(password.getPassword()).trim().length()==0){
	  				JOptionPane.showMessageDialog(null, "empty field!");
	  				return;
	  			}
	  			try {
					if(dc.checkPlayer(user)){
						JOptionPane.showMessageDialog(null, "login successfully!");
						setVisible(false);
						gameState.setLoginState(true);
						GameState.playerName = user.getUserName();

						
						
					}
					else{
						JOptionPane.showMessageDialog(null, "wrong information!");
					}
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  		}
	  	});
	      
	      cancelbtn.addActionListener(new ActionListener() {
	    	  
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
 
 
	}
	
 
}
