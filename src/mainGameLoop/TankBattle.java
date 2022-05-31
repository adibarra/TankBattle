package mainGameLoop;

//Alec Ibarra
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import loader.ImageLoader;
import objects.Bullet;
import objects.Particle;
import objects.Powerup;
import objects.Tank;
import objects.Wall;

public class TankBattle
{

    public static void main(String[] args)
    {
    	Logic game = new Logic();
		
		game.addWindowListener(new WindowAdapter()
		{public void windowClosing(WindowEvent e)
		{System.exit(0);}});
		game.setSize(1000,671);
		game.setResizable(false);
		game.setVisible(true);
    
		game.addComponentListener(new ComponentListener() {
        public void componentResized(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentShown(ComponentEvent e) {}
		public void componentHidden(ComponentEvent e) {}
    });
    }
}

@SuppressWarnings("serial")
class Logic extends Frame implements MouseListener,MouseMotionListener,KeyListener,MouseWheelListener
{
	
	Image offscreen = null;
    Graphics g2;
    
    //game state controls
    static boolean gameOver = false;
    static boolean paused = true;
    static boolean gameStarted = false;
    static boolean showControls = false;
    static boolean showSettings= false;
    static boolean showDebug = false;
    
    //game movement controls
    static boolean up = false;
    static boolean left = false;
    static boolean right = false;
    static boolean down = false;
    static boolean boost = false;
    static boolean shoot = false;
    static boolean moveNow = true;
    
    //game settings
    static boolean landGrab = false;
    static boolean bulletShred = false;
    static boolean friendlyFire = false;
    static boolean TDM = true;//does nothing atm
    
    //game powerups
    static boolean pos0used = false;
    static boolean pos1used = false;
    static boolean pos2used = false;
    static boolean pos3used = false;
    
    //game control
    static double now;
	static double last = new Date().getTime();
	static double passed;
	static double dt = 1000 / 60;
	static double accumulator;
	
	//game mechanics control
	static int mousex = -100;
	static int mousey = -100;
	static int counter = 0;
	
	//important variables
	static int teamOneID = (int)(Math.random()*10000)+1;
	static int teamTwoID = (int)(Math.random()*10000)+1;
	
	static int myTankID = (int)(Math.random()*10000)+1;
	static int tank2ID = (int)(Math.random()*10000)+1;
	static int tank3ID = (int)(Math.random()*10000)+1;
	static int tank4ID = (int)(Math.random()*10000)+1;
	
	//buttons
	Rectangle controls = new Rectangle(150,400,150,75);
	Rectangle play = new Rectangle(400,400,200,75);
	Rectangle settings = new Rectangle(700,400,150,75);
	
	Rectangle menuControls = new Rectangle(400,100,200,75);
	Rectangle menuPlay = new Rectangle(400,250,200,75);
	Rectangle menuSettings = new Rectangle(400,400,200,75);
	
	Rectangle guiBack = new Rectangle(2,23,25,25);
	Rectangle guiPause = new Rectangle(973,23,25,25);
	
	Rectangle guiLandGrab = new Rectangle(325,150,25,25);
	Rectangle guiBulletShred = new Rectangle(325,200,25,25);
	Rectangle guiFriendlyFire = new Rectangle(325,250,25,25);
	Rectangle guiTDM = new Rectangle(325,300,25,25);
	
	//class instances
	ImageLoader imageLoader = new ImageLoader();
	
	//objects
	Tank myTank = new Tank(200,200,myTankID,teamOneID);
	Tank tank2 = new Tank(800,100,tank2ID,teamTwoID);
	Tank tank3 = new Tank(800,200,tank3ID,teamTwoID);
	Tank tank4 = new Tank(800,300,tank4ID,teamTwoID);
	
	//temp objects
	Tank tempTank = new Tank();
	Wall tempWall = new Wall();
	Bullet tempBullet = new Bullet();
	Bullet tempBullet2 = new Bullet();
	Particle tempParticle = new Particle();
	Powerup tempPowerup = new Powerup();
	
	
    
	
	
	public Logic()
    {
        super("TankBattle");//sets program name
        List<Image> icons = new ArrayList<Image>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon128x128.png")));//loads icon 128x128
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon64x64.png")));//loads icon 64x64
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon32x32.png")));//loads icon 32x32
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Icon16x16.png")));//loads icon 16x16
        super.setIconImages(icons);//loads icons
        addMouseListener(this);//adds neccessary listener
		addMouseMotionListener(this);//adds neccessary listener
		addMouseWheelListener(this);//adds neccessary listener
		addKeyListener(this);//adds neccessary listener
        requestFocusInWindow();//calls focus for window
        EntityKeeper.buildNoZone();
        
        EntityKeeper.tanks.add(myTank);
        EntityKeeper.tanks.add(tank2);
        EntityKeeper.tanks.add(tank3);
        EntityKeeper.tanks.add(tank4);

        EntityKeeper.walls.add(new Wall(475,21,75,250));
        EntityKeeper.walls.add(new Wall(475,421,75,250));
        EntityKeeper.buildNoZone();
    }
	
	/***************************************** 
					Main Loop
	*****************************************/
	
	public void update(Graphics g)//main method
    {    	
		//prepare double buffer
		offscreen = createImage(1000,671);
		g2 = offscreen.getGraphics();
		
		if(!gameOver)
		{	
			if(!paused)
			{					
				calcTimesToRun();
				while (accumulator >= dt) 
				{
					runLogic();//main logic method
					accumulator -= dt;
				}
				display(g2);//main display method
				delay(dt);
			}
			else
			{
				delay(40);
				last = new Date().getTime();
				
				if(!gameStarted && !showControls && !showSettings)//draws splash screen
				{
					drawSplashScreen(g2);
				}
				else if(showControls && !showSettings)//draws splash screen
				{
					drawControlsScreen(g2);
				}
				else if(!showControls && showSettings)//draws splash screen
				{
					drawSettingsScreen(g2);
				}
				else if(!showSettings && !showControls && gameStarted && paused)//draws paused menu
				{	
					drawPauseScreen(g2);
				}	
			}
		}
		else
		{	
			drawGameOver(g2);//draw GameOver
			delay(dt);
		}
		
		g.drawImage(offscreen, 0, 0, this);//draw screen from buffer
		counter++;
		repaint();
	}
	
	
	/***************************************** 
					Logic
	*****************************************/
	
