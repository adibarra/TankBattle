package loader;

//Alec Ibarra
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	static BufferedImage splashScreen, background, backgroundRed, backgroundGreen, guiBack, guiPause, guiCheckedCheckbox, guiCheckbox;
	static BufferedImage effectFire, effectSmoke, bullet, powerupAmmo, powerupHealth;
	static BufferedImage tank, wall1, noTexture;
	private static BufferedImage powerupBoost;
	
	public ImageLoader()
	{
		try {//load images to use
        	
        	splashScreen = ImageIO.read(getClass().getClassLoader().getResource("SplashScreen.png"));
        	background = ImageIO.read(getClass().getClassLoader().getResource("Background.png"));
        	backgroundRed = ImageIO.read(getClass().getClassLoader().getResource("BackgroundRed.png"));
        	backgroundGreen = ImageIO.read(getClass().getClassLoader().getResource("BackgroundGreen.png"));
        	
        	guiBack = ImageIO.read(getClass().getClassLoader().getResource("GUIBack.png"));
        	guiPause = ImageIO.read(getClass().getClassLoader().getResource("GUIPause.png"));
        	guiCheckedCheckbox = ImageIO.read(getClass().getClassLoader().getResource("GUICheckedCheckbox.png"));
        	guiCheckbox = ImageIO.read(getClass().getClassLoader().getResource("GUICheckbox.png"));
        	
            tank = ImageIO.read(getClass().getClassLoader().getResource("Tank.png"));
            bullet = ImageIO.read(getClass().getClassLoader().getResource("Bullet.png"));
            
            powerupAmmo = ImageIO.read(getClass().getClassLoader().getResource("PowerupAmmo.png"));
            powerupHealth = ImageIO.read(getClass().getClassLoader().getResource("PowerupHealth.png"));
            powerupBoost = ImageIO.read(getClass().getClassLoader().getResource("PowerupBoost.png"));
            
            wall1 = ImageIO.read(getClass().getClassLoader().getResource("Wall1.png"));
            
        	effectFire = ImageIO.read(getClass().getClassLoader().getResource("EffectFire.png"));
        	effectSmoke = ImageIO.read(getClass().getClassLoader().getResource("EffectSmoke.png"));
        	noTexture = ImageIO.read(getClass().getClassLoader().getResource("NoTexture.png"));
        	
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public static BufferedImage getRandomWallIcon()
	{
		int random = (int)(Math.random()*1)+0;
		
		if(random == 0)
		{
			return wall1;
		}
		return wall1;
	}
	
	public static BufferedImage getImage(String imageName)
	{
		if(imageName.equals("background"))
			return background;
		
		if(imageName.equals("backgroundRed"))
			return backgroundRed;
		
		if(imageName.equals("backgroundGreen"))
			return backgroundGreen;
		
		if(imageName.equals("splashScreen"))
			return splashScreen;
		
		if(imageName.equals("guiBack"))
			return guiBack;
		
		if(imageName.equals("guiPause"))
			return guiPause;
		
		if(imageName.equals("guiCheckedCheckbox"))
			return guiCheckedCheckbox;
		
		if(imageName.equals("guiCheckbox"))
			return guiCheckbox;
		
		if(imageName.equals("tank"))
			return tank;	
		
		if(imageName.equals("wall1"))
			return wall1;
		
		if(imageName.equals("bullet"))
			return bullet;
		
		if(imageName.equals("powerupAmmo"))
			return powerupAmmo;
		
		if(imageName.equals("powerupHealth"))
			return powerupHealth;
		
		if(imageName.equals("powerupBoost"))
			return powerupBoost;
		
		if(imageName.equals("effectFire"))
			return effectFire;
		
		if(imageName.equals("effectSmoke"))
			return effectSmoke;

		return noTexture;
	}

}
