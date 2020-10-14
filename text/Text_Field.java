package text;

import java.util.Set;
import java.util.TreeSet;
import org.xml.sax.Attributes;

/**
 * ´¦Àí<text:date>¡¢<text:time>¡¢<text:word-count>¡¢<text:file-name>¡¢<text:character-count>¡¢
 * <text:editing-duration>¡¢<text:page-number>¡¢<text:page-count>µÈ µ½ ×Ö:ÓòµÄ×ª»»¡£
 * 
 * @author xie
 *
 */
public class Text_Field {
	//
	private static String _chs = "";
	//the result
	private static String _result = ""; 
	//@text:*-value
	//private static String _value = "";
	//@text:fixed
	private static String _fixed = "";
	//×Ö:Óò´úÂë
	private static String _code = "";
	//the collection of text-field's names
	private static Set<String> _field_name_set = new TreeSet<String>();
	
	
	public static void create_set(){
		
		_field_name_set.add("text:date");				//ÈÕÆÚ
		
		_field_name_set.add("text:time");				//Ê±¼ä
		
		_field_name_set.add("text:word-count");			//×ÖÊý
		
		_field_name_set.add("text:file-name");			//Â·¾¶Ãû³Æ
		
		_field_name_set.add("text:character-count");	//×Ö·ûÊý
		
		_field_name_set.add("text:editing-duration");	//±à¼­Ê±¼ä
		
		_field_name_set.add("text:page-number");		//Ò³Âë
		
		_field_name_set.add("text:page-count");			//Ò³Êý
		
		_field_name_set.add("text:initial-creator");	//×÷Õß
		
		_field_name_set.add("text:editing-cycles");		//ÐÞ¸Ä´ÎÊý
		
		_field_name_set.add("text:sequence");			//Ìâ×¢
	}
	
	public static boolean is_field_name(String qName){
		return _field_name_set.contains(qName);
	}
	
	private static void clear(){
		_chs = "";
		_result = "";	
		_fixed = "";
		_code = "";
	}
	
	public static String get_result(){
		String str = _result;
		clear();
		return str;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		_fixed = atts.getValue("text:fixed");
		_fixed = (_fixed==null) ? "false" : _fixed;
		
		if(qName.equals("text:sequence")){
			String format = atts.getValue("style:num-format");
			
			attVal = atts.getValue("text:name");
			if(attVal != null){
				_code = "Seq " + attVal + " \\* " + conv_num_format(format);
			}
		}
	}
	
	public static void process_chars(String chs){	
		_chs += chs;
	}
	
	public static void process_end(String qName){	
		if(qName.equals("text:date")){
			_result += gen_a_field("Date",_fixed,_chs);
		}
		
		else if(qName.equals("text:time")){
			_result += gen_a_field("Time",_fixed,_chs);
		}
		
		else if(qName.equals("text:character-count")){
			_result += gen_a_field("NumChars",_fixed,_chs);
		}
		
		else if(qName.equals("text:word-count")){
			_result += gen_a_field("NumWords",_fixed,_chs);
		}
		
		else if(qName.equals("text:file-name")){
			_result += gen_a_field("FileName",_fixed,_chs);
		}
		
		else if(qName.equals("text:editing-duration")){
			_result += gen_a_field("EditTime",_fixed,_chs);
		}
		
		else if(qName.equals("text:page-number")){
			_result += gen_a_field("Page",_fixed,_chs);
		}

		else if(qName.equals("text:page-count")){
			_result += gen_a_field("NumPages",_fixed,_chs);
		}
		
		else if(qName.equals("text:initial-creator")){
			_result += gen_a_field("Author",_fixed,_chs);
		}
		
		else if(qName.equals("text:editing-cycles")){
			_result += gen_a_field("RevNum",_fixed,_chs);
		}
		
		else if(qName.equals("text:sequence")){
			_result += gen_sequence_field(_code,_fixed,_chs);
		}
		
		_chs = "";
	}
	
