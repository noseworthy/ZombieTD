package Model;


import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Observer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Model.Behavior;
import View.GamePlayPanel;
import View.GameFrame;
import Interfaces.RootModelI;

/**
 * GameRootNode extends GameNode. It is the top of the GameNode tree structure and all</br>
 * other Gamenodes should be descendant from it. GameRootNode implements the RootModelI </br>
 * interface, and acts as the access point to the GameNode tree structure.
 * 
 * @author michaelnoseworthy
 *
 */
public class GameRootNode extends GameNode implements RootModelI 
{
	private final GameFrame m_gameWindow;
	private ImageBank m_imgBank = new ImageBank();
	private GameEngine m_engine = null;

	/**
	 * GameRootNode Constructor that takes an XML document element and GameFrame.</br>
	 * Creates a behavior that has the Properties, "Resources", "KillCount" and any other</br>
	 * Properties parsed from the XML Element.
	 * @param e XML document Element to be parsed for Property data
	 * @param gameWindow a GameFrame that the node structure is to be displayed on
	 */
	public GameRootNode(Element e, GameFrame gameWindow)
	{
		super(null, e);
		this.addBehavior(new BehaviorGameRules(this));
		m_gameWindow = gameWindow;
		if ( !m_properties.containsKey("Resources") ) this.addProperty("Resources", "1000");
		setup();
		m_gameWindow.setRoot(this);
		m_gameWindow.repaint();
		assurePropertyValue("KillCount", "0");
	}
	
	/**
	 * Accessor method for the GamePlayFrame
	 * @return The GameRootNode's GamePlayFrame.
	 */
	public GamePlayPanel getGameFrame() { return m_gameWindow.getGamePlayPanel(); }
	
	/**
	 * Accessor method for the GameRootNode's GameFrame
	 * @return the current gameFrame
	 */
	public GameFrame getGameWindow() { return m_gameWindow; }
	
	/**
	 * Override of GameNode's tick method. In addition to the functionality of</br>
	 * the superClass's tick method, the GameRootNode tick method checks the Game's</br>
	 * state and updates the play accordingly. If the Game is lost, a "You Lose" </br>
	 * message is displayed on the screen. If the Game is won but not perfect </br>
	 * a message of "You don't suck" is displayed on the screen with the current</br>
	 * Killcount. If the Game is won and no Zombies were let through, a message of </br>
	 * you win is displayed.
	 * 
	 * @param gameTime the current time of the game in ticks.
	 */
	@Override
	public void tick(int gameTime) 
	{
		super.tick(gameTime);
		if
			(getGameState() == GameState.GAME_LOST)
		{
			m_gameWindow.setScreenMessage("YOU LOSE", getKillCount());
			m_gameWindow.repaint();
		}
		else if
			(getGameState() == GameState.GAME_WON)
		{
			if ( this.getPlayerHealth() >= 10 ) m_gameWindow.setScreenMessage("YOU WIN", getKillCount());
			else m_gameWindow.setScreenMessage("YOU DON'T SUCK", getKillCount());
			m_gameWindow.repaint();
		}
		
	}
	
	/**
	 * Setter for the KillCount Property. Increases the KillCount by 1
	 */
	public void increaseKillCount()
	{
		int kills = getKillCount() + 1;
		setProperty("KillCount", Integer.toString(kills));
	}
	
	/**
	 * Accessor for the KillCount Property, retrieves the KillCount property from </br>
	 * the GameRootNode's HashMap.
	 * @return current KillCount.
	 */
	public int getKillCount()
	{
		return Integer.parseInt((String)assurePropertyValue("KillCount", "0"));
	}
	
	/** 
	 * convenience method for GameRootNode, adds an Observer to the GameRootNode
	 */
	private void setup() 
	{
		if (m_gameWindow != null)addObserver((Observer)m_gameWindow);
	}
	
	/**
	 * Setter for the Resources Property
	 * increases Resources by a signed integer amount
	 * 
	 * @modifies the amount of resources the player has
	 * @return the new value of resources.
	 */
	@Override
	public int modResourcesBy( int signedIncrease )
	{
		int old = this.getResources();
		old += signedIncrease;
		m_properties.get("Resources").setValue(Integer.toString(old));
		m_gameWindow.getControlPanel().repaint();
		return old;
	}
	
	/**
	 * checks to see if the game Engine is paused.
	 * @return the value of the Paused Property, False if the Paused property didn't previously</br>
	 * exist
	 */
	public boolean isPaused() { return Boolean.parseBoolean((String)assurePropertyValue("Paused", "False")); }

	/**
	 * setter for the Paused Property. Sets Paused to True
	 */
	public void pause() 
	{
		this.addProperty("Paused", "True");
	}

	/**
	 * setter for the Paused Property. Sets Paused to False
	 */
	public void start() 
	{
		this.addProperty("Paused", "False");
	}

