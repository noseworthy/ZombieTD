package Model;

import org.w3c.dom.Element;

/**
 * Behavior class is an abstract class that controls what each GameNode does on a tick call.</br>
 * Each GameNode should have at least one Behavior. Every behavior knows the gameRootNode</br>
 * and the GameNode that owns it.
 * 
 * @author Mike Noseworthy</br>Mitchell Chaulk
 *
 */
public abstract class Behavior extends GameNode {
	protected final GameRootNode m_gameRootNode;

	/**
	 * Constructor for Behavior class that uses an XML document Element</br>
	 * for construction.
	 * 
	 * @param owner the GameNode that this behavior belongs to
	 * @param e XML document Element that is used to add Properties to the </br>
	 *        GameNode.
	 */
	public Behavior(GameNode owner, Element e) 
	{
		super(owner, e);
		m_gameRootNode = owner.getRootNode();
	}
	
	/**
	 * constructor for Behavior that doesn't set any properties and </br>
	 * associates itself with the GameNode that owns it
	 * @param owner the GameNode that this Behavior belongs to.
	 */
	public Behavior(GameNode owner)
	{
		super(owner);
		m_gameRootNode = owner.getRootNode();
	}	

	/**
	 * tick method for the Behavior class, an abstract function that should be implemented so that it</br>
	 * performs the appropriate action on each tick for it's owning GameNode. Ticks should update </br>
	 * any observables on each tick call, and manage the GameNode's state.
	 * 
	 * @param aGameTime the current time of the game's execution, in ticks
	 */
	public abstract void tick(int aGameTime);
}