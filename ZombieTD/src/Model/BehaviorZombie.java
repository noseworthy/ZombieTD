package Model;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.Rectangle;

import org.w3c.dom.Element;

public class BehaviorZombie extends Behavior 
{
	/**
	 * Enumeration to keep track of the collisions on Zombies.
	 * 
	 * @author Mike Noseworthy </br>Mitchell Chaulk
	 *
	 */
	private enum moveState{
		BLOCKED_RIGHT, //zombie is being blocked immediately to it's right
		BLOCKED_UP,    //Zombie is being blocked from above
		BLOCKED_DOWN, //Zombie is being blocked from below
		CLEAR;        //zombie is clear in all directions
	}  
	
	private final Zombie m_zombie;
	private int m_actionTick = 0;
	public static final int DEATH_REWARD = 15;
	private ArrayList<moveState> m_moveState = new ArrayList<moveState>();
	
	/**
	 * BehaviorZombie constructor which parses a XML document Element, for</br>
	 * Properties.
	 * 
	 * @param owner Zombie GameNode that is associated with this BehaviorZombie
	 * @param e XML document Element that is to be parsed for Properties
	 */
	public BehaviorZombie(GameNode owner, Element e) 
	{
		super(owner, e);
		m_zombie = (Zombie) owner;
		Integer.parseInt((String)owner.assurePropertyValue("GameTime", "0"));
		m_moveState.clear();
		m_moveState.add(moveState.CLEAR);
		this.assurePropertyValue("Type", "Model.BehaviorZombie");
	}

	/**
	 * BehaviorZombie constructor which creates a new Behavior Zombie</br>
	 * and associates it with a particular Zombie, owner
	 * 
	 * @param owner Zombie GameNode that is associated with this BehaviorZombie
	 */
	public BehaviorZombie(GameNode owner) 
	{
		super(owner);
		m_zombie = (Zombie) owner;
		Integer.parseInt((String)owner.assurePropertyValue("GameTime", "0"));
		m_moveState.clear();
		m_moveState.add(moveState.CLEAR);
		this.assurePropertyValue("Type", "Model.BehaviorZombie");
	}

	/**
	 * tick method for the BehaviorZombie checks collisions with Obstacles</br>
	 * around the Zombie and then evaluates it's state based on the collisions</br>
	 * detected and the Zombies speed.
	 * 
	 * @param gameTime - current gameTime, used to update the Zombie's position
	 */
	public void tick(int gameTime) 
	{
		int lastGameTime = this.getGameTime();
		checkCollision(m_zombie.getPosition());
		evaluateState(m_zombie.getSpeed());
		double dx = (gameTime - lastGameTime) * m_zombie.getSpeed().getXSpeed();
		double dy = (gameTime - lastGameTime) * m_zombie.getSpeed().getYSpeed();
		int x = (int) (m_zombie.getPosition().getX() + dx);
		int y = (int) (m_zombie.getPosition().getY() + dy);
		Point nextMove = new Point(x,y);
		if(m_zombie.getState() == ModelState.ACTION) attack();
		m_zombie.moveTo(nextMove);
		this.setGameTime(gameTime);
	}

	/**
	 * evaluateState is a helper method for tick. It updates the zombies</br>
	 * ModelState based on speed, and collisions with objects around the Zombie.
	 * 
	 * 	-If the zombie's health is zero, set the state to DEAD and reward the player</br>
	 * 	-If the zombie is dead, and the animation is over, set the Zombie toRemove</br>
	 *  -If the Zombie's moveState is CLEAR, then set the Zombie to move right at max speed</br>
	 *  -If the Zombie's moveState is blocked right and blocked up or down, set the Zombie to Action</br>
	 *  and start attacking the obstacle that is closest to the Zombie's right.</br>
	 *  -If the Zombie is only blocked on it's right, then pseudorandomly move up</br>
	 *  or down.
	 * 
	 * @param speed current speed of the Zombie
	 */
	private void evaluateState( SpeedVector speed )
	{
		double magnitude = Math.sqrt(Math.pow(speed.getXSpeed(), 2) + Math.pow(speed.getYSpeed(),2));
		if ( magnitude < 2 ) magnitude = 2;
		if (m_zombie.getDeadTick() >= 18)
		{
			m_zombie.setToRemove();
		}
		else if( m_zombie.isDead() && m_zombie.getDeadTick() < 18)
		{
			if (m_zombie.getState() != ModelState.DEAD) m_zombie.getRootNode().modResourcesBy(DEATH_REWARD);
			m_zombie.setState(ModelState.DEAD);
			m_zombie.setSpeed(new SpeedVector(0,0));
			m_zombie.setDeadTick(m_zombie.getDeadTick()+1);
		}
		else if(m_moveState.contains(moveState.CLEAR))
		{
			m_zombie.setState(ModelState.MOVE_RIGHT);
			m_zombie.setSpeed(new SpeedVector(2,0));
		}
		else if(m_moveState.contains(moveState.BLOCKED_RIGHT) )
		{
			if( (m_moveState.contains(moveState.BLOCKED_DOWN) || m_moveState.contains(moveState.BLOCKED_UP)) )
			{
				m_zombie.setState(ModelState.ACTION);
				m_zombie.setSpeed(new SpeedVector(0,0));
			}
			else if(m_zombie.getPosition().getY() % 2 == 0)
			{
				m_zombie.setSpeed(new SpeedVector(0, 2));
				m_zombie.setState(ModelState.MOVE_DOWN);
			}
			else 
			{
				m_zombie.setSpeed(new SpeedVector(0, -1*2));
				m_zombie.setState(ModelState.MOVE_UP);
			}
		}
	}
	
