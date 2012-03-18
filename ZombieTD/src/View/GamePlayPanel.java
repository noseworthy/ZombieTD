package View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import Interfaces.GameClickListenerI;
import Interfaces.RootModelI;
import Model.GameRootNode;
import Model.GameState;
import Model.GenericModel;
import Model.Property;

@SuppressWarnings("serial")
public class GamePlayPanel extends JPanel
{
	public static final Dimension SIZE = new Dimension(1250, 600);
	private String m_toBuild = "";
	private String m_toBuildBehavior = "";
	private Dimension m_toBuildDimension = null;
	private int m_nextSpendings = 0;
	private ArrayList<BufferedImage> timerImg = new ArrayList<BufferedImage>();
	
	private final ArrayList<GenericModel> m_toRepaint = new ArrayList<GenericModel>();
	private final Image m_bg = Toolkit.getDefaultToolkit().getImage("./src/Resources/BG_tiles/grass1.png") ;
	private RootModelI m_root = null;
	
	public GamePlayPanel() 
	{
		this.setMinimumSize(SIZE);
		this.setVisible(true);
		this.setLayout(null);
		for(int i = 0 ; i < 6; i++)
		{
			try{
				timerImg.add(ImageIO.read(new File("./src/Resources/countDownTimer/"+i+".png")));
			}
			catch(IOException e)
			{
				//Do NOTHING!
			}
		}
	}

	public RootModelI getRoot() { return m_root; } 
	public void setRoot( RootModelI root) { m_root = root; }
	
	public void flagToBuild( String toBuild, String behavior, Dimension bounds, int cost )
	{
		m_toBuild = toBuild;
		m_toBuildBehavior = behavior;
		m_nextSpendings = cost;
		m_toBuildDimension = bounds;
	}
	
	public Dimension getToBuildDimension() { return m_toBuildDimension; }
	
	public String getToBuild() { return m_toBuild; }
	
	public int getNextSpendings() { return m_nextSpendings; }
	
	public double zoomIn() 
	{
		return 0;
	}

	public double zoomOut() 
	{
		return 0;
	}

	public double getZoom() {
		throw new UnsupportedOperationException();
	}

	public void update(Observable o, Object arg) 
	{
		try
		{
			GenericModel toAdd = (GenericModel) ((Property) arg).getOwner();
			for 
				( int i = 0; i < m_toRepaint.size(); i++ )
			{
				if
					( m_toRepaint.get(i).getPosition().y > toAdd.getPosition().y )
				{
					m_toRepaint.add(i, toAdd);
					break;
				}
			}
			
			if (!m_toRepaint.contains(toAdd)) m_toRepaint.add(toAdd);
		}
		catch (Throwable th)
		{
			//Only the root node should ever cause this to be thrown. For resources.
		}
		
		repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		
		super.paint(g);
		for (int i = 0; i <= getWidth(); i = i+m_bg.getHeight(null))
		{
			for (int j =0 ; j <= getHeight(); j = j+m_bg.getWidth(null))
			{
				this.getGraphics().drawImage(m_bg, i, j, null);
			}
		}
		for ( GenericModel m : m_toRepaint ) 
		{
			m.paintSelf(g);
		}
		
		m_toRepaint.clear();
		
		if( m_root.getGameState() == GameState.NEXT_WAVE_WARNING)
		{
			int i = m_root.getTimetoNextWave();
			if (i >= 0) g.drawImage(timerImg.get(i), this.getWidth() *1/3 + 100, this.getHeight()*1/3, null);
			
		}
		
		
	}

	public void saveGame( String gameName ) { if ( m_root != null ) m_root.saveGame(gameName); }
	
	public String getToBuildBehavior() 
	{
		return m_toBuildBehavior;
	}

	public void loadGame(String string) 
	{
		Document doc = null;
		try
		{
			File file = new File(string);
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			doc.getDocumentElement().normalize();
		}
		catch
			( Exception ex )
		{
			ex.printStackTrace();
		}
		if 
			( m_root != null && doc != null )
		{
			m_root.reparse(doc.getDocumentElement());
		}
		
	}
	
}