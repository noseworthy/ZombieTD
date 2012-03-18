package Model;

public class SpeedVector 
{
	private final double m_x;
	private final double m_y;
	
	//NOTE: any newly desired velocity vectors must be created from scratch, deleting the old. 
	//VelocityVectors are not to have their properties updated. They are temporary containers.
	public SpeedVector( double xSpeed, double ySpeed ) 
	{
		m_x = xSpeed;
		m_y = ySpeed;

	}

	public double getXSpeed() { return m_x; }
	
	public double getYSpeed() { return m_y; }
	
	public boolean isStill() { return ( Double.compare(m_x, 0.0) == 0 )&&( Double.compare(m_y, 0.0) == 0); };
}
