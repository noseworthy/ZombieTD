package Model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Model.Property;

/**
 * <b>Game Node</b>
 * <p> The base class in our Composite design pattern. Almost everything in the model package extends a game node</br>
 * 
 * @author Mike Noseworthy and Mitchell Chaulk
 *
 */
public class GameNode 
{
	protected ArrayList<Behavior> m_behaviors = new ArrayList<Behavior>();
	protected HashMap<String, Property> m_properties = new HashMap<String, Property>();
	protected ArrayList<GameNode> m_nodes = new ArrayList<GameNode>();
	protected ArrayList<GameNode> m_toAdd = new ArrayList<GameNode>();
	protected final GameNode m_owner;

	/**
	 * <b> GameNode Constructor</b>
	 * <p> Constructs a game node with the Property, "ToRemove" set to False and </br>
	 * the uses the elements parsed from an input xml file</p>
	 * @param owner ancestor/parent of this node in the composite tree, another GameNode.</pre>
	 * @param e an Element from an xml file used to construct the GameNode with the appropriate</br>
	 * properties and member variables.
	 * 	
	 */
	public GameNode(GameNode owner, Element e) 
	{
		m_owner = owner;
		parse(e);
		if ( m_properties.containsKey("Clones") ) this.m_properties.remove("Clones");
		assurePropertyValue("ToRemove", "False");
	}
	
	/**
	 * <b> GameNode Constructor</b>
	 * <p> Constructs a game node with the Property, "ToRemove" set to False and </br>
	 * the uses the elements parsed from an input xml file</p>
	 * @param owner ancestor/parent of this node in the composite tree, another GameNode.</pre>
	 * 	
	 */
	public GameNode(GameNode owner)
	{
		m_owner = owner;
		assurePropertyValue("ToRemove", "False");
	}
	
	/**
	 * Method used to tick this GameNode's behaviors. Also recursively calls tick on all of it's children.
	 * </br> Before ticking, it adds the GameNodes in the m_toAdd ArrayList as children, and adds an observer to
	 * </br> each new child. Then clears the m_toAdd ArrayList. After ticking all behaviors, then</br>
	 * all nodes, tick() then removes any of it's children nodes that are flagged to be removed.
	 * @param gameTime an int representing the current game time.
	 * 
	 */
	public void tick(int gameTime)
	{
		if
			(m_toAdd.size() != 0)
		{
			for ( GameNode n : m_toAdd)
			{
				m_nodes.add(n);
				n.addObserver(getRootNode().getGameWindow());
			}
			m_toAdd.clear();
		}
		
		for ( Behavior b: m_behaviors ) b.tick(gameTime);
		
		for ( GameNode n: m_nodes ) { n.tick(gameTime);	}
	
		
		ArrayList<GameNode> removeNodes = new ArrayList<GameNode>();
		
		for ( GameNode n: m_nodes )
		{
			if(n.isToRemove())
			{
				removeNodes.add(n);
			}
		}
		for ( GameNode n: removeNodes) 
		{
			this.removeGameNode(n);
		}
		this.addProperty("GameTime", Integer.toString(gameTime));
		
	}

	/**
	 * <b> isToRemove</b>
	 * <p> Function to determine if a GameNode is set to be removed on the next engine tick </p>
	 * @return - true if the GameNode contains the Property, "ToRemove" as true, or the GameNode</br>
	 * doesn't contain the Property "ToRemove"</br>
	 * - false if the property "ToRemove" is set to false.
	 * 	
	 */
	public boolean isToRemove()
	{
		return Boolean.parseBoolean((String) assurePropertyValue("ToRemove", "True"));
	}
	
	/**
	 * A method used to return the first selected tower that is found in the GameNode tree
	 * 
	 * @return - Tower tow: the currently selected tower in the tree or,
	 * 		   - null if no tower is selected.
	 */
	public Tower getSelectedTowers()
	{
		ArrayList<Tower> towers = getRootNode().getTowers();
		for (Tower tow : towers)
		{
			if(tow.isSelected())
			{
				return tow;
			}
		}
		return null;
	}
	
	
	/**
	 *  A method which gets the current game time, in ticks.
	 * @return an integer value for the GameTime Property
	 */
	public int getGameTime()
	{
		return Integer.parseInt((String)assurePropertyValue("GameTime", "0"));
	}
	
