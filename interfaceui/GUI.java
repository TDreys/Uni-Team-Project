package interfaceui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaceui.elements.*;
import interfaceui.elements.Button;
import interfaceui.elements.Label;
import interfaceui.elements.TextField;
import networking.Client;
import networking.Server;
import program.Main;
import rendering.util.fontUtil.Font;
import audio.*;
import static audio.Source.bgcount;
import static program.Main.State.*;

public class GUI {

    //different scenes for the ui
    private ArrayList<Node> mainMenu = new ArrayList<>();
    private ArrayList<Node> lobbyMenu = new ArrayList<>();
    private ArrayList<Node> pauseMenu = new ArrayList<>();
    private ArrayList<Node> joiningMenu = new ArrayList<>();
    private ArrayList<Node> optionsMenu = new ArrayList<>();
    private ArrayList<Node> joinGameMenu = new ArrayList<>();
    private ArrayList<Node> optionsGameMenu = new ArrayList<>();

    //dynamic elements here
    private RowTable lobbyPlayersRowTable = null;
    private Label lobbyIPLabel = null;
    private TextField IPTextField = null;
    private TextField nameTextField = null;
    private Button pvpButton = null;
    private Button pveButton = null;
    private String selectedGameMode = null;

    private Node focused = null;

    private static int buttonWidth = 220;
	private static int buttonHeight = 65;

    public static Font font = new Font("src/res/segoeui.ttf", 50);
    public static Font largeFont = new Font("src/res/segoeui.ttf", 155);

    public GUI()
    {
        init();
    }

