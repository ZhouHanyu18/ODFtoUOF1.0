package text;

import org.xml.sax.Attributes;

/**
 * ´¦Àí Ä¿Â¼ µÄ×ª»»¡£
 * 
 * @author xie
 *
 */
public class Text_TOC {
	private static String _result = "";
	private static boolean _para_tag = false;

	public static String get_result(){
		String rst = _result;
		_result = "";
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		
		if(_para_tag){
			Text_P.process_start(qName,atts);
		}
		else if(qName.equals("text:p")){
			_para_tag = true;
			Text_P.process_start(qName,atts);
		}
	}
	
	public static void process_chars(String chs){
		if(_para_tag){
			Text_P.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("text:p")){
			_para_tag = false;
			Text_P.process_end(qName);
			_result += Text_P.get_result();
		}
		else if(_para_tag){
			Text_P.process_end(qName);
		}
		
		else if(qName.equals("text:index-title")){
			String tocStart = "";
			
			tocStart += "<×Ö:¶ÎÂä><×Ö:¶ÎÂäÊôÐÔ/>";
			tocStart += "<×Ö:Óò¿ªÊ¼ ×Ö:ÀàÐÍ=\"ref\" ×Ö:Ëø¶¨=\"false\"/>";
			tocStart += "<×Ö:Óò´úÂë><×Ö:¶ÎÂä>";
			tocStart += "<×Ö:¾ä><×Ö:¾äÊôÐÔ/>";
			tocStart += "<×Ö:ÎÄ±¾´®>TOC \\o\"1-3\" \\h \\z </×Ö:ÎÄ±¾´®>";
			tocStart += "</×Ö:¾ä>";
			tocStart += "</×Ö:¶ÎÂä></×Ö:Óò´úÂë>";
			tocStart += "</×Ö:¶ÎÂä>";
			
			_result += tocStart;
		}
		
		else if(qName.equals("text:index-body")){
			String tocEnd = "";
			
			tocEnd += "<×Ö:¶ÎÂä><×Ö:¶ÎÂäÊôÐÔ/>";
			tocEnd += "<×Ö:Óò½áÊø/>";
			tocEnd += "</×Ö:¶ÎÂä>";
			
			_result += tocEnd;
		}
	}
}
