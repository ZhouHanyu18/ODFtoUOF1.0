package graphic_content;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import stored_data.*;


public class OLEType_Handler extends DefaultHandler {
	
	public OLEType_Handler() { 
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException 
	{
		if (qName.equals("office:chart"))
			Chart_Data.set_ole_type("chart");
		if (qName.equals("math:math"))
			Chart_Data.set_ole_type("formula");
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException 
	{
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	}
	
	public void error(SAXParseException exception) 
	{
		System.err.println("Error parsing the file: "+exception.getMessage());
	}
	
	public void warning(SAXParseException exception) 
	{
		System.err.println("Warning parsing the file: "+exception.getMessage());
	}
	
	public void fatalError(SAXParseException exception) 
	{
		System.err.println("Fatal error parsing the file: "+exception.getMessage());
		System.err.println("Cannot continue.");
	}
	
}