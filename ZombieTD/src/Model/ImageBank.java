package Model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageBank 
{
	private HashMap<ModelState, ArrayList<BufferedImage>> m_zombie_map = new HashMap<ModelState, ArrayList<BufferedImage>>();
	private HashMap<ModelState, ArrayList<BufferedImage>> m_projectile_map = new HashMap<ModelState, ArrayList<BufferedImage>>();
	private HashMap<ModelState, ArrayList<BufferedImage>> m_TowerBasic_map = new HashMap<ModelState, ArrayList<BufferedImage>>();
	private HashMap<ModelState, ArrayList<BufferedImage>> m_ObstacleBasic_map = new HashMap<ModelState, ArrayList<BufferedImage>>();

	private ArrayList<BufferedImage> m_TowerBasicCursors = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> m_obstacleBasicCursors = new ArrayList<BufferedImage>();


	public ImageBank()
	{
		try{
			ArrayList<BufferedImage> zombie1_DEAD = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> zombie1_UP = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> zombie1_DOWN = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> zombie1_action = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> walk_left = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> walk_right = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> still = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> bullet = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> TowerBasic_STILL = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> TowerBasic_ACTION = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> ObstacleBasic_STILL = new ArrayList<BufferedImage>();
			ArrayList<BufferedImage> ObstacleBasic_DEAD = new ArrayList<BufferedImage>();
			bullet.add(ImageIO.read(new File("./src/Resources/Bullets/bullets.png")));
			for ( int i = 0 ; i < 10; i++)
			{
				zombie1_DOWN.add(ImageIO.read(new File("./src/Resources/Zombie1_walk_right/"+i+".png")));
				zombie1_UP.add(ImageIO.read(new File("./src/Resources/Zombie1_walk_right/"+i+".png")));
				walk_left.add(ImageIO.read(new File("./src/Resources/Zombie1_walk_left/"+i+".png")));
				walk_right.add(ImageIO.read(new File("./src/Resources/zombie1_walk_right/"+i+".png")));
				still.add(ImageIO.read(new File("./src/Resources/zombie1_STILL/"+i+".png")));
			}
			for ( int i = 0 ; i <= 11; i++)
			{
				TowerBasic_ACTION.add(ImageIO.read(new File("./src/Resources/TowerBasic_ACTION/"+i+".png")));
			}
			for (int i = 0 ; i <= 18; i++)
			{
				zombie1_DEAD.add(ImageIO.read(new File("./src/Resources/Zombie1_DEAD/"+i+".png")));
			}
			for (int i = 0 ; i <= 10; i++)
			{
				zombie1_action.add(ImageIO.read(new File("./src/Resources/zombie1_action/"+i+".png")));
			}
			for (int i = 0 ; i <= 20; i++)
			{
				ObstacleBasic_STILL.add(ImageIO.read(new File("./src/Resources/fence1/"+i+".png")));
			}
			for (int i = 0 ; i <= 5; i++)
			{
				ObstacleBasic_DEAD.add(ImageIO.read(new File("./src/Resources/Fence1_DEAD/"+i+".png")));
			}
			TowerBasic_STILL.add(ImageIO.read(new File("./src/Resources/TowerBasic_STILL/0.png")));
			m_zombie_map.put(ModelState.MOVE_LEFT, walk_left);
			m_zombie_map.put(ModelState.MOVE_RIGHT, walk_right);
			m_zombie_map.put(ModelState.MOVE_UP, zombie1_UP);
			m_zombie_map.put(ModelState.MOVE_DOWN, zombie1_DOWN);
			m_zombie_map.put(ModelState.STILL, still);
			m_zombie_map.put(ModelState.ACTION, zombie1_action);
			m_zombie_map.put(ModelState.DEAD, zombie1_DEAD);
			m_projectile_map.put(ModelState.ACTION, bullet);
			m_projectile_map.put(ModelState.STILL, bullet);
			m_TowerBasic_map.put(ModelState.STILL, TowerBasic_STILL);
			m_TowerBasic_map.put(ModelState.ACTION, TowerBasic_ACTION);
			m_ObstacleBasic_map.put(ModelState.STILL, ObstacleBasic_STILL);
			m_ObstacleBasic_map.put(ModelState.DEAD, ObstacleBasic_DEAD);
			m_TowerBasicCursors.add(ImageIO.read(new File("./src/Resources/TowerBasic_STILL/CURSOR0.png")));
			m_TowerBasicCursors.add(ImageIO.read(new File("./src/Resources/TowerBasic_STILL/CURSOR1.png")));
			m_obstacleBasicCursors.add(ImageIO.read(new File("./src/Resources/Fence1/CURSOR0.png")));
			m_obstacleBasicCursors.add(ImageIO.read(new File("./src/Resources/Fence1/CURSOR1.png")));
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public BufferedImage getZombieImage(ModelState state, int i)
	{
		if ( m_zombie_map.get(state) == null)
		{
			System.err.println("GameSprite does not contain an image path for " + state);
			return null;
		}
		return m_zombie_map.get(state).get(i);
	}
	
	public BufferedImage getProjectileImage(ModelState state, int i)
	{
		if ( m_projectile_map.get(state) == null)
		{
			System.err.println("GameSprite does not contain an image path for " + state);
			return null;
		}
		return m_projectile_map.get(state).get(0);
	}

	public BufferedImage getImage(String type, ModelState state, int index) 
	{		
		if ( type.compareToIgnoreCase("Zombie") == 0 ) return getZombieImage(state, index);
		else if ( type.compareToIgnoreCase("Projectile") == 0 ) return getProjectileImage(state, index);
		else if ( type.compareToIgnoreCase("TowerBasic") == 0 ) return getTowerBasicImage(state, index);
		else if ( type.compareToIgnoreCase("ObstacleBasic") == 0 ) return getObstacleBasicImage(state, index);
		return null;
	}

	public BufferedImage getTowerBasicImage(ModelState state, int index) {
		if ( m_TowerBasic_map.get(state) == null)
		{
			System.err.println("GameSprite does not contain an image path for " + state);
			return null;
		}
		if (state == ModelState.STILL){ index =0 ;}
		return m_TowerBasic_map.get(state).get(index);
	}
	
	public BufferedImage getObstacleBasicImage(ModelState state, int index) {
		if ( m_ObstacleBasic_map.get(state) == null)
		{
			System.err.println("GameSprite does not contain an image path for " + state);
			return null;
		}
		return m_ObstacleBasic_map.get(state).get(index);
	}

	public int getSpriteImageNumber(String type, ModelState state) 
	{
		if ( type.compareToIgnoreCase("Zombie") == 0 ) return m_zombie_map.get(state).size();
		else if ( type.compareToIgnoreCase("Projectile") == 0 ) return m_projectile_map.get(state).size();
		else if ( type.compareToIgnoreCase("TowerBasic") == 0 ) return m_TowerBasic_map.get(state).size();
		else if ( type.compareToIgnoreCase("ObstacleBasic") == 0 ) return m_ObstacleBasic_map.get(state).size();
		else return 0;
	}
	
	public BufferedImage getTowerBasicCursor(int index)
	{
		return m_TowerBasicCursors.get(index);
	}
	
	public BufferedImage getObstacleBasicCursor(int index)
	{
		return m_obstacleBasicCursors.get(index);
	}
}
