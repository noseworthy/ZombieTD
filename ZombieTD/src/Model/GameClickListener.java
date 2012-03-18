package Model;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import View.GamePlayPanel;
import View.GameFrame;
import Interfaces.GameClickListenerI;

public class GameClickListener implements GameClickListenerI
{
	private final GamePlayPanel m_gamePlayPanel;
	private final GameFrame m_gameFrame;
	private final ArrayList<GenericModel> m_selected = new ArrayList<GenericModel>();
	private boolean m_canBuild = true;
	private Cursor m_cursorHold = null;
	
	public GameClickListener( GameFrame gameFrame ) 
	{
		m_gamePlayPanel = gameFrame.getGamePlayPanel();
		m_gameFrame = gameFrame;
		
	}
	
	public void mouseClicked(MouseEvent evt)
	{
	}
	
	public void mousePressed(MouseEvent evt) 
	{
	}
	
	public void mouseReleased(MouseEvent evt) 
	{
		
		if (MouseEvent.BUTTON3 == evt.getButton())
		{
			m_gameFrame.getGamePlayPanel().flagToBuild("", "",null, 0);
			m_cursorHold = null;
			m_gameFrame.setCursor(Cursor.getDefaultCursor());
		}
		else
		{
			Point p = getWorldPoint(evt);
			ArrayList<GenericModel> tmp = m_gameFrame.getRoot().getIntersectList(p);
			try
			{
			for ( GenericModel m: m_selected ) 
				if(!m.isDead() && m.isOnScreen())m.unSelect(); 
				else m_selected.remove(m);
			}
			catch (java.util.ConcurrentModificationException ex) {}
			
			m_selected.clear();
			
			if 
				( m_gameFrame.getRoot() != null ) 
			{
				if
					( m_gamePlayPanel.getToBuild().compareTo("") != 0 && m_canBuild )
				{
					m_gameFrame.getRoot().build( p, m_gamePlayPanel.getToBuild(), m_gamePlayPanel.getToBuildBehavior(), m_gamePlayPanel.getNextSpendings() );
					m_gameFrame.getControlPanel().repaint();
					m_gameFrame.setCursor(m_cursorHold);
					m_canBuild = false;
				}
				else if (m_gamePlayPanel.getToBuild().compareTo("") == 0 && tmp.size() > 0) 
				{
					try
					{
						Projectile projectile = (Projectile)tmp.get(0);
					}
					catch (Throwable th)
					{
						tmp.get(0).select();
						m_selected.add(tmp.get(0));
					}
				}
			}
			else
			{
				//Do nothing
			}
		}
	}

	public void mouseDragged(MouseEvent evt) 
	{
	}

	@Override
	public ArrayList<GenericModel> getSelected() { return m_selected; }
	
	public void mouseMoved(MouseEvent evt) 
	{
		Point p = getWorldPoint(evt);
		
		if ( m_cursorHold != null && m_cursorHold.getType() != Cursor.getDefaultCursor().getType() && m_cursorHold.getType() != m_gameFrame.getCursor().getType())
		{
			m_gameFrame.setCursor(m_cursorHold);
		}
		
		if
			( m_gamePlayPanel.getToBuildDimension() != null )
		{
			Dimension dim = m_gamePlayPanel.getToBuildDimension();
			Rectangle bounds = null;
			if (dim.width > 40 && dim.height > 40) bounds = new Rectangle( p.x - (dim.width/2) + 20, p.y - (dim.height/2) + 20, dim.width - 40, dim.height -40 );
			else bounds = new Rectangle( p.x - (dim.width/2), p.y - (dim.height/2), dim.width, dim.height );
			boolean intersects = false;
			for ( GameNode n: m_gameFrame.getRoot().getSubNodes() )
			{
				try
				{
					Projectile projectile = (Projectile) n;
				}
				catch (Throwable th)
				{
					try
					{
						GenericModel m = (GenericModel) n;
						if
							( m.intersects(bounds) )
						{
							if ( m_gamePlayPanel.getToBuild().compareToIgnoreCase("Model.TowerBasic") == 0 )
							{
								if (!m_gameFrame.getCursor().equals(m_gameFrame.getControlPanel().m_cannotBuildBasicTower))
								{
									m_cursorHold = m_gameFrame.getCursor();
									m_gameFrame.setCursor(m_gameFrame.getControlPanel().m_cannotBuildBasicTower);
								}
							}
							else if ( m_gamePlayPanel.getToBuild().compareToIgnoreCase("Model.ObstacleBasic") == 0 )
							{
								if (!m_gameFrame.getCursor().equals(m_gameFrame.getControlPanel().m_cannotBuildFence)) 
								{
									m_cursorHold = m_gameFrame.getCursor();
									m_gameFrame.setCursor(m_gameFrame.getControlPanel().m_cannotBuildFence);
								}
							}
							intersects = true;
							m_canBuild = false;
							break;
						}
					}
					catch (Throwable th2)
					{
						
					}
				}
			}
			if ( !intersects )
			{
				if ( m_gamePlayPanel.getToBuild().compareToIgnoreCase("Model.TowerBasic") == 0 )
				{
					if (!m_gameFrame.getCursor().equals(m_gameFrame.getControlPanel().m_canBuildBasicTower))
					{
						m_cursorHold = m_gameFrame.getCursor();
						m_gameFrame.setCursor(m_gameFrame.getControlPanel().m_canBuildBasicTower);
					}
				}
				else if ( m_gamePlayPanel.getToBuild().compareToIgnoreCase("Model.ObstacleBasic") == 0 )
				{
					if (!m_gameFrame.getCursor().equals(m_gameFrame.getControlPanel().m_canBuildFence)) 
					{
						m_cursorHold = m_gameFrame.getCursor();
						m_gameFrame.setCursor(m_gameFrame.getControlPanel().m_canBuildFence);
					}
				}
				m_canBuild = true;
			}
		}
		
	}
	
	/**
	 * 
	 * @param evt - mouse event
	 * @return a point representing the position on screen taking into account the offset in y.
	 */
	private Point getWorldPoint(MouseEvent evt) 
	{
		int x = evt.getX();
		int y = evt.getY() - 22;
		Point viewPoint = new Point( x, y ) ;
		return viewPoint;
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		//Do nothing
	}

	@Override
	public void clearSelected() 
	{
		m_selected.clear();
	}
}