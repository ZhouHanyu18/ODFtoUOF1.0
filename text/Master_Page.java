package text;

import org.xml.sax.Attributes;
import text.Sec_Style;

/**
 * ´¦Àí<style:master-page> µ½ <×Ö:·Ö½Ú>µÄ×ª»»¡£
 * 
 * @author xie
 *
 */
public class Master_Page {
	private static String _left_header_content = "";			//Å¼ÊýÒ³Ã¼µÄÄÚÈÝ
	private static String _header_content = "";					//±ê×¼Ò³Ã¼µÄÄÚÈÝ
	private static String _left_footer_content = "";			//Å¼ÊýÒ³½ÅµÄÄÚÈÝ
	private static String _footer_content = "";					//±ê×¼Ò³½ÅµÄÄÚÈÝ
	//tag for text content
	private static boolean _text_content_tag = false;
	//name of first page-layout used by master-page
	private static String _first_PLname = "";
	//
	private static Sec_Style _cur_sec_style = null;				//¶ÔÓ¦µÄSecStyleÒýÓÃ
	
	
	private static void clear(){
		_footer_content =  "";
		_left_footer_content = "";
		_header_content =  "";
		_left_header_content = "";
	}
	
	public static String get_result(){
		String rst = "";	
		Sec_Style first = Page_Layout.get_sec_style(_first_PLname);
		
		rst = "<×Ö:·Ö½Ú>";
		if(first != null){
			rst += first.get_result();
		}
		rst += "</×Ö:·Ö½Ú>";
		
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(_text_content_tag){
			Text_Content.process_start(qName,atts);
		}
		else if(qName.equals("style:header")||qName.equals("style:header-left")
				||qName.equals("style:footer")||qName.equals("style:footer-left")){
			_text_content_tag = true;
		}
		else if(qName.equals("style:master-page")){
			attVal = atts.getValue("style:page-layout-name");
			attVal = (attVal==null) ? "" : attVal;
			
			if(_first_PLname.equals("")){
				_first_PLname = attVal;
			}
			
			_cur_sec_style = Page_Layout.get_sec_style(attVal);
		}
	}
	
	public static void process_chars(String chs){
		if(_text_content_tag){
			Text_Content.process_chars(chs);
		}
	}
	

	public static void process_end(String qName){	
		if(qName.equals("style:header")){
			_text_content_tag = false;
			_header_content = Text_Content.get_result();
		}
		else if(qName.equals("style:header-left")){
			_text_content_tag = false;
			_left_header_content = Text_Content.get_result();
		}
		else if(qName.equals("style:footer")){
			_text_content_tag = false;
			_footer_content = Text_Content.get_result();
		}
		else if(qName.equals("style:footer-left")){
			_text_content_tag = false;
			_left_footer_content = Text_Content.get_result();
			if(_left_footer_content.length() != 0){
				String str = "<×Ö:ÆæÅ¼Ò³Ò³Ã¼Ò³½Å²»Í¬ ×Ö:Öµ=\"true\"/>";
				_cur_sec_style.set_differ_even_odd(str);
			}
		}
		else if(qName.equals("style:master-page")){
			_cur_sec_style.set_footer(get_footer());
			_cur_sec_style.set_header(get_header());
			clear();
		}
		else if(_text_content_tag){
			Text_Content.process_end(qName);
		}
	}
	
	//È¡µÃ<×Ö:Ò³Ã¼>µÄ×ª»»½á¹û
	private static String get_header(){
		String header = "";
		
		if(!_header_content.equals("")||!_left_header_content.equals("")){
			header += "<×Ö:Ò³Ã¼>";
			
			header += "<×Ö:ÆæÊýÒ³Ò³Ã¼>" + _header_content + "</×Ö:ÆæÊýÒ³Ò³Ã¼>";
			
			if(_left_header_content.equals("")){
				header += "<×Ö:Å¼ÊýÒ³Ò³Ã¼>" + _header_content + "</×Ö:Å¼ÊýÒ³Ò³Ã¼>";
			}else{
				header += "<×Ö:Å¼ÊýÒ³Ò³Ã¼>" + _left_header_content + "</×Ö:Å¼ÊýÒ³Ò³Ã¼>";
			}
			header += "</×Ö:Ò³Ã¼>";
		}
		
		return header;
	}
	
	//È¡µÃ<×Ö:Ò³½Å>µÄ×ª»»½á¹û
	private static String get_footer(){
		String footer = "";
		
		if(!_footer_content.equals("")||!_left_footer_content.equals("")){
			footer += "<×Ö:Ò³½Å>";
			
			footer += "<×Ö:ÆæÊýÒ³Ò³½Å>" + _footer_content + "</×Ö:ÆæÊýÒ³Ò³½Å>";
			
			if(_left_footer_content.equals("")){
				footer += "<×Ö:Å¼ÊýÒ³Ò³½Å>" + _footer_content + "</×Ö:Å¼ÊýÒ³Ò³½Å>";
			}else{
				footer += "<×Ö:Å¼ÊýÒ³Ò³½Å>" + _left_footer_content + "</×Ö:Å¼ÊýÒ³Ò³½Å>";
			}
			footer += "</×Ö:Ò³½Å>";
		}

		return footer;
	}
}
