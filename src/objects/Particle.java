package objects;

import loader.ImageLoader;

//Alec Ibarra
public class Particle extends Entity{

	public Particle()
	{
		this.icon = ImageLoader.getImage("noTexture");
		this.type = "unknown";
		this.health = 50;
	}
	
	public Particle(double mapPosX, double mapPosY, int rotation, String type)
	{
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.type = type;
		this.health = 50;
		this.maxHealth = 50;
		this.rotation = rotation;
		this.icon = ImageLoader.getImage(type);
	}
	
	public Particle(double mapPosX, double mapPosY, int rotation, String type, int health)
	{
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.type = type;
		this.health = health;
		this.maxHealth = health;
		this.rotation = rotation;
		this.icon = ImageLoader.getImage(type);
	}
	
}
