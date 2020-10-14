package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import tables.Meta_Table;
import stored_data.*;

/**
 * 内容处理程序，用于第二轮扫描meta.xml，处理前两类元素，将转换后的结果写到中间结果文档中。
 * 
 * @author xie
 *
 */
public class Second_Meta_Handler extends DefaultHandler{
	private String _text_node = "";
	private boolean _need_to_store_text = false;
	
	public Second_Meta_Handler()
	{	
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		String result = "";
		
		if (qName.equals("office:document-meta"))
			result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><uof:UOF xmlns:uof=\"http://schemas.uof.org/cn/2003/uof\" xmlns:图=\"http://schemas.uof.org/cn/2003/graph\" xmlns:字=\"http://schemas.uof.org/cn/2003/uof-wordproc\" xmlns:表=\"http://schemas.uof.org/cn/2003/uof-spreadsheet\" xmlns:演=\"http://schemas.uof.org/cn/2003/uof-slideshow\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://schemas.uof.org/cn/2003/uof D:\\UOF\\uof_schema\\uof.xsd\" uof:language=\"cn\" uof:version=\"1.0\">";
		else if (qName.equals("office:meta"))
			result = "<uof:元数据>";
		else if (qName.equals("meta:document-statistic")) {
			if (atts.getValue("meta:page-count") != null)
				result += "<uof:页数>" + atts.getValue("meta:page-count") + "</uof:页数>";
			if (atts.getValue("meta:word-count") != null)
				result += "<uof:字数>" + atts.getValue("meta:word-count") + "</uof:字数>";
			if (atts.getValue("meta:row-count") != null)
				result += "<uof:行数>" + atts.getValue("meta:row-count") + "</uof:行数>";
			if (atts.getValue("meta:paragraph-count") != null)
				result += "<uof:段落数>" + atts.getValue("meta:paragraph-count") + "</uof:段落数>";
			if (atts.getValue("meta:ole-object-count") != null)
				result += "<uof:对象数>" + atts.getValue("meta:ole-object-count") + "</uof:对象数>";
			if (atts.getValue("meta:object-count") != null)
				result += "<uof:对象数>" + atts.getValue("meta:object-count") + "</uof:对象数>";
		}
		else if (qName.equals("meta:template")) {
			result = "<uof:文档模板>" + atts.getValue("xlink:href") + "</uof:文档模板>";
		}
		else if (Meta_Table.get_ele_name(qName) != null) {
			_need_to_store_text = true;
			result = "<" + Meta_Table.get_ele_name(qName) + ">";
		}
		
		Results_Processor.process_result(result);
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		String result = "";
		
		if (qName.equals("office:meta")) {
			result = Meta_Data.get_keyword_set() + Meta_Data.get_udm_set() + "</uof:元数据>";
		}
		else if (Meta_Table.get_ele_name(qName) != null) {
			result = _text_node + "</" + Meta_Table.get_ele_name(qName) + ">";
		}
		
		Results_Processor.process_result(result);
		
		_text_node = "";
		_need_to_store_text = false;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  
	{
		if (_need_to_store_text) {
			String chs = new String(ch, start, length).trim();
			_text_node += chs;			
		}
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