	public void runLogic()
	{
		worldLogic();
		playerLogic();
		botLogic();
		bulletLogic();
	}
	
	public void worldLogic()
	{
		if((int)(Math.random()*80) == 0)
		{
			int pos = (int)(Math.random()*4);
			int x = -100;
			int y = -100;
			
			if(pos == 0 && !pos0used)
			{
				pos0used = true;
				x = 25;
				y = 46;
			}
			else if(pos == 1 && !pos1used)
			{
				pos1used = true;
				x = 950;
				y = 46;
			}
			else if(pos == 2 && !pos2used)
			{
				pos2used = true;
				x = 25;
				y = 596;
			}
			else if (pos == 3 && !pos3used)
			{
				pos3used = true;
				x = 950;
				y = 596;
			}
			
			String type;
			int powerupChosen = (int)(Math.random()*3);
			
			if(powerupChosen == 0)
			{
				type = "powerupAmmo";
			} 
			else if(powerupChosen == 1)
			{
				type = "powerupHealth";
			} 
			else
			{
				type = "powerupBoost";
			}
		
			if (x != -100 && y != -100)
			{
				EntityKeeper.powerups.add(new Powerup(x,y,type,pos));
			}
		}	
		
		for (int k = 0; k < EntityKeeper.powerups.size(); k++)
		{
			tempPowerup = EntityKeeper.powerups.get(k);
						
			if(tempPowerup.getBoundingBox().contains(myTank.getMapPosX()+myTank.getIconSizeX()/2,myTank.getMapPosY()+myTank.getIconSizeY()/2))
			{
				
				if (tempPowerup.getType().equals("powerupAmmo"))
				{
					tempPowerup.activate();
					myTank.setPowerupShoot(true);
				}
				if (tempPowerup.getType().equals("powerupHealth"))
				{
					tempPowerup.activate();
					myTank.setPowerupHealth(true);
				}
				if (tempPowerup.getType().equals("powerupBoost"))
				{
					tempPowerup.activate();
					myTank.setPowerupBoost(true);
				}
			}
			
			if (tempPowerup.getActivated())
			{
				tempPowerup.update();
			}
			
			if(tempPowerup.getHealth() <= 0)
			{
				EntityKeeper.powerups.remove(k);
				
				if (tempPowerup.getType().equals("powerupAmmo"))
				{
					myTank.setPowerupShoot(false);
				}
				if (tempPowerup.getType().equals("powerupHealth"))
				{
					myTank.setPowerupHealth(false);
				}
				if (tempPowerup.getType().equals("powerupBoost"))
				{
					myTank.setPowerupBoost(false);
				}
				
				int pos = tempPowerup.getPos();
				if(pos == 0)
				{
					pos0used = false;
				}
				else if(pos == 1)
				{
					pos1used = false;
				}
				else if(pos == 2)
				{
					pos2used = false;
				}
				else if(pos == 3)
				{
					pos3used = false;
				}
			}
		}
		
		for(int k = 0; k < EntityKeeper.tanks.size(); k++)
		{
			tempTank = EntityKeeper.tanks.get(k);
			checkForPowerUps(tempTank);	
		}
	}
	
