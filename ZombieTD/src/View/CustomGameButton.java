package View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class CustomGameButton extends JComponent
{
	private Image m_image;
	private final int m_cost;
	private AbstractAction m_action;
	private Color m_bgColor =  new Color(0, 0, 0, 0);
	private Cursor m_cursorOnRelease = null;
	private final GameFrame m_gameFrame;
	private Rectangle m_buttonPos;
	
	MouseListener m_listener = new MouseListener() 
	{
		private boolean released = true;
		private Cursor hold = null;
		
		@Override
		public void mouseReleased(MouseEvent e) 
		{
			released = true;
			translateBoundsBy(-5, -5, 0, 0);
			m_action.actionPerformed(null);
			if ( m_cursorOnRelease != null && m_cost <= m_gameFrame.getRoot().getResources() ) m_gameFrame.setCursor(m_cursorOnRelease);
			repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent e) 
		{
			released = false;
			translateBoundsBy(5, 5, 0, 0);
			repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e) 
		{
			if (released && m_gameFrame.getCursor().getType() == Cursor.HAND_CURSOR && hold != null) m_gameFrame.setCursor(hold);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) 
		{
			hold = m_gameFrame.getCursor();
			if ( m_gameFrame.getCursor().getType() == Cursor.DEFAULT_CURSOR ) m_gameFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		
		@Override
		public void mouseClicked(MouseEvent e) 
		{
		}
	};
	
	public CustomGameButton( AbstractAction action, Image image, Cursor cursorOnRelease, GameFrame gFrame, int constructionCost ) 
	{
		m_cost = constructionCost;
		m_gameFrame = gFrame;
		m_cursorOnRelease = cursorOnRelease;
		m_image = image;
		m_buttonPos = new Rectangle(0,0,image.getWidth(null), image.getHeight(null));
		m_action = action;
		this.setBackground(m_bgColor);
		this.addMouseListener(m_listener);
	}
	
	public void setCursorOnRelease( Cursor onRelease ) { m_cursorOnRelease = onRelease; }
	
	public void translateBoundsBy( int x, int y, int width, int height)
	{
		if ( m_buttonPos == null ) m_buttonPos = new Rectangle(0, 0, 0, 0);
		m_buttonPos = new Rectangle(m_buttonPos.x + x, m_buttonPos.y + y, m_buttonPos.width + width, m_buttonPos.height + height);
	}
	
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		g.drawImage(m_image, m_buttonPos.x, m_buttonPos.y, m_buttonPos.width, m_buttonPos.height, new Color(50, 150, 100,0), null);
		Color c = g.getColor();
	       Font tmp = g.getFont();
	       g.setFont(new Font(tmp.getName(), tmp.getStyle(), tmp.getSize()));
	       g.setColor(Color.BLACK);
	       g.drawString("$"+Integer.toString(m_cost), this.getBounds().width/2 - 17, this.getBounds().height -5);
	       g.setColor(c);
	       g.setFont(tmp);
	}
	
}