	/**
	 * Allows the node to set it's GameTime property, which holds an integer value representing the number of</br>
	 * ticks that have currently elapsed.
	 * @param time an integer to set in the GameTime Property
	 */
	public void setGameTime( int time )
	{
		this.addProperty("GameTime", Integer.toString(time));
	}
	
	/**
	 * getZombies returns all of the Zombie nodes that are
	 * descendants of this node
	 * 
	 * @return ArrayList zombies - an arraylist of the zombies that are </br> 
	 * descendants of this node
	 */
	public ArrayList<Zombie> getZombies()
	{
		ArrayList<Zombie> zombies = new ArrayList<Zombie>();
		for (GameNode node : getNodeList())
		{
			try
			{
				@SuppressWarnings("unused")
				Wave w = (Wave)node;
				//DO NOT ADD
			}
			catch (Throwable th)
			{
				for ( Zombie zomb: node.getZombies() ) 
				{
					zombies.add(zomb);
				}
				try
				{
					zombies.add((Zombie)node);
				}
				catch (Exception e)
				{
					// DO NOTHING
				}
			}
		}
		return zombies;
	}
	
	/**
	 *  Retrieves all of the Tower nodes that have descended from this node
	 *  
	 * @return Tower towers: an ArrayList of this node's descendant Tower nodes
	 */
	public ArrayList<Tower> getTowers()
	{
		ArrayList<Tower> towers = new ArrayList<Tower>();
		for (GameNode node : getNodeList())
		{
			for ( Tower tow: node.getTowers() ) towers.add(tow);
			try
			{
				towers.add((Tower)node);
			}
			catch (Exception e)
			{
				// DO NOTHING
			}
		}
		return towers;
	}
	
	/**
	 * getAllSubNodes returns all of this node's descendant GameNodes
	 * 
	 * @return ArrayList nodes: an ArrayList of all of this node's descendant GameNodes
	 *
	 */
	public ArrayList<GameNode> getAllSubNodes()
	{
		ArrayList<GameNode> nodes = new ArrayList<GameNode>();
		for ( GameNode n: m_nodes)
		{
			nodes.add(n);
			nodes.addAll(n.getNodeList());
		}
		return nodes;
	}
	
	/**
	 * getObstacles Retrieves all of the Obstacle nodes that have descended from this node
	 *  
	 * @return Obstacle obstacles: an ArrayList of this node's descendant Obstacle nodes
	 */
	public ArrayList<Obstacle> getObstacles() {
		ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
		for (GameNode node : getNodeList())
		{
			for ( Obstacle obs: node.getObstacles() ) obstacles.add(obs);
			try
			{
				obstacles.add((Obstacle)node);
			}
			catch (Exception e)
			{
				// DO NOTHING
			}
		}
		return obstacles;
		
	}
	
	/**
	 * SetToRemove sets the ToRemove property to true so that on the next clock tick</br>
	 * the node will be removed from the tree by it's parent.
	 */
	protected void setToRemove()
	{
		addProperty("ToRemove", "True");
	}