    /**
     * creates the gui elements and adds them to the corresponding arraylist
     */
	public void init() {

        Color buttonColor = new Color(140,90,255);

        //creating the main menu scene elements
        Label title = new Label("LAST OF US",460,135);
        title.setFont(largeFont);
        mainMenu.add(title);
        
        Button startButton = new Button(buttonWidth, buttonHeight, 500, 220,new Action() {
            @Override
            public void action() {
                Main.state = JOINING_MENU;
                Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        startButton.setText("START");
        startButton.setColor(buttonColor);
        startButton.setFont(font);
        mainMenu.add(startButton);
        
        Button optionsButton = new Button(buttonWidth, buttonHeight, 500, 300, new Action() {
            @Override
            public void action() {
            	Main.state = OPTIONS;
            	Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        optionsButton.setText("OPTIONS");
        optionsButton.setColor(buttonColor);
        optionsButton.setFont(font);
        mainMenu.add(optionsButton);
        
        Button databaseButton = new Button(buttonWidth, buttonHeight, 500, 460, new Action() {
            @Override
            public void action() {
         Main.databaseUI.setVisible(true);
         try {
			Main.databaseUI.loadData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            }
        });
        databaseButton.setText("DATABASE");
        databaseButton.setColor(buttonColor);
        databaseButton.setFont(font);
        mainMenu.add(databaseButton);
        
        Button loginButton = new Button(100, 50, 50, 650, new Action() {
            @Override
            public void action() {
         Main.loginUI.setVisible(true);
            }
        });
        loginButton.setText("Login");
        loginButton.setColor(buttonColor);
        loginButton.setFont(font);
        mainMenu.add(loginButton);
        
        Button exitButton = new Button(buttonWidth, buttonHeight, 500, 380, new Action() {
            @Override
            public void action() {
                if(Main.client.getClientInfo().isHost())
                {
                    Main.server.close();
                }
                Main.client.close();
                System.exit(1);
            }
        });
        exitButton.setText("EXIT");
        exitButton.setColor(buttonColor);
        exitButton.setFont(font);
        mainMenu.add(exitButton);

        //creating the options menu scene elements
        Label optionsTitle = new Label("OPTIONS",490,135);
        optionsTitle.setFont(largeFont);
        optionsMenu.add(optionsTitle);
        
        Button onButton = new Button(buttonWidth/4, buttonHeight, 605, 220,new Action() {
            @Override
            public void action() {
            	//switch sound on
            	if(!Source.on) {
	            	Buffer.init();
	            	try
					{
						Buffer.loadALData();
					}
					catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                Source.init();           
	                Source.clickSou.play(Buffer.clickButtonBuffer);
	                if(bgcount<1) {
	                	bgcount=1;
	                	Source.bgSou.play(Buffer.bgBuffer);
	                }
	                Source.on=true;
	            } else {
	            	Source.clickSou.play(Buffer.clickButtonBuffer);
	            }
            }
        });
        onButton.setText("ON");
        onButton.setColor(buttonColor);
        onButton.setFont(font);
        optionsMenu.add(onButton);

        Label soundOptionLabel = new Label("Sound: ",520,240);
        soundOptionLabel.setFont(font);
        optionsMenu.add(soundOptionLabel);
        
        Button offButton = new Button(buttonWidth/4, buttonHeight, 665, 220,new Action() {
            @Override
            public void action() {
            	//switch sound off
            	if (Source.on) {
	            	bgcount=0;
	            	Source.bgSou.stop();
	            	Source.destroy();
	            	Buffer.cleanup();
	            	Source.on=false;
            	}       	
            }
        });
        offButton.setText("OFF");
        offButton.setColor(buttonColor);
        offButton.setFont(font);
        optionsMenu.add(offButton);
        
        Button optionMenuBackButton = new Button(buttonWidth, buttonHeight, 500, 300,new Action() {
            @Override
            public void action() {
            	Main.state = MAIN_MENU;
            	// add condition to go back to paused menu if clicked options clicked on when paused
            	Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        optionMenuBackButton.setText("BACK");
        optionMenuBackButton.setColor(buttonColor);
        optionMenuBackButton.setFont(font);
        optionsMenu.add(optionMenuBackButton);
        
        
        //creating the joining menu scene elements
        joiningMenu.add(title);
        Button hostButton = new Button(buttonWidth, buttonHeight, 500, 220,new Action() {
            @Override
            public void action() {
                Main.server = new Server();
                Main.server.start();

                try {
                    Main.client = new Client("Host", InetAddress.getLocalHost(),true);
                    int id = Main.client.connectToServer(InetAddress.getLocalHost(),55555);
                    Main.inputHandler.state.playerID = id;
                    Main.client.getClientInfo().setClientID(id);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Main.state = LOBBY;
                Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        hostButton.setText("HOST GAME");
        hostButton.setColor(buttonColor);
        hostButton.setFont(font);
        joiningMenu.add(hostButton);
        
        Button joinButton = new Button(buttonWidth, buttonHeight, 500, 300,new Action() {
            @Override
            public void action() {
                Main.state = JOIN_GAME;
                Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        joinButton.setText("JOIN GAME");
        joinButton.setColor(buttonColor);
        joinButton.setFont(font);
        joiningMenu.add(joinButton);
        
        Button joiningMenuBackButton = new Button(buttonWidth, buttonHeight, 500, 380,new Action() {
            @Override
            public void action() {
                Main.state = MAIN_MENU;
                Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        joiningMenuBackButton.setText("BACK");
        joiningMenuBackButton.setColor(buttonColor);
        joiningMenuBackButton.setFont(font);
        joiningMenu.add(joiningMenuBackButton);

        //creating the lobby menu
    
        Label titleLobby = new Label("LOBBY",510,95);
        titleLobby.setFont(largeFont);
        lobbyMenu.add(titleLobby);

        pvpButton = new Button(90, 40, 480, 185, new Action() {
            @Override
            public void action() {
                selectedGameMode = "PVP";
            }
        });
        pvpButton.setText("PVP");
        pvpButton.setFont(font);
        pvpButton.setColor(buttonColor);
        lobbyMenu.add(pvpButton);
        pvpButton.setSelected(true);

        pveButton = new Button(90, 40, 625, 185, new Action() {
            @Override
            public void action() {
                selectedGameMode = "TPVE";
            }
        });
        pveButton.setText("TPVE");
        pveButton.setFont(font);
        pveButton.setColor(buttonColor);
        lobbyMenu.add(pveButton);

        lobbyIPLabel = new Label("",510,150);
        lobbyIPLabel.setFont(font);
        lobbyMenu.add(lobbyIPLabel);
        
        //example rowTable, rows are added when update method is called, this is because content of table is dependent on gamestate
        RowTable rowTable = new RowTable(750,400,250,250,8);
        lobbyPlayersRowTable = rowTable;
        lobbyMenu.add(rowTable);
        
        // add back button
        Button lobbyMenuBackButton = new Button(buttonWidth, buttonHeight, 250, 480,new Action() {
            @Override
            public void action() {
                if(Main.client.getClientInfo().isHost())
                {
                    Main.server.close();
                }
                Main.client.close();
                Main.state = JOINING_MENU;
                Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        lobbyMenuBackButton.setText("BACK");
        lobbyMenuBackButton.setColor(buttonColor);
        lobbyMenuBackButton.setFont(font);
        lobbyMenu.add(lobbyMenuBackButton);
        
        // add play button
        Button lobbyMenuPlayButton = new Button(buttonWidth, buttonHeight, 780, 480,new Action() {
            @Override
            public void action() {
                if(Main.client.getClientInfo().isHost())
                {
                    Main.client.sendMessage("START_GAME:"+selectedGameMode);
                    try {
                        Main.server.serverSocket.close();
                        Source.clickSou.play(Buffer.clickButtonBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        lobbyMenuPlayButton.setText("START GAME");
        lobbyMenuPlayButton.setColor(buttonColor);
        lobbyMenuPlayButton.setFont(font);
        lobbyMenu.add(lobbyMenuPlayButton);

        //creating the Join Game menu
        Label titleJoin = new Label("JOIN GAME",470,140);
        titleJoin.setFont(largeFont);
        joinGameMenu.add(titleJoin);

        final Label enterIP = new Label("Enter IP Address:",550,210);
        enterIP.setFont(font);
        joinGameMenu.add(enterIP);
        
        //create text box to enter IP
        IPTextField = new TextField(340,250);
        IPTextField.setFont(largeFont);
        IPTextField.setWidth(600);
        IPTextField.setHeight(90);
        joinGameMenu.add(IPTextField);

        final Label enterName = new Label("Enter Name:",570,360);
        enterName.setFont(font);
        joinGameMenu.add(enterName);

        nameTextField = new TextField(340,400);
        nameTextField.setFont(largeFont);
        nameTextField.setWidth(600);
        nameTextField.setHeight(90);
        joinGameMenu.add(nameTextField);
        
        Button joiningButton = new Button(buttonWidth, buttonHeight, 720, 505,new Action() {
            @Override
            public void action() {
                try {
                    if (IPTextField.getContent().equals(""))
                    {
                        throw new UnknownHostException();
                    }
                    InetAddress address = InetAddress.getByName(IPTextField.getContent());
                    Main.client = new Client(nameTextField.getContent(),InetAddress.getLocalHost(),false);
                    int id = Main.client.connectToServer(address,55555);
                    Main.inputHandler.state.playerID = id;
                    Main.client.getClientInfo().setClientID(id);
                    Main.state = LOBBY;
                    Source.clickSou.play(Buffer.clickButtonBuffer);
                } catch (UnknownHostException e) {
                    enterIP.setText("Invalid IP, please try again");
                } catch (IOException e)
                {
                    enterIP.setText("Invalid IP, please try again");
                }
            }
        });
        joiningButton.setText("JOIN GAME");
        joiningButton.setColor(buttonColor);
        joiningButton.setFont(font);
        joinGameMenu.add(joiningButton);
        
     // add back button
        Button joinGameBackButton = new Button(buttonWidth, buttonHeight, 340, 505,new Action() {
            @Override
            public void action() {
                Main.state = JOINING_MENU;
                Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        joinGameBackButton.setText("BACK");
        joinGameBackButton.setColor(buttonColor);
        joinGameBackButton.setFont(font);
        joinGameMenu.add(joinGameBackButton);

        //creating the Pause options game menu
        Label optionsGameTitle = new Label("OPTIONS",490,135);
        optionsGameTitle.setFont(largeFont);
        optionsGameMenu.add(optionsGameTitle);
        
        onButton = new Button(buttonWidth/4, buttonHeight, 605, 220,new Action() {
            @Override
            public void action() {
            	//switch sound on
            	if(!Source.on) {
	            	Buffer.init();
	            	try
					{
						Buffer.loadALData();
					}
					catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                Source.init();           
	                Source.clickSou.play(Buffer.clickButtonBuffer);
	                if(bgcount<1) {
	                	bgcount=1;
	                	Source.bgSou.play(Buffer.bgBuffer);
	                }
	                Source.on=true;
	            } else {
	            	Source.clickSou.play(Buffer.clickButtonBuffer);
	            }
            }
        });
        onButton.setText("ON");
        onButton.setColor(buttonColor);
        onButton.setFont(font);
        optionsGameMenu.add(onButton);

        optionsGameMenu.add(soundOptionLabel);
        
        offButton = new Button(buttonWidth/4, buttonHeight, 665, 220,new Action() {
            @Override
            public void action() {
            	//switch sound off
            	if (Source.on) {
	            	bgcount=0;
	            	Source.bgSou.stop();
	            	Source.destroy();
	            	Buffer.cleanup();
	            	Source.on=false;
            	}
            }
        });
        offButton.setText("OFF");
        offButton.setColor(buttonColor);
        offButton.setFont(font);
        optionsGameMenu.add(offButton);
        
        Button optionGameMenuBackButton = new Button(buttonWidth, buttonHeight, 500, 300,new Action() {
            @Override
            public void action() {
            	Main.state = PAUSED;
            	Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        optionGameMenuBackButton.setText("BACK");
        optionGameMenuBackButton.setColor(buttonColor);
        optionGameMenuBackButton.setFont(font);
        optionsGameMenu.add(optionGameMenuBackButton);
        
        //creating the pause menu scene elements
        
        Label titlePause = new Label("PAUSED",510,135);
        titlePause.setFont(largeFont);
        pauseMenu.add(titlePause);
        
        Button resumeButton = new Button(buttonWidth, buttonHeight, 500, 220,new Action() {
            @Override
            public void action() {
            	//pressed paused button
                Main.state = PLAYING;
                Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        resumeButton.setText("RESUME");
        resumeButton.setColor(buttonColor);
        resumeButton.setFont(font);
        pauseMenu.add(resumeButton);
        
        optionsButton = new Button(buttonWidth, buttonHeight, 500, 300, new Action() {
            @Override
            public void action() {
            	//pressed Options button
				Main.state = OPTIONS_GAME;
				Source.clickSou.play(Buffer.clickButtonBuffer);
            }
        });
        optionsButton.setText("OPTIONS");
        optionsButton.setColor(buttonColor);
        optionsButton.setFont(font);
        pauseMenu.add(optionsButton);
        
        exitButton = new Button(buttonWidth, buttonHeight, 500, 380, new Action() {
            @Override
            public void action() {
            	//pressed Exit button
				System.exit(1); //Exit or should we go back to main menu?
            }
        });
        exitButton.setText("EXIT");
        exitButton.setColor(buttonColor);
        exitButton.setFont(font);
        pauseMenu.add(exitButton);
	}

    /**
     * this method is called whenever the mouse button is clicked
     * it is called with glfw callbacks
     * @param mousex x position of the mouse in pixels
     * @param mousey y position of the mouse in pixels
     */
	public void handleMouseClickInput(double mousex, double mousey)
    {
        switch (Main.state)
        {
            case MAIN_MENU: calculateClickTarget(mainMenu,mousex,mousey); break;
            case PAUSED: calculateClickTarget(pauseMenu,mousex,mousey); break;
            case JOINING_MENU: calculateClickTarget(joiningMenu,mousex,mousey); break;
            case OPTIONS: calculateClickTarget(optionsMenu,mousex,mousey); break;
            case LOBBY: calculateClickTarget(lobbyMenu,mousex,mousey); break;
            case JOIN_GAME: calculateClickTarget(joinGameMenu,mousex,mousey); break;
            case OPTIONS_GAME: calculateClickTarget(optionsGameMenu,mousex,mousey); break;
        }
    }

    /**
     * determines what element is clicked on a given scene
     * @param scene the scene that contains the wanted node, usually the visible scene
     * @param mousex the x position of the mouse in pixels
     * @param mousey the y position of the mouse in pixels
     */
    private void calculateClickTarget(ArrayList<Node> scene, double mousex, double mousey)
    {
        for (Node node:scene) {
            if (node instanceof Button) {
                if (mousex >= node.getX() && mousex <= (node.getX()+ ((Button) node).getWidth())) {
                    if(mousey >= node.getY() && mousey <= (node.getY() + ((Button) node).getHeight())) {
                        ((Button) node).onClick.action();
                        if(node == pvpButton)
                        {
                            pvpButton.setSelected(true);
                            pveButton.setSelected(false);
                        }
                        else if(node == pveButton)
                        {
                            pvpButton.setSelected(false);
                            pveButton.setSelected(true);
                        }

                    }
                }
            }
            else if(node instanceof TextField)
            {
                if (mousex >= node.getX() && mousex <= (node.getX()+ ((TextField) node).getWidth())) {
                    if(mousey >= node.getY() && mousey <= (node.getY() + ((TextField) node).getHeight())) {
                        focused = node;
                        if(node == IPTextField)
                        {
                            IPTextField.setFocused(true);
                            nameTextField.setFocused(false);
                        }
                        else
                        {
                            IPTextField.setFocused(false);
                            nameTextField.setFocused(true);
                        }
                    }
                }
            }
        }
    }

    public void handleTextInput(int code)
    {
        if(focused instanceof TextField)
        {
            TextField focusedField = (TextField) focused;
            if(code == -1)
            {
                if (focusedField.getContent().length() == 0)
                {
                    System.out.println("no characters left");
                }
                else
                {
                    focusedField.setContent(focusedField.getContent().substring(0,focusedField.getContent().length()-1));
                }
            }
            else
            {
                focusedField.setContent(focusedField.getContent()+(char) code);
            }
        }
    }

    public ArrayList<Node> getMainMenu() {
        return mainMenu;
    }

    public ArrayList<Node> getLobbyMenu() {
        return lobbyMenu;
    }

    public ArrayList<Node> getPauseMenu() {
        return pauseMenu;
    }

    public ArrayList<Node> getJoiningMenu() {
        return joiningMenu;
    }
    
    public ArrayList<Node> getOptionsMenu() {
        return optionsMenu;
    }
    
    public ArrayList<Node> getJoinGameMenu() {
        return joinGameMenu;
    }
    
    public ArrayList<Node> getOptionsGameMenu() {
        return optionsGameMenu;
    }
    
    public RowTable getLobbyPlayersRowTable() {
        return lobbyPlayersRowTable;
    }

    public Label getLobbyIPLabel() {
        return lobbyIPLabel;
    }
}
