package objects;

import loader.ImageLoader;

//Alec Ibarra
public class Tank extends Entity{

	int shootTimer = 0;
	int shootCoolDown = 500;
	int boostTimer = 0;
	int boostCoolDown = 500;
	
	boolean powerupShoot = false;
	boolean powerupBoost = false;
	boolean powerupHealth = false;
	
	public Tank()
	{
		this.health = 100;
		this.maxHealth = 100;
		this.icon = ImageLoader.getImage("tank");
		this.iconSizeX = icon.getWidth();
		this.iconSizeY = icon.getHeight();
	}
	
	public Tank(double mapPosX, double mapPosY)
	{
		this.health = 100;
		this.maxHealth = 100;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.icon = ImageLoader.getImage("tank");
		this.iconSizeX = this.icon.getWidth();
		this.iconSizeY = this.icon.getHeight();
	}
	
	public Tank(double mapPosX, double mapPosY, int ID)
	{
		this.ID = ID;
		this.health = 100;
		this.maxHealth = 100;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.icon = ImageLoader.getImage("tank");
		this.iconSizeX = this.icon.getWidth();
		this.iconSizeY = this.icon.getHeight();
	}
	
	public Tank(double mapPosX, double mapPosY, int ID, int teamID)
	{
		this.ID = ID;
		this.teamID = teamID;
		this.health = 100;
		this.maxHealth = 100;
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.startX = mapPosX;
		this.startY = mapPosY;
		this.icon = ImageLoader.getImage("tank");
		this.iconSizeX = this.icon.getWidth();
		this.iconSizeY = this.icon.getHeight();
	}
	
	public boolean getPowerupShoot()
	{
		return powerupShoot;
	}
	
	public boolean getPowerupBoost()
	{
		return powerupBoost;
	}
	
	public boolean getPowerupHealth()
	{
		return powerupHealth;
	}
	
	public void setPowerupShoot(boolean powerupShoot)
	{
		this.powerupShoot = powerupShoot;
	}
	
	public void setPowerupBoost(boolean powerupBoost)
	{
		 this.powerupBoost = powerupBoost;
	}
	
	public void setPowerupHealth(boolean powerupHealth)
	{
		this.powerupHealth = powerupHealth;
	}
	
	public void addShootTimer(int time)
	{
		shootTimer += time;
	}
	
	public void addShootCoolDown(int time)
	{
		shootCoolDown += time;
	}
	
	public void addBoostTimer(int time)
	{
		boostTimer += time;
	}
	
	public void addBoostCoolDown(int time)
	{
		boostCoolDown += time;
	}
	
	public void setShootTimer(int shootTimer)
	{
		this.shootTimer = shootTimer;
	}
	
	public void setShootCoolDown(int shootCoolDown)
	{
		this.shootCoolDown = shootCoolDown;
	}
	
	public void setBoostTimer(int boostTimer)
	{
		this.boostTimer = boostTimer;
	}
	
	public void setBoostCoolDown(int boostCoolDown)
	{
		this.boostCoolDown = boostCoolDown;
	}
	
	public int getShootTimer()
	{
		return shootTimer;
	}
	
	public int getShootCoolDown()
	{
		return shootCoolDown;
	}
	
	public int getBoostTimer()
	{
		return boostTimer;
	}
	
	public int getBoostCoolDown()
	{
		return boostCoolDown;
	}
}
