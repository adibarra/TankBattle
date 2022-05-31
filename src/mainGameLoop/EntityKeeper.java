package mainGameLoop;

//Alec Ibarra
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;

import objects.Bullet;
import objects.Particle;
import objects.Powerup;
import objects.Tank;
import objects.Wall;


public class EntityKeeper {
	
	static ArrayList<Tank> tanks = new ArrayList<Tank>();
	static ArrayList<Wall> walls = new ArrayList<Wall>();
	static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	static ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	static ArrayList<Particle> particles = new ArrayList<Particle>();
	
	static Area noZone = new Area();
	static Rectangle bigScreen = new Rectangle(-100,-100,1200,800);
	static Rectangle screen = new Rectangle(0,21,990,650);
	static int world1 = 26;
	static int world2 = 40;
	static int[][] world = new int[world1][world2];
	
	static Rectangle tempRec = new Rectangle();
	static Area tempArea = new Area();
	static Wall tempWall = new Wall();
	
	public static void updateWorld()
	{
		for (int k = 0; k < EntityKeeper.tanks.size(); k++)
        {
			int xPos = (int) (Math.round((tanks.get(k).getMapPosX()+10)/10)*10);
			int yPos = (int) (Math.round((tanks.get(k).getMapPosY()-10)/10)*10);
				
			if(yPos/25 <= world1 && xPos/25 <= world2)//if inside world bounds
			{
				if(tanks.get(k).getTeamID() == Logic.teamOneID)
				{
					world[yPos/25][xPos/25] = 1;
				}
				else if(tanks.get(k).getTeamID() == Logic.teamTwoID)
				{
					world[yPos/25][xPos/25] = 2;
				}
			}	
        }
	}
	
	public static void buildNoZone()//call whenever a wall is placed
	{
		noZone = new Area();
		
		//creates barrier around screen
		tempArea = new Area(bigScreen);
		noZone.add(tempArea);
		tempArea = new Area(screen);
		noZone.subtract(tempArea);
		
		//adds walls to nozone
		for (int k = 0; k < EntityKeeper.walls.size(); k++)
        {
			tempWall.clone(EntityKeeper.walls.get(k));
			tempRec = tempWall.getBoundingBox();
			tempArea = new Area(tempRec);
			noZone.add(tempArea);
        }
	}

}
