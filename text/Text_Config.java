package text;

import org.xml.sax.Attributes;
import convertor.Unit_Converter;

/**
 * ����<text:notes-configuration> �� <��:��ע����>/<��:βע����>��
 * 	  <text:linenumbering-configuration> �� <��:�к�����>��ת����
 * 
 * @author xie
 *
 */
public class Text_Config {
	//the result
	private static String _result = "";
	
	
	public static String get_result(){
		String rst = "";
		
		rst = _result;
		_result = "";
		return _result;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		String config = "";	
		
		if(qName.equals("text:notes-configuration")){
			String type = "";
			
			type = atts.getValue("text:note-class");
			if(type != null && type.equals("footnote")){
				config = "<��:��ע����";
				config += " ��:λ��=\"page-bottom\"";
			}
			else {
				config = "<��:βע����";
				config += " ��:λ��=\"doc-end\"";
			}
			
			attVal = conv_format(atts.getValue("style:num-format"));
			config += " ��:��ʽ=\"" + attVal + "\"";
			
			int startNum = 1;
			attVal = atts.getValue("text:start-value");
			if(attVal != null){
				startNum = Integer.parseInt(attVal) + 1;
			}
			config += " ��:��ʼ���=\"" + startNum + "\"";
			
			config += " ��:��ŷ�ʽ=\"continuous\"";
			config += "/>";
			
			_result = config;
		}
		
		else if(qName.equals("text:linenumbering-configuration")){
			config = "<��:�к�����";
			
			attVal = atts.getValue("text:number-lines");
			attVal = (attVal == null) ? "true" : attVal;
			config += " ��:ʹ���к�=\"" + attVal + "\"";
			
			config += " ��:��ŷ�ʽ=\"page\"";
			
			int startNum = 1;
			attVal = atts.getValue("text:start-value");
			if(attVal != null){
				startNum = Integer.parseInt(attVal) + 1;
			}
			config += " ��:��ʼ���=\"" + startNum + "\"";
			
			attVal = atts.getValue("text:offset");
			if(attVal != null){
				attVal = Unit_Converter.convert(attVal);
				config += " ��:��߽�=\"" + attVal + "\"";
			}
			
			attVal = atts.getValue("text:increment");
			attVal = (attVal==null) ? "1" : attVal;
			config += " ��:�кż��=\"" + attVal + "\"";
			config += "/>";
			
			_result += config;
		}
	}
	
	public static String conv_format(String val){
		String format = "decimal";
		
		val = (val==null) ? "" : val;
		if(val.equals("1")){
			format = "decimal";
		}
		else if(val.equals("a")){
			format = "lower-letter";
		}
		else if(val.equals("A")){
			format = "upper-letter";
		}
		else if(val.equals("i")){
			format = "lower-roman";
		}
		else if(val.equals("I")){
			format = "upper-roman";
		}
		else if(val.equals("��, ��, ��, ...")){
			format = "ideograph-zodiac";
		}
		else if(val.equals("һ, ��, ��, ...")){
			format = "chinese-counting";
		}
		else if(val.equals("Ҽ, ��, ��, ...")){
			format = "chinese-legal-simplified";
		}
		else if(val.equals("��, ��, ��, ...")){
			format = "ideograph-traditional";
		}
		else if(val.equals("��, ��, ��, ...")){
			format = "decimal-enclosed-circle-chinese";
		}
		else if(val.equals("��, ��, ��, ...")){
			format = "decimal-full-width";
		}
		
		return format;
	}
}
