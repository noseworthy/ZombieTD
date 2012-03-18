package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import Model.GameState;

@SuppressWarnings("serial")
public class StartScreen extends JPanel
{
	public boolean flagRemove = false;
	public final Dimension SIZE;
	private final GameFrame m_gameFrame;
	private BufferedImage m_bg = null;
	JButton b;
	
	public StartScreen(Dimension size, GameFrame gameFrame)
	{
		try {
			m_bg = ImageIO.read(new File("./src/Resources/startScreenBg.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		m_gameFrame = gameFrame;
		SIZE = size;
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		this.setBounds(GamePlayPanel.WIDTH/2, 0, GamePlayPanel.WIDTH / 2, GameFrame.HEIGHT + ControlPanel.HEIGHT);
		this.setSize(SIZE);
		this.setMinimumSize(SIZE);
		
		this.setLayout(null);
		
		AbstractAction action =  new AbstractAction("Start") {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(m_gameFrame.getRoot().getGameState() == GameState.LOAD_SCREEN ||
						m_gameFrame.getRoot().getGameState() == GameState.GAME_LOST ||
						m_gameFrame.getRoot().getGameState() == GameState.GAME_WON)
				{
					m_gameFrame.getRoot().setGameState(GameState.FIGHTING_WAVES);
					flagRemove = true;
					m_gameFrame.repaint();
				}
			}
		};
		
		b = new JButton(action);
		b.setBackground(Color.BLACK);
		b.setForeground(Color.RED);
		b.setSize(new Dimension(200, 100));;
		b.setBounds(SIZE.width/2 - b.getSize().width/2, SIZE.height/2 + b.getSize().height, b.getSize().width, b.getSize().height);
		
		this.add(b);
	}
	
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		g.drawImage(m_bg, SIZE.width/4, SIZE.height/3, SIZE.width/2, SIZE.height/2, null);
	}
}
