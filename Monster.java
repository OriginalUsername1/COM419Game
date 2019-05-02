import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster {
	private String name;
	private String status;
	private int damage;
	private int health;
	private double xVal;
	private double speed;
	private int temp;
	private BufferedImage sprite;

// A constructor
	public Monster(String n, int d, int h, double s) {
		name = n;
		damage = d;
		health = h;
		status = "move";
		speed = s;
		xVal = -0.052;
                   
		try {
			sprite = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
                   
	}
	
	
	public Monster(String n)    {
		name = n;
		damage = 5;   //default
		health = 10;
		status = "move";
		speed = 0.003;
		xVal = -0.052;
                    
		try {
			sprite = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\" + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int update() {
		
		if (status == "move")
		{
			xVal  += speed;
			if (xVal >= 0.350)
			{
				status = "attack";
			}
			
			return 0;
		}
		else if (status == "attack")
		{
			status = "cd";
			temp = 0;
			return damage;
		}
		else if (status == "cd")
		{
			temp++;
			if (temp == speed * 10000)
			{
				status = "attack";
			}
			
			return 0;
		}
		
		return 0;
	}
	
          
	public String getName() {return name;}
	
	
	public BufferedImage getSprite() {return sprite;}
	
	
	public double getX() {return xVal;}
	
	
	public int getHP() 
	{
		return health;
	}
        
	
	public boolean dealDamage (int dam)
	{
		health -= dam;
        	  
		if (health < 1)
			return true;
		else
			return false;
	}
	

}
