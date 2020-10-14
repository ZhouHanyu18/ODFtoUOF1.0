package presentation;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

/**
 * ´¦Àí <anim:par> µ½ <ÑÝ:¶¯»­>µÄ×ª»»¡£
 * 
 * @author xie
 *
 */
public class Anim_Par {
	//the result
	private static String _result = "";
	//
	private static Anim_Par_Struct _one_par = null;
	//nested level of elements
	private static int _ele_level = 0;
	//id of current <anim:par>
	private static String _anim_id = "";
	//target element of this animation
	private static String _target_id = "";
	//
	private static Map<String,String> _effect_table = new HashMap<String,String>();
	
	
	//initialize
	public static void init(){
		_result = "";
		_one_par = null;
		_ele_level = 0;
		_effect_table.clear();
	}
	
	private static void clear(){
		_anim_id = "";
		_target_id = "";
	}
	
	public static String get_result(){
		String rst = "";
		
		rst = "<ÑÝ:¶¯»­>" + _result + "</ÑÝ:¶¯»­>";
		_result = "";
		
		return rst;
	}
	
	private static String skip_null(String str){
		return (str==null) ? "" : str;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		_ele_level ++;
		if(qName.equals("anim:par") && _ele_level == 3){
			_one_par = new Anim_Par_Struct();
			_one_par.init();
		}
		else if(qName.equals("anim:par") && _ele_level == 5){
			_anim_id = skip_null(atts.getValue("anim:id"));
			
			attVal = atts.getValue("presentation:preset-class");
			_one_par.set_present_class(skip_null(attVal));
	
			attVal = skip_null(atts.getValue("presentation:node-type"));
			_one_par.add_timing_att("ÑÝ:ÊÂ¼þ",get_event_type(attVal));
			
			attVal = skip_null(atts.getValue("smil:begin"));
			_one_par.add_timing_att("ÑÝ:ÑÓÊ±",get_duration(attVal));
			
			attVal = skip_null(atts.getValue("smil:repeatCount"));
			_one_par.add_timing_att("ÑÝ:ÖØ¸´",get_repeat_count(attVal));
			
			attVal = skip_null(atts.getValue("presentation:preset-id"));
			_one_par.set_present_id(attVal);
			
			attVal = skip_null(atts.getValue("presentation:preset-sub-type"));
			_one_par.set_sub_type(attVal);
		}
		/*
		else if(qName.equals("anim:iterate")){
			_anim_id = skip_null(atts.getValue("anim:id"));
			
			attVal = atts.getValue("presentation:preset-class");
			_present_class = skip_null(attVal);
			
			attVal = skip_null(atts.getValue("presentation:node-type"));
			if(!get_event_type(attVal).equals("")){
				_timing_atts += " ÑÝ:ÊÂ¼þ=\"" + get_event_type(attVal) + "\"";
			}
			
			attVal = skip_null(atts.getValue("smil:begin"));
			if(!attVal.equals("")){ //?? zhuan huan
				//_timing_atts += " ÑÝ:ÑÓÊ±=\"" + attVal + "\"";
			}
			
			attVal = skip_null(atts.getValue("smil:repeatCount"));
			if(!get_repeat_count(attVal).equals("")){
				_timing_atts += " ÑÝ:ÖØ¸´=\"" + get_repeat_count(attVal) + "\"";
			}
			
			attVal = skip_null(atts.getValue("anim:iterate-type"));		
			if(attVal.equals("by-letter") || attVal.equals("by-word")){
				_text_atts += " ÑÝ:·¢ËÍ=\"" + attVal + "\"";
			}
			else{
				_text_atts += " ÑÝ:·¢ËÍ=\"all at once\"";
			}
			
			attVal = skip_null(atts.getValue("smil:targetElement"));
			if(!_target_id.equals("")){
				_target_id = attVal;
			}
		}
		*/
		else if(qName.equals("anim:set")){
			attVal = skip_null(atts.getValue("smil:targetElement"));
			attVal = skip_null(Draw_Page.get_draw_id(attVal));
			
			if(_target_id.equals("")){
				_target_id = attVal;
				_one_par.set_target(attVal);
			}
			//_target_id has been set
			else if(attVal.equals(_target_id)){
				String begin = skip_null(atts.getValue("smil:begin"));
				String att = skip_null(atts.getValue("smil:attributeName"));
				String to = skip_null(atts.getValue("smil:to"));
				if(begin.equals(_anim_id+".end") &&
					att.equals("visibility") && to.equals("hidden")){
					_one_par.add_after_att("ÑÝ:²¥·ÅºóÒþ²Ø","true");
				}
			}
		}
		
		else if(qName.equals("anim:transitionFilter")){
			attVal = skip_null(atts.getValue("smil:dur"));
			String sp = get_speed(attVal);
			
			_one_par.add_timing_att("ÑÝ:ËÙ¶È",sp);
			_one_par.set_speed(sp);
		}
		
		else if(qName.equals("anim:animateColor")){
			attVal = skip_null(atts.getValue("smil:attributeName"));
			if(attVal.equals("dim")){
				String color = skip_null(atts.getValue("smil:to"));
				_one_par.add_after_att("ÑÝ:ÑÕÉ«",color);
			}
		}
		
		else if(qName.equals("anim:animateMotion")){
			attVal = skip_null(atts.getValue("smil:targetElement"));
			attVal = skip_null(Draw_Page.get_draw_id(attVal));
			_one_par.set_target(attVal);
			
			_one_par.set_motion_path(atts.getValue("svg:path"));
			
			attVal = skip_null(atts.getValue("smil:dur"));
			_one_par.add_timing_att("ÑÝ:ÑÓÊ±",get_duration(attVal));
		}
	}
	
