package presentation;

import org.xml.sax.Attributes;

import text.Text_Config;
import convertor.Common_Pro;

/**
 * ����<style:page-layout> �� <��:ҳ������>��ת����
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
		
		rst = "<��:ҳ�����ü�>" + _result + "</��:ҳ�����ü�>";
		clear();
		
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:page-layout")){
			_result += "<��:ҳ������";
			_result += " ��:��ʶ��=\"" + atts.getValue("style:name") + "\"";
			_result += ">";
		}
		else if(qName.equals("style:page-layout-properties")){
			_result += get_page("presentation", atts);
			_result += get_margins("presentation", atts);
			
			if((attVal=atts.getValue("style:num-format"))!=null){
				_result += "<��:ҳ���ʽ>";
				_result += Text_Config.conv_format(attVal);
				_result += "</��:ҳ���ʽ>";
			}
			_result += get_orientation("presentation", atts);
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("style:page-layout")){
			_result += "</��:ҳ������>";
		}
	}
}
