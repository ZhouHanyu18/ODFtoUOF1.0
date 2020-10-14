package presentation;

import org.xml.sax.Attributes;

import text.Text_Config;
import convertor.Common_Pro;

/**
 * 处理<style:page-layout> 到 <演:页面设置>的转换。
 * 
 * @author xie
 *
 */
public class Page_Layout_p extends Common_Pro{
	//the result
	private static String _result = ""; 
	

	private static void clear(){
		_result = "";
	}
	
	public static String get_result(){
		String rst = "";
		
		rst = "<演:页面设置集>" + _result + "</演:页面设置集>";
		clear();
		
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:page-layout")){
			_result += "<演:页面设置";
			_result += " 演:标识符=\"" + atts.getValue("style:name") + "\"";
			_result += ">";
		}
		else if(qName.equals("style:page-layout-properties")){
			_result += get_page("presentation", atts);
			_result += get_margins("presentation", atts);
			
			if((attVal=atts.getValue("style:num-format"))!=null){
				_result += "<演:页码格式>";
				_result += Text_Config.conv_format(attVal);
				_result += "</演:页码格式>";
			}
			_result += get_orientation("presentation", atts);
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("style:page-layout")){
			_result += "</演:页面设置>";
		}
	}
}