	/**
	 * Accessor method for this GameNode's Image Bank
	 */
	@Override
	public ImageBank getImageBank() 
	{
		return m_imgBank;
	}
	
	/**
	 * Retrieves an ArrayList of all the GenericModels that contain a 
	 * specified point, p
	 * 
	 * @param p a Point on the GamePlayPanel
	 * 
	 * @return an ArrayList of GenericModels that intersect the point p
	 */
	@Override
	public ArrayList<GenericModel> getIntersectList( Point p )
	{
		ArrayList<GenericModel> tmp = new ArrayList<GenericModel>();
		
		for ( GameNode node: this.getAllSubNodes() )
		{
			try
			{
				GenericModel m = (GenericModel)node;
				if ( m.contains(p) ) tmp.add(m);
			}
			catch(Throwable th)
			{
				
			}
		}
		return tmp;
	}
	
	/**
	 * returns all the descendant GameNodes for the GameRootNode
	 * 
	 * @return an ArrayList of GameNodes that are all the descendants of the GameRootNode
	 * 
	 * @see Model.GameNode
	 */
	@Override
	public ArrayList<GameNode> getSubNodes() { return super.getAllSubNodes(); }

	/**
	 * builds a new GenericModel at position p with the specified behavior. Also reduces the 
	 * quantity of resources by 'cost'. Further description found in "RootModelI"
	 */
	@Override
	public void build(Point p, String toBuild, String behavior, int cost) 
	{
		if ( Math.abs(cost) <= getResources())
		{
			if ( toBuild.compareTo("") == 0 ) return;
			else
			{
				try
				{
					Class<?> c = Class.forName(toBuild);
					Constructor<?> constructor = c.getConstructor(GameNode.class, String.class);
					GenericModel m = (GenericModel) constructor.newInstance(this, Integer.toString(m_nodes.size()));
					this.addNode(m);
					Property prop = new Property(m, "Position", Integer.toString(p.x - (m.getImageSize().width/2)) + " " + Integer.toString(p.y - (m.getImageSize().height/2) ), this.getGameWindow());
					Property type = new Property(m, "Type", toBuild);
					m.addProperty(prop);
					m.addProperty(type);
					
					if
						( behavior != null )
					{
						Class<?> cb = Class.forName(behavior);
						Constructor<?> constructorb = cb.getConstructor(GameNode.class);
						Behavior b = (Behavior)constructorb.newInstance(m);
						Property btype = new Property(m, "Type", behavior);
						b.addProperty(btype);
						m.addBehavior(b);
					}
					this.modResourcesBy(cost);
				}
				catch (Exception ex) 
				{
					System.err.println("Failed with " + toBuild);
					ex.printStackTrace();
				}
			}
		}
		else
		{
			java.awt.Toolkit.getDefaultToolkit().beep();
		}
		
	}
	
	@Override
	public void reparse(Element e)
	{
		clearAll();
		this.parse(e);
		if ( m_behaviors.size() == 0 ) this.addBehavior(new BehaviorGameRules(this));
		this.setup();
		if (m_engine != null) m_engine.setTickCount(getGameTime());
		//System.err.println(this);
	}
	
	public void setEngine( GameEngine engine ){	m_engine = engine;	}
	
	@Override
	public void saveGame(String gameName)
	{
	//	System.out.println(this);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		Document document = null;
		Element rootElement = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();
			rootElement = document.createElement("Root");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if
			(rootElement != null && document != null)
		{
			buildXml(document, rootElement);
			document.appendChild(rootElement);
			try
			{
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
				StreamResult result = new StreamResult(new StringWriter());
				DOMSource source = new DOMSource(document);
				transformer.transform(source, result);
	
				String xmlString = result.getWriter().toString();
				BufferedWriter out = new BufferedWriter(new FileWriter(gameName));
				out.write(xmlString);
				out.close();
			}
			catch (Throwable th)
			{
				th.printStackTrace();
			}
		}		
	}
	
	/**
	 * ClearAll prepares the GameRootNode for deletion, it clears all ArrayLists, and removes all Screen Messages
	 *
	 */
	@Override
	public void clearAll()
	{
		m_nodes.clear();
		m_properties.clear();
		m_toAdd.clear();
		m_behaviors.clear();
		m_gameWindow.setScreenMessage("", -1);
	}
	
	/**
	 * Retrieves the Resources Property value. 
	 * 
	 * @return an integer value representing the amount of Resources currently owned by the Player
	 */
	@Override
	public int getResources() 
	{
		return Integer.parseInt((String)assurePropertyValue("Resources", "0"));
	}

	/**
	 * getGameTimeMilliseconds is used to get the time in milliseconds since the </br>
	 * game was started up.
	 * 
	 * @return an integer value representing the gameTime in milliseconds
	 */
	@Override
	public int getGameTimeMilliseconds() 
	{
		return this.getGameTime();
	}
	
