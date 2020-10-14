package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import stored_data.Meta_Data;

/**
 * 内容处理程序，用于第一轮扫描meta.xml，处理第二类元素，将需要存储的信息提取出来并存储。
 * 
 * @author xie
 *
 */
public class First_Meta_Handler extends DefaultHandler {
	private String _text_node = "";   				//用于存储文本节点的值
	private boolean _need_to_store_text = false; 	//标志是否需要存储文本节点
	private String _udm = "";   					//用户自定义元数据
	
	public First_Meta_Handler(){
		
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException{
		
		String value = "";
		
		Convertor_ODF_To_UOF.write_source_ta("parsing <" + qName + ">...\n");
		
		if (qName.equals("meta:user-defined")) {
			 _udm = "<uof:用户自定义元数据";
			if ((value = atts.getValue("meta:name")) != null)
				_udm += " uof:名称=\"" + value + "\"";
			if ((value = atts.getValue("meta:type")) != null) {
				if (value.equals("date"))
					_udm += " uof:类型=\"datetime\"";
				else if (!value.equals("time"))
					_udm += " uof:类型=\"" + value + "\"";
			}
			_udm += ">";
			_need_to_store_text = true;
		}
		else if (qName.equals("meta:printed-by")) {
			_udm = "<uof:用户自定义元数据 uof:名称=\"最后打印者\" uof:类型=\"string\">";
			_need_to_store_text = true;
		}
		else if (qName.equals("dc:date")) {
			_udm = "<uof:用户自定义元数据 uof:名称=\"最后修改时间\" uof:类型=\"datetime\">";
			_need_to_store_text = true;
		}
		else if (qName.equals("meta:print-date")) {
			_udm = "<uof:用户自定义元数据 uof:名称=\"最后打印时间\" uof:类型=\"datetime\">";
			_need_to_store_text = true;
		}
		else if (qName.equals("meta:keyword")) {
			_need_to_store_text = true;
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException{
		
		Convertor_ODF_To_UOF.write_source_ta("parsing </" + qName + ">...\n");
		
		if (qName.equals("meta:user-defined") || qName.equals("meta:printed-by") || qName.equals("dc:date")
				|| qName.equals("meta:print-date")) {
			_udm += _text_node + "</uof:用户自定义元数据>";
			Meta_Data.add_udm(_udm);
			_udm = "";
			_text_node = "";
			_need_to_store_text = false;
		}
		else if (qName.equals("meta:keyword")) {
			Meta_Data.add_keyword("<uof:关键字>" + _text_node + "</uof:关键字>");
			_text_node = "";
			_need_to_store_text = false;
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  
	{
		if (_need_to_store_text) {
			String chs = new String(ch, start, length);
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
