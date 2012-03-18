package Model;

import java.awt.Dimension;

import org.w3c.dom.Element;

public class UserAvatar extends Target 
{

	public UserAvatar(GameNode owner, Element e) 
	{
		super(owner, e);
	}
	
	public UserAvatar(GameNode owner, String name)
	{
		super(owner, name);
	}

	@Override
	public Dimension getImageSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "You";
	}
}