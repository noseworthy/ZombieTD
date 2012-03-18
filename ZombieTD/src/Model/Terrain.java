package Model;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.w3c.dom.Element;

public class Terrain extends GenericModel 
{
	public Terrain(GameNode owner, Element e) 
	{
		super(owner, e);
	}
	
	public Terrain(GameNode owner, String name)
	{
		super(owner, name);
	}

	@Override
	protected Rectangle getModelBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getImageSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
	

}