	public void playerLogic()
	{					
		double tankCos = Math.cos(Math.toRadians(myTank.getRotation()-90));
		double tankSin = Math.sin(Math.toRadians(myTank.getRotation()-90));
		double tankX = myTank.getMapPosX();
		double tankY = myTank.getMapPosY();
		int tankIconX = myTank.getIconSizeX();
		int tankIconY = myTank.getIconSizeY();
		
		if(up)//move tank
		{	
			myTank.addMapPosX( (myTank.getSpeed() * tankCos ));
			myTank.addMapPosY( (myTank.getSpeed() * tankSin ));
			
			//if tank would be in wall then undo
			if(EntityKeeper.noZone.contains(tankX,tankY) || EntityKeeper.noZone.contains(tankX+tankIconX,tankY+tankIconY))
			{
				if((myTank.getRotation() > 315 || myTank.getRotation() <= 45))//going up
				{
					myTank.addMapPosY( -((myTank.getSpeed()+.1) * tankSin ));
				}
				else if((myTank.getRotation() > 45 && myTank.getRotation() <= 135))//going right
				{
					myTank.addMapPosX( -((myTank.getSpeed()+.1) * tankCos ));
				}
				else if((myTank.getRotation() > 135 && myTank.getRotation() <= 225))//going down
				{
					myTank.addMapPosY( -((myTank.getSpeed()+.1) * tankSin ));
				}
				else if((myTank.getRotation() > 225 && myTank.getRotation() <= 315))//going left
				{
					myTank.addMapPosX( -((myTank.getSpeed()+.1) * tankCos ));
				}
			}
		}
		if(left)//rotate tank
		{	
			myTank.addRotation(-2);
		}
		if(right)//rotate tank
		{	
			myTank.addRotation(2);
		}
		if(down)//move tank
		{	
			myTank.addMapPosX( -((myTank.getSpeed()+.2) * tankCos ));
			myTank.addMapPosY( -((myTank.getSpeed()+.2) * tankSin ));
		}
			
		if(EntityKeeper.noZone.contains(tempTank.getBoundingBox()))//if any tank completely outside boundries then move to center of screen
		{	
			myTank.setMapPosX(myTank.getStartX());
			myTank.setMapPosY(myTank.getStartY());
		}	

			double tTankX = myTank.getMapPosX();
			double tTankY = myTank.getMapPosY();
		
			//Shoot stuff
			if(shoot && myTank.getShootCoolDown() <= 0)//only allow the shoot to be used if it is full
			{	
			
				EntityKeeper.bullets.add(new Bullet(tankX+9,tankY+tempBullet.getIconSizeY(),myTank.getRotation(),myTank.getID(),myTank.getTeamID()));
 			
				myTank.setShootTimer(0);
				myTank.setShootCoolDown(500);
			
			}
			else//if shoot not being used then start cooldown
			{
				myTank.addShootCoolDown(-2);
			}
		
			if(tempTank.getShootTimer() < 0)//if boost reaches empty then start cooldown count again && limit minboost
			{
				myTank.setShootTimer(0);
				myTank.setShootCoolDown(500);
			}	
			if(myTank.getShootTimer() > 500)//if shoot reaches full then force cooldown end && limit maxboost
			{	
				myTank.setShootTimer(500);
				myTank.setShootCoolDown(0);
			}
		
			//Boost stuff
			if(boost && myTank.getBoostCoolDown() <= 0)//only allow the boost to be used if it is full
			{	
				addParticle(tTankX-2,tTankY,myTank.getRotation(),"effectFire",10);
				addParticle(tTankX,tTankY,myTank.getRotation(),"effectFire",10);
				addParticle(tTankX,tTankY,myTank.getRotation(),"effectFire",10);
				addParticle(tTankX+2,tTankY,myTank.getRotation(),"effectFire",10);
				addParticle(tTankX,tTankY+2,myTank.getRotation(),"effectFire",10);
				myTank.setSpeed(2);
				myTank.addBoostTimer(-4);		
			}
			else//if boost not being used then speed is 1
			{
				myTank.setSpeed(1);
				myTank.addBoostCoolDown(-1);
			}
		
			if(myTank.getBoostTimer() < 0)//if boost reaches empty then start cooldown count again && limit minboost
			{
				myTank.setBoostTimer(0);
				myTank.setBoostCoolDown(500);
			}	
			if(myTank.getBoostTimer() > 500)//if boost reaches full then force cooldown end && limit maxboost
			{	
				myTank.setBoostTimer(500);
				myTank.setBoostCoolDown(0);
			}
			myTank.addShootTimer(2);//shoot recharge
			myTank.addBoostTimer(1);//boost recharge
		
			if(EntityKeeper.noZone.contains(myTank.getBoundingBox()))//if any tank completely outside boundries then move to center of screen
			{	
				myTank.setMapPosX(myTank.getStartX());
				myTank.setMapPosY(myTank.getStartY());
			}
		
		for(int k = 0; k < EntityKeeper.tanks.size(); k++)
		{
			tempTank = EntityKeeper.tanks.get(k);
			checkForPowerUps(tempTank);	
		}
	}
	
	public void botLogic()
	{
		for(int k = 0; k < EntityKeeper.tanks.size(); k++)
		{
			if(k != 0)
			{
				tempTank = EntityKeeper.tanks.get(k);
				
				double myTankX = 0;
				double myTankY = 0;
				double tank2X;
				double tank2Y;
				double tankIconX;
				double tankIconY;
				double fuzzX;
	    		double fuzzY;
	    		
	    		moveNow = true;
	    		for(int j = 0; j < EntityKeeper.tanks.size(); j++)
	    		{
	    			Tank otherTank = EntityKeeper.tanks.get(j);
	    			
	    			if(TDM)
	    			{
	    				//if tank is 
	    				if((getDistance(tempTank,myTank) < 75 || getDistance(tempTank,otherTank) < 25) && otherTank.getID() != tempTank.getID())
	    				{
	    					moveNow = false;
	    				}
	    				else
	    				{
	    					break;
	    				}
	    			}
	    			else
	    			{
	    				if(getDistance(tempTank,otherTank) < 25 && otherTank.getID() != tempTank.getID())
	    				{
	    					moveNow = false;
	    				}
	    				else
	    				{
	    					break;
	    				}
	    			}
	    		}
	    		//TODO make tanks find enemy based on team settings && teamIDs
	    		myTankX = myTank.getMapPosX();
				myTankY = myTank.getMapPosY();
				tank2X = tempTank.getMapPosX();
				tank2Y = tempTank.getMapPosY();
				tankIconX = tempTank.getIconSizeX();
				tankIconY = tempTank.getIconSizeY();
				fuzzX = 0;//(Math.random()*.1);
	    		fuzzY = 0;//(Math.random()*.1);
		
				float angle = (float) Math.toDegrees(Math.atan2(myTankY - tank2Y, myTankX - tank2X));
				if(angle < 0)
				{
	    			angle += 360;
	    		} else if(angle > 360)
	    		{
	    			angle -= 360;
	    		}
	    
	    		tempTank.setRotation((int)angle+90);
	    		double tankCos = Math.cos(Math.toRadians(tempTank.getRotation()-90));
	    		double tankSin = Math.sin(Math.toRadians(tempTank.getRotation()-90));
	    		
	    		if(moveNow)
    			{
    				tempTank.addMapPosX( ((tempTank.getSpeed()+fuzzX) * tankCos ));
    				tempTank.addMapPosY( ((tempTank.getSpeed()+fuzzY) * tankSin ));
    			}
    			else
    			{
    				tempTank.addMapPosX( -((tempTank.getSpeed()+fuzzX) * tankCos ));
    				tempTank.addMapPosY( -((tempTank.getSpeed()+fuzzY) * tankSin ));
    			}

				//if tank would be in wall then undo
				if(EntityKeeper.noZone.contains(tank2X,tank2Y) || EntityKeeper.noZone.contains(tank2X+tankIconX,tank2Y+tankIconY))
				{
					if((tempTank.getRotation() > 315 || tempTank.getRotation() <= 45))//going up
					{
						tempTank.addMapPosY( -((tempTank.getSpeed()+.1) * tankSin ));
					}
					else if((tempTank.getRotation() > 45 && tempTank.getRotation() <= 135))//going right
					{
						tempTank.addMapPosX( -((tempTank.getSpeed()+.1) * tankCos ));
					}
					else if((tempTank.getRotation() > 135 && tempTank.getRotation() <= 225))//going down
					{
						tempTank.addMapPosY( -((tempTank.getSpeed()+.1) * tankSin ));
					}
					else if((tempTank.getRotation() > 225 && tempTank.getRotation() <= 315))//going left
					{
						tempTank.addMapPosX( -((tempTank.getSpeed()+.1) * tankCos ));
					}
				}
			
				tank2X = tempTank.getMapPosX();
				tank2Y = tempTank.getMapPosY();
		
				if(TDM)
    			{
					if(getDistance(tempTank,myTank) < 200 && tempTank.getShootCoolDown() <= 0)//if tanks are close enough AND cooldown is done then shoot
					{
						EntityKeeper.bullets.add(new Bullet(tank2X+9,tank2Y+tempBullet.getIconSizeY(),tempTank.getRotation(),tempTank.getID(),tempTank.getTeamID()));
			
						tempTank.setShootTimer(0);
						tempTank.setShootCoolDown(500);
					}
					else//if shoot not being used then start cooldown
					{
						tempTank.addShootCoolDown(-2);
					}
    			}
				else
				{
					for(int j = 0; j < EntityKeeper.tanks.size(); j++)
		    		{
		    			Tank otherTank = EntityKeeper.tanks.get(j);
		    			
		    			if(getDistance(tempTank,otherTank) < 200 && tempTank.getShootCoolDown() <= 0)//if tanks are close enough AND cooldown is done then shoot
						{
							EntityKeeper.bullets.add(new Bullet(tank2X+9,tank2Y+tempBullet.getIconSizeY(),tempTank.getRotation(),tempTank.getID(),tempTank.getTeamID()));
			
							tempTank.setShootTimer(0);
							tempTank.setShootCoolDown(500);
						}
						else//if shoot not being used then start cooldown
						{
							tempTank.addShootCoolDown(-2);
							tempTank.addShootTimer(2);//shoot recharge
						}
		    		}
    			}
		
				if(tempTank.getShootTimer() < 0)//if shoot reaches empty then start cooldown count again && limit minboost
				{
					tempTank.setShootTimer(0);
					tempTank.setShootCoolDown(500);
				}	
				if(tempTank.getShootTimer() > 500)//if shoot reaches full then force cooldown end && limit maxboost
				{	
					tempTank.setShootTimer(500);
					tempTank.setShootCoolDown(0);
				}
			}
		
			if(EntityKeeper.noZone.contains(tempTank.getBoundingBox()))//if tank completely outside boundries then move to center of screen
			{	
				tempTank.setMapPosX(tempTank.getStartX());
				tempTank.setMapPosY(tempTank.getStartY());
			}	
		}
	}
	
