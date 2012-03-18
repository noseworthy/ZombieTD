package Model;

import java.awt.Point;

import org.w3c.dom.Element;

public abstract class Tower extends GenericModel 
{
	public Tower(GameNode owner, Element e)
	{
		super(owner, e);
	}
	
	public Tower(GameNode owner, String name) 
	{
		super(owner, name);
	}

	public int getShotTimer() {
		assurePropertyValue("ShotTimer", 0);
		int shotTimer = Integer.parseInt(this.getPropertyList().get("ShotTimer").getValue().toString());
		return shotTimer;
	}
	
	public void setShotTimer(int timer)
	{
		addProperty("ShotTimer", Integer.toString(timer));
	}
	
	public int getDamage()
	{
		return Integer.parseInt((String)assurePropertyValue("Damage", "0"));
	}
	
	public void setDamage(int damage)
	{
		addProperty("Damage", Integer.toString(damage));
	}
	
	public int getRange()
	{
		return Integer.parseInt((String)assurePropertyValue("Range", "0"));
	}
	
	public void setRange(int range)
	{
		addProperty("Range", Integer.toString(range));
	}
	
	public int getBulletSpeed()
	{
		return Integer.parseInt((String)assurePropertyValue("BulletSpeed", "0"));
	}
	
	public void setBulletSpeed(int speed)
	{
		addProperty("BulletSpeed", Integer.toString(speed));
	}
	
	public abstract Point getMuzzlePosition();
	
	protected void setMaxUpgrade(int maxLevel)
	{
		assurePropertyValue("MaxUpgrade", maxLevel);
		setProperty("MaxUpgrade", Integer.toString(maxLevel));
	}

	public int getMaxUpgrade() {
		assurePropertyValue("MaxUpgrade", 3);
		int shotTimer = Integer.parseInt(this.getPropertyList().get("MaxUpgrade").getValue().toString());
		return shotTimer;
	}
	
}