import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Map;
import java.util.HashMap;


public class Game implements Runnable, ImageObserver{
   
	//screen stuff
	final int WIDTH = 1600;
	final int HEIGHT = 860;
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	private PlaySound daySong;
	private PlaySound nightSong;
	private boolean gameover = false;
	
	//assets
	private BufferedImage background;
	private BufferedImage build;
	private BufferedImage gameOverImg;
	private BufferedImage pauseMenu[];
	private int buildMenuWidth = (int)(WIDTH/2);
	private int buildMenuHeight = (int)(HEIGHT/2);
	
	//time
	private double timer = 0;
	private int tick = 0;
	private int day = 1;
	private boolean pause = false;
	
	//monsters
	private Monster monsters[];
	private int monstersSpawned = 0;
	private boolean night = false;
	private Defense defenses = new Defense();
	
	//castle
	private Room rooms[];
	private int selectedRoom = -1;
	private int wood = 20;
	private int stone = 20;
	private int food = 5;
	private int worked = 20;
	private int people = 20;
	private int gold = 0;
	private int silver = 0;
	private int hp = 100;

	// Left, Top, Right, Bottom
	private double[][] selectorPositions = new double[][] {
		{0.671, 0.440, 0.791, 0.525}, 
		{0.802, 0.440, 0.921, 0.525}, 
		{0.671, 0.549, 0.791, 0.632}, 
		{0.802, 0.549, 0.921, 0.632}, 
		{0.671, 0.660, 0.791, 0.740}, 
		{0.802, 0.660, 0.921, 0.740}, 
		{0.671, 0.765, 0.791, 0.849}, 
		{0.802, 0.765, 0.921, 0.849}, 
	};

	private double[][] buildMenuSelectorPositions = new double[][] {
		{0.452, 0.406, 0.513, 0.440}, 
		{0.669, 0.422, 0.730, 0.453}, 
		{0.452, 0.496, 0.513, 0.529}, 
		{0.669, 0.521, 0.730, 0.551}, 
		{0.452, 0.576, 0.513, 0.608}, 
		{0.669, 0.607, 0.730, 0.642}, 
		{0.454, 0.692, 0.541, 0.729}
	};
	private final String[] buildNames = new String[]{
		"throne",
		"mines",
		"barracks",
		"mill",
		"armory",
		"farm",
		"destroy"
	};
	@SuppressWarnings("serial")
	private final Map<String, BuildRequirement> buildReqs = new HashMap<String, BuildRequirement>() {
		{
			put("throne", new BuildRequirement(15, 15, 0, 0, 0));
			put("mines", new BuildRequirement(0, 0, 5, 0, 0));
			put("barracks", new BuildRequirement(10, 10, 5, 0, 0));
			put("mill", new BuildRequirement(0, 0, 10, 0, 0));
			put("armory", new BuildRequirement(20, 25, 3, 0, 0));
			put("farm", new BuildRequirement(0, 0, 5, 0, 0));
			put ("destroy", new BuildRequirement(0, 0, 0, 0, 0));
		}
	};
	
   
	