	/**
	 *  Function responsible for parsing a GameNode's properties as well as sub-GameNodes based one the attributes
	 *  and child elements belonging to the Element passed in its parameters.
	 * @param e - the document element to be turned into a GameNode
	 */
	public void parse( Element e )
	{
		for 
			( int i = 0; i < e.getAttributes().getLength(); i++)
		{
			this.addProperty(e.getAttributes().item(i));
		}
		
		NodeList ndlst = e.getElementsByTagName("Wave");
		for
			(int i = 0; i < ndlst.getLength(); i++)
		{
			String path = "";
			Wave wave = null;
			Element toPass = (Element) ndlst.item(i);
			int clones = 0;
			
			if
				( toPass.getParentNode().equals(e) )
			{
				if ( toPass.hasAttribute("Clones") ) 
				{
					clones = Integer.parseInt(toPass.getAttribute("Clones"));
				}
			}
			
			for ( int j = 0; j < clones+1; j++)
			{
				path = "";
				if
					( toPass.getParentNode().equals(e) )
				{
					if
						( toPass.hasAttribute("Type") )
					{
						path += "Model." + toPass.getAttribute("Type");
					}
					if
						( toPass.hasAttribute("Path") )
					{
						path += toPass.getAttribute("Path");
					}
					else
					{
					}	
			
					//Reflection is used here to build the wave based on the passed type
					try
					{
						Class<?> c = Class.forName(path);
						Constructor<?> constructor = c.getConstructor(GameNode.class, Element.class);
						wave = (Wave) constructor.newInstance(this, ndlst.item(i));
					}
					catch (Exception ex) 
					{
						try
						{
							Class<?> c = Class.forName(toPass.getAttribute("Type"));
							Constructor<?> constructor = c.getConstructor(GameNode.class, Element.class);
							wave = (Wave) constructor.newInstance(this, ndlst.item(i));
						}
						catch (Exception ex2)
						{
							System.err.println("Failed with " + path);
							ex2.printStackTrace();
						}
					}
					if (wave != null) this.m_nodes.add(wave);
				}
			}
		}
		
		
		ndlst = e.getElementsByTagName("Model");
		for
			(int i = 0; i < ndlst.getLength(); i++)
		{
			String path = "";
			GenericModel m = null;
			Element toPass = (Element) ndlst.item(i);
			int clones = 0;
			
			if
				( toPass.getParentNode().equals(e) )
			{
				if ( toPass.hasAttribute("Clones") ) 
				{
					clones = Integer.parseInt(toPass.getAttribute("Clones"));
				}
			}
			
			for ( int j = 0; j < clones+1; j++)
			{
				path = "";
				if
					( toPass.getParentNode().equals(e) )
				{
					//SET ID PERHAPS?
					if
						( toPass.hasAttribute("Type") )
					{
						path += "Model." + toPass.getAttribute("Type");
					}
					if
						( toPass.hasAttribute("Path") )
					{
						path += toPass.getAttribute("Path");
					}
					else
					{
						//THROW
					}	
					
					try
					{
						Class<?> c = Class.forName(path);
						Constructor<?> constructor = c.getConstructor(GameNode.class, Element.class);
						m = (GenericModel) constructor.newInstance(this, ndlst.item(i));
					}
					catch (Exception ex) 
					{
						try
						{
							Class<?> c = Class.forName(toPass.getAttribute("Type"));
							Constructor<?> constructor = c.getConstructor(GameNode.class, Element.class);
							m = (GenericModel) constructor.newInstance(this, ndlst.item(i));
						}
						catch (Exception ex2)
						{
							System.err.println("Failed with " + path);
							ex2.printStackTrace();
						}
					}
					if (m != null) this.m_nodes.add(m);
				}
			}
		}
		ndlst = e.getElementsByTagName("Behavior");
		for
			(int i = 0; i < ndlst.getLength(); i++)
		{
			String path = "";
			Behavior b = null;// = new GenericModel(this, ndlst.item(i)) ;
			Element toPass = (Element) ndlst.item(i);
			if ( toPass.getParentNode().equals(e) )
			{
				if
					( toPass.hasAttribute("Type") )
				{
					path += "Model.Behavior" + toPass.getAttribute("Type");
				}
				if
					( toPass.hasAttribute("Path") )
				{
					path += toPass.getAttribute("Path");
				}
				else
				{
					//THROW
				}
			
				try
				{
					Class<?> c = Class.forName(path);
					Constructor<?> constructor = c.getConstructor(GameNode.class, Element.class);
					b = (Behavior) constructor.newInstance(this, ndlst.item(i));
				}
				catch (Exception ex) 
				{
					try
					{
						Class<?> c = Class.forName(toPass.getAttribute("Type"));
						Constructor<?> constructor = c.getConstructor(GameNode.class, Element.class);
						b = (Behavior) constructor.newInstance(this, ndlst.item(i));
					}
					catch (Exception ex2)
					{
						System.err.println("Failed with " + path + "    " + toPass.getAttribute("Type"));
						ex2.printStackTrace();
					}
				}
				if (b != null) this.m_behaviors.add(b);
		
			}
		}
	}
	
