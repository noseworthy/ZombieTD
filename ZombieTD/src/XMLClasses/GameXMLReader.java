package XMLClasses;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class GameXMLReader implements XMLReader
{

	@Override
	public ContentHandler getContentHandler() 
	{
		return null;
	}

	@Override
	public DTDHandler getDTDHandler() 
	{
		return null;
	}

	@Override
	public EntityResolver getEntityResolver() 
	{
		return null;
	}

	@Override
	public ErrorHandler getErrorHandler() 
	{
		return null;
	}

	@Override
	public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException 
	{
		return false;
	}

	@Override
	public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException
	{
		return null;
	}

	@Override
	public void parse(InputSource input) throws IOException, SAXException 
	{
		
	}

	@Override
	public void parse(String systemId) throws IOException, SAXException 
	{
		
	}

	@Override
	public void setContentHandler(ContentHandler handler) 
	{
		
	}

	@Override
	public void setDTDHandler(DTDHandler handler)
	{
		
	}

	@Override
	public void setEntityResolver(EntityResolver resolver) {
		
	}

	@Override
	public void setErrorHandler(ErrorHandler handler) 
	{
		
	}

	@Override
	public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException 
	{
		
	}

	@Override
	public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException 
	{
		
	}

}
