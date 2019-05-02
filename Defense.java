import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Defense {
	
	private int numArchers;
	private int archerDamage;
	private int numKnights;
	private int knightDamage;
	private int knightMaxHealth;
	private int hp;
	private BufferedImage attack[];
	private BufferedImage die[];
	private String action;
	private int frame;
	
	
	public Defense ()
	{
		numArchers = 0;
		archerDamage = 1;
		numKnights = 2;
		knightMaxHealth = 20;
		knightDamage = 5;
		hp = knightMaxHealth;
		action = "idle";
		frame = 0;
		
		attack = new BufferedImage[9];
		die = new BufferedImage[9];
		
		try {
			for (int i=0;i<9;i++)
			{
				attack[i] = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\knight\\" + "attack" + i + ".png"));
				die[i] = ImageIO.read(new File(System.getProperty("user.dir") + "\\assets\\knight\\" + "die" + i + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public int getADam ()
	{
		return archerDamage;
	}
	
	
	public int getKDam ()
	{
		frame = 0;
		action = "attack";
		return knightDamage;
	}
	
	
	public int getANum ()
	{
		return numArchers;
	}
	
	
	public int getKNum ()
	{
		return numKnights;
	}
	
	
	public int getHP()
	{
		return hp;
	}
	
	
	public void attack (int damage)
	{
		hp -= damage;
		
		if (hp <= 0)
		{
			numKnights--;
			hp = knightMaxHealth;
			action = "die";
			frame = 0;
		}
	}
	
	
	public BufferedImage getFrame ()
	{
		if (action == "attack")
		{
			if (frame < 8)
			{
				frame++;
				return attack [frame];
			}
			else
			{
				action = "idle";
				frame = 0;
			}
		}
		if (action == "idle")
		{
			return attack[0];
		}
		if (action == "die")
		{
			if (frame < 8)
			{
				frame++;
				return die[frame];
			}
			else
			{
				return die[8];
			}
		}
		
		return attack[0];
	}
	
	
	public void recruit (String type)
	{
		if (type == "archer")
		{
			numArchers++;
		}
		else if (type == "knight")
		{
			numKnights++;
		}
	}
	
	
	public void upgrade (String up, int amount)
	{
		if (up == "aDam")
		{
			archerDamage += amount;
		}
		else if (up == "kDam")
		{
			knightDamage += amount;
		}
		else if (up == "kHP")
		{
			knightMaxHealth += amount;
		}
	}
	
	
	public void reduce (String type)
	{
		if (type == "archer")
		{
			numArchers--;
		}
		else if (type == "knight")
		{
			numKnights--;
		}
	}
	
}
