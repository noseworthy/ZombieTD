package Model;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class Property extends Observable {
	private final String m_name;
	private Object m_value = null;
	private final GameNode m_owner;
	
	public Property(GameNode owner, String aName, Object aValue) 
	{
		m_owner = owner;
		m_name = aName;
		m_value = aValue;
		try
		{
			if ( ((String)m_value).length() >= 2 && ((String)m_value).substring(0, 2).compareToIgnoreCase("r:") == 0 ) evaluateRandom();
		}catch (Throwable th) {}
	}
	
	public Property(GameNode owner, String aName, Object aValue, Observer observer) 
	{
		this.addObserver(observer);
		m_owner = owner;
		m_name = aName;
		m_value = aValue;
		try
		{
			if ( ((String)m_value).length() >= 2 && ((String)m_value).substring(0, 2).compareToIgnoreCase("r:") == 0 ) evaluateRandom();
		}catch (Throwable th) {}
		this.setChanged();
		this.notifyObservers(this);
	}
	
	public Property(GameNode owner, Node n) 
	{
		m_owner = owner;
		m_name = n.getNodeName();
		m_value = n.getNodeValue();
		try
		{
			if ( ((String)m_value).length() >= 2 && ((String)m_value).substring(0, 2).compareToIgnoreCase("r:") == 0 ) evaluateRandom();
		}catch (Throwable th) {}
	}
	
	private void evaluateRandom()
	{
		String str = ((String)m_value).substring(2, ((String)m_value).length());
		while ( str.charAt(0) == ' ' ) str = str.substring(1, str.length());
		int numberOfValues = 0;
		for ( char a: str.toCharArray()) if ( a == ',' )numberOfValues++;
		Random rand = new Random();
		
		String newStr = "";
		for ( int i = 0; i < numberOfValues; i++)
		{
			while ( str.charAt(0) == ' ' ) str = str.substring(1, str.length());
			int val1;
			try
			{
				val1 = Integer.parseInt(str.substring(0, str.indexOf(',')));
			}
			catch (Throwable th)
			{
				val1 = (int) Double.parseDouble(str.substring(0, str.indexOf(',')));
			}
			str = str.substring(str.indexOf(',')+1, str.length());
			while ( str.charAt(0) == ' ' ) str = str.substring(1, str.length());
			int val2;
			if 
				(str.contains(","))
			{
				try
				{
					val2 = Integer.parseInt(str.substring(0, str.indexOf(' ')));
				}
				catch (Throwable th)
				{
					val2 = (int) Double.parseDouble(str.substring(0, str.indexOf(' ')));
				}
				str = str.substring(str.indexOf(' ') + 1);
			}
			else 
			{
				try
				{
					val2 = Integer.parseInt(str.substring(0, str.length()));
				}
				catch (Throwable th)
				{
					val2 = (int) Double.parseDouble(str.substring(0, str.length()));
				}
			}
			int val;
			if ( val1 >= 0) 
				val = val1 + rand.nextInt(val2);
			else
				val = val2 - rand.nextInt(val2 - val1);
			
			//index = str.indexOf(Integer.toString(val2) + Integer.toString(val2).length());
			newStr += Integer.toString(val) + " ";
		}
		while ( newStr.charAt(newStr.length() - 1) == ' ' ) newStr = newStr.substring(0, newStr.length() - 1);
		if ( newStr == null ) newStr = "invalid random";
		m_value = newStr;
	}

	public void setValue(Object aObj) 
	{
		m_value = aObj;
		try
		{
			if ( ((String)m_value).length() >= 2 && ((String)m_value).substring(0, 2).compareToIgnoreCase("r:") == 0 ) evaluateRandom();
		}catch (Throwable th) {}
		this.setChanged();
		notifyObservers(this);
	}
	
	public GameNode getOwner() {return m_owner;}
	
	public Object getValue() 
	{
		return m_value;
	}

	public String getName() 
	{
		return m_name;
	}

	public void buildXml(Element e) 
	{
		e.setAttribute(m_name, (String) m_value);
	}
	
	public String toString()
	{
		return this.m_name + ": " + m_value;
	}
	
}