	/**
	 * CheckCollision is a helper method for EvaluateState. It checks the bounds of the Zombie for a</br>
	 * collision to the right, up or down. It generates three bounding rectangles that are slightly to the right</br>
	 * slightly above and slightly down from the zombie. If these bounding rectangles are intersecting the bounding</br>
	 * rectangle of an obstacle, then the appropriate moveState is added to the moveState ArrayList. If the Zombie</br>
	 * is bounding up or down, but not to the right, this is ignored and the moveState is set to CLEAR.
	 * 
	 * @param currentPoint currentPosition of the Zombie
	 */
	private void checkCollision(Point currentPoint) {
		m_moveState.clear();
		boolean isColliding = false;
		ArrayList<Obstacle> obstacles = getRootNode().getObstacles();
		Rectangle upperBound = new Rectangle(currentPoint.x, (int)(currentPoint.y - 5 + m_zombie.IMAGE_SIZE.height*1/3), m_zombie.IMAGE_SIZE.width, m_zombie.IMAGE_SIZE.height*1/3);
		Rectangle lowerBound = new Rectangle(currentPoint.x, (int)(currentPoint.y + 5 + m_zombie.IMAGE_SIZE.height*1/3), m_zombie.IMAGE_SIZE.width, m_zombie.IMAGE_SIZE.height*1/3);
		Rectangle rightBound = new Rectangle((int)(currentPoint.x + 5), currentPoint.y + m_zombie.IMAGE_SIZE.height*1/3 , m_zombie.IMAGE_SIZE.width, m_zombie.IMAGE_SIZE.height*1/3);
		for ( Obstacle obs : obstacles)
		{
			Rectangle obstacleBounds = obs.getModelBounds();
			if ( obstacleBounds.intersects(upperBound) || (m_zombie.getPosition().y <= 0))
			{
				m_moveState.remove(moveState.CLEAR);
				if(!m_moveState.contains(moveState.BLOCKED_UP))
					m_moveState.add(moveState.BLOCKED_UP);
				isColliding = true;
			}
			if ( obstacleBounds.intersects(lowerBound) || (m_zombie.getPosition().y + m_zombie.IMAGE_SIZE.height >= getRootNode().getGameFrame().getHeight()))
			{
				m_moveState.remove(moveState.CLEAR);
				if(!m_moveState.contains(moveState.BLOCKED_DOWN))
					m_moveState.add(moveState.BLOCKED_DOWN);
				isColliding = true;
			}
			if ( obstacleBounds.intersects(rightBound))
			{
				m_moveState.remove(moveState.CLEAR);
				if(!m_moveState.contains(moveState.BLOCKED_RIGHT))
					m_moveState.add(moveState.BLOCKED_RIGHT);
				isColliding = true;
			}
		}
		if(!isColliding || !m_moveState.contains(moveState.BLOCKED_RIGHT))
		{
			m_moveState.clear();
			if(!m_moveState.contains(moveState.CLEAR))
				m_moveState.add(moveState.CLEAR);
		}	
	}
	
	/**
	 * helper method for Tick method. Is used to attack the obstacle closest to the Zombie's right when the Zombie</br>
	 * is blocked on it's right side, as well as from above and below. 
	 */
	private void attack() 
	{
		Rectangle rightBound = new Rectangle((int)(m_zombie.getPosition().getX() + 5), (int)m_zombie.getPosition().getY() + m_zombie.IMAGE_SIZE.height*1/3 , m_zombie.IMAGE_SIZE.width, m_zombie.IMAGE_SIZE.height*1/3); 
		Obstacle target = null;
		for ( Obstacle obs : getRootNode().getObstacles())
		{
			if( rightBound.intersects(obs.getModelBounds()))
			{
				target = obs;
				break;
			}
			else target = null;
		}
		if (target == null) return;
		if(target != null && m_actionTick == 0)
		{
			target.setHealth(target.getHealth() - m_zombie.getDamage());
		}
		else if(m_actionTick < 10)
		{
			m_actionTick++;
		}
		else
		{
			m_actionTick = 0;
		}
	}
}