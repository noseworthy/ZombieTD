package Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.w3c.dom.Element;

public class HealthBar extends GenericModel
{
	private static final Dimension SIZE = new Dimension(20, 5);
	
	public HealthBar(GameNode owner, String name)
	{
		super(owner, name);
		owner.assurePropertyValue("Health", "100");
		this.addProperty(owner.getPropertyList().get("Health"));
		this.addProperty(new Property(this, "Type", "HealthBar"));
	}
	
	public HealthBar(GameNode owner, Element e)
	{
		super(owner, e);
		this.addProperty(owner.getPropertyList().get("Health"));
	}
	
	@Override
	public void paintSelf(Graphics g) 
	{
		int max = Integer.parseInt((String)m_owner.assurePropertyValue("MaxHealth", "100"));
		Color hold = g.getColor();
		int x = ((GenericModel)m_owner).getPosition().x; 
		int y = ((GenericModel)m_owner).getPosition().y;
		double health = this.getHealth();
		g.setColor(Color.RED);
		((Graphics2D)g).fill(new Rectangle(x, y, SIZE.width, SIZE.height));
		g.setColor(Color.GREEN);
		int w = (int) (SIZE.width*(health/max));
		
		((Graphics2D)g).fill(new Rectangle(x, y, w, SIZE.height));
		
		g.setColor(hold);
		
	}

	@Override
	protected Rectangle getModelBounds() {
		return null;
	}

	@Override
	public Dimension getImageSize() {
		return SIZE;
	}

	@Override
	public String getDisplayName() {
		return "Health";
	}


}
