package text;

import org.xml.sax.Attributes;
import text.Sec_Style;

/**
 * ����<style:master-page> �� <��:�ֽ�>��ת����
 * 
 * @author xie
 *
 */
public class Master_Page {
	private static String _left_header_content = "";			//ż��ҳü������
	private static String _header_content = "";					//��׼ҳü������
	private static String _left_footer_content = "";			//ż��ҳ�ŵ�����
	private static String _footer_content = "";					//��׼ҳ�ŵ�����
	//tag for text content
	private static boolean _text_content_tag = false;
	//name of first page-layout used by master-page
	private static String _first_PLname = "";
	//
	private static Sec_Style _cur_sec_style = null;				//��Ӧ��SecStyle����
	
	
	private static void clear(){
		_footer_content =  "";
		_left_footer_content = "";
		_header_content =  "";
		_left_header_content = "";
	}
	
	public static String get_result(){
		String rst = "";	
		Sec_Style first = Page_Layout.get_sec_style(_first_PLname);
		
		rst = "<��:�ֽ�>";
		if(first != null){
			rst += first.get_result();
		}
		rst += "</��:�ֽ�>";
		
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
				String str = "<��:��żҳҳüҳ�Ų�ͬ ��:ֵ=\"true\"/>";
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
	
	//ȡ��<��:ҳü>��ת�����
	private static String get_header(){
		String header = "";
		
		if(!_header_content.equals("")||!_left_header_content.equals("")){
			header += "<��:ҳü>";
			
			header += "<��:����ҳҳü>" + _header_content + "</��:����ҳҳü>";
			
			if(_left_header_content.equals("")){
				header += "<��:ż��ҳҳü>" + _header_content + "</��:ż��ҳҳü>";
			}else{
				header += "<��:ż��ҳҳü>" + _left_header_content + "</��:ż��ҳҳü>";
			}
			header += "</��:ҳü>";
		}
		
		return header;
	}
	
	//ȡ��<��:ҳ��>��ת�����
	private static String get_footer(){
		String footer = "";
		
		if(!_footer_content.equals("")||!_left_footer_content.equals("")){
			footer += "<��:ҳ��>";
			
			footer += "<��:����ҳҳ��>" + _footer_content + "</��:����ҳҳ��>";
			
			if(_left_footer_content.equals("")){
				footer += "<��:ż��ҳҳ��>" + _footer_content + "</��:ż��ҳҳ��>";
			}else{
				footer += "<��:ż��ҳҳ��>" + _left_footer_content + "</��:ż��ҳҳ��>";
			}
			footer += "</��:ҳ��>";
		}

		return footer;
	}
}
