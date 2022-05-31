package objects;

import loader.ImageLoader;

//Alec Ibarra
public class Wall extends Entity{
	
	public Wall()
	{
		this.icon = ImageLoader.getRandomWallIcon();
		this.iconSizeX = this.icon.getWidth();
		this.iconSizeY = this.icon.getHeight();
	}
	
	public Wall(double mapPosX, double mapPosY){
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.icon = ImageLoader.getRandomWallIcon();
		this.iconSizeX = this.icon.getWidth();
		this.iconSizeY = this.icon.getHeight();
	}
	
	public Wall(double mapPosX, double mapPosY, int width, int height){
		this.mapPosX = mapPosX;
		this.mapPosY = mapPosY;
		this.icon = ImageLoader.getRandomWallIcon();
		this.iconSizeX = width;
		this.iconSizeY = height;
	}

}