	private static String gen_a_field(String type,String fixed,String chs){
		String field = "";
		
		field = "<×Ö:Óò¿ªÊ¼ ×Ö:ÀàÐÍ=\"" + type + "\" ×Ö:Ëø¶¨=\"" + fixed + "\"/>";
		field += "<×Ö:Óò´úÂë>";
		field += "<×Ö:¶ÎÂä><×Ö:¾ä><×Ö:¾äÊôÐÔ/>";
		field += "<×Ö:ÎÄ±¾´®>" + get_field_code(type) + "</×Ö:ÎÄ±¾´®>";
		field += "</×Ö:¾ä></×Ö:¶ÎÂä>";
		field += "</×Ö:Óò´úÂë>";
		field += "<×Ö:¾ä><×Ö:¾äÊôÐÔ/>";
		field += "<×Ö:ÎÄ±¾´®>" + chs + "</×Ö:ÎÄ±¾´®>";
		field += "</×Ö:¾ä>";
		field += "<×Ö:Óò½áÊø/>";
		field += "<×Ö:¾ä><×Ö:¾äÊôÐÔ/></×Ö:¾ä>";
		
		return field;
	}
	
	private static String gen_sequence_field(String code,String fixed,String chs){
		String field = "";
		
		field = "<×Ö:Óò¿ªÊ¼ ×Ö:ÀàÐÍ=\"Seq\" ×Ö:Ëø¶¨=\"" + fixed + "\"/>";
		field += "<×Ö:Óò´úÂë>";
		field += "<×Ö:¶ÎÂä><×Ö:¾ä><×Ö:¾äÊôÐÔ/>";
		field += "<×Ö:ÎÄ±¾´®>" + _code + "</×Ö:ÎÄ±¾´®>";
		field += "</×Ö:¾ä></×Ö:¶ÎÂä>";
		field += "</×Ö:Óò´úÂë>";
		field += "<×Ö:¾ä><×Ö:¾äÊôÐÔ/>";
		field += "<×Ö:ÎÄ±¾´®>" + chs + "</×Ö:ÎÄ±¾´®>";
		field += "</×Ö:¾ä>";
		field += "<×Ö:Óò½áÊø/>";
		field += "<×Ö:¾ä><×Ö:¾äÊôÐÔ/></×Ö:¾ä>";
		
		return field;
	}
	
	private static String get_field_code(String type){
		String code = "";
		
		if(type.equals("Date")){
			code = "DATE \\@ \"yyyyÄêMÔÂdÈÕ\"";
		}
		else if(type.equals("Time")){
			code = "\\@ \"h:mm:ss AM/PM\"";
		}
		else if(type.equals("NumChars")){
			code = "NUMCHARS \\* MERGEFORMAT";
		}
		else if(type.equals("FileName")){
			code = "FileName \\p \\* MERGEFORMAT";
		}
		else if(type.equals("NumWords")){
			code = "NUMWORDS  \\* MERGEFORMAT";
		}
		else if(type.equals("EditTime")){
			code = "EDITTIME  \\* MERGEFORMAT";
		}
		else if(type.equals("Page")){
			code = "PAGE \\* MERGEFORMAT";
		}
		else if(type.equals("NumPages")){
			code = "NUMPAGES  \\* MERGEFORMAT";
		}
		else if(type.equals("Author")){
			code = "AUTHOR \\* MERGEFORMAT";
		}
		else if(type.equals("RevNum")){
			code = "REVNUM  \\* MERGEFORMAT";
		}
		
		return code;
	}

	private static String conv_num_format(String odfVal){
		String uofVal = "Arabic";
		
		if(odfVal == null){
			
		}
		else if(odfVal.equals("1")){
			uofVal = "Arabic";
		}
		else if(odfVal.equals("a")){
			uofVal = "alphabetic";
		}
		else if(odfVal.equals("A")){
			uofVal = "ALPHABETIC";
		}
		else if(odfVal.equals("i")){
			uofVal = "roman";
		}
		else if(odfVal.equals("I")){
			uofVal = "ROMAN";
		}
		else if(odfVal.equals("Ò», ¶þ, Èý, ...")){
			uofVal = "CHINESENUM3";
		}
		else if(odfVal.equals("Ò¼, ·¡, Èþ, ...")){
			uofVal = "CHINESENUM2";
		}
		else if(odfVal.equals("¼×, ÒÒ, ±û, ...")){
			uofVal = "ZODIAC1";
		}
		else if(odfVal.equals("×Ó, ³ó, Òú, ...")){
			uofVal = "ZODIAC2";
		}
		else if(odfVal.equals("¢Ù, ¢Ú, ¢Û, ...")){
			uofVal = "GB3";
		}
		
		return uofVal;
	}
}
