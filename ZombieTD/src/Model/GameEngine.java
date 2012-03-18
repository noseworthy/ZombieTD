package Model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GameEngine implements Runnable 
{
	private final GameRootNode m_gameRootNode;
	public static final int TIME_RESOLUTION = 50;
	private int m_tickCount = 0;
	
	JPanel panel = new JPanel();
	boolean done = false;
	
	//Timer action
	ActionListener listener =  new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if( m_gameRootNode.getGameState() == GameState.LOAD_SCREEN || m_gameRootNode.getGameState() == GameState.GAME_WON || m_gameRootNode.getGameState() == GameState.GAME_LOST )
			{
				m_tickCount = 0;
			}
			m_gameRootNode.tick(m_tickCount);
			m_tickCount++;
		}
	};
	
	/**
	 * To be called when a new game instance is being loaded. Sets the tick count to that specified in the parameters.
	 * @param tick - the tick count to be set.
	 */
	public void setTickCount( int tick ) { m_tickCount = tick; }
	
	private Timer m_timer = new Timer(TIME_RESOLUTION, listener);

	public GameEngine(GameRootNode root) 
	{
		panel.setVisible(true);
		panel.setBackground(Color.BLACK);
		panel.setBounds(50, 50, 50, 50);
		m_gameRootNode = root;
		
	}

	public void pause() 
	{
		m_timer.stop();
	}

	@Override
	public void run() 
	{
		if
		( !m_gameRootNode.isPaused() )
		{
			m_timer.start();
		}
		else pause();
		
	}
}