package Model;

import java.awt.Dimension;
import java.awt.Point;
import org.w3c.dom.Element;
import View.GameSprite;

public class TowerBasic extends Tower 
{
	public static final int COST = 20;
	public static final int UPGRADE_COST = 30;
	public final static Dimension SIZE = new Dimension(84, 64);
	public TowerBasic(GameNode owner, Element e) 
	{
		super( owner, e);
		setState(ModelState.STILL);
		setShotTimer(45);
		setDamage(10);
		setRange(200);
		setBulletSpeed(500);
		setUpgradeLevel(1);
		setMaxUpgrade(3);
		m_sprite = new GameSprite(getRootNode(), "TowerBasic");
	}
	
	public TowerBasic(GameNode owner, String name)
	{
		super(owner, name);
		setState(ModelState.STILL);
		setShotTimer(45);
		setDamage(10);
		setRange(200);
		setBulletSpeed(500);
		setUpgradeLevel(1);
		setMaxUpgrade(3);
		m_sprite = new GameSprite(getRootNode(), "TowerBasic");
	}
	
	public Point getMuzzlePosition() {
		return new Point(getPosition().x+14, getPosition().y+15);
	}

	@Override
	public Dimension getImageSize() 
	{
		return SIZE;
	}

	@Override
	public String getDisplayName() {
		return "Mounted Shotty";
	}
	
	public int getActionTick()
	{
		return Integer.parseInt((String)assurePropertyValue("ActionTick", "0"));
	}
	
	public void setActionTick(int tick)
	{
		this.addProperty("ActionTick", Integer.toString(tick));
	}
	
	public int getShotTick()
	{
		return Integer.parseInt((String)assurePropertyValue("ShotTick", "0"));
	}
	
	public void setShotTick(int tick)
	{
		this.addProperty("ShotTick", Integer.toString(tick));
	}

}