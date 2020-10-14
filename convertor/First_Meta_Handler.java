package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import stored_data.Meta_Data;

/**
 * ���ݴ���������ڵ�һ��ɨ��meta.xml������ڶ���Ԫ�أ�����Ҫ�洢����Ϣ��ȡ�������洢��
 * 
 * @author xie
 *
 */
public class First_Meta_Handler extends DefaultHandler {
	private String _text_node = "";   				//���ڴ洢�ı��ڵ��ֵ
	private boolean _need_to_store_text = false; 	//��־�Ƿ���Ҫ�洢�ı��ڵ�
	private String _udm = "";   					//�û��Զ���Ԫ����
	
	public First_Meta_Handler(){
		
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException{
		
		String value = "";
		
		Convertor_ODF_To_UOF.write_source_ta("parsing <" + qName + ">...\n");
		
		if (qName.equals("meta:user-defined")) {
			 _udm = "<uof:�û��Զ���Ԫ����";
			if ((value = atts.getValue("meta:name")) != null)
				_udm += " uof:����=\"" + value + "\"";
			if ((value = atts.getValue("meta:type")) != null) {
				if (value.equals("date"))
					_udm += " uof:����=\"datetime\"";
				else if (!value.equals("time"))
					_udm += " uof:����=\"" + value + "\"";
			}
			_udm += ">";
			_need_to_store_text = true;
		}
		else if (qName.equals("meta:printed-by")) {
			_udm = "<uof:�û��Զ���Ԫ���� uof:����=\"����ӡ��\" uof:����=\"string\">";
			_need_to_store_text = true;
		}
		else if (qName.equals("dc:date")) {
			_udm = "<uof:�û��Զ���Ԫ���� uof:����=\"����޸�ʱ��\" uof:����=\"datetime\">";
			_need_to_store_text = true;
		}
		else if (qName.equals("meta:print-date")) {
			_udm = "<uof:�û��Զ���Ԫ���� uof:����=\"����ӡʱ��\" uof:����=\"datetime\">";
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
			_udm += _text_node + "</uof:�û��Զ���Ԫ����>";
			Meta_Data.add_udm(_udm);
			_udm = "";
			_text_node = "";
			_need_to_store_text = false;
		}
		else if (qName.equals("meta:keyword")) {
			Meta_Data.add_keyword("<uof:�ؼ���>" + _text_node + "</uof:�ؼ���>");
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
