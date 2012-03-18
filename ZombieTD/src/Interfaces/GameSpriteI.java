package Interfaces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import Model.GenericModel;

public interface GameSpriteI extends ImageObserver
{
	/**
	 * 
	 * @param model - the model owning the sprite
	 * @param g - the graphics object used to draw the model
	 */
	public void draw(GenericModel model, Graphics g);
	
	/**
	 * 
	 * @return returns the last sprite that was drawn by the GameSpriteI implementer.
	 */
	public BufferedImage getCurrentImage();

}