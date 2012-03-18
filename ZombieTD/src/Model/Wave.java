package Model;

import java.util.ArrayList;

import org.w3c.dom.Element;

public class Wave extends GameNode
{
	private Zombie m_toClone = null;

	public Wave(GameNode owner, Element e)
	{
		super(owner, e);
		m_toClone = getZombies().get(0);
		if (!m_properties.containsKey("MaxTime"))addProperty("MaxTime", Integer.toString(Integer.parseInt((String)assurePropertyValue("Time", "10000")) / 50));

		assurePropertyValue("IsReady", "False");
		assurePropertyValue("IsStarted", "False");
		assurePropertyValue("IsFinished", "False");
		assurePropertyValue("SetLastGameTime", "True");
		
	}
	
	@Override
	public ArrayList<Zombie> getZombies() 
	{
		ArrayList<Zombie> temp = new ArrayList<Zombie>();
		for (GameNode node : getNodeList())
		{
				for ( Zombie zomb: node.getZombies() ) 
				{
					temp.add(zomb);
				}
				try
				{
					temp.add((Zombie)node);
				}
				catch (Exception e)
				{
					// DO NOTHING
				}
		}
		return temp;
	}
	
	private void finishup()
	{
		int clones = Integer.parseInt((String)assurePropertyValue("Zombies", "-1"));
		if (m_toClone != null)
		{
			for ( int i = 0; i < clones; i++)
			{
				String pos = "r: " + (String)assurePropertyValue("ZombiePosition", "-100,0 0,550");
				Zombie clone = m_toClone.clone(getRootNode());
				
				clone.addProperty("Position", pos);

				clone.m_behaviors.get(0).setGameTime(getRootNode().getGameTime());
			}
			m_toClone.setToRemove();
		}
		
		setIsFinished(true);
		setIsReady(false);
		setIsStarted(false);
	}
	
	public void setIsFinished( boolean bool ) { addProperty("IsFinished", Boolean.toString(bool)); }
	public void setIsReady( boolean bool ) { addProperty("IsReady", Boolean.toString(bool)); }
	public void setIsStarted( boolean bool ) { addProperty("IsStarted", Boolean.toString(bool)); }
	public void setSetLastGameTime ( boolean bool ) { addProperty("SetLastGameTime", Boolean.toString(bool));}
	
	public boolean isReady() { return Boolean.parseBoolean((String) assurePropertyValue("IsReady", "False")); }
	
	public boolean isStarted() { return Boolean.parseBoolean((String) assurePropertyValue("IsStarted", "False")); }
	
	public boolean isFinished() { return Boolean.parseBoolean((String) assurePropertyValue("IsFinished", "False")); }
	
	public void start() 
	{ 
		setIsStarted(true);
		setIsReady(true);
	}
	
	public void pauseWave() { setIsReady(false); }
	public void resume() { setIsReady(true); };
	
	@Override
	public void tick(int gameTime) 
	{
		int m_maxTime = Integer.parseInt((String) assurePropertyValue("MaxTime", "1000")) ; 
		if
			( (gameTime - this.getGameTime() ) >= m_maxTime  )
		{
	
			if( ( this.isReady() && this.isStarted()) || this.isFinished() )
			{
				if ( this.isSetLastGameTime() )
				{
					finishup();
					this.setSetLastGameTime(false);
				}
			}
		}
		
		
	}
	
	public int getStartTime()
	{
		int temp = Integer.parseInt((String)this.getProperty("Time").getValue());
		return temp;
	}

	private boolean isSetLastGameTime() 
	{
		return Boolean.parseBoolean((String) assurePropertyValue("SetLastGameTime", "False"));
	}

}
