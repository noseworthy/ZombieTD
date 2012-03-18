package Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;
import View.GamePlayPanel;

import Interfaces.GameSpriteI;

public abstract class GenericModel extends GameNode 
{
	public final Dimension IMAGE_SIZE = getImageSize();
	protected GameSpriteI m_sprite;
	
	public GenericModel(GameNode owner, Element e) 
	{
		super(owner, e);
		
		if ( !m_properties.containsKey("Selected") ) this.addProperty("Selected", "False");
	}

	public GenericModel( GameNode owner, String name)
	{
		super(owner);
		this.addProperty("Name", name);
		if ( !m_properties.containsKey("Selected") ) this.addProperty("Selected", "False");
	}
	
	public GenericModel( GameNode owner)
	{
		super(owner);
		if ( !m_properties.containsKey("Selected") ) this.addProperty("Selected", "False");
	}

	public void select()
	{
		if ( !m_properties.containsKey("Selected") ) this.addProperty("Selected", "True");
		m_properties.get("Selected").setValue("True");
	}
	
	public void unSelect()
	{
		if ( !m_properties.containsKey("Selected") ) this.addProperty("Selected", "False");
		m_properties.get("Selected").setValue("False");
	}
	
	public boolean isSelected()
	{
		return Boolean.parseBoolean((String) this.assurePropertyValue("Selected", "False"));
	}
	
	public boolean contains( Point p )
	{
		return this.getModelBounds().contains(p);
	}
	
	public abstract Dimension getImageSize();
	
	public String getName() 
	{
		return (String)assurePropertyValue("Name", "No Name");
	}

	
	public boolean intersects(Rectangle2D worldPoint) 
	{
		Point p = this.getPosition();
		Rectangle bounds = new Rectangle(p.x, p.y, this.getImageSize().width, this.getImageSize().height);
		return bounds.intersects(worldPoint);
	}

	public void paintSelf(Graphics g) 
	{ 
		if ( this.isToRemove() ){
			return;
		}
		else{
			if (m_sprite!=null)m_sprite.draw(this, g);
			
			Color tmp = g.getColor();
			g.setColor(Color.RED);
			if (this.isSelected()) 
			{
				g.fillOval(this.getPosition().x + IMAGE_SIZE.width/2, this.getPosition().y, 5, 5);
			}
			g.setColor(tmp);
			
			for ( GameNode n: m_nodes )
			{
				try
				{
					((GenericModel)n).paintSelf(g);
				}
				catch (Throwable th) {}
			}
		}
	}

	public abstract String getDisplayName();
	
	public int getUpgradeLevel() {
		assurePropertyValue("Level", 1);
		int level = Integer.parseInt(this.getPropertyList().get("Level").getValue().toString());
		return level;
	}
	
	public void setUpgradeLevel(int level)
	{
		String toSet = Integer.toString(level);
		this.setProperty("Level", toSet);
	}
	
	public void setDamage(int damage)
	{
		String toSet = Integer.toString(damage);
		this.setProperty("Damage", toSet);
	}
	
	public Point getPosition() 
	{
		String p = (String)assurePropertyValue("Position", "0 0");
		int x = Integer.parseInt(p.substring(0, p.indexOf(' ')));
		int y = Integer.parseInt(p.substring(p.indexOf(' ')+1, p.length()));
		return new Point(x,y);
	}

	public void setPosition(Point aP) 
	{
		String toSet = Integer.toString(aP.x) + ' ' + Integer.toString(aP.y);
		this.setProperty("Position", toSet);
	}
	
	public void setHealth(int hp) 
	{
		String toSet = Integer.toString(hp);
		this.setProperty("Health", toSet);
		HealthBar hb = getHealthBar();
		if ( hb != null && hb.getProperty("Health") != this.getProperty("Health") ) hb.addProperty(this.getProperty("Health"));
	}

	public SpeedVector getSpeed()
	{
		String p = (String) assurePropertyValue("Speed", "0.0 0.0");
		double x = Double.parseDouble(p.substring(0, p.indexOf(' ')));
		double y = Double.parseDouble(p.substring(p.indexOf(' ')+1, p.length()));
		return new SpeedVector(x, y);
	}
	
	public String getType()
	{
		String type = (String) assurePropertyValue("Type", "none");
		return type;
	}
	
	public void setSpeed(SpeedVector speed)
	{
		String toSet = Double.toString(speed.getXSpeed()) + ' ' + Double.toString(speed.getYSpeed());
		this.addProperty("Speed", toSet);
	}

	/**
	 * A method to move a node to another position.
	 * <p> Checks if aP is on the GameFrame, and overwrites or creates a
	 *     Property called "Position" with the point as a value.
	 * @param aP	- the point the node should be moved to.
	 * 
	 * @author Mike
	 */
	public void moveTo(Point aP) 
	{
		this.setPosition(aP);
		if (!this.isOnScreen() )
			{
				try
				{
					Zombie zomb = (Zombie)this;
					if (zomb.getHealth() > 0){
						getRootNode().decreasePlayerHealth(1);
					}
				}
				catch(Throwable e)
				{
					//DO NOTHING
				}
				setToRemove();
			}
	}

