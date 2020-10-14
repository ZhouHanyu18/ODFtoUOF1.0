package spreadsheet;

import org.xml.sax.Attributes;
import convertor.Common_Pro;

/**
 * 处理<style:page-layout> 到 <表:页面设置>的转换。
 * 
 * @author xie
 *
 */
public class Page_Layout_S extends Common_Pro{
	//the result
	private static String _result = "";
	//tag for filtration
	private static boolean _filter = false;
	//the page-layout used by default master-page
	private static String _default_plname = "";
	
	
	public static String get_result(){
		String rst = "";
		
		rst = "<表:页面设置  表:名称=\"页面设置\">";
		rst += _result + Master_Page_S.get_result();
		rst += "</表:页面设置>";
		
		_result = "";
		_default_plname = "";
		
		return rst;
	}
	
	public static void set_default_plname(String deName){
		_default_plname = deName;
	}
	
	public static void process_start(String qName,Attributes atts){		
		
		if(qName.equals("style:page-layout")){
			
			String name = atts.getValue("style:name");
			
			_filter = (name == null || !name.equals(_default_plname));
		}
		
		else if(!_filter && qName.equals("style:page-layout-properties")){
			
			_result += get_page("spreadsheet", atts);			//表:纸张
			
			_result += get_orientation("spreadsheet", atts);	//表:纸张方向
			
			String scale = atts.getValue("style:scale-to");		//表:缩放
			if(scale == null){
				scale = "100";
			}
			else {
				scale = scale.substring(0,scale.length()-1);
			}
			_result += "<表:缩放>" + scale + "</表:缩放>";
			
			_result += get_margins("spreadsheet", atts);		//表:页边距
		}
	}
}