	public Game() 
	{
		//setting variables
		pauseMenu = new BufferedImage[3];
		
		try {
			background = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "bg" + ".png"));
			build = ImageIO.read(new File (System.getProperty("user.dir") + "\\assets\\" + "Build" + ".jpg"));
			gameOverImg = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "gameOverImg" + ".jpg"));
			for (int i=0;i<3;i++)
			{
				pauseMenu[i] = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "HTP" + i + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		daySong = new PlaySound ("day");
		daySong.play();
		
		rooms = new Room[8];
		monsters = new Monster[20];
		
		for (int i=0;i<8;i++)
		{
			rooms[i] = null;
		}
		for (int i=0;i<20;i++)
		{
			monsters[i] = null;
		}
		
		updatePop ();
		
		//setting frame
		frame = new JFrame("Untitled 1");
      
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);
      
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);
      
		panel.add(canvas);
      
		canvas.addMouseListener(new MouseController());
      
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
      
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
      
		canvas.requestFocus();
	}
   
        
	//reading clicks. Change in the game from clicking goes here
	public class MouseController extends MouseAdapter
	{ 
		
		
		public void mouseClicked(MouseEvent e) {
			
			int x = e.getX();
			int y = e.getY();
			
			//System.out.println(((double)x/WIDTH) + ", " + ((double)y/HEIGHT));
			
			if (gameover)
			{
				running = false;
			} 
			else if (pause)
			{
				if (selectedRoom < 2)
				{
					selectedRoom ++;
				}
				else if (selectedRoom == 2)
				{
					selectedRoom = -1;
					pause = false;
					buildMenuWidth = (int)(WIDTH/2);
					buildMenuHeight = (int)(HEIGHT/2);
				}
			}
			else if (x > (int)(0.850*WIDTH) && y < (int)(0.150*HEIGHT))
			{
				selectedRoom = 0;
				pause = true;
				buildMenuWidth = (int)(WIDTH/1.25);
				buildMenuHeight = (int)(HEIGHT/1.25);
			}
			else if (selectedRoom == -1)
			{
				for (int i = 0; i < selectorPositions.length; i++) {
					if (x > (int)(WIDTH*selectorPositions[i][0]) && x < (int)(WIDTH*selectorPositions[i][2]) && y > (int)(HEIGHT*selectorPositions[i][1]) && y < (int)(HEIGHT*selectorPositions[i][3])) {
						selectedRoom = i;
					}
				}
			}
			else
			{
				int buildOption = -1;
				for (int i = 0; i < buildMenuSelectorPositions.length; i++) {
					if (x > (int)(WIDTH*buildMenuSelectorPositions[i][0]) && x < (int)(WIDTH*buildMenuSelectorPositions[i][2]) && y > (int)(HEIGHT*buildMenuSelectorPositions[i][1]) && y < (int)(HEIGHT*buildMenuSelectorPositions[i][3])) {
						buildOption = i;
					}
				}
				if (buildOption == -1) {
					selectedRoom = -1;
				} else {
					String buildName = buildNames[buildOption];
					BuildRequirement req = buildReqs.get(buildName);
					if (req.getWood() > wood) {
						System.out.println("You don't have enough wood");
					} else if (req.getStone() > stone) {
						System.out.println("You don't have enough stone");
					} else if (req.getPeasants() > people-worked) {
						System.out.println("You don't have enough people");
					} else if (req.getSilver() > silver) {
						System.out.println("You don't have enough silver");
					} else if (req.getGold() > gold) {
						System.out.println("You don't have enough gold");
					} else if (makeRoom(buildName)) {
						wood -= req.getWood();
						stone -= req.getStone();
						worked -= req.getPeasants();
						silver -= req.getSilver();
						gold -= req.getGold();
						updatePop();
					}
				}
			}
		
		}
	}
   
	//fps and image updating
	long desiredFPS = 60;
	long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
    
	boolean running = true;
	
	
	//Runs time forward, sets the frames
	public void run()
	{
      
		long beginLoopTime;
		long endLoopTime;
      	long currentUpdateTime = System.nanoTime();
      	long lastUpdateTime;
      	long deltaLoop;
      
      	while(running){
      		beginLoopTime = System.nanoTime();
         
      		render();
         
      		lastUpdateTime = currentUpdateTime;
      		currentUpdateTime = System.nanoTime();
      		update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));
         
      		endLoopTime = System.nanoTime();
      		deltaLoop = endLoopTime - beginLoopTime;
           
      		if(deltaLoop > desiredDeltaLoop){
      			//Do nothing
      		}else{
      			try{
      				Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
      			}catch(InterruptedException e){
      				//Do nothing
      			}
      		}
      	}
      	
      	frame.dispose();
      	daySong.stop();
      	nightSong.stop();
      	
	}
   
	
	//makes the frame
	private void render() 
	{
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();
	}
   
   
	//game updates. Change in the  game based on time goes here
	protected void update (int deltaTime)
	{
		if (!pause)
		{
			timer += (deltaTime/4);
		}
		
		if (timer >= 10)
		{
			tick ++;
			
			if (tick % 1000 == 0)
			{
				if (night == false)
				{
					night = true;
					daySong.stop();
					nightSong = new PlaySound ("day");
					nightSong.play();
				}
				else
				{
					night = false;
					nightSong.stop();
					daySong = new PlaySound ("day");
					daySong.play();
					day++;
					monstersSpawned = 0;
					
					updateRoom ("barracks");
					if (people > food)
					{
						people = (people-food)/2;
						food = 0;
					}
					else
					{
						food -= people;
						people += food/6;
					}
					
					updatePop();
				}
				
			}
			
			if (tick % 50 == 0)
			{
				updateRoom ("throne");
				defend ();
			}
			
			if (tick % 100 == 0)
			{
				updateRoom ("farm");
				updateRoom ("mines");
				updateRoom ("mill");
			}
			
			updateMonsters();
			
			if (night)
			{
				switch (day) {
				
				case 1:
					if (tick % 100 == 0 && monstersSpawned < 3)
					{
						makeMonster ("goblin");
					}
					break;
				case 2:
					if (tick % 100 == 0 && monstersSpawned < 5)
					{
						makeMonster ("goblin");
					}
					break;
				case 3:
					if (tick % 125 == 0)
					{
						makeMonster ("goblin");
					}
					break;
				case 4:
					if (tick % 100 == 0)
					{
						makeMonster ("goblin");
					}
					break;
				case 5:
					if (tick % 50 == 0 && monstersSpawned < 9)
					{
						makeMonster ("goblin");
					}
					else if (tick % 100 == 0 && monstersSpawned < 10)
					{
						makeMonster ("chimeraGooseMan");
					}
					break;
				}
			}
			
			timer -= 10;
		}
	}
   
   
	//draws images to the screen. Any changes that affect the screen go here.
	protected void render(Graphics2D g){
		if (hp <= 0) {
			g.drawImage(gameOverImg, 0, 0, WIDTH, HEIGHT, null);
			return;
		}
		
		g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
		
		g.setColor(Color.RED);
		for (int i=0;i<20;i++)
		{
			if (monsters[i] != null)
			{
				g.drawImage(monsters[i].getSprite(), (int)(WIDTH*monsters[i].getX()), (int)(HEIGHT*0.699), (int)(WIDTH*0.033), (int)(HEIGHT*0.059), null);
				g.fillRect((int)(WIDTH*(monsters[i].getX()-0.01)), (int)(HEIGHT*0.680), (int)((WIDTH*0.005)*monsters[i].getHP()), (int)(HEIGHT*0.010));
			}
		}
		
		g.drawImage(defenses.getFrame(), (int)(WIDTH*0.365), (int)(HEIGHT*0.660), (int)(WIDTH*0.066), (int)(HEIGHT*0.118), null);
		
		if (defenses.getKNum() >= 1)
		{
			g.fillRect((int)(WIDTH*0.350), (int)(HEIGHT*0.530), (int)((WIDTH*0.005)*defenses.getHP()), (int)(HEIGHT*0.010));
		}
		
		g.fillRect((int)(WIDTH*0.104), (int)(HEIGHT*0.110), (int)((WIDTH*0.001)*hp), (int)(HEIGHT*0.010)); 
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		
		if (night)
		{
			g.drawString("Night " + day, ((int)(WIDTH*0.052)), ((int)(HEIGHT*0.100)));
		}
		else
		{
			g.drawString("Day " + day, ((int)(WIDTH*0.052)), ((int)(HEIGHT*0.100)));
		}
		
		g.drawString("Health: " + hp, (int)(WIDTH*0.104), (int)(HEIGHT*0.100));
		g.drawString("Wood: " + wood, (int)(WIDTH*0.052), (int)(HEIGHT*0.069));
		g.drawString("Stone: " + stone, (int)(WIDTH*0.104), (int)(HEIGHT*0.069));
		g.drawString("Gold: " + gold, (int)(WIDTH*0.156), (int)(HEIGHT*0.069));
		g.drawString("Food: " + food, (int)(WIDTH*0.208), (int)(HEIGHT*0.069));
		g.drawString("Knights: " + defenses.getKNum(), (int)(WIDTH*0.260), (int)(HEIGHT*0.069));
		g.drawString("Archers: " + defenses.getANum(), (int)(WIDTH*0.312), (int)(HEIGHT*0.069));
		g.drawString("Peasants: " + (people-worked) + "/" + people, (int)(WIDTH*0.364), (int)(HEIGHT*0.069));
		g.drawString("Help", (int)(WIDTH*0.900), (int)(HEIGHT*0.100));
		
		if (selectedRoom != -1)
		{
			if (pause)
			{
				g.drawImage(pauseMenu[selectedRoom], ((WIDTH - buildMenuWidth)/2), ((HEIGHT- buildMenuHeight)/2), buildMenuWidth, buildMenuHeight, null);
			}
			else
			{
				g.drawImage(build, ((WIDTH - buildMenuWidth)/2), ((HEIGHT- buildMenuHeight)/2), buildMenuWidth, buildMenuHeight, null);
			}
		}
	}
	
	
	//creates a monster
	private void makeMonster (String name)
	{
		int i=0;
		
		for (i=0;i<20;i++)
		{
			if (monsters[i] == null)
			{
				monsters[i] = new Monster(name);
				monstersSpawned++;
				return;
			}
		}
		
		monsters[0] = null;
		monsters[0] = new Monster (name);
		monstersSpawned++;
	}
   
	
	//updates a room, adding necessary materials to the player inv
	private void updateRoom (String roomType)
	{
		int i=0;
		
		for (i=0;i<8;i++)
		{
			if (rooms[i] != null)
			{
				if (rooms[i].getName() == roomType)
				{
					wood += rooms[i].getWood();
					stone += rooms[i].getStone();
					gold += rooms[i].getGold();
					people += rooms[i].getPeople();
					food += rooms[i].getFood();
				}
			}
		}
		
		return;
	}
	
	
	//lets the defenses hit  the monsters
	private void defend ()
	{
		int i=0, j=0;
		boolean dead = false;
		
		if (defenses.getKNum() >= 1)
		{
			for (i=0;i<20;i++)
			{
				if (monsters[i] != null)
				{
					if (monsters[i].getX() >= 0.350)
					{
						dead = monsters[i].dealDamage(defenses.getKDam());
						
						if (dead)
						{
							monsters[i] = null;
						}
												
						break;
					}
				}
			}
		}
		
		for (i=0;i<defenses.getANum();i++)
		{
			for (j=0;j<20;j++)
			{
				if (monsters[j] != null)
				{
					dead = monsters[j].dealDamage(defenses.getADam());
					
					if (dead)
						monsters[j] = null;
					
					break;
				}
			}
		}
		
		return;
	}

	
	//lets the monsters hit the defenses (and move)
	private void updateMonsters ()
	{
		int i=0, temp=0;
		
		for (i=0;i<20;i++)
		{
			if (monsters[i] != null)
			{
				temp = monsters[i].update();
				
				if (temp != 0)
				{
					if (defenses.getKNum() > 0)
					{
						defenses.attack(temp);
						updatePop ();
					}
					else
					{
						hp -= temp;
						if (hp <= 0)
						{
							gameover = true;
						}
					}
				}
			}
		}
	}
	
	
	//builds a room
	private boolean makeRoom (String roomType)
	{
		if (roomType == "destroy")
		{
			return destroyRoom();
		}
		if (selectedRoom != -1)
		{
			if (rooms[selectedRoom] == null)
			{
				rooms[selectedRoom] = new Room (roomType);
				
				if (roomType == "armory")
				{
					defenses.recruit("knight");
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		
		
		return false;
	}
	
	
	private boolean destroyRoom ()
	{
		if (selectedRoom != -1)
		{
			if (rooms[selectedRoom] == null)
			{
				return false;
			}
			else
			{
				rooms[selectedRoom] = null;
				
				return true;
			}
		}
		
		return false;
	}
	
	
	//updates the working population
	private void updatePop ()
	{
		int i=0, temp=0;
		
		worked = 0;
		
		for (i=0;i<defenses.getANum();i++)
		{
			if (worked < people)
				worked++;
			else
			{
				defenses.reduce("archer");
			}
		}
		
		for (i=0;i<defenses.getKNum();i++)
		{
			if (worked < people)
				worked++;
			else
			{
				defenses.reduce("knight");
			}
		}
		
		for (i=0;i<8;i++)
		{
			if (rooms[i] != null)
			{
				temp = rooms[i].getWorkers();
				
				if (worked + temp <= people)
				{
					rooms[i].isWorked(true);
					worked += temp;
				}
				else
				{
					rooms[i].isWorked(false);
				}
			}
		}
		
		return;
	}
	
	
	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		return false;
	}

}
