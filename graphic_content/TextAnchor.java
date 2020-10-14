package graphic_content;

import org.xml.sax.Attributes;

public class TextAnchor {

	private String _id = "";   					//��ʶ��
	
	private String _begin_element = "<��:ê��";
	private String _drawing = "";   			//ͼ��
	private String _anchor_atts = "";   		//ê������
	private String _end_element = "</��:ê��>";
	private String _type = "";
	
	public TextAnchor() {
	}
	
	public void set_id(String id) {
		_id = id;
	}
	
	public String get_id() {
		return _id;
	}
	
	public void set_type(String type) {
		_type = type;
	}
	
	public void add_begin_element(String string) {
		_begin_element += string;
	}
	
	public void set_anchor_atts(String string) {
		_anchor_atts = string;
	}
	
	public String get_anchor_atts()
	{
		return 	_anchor_atts;
	}
	
	public void add_drawing(String drawString) {
		_drawing += drawString;
	}
	
	public void process_atts(Attributes atts) {
		String value = "";
		
		if ((value = atts.getValue("text:anchor-type")) != null) {
			if (value.equals("as-char"))
				_type = "inline";
			else
				_type = "normal";
		}
	}
	
	public String get_text_anchor_string() {
		return (_begin_element + " ��:����=\"" + _type + "\">" + "<��:ê������>" + _anchor_atts
				+ "</��:ê������>" + _drawing + _end_element);
	}
}