	/**
	 * sets the gameState Property of the GameRootNode to a String representation of state.
	 * 
	 * @param state the GameState to be set in the gameState Property.
	 */
	public void setGameState(GameState state) {
		if ( state == GameState.FIGHTING_WAVES ) m_behaviors.get(0).addProperty("gameState", "FIGHTING_WAVES");
		else if ( state == GameState.GAME_LOST ) m_behaviors.get(0).addProperty("gameState", "GAME_LOST");
		else if ( state == GameState.GAME_WON ) m_behaviors.get(0).addProperty("gameState", "GAME_WIN");
		else if ( state == GameState.LOAD_SCREEN ) m_behaviors.get(0).addProperty("gameState", "LOAD_SCREEN");
		else if ( state == GameState.NEXT_WAVE_WARNING ) m_behaviors.get(0).addProperty("gameState", "NEXT_WAVE_WARNING");
		else if ( state == GameState.WAIT_TO_START ) m_behaviors.get(0).addProperty("gameState", "WAIT_TO_START");
		else m_behaviors.get(0).addProperty("gameState", "LOAD_SCREEN");
		
	}
	
	
	/**
	 * retrieves the value of the gameState Property
	 * 
	 * @return the current GameState
	 */
	public GameState getGameState() {
		String state = (String)m_behaviors.get(0).assurePropertyValue("gameState", "LOAD_SCREEN");
		if ( state.compareToIgnoreCase("FIGHTING_WAVES") == 0 ) return GameState.FIGHTING_WAVES;
		else if ( state.compareToIgnoreCase("GAME_LOST") == 0 ) return GameState.GAME_LOST;
		else if ( state.compareToIgnoreCase("GAME_WIN" ) == 0 ) return GameState.GAME_WON;
		else if ( state.compareToIgnoreCase("LOAD_SCREEN" ) == 0 ) return GameState.LOAD_SCREEN;
		else if ( state.compareToIgnoreCase("NEXT_WAVE_WARNING" ) == 0 ) return GameState.NEXT_WAVE_WARNING;
		else if ( state.compareToIgnoreCase("WAIT_TO_START" ) == 0 ) return GameState.WAIT_TO_START;
		else 
		{
			System.err.println("gameState not recognized for " + state + ". Returning LOAD_SCREEN.");
			return GameState.LOAD_SCREEN;
		}
	}
	
	/**
	 * updates the player's Health with hp if it exists. Otherwise it sets Health to 10
	 * 
	 * @param hp an integer representing the new value of the player's health 
	 */
	protected void decreasePlayerHealth(int hp)
	{
		int newHealth = getPlayerHealth() - hp;
		setPlayerHealth(newHealth);
	}
	
	/**
	 * retrieves the Player's health if it exists. Otherwise sets health to 10 and returns 10.
	 * 
	 * @return the player's current health
	 */
	public int getPlayerHealth() 
	{
		assurePropertyValue("PlayerHealth", "10");
		int health = Integer.parseInt(this.getPropertyList().get("PlayerHealth").getValue().toString());
		return health;
	}
	
	/**
	 * sets the Player's Health to the value of hp
	 * @param hp the value of health to be set
	 */
	public void setPlayerHealth(int hp) 
	{
		String toSet = Integer.toString(hp);
		this.addProperty("PlayerHealth", toSet);
		m_gameWindow.getControlPanel().repaint();
	}
	
	public static void main(String[] args)
	{
		Document doc = null;
		try
		{
			//File file = new File("./SavedGame.xml");
			File file = new File("./src/input.xml");
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			doc.getDocumentElement().normalize();
		}
		catch
			( Exception ex )
		{
			ex.printStackTrace();
		}
		
		File music = new File ( "./src/Resources/Fortress.mid");

		 try {
		        // From file
		        Sequence sequence = MidiSystem.getSequence(music);
		        // Create a sequencer for the sequence
		        Sequencer sequencer = MidiSystem.getSequencer();
		        sequencer.open();
		        sequencer.setSequence(sequence);
		    
		        // Start playing
		        sequencer.start();
		    } catch (MalformedURLException e) {
		    } catch (IOException e) {
		    } catch (MidiUnavailableException e) {
		    } catch (InvalidMidiDataException e) {
		    }
		
		//StartScreen startScreen = new StartScreen();

		GameFrame window = new GameFrame();
		GameRootNode root = new GameRootNode(doc.getDocumentElement(), window);
		//System.out.println( root );
		GameEngine engine = new GameEngine(root);
		root.setEngine(engine);
		engine.run();
			
	}

	/**
	 * accessor method to the TimeToNextWave Property. TimeToNextWave is used to </br>
	 * count down the last 5 seconds until the next Zombie Wave is created.
	 * 
	 * @return integer value of the time in seconds until the next wave arrives
	 */
	public int getTimetoNextWave() {
		return Integer.parseInt((String)getProperty("TimeToNextWave").getValue());
	}

}