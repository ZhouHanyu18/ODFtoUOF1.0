package presentation;

import org.xml.sax.Attributes;

import stored_data.Content_Data;
import tables.Draw_Type_Table;

/**
 * ����<style:master-page> �� <��:ĸ��>��ת����
 * 
 * @author xie
 *
 */
public class Master_Pane {
	//the result
	private static String _result = "";
	//master-page for slide
	private static String _slide_pane = "";
	//master-page for notes
	private static String _notes_pane = "";
	//tag for <presentation:notes>
	private static boolean _notes_tag = false;
	//
	private static String _bg_color = "";
	//
	private static String _anchor_id = "";
	
	
	public static void set_bg_color(String color){
		_bg_color = color;
	}
	
	private static void clear(){
		_slide_pane = "";
		_notes_pane = "";
		_bg_color = "";
	}
	
	public static String get_result(){
		String str = "";
		
		str += "<��:ĸ�漯>" + _result + "</��:ĸ�漯>";

		_result = "";
		_anchor_id = "";
		return str;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:master-page")){
			_slide_pane += "<��:ĸ�� ��:����=\"slide\"";
			
			attVal = atts.getValue("style:name");
			_slide_pane += " ��:����=\"" + attVal + "\"";
			_slide_pane += " ��:��ʶ��=\"" + attVal + "\"";

			if((attVal=atts.getValue("style:page-layout-name")) != null){
				_slide_pane += " ��:ҳ����������=\"" + attVal + "\"";
			}

			_slide_pane += " ��:�ı�ʽ������=\"ps001\"";
			_slide_pane += ">";
		}
		
		else if (Draw_Type_Table._in_list(qName) && !_anchor_id.equals("")) {
			if(!_notes_tag){
				_slide_pane += Content_Data.get_presentation_anchor(_anchor_id);
			}
			else {
				_notes_pane += Content_Data.get_presentation_anchor(_anchor_id);
			}
		}
		
		else if(qName.equals("presentation:notes")){
			_notes_tag = true;
			
			_notes_pane = "<��:ĸ�� ��:����=\"notes\" ��:��ʶ��=\"notes001\"";
			
			if((attVal=atts.getValue("style:page-layout-name")) != null){
				_notes_pane += " ��:ҳ����������=\"" + attVal + "\"";
			}
			_notes_pane += ">";
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("style:master-page")){
			_slide_pane += get_background();
			_slide_pane += "</��:ĸ��>";
			
			_result += _slide_pane;
			_result += _notes_pane;
			clear();
		}
		
		else if(qName.equals("presentation:notes")){
			_notes_pane += "</��:ĸ��>";
		}
	}
	
	private static String get_background(){
		String bg = "";
		
		if(!_bg_color.equals("")){
			bg = "<��:����>";
			bg += "<ͼ:��ɫ>" + _bg_color + "</ͼ:��ɫ>";
			bg += "</��:����>";
		}
		
		_bg_color = "";
		return bg;
	}
	
	public static void set_achor_id(String anchorID) {
		_anchor_id = anchorID;
	}
}
