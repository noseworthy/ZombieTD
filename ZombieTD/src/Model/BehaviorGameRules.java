package Model;

import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 * The BehaviorGameRules class extends Behavior, and is intended to control the flow of</br>
 * gameplay using Properties that track the gameState.
 * 
 * @author michaelnoseworthy
 *
 */
public class BehaviorGameRules extends Behavior 
{
	private final GameRootNode m_root;


	/**
	 * BehaviorGameRules constructor that associates itself with the GameRootNode</br>
	 * and sets Properties to their default values.
	 * @param root
	 */
	public BehaviorGameRules(GameRootNode root)
	{
		super(root);
		m_root = root;
		this.assurePropertyValue("Type", "Model.BehaviorGameRules");
		m_root.assurePropertyValue("PlayerScore", "0");
		m_root.assurePropertyValue("PlayerHealth", "10");
		m_root.assurePropertyValue("gameState", "LOAD_SCREEN");
	}
	
	/**
	 * BehaviorGameRules constructor that ssociates itself with the GameRootNode then </br>
	 * sets default Parameters, PlayerScore, PlayerHealth, and gameState, and then parses</br>
	 * the XML document Element e for further Properties to set.
	 * 
	 * @param root the root of the GameNode tree structure
	 * @param e XML document Element that is to be parsed for Properties
	 */
	public BehaviorGameRules(GameNode root, Element e)
	{
		super(root, e);
		m_root = (GameRootNode)root;
		this.assurePropertyValue("Type", "Model.BehaviorGameRules");
		m_root.assurePropertyValue("PlayerScore", "0");
		m_root.assurePropertyValue("PlayerHealth", "10");
		m_root.assurePropertyValue("gameState", "LOAD_SCREEN");

	}

	/**
	 * Tick method for the BehaviorGameRules class evaluates current state of the</br>
	 * game and sets States based on current State, gameTime and input from the and</br>
	 * 
	 * @param gameTime current game time in ticks
	 * 
	 */
	@Override
	public void tick(int gameTime) 
	{
		/* set states that only loop into each other
		 * and require a signal from the view to do something else
		 */
		if( m_root.getGameState() == GameState.LOAD_SCREEN)
		{
			m_root.setGameState( GameState.LOAD_SCREEN);
			return;
		}
		else if ( m_root.getGameState() == GameState.GAME_LOST)
		{
			m_root.setGameState(GameState.GAME_LOST);
			return;
		}
		else if ( m_root.getGameState() == GameState.GAME_WON)
		{
			m_root.setGameState(GameState.GAME_WON);
			return;
		}
		
		/*
		 * otherwise just tick normally, cycling through the states as usual.
		 */
		ArrayList<Wave> waves = m_root.getWaves();
		int currentWaveIndex = Integer.parseInt((String)this.assurePropertyValue("CurrentWave", "0"));
		if ( m_root.getPlayerHealth() <= 0)
		{
			m_root.setGameState(GameState.GAME_LOST);
		}
		else if ( m_root.getPlayerHealth() > 0 && getRootNode().getZombies().size() == 0 && currentWaveIndex == waves.size() -1 )
		{
			m_root.setGameState(GameState.GAME_WON);
			this.setProperty("CurrentWave", "0");
		}
		else if 
			(m_root.getPlayerHealth() > 0 && waves.size() > 0)
		{
			int nextStartTime = (waves.get(currentWaveIndex).getStartTime())/50;
			if (( gameTime - nextStartTime) > -100)
			{
				int tempTime = gameTime - nextStartTime+1;
				m_root.setGameState(GameState.NEXT_WAVE_WARNING);
				m_root.assurePropertyValue("TimeToNextWave", "5");
				if (tempTime% 20 == 0)
				{
					int nextTime = m_root.getTimetoNextWave() - 1;
					m_root.setProperty("TimeToNextWave", Integer.toString(nextTime));
					m_root.getGameFrame().repaint();
				}
			}
			else
			{
				m_root.setGameState(GameState.FIGHTING_WAVES);
				m_root.getPropertyList().remove("TimeToNextWave");
			}
			if ( !waves.get(currentWaveIndex).isStarted() && !waves.get(currentWaveIndex).isFinished() ) 
			{
				waves.get(currentWaveIndex).start();
				waves.get(currentWaveIndex).m_behaviors.get(0).setGameTime(getRootNode().getGameTime());
			}
			
			if ( waves.get(currentWaveIndex).isFinished() )
			{
				currentWaveIndex++;
				if (waves.size() > currentWaveIndex) 
				{
					waves.get(currentWaveIndex).start();
					this.setProperty("CurrentWave", Integer.toString(currentWaveIndex));
				}
				
			}
		}
		else if
			( m_root.getGameState() == GameState.WAIT_TO_START)
		{
			m_root.setGameState(GameState.WAIT_TO_START);
		}
	}
}