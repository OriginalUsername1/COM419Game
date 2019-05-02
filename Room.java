public class Room {
	
	private String name;
	private boolean worked;
	private int wood = 0;
	private int stone = 0;
	private int food = 0;
	private int people = 0;
	private int workingPop = 0;
	private int gold = 0;
	
	
	
	public Room (String input)
	{
		
		name = input;
		
		if (name == "throne")
		{
			wood = 1;
			stone = 1;
		}
		else if (name == "barracks")
		{
			people = 15;
			workingPop = 5;
		}
		else if (name == "farm")
		{
			food = 5;
			workingPop = 5;
		}
		else if (name == "mill")
		{
			wood = 5;
			workingPop = 10;
		}
		else if (name == "armory")
		{
			workingPop = 3;
		}
		else if (name == "mines")
		{
			stone = 2;
			gold = 2;
			workingPop = 8;
		}
		
	}
	
	
	public String getName ()
	{
		return name;
	}
	
	
	public int getWood ()
	{
		if (worked)
			return wood;
		else
			return 0;
	}
	
	
	public int getStone ()
	{
		if (worked)
			return stone;
		else
			return 0;
	}
	
	
	public int getFood ()
	{
		if (worked)
			return food;
		else
			return 0;
	}
	
	
	public int getPeople ()
	{
		if (worked)
			return people;
		else
			return 0;
	}
	
	
	public int getGold ()
	{
		if (worked)
			return gold;
		else
			return 0;
	}
	
	
	public int getWorkers ()
	{
		return workingPop;
	}
	
	
	public void isWorked (boolean wrk)
	{
		worked = wrk;
	}
	
	
	public void upgrade ()
	{
		
	}

}
