package Model;

import org.w3c.dom.Element;

public class BehaviorWave extends Behavior
{

	public BehaviorWave(GameNode owner, Element e) 
	{
		super(owner, e);
		this.addProperty("Type", "Wave");
	}

	/**
	 * Currently does nothing. However it is still being called in the case that we add implementation here at some point
	 * in the future.
	 */
	@Override
	public void tick(int gameTime) 
	{
	}

}
