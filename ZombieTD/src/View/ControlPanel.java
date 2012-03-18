package View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Model.GenericModel;
import Model.ModelState;
import Model.ObstacleBasic;
import Model.Property;
import Model.Tower;
import Model.TowerBasic;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel
{
	public static final Dimension SIZE = new Dimension(GamePlayPanel.SIZE.width, 140);
	private final GamePlayPanel m_playFrame;
	private final GameFrame m_gameFrame;
	private final CustomGameButton m_levelupButton;
	private JTextArea m_propertyDisplay = new JTextArea(50, 50);
	private JScrollPane m_propertyPane = new JScrollPane(m_propertyDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private String m_lastDisplayed = "blank";
	private JTextArea m_clock = new JTextArea(60, 20);
	private JScrollPane m_clockPane = new JScrollPane(m_clock, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JTextArea m_resources = new JTextArea(60, 20);
	private JScrollPane m_resourcePane = new JScrollPane(m_resources, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JTextArea m_health = new JTextArea(60, 20);
	private JScrollPane m_healthPane = new JScrollPane(m_health, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	public final Cursor m_cannotBuildBasicTower;
	public final Cursor m_canBuildBasicTower;
	public final Cursor m_cannotBuildFence;
	public final Cursor m_canBuildFence;
	private BufferedImage levelupImg;
	private BufferedImage playerHealthImg;
	private BufferedImage resourceImg;
	
	ActionListener listener = new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Date d = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			//m_clock = new JTextArea(sdf.format(d));
			m_clock.setText(sdf.format(d));
			
			//repaint();
		}
	};
	
	Timer m_displayManager = new Timer (1000, listener);
	
	

	public ControlPanel(GameFrame playFrame ) 
	{
		try
		{
			playerHealthImg = ImageIO.read(new File("./src/Resources/PlayerHealth.png"));
			resourceImg = ImageIO.read(new File("./src/Resources/ResourceImage.png"));
			levelupImg = ImageIO.read(new File("./src/Resources/upgradeButton.png"));
		}
		catch(IOException e)
		{
			playerHealthImg = null;
			resourceImg = null;
			levelupImg = null;
		}

		m_playFrame = playFrame.getGamePlayPanel();
		m_gameFrame = playFrame;
		
		Image ci1 = m_playFrame.getRoot().getImageBank().getTowerBasicCursor(1);
		m_cannotBuildBasicTower = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(ci1, new Point(TowerBasic.SIZE.width/2, TowerBasic.SIZE.height/2), "img");//(cimg, new Point(42, 32 - 20), "img");
		
		Image ci2 = m_playFrame.getRoot().getImageBank().getObstacleBasicCursor(1);
		m_cannotBuildFence = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(ci2, new Point(ObstacleBasic.SIZE.width/2, ObstacleBasic.SIZE.height/2), "img");//(cimg2, new Point(35, 35 - 20), "img");
		
		Image img = m_playFrame.getRoot().getImageBank().getTowerBasicImage(ModelState.ACTION, 8);
		Image cimg = m_playFrame.getRoot().getImageBank().getTowerBasicCursor(0);
		Cursor c = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(cimg, new Point(TowerBasic.SIZE.width/2, TowerBasic.SIZE.height/2), "img");//(cimg, new Point(42, 32 - 20), "img");
		m_canBuildBasicTower = c;
		
		Image img2 = m_playFrame.getRoot().getImageBank().getObstacleBasicImage(ModelState.STILL, 0);
		Image cimg2 = m_playFrame.getRoot().getImageBank().getObstacleBasicCursor(0);
		Cursor c2 = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(cimg2, new Point(ObstacleBasic.SIZE.width/2, ObstacleBasic.SIZE.height/2), "img");//(cimg2, new Point(35, 35 - 20), "img");
		m_canBuildFence = c2;
		
		m_propertyDisplay.setVisible(true);
		m_propertyDisplay.setWrapStyleWord(true);
		m_propertyPane.setVisible(true);
		m_propertyDisplay.setDragEnabled(false);
		m_propertyDisplay.setEditable(false);
		m_propertyPane.setBounds(SIZE.width - 240, 20, 120, SIZE.height - 40);
		m_propertyPane.setBackground(new Color(100,100,100,100));
		m_propertyDisplay.setBackground(new Color(100,100,100,100));
		
		m_propertyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		m_propertyPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(m_propertyPane);
		
		//CLOCK
		
		m_clock.setVisible(true);
		m_clock.setForeground(Color.WHITE);
		m_clock.setWrapStyleWord(true);
		m_clockPane.setVisible(true);
		m_clockPane.setBounds(SIZE.width/2 - 50, 1, 65, 18);
		m_clock.setBackground(new Color(0,0,0,250));
		m_clockPane.setBackground(new Color(0,0,0,250));
		
		this.add(m_clockPane);
		
		//RESOURCES
		m_resources.setVisible(true);
		m_resources.setForeground(Color.WHITE);
		m_resources.setWrapStyleWord(true);
		m_resourcePane.setVisible(true);
		m_resourcePane.setBounds(500 - 5, 1, 40, 18);
		m_resources.setBackground(new Color(0,0,0,250));
		m_resourcePane.setBackground(new Color(0,0,0,250));
		this.add(m_resourcePane);
		
		//HEALTH
		m_health.setVisible(true);
		m_health.setForeground(Color.WHITE);
		m_healthPane.setVisible(true);
		m_healthPane.setBounds(720, 1, 20, 18);
		m_healthPane.setBackground(new Color(0,0,0,250));
		m_health.setBackground(new Color(0,0,0,250));
		this.add(m_healthPane);
		
		m_displayManager.start();
		
		this.setBounds(0, GamePlayPanel.SIZE.height, GamePlayPanel.SIZE.width, SIZE.height);
		this.setBackground(new Color(0, 0, 0,250));
		this.setVisible(true);
		this.setLayout(null);
		
		//BUILD TOWER
		
		AbstractAction buildTower = new AbstractAction("BasicTower") 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if ( m_playFrame.getRoot().getResources() >= TowerBasic.COST )
				{
					m_playFrame.flagToBuild("Model.TowerBasic", "Model.BehaviorTowerBasic", new Dimension(84, 64), -1 * TowerBasic.COST);
				}
				else
				{
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		};
		
		CustomGameButton b =  new CustomGameButton(buildTower, img, c, m_gameFrame, TowerBasic.COST );
		b.setBounds(new Rectangle(15, 15, 84 + 5, 64 + 5)); 
		this.add(b);
		

		//BUILD FENCE
		AbstractAction buildFence = new AbstractAction("Fence") 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if ( m_playFrame.getRoot().getResources() >= ObstacleBasic.COST )
				{
					m_playFrame.flagToBuild("Model.ObstacleBasic", "Model.BehaviorObstacle", new Dimension(71, 71), -1*ObstacleBasic.COST);
				}
				else
				{
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		};
		
		CustomGameButton b1 =  new CustomGameButton(buildFence, img2, c2, m_gameFrame, ObstacleBasic.COST );
		b1.setBounds(new Rectangle(85 + 5 + 15, 15 + 5, 71 + 5, 71 + 5)); 
		this.add(b1);
		
		AbstractAction levelUp = new AbstractAction("TowerBasicUpgrade") 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if ( m_playFrame.getRoot().getSelectedTowers().getMaxUpgrade() >  m_playFrame.getRoot().getSelectedTowers().getUpgradeLevel())
				{
					if ( m_playFrame.getRoot().getResources() >= TowerBasic.UPGRADE_COST )
					{
						Tower selectedTow = m_playFrame.getRoot().getSelectedTowers();
						selectedTow.setUpgradeLevel(selectedTow.getUpgradeLevel()+1);
						m_playFrame.getRoot().modResourcesBy(-1 * TowerBasic.UPGRADE_COST);
					}
					else
					{
						java.awt.Toolkit.getDefaultToolkit().beep();
					}
				}
				else{
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		};
		CustomGameButton levelupButton = new CustomGameButton(levelUp, levelupImg, getCursor(), m_gameFrame, TowerBasic.UPGRADE_COST);
		m_levelupButton = levelupButton;
		levelupButton.setBounds(new Rectangle(1180, 20, 40 , 40));
		levelupButton.setVisible(false);
		this.add(levelupButton);

		
		AbstractAction saveGame = new AbstractAction("Save Game") 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				m_playFrame.saveGame("SavedGame.xml");
			}
		};
		JButton b2 = new JButton(saveGame);
		b2.setBounds(400 + 50, 55, 150, SIZE.height - 100);
		this.add(b2);
		
		AbstractAction loadGame = new AbstractAction("Load Game") 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				m_playFrame.loadGame("./SavedGame.xml");
			}
		};
		JButton b3 = new JButton(loadGame);
		b3.setBounds(560 + 100, 55, 150, SIZE.height - 100);
		this.add(b3);
		
		try {
			ControlPanelBackground background = new ControlPanelBackground(ImageIO.read(new File("./src/Resources/controlPanel.png")));
			background.setBounds(0, 0, SIZE.width, SIZE.height);
			this.add(background);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private BufferedImage m_imageHold = null;
	
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		
		int resources = m_gameFrame.getRoot().getResources();
		int health = m_gameFrame.getRoot().getPlayerHealth();
		g.drawImage(resourceImg, 475, 0, 15, 15, null);
		g.drawImage(playerHealthImg, 705, 0, 15, 15, null);

		if ( Integer.toString(resources).compareToIgnoreCase(m_resources.getText()) != 0 ) m_resources.setText(Integer.toString(m_gameFrame.getRoot().getResources()));
		if ( Integer.toString(health).compareToIgnoreCase(m_health.getText()) != 0 ) m_health.setText((Integer.toString(m_gameFrame.getRoot().getPlayerHealth())));
		
		if (m_gameFrame.getGameClickListener().getSelected().size() == 1)
		{
			
			GenericModel m = m_gameFrame.getGameClickListener().getSelected().get(0);
			m_levelupButton.setVisible(false);
			if(m == m_gameFrame.getRoot().getSelectedTowers()){
				m_levelupButton.setVisible(true);
			}
			BufferedImage img = m.getCurrentImage();
			if ( img != m_imageHold )
			{
				int x = SIZE.width - m.getImageSize().width - 40;
				int y = 25;
				g.drawImage(img, x, y, new Color(0,0,0,0), null);
				m_imageHold = img;
			}
			
			String toDisplay = m.getDisplayName() + "\n";
			for ( Property p: m.getPropertyList().values() )
			{
				if ( p.getName().compareToIgnoreCase("Type") != 0 && p.getName().compareToIgnoreCase("Name") != 0
					&& p.getName().compareToIgnoreCase("GameTime") != 0 && p.getName().compareToIgnoreCase("Position") != 0	
					&& p.getName().compareToIgnoreCase("Speed") != 0 && p.getName().compareToIgnoreCase("Selected") != 0
					&& p.getName().compareToIgnoreCase("ModelState") != 0 && p.getName().compareToIgnoreCase("ShotTick") != 0
					&& p.getName().compareToIgnoreCase("ActionTick") != 0 && p.getName().compareToIgnoreCase("DeadTick") != 0 )
					toDisplay += (p.getName() + ": " + p.getValue()+ "\n");
			}
			if ( m_lastDisplayed.compareTo(toDisplay) != 0 )
			{
				m_propertyDisplay.setText(toDisplay);
				m_lastDisplayed = toDisplay;
			}
			
		}
		else
		{
			m_propertyDisplay.setText("");
			m_lastDisplayed = "blank";
			m_levelupButton.setVisible(false);
			
		}
		
	}
}