	public void bulletLogic()
	{
		for(int k = 0; k < EntityKeeper.bullets.size(); k++)
		{	
			tempBullet = EntityKeeper.bullets.get(k);
			double xPos = tempBullet.getMapPosX();
			double yPos = tempBullet.getMapPosY();
			
			//move bullet
			tempBullet.addMapPosX( (tempBullet.getSpeed() * Math.cos(Math.toRadians(tempBullet.getRotation()-90)) ));
			tempBullet.addMapPosY( (tempBullet.getSpeed() * Math.sin(Math.toRadians(tempBullet.getRotation()-90)) ));
			
			for(int j = 0; j < EntityKeeper.tanks.size(); j++)//check each tank if bullet has hit it
			{
				tempTank = EntityKeeper.tanks.get(j);
				
				if(friendlyFire)
				{
					//if bullet hits tank REGARDLESS OF TEAM then kill bullet then hurt tank
					if(tempTank.getBoundingBox().contains(xPos,yPos) && tempTank.getID() != tempBullet.getID())
					{
						tempBullet.setHealth(0);
						tempTank.setHealth(tempTank.getHealth()-tempBullet.getDamage());
					}
				}
				else
				{
					//if bullet hits tank WHICH IS NOT THE SAME TEAM then kill bullet then hurt tank
					if(tempTank.getBoundingBox().contains(xPos,yPos) && tempTank.getTeamID() != tempBullet.getTeamID() && tempTank.getID() != tempBullet.getID())
					{
						tempBullet.setHealth(0);
						tempTank.setHealth(tempTank.getHealth()-tempBullet.getDamage());
					}
				}
				
				if(tempTank.getHealth() <= 0)//if is dead get ID of owner of bullet and credit kill
				{	
					tempTank.setMapPosX(tempTank.getStartX());
					tempTank.setMapPosY(tempTank.getStartY());
					tempTank.setHealth(100);
					if (TDM)
					{
						addPoints(tempBullet.getTeamID(),1);
					}
					else
					{
						addPoints(tempBullet.getID(),1);
					}
					tempTank = EntityKeeper.tanks.get(j);
				}
			}
			
			if(bulletShred)//if true then enable bullet vs bullet collisions
			{
				for(int j = 0; j < EntityKeeper.bullets.size(); j++)//check each bullet if bullet has hit it
				{
					tempBullet2 = EntityKeeper.bullets.get(j);
				
					//if bullet hits tank which is not its owner then kill bullet then hurt tank
					if(tempBullet2.getBoundingBox().contains(xPos,yPos) && tempBullet.getID() != tempBullet2.getID())
					{
						tempBullet2.setHealth(0);
					}
				}
			}	
			
			//if bullet hits wall or if bullet outside screen then kill bullet
			if(EntityKeeper.noZone.contains(xPos,yPos))
			{
				tempBullet.setHealth(0);
			}
			
			if(tempBullet.isDead())//if bullet is dead then add explosion particles
			{
				EntityKeeper.bullets.remove(k);
				addParticlesBy(xPos-tempBullet.getIconSizeX(),yPos,10,5,0,"effectSmoke",20);
			}
			else
			{
				tempBullet.setHealth(tempBullet.getHealth()-1);//bullet death tick
			}
		}	
	}
	
	/***************************************** 
					Display
	*****************************************/
	
	public void display(Graphics g2)
	{
		drawWorld(g2);
		drawEffects(g2);
		drawBullets(g2);
		drawPlayers(g2);
		drawHUD(g2);
	}
	