	/**
	 * getWaves is used to return all of the Wave nodes that are descendant from this node
	 * 
	 * @return waves an ArrayList of the descendant Waves of this GameNode
	 */
	public ArrayList<Wave> getWaves()
	{
		ArrayList<Wave> waves = new ArrayList<Wave>();
		HashMap<Integer, Wave> sortedWaves = new HashMap<Integer, Wave>();
		for ( GameNode n: m_nodes)
		{
			for ( Wave w: n.getWaves() ) waves.add(w);
			try
			{
				waves.add((Wave)n);
				sortedWaves.put(Integer.parseInt((String) ((Wave)n).assurePropertyValue("Index", "0") ), ((Wave)n));
			}
			catch (Throwable th)
			{
				//DO NOTHING
			}
		}
		waves.clear();
		for ( int i = 0; i < sortedWaves.values().size(); i++ )
		{
			waves.add(sortedWaves.get(i));
		}
		return waves;
	}
	
	/**
	 * addProperty adds a property to this GameNode from an xml document using the Node interface
	 * 
	 * @param n the xml document node that is to be added to the GameNode
	 */
	public void addProperty(Node n)
	{
		Property p = new Property(this, n);
		m_properties.put(p.getName(), p);
	}
	
	/**
	 *  getNodeList() is an accessor function for the GameNode class which returns a
	 *  copy of the child nodes of this GameNode
	 * @return ArrayList children: The children of this GameNode
	 */
	public ArrayList<GameNode> getNodeList() 
	{
		ArrayList<GameNode> children = new ArrayList<GameNode>(m_nodes);
		return children;
	}

	/**
	 * addnode is a method to add a new GameNode to the GameNode tree structure</br>
	 * adding the node to the m_toAdd ArrayList so that it may be added to the</br>
	 * current GameNode's m_nodes ArrayList at the end of the current tick call.</br>
	 * to avoid concurrency issues.
	 * 
	 * @param n GameNode to be added to the current node.
	 */
	public void addNode(GameNode n) 
	{
		m_toAdd.add(n);
	}

	/**
	 * getPropertyList() retrieves all Properties of this GameNode
	 * @return the m_properties HashMap
	 */
	public HashMap<String, Property> getPropertyList() 
	{
		return m_properties;
	}

	/**
	 * asurePropertyValue is used to assure that a value exists in the HashMap for a particular key,</b>
	 * if it does not exist, it will create an entry for the Key, using a fall back Value. It then
	 * returns the value for the key.
	 * 
	 * @param name the key the hash map is to be checked for 
	 * @param fallbackValue the value to be put into the Hashmap for "name"</b>
	 * if "name" doesn't already exist.
	 * @return returns the value for the key "name" after the Hashmap has been checked</b>
	 * for "name" and possibly updated.
	 */
	public Object assurePropertyValue(String name, Object fallbackValue)
	{
		if ( !m_properties.containsKey(name) )addProperty(name, fallbackValue);
		return m_properties.get(name).getValue();
	}
	
	/**
	 * add a property to this GameNode's m_properties HashMap 
	 * @param aProperty property to be added.
	 */
	public void addProperty(Property aProperty) 
	{
		m_properties.put(aProperty.getName(), aProperty);
	}
	
	/**
	 * add a property to this GameNode's m_properties HashMap 
	 * @param name Key to be used in the HashMap for this property
	 * @param value an object representing the "value" of the Property
	 */
	public void addProperty(String name, Object value)
	{
		m_properties.put(name, new Property(this, name, value) );
	}
	
	/**
	 * setProperty sets a value for the property, "name", and adds it to the 
	 * HashMap if it didn't exist before.
	 * @param name Key to be used in the HashMap for this property
	 * @param value a String representing the "value" of the Property
	 */
	public void setProperty(String name, String value) 
	{
		if ( !m_properties.containsKey(name) )
		{
			this.addProperty(name, value);
		}
		m_properties.get(name).setValue(value);
		
	}

