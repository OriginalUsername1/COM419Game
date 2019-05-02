import java.awt.Canvas;
import java.awt.Dimension;
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


public class Menu implements Runnable, ImageObserver{
   
	final int WIDTH = 1280;
	final int HEIGHT = 720;
	
	private String screen = "main";
	private BufferedImage main;
	//private BufferedImage settings;
	private BufferedImage howTo [];
	private int howToNum = 0;
	private BufferedImage credits;
	private PlaySound menuSong;
   
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
   
	
	public Menu() 
	{
		
		howTo = new BufferedImage [3];
		
		try {
			main = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "main" + ".png"));
			//settings = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "settings" + ".png"));
			for (howToNum=0;howToNum<3;howToNum++)
			{
				howTo[howToNum] = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "HTP" + howToNum + ".png"));
			}
			credits = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "credits" + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		menuSong = new PlaySound ("menu");
		menuSong.play();
		
		frame = new JFrame("Game Launcher");
      
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);
      
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);
      
		panel.add(canvas);
      
		canvas.addMouseListener(new MouseControl());
      
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
      
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
      
		canvas.requestFocus();
	}
   
        
private class MouseControl extends MouseAdapter{ 
	
	
	public void mouseClicked(MouseEvent e) 
	{
		
		int x = e.getX();
		int y = e.getY();
				
		if (screen == "main")
		{
			if (x>(int)(WIDTH*0.350) && x<(int)(WIDTH*0.604) && y>(int)(HEIGHT*0.444) && y<(int)(HEIGHT*0.586))
			{
				menuSong.stop();
				Game game = new Game ();
				new Thread(game).start();
				
			}
			else if (x>(int)(WIDTH*0.173) && x<(int)(WIDTH*0.427) && y>(int)(HEIGHT*0.669) && y<(int)(HEIGHT*0.803))
			{
				howToNum = 0;
				screen = "howTo";
			}
			/*else if (x>(int)(WIDTH*0.228) && x<(int)(WIDTH*0.378) && y>(int)(HEIGHT*0.748) && y<(int)(HEIGHT*0.828))
			{
				screen = "settings";
			}*/
			else if (x>(int)(WIDTH*0.524) && x<(int)(WIDTH*0.780) && y>(int)(HEIGHT*0.667) && y<(int)(HEIGHT*0.807))
			{
				screen = "credits";
			}
		}
		else if (screen == "howTo")
		{
			howToNum++;
			
			if (howToNum == 3)
			{
				screen = "main";
			}
		}
		else
		{
			screen = "main";
		}
		
	}
	
	
}
   
	long desiredFPS = 60;
	long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
    
	boolean running = true;
   
	public void run(){
      
		long beginLoopTime;
		long endLoopTime;
      	long currentUpdateTime = System.nanoTime();
      	long lastUpdateTime;
      	long deltaLoop;
      
      	while(running){
      		beginLoopTime = System.nanoTime();
      		
      		render ();
         
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
	}
   
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();
	}
   
   
	protected void update(int deltaTime)
	{
		
		
		
	}
   
   
	protected void render(Graphics2D g)
	{
		
		if (screen == "main")
		{
			g.drawImage(main, 0, 0, WIDTH, HEIGHT, null);
		}
		else if (screen == "settings")
		{
			//g.drawImage(settings, 0, 0, WIDTH, HEIGHT, null);
		}
		else if (screen == "howTo")
		{
			g.drawImage(howTo[howToNum], 0, 0, WIDTH, HEIGHT, null);
		}
		else if (screen == "credits")
		{
			g.drawImage(credits, 0, 0, WIDTH, HEIGHT, null);
		}

	}
   
   


	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		return false;
	}

}
