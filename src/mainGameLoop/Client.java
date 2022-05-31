package mainGameLoop;

//Alec Ibarra
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import objects.Bullet;
import objects.Particle;
import objects.Powerup;
import objects.Tank;
import objects.Wall;

public class Client {

	static Socket socket;
	static BufferedReader in;
	static PrintWriter out;
	static String line;
	static String serverAddress = "";
	static int counter;
	
	static ArrayList<Tank> tanks = new ArrayList<Tank>();
	static ArrayList<Wall> walls = new ArrayList<Wall>();
	static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	static ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	static ArrayList<Particle> particles = new ArrayList<Particle>();
	
	public void run(Tank myTank) throws IOException
	{

	   while (serverAddress.isEmpty())
	   {
		   serverAddress = getServerAddress();
	   
		   line = "";
   		   socket = new Socket(serverAddress, 7778);
   		   in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	   	   out = new PrintWriter(socket.getOutputStream(),false);
	   	   counter = 0;
	   }
	   
	   in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   	   out = new PrintWriter(socket.getOutputStream(),true);
	   
	   line = "";//erase contents of line
	   
	   out.println("NAME"+myTank.getID());//Sent server client name
	   
	   for (int k = 0; k < tanks.size(); k++)
	   {
		   out.println("NEWTANK");
		   out.println(tanks.get(k).getID());
		   out.println(tanks.get(k).getMapPosX());
		   out.println(tanks.get(k).getMapPosY());
		   out.println(tanks.get(k).getRotation());
		   out.println(tanks.get(k).getHealth());
		   out.println("ENDTANK");
	   }
	   
	   for (int k = 0; k < walls.size(); k++)
	   {
		   out.println("NEWWALL");
		   out.println(walls.get(k).getMapPosX());
		   out.println(walls.get(k).getMapPosY());
		   out.println(walls.get(k).getIconSizeX());
		   out.println(walls.get(k).getIconSizeY());
		   out.println("ENDWALL");
	   }
	   
	   for (int k = 0; k < bullets.size(); k++)
	   {
		   out.println("NEWBULLET");
		   out.println(bullets.get(k).getID());
		   out.println(bullets.get(k).getMapPosX());
		   out.println(bullets.get(k).getMapPosY());
		   out.println(bullets.get(k).getRotation());
		   out.println(bullets.get(k).getHealth());
		   out.println(bullets.get(k).getVelocityX());
		   out.println(bullets.get(k).getVelocityY());
		   out.println("ENDBULLETS");
	   }
	   
	   
	   out.println("ENDALL");
	   out.flush();
	   counter++;
	   
	   while((line = in.readLine())!= null)//Process remaining received messages
	   {
		
		   //System.out.println(line);
		   if (line.startsWith("BULLET"))
		   {
			   
		   }
		   else if (line.startsWith("TANK"))
		   {
			   
		   }
		   else if (line.startsWith("PARTICLE"))
		   {
			   
		   }
	   }
	}   
	
	public String getServerAddress() 
	{
		return JOptionPane.showInputDialog("Enter IP Address of the Server:");
	}
	
	public void send(String message)
	{
		
	}
	
}
