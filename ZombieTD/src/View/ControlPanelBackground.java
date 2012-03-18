package View;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ControlPanelBackground extends JComponent
{
	public final Image BACKGROUND;
	
	public ControlPanelBackground(Image background) 
	{
		BACKGROUND = background;
		this.setVisible(true);
		this.setBounds(0, 0, background.getWidth(null), background.getHeight(null));
	}
	
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		g.drawImage(BACKGROUND, 0, 0, this.getWidth(), this.getHeight(), null);
	}

}
