package Model;

import org.w3c.dom.Element;

/**
 * Extension of the Behavior class to handle the behaviors of Obstacles
 * 
 * @author michaelnoseworthy
 *
 */
public class BehaviorObstacle extends Behavior 
{
	private Obstacle m_Obstacle;
	private int m_deadTick = 0;
	
	/**
	 * Constructor for the BehaviorObstacle that keeps track of its Owner and sets </br>
	 * the Type Property to BehaviorObstacle.
	 * 
	 * @param owner GameNode that this BehaviorObstacle belongs to
	 */
	public BehaviorObstacle(GameNode owner)
	{
		super(owner);
		m_Obstacle = (Obstacle)owner;
		this.assurePropertyValue("Type", "Model.BehaviorObstacle");
	}

	/**
	 * Constructor for the BehaviorObstacle that keeps track of it's owner, sets</br>
	 * the Type parameter to BehaviorObstacle, and parses the XML document </br>
	 * Element for other Properties to be set.
	 *
	 * @param owner the GameNode that this BehaviorObstacle belongs to
	 * @param e XML document Element that is parsed for Properties to add
	 */
	public BehaviorObstacle(GameNode owner, Element e) 
	{
		super(owner, e);
		m_Obstacle = (Obstacle)owner;
		this.assurePropertyValue("Type", "Model.BehaviorObstacle");
	}

	/**
	 * 
	 * tick method for the BehaviorObstacle, ensures that Obstacles stay in the ModelState</br>
	 * STILL, until their health equals zero. Once the Health property decreases to zero </br>
	 * the Obstacles ModelState is set to DEAD and a Death animation plays.
	 * 
	 * @param gameTime the current game time in ticks
	 */
	public void tick(int gameTime) 
	{
		if (m_Obstacle.getHealth() > 0){
			m_Obstacle.setState(ModelState.STILL);
			m_Obstacle.moveTo(m_Obstacle.getPosition());
		}
		else
		{
			m_Obstacle.setState(ModelState.DEAD);
			if(m_deadTick >= 6)
			{
				m_Obstacle.setToRemove();
			}
			m_deadTick++;
		}
	}
}