package graphic_content;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import stored_data.*;

public class ObjectProcessor {

	public ObjectProcessor() {
	}
	
	public static String process_formula(String xLinkHref)
	{
		//To do. For formula
		return "";
	}
	
	public static String process_chart(String xLinkHref)
	{
		Chart.clear();
		String inputPath = "testfile" + xLinkHref + "/content.xml";
		XMLReader xmlReader = null;
		try {
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			spfactory.setValidating(false);
			SAXParser saxParser = spfactory.newSAXParser();
			xmlReader = saxParser.getXMLReader();	
			InputSource source = new InputSource(inputPath);
			DefaultHandler chart_handler = new Chart_Handler();
			xmlReader.setContentHandler(chart_handler);
			xmlReader.setErrorHandler(chart_handler);	
			xmlReader.parse(source);	
		} catch (Exception exception) {	
			exception.printStackTrace();
			System.err.println(exception);	
		}	
		return Chart.get_chart_string();
	}
	
	public static String get_obj_type(String xLinkHref) 
	{
		String inputPath = "testfile" + xLinkHref + "/content.xml";
		XMLReader xmlReader = null;
		try {
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			spfactory.setValidating(false);
			SAXParser saxParser = spfactory.newSAXParser();
			xmlReader = saxParser.getXMLReader();	
			InputSource source = new InputSource(inputPath);
			DefaultHandler oletype_handler = new OLEType_Handler();
			xmlReader.setContentHandler(oletype_handler);
			xmlReader.setErrorHandler(oletype_handler);	
			xmlReader.parse(source);	
		} catch (Exception exception) {	
			exception.printStackTrace();
			System.err.println(exception);	
		}
		return Chart_Data.get_ole_type();
	}
}
