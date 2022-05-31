package objects;

import loader.ImageLoader;

//Alec Ibarra
public class Powerup extends Entity{
	
	boolean draw = true;
	boolean activated = false;
	int pos;
	
	public Powerup()
	{
		this.icon = ImageLoader.getImage("noTexture");
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
		this.health = 100;
	}
	
	public Powerup(int mapPosX, int mapPosY, String type)
	{
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.health = 200;
		this.type = type;
		this.icon = ImageLoader.getImage(type);
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
	}
	
	public Powerup(int mapPosX, int mapPosY, String type, int pos)
	{
		this.pos = pos;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.health = 200;
		this.maxHealth = 200;
		this.type = type;
		this.icon = ImageLoader.getImage(type);
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
	}
	
	public Powerup(int mapPosX, int mapPosY, int health, String type)
	{
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.health = health;
		this.type = type;
		this.maxHealth = health;
		this.icon = ImageLoader.getImage(type);
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
	}
	
	public void activate()
	{
		activated = true;
	}
	
	public void update()
	{
		this.health--;
	}
	
	public int getPos(){
		return pos;
	}
	
	public boolean getDraw(){
		return draw;
	}
	
	public boolean getActivated(){
		return activated;
	}

}
