package presentation;

import java.util.*;
import org.xml.sax.Attributes;
import stored_data.Content_Data;
import tables.Draw_Type_Table;

/**
 * ´¦Àí<draw:page> µ½ <ÑÝ:»ÃµÆÆ¬>µÄ×ª»»¡£
 * 
 * @author xie
 *
 */
public class Draw_Page {
	//the result
	private static String _result = ""; 
	//name of the draw style
	private static String _style_name = "";
	//attributes of <ÑÝ:»ÃµÆÆ¬>
	private static String _ele_atts = "";
	//tag for <presentation:animations>
	private static boolean _present_anim_tag = false;	
	//tag for <anim:par>
	private static boolean _anim_par_tag = false;
	//stack for nesting
	private static Stack<String> _stack = new Stack<String>();
	//store draw-id and its corresponding anchor-id
	private static Map<String,String> _draw_id_map = new TreeMap<String,String>();
	//
	private static String _anchor_id = "";
	//
	private static int _group_level = 0;
	
	
	//initialize
	public static void init(){
		_stack.clear();
		_draw_id_map.clear();
		_anchor_id = "";
		_group_level = 0;
	}
	
	private static void clear(){
		_result = "";
		_style_name = "";
		_ele_atts = "";
	}
	
	public static String get_result(){
		String rst = "";
		
		rst = "<ÑÝ:»ÃµÆÆ¬" + _ele_atts + ">";
		rst += _result;
		rst += Draw_Page_Style.get_background(_style_name);
		rst += Draw_Page_Style.get_transition(_style_name);
		rst += "</ÑÝ:»ÃµÆÆ¬>";
		
		clear();
		return rst;
	}
	
	public static void add_draw_id(String anchID,String drawID){
		_draw_id_map.put(anchID,drawID);
	}
	
	public static String get_draw_id(String id){
		return _draw_id_map.get(id);
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("draw:page")){		
			attVal = atts.getValue("draw:name");
			attVal = (attVal==null) ? "" : attVal;
			
			if(!attVal.equals("")){
				_ele_atts += " ÑÝ:Ãû³Æ=\"" + attVal + "\"";
				_ele_atts += " ÑÝ:±êÊ¶·û=\"" + attVal + "\"";
			}
			
			attVal = atts.getValue("draw:style-name");
			_style_name = (attVal==null) ? "" : attVal;
			
			if((attVal=atts.getValue("presentation:presentation-page-layout-name")) != null){
				_ele_atts += " ÑÝ:Ò³Ãæ°æÊ½ÒýÓÃ=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("draw:master-page-name")) != null){
				_ele_atts += " ÑÝ:Ä¸°æÒýÓÃ=\"" + attVal + "\"";
			}
		}

		else if (Draw_Type_Table._in_list(qName) && _group_level == 0) {			
			if (!_anchor_id.equals("")) {
				_result += Content_Data.get_presentation_anchor(_anchor_id);
			}
			
			attVal = atts.getValue("draw:id");
			String drawID = (attVal==null) ? "" : attVal;	
			if(!_anchor_id.equals("") && !drawID.equals("")){
				_draw_id_map.put(drawID,_draw_id_map.get(_anchor_id));	//
			}
		}
		
		else if(qName.equals("presentation:notes")){
			_result += "<ÑÝ:»ÃµÆÆ¬±¸×¢>";
		}
		
		else if(_anim_par_tag){
			_stack.push(qName);
			Anim_Par.process_start(qName,atts);
		}
		else if(qName.equals("anim:par")){
			_anim_par_tag = true;
			_stack.push(qName);
			Anim_Par.process_start(qName,atts);
		}
		
		else if(_present_anim_tag){
			Animation.process(qName,atts);
		}
		else if(qName.equals("presentation:animations")){
			_present_anim_tag = true;
		}
		
		if (qName.equals("draw:g")) {
			_group_level++;
		}
	}
	
	public static void process_end(String qName){		
		if (qName.equals("draw:g")) {
			_group_level--;
		}

		else if(qName.equals("draw:page")){
			//
		}

		else if(qName.equals("presentation:notes")){
			_result += "</ÑÝ:»ÃµÆÆ¬±¸×¢>";
		}
		
		else if(_anim_par_tag){
			Anim_Par.process_end(qName);
			_stack.pop();
			if(_stack.empty()){
				_anim_par_tag = false;
				_result += Anim_Par.get_result();
			}
		}
		
		else if(qName.equals("presentation:animations")){
			_result += Animation.get_result();
			_present_anim_tag = false;
		}
	}
	
	public static void set_achor_id(String anchorID) {
		_anchor_id = anchorID;
	}
}