	/**
	 * recursively calls up the GameNode structure until reaching a GameNode without an</br>
	 * ancestor, then returns this GameNode as the Root.
	 * 
	 * @return the root of the GameNode tree.
	 */
	public GameRootNode getRootNode() 
	{
		if ( m_owner == null ) return (GameRootNode) this;
		else return m_owner.getRootNode();
	}

	/**
	 * retrieves the direct ancestor or parent of this GameNode
	 * @return m_owner: the "parent" of this GameNode
	 */
	public GameNode getOwner() 
	{
		return m_owner;
	}
	
	/**
	 * addBehavior adds a Behavior to the GameNode's ArrayList of behaviors
	 * @param Behavior b: the behavior to be added
	 */
	public void addBehavior(Behavior b) 
	{
		m_behaviors.add(b);
	}
	
	/**
	 * retrieves the properties for this GameNode
	 * @param name name of the property to be retrived from the HashMap
	 * @return Property having the name "name"
	 */
	public Property getProperty(String name) 
	{
		return m_properties.get(name);
	}
	
	/**
	 * adds an observer to all properties, behaviors, and GameNodes Known by this</br>
	 * GameNode lower in the tree. 
	 * @param o Observer to be added
	 */
	public void addObserver(Observer o)
	{
		for( Property p: m_properties.values())
		{
			try
			{
				p.addObserver(o);
			}
			catch ( Throwable th ) { /*DO NOTHING*/}
		}
		for( GameNode n: m_nodes )
		{
			try
			{
				n.addObserver(o);
			}
			catch (Throwable th) {/*DO NOTHING*/}
		}
		for( Behavior b: m_behaviors )
		{
			try
			{
				b.addObserver(o);
			}
			catch (Throwable th) {/*DO NOTHING*/}
		}
		for ( GameNode toAdd: m_toAdd)
		{
			toAdd.addObserver(o);
		}
	}
	
	/**
	 * method to remove a particular node from this GameNode's list of descendants
	 * @param node GameNode that is to be removed\
	 */
	public void removeGameNode( GameNode node )
	{
		node.clear();
		m_nodes.remove(node);
	}
	
	/**
	 * method used to prepare this GameNode to be removed, clears all behaviors and properties from it's self,</br>
	 * and all it's descendants.
	 */
	public void clear() 
	{
		m_behaviors.clear();
		m_nodes.clear();
		m_properties.clear();
		
	}

	/**
	 * toString method for GameNode. Parses the properties, behaviors and descendant GameNodes of this GameNode</br>
	 * into an easily readable way to be displayed
	 */
	public String toString()
	{
		String toReturn = "{\nProperties:";
		for ( Property p: m_properties.values() ) toReturn += "\n	" + p.getName() + ":  " + p.getValue();
		toReturn += "\n";
		for ( GameNode n: m_nodes ) toReturn += "SubNode:\n" + n.toString() + "End of SubNode\n";
		for ( Behavior b: m_behaviors ) toReturn += "Behavior:\n" + b.toString() + "End Behavior\n";
		toReturn += "}\n";
		return toReturn;
	}

	/**
	 * Function called to build an XML file. Creates an element to be appended as a child of the parameter 'e'.
	 * Passes its new element instance, as well as doc to all of its child nodes to continue a tree construction
	 * of an XML file.
	 * 
	 * @param doc - the Document used to create a new element
	 * @param e - the parent element of the GameNode
	 */
	public void buildXml( Document doc, Element e )
	{
		for ( Property p: m_properties.values() ) p.buildXml(e);
		for ( GameNode n: m_nodes ) 
		{
			try
			{
				
				Element toAdd = doc.createElement("Wave");
				((Wave)n).buildXml(doc, toAdd);
				e.appendChild(toAdd);
			}
			catch (Throwable th)
			{
				try
				{
					
					Element toAdd = doc.createElement("Model");
					n.buildXml(doc, toAdd);
					e.appendChild(toAdd);
				}
				catch (Throwable th2)
				{
				}
			}
		}
		for ( Behavior b: m_behaviors )
		{
			try
			{
				Element toAdd = doc.createElement("Behavior");
				b.buildXml(doc, toAdd);
				e.appendChild(toAdd);
			}
			catch (Throwable t)
			{
			}
		}
	}
	
}