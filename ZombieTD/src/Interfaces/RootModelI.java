package Interfaces;

import java.awt.Point;
import java.util.ArrayList;
import org.w3c.dom.Element;

import Model.GameNode;
import Model.GameState;
import Model.GenericModel;
import Model.ImageBank;
import Model.Tower;

public interface RootModelI 
{

	/**
	 * 
	 * @return the bank of images built on start up
	 */
	public ImageBank getImageBank();

	/**
	 * 
	 * @return a list of the sub GameNodes
	 */
	public ArrayList<GameNode> getSubNodes();

	/**
	 * 
	 * @param p - position to build the model at
	 * @param toBuild - the type of model to be built
	 * @param behavior - the type of behavior to be added to the model being built
	 * @param cost - the game resource cost of the build for this object
	 */
	public void build(Point p, String toBuild, String behavior, int cost);

	/**
	 * Initializes the xml building for saving a game instance
	 * @param gameName - path name for the xml file to be constructed
	 */
	public void saveGame(String gameName);

	/**
	 * clears the root node of all child nodes
	 */
	public void clearAll();

	/**
	 * Clears the current game instance and rebuilds the game instance using e as the root element.
	 * @param e - the root element of the game instance to be constructed.
	 */
	public void reparse(Element e);

	/**
	 * @return amount of resources available for the user to use
	 */
	public int getResources();

	/**
	 * 
	 * @param signedIncrease - the amount to change the resource quantity by
	 * @return the new amount of resources
	 */
	public int modResourcesBy(int signedIncrease);

	/**
	 * 
	 * @param p - the point of interest
	 * @return - a list of models that intersect with p on screen
	 */
	public ArrayList<GenericModel> getIntersectList(Point p);
	
	/**
	 * 
	 * @return the current game time
	 */
	public int getGameTimeMilliseconds();
	
	/**
	 * 
	 * @return returns a Tower if there is one selected, null otherwise
	 */
	public Tower getSelectedTowers();
	
	/**
	 * 
	 * @return the overall gamestate
	 */
	public GameState getGameState();
	
	/**
	 * 
	 * @return the health of the player. (Is reduced by one if a Zombie makes it to the right side of the screen)
	 */
	public int getPlayerHealth();
	
	/**
	 * 
	 * @param state - the GameState to set the game to.
	 */
	public void setGameState(GameState state);

	/**
	 * 
	 * @return time before the next wave begins
	 */
	public int getTimetoNextWave();

}