	public void drawWorld(Graphics g2)
	{
		if(landGrab)
		{
			EntityKeeper.updateWorld();
			
			for (int k = 0; k < 26; k++)
				for(int j = 0; j < 40; j++)
				{
					if(EntityKeeper.world[k][j] == 0)
					{
						g2.drawImage(ImageLoader.getImage("background"), 25*j, 25*k+21, this);
					}
					else if(EntityKeeper.world[k][j] == 1)
					{
						g2.drawImage(ImageLoader.getImage("backgroundRed"), 25*j, 25*k+21, this);
					}
					else if(EntityKeeper.world[k][j] == 2)
					{
						g2.drawImage(ImageLoader.getImage("backgroundGreen"), 25*j, 25*k+21, this);
					}
				}
		}
		else
		{
			for (int k = 0; k < 26; k++)
				for(int j = 0; j < 40; j++)
					g2.drawImage(ImageLoader.getImage("background"), 25*j, 25*k+21, this);	
		}
		
		for (int k = 0; k < EntityKeeper.powerups.size(); k++)
		{
			tempPowerup = EntityKeeper.powerups.get(k);//can not use clone because of extra variables
			
			float alpha = (float)(tempPowerup.getHealth()/2)/(float)(tempPowerup.getMaxHealth()/2); //calculate transparency to draw with
    		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);//set transparency
    		((Graphics2D) g2).setComposite(ac);//set transparency
    		
			g2.drawImage(tempPowerup.getIcon(),(int)tempPowerup.getMapPosX(),(int)tempPowerup.getMapPosY(),this);
				
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f);//reset transparency
        	((Graphics2D) g2).setComposite(ac);//reset transparency
			
			if(showDebug)
			{
				((Graphics2D) g2).draw(tempPowerup.getBoundingBox());
			}	
		}
		
		for (int k = 0; k < EntityKeeper.walls.size(); k++)
		{
			tempWall = EntityKeeper.walls.get(k);
    			
			g2.drawImage(tempWall.getIcon(),(int)tempWall.getMapPosX(),(int)tempWall.getMapPosY(),tempWall.getIconSizeX(),tempWall.getIconSizeY(),this);
			
			if(showDebug)
			{
				((Graphics2D) g2).draw(tempWall.getBoundingBox());
			}	
		}
		
