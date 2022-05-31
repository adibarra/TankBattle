package objects;

import loader.ImageLoader;

//Alec Ibarra
public class Bullet extends Entity{

	public Bullet()
	{
		this.icon = ImageLoader.getImage("bullet");
		this.health = 120;
		this.speed = 3.0;
	}
	
	public Bullet(double mapPosX, double mapPosY, int rotation)
	{
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.rotation = rotation;
		this.icon = ImageLoader.getImage("bullet");
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
		this.health = 120;
		this.speed = 3.0;
	}
	
	public Bullet(double mapPosX, double mapPosY, int rotation, int ownerID)
	{
		this.ID = ownerID;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.rotation = rotation;
		this.icon = ImageLoader.getImage("bullet");
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
		this.health = 350;
		this.speed = 3.0;
		this.damage = 25;
	}
	
	public Bullet(double mapPosX, double mapPosY, int rotation, int ownerID, int teamID)
	{
		this.ID = ownerID;
		this.teamID = teamID;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.rotation = rotation;
		this.icon = ImageLoader.getImage("bullet");
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
		this.health = 350;
		this.speed = 3.0;
		this.damage = 25;
	}
}
