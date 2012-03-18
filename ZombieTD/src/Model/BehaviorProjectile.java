package Model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 * Behavior Class for the Projectile class.
 * @author Mike Noseworthy </br> Mitchell Chaulk
 *
 */
public class BehaviorProjectile extends Behavior {
	
	private Projectile m_projectile;

	/**
	 * Constructor for the BehaviorProjectile
	 * @param owner
	 */
	public BehaviorProjectile(GenericModel owner) 
	{
		super(owner);
		m_projectile = (Projectile)owner;
		this.assurePropertyValue("Type", "Model.BehaviorProjectile");
	}
	
	public BehaviorProjectile(GameNode owner, Element e) 
	{
		super(owner, e);
		m_projectile = (Projectile)owner;
		this.assurePropertyValue("Type", "Model.BehaviorProjectile");
	}

	@Override
	public void tick(int gameTime) 
	{
		hitZombie();
		int lastGameTime = this.getRootNode().getGameTime();
		
		if(!(m_projectile.isOnScreen()))
		{
			m_projectile.setToRemove();
			return;
		}
		if 
			(!m_projectile.isToRemove())
		{
			SpeedVector speed = m_projectile.getSpeed();
			double dx = (gameTime - lastGameTime) * speed.getXSpeed();
			double dy = (gameTime - lastGameTime) * speed.getYSpeed();
			
			int x = (int) (m_projectile.getPosition().getX() + dx);
			int y = (int) (m_projectile.getPosition().getY() + dy);
			m_projectile.moveTo(new Point(x, y));
			this.setGameTime(gameTime);
		}
	}
	
	public void hitZombie()
	{
		ArrayList<Zombie> zombies = getRootNode().getZombies();
		for(Zombie zomb: zombies){
			Rectangle zomb_bounds = zomb.getModelBounds();
			if((zomb_bounds.contains(m_projectile.getPosition())) && (zomb.getHealth() > 0))
			{
				zomb.decreaseHealth(((Tower)m_projectile.getOwner()).getDamage());
				m_projectile.setToRemove();
				return;
			}
			
		}
	}

}
