package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import Interfaces.GameClickListenerI;
import Interfaces.RootModelI;
import Model.GameClickListener;
import Model.GameState;
import Model.GenericModel;
import Model.Property;

public class GameFrame extends JFrame implements Observer 
{
	private final GamePlayPanel m_gamePlayPanel = new GamePlayPanel();
	private GameClickListenerI m_clicklistener = new GameClickListener(this);
	protected ControlPanel m_controlFrame;
	public StartScreen m_startScreen;
	private RootModelI m_root = null;
	private boolean m_rebuild = false;
	private String m_screenMessage = "";
	private String m_screenSubMessage = "";

	public GameFrame() 
	{
		this.setVisible(true);
		this.addMouseMotionListener(m_clicklistener);
		this.addMouseListener(m_clicklistener);
		this.setLayout(null);
		m_gamePlayPanel.setBounds(0, 0, GamePlayPanel.SIZE.width, GamePlayPanel.SIZE.height);
		this.setSize(new Dimension(GamePlayPanel.SIZE.width, GamePlayPanel.SIZE.height + ControlPanel.SIZE.height));
		this.setMinimumSize(new Dimension(GamePlayPanel.SIZE.width, GamePlayPanel.SIZE.height + ControlPanel.SIZE.height + 30));
		
		m_startScreen = new StartScreen(this.getSize(), this);
		this.add(m_startScreen);
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
	}
	
	public void setRoot ( RootModelI root )
	{ 
		if ( m_gamePlayPanel != null ) m_gamePlayPanel.setRoot(root);
		m_controlFrame = new ControlPanel(this);
		m_root = root; 
	}
	
	public RootModelI getRoot() { return m_root; }
	
	public GamePlayPanel getGamePlayPanel() { return m_gamePlayPanel; }
	
	public ControlPanel getControlPanel() { return m_controlFrame; }
	
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		if (m_startScreen!=null && m_startScreen.flagRemove)
		{
			this.remove(m_startScreen);
			this.add(m_gamePlayPanel);
			this.add(m_controlFrame);
			m_gamePlayPanel.repaint();
			m_controlFrame.repaint();
			repaint();
			super.paint(g);
			m_startScreen = null;
		}
		
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		Font tmp = g.getFont();
		g.setFont(new Font(tmp.getName(), tmp.getStyle(), tmp.getSize() + 100));
		g.drawString(m_screenMessage, 400, 350);
		g.setFont(new Font(tmp.getName(), tmp.getStyle(), tmp.getSize() + 10));
		g.drawString(m_screenSubMessage, 400, 400);
		g.setColor(c);
	}
	
	public GameClickListener getGameClickListener()
	{
		return (GameClickListener) m_clicklistener;
	}
	
	@Override
	public void update(Observable o, Object arg) 
	{
		try
		{
			GenericModel toAdd = (GenericModel) ((Property) arg).getOwner();
			if ( toAdd.isSelected() ) 
			{
				if (toAdd.isDead() || toAdd.isToRemove() || !toAdd.isOnScreen()) m_clicklistener.clearSelected();
				m_controlFrame.repaint();
			}
		}
		catch (Throwable th)
		{
			//do nothing
		}
		m_gamePlayPanel.update(o, arg);
		
	}

	public void setScreenMessage(String message, int killCount) 
	{
		m_screenMessage = message;
		if ( killCount >= 0 ) m_screenSubMessage = "Zombies Killed: " + Integer.toString(killCount);
		else m_screenSubMessage = "";
	}
}