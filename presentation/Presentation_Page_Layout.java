package presentation;

import java.util.ArrayList;
import org.xml.sax.Attributes;

/**
 * ����<style:presentation-page-layout> �� <��:ҳ���ʽ>��ת����
 * 
 * @author xie
 *
 */
public class Presentation_Page_Layout {
	//<��:ҳ���ʽ��>
	private static String _pst_PLs = "";
	//style name of this page-layout
	private static String _id = "";
	//the type of the place-holders
	//used by current page-layout.
	private static ArrayList<String> _type_list = new ArrayList<String>();
	
	private static void clear(){
		_id = "";
		_type_list.clear();
	}
	
	private static String get_one_PL(){
		String pl = "";		
		String phPls = "";
		String types = "";
		
		for(int i=0; i<_type_list.size(); i++){
			types += _type_list.get(i) + "|";
		}
		phPls = get_place_holders(types);
		if(phPls.equals("")){		
			if(_type_list.size() == 2){
				phPls += get_PL("text-body");
			}
			else if(_type_list.size() == 3){
				phPls += get_PL("2-columns");
			}
			
			for(int i=0; i<_type_list.size(); i++){
				String convtype = conv_PH_type(_type_list.get(i));
				phPls += get_PH(convtype);
			}
		}
		
		pl = "<��:ҳ���ʽ ��:��ʶ��=\"" + _id + "\">";
		pl += phPls;
		pl += "</��:ҳ���ʽ>";
		
		clear();
		return pl;
	}
	
	public static String get_result(){
		String rst = "";
		
		rst = "<��:ҳ���ʽ��>";
		rst += _pst_PLs;
		rst += "</��:ҳ���ʽ��>";
		
		_pst_PLs = "";
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		if (qName.equals("style:presentation-page-layout")) {
			_id = atts.getValue("style:name");
		}
		else if (qName.equals("presentation:placeholder")) {
			attVal = atts.getValue("presentation:object");
			if (attVal != null){
				_type_list.add(attVal);
			}
		}
	}
	
	public static void process_end(String qName){
		
		if (qName.equals("style:presentation-page-layout")) {
			_pst_PLs += get_one_PL();
		}
		else if (qName.equals("presentation:placeholder")) {
			//To do.ռλ���洢ê��
		}
	}
	
	//<��:����>and<��:ռλ��>
	private static String get_place_holders(String type) {
		String rst = "";
		
		if (type.equals("title|subtitle|")) {
			rst = get_PL("title-subtitle") 
				+ get_PH("centertitle") + get_PH("subtitle");
		}
		
		else if (type.equals("title|")) {
			rst = get_PL("title-only") + get_PH("title");
		}
		
		else if(type.equals("vertical_title|vertical_outline|")){
			rst = get_PL("v-title-body") 
				+ get_PH("vertical_title") + get_PH("vertical_text");
		}
			
		else if(type.equals("vertical_title|vertical_outline|chart|")){
			rst = get_PL("v-2-rows") + get_PH("vertical_title") 
				+ get_PH("vertical_text") + get_PH("chart");
		}
		
		return rst;
	}
	
	//There are 16 types all together in ODF:
	//title,outline,subtitle,text,graphic,
	//object,chart,table,orgchart,page,
	//notes,header,footer,date-time,handout
	//page-number
	public static String conv_PH_type(String val){
		String phType = "";
		
		if(val.equals("outline")){
			phType = "text"; 	//"outline"
		}
		else if(val.equals("vertical-outline")){
			phType = "vertical-text";
		}
		else if(val.equals("graphic")){
			phType = "clipart"; //or "graphics"
		}
		else if(val.equals("orgchart")){
			phType = "chart";	//??
		}
		else if(val.equals("page")){
			phType = "object";	//??
		}
		else if(val.equals("date-time")){
			phType = "date";
		}
		else if(val.equals("page-number")){
			phType = "number";
		}
		else {
			phType = val;
		}
		
		return phType;
	}
	
	//<��:����>
	private static String get_PL(String type){
		return "<��:���� ��:����=\"" + type + "\"/>";
	}
	
	//<��:ռλ��>
	private static String get_PH(String type){
		return "<��:ռλ�� ��:����=\"" + type + "\"/>";
	}
}