	public static void process_end(String qName){
		_ele_level --;
		
		if(qName.equals("anim:par") && _ele_level == 2){
			_result += _one_par.get_result();
			clear();
		}
	}
	
	private static String get_duration(String time){
		String s = time;
		if(time.contains("s")){
			s = time.substring(0,time.indexOf("s"));
		}
		
		return s ;
	}
	
	private static String get_repeat_count(String val){
		String repeat = "";
		
		if(val.equals("indefinite")){
			repeat = "until next click";
		}else {
			repeat = val;
		}
		
		return repeat;
	}
	
	private static String get_event_type(String val){
		String type = "";
		
		if(val.equals("on click")
			||val.equals("with previous")
			||val.equals("after previous")){
			type = "";
		}
		
		return type;
	}
	
	private static String get_speed(String val){
		String speed = "fast";
		
		try{
			if(val.endsWith("s")){
				float dur = 0;
				int len = val.length();
				
				val = val.substring(0,len - 1);
				dur = Float.parseFloat(val);
				if(dur < 0.5){
					speed = "very fast";
				}else if(dur < 1){
					speed = "fast";
				}else if(dur < 2){
					speed = "medium";
				}else if(dur < 3){
					speed = "slow";
				}else {
					speed = "very slow";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return speed;
	}
	
	public static String get_effect(String presentID,String subType){
		String effect = "";
		String rst = "";
		
		if(presentID.contains("emphasis")){
			effect = _effect_table.get(presentID);
		}

		else if(presentID.equals("ooo-entrance-appear")){
			effect = "ÑÝ:³öÏÖ";
		}
		else if(presentID.equals("ooo-exit-disappear")){
			effect = "ÑÝ:ÏûÊ§";
		}
		else{
			int ind1 = presentID.indexOf("-");
			ind1 = presentID.indexOf("-",ind1+1);
			
			presentID = presentID.substring(ind1+1);
			if(presentID.equals("wheel")){
				effect = "ÑÝ:ÂÖ×Ó" + " ÑÝ:·øÉä×´=\"" + subType + "\"";
			}
			else {
				rst = _effect_table.get(presentID + "|" + subType);
				if(rst != null && rst.contains("|")){
					int ind = rst.indexOf("|");
					String val1 = rst.substring(0,ind);
					String val2 = rst.substring(ind + 1);
					effect = "" + val1 + " ÑÝ:·½Ïò=\"" + val2 + "\"";
				}
			}
		}
		return effect;
	}
	
	public static void init_effect_table(){
		//entrance/exit
		_effect_table.put("wipe|from-left","ÑÝ:²Á³ý|from left");
		
		_effect_table.put("wipe|from-up","ÑÝ:²Á³ý|from up");
		
		_effect_table.put("wipe|from-bottom","ÑÝ:²Á³ý|from bottom");
		
		_effect_table.put("wipe|from-right","ÑÝ:²Á³ý|from right");
		
		_effect_table.put("split|horizontal-in","ÑÝ:ÅüÁÑ|horizontal in");//5
		
		//_effect_table.put("appear|","ÑÝ:³öÏÖ");
		
		_effect_table.put("diagonal-squares|left-to-bottom","ÑÝ:½×ÌÝ×´|left down");
		
		_effect_table.put("fly-in|from-bottom","ÑÝ:·ÉÈë|from bottom");
		
		_effect_table.put("wheel|4","ÑÝ:ÂÖ×Ó|4");//ÑÝ:·øÉä×´

		_effect_table.put("fly-in-slow|from-right","ÑÝ:»ºÂý½øÈë|from right");
		
		_effect_table.put("venetian-blinds|horizontal","ÑÝ:°ÙÒ¶´°|horizontal");
		
		_effect_table.put("box|in","ÑÝ:ºÐ×´|in");
		
		_effect_table.put("diamond|out","ÑÝ:ÁâÐÎ|out");
		
		//exit
		_effect_table.put("fly-out|from-bottom","ÑÝ:·É³ö|from bottom");
		
		_effect_table.put("disappear","ÑÝ:ÏûÊ§");
		
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		
		//emphasis
		_effect_table.put("fill-color","ÑÝ:¸ü¸ÄÌî³äÑÕÉ«");
		
		_effect_table.put("line-color","ÑÝ:¸ü¸ÄÏßÌõÑÕÉ«");
		
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		_effect_table.put("","");
		
	}
}
