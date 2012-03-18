package Model;

import java.awt.Dimension;
import org.w3c.dom.Element;
import View.GameSprite;

public class ObstacleBasic extends Obstacle
{
	public static final int COST = 10;
	public final static Dimension SIZE = new Dimension(71, 71);
	public ObstacleBasic(GameNode owner) {
		super(owner);
		setSpeed(new SpeedVector(0,0));
		setState(ModelState.STILL);
		setHealth(100);
		m_sprite = new GameSprite(getRootNode(), "ObstacleBasic");
		addBehavior(new BehaviorObstacle(m_owner));
	}
	
	public ObstacleBasic(GameNode owner, Element e) {
		super(owner, e);
		m_sprite = new GameSprite(getRootNode(), "ObstacleBasic");
	}
	
	public ObstacleBasic(GameNode owner, String name) {
		super(owner, name);
		setSpeed(new SpeedVector(0,0));
		setState(ModelState.STILL);
		setHealth(100);
		m_sprite = new GameSprite(getRootNode(), "ObstacleBasic");
	}
	
	protected void decreaseHealth(int hp)
	{
		int newHealth = getHealth() - hp;
		setHealth(newHealth);
	}
	
	@Override
	public Dimension getImageSize() {
		return SIZE;
	}
	
	@Override
	public String getDisplayName() {
		return "Fence";
	}
	
	

}
