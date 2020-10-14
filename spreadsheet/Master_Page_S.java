package spreadsheet;

import org.xml.sax.Attributes;
import text.Text_Content;

public class Master_Page_S {
	//the result
	private static String _result = "";
	//"footer" or "header"
	private static String _mp_type = "";
	//tag for text content
	private static boolean _text_content_tag = false;
	
	
	public static String get_result(){
		String rst = "";
		
		rst = _result;
		_result = "";
		
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:header")){
			_mp_type = "header";
		}
		else if(qName.equals("style:footer")){
			_mp_type = "footer";
		}
		
		else if(_text_content_tag){
			Text_Content.process_start(qName,atts);
		}
		else if(qName.equals("style:region-left") 
				|| qName.equals("style:region-center") 
				|| qName.equals("style:region-right")){			
			attVal = atts.getValue("style:display");
			
			if(attVal == null || !attVal.equals("false")){
				_text_content_tag = true;
			}
		}
	}
	
	public static void process_chars(String chs){
		if(_text_content_tag){
			Text_Content.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		if(_text_content_tag){
			Text_Content.process_end(qName);
			
			if(qName.equals("style:region-left")){
				_text_content_tag = false;
				_result += footer_or_header(Text_Content.get_result(), _mp_type + "left");
			}
			else if(qName.equals("style:region-right")){
				_text_content_tag = false;
				_result += footer_or_header(Text_Content.get_result(), _mp_type + "right");
			}
			else if(qName.equals("style:region-center")){
				_text_content_tag = false;
				_result += footer_or_header(Text_Content.get_result(), _mp_type + "center");
			}
		}
	}
	
	private static String footer_or_header(String content,String position){
		String fh = "";
		
		fh = "<±í:Ò³Ã¼Ò³½Å ±í:Î»ÖÃ=\"" + position + "\">";
		fh += content;
		fh += "</±í:Ò³Ã¼Ò³½Å>";
		
		return fh;
	}
}
