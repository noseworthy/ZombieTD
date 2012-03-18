package Model;

import java.awt.Dimension;
import java.awt.Point;
import org.w3c.dom.Element;
import View.GamePlayPanel;
import View.GameSprite;

public class Zombie extends Target
{	
	public Zombie(GameNode owner, Element e) 
	{
		super(owner, e);
		m_sprite = new GameSprite(owner.getRootNode(), "Zombie");
		this.setState(ModelState.STILL);
		setDamage(1);
		this.addNode(new HealthBar(this, "Health"));
	}
	
	public Zombie(GameNode owner ) 
	{
		super(owner, "Zombie");
		m_sprite = new GameSprite(owner.getRootNode(), "Zombie");
		this.setState(ModelState.STILL);
		setDamage(5);
		this.addNode(new HealthBar(this, "Health"));
	}
	
	protected void decreaseHealth(int hp)
	{
		int newHealth = getHealth() - hp;
		if ( newHealth <= 0 ) getRootNode().increaseKillCount();
		setHealth(newHealth);
	}
	
	@Override
	public Dimension getImageSize()
	{
		return new Dimension(65, 98);
	}
	
	
	@Override
	public String getDisplayName() {
		return "Zombie";
	}

	@Override
	public boolean isOnScreen() 
	{
		Point p = this.getPosition();
		if ( p.y < (-1*this.getImageSize().height) || p.x < (-1* (this.getImageSize().width + 300)) ||
			 p.y > ( this.getImageSize().height + GamePlayPanel.SIZE.height ) ||
			 p.x > (this.getImageSize().width + GamePlayPanel.SIZE.width))
		{
			return false;
		}
		else return true;
	}
	
	public Zombie clone(GameNode owner) 
	{
		Zombie zomb = new Zombie(owner);
		for ( Property p: getPropertyList().values() ) zomb.addProperty(p.getName(), p.getValue());
		zomb.addBehavior(new BehaviorZombie(zomb));
		owner.addNode(zomb);
		return zomb;
	}
	
	public int getDeadTick()
	{
		return Integer.parseInt((String)assurePropertyValue("DeadTick", "0"));
	}
	
	public void setDeadTick( int tick )
	{
		addProperty("DeadTick", Integer.toString(tick));
	}
	
	@Override
	public boolean isDead() 
	{
		if( this.getHealth() <= 0 || this.getState() == ModelState.DEAD)
		{
			return true;
		}
		else return false;
	}
}