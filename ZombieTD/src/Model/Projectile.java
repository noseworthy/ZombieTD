package Model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.w3c.dom.Element;

import View.GameSprite;

public class Projectile extends GenericModel
{
	
	public Projectile(Tower owner, Point start_pos, SpeedVector bulletSpeed) 
	{
		super(owner);
		setState(ModelState.ACTION);
		this.addProperty("Type", "Model.Projectile");
		setPosition(start_pos);
		setSpeed(bulletSpeed);
		m_sprite = new GameSprite(this.getRootNode(), "Projectile");
		addBehavior(new BehaviorProjectile(this));
	}
	
	public Projectile(GameNode owner, Element e) 
	{
		super(owner, e);
		setState(ModelState.ACTION);
		this.addProperty("Type", "Model.Projectile");
		m_properties.put("Type", new Property(this, "Type", "Projectile"));
		m_sprite = new GameSprite(this.getRootNode(), "Projectile");
	}


	@Override
	protected Rectangle getModelBounds() 
	{
		ModelState modelState = getState();
		int height = getRootNode().getImageBank().getImage("Projectile", modelState, 0).getHeight(null);
		int width = getRootNode().getImageBank().getImage("Projectile", modelState, 0).getWidth(null);
		return new Rectangle(getPosition().x, getPosition().y, width, height);
	}

	@Override
	public Dimension getImageSize() 
	{
		return new Dimension(7,7);
	}

	@Override
	public String getDisplayName() {
		return "Bullet";
	}
	
	
}
