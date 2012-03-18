package Model;

import org.w3c.dom.Element;

public class BehaviorTowerBasic extends Behavior {
	private final TowerBasic m_tower;

	public BehaviorTowerBasic(GameNode owner, Element e) 
	{
		super(owner, e);
		m_tower = (TowerBasic)owner;
		if (!this.m_properties.containsKey("ActionTick")) addProperty("ActionTick", "0");
		if (!this.m_properties.containsKey("ShotTick")) addProperty("ShotTick", "0");
		this.assurePropertyValue("Type", "Model.BehaviorTowerBasic");
	}
	
	public BehaviorTowerBasic(GameNode owner)
	{
		super(owner);
		m_tower = (TowerBasic)owner;
		if (!this.m_properties.containsKey("ActionTick")) addProperty("ActionTick", "0");
		if (!this.m_properties.containsKey("ShotTick")) addProperty("ShotTick", "0");
		this.assurePropertyValue("Type", "Model.BehaviorTowerBasic");
	}
	
	

	@Override
	public void tick(int aGameTime) 
	{
		evaluateState(aGameTime);
		int shotTick = m_tower.getShotTick();
		
		if (shotTick == m_tower.getShotTimer())
		{
			m_tower.setShotTick(0);
			for ( Zombie zombie : getRootNode().getZombies())
			{
				if( m_tower.getPosition().x > zombie.getPosition().x){
					m_tower.setState(ModelState.ACTION);
					addShot(m_tower, aGameTime);
					break;
				}
			}
		}
		m_tower.moveTo(m_tower.getPosition());
	}

	private void addShot(Tower tower, int gameTime) {
		m_tower.m_toAdd.add(new Projectile(m_tower, m_tower.getMuzzlePosition(), new SpeedVector(-15, 0)));
		for (int i = 1 ; i < m_tower.getUpgradeLevel(); i++)
		{
			if(m_tower.getUpgradeLevel() > 1 )
			{
				m_tower.m_toAdd.add(new Projectile(m_tower, m_tower.getMuzzlePosition(), new SpeedVector(-15, 0+i)));
				m_tower.m_toAdd.add(new Projectile(m_tower, m_tower.getMuzzlePosition(), new SpeedVector(-15, 0-i)));
			}
		}
	}

	private void evaluateState(int aGameTime) 
	{
		int actionTick = m_tower.getActionTick();
		int shotTick = m_tower.getShotTick();
		if(m_tower.isDead())
		{
			m_tower.setToRemove();
		}
		else if( (actionTick < 12) && (m_tower.getState() == ModelState.ACTION))
		{
			m_tower.setState(ModelState.ACTION);
			actionTick++;
			shotTick++;
		}
		else
		{
			actionTick = 0;
			shotTick++;
			m_tower.setState(ModelState.STILL);
		}
		m_tower.setActionTick(actionTick);
		m_tower.setShotTick(shotTick);
	}

}
