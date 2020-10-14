package presentation;

import java.util.ArrayList;

import org.xml.sax.Attributes;

/**
 * ����<presentation:setting> �� <��:��ӳ����>��ת����
 * 
 * @author xie
 *
 */
public class Presentation_Setting {
	//the result
	private static String _settings = ""; 
	//a sequence of page names
	private static ArrayList<String> _page_seq = new ArrayList<String>();
	

	public static String get_result(){
		String rst = "";
		
		rst = "<��:��ӳ����>" + _settings + "</��:��ӳ����>";
		_settings = "";
		_page_seq.clear();
		
		return rst;
	}

	public static void add_page_name(String page){
		_page_seq.add(page);
	}
	
	public static void process(String qName,Attributes atts){
		String setting = "";
		String attVal = "";

		String startPage = "";
		String endPage = _page_seq.get(_page_seq.size()-1);
		String seqID = "SEQID";
		
		attVal=atts.getValue("presentation:start-page");		
		if(attVal != null){
			startPage = attVal;
		}else {
			startPage = _page_seq.get(0);
		}
		setting += "<��:�õ�Ƭ���� ��:��ʶ��=\"" + seqID + "\" ��:�Զ���=\"false\">";
		setting += startPage + " " + endPage;
		setting += "</��:�õ�Ƭ����>";
		
		setting += "<��:��ӳ˳�� ��:��������=\"" + seqID + "\"/>";
		
		if((attVal=atts.getValue("presentation:full-screen"))!= null){
			setting += "<��:ȫ����ӳ>" + attVal + "</��:ȫ����ӳ>";
		}
		
		if((attVal=atts.getValue("presentation:endless"))!= null){
			setting += "<��:ѭ����ӳ>" + attVal + "</��:ѭ����ӳ>";
		}
		
		if((attVal=atts.getValue("presentation:pause"))!= null){
			setting += "<��:��ӳ���>" + Draw_Page_Style.conv_time(attVal) + "</��:��ӳ���>";
		}
		
		if((attVal=atts.getValue("presentation:force-manual"))!= null){
			setting += "<��:�ֶ���ʽ>" + attVal + "</��:�ֶ���ʽ>";
		}
		
		if((attVal=atts.getValue("presentation:start-with-navigator"))!= null){
			setting += "<��:��������>" + attVal + "</��:��������>";
		}
		
		if((attVal=atts.getValue("presentation:animations"))!= null){
			setting += "<��:��ӳ����>" + tran_val(attVal) + "</��:��ӳ����>";
		}
		
		if((attVal=atts.getValue("presentation:stay-on-top"))!= null){
			setting += "<��:ǰ����ʾ>" + attVal + "</��:ǰ����ʾ>";
		}
		
		if((attVal=atts.getValue("presentation:transition-on-click"))!= null){
			setting += "<��:������ת>" + tran_val(attVal) + "</��:������ת>";
		}
		
		setting += "";
		
		_settings = setting;
	}
		
	private static String tran_val(String val){
		String str = "";
		
		if(val.equals("enabled")){
			str = "true";
		}
		else{
			str = "false";
		}
		
		return str;
	}
}
