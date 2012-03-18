package Model;

import org.w3c.dom.Element;

public abstract class Target extends GenericModel 
{
	public Target(GameNode owner, Element e)
	{
		super(owner, e);
	}
	
	public Target(GameNode owner, String name)
	{
		super(owner, name);
	}

}