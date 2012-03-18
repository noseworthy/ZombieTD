package View;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import Interfaces.GameSpriteI;
import Interfaces.RootModelI;
import Model.GenericModel;
import Model.ModelState;

public class GameSprite implements GameSpriteI
{
	private ModelState m_lastState = ModelState.STILL;
	int m_index = 0;
	private final RootModelI m_root;
	private final String m_spriteType;
	private BufferedImage m_currentImage = null;

	public GameSprite(RootModelI root, String type) 
	{
		m_root = root;
		m_spriteType = type;
		
	}
	
	public void draw(GenericModel model, Graphics g) 
	{
		if 
			( m_lastState != model.getState() )
		{
			m_index = 0;
			m_lastState = model.getState();
		}
		if ( m_index >= m_root.getImageBank().getSpriteImageNumber(m_spriteType, model.getState()) ) m_index = 0;
		int x = (int) ( model.getPosition().getX());
		int y = (int) ( model.getPosition().getY());
		m_currentImage = m_root.getImageBank().getImage(m_spriteType, model.getState(), m_index);
		g.drawImage(m_currentImage, x, y, this);
	
		m_index++;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
	{
		//TEMP
		if (infoflags != ALLBITS)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public BufferedImage getCurrentImage() 
	{
		return m_currentImage;
	}

}