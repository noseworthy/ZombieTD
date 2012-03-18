package Model;

import org.w3c.dom.Element;

public abstract class Obstacle extends GenericModel {

	public Obstacle(GameNode owner) {
		super(owner);
	}
	
	public Obstacle(GameNode owner, Element e) {
		super(owner, e);
	}
	
	
	public Obstacle(GameNode owner, String name) {
		super(owner, name);
	}

}