		if(showDebug)//draw debug lines
    	{
    		g2.setColor(Color.BLACK);
    		for (int k = 0; k < 40; k++)
    			g2.drawLine(k*25,0,k*25,1000*25);
    		
    		for (int k = 0; k < 26; k++)
    			g2.drawLine(0,k*25+21,1000*25,k*25+21);
    	}
	}
	
	public void drawBullets(Graphics g2)
	{
		for (int k = 0; k < EntityKeeper.bullets.size(); k++)
        {
			tempBullet = EntityKeeper.bullets.get(k);
			
			//for each tank draw it with correct orientation and location
			AffineTransform at = new AffineTransform();
			at.translate(tempBullet.getMapPosX()+tempBullet.getIconSizeX()/2,tempBullet.getMapPosY()+tempBullet.getIconSizeY()/2);//position image
		
			at.rotate(Math.toRadians(tempBullet.getRotation()));
    		at.translate(-tempBullet.getIcon().getWidth()/2, -tempBullet.getIcon().getHeight()/2);//translate image to use center as point of rotation
    		// draw the image
    		((Graphics2D) g2).drawImage(tempBullet.getIcon(), at, this);
    		at.rotate(Math.toRadians(-tempBullet.getRotation()));
		
    		if(showDebug)
    		{
    			((Graphics2D) g2).draw(tempBullet.getBoundingBox());
    		}
        }
	}
	
	public void drawPlayers(Graphics g2)
	{
		g2.setColor(Color.BLACK);
		
		for (int k = 0; k < EntityKeeper.tanks.size(); k++)
        {
			tempTank = EntityKeeper.tanks.get(k);
			
			//for each tank draw it with correct orientation and location
			AffineTransform at = new AffineTransform();
			at.translate(tempTank.getMapPosX()+tempTank.getIconSizeX()/2,tempTank.getMapPosY()+tempTank.getIconSizeY()/2);//position image
		
			at.rotate(Math.toRadians(tempTank.getRotation()));
    		at.translate(-tempTank.getIcon().getWidth()/2, -tempTank.getIcon().getHeight()/2);//translate image to use center as point of rotation
    		// draw the image
    		((Graphics2D) g2).drawImage(tempTank.getIcon(), at, this);
    		at.rotate(Math.toRadians(-tempTank.getRotation()));
    		
    		double healthBarLength = (((double)tempTank.getHealth())/((double)tempTank.getMaxHealth())*25);
    		
    		if (healthBarLength > 15)//healthBarColorSelector
    			g2.setColor(new Color(0,255,0,200));
    		else if (healthBarLength > 8)
    			g2.setColor(new Color(255,255,0,200));
    		else
    			g2.setColor(new Color(255,0,0,200));
    		//Healthbar drawing
    		g2.fillRect((int)tempTank.getMapPosX(), (int)tempTank.getMapPosY()-10, (int)healthBarLength, 5);
    		g2.setColor(Color.BLACK);
    		g2.drawRect((int)tempTank.getMapPosX(), (int)tempTank.getMapPosY()-10, 25, 5);
    		
    		g2.setColor(Color.BLACK);
    		g2.setFont(new Font("Ariel", Font.PLAIN, 10));
    		g2.drawString(String.valueOf(tempTank.getPoints()), (int)tempTank.getMapPosX()+27, (int)tempTank.getMapPosY()-3);
    		
    		if(showDebug)
    		{
    			((Graphics2D) g2).draw(tempTank.getBoundingBox());
    		}
        }		
	}
	
	public void drawEffects(Graphics g2)
	{
		
		for (int k = 0; k < EntityKeeper.particles.size(); k++)
        {
			tempParticle = EntityKeeper.particles.get(k);
			
			if(tempParticle.getType().equals("effectFire"))
			{
				AffineTransform at = new AffineTransform();
				at.translate(tempParticle.getMapPosX()+tempTank.getIconSizeX()/2,
						tempParticle.getMapPosY()+tempTank.getIconSizeY()/2);//position image
		
				at.rotate(Math.toRadians(tempParticle.getRotation()));
				at.translate(0,tempTank.getIcon().getHeight()/2);//translate image to use center as point of rotation
				
    			// draw the image
				float alpha = (float)tempParticle.getHealth()/(float)tempParticle.getMaxHealth(); //calculate transparency to draw with
    			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);//set transparency
    			((Graphics2D) g2).setComposite(ac);//set transparency
    			
				((Graphics2D) g2).drawImage(tempParticle.getIcon(), at, this);
				at.rotate(Math.toRadians(-tempParticle.getRotation()));
				
				ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f);//reset transparency
        		((Graphics2D) g2).setComposite(ac);//reset transparency
				
				if(tempParticle.isDead())
				{
					EntityKeeper.particles.remove(k);
				}
				else
				{
					tempParticle.setHealth(tempParticle.getHealth()-1);
				}
			}
			
			if(tempParticle.getType().equals("effectSmoke"))
			{
				AffineTransform at = new AffineTransform();
				at.translate(tempParticle.getMapPosX()+tempTank.getIconSizeX()/2,
						tempParticle.getMapPosY()+tempTank.getIconSizeY()/2);//position image
		
				at.rotate(Math.toRadians(tempParticle.getRotation()));
				at.translate(0,-tempTank.getIcon().getHeight()/2);//translate image to use center as point of rotation
				
    			// draw the image
				float alpha = (float)tempParticle.getHealth()/(float)tempParticle.getMaxHealth(); //calculate transparency to draw with
    			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);//set transparency
    			((Graphics2D) g2).setComposite(ac);//set transparency
    			
				((Graphics2D) g2).drawImage(tempParticle.getIcon(), at, this);
				at.rotate(Math.toRadians(-tempParticle.getRotation()));
				
				ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f);//reset transparency
        		((Graphics2D) g2).setComposite(ac);//reset transparency
				
				if(tempParticle.isDead())
				{
					EntityKeeper.particles.remove(k);
				}
				else
				{
					tempParticle.setHealth(tempParticle.getHealth()-1);
				}
			}
        }
	}
	
	public void drawHUD(Graphics g2)
	{
		g2.setColor(new Color(255,255,255,200));
		g2.fillRect(2, 642, 186, 25);
		g2.setColor(Color.BLACK);
		g2.drawRect(2, 642, 186, 25);//power ups checker bar
		g2.setFont(new Font("Ariel",Font.BOLD,12));
		g2.drawString("Active Powerups:", 6, 660);
		g2.setFont(new Font("Ariel",Font.PLAIN,11));
		if(myTank.getPowerupBoost())
			g2.drawString("\u221e Boost", 120, 654);
		if(myTank.getPowerupBoost())
			g2.drawString("\u221e Ammo", 120, 664);
		
		if(myTank.getShootTimer() > 350)//shoot bar start
			g2.setColor(new Color(0,255,0,200));
		else if (myTank.getShootTimer() > 200)
			g2.setColor(new Color(255,255,0,200));
		else
			g2.setColor(new Color(255,0,0,200));
		
		g2.fillRect(190, 642, (myTank.getShootTimer()/5)*2, 25);
		g2.setColor(Color.BLACK);
		g2.drawRect(190, 642, 200, 25);
		g2.setFont(new Font("Ariel",Font.BOLD,12));
		g2.drawString("Shoot Recharge: " + String.valueOf(myTank.getShootTimer()/5) + "%", 228, 659);//shoot bar end
		
		
		if(myTank.getBoostTimer() > 350)//boost bar start
			g2.setColor(new Color(0,255,0,200));
		else if (myTank.getBoostTimer() > 200)
			g2.setColor(new Color(255,255,0,200));
		else
			g2.setColor(new Color(255,0,0,200));
		
		g2.fillRect(392, 642, (myTank.getBoostTimer()/5)*2, 25);
		g2.setColor(Color.BLACK);
		g2.drawRect(392, 642, 200, 25);
		g2.setFont(new Font("Ariel",Font.BOLD,12));
		g2.drawString("Boost Recharge: " + String.valueOf(myTank.getBoostTimer()/5) + "%", 424, 659);//boost bar end
		
		
		if(myTank.getHealth() > 50)//health bar start
			g2.setColor(new Color(0,255,0,200));
		else if (myTank.getHealth() > 25)
			g2.setColor(new Color(255,255,0,200));
		else
			g2.setColor(new Color(255,0,0,200));
		
		g2.fillRect(594, 642, myTank.getHealth()*4+2, 25);
		g2.setColor(Color.BLACK);
		g2.drawRect(594, 642, 402, 25);
		g2.setFont(new Font("Areil",Font.BOLD,12));
		g2.drawString("Tank Health: " + String.valueOf(myTank.getHealth()) + "%", 738, 659);//health bar end
		
		
		g2.setColor(new Color(255,255,255,200));
		if(guiPause.contains(mousex, mousey))
			((Graphics2D) g2).fill(guiPause);

		g2.drawImage(ImageLoader.getImage("guiPause"),guiPause.x,guiPause.y,this);
		
		if(showDebug)
		{
			g2.setColor(new Color(255,255,255,200));
			g2.fillRect(900, 21, 100, 60);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Areil",Font.BOLD,12));
			g2.drawString("Tanks: " + EntityKeeper.tanks.size(), 910, 35);
			g2.drawString("Bullets: " + EntityKeeper.bullets.size(), 910, 55);
			g2.drawString("Particles: " + EntityKeeper.particles.size(), 910, 75);
		}
	}
	
	public void drawSplashScreen(Graphics g2)
	{
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, 1000, 671);
		//g2.drawImage(ImageLoader.getImage("splashScreen"), 0, 0, this);
		g2.setColor(Color.BLACK);
		g2.drawString("Press any button to continue...", 400, 350);
		
		g2.setColor(new Color(255,255,255,100));
		((Graphics2D) g2).fill(play);
		((Graphics2D) g2).fill(controls);
		((Graphics2D) g2).fill(settings);
		
		g2.setColor(new Color(255,255,255,200));
		if(play.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(play);
		}
		else if(controls.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(controls);
		}
		else if(settings.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(settings);	
		}
		
		g2.setColor(Color.BLACK);
		((Graphics2D) g2).draw(play);
		((Graphics2D) g2).draw(controls);
		((Graphics2D) g2).draw(settings);
		
		g2.setFont(new Font("Ariel",Font.BOLD,24));
		g2.drawString("Controls",controls.x+22,controls.y+45);
		g2.drawString("Settings",settings.x+20,settings.y+45);
		
		g2.setFont(new Font("Ariel",Font.BOLD,48));
		g2.drawString("Play",play.x+50,play.y+55);
	}
	
	public void drawControlsScreen(Graphics g2)
	{
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0,1000,671);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Ariel",Font.BOLD,30));
		g2.drawString("Controls", 430, 50);
		g2.drawString("_____________", 395, 52);
		g2.drawString("WASD for movement", 340, 100);
		g2.drawString("SHIFT for boost", 370, 150);
		g2.drawString("SPACE to shoot", 370, 200);
		
		g2.setColor(new Color(0,0,0,50));
		if(guiBack.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(guiBack);	
		}
		g2.drawImage(ImageLoader.getImage("guiBack"), guiBack.x, guiBack.y, this);
		
	}
	
	public void drawSettingsScreen(Graphics g2)
	{
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0,1000,671);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Ariel",Font.BOLD,30));
		g2.drawString("Settings", 430, 50);
		g2.drawString("_____________", 395, 52);
		g2.drawString("Land Grab", guiLandGrab.x+50, guiLandGrab.y+22);
		g2.drawString("Bullet Shred", guiBulletShred.x+50, guiBulletShred.y+22);
		g2.drawString("Friendly Fire", guiFriendlyFire.x+50, guiFriendlyFire.y+22);
		g2.drawString("Team Death Match", guiTDM.x+50, guiTDM.y+22);
		
		g2.setColor(new Color(0,0,0,50));
		if(guiBack.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(guiBack);	
		}
		else if(guiLandGrab.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(guiLandGrab);	
		}
		else if(guiBulletShred.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(guiBulletShred);	
		}
		else if(guiFriendlyFire.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(guiFriendlyFire);	
		}
		else if(guiTDM.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(guiTDM);	
		}
		g2.drawImage(ImageLoader.getImage("guiBack"), guiBack.x, guiBack.y, this);
		
		if(landGrab)
		{
			g2.drawImage(ImageLoader.getImage("guiCheckedCheckbox"), guiLandGrab.x, guiLandGrab.y, this);
		}
		else
		{
			g2.drawImage(ImageLoader.getImage("guiCheckbox"), guiLandGrab.x, guiLandGrab.y, this);
		}
		
		if(bulletShred)
		{
			g2.drawImage(ImageLoader.getImage("guiCheckedCheckbox"), guiBulletShred.x, guiBulletShred.y, this);
		}
		else
		{
			g2.drawImage(ImageLoader.getImage("guiCheckbox"), guiBulletShred.x, guiBulletShred.y, this);
		}
		
		if(friendlyFire)
		{
			g2.drawImage(ImageLoader.getImage("guiCheckedCheckbox"), guiFriendlyFire.x, guiFriendlyFire.y, this);
		}
		else
		{
			g2.drawImage(ImageLoader.getImage("guiCheckbox"), guiFriendlyFire.x, guiFriendlyFire.y, this);
		}
		
		if(TDM)
		{
			g2.drawImage(ImageLoader.getImage("guiCheckedCheckbox"), guiTDM.x, guiTDM.y, this);
		}
		else
		{
			g2.drawImage(ImageLoader.getImage("guiCheckbox"), guiTDM.x, guiTDM.y, this);
		}
	}
	
	public void drawPauseScreen(Graphics g2)
	{
		display(g2);
		g2.setColor(new Color(255,255,255,100));
		g2.fillRect(0, 0, 1000, 671);
		
		g2.setColor(new Color(255,255,255,100));
		((Graphics2D) g2).fill(menuPlay);
		((Graphics2D) g2).fill(menuControls);
		((Graphics2D) g2).fill(menuSettings);
		
		g2.setColor(new Color(255,255,255,200));
		if(menuPlay.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(menuPlay);
		}
		else if(menuControls.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(menuControls);
		}
		else if(menuSettings.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(menuSettings);	
		}
		else if(guiBack.contains(mousex,mousey))
		{
			((Graphics2D) g2).fill(guiBack);	
		}
		
		g2.setColor(Color.BLACK);
		((Graphics2D) g2).draw(menuPlay);
		((Graphics2D) g2).draw(menuControls);
		((Graphics2D) g2).draw(menuSettings);
		
		g2.drawImage(ImageLoader.getImage("guiBack"), guiBack.x, guiBack.y, this);
		
		g2.setFont(new Font("Ariel",Font.BOLD,24));
		g2.drawString("Controls",menuControls.x+48,menuControls.y+45);
		g2.drawString("Settings",menuSettings.x+50,menuSettings.y+45);
		
		g2.setFont(new Font("Ariel",Font.BOLD,48));
		g2.drawString("Play",menuPlay.x+49,menuPlay.y+55);
	}
	
	public void drawGameOver(Graphics g2)
	{
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0,1000,671);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Ariel",Font.BOLD,72));
		g2.drawString("GAME OVER", 250, 350);
	}
	
	
	/***************************************** 
					Utilities
	*****************************************/
	
	public void checkForPowerUps(Tank tank1)
	{
		if(tank1.getPowerupShoot())
		{
			tank1.setShootTimer(500);
			tank1.setShootCoolDown(0);
		}
		
		if(tank1.getPowerupBoost())
		{
			tank1.setBoostTimer(500);
			tank1.setBoostCoolDown(0);
		}
		
		if (tank1.getPowerupHealth())
		{
			tank1.setHealth(100);
			tank1.setPowerupHealth(false);
		}	
	}
	
	public void addPoints(int ID, int numPoints)
	{
		for (int k = 0; k < EntityKeeper.tanks.size(); k++)
		{
			tempTank = EntityKeeper.tanks.get(k);
			
			if (TDM)
			{
				if (tempTank.getTeamID() == ID)
				{
					tempTank.addPoints(numPoints);
				}
			}
			else
			{	
				if (tempTank.getID() == ID)
				{
					tempTank.addPoints(numPoints);
				}
			}	
		}
	}
	
	public static void addParticle(double xPos, double yPos, int rotation, String type, int health)
	{
		EntityKeeper.particles.add(new Particle(xPos,yPos,rotation,type,health));
	}
	
	public static void addParticlesBy(double mapPosX, double mapPosY, int numberOfPoints, int radius, int rotation, String type, int health)
    {
    	int newX;
    	int newY;
    	
    	for(int k = 0; k < numberOfPoints; k++)
    	{
    		newX = (int)(Math.random()*radius + mapPosX - Math.random()*radius);
    		newY = (int)(Math.random()*radius + mapPosY - Math.random()*radius);
    		
    		EntityKeeper.particles.add(new Particle(newX, newY, rotation, type, health));
    	}
    }
	
	public static double getDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
	}
	
	public static double getDistance(Tank tank1, Tank tank2)
	{
		return Math.sqrt(Math.pow(tank2.getMapPosX()-tank1.getMapPosX(),2)+Math.pow(tank2.getMapPosY()-tank1.getMapPosY(),2));
	}
	
	public static void calcTimesToRun()//calculates times to run logic
	{
		now = new Date().getTime();
		passed = now - last;
		last = now;
		accumulator += passed;
	}
	
	public static void delay(double nt)//better time delay method
	{
		try {
			Thread.sleep((long)nt);
		} catch (InterruptedException e) {}
	}
	
	/***************************************** 
					Listeners
	*****************************************/
	
	public void mouseDragged(MouseEvent e)
	{
		boost = true;//working boost click
		
		if(gameStarted && !paused && !showSettings && !showControls)//in game
		{
			if(guiPause.contains(mousex,mousey))
			{
				paused = true;
			}
		}
		else if(gameStarted && paused && !showSettings && !showControls)//pause menu
		{
			if(menuPlay.contains(mousex,mousey))
			{
				showControls = false;
				showSettings = false;
				gameStarted = false;
			}
			else if(menuControls.contains(mousex,mousey))
			{
				showControls = true;
				showSettings = false;
				gameStarted = true;
			}
			else if(menuSettings.contains(mousex,mousey))
			{
				showSettings = true;
				showControls = false;
				gameStarted = true;
			}
			else if(guiBack.contains(mousex,mousey))
			{
				paused = false;
				showSettings = false;
				showControls = false;
			}
		}
		else if(showControls && !showSettings)//show controls if paused and enabled
		{
			if(guiBack.contains(mousex,mousey))
			{
				showControls = false;
			}
		}
		else if(showSettings && !showControls)//show settings if paused and enabled
		{
			if(guiBack.contains(mousex,mousey))
			{
				showSettings = false;
			}
			else if(guiLandGrab.contains(mousex,mousey))
			{
				landGrab = !landGrab;
			}
			else if(guiBulletShred.contains(mousex,mousey))
			{
				bulletShred = !bulletShred;
			}
			else if(guiFriendlyFire.contains(mousex,mousey))
			{
				friendlyFire = !friendlyFire;
			}
			else if(guiTDM.contains(mousex,mousey))
			{
				TDM = !TDM;
			}
		}
		else if(!gameStarted && !showControls && !showSettings && paused)//splash screen
		{
			if(play.contains(mousex,mousey))
			{
				paused = false;
				gameStarted = true;
				showControls = false;
				showSettings = false;
			}
			else if(controls.contains(mousex,mousey))
			{
				showControls = true;
				showSettings = false;
			}
			else if(settings.contains(mousex,mousey))
			{
				showSettings = true;
				showControls = false;
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		boost = false;//working boost click
	}
	
	public void mouseMoved(MouseEvent e) 
	{
		mousex = e.getX();
		mousey = e.getY();
	}

	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode()) 
		{ 
			case KeyEvent.VK_W:
			{		
				up = true;
				break;
			}
			case KeyEvent.VK_A:
			{		
				left = true;
				break;
			}
			case KeyEvent.VK_D:
			{		
				right = true;
				break;
			}
			case KeyEvent.VK_S:
			{		
				down = true;
				break;
			}
			case KeyEvent.VK_SHIFT:
			{		
				boost = true;
				break;
			}
			case KeyEvent.VK_SPACE:
			{		
				shoot = true;
				break;
			}
			case KeyEvent.VK_P://toggles pause state
			{	
				paused = !paused;
				break;
			}
			case KeyEvent.VK_ESCAPE://toggles pause state
			{	
				paused = !paused;
				break;
			}
			case KeyEvent.VK_F2://toggles debug
			{	
				showDebug = !showDebug;
				break;
			}
		}
	}
	
	public void keyReleased(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{ 
			case KeyEvent.VK_W:
			{		
				up = false;
				break;
			}
			case KeyEvent.VK_A:
			{		
				left = false;
				break;
			}
			case KeyEvent.VK_D:
			{		
				right = false;
				break;
			}
			case KeyEvent.VK_S:
			{		
				down = false;
				break;
			}
			case KeyEvent.VK_SHIFT:
			{		
				boost = false;
				break;
			}
			case KeyEvent.VK_SPACE:
			{		
				shoot = false;
				break;
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}
	
	public void paint(Graphics g){
		update(g);
	}

	public void keyTyped(KeyEvent e) {}
	
	public void mouseWheelMoved(MouseWheelEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
}
