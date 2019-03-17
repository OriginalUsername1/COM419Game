import java.awt.Canvas;
import java.awt.Color;
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
   
	final int WIDTH = 800;
	final int HEIGHT = 500;
	
	private int difficulty = 0;
	private String screen = "main";
	private BufferedImage main;
	private BufferedImage settings;
	private BufferedImage howTo;
	private BufferedImage credits;
   
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
   
	
	public Menu() 
	{
		
		try {
			main = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "main" + ".png"));
			//settings = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "settings" + ".png"));
			howTo = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "howTo" + ".png"));
			credits = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + "credits" + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
			if (x>127 && x<360 && y>228 && y<319)
			{
				Game game = new Game ();
				new Thread(game).start();
			}
			else if (x>444 && x<677 && y>227 && y<319)
			{
				screen = "howTo";
			}
			else if (x>182 && x<302 && y> 374 && y<414)
			{
				screen = "settings";
			}
			else if (x>502 && x<623 && y>373 && y< 415)
			{
				screen = "credits";
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
         
      		render();
         
      		lastUpdateTime = currentUpdateTime;
      		currentUpdateTime = System.nanoTime();
      		update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));
         
      		endLoopTime = System.nanoTime();
      		deltaLoop = endLoopTime - beginLoopTime;
           
      		if(deltaLoop > desiredDeltaLoop){
      			//Do nothing. We are already late.
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
			g.drawImage(howTo, 0, 0, WIDTH, HEIGHT, null);
		}
		else if (screen == "credits")
		{
			g.drawImage(credits, 0, 0, WIDTH, HEIGHT, null);
		}

	}
   
   


	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

}