	/**
	 * a method which returns a bool indicating whether the unit is still alive
	 * 
	 * @return - false if unit state is not ModelState.DEAD<p>
	 * 		   - true if unit state is ModelState.DEAD
	 * 
	 * @author Mike
	 */
	public boolean isDead() 
	{
		if ( getState() == ModelState.DEAD)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public int getDamage()
	{
		assurePropertyValue("Damage", 0);
		int damage = Integer.parseInt(this.getPropertyList().get("Damage").getValue().toString());
		return damage;
	}

	public int getHealth() 
	{
		assurePropertyValue("Health", 0);
		int health = Integer.parseInt(this.getPropertyList().get("Health").getValue().toString());
		return health;
	}

	public HealthBar getHealthBar()
	{
		for ( GameNode n: m_nodes )
		{
			try
			{
				return (HealthBar)n;
			}
			catch (Throwable th)
			{
				//Do nothing
			}
		}
		return null;
	}
	
	public int getMaxHealth() 
	{
		assurePropertyValue("MaxHealth", 0);
		int maxHealth = Integer.parseInt( this.getPropertyList().get("MaxHealth").toString() );
		return maxHealth;
	}
	
	public ModelState getState()
	{
		String state = (String) assurePropertyValue("ModelState", "STILL");
		if ( state.compareToIgnoreCase("STILL") == 0 ) return ModelState.STILL;
		else if ( state.compareToIgnoreCase("ACTION") == 0 ) return ModelState.ACTION;
		else if ( state.compareToIgnoreCase("ACTION_INJURED") == 0 ) return ModelState.ACTION_INJURED;
		else if ( state.compareToIgnoreCase("DEAD") == 0 ) return ModelState.DEAD;
		else if ( state.compareToIgnoreCase("HIT") == 0 ) return ModelState.HIT;
		else if ( state.compareToIgnoreCase("INJURED") == 0 ) return ModelState.INJURED;
		else if ( state.compareToIgnoreCase("MOVE_LEFT") == 0 ) return ModelState.MOVE_LEFT;
		else if ( state.compareToIgnoreCase("MOVE_LEFT_INJURED") == 0 ) return ModelState.MOVE_LEFT_INJURED;
		else if ( state.compareToIgnoreCase("MOVE_RIGHT") == 0 ) return ModelState.MOVE_RIGHT;
		else if ( state.compareToIgnoreCase("MOVE_RIGHT_INJURED") == 0 ) return ModelState.MOVE_RIGHT_INJURED;
		else if ( state.compareToIgnoreCase("MOVE_UP") == 0 ) return ModelState.MOVE_UP;
		else if ( state.compareToIgnoreCase("MOVE_DOWN") == 0 ) return ModelState.MOVE_DOWN;
		else 
		{
			System.err.println("ModelState not recognized for " + this.getName() + ". Returning STILL.");
			return ModelState.STILL;
		}
	}
	
	public void setState(ModelState state)
	{
		if ( state == ModelState.STILL ) this.addProperty("ModelState", "STILL");
		else if ( state == ModelState.ACTION ) this.addProperty("ModelState", "ACTION");
		else if ( state == ModelState.ACTION_INJURED ) this.addProperty("ModelState", "ACTION_INJURED");
		else if ( state == ModelState.DEAD ) this.addProperty("ModelState", "DEAD");
		else if ( state == ModelState.HIT ) this.addProperty("ModelState", "HIT");
		else if ( state == ModelState.INJURED ) this.addProperty("ModelState", "INJURED");
		else if ( state == ModelState.MOVE_LEFT ) this.addProperty("ModelState", "MOVE_LEFT");
		else if ( state == ModelState.MOVE_LEFT_INJURED ) this.addProperty("ModelState", "MOVE_LEFT_INJURED");
		else if ( state == ModelState.MOVE_RIGHT ) this.addProperty("ModelState", "MOVE_RIGHT");
		else if ( state == ModelState.MOVE_RIGHT_INJURED ) this.addProperty("ModelState", "MOVE_RIGHT_INJURED");
		else if ( state == ModelState.MOVE_DOWN ) this.addProperty("ModelState", "MOVE_DOWN");
		else if ( state == ModelState.MOVE_UP ) this.addProperty("ModelState", "MOVE_UP");
		else 
		{
			System.err.println("ModelState " + state + " not recognized for " + this.getName() + ". Setting state as STILL.");
		}
	}
	
	protected Rectangle getModelBounds()
	{
		return new Rectangle(this.getPosition().x, this.getPosition().y, IMAGE_SIZE.width, IMAGE_SIZE.height);
	}

	public BufferedImage getCurrentImage()
	{
		return m_sprite.getCurrentImage();
	}
	
	public Point getCenter() {
		Point center = getPosition();
		center.x = center.x + IMAGE_SIZE.width;
		center.y = center.y + IMAGE_SIZE.height;
		return center;
	}
	
	public boolean isOnScreen()
	{
		Point p = this.getPosition();
		if ( p.y < (-1*this.getImageSize().height) || p.x < (-1*this.getImageSize().width) ||
			 p.y > ( this.getImageSize().height + GamePlayPanel.SIZE.height ) ||
			 p.x > (this.getImageSize().width + GamePlayPanel.SIZE.width))
			return false;
		else return true;
	}
}