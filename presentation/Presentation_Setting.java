package presentation;

import java.util.ArrayList;

import org.xml.sax.Attributes;

/**
 * 处理<presentation:setting> 到 <演:放映设置>的转换。
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
		
		rst = "<演:放映设置>" + _settings + "</演:放映设置>";
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
		setting += "<演:幻灯片序列 演:标识符=\"" + seqID + "\" 演:自定义=\"false\">";
		setting += startPage + " " + endPage;
		setting += "</演:幻灯片序列>";
		
		setting += "<演:放映顺序 演:序列引用=\"" + seqID + "\"/>";
		
		if((attVal=atts.getValue("presentation:full-screen"))!= null){
			setting += "<演:全屏放映>" + attVal + "</演:全屏放映>";
		}
		
		if((attVal=atts.getValue("presentation:endless"))!= null){
			setting += "<演:循环放映>" + attVal + "</演:循环放映>";
		}
		
		if((attVal=atts.getValue("presentation:pause"))!= null){
			setting += "<演:放映间隔>" + Draw_Page_Style.conv_time(attVal) + "</演:放映间隔>";
		}
		
		if((attVal=atts.getValue("presentation:force-manual"))!= null){
			setting += "<演:手动方式>" + attVal + "</演:手动方式>";
		}
		
		if((attVal=atts.getValue("presentation:start-with-navigator"))!= null){
			setting += "<演:导航帮助>" + attVal + "</演:导航帮助>";
		}
		
		if((attVal=atts.getValue("presentation:animations"))!= null){
			setting += "<演:放映动画>" + tran_val(attVal) + "</演:放映动画>";
		}
		
		if((attVal=atts.getValue("presentation:stay-on-top"))!= null){
			setting += "<演:前端显示>" + attVal + "</演:前端显示>";
		}
		
		if((attVal=atts.getValue("presentation:transition-on-click"))!= null){
			setting += "<演:单击跳转>" + tran_val(attVal) + "</演:单击跳转>";
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
