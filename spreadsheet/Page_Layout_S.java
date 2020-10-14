package spreadsheet;

import org.xml.sax.Attributes;
import convertor.Common_Pro;

/**
 * ����<style:page-layout> �� <��:ҳ������>��ת����
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
		
		rst = "<��:ҳ������  ��:����=\"ҳ������\">";
		rst += _result + Master_Page_S.get_result();
		rst += "</��:ҳ������>";
		
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
			
			_result += get_page("spreadsheet", atts);			//��:ֽ��
			
			_result += get_orientation("spreadsheet", atts);	//��:ֽ�ŷ���
			
			String scale = atts.getValue("style:scale-to");		//��:����
			if(scale == null){
				scale = "100";
			}
			else {
				scale = scale.substring(0,scale.length()-1);
			}
			_result += "<��:����>" + scale + "</��:����>";
			
			_result += get_margins("spreadsheet", atts);		//��:ҳ�߾�
		}
	}
}
