package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import tables.*;

/**
 * 内容处理程序，用于第三轮扫描中间结果文档，为每个元素加上uof:locID和uof:attrList属性。
 * 
 * @author xie
 *
 */
public class Third_Handler extends DefaultHandler {
	
	public Third_Handler() {
		
	}
	
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException{
		String originAtts = "";
		String newAtts = "";
		
		for (int att = 0; att < atts.getLength(); att++) {
			originAtts += " " + atts.getQName(att) + "=\"" 
					+ atts.getValue(atts.getQName(att)) + "\"";	   
		}
		
		if(atts.getValue("uof:locID") == null){
			newAtts = UOF_LocID_Table.get_locID_atts(qName);
			newAtts = (newAtts==null) ? "" : newAtts;
		}
	
		Results_Processor.write_final_file("<" + qName + originAtts + newAtts + ">");
	}
	
	public void endElement(String namespaceURI,String localName, String qName) throws SAXException{
		Results_Processor.write_final_file("</" + qName + ">");
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  {
		String chs = new String(ch, start, length);

		Results_Processor.write_final_file(chs);
	}
	
	public void error(SAXParseException exception) {
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
