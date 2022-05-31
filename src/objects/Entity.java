package objects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import loader.ImageLoader;

//Alec Ibarra
public class Entity {

	double mapPosX = -100;
	double mapPosY = -100;
	double startX = -100;
	double startY = -100;
	int points = 0;
	int ID = 0;
	int teamID = 0;
	int health = 100;
	int maxHealth = 100;
	int damage = 0;
	int iconSizeX = 25;
	int iconSizeY = 25;
	int startMoney = 200;
	int money = 200;
	int maxMoney = 200;
	int cooldown = 120;
	int maxCooldown = 120;
	int rotation = 0;
	double speed = 1.0;
	double velocityX = 0.0;
	double velocityY = 0.0;
	String owner = "unknown";
	String type = "unknown";
	Rectangle boundingBox = new Rectangle();
	BufferedImage icon = ImageLoader.getImage("noTexture");
	
	/***************************************** 
				  Constructers
	*****************************************/
	
	public Entity(){}
	
	public Entity(double mapPosX, double mapPosY, BufferedImage icon){
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.icon = icon;
	}
	
	/***************************************** 
				    Utilities
	*****************************************/
	
	public void clone(Entity other){//Very CPU expensive on large scale better to assign to temp object instead
		this.mapPosX = other.mapPosX;
		this.mapPosY = other.mapPosY;
		this.startX = other.startX;
		this.startY = other.startY;
		this.points = other.points;
		this.ID = other.ID;
		this.teamID = other.teamID;
		this.health = other.health;
		this.maxHealth = other.maxHealth;
		this.damage = other.damage;
		this.iconSizeX = other.iconSizeX;
		this.iconSizeY = other.iconSizeY;
		this.startMoney = other.startMoney;
		this.money = other.money;
		this.maxMoney = other.maxMoney;
		this.cooldown = other.cooldown;
		this.maxCooldown = other.maxCooldown;
		this.rotation = other.rotation;
		this.speed = other.speed;
		this.velocityX = other.velocityX;
		this.velocityY = other.velocityY;
		this.owner = other.owner;
		this.type = other.type;
		this.boundingBox = other.boundingBox;
		this.icon = other.icon;
	}
	
	public boolean isDead()
	{
		if(health <= 0)
			return true;
		else
			return false;
	}
	
	public void addMoney(int money){
		this.money += money;
	}
	
	public void addMapPosX(int mapPosX){
		this.mapPosX += mapPosX;
	}
	
	public void addMapPosY(int mapPosY){
		this.mapPosY += mapPosY;
	}
	
	public void addMapPosX(double mapPosX){
		this.mapPosX += mapPosX;
	}
	
	public void addMapPosY(double mapPosY){
		this.mapPosY += mapPosY;
	}
	
	public void addRotation(int rotation) {
		this.rotation += rotation;
		if(this.rotation > 360)
			this.rotation -= 360;
	}
	
	public void addMaxHealth(int maxHealth){
		this.maxHealth += maxHealth;
	}
	
	public void addPoints(int numPoints)
	{
		points += numPoints;
	}
	
	public void updateBoundingBox(){
		boundingBox = new Rectangle((int)this.mapPosX,(int)this.mapPosY,this.iconSizeX,this.iconSizeY);
	}
	
	/***************************************** 
				Getters and Setters
	*****************************************/

	public double getMapPosX() {
		return mapPosX;
	}

	public void setMapPosX(double mapPosX) {
		this.mapPosX = mapPosX;
	}

	public double getMapPosY() {
		return mapPosY;
	}

	public void setMapPosY(double mapPosY) {
		this.mapPosY = mapPosY;
	}
	
	public double getStartX() {
		return startX;
	}

	public void setStartX(double startX) {
		this.startX = startX;
	}
	
	public double getStartY() {
		return startY;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int TeamID) {
		this.teamID = TeamID;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getIconSizeX() {
		return iconSizeX;
	}

	public void setIconSizeX(int iconSizeX) {
		this.iconSizeX = iconSizeX;
	}

	public int getIconSizeY() {
		return iconSizeY;
	}

	public void setIconSizeY(int iconSizeY) {
		this.iconSizeY = iconSizeY;
	}

	public int getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(int startMoney) {
		this.startMoney = startMoney;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getMaxCooldown() {
		return maxCooldown;
	}

	public void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}
	
	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Rectangle getBoundingBox() {
		updateBoundingBox();
		return boundingBox;
	}

	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}
}
