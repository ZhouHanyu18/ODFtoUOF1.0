package text;

import org.xml.sax.Attributes;
import convertor.Unit_Converter;

/**
 * 处理<text:notes-configuration> 到 <字:脚注设置>/<字:尾注设置>、
 * 	  <text:linenumbering-configuration> 到 <字:行号设置>的转换。
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
				config = "<字:脚注设置";
				config += " 字:位置=\"page-bottom\"";
			}
			else {
				config = "<字:尾注设置";
				config += " 字:位置=\"doc-end\"";
			}
			
			attVal = conv_format(atts.getValue("style:num-format"));
			config += " 字:格式=\"" + attVal + "\"";
			
			int startNum = 1;
			attVal = atts.getValue("text:start-value");
			if(attVal != null){
				startNum = Integer.parseInt(attVal) + 1;
			}
			config += " 字:起始编号=\"" + startNum + "\"";
			
			config += " 字:编号方式=\"continuous\"";
			config += "/>";
			
			_result = config;
		}
		
		else if(qName.equals("text:linenumbering-configuration")){
			config = "<字:行号设置";
			
			attVal = atts.getValue("text:number-lines");
			attVal = (attVal == null) ? "true" : attVal;
			config += " 字:使用行号=\"" + attVal + "\"";
			
			config += " 字:编号方式=\"page\"";
			
			int startNum = 1;
			attVal = atts.getValue("text:start-value");
			if(attVal != null){
				startNum = Integer.parseInt(attVal) + 1;
			}
			config += " 字:起始编号=\"" + startNum + "\"";
			
			attVal = atts.getValue("text:offset");
			if(attVal != null){
				attVal = Unit_Converter.convert(attVal);
				config += " 字:距边界=\"" + attVal + "\"";
			}
			
			attVal = atts.getValue("text:increment");
			attVal = (attVal==null) ? "1" : attVal;
			config += " 字:行号间隔=\"" + attVal + "\"";
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
		else if(val.equals("子, 丑, 寅, ...")){
			format = "ideograph-zodiac";
		}
		else if(val.equals("一, 二, 三, ...")){
			format = "chinese-counting";
		}
		else if(val.equals("壹, 贰, 叁, ...")){
			format = "chinese-legal-simplified";
		}
		else if(val.equals("甲, 乙, 丙, ...")){
			format = "ideograph-traditional";
		}
		else if(val.equals("①, ②, ③, ...")){
			format = "decimal-enclosed-circle-chinese";
		}
		else if(val.equals("１, ２, ３, ...")){
			format = "decimal-full-width";
		}
		
		return format;
	}
}
