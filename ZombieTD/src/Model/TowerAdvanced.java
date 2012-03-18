package Model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.w3c.dom.Element;

public class TowerAdvanced extends Tower 
{

	public TowerAdvanced(GameNode owner, Element e) 
	{
		super( owner, e);
	}

	public Point getMuzzlePosition() {
		// TODO Auto-generated method stub
		return null;
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