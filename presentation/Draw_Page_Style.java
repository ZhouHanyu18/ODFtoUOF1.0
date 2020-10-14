package presentation;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;

import graphic_content.Media_Obj;

/**
 * ´¦Àístyle:family="presentation"µÄ<style:style> µ½ <ÑÝ:ÇÐ»»>\<ÑÝ:±³¾°>µÄ×ª»»¡£
 * 
 * @author xie
 *
 */
public class Draw_Page_Style {
	//the style name
	private static String _style_name = "";
	//transition effect
	private static String _effect = "";
	//presentation:transition-speed
	private static String _speed = "";
	//presentation:duration
	private static String _duration = "";
	//store <ÑÝ:ÇÐ»»> for corresponding page
	private static Map<String,String>
		_transition_map = new TreeMap<String,String>();
	//
	private static Map<String,String>
		_effect_table = new HashMap<String,String>();
	//
	private static Map<String,String>
		_background_map = new TreeMap<String,String>();
	//
	private static String _sound = "";
	
	
	//initialize
	public static void init(){
		_duration = "";
		_transition_map.clear();
		_effect_table.clear();
		_background_map.clear();
	}
	
	private static void clear(){
		_style_name = "";
		_effect = "";
		_speed = "";
		_sound = "";
	}
	
	//return <ÑÝ:ÇÐ»»> for specified draw page
	public static String get_transition(String name){
		String tst = "";
		
		tst = _transition_map.get(name);
		tst = (tst==null) ? "" : tst;
		
		return tst;
	}
	
	//return <ÑÝ:±³¾°> for specified draw page
	public static String get_background(String name){
		String bg = "";
		String bgColor = "";
		
		bgColor = _background_map.get(name);
		if(bgColor != null){
			bg = "<ÑÝ:±³¾°>";
			bg += bgColor;
			bg += "</ÑÝ:±³¾°>";
		}
		
		return bg;
	}
	
	private static void add_transition(){
		String rst = "";
		
		rst = "<ÑÝ:ÇÐ»»";
		if(!_effect.equals("")){
			rst += " ÑÝ:Ð§¹û=\"" + _effect + "\"";
		}
		if(!_speed.equals("")){
			rst += " ÑÝ:ËÙ¶È=\"" + _speed + "\"";
		}
		rst += ">";
		
		if(!_sound.equals("")) {
			rst += _sound;
		}
		
		rst += "<ÑÝ:·½Ê½>";
		rst += "<ÑÝ:µ¥»÷Êó±ê>true</ÑÝ:µ¥»÷Êó±ê>";
		if(!_duration.equals("")){
			rst += "<ÑÝ:Ê±¼ä¼ä¸ô>";
			rst += _duration;
			rst += "</ÑÝ:Ê±¼ä¼ä¸ô>";
		}
		rst += "</ÑÝ:·½Ê½>";
		
		rst += "</ÑÝ:ÇÐ»»>";
		
		_transition_map.put(_style_name,rst);
		clear();
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:style")){
			attVal = atts.getValue("style:name");
			_style_name = (attVal==null) ? "" : attVal;
		}
		
		else if(qName.equals("style:drawing-page-properties")){
			
			attVal = atts.getValue("presentation:transition-speed");
			_speed = (attVal==null) ? "" : attVal;
			
			attVal = atts.getValue("presentation:duration");
			_duration = conv_time(attVal);
			
			String smilType = atts.getValue("smil:type");
			String smilSubtype = atts.getValue("smil:subtype");
			String smilDirection = atts.getValue("smil:direction");
			_effect = get_effect(smilType,smilSubtype,smilDirection);	
			
			//background
			String name = "";
			String fill = atts.getValue("draw:fill");
			String fillColor = atts.getValue("draw:fill-color");
			
			fill = (fill==null) ? "" : fill;
			if(fill.equals("solid")){
				String color = "<Í¼:ÑÕÉ«>" + fillColor + "</Í¼:ÑÕÉ«>";
				_background_map.put(_style_name,color);
			}
			else if(fill.equals("gradient")){
				name = atts.getValue("draw:fill-gradient-name");
				_background_map.put(_style_name,Draw_Padding.get_gradient(name));
			}
			else if(fill.equals("bitmap")){
				name = atts.getValue("draw:fill-image-name");
				_background_map.put(_style_name,Draw_Padding.get_fill_image(name));
			}
		}
		
		else if(qName.equals("presentation:sound")){
			String href = "";
			String objID = "";
			
			href = atts.getValue("xlink:href");
			if(href != null){
				objID = Media_Obj.process_href(href);
			}
			_sound = "<ÑÝ:ÉùÒô ÑÝ:×Ô¶¨ÒåÉùÒô=\"" + objID + "\"/>";
		}
	}
	
	
	public static void process_end(String qName){
		if(qName.equals("style:style")){
			add_transition();
		}
	}
	
	//convert the duaration value from odf to uof
	//for example: PT00H00M02S => 2000
	public static String conv_time(String val){
		String time = "";
		long secs = 0;
		
		if(val == null || !val.startsWith("PT")){
			return "";
		}
		
		try{
			String hours = val.substring(2,4);
			String minutes = val.substring(5,7);
			String seconds = val.substring(8,10);
			
			secs += Integer.parseInt(hours) * 3600;
			secs += Integer.parseInt(minutes) * 60;
			secs += Integer.parseInt(seconds);
			
		}catch (Exception e){
			time = "";
			e.printStackTrace();
		}
		time = "" + (secs*1000);
		
		return time;
	}
	
	//return transition effect
	private static String get_effect(String type,String subtype,String direction){
		String effect = "";
		String str = "";
		
		if(type == null){
			return "";
		}
		
		subtype = (subtype==null) ? "" : subtype;
		if(direction != null && direction.equals("reverse")){
			str = type + "|" + subtype + "|reverse";
		}
		else {
			str = type + "|" + subtype;
		}
		
		effect = _effect_table.get(str);
		effect = (effect==null) ? "" : effect;
		
		return effect;
	}
	
	public static void init_effect_table(){
		
		_effect_table.put("barWipe|topToBottom|reverse","wipe up");
		
		_effect_table.put("barWipe|topToBottom","wipe down");
		
		_effect_table.put("barWipe|leftToRight","wipe right");
		
		_effect_table.put("barWipe|leftToRight|reverse","wipe left");
		
		_effect_table.put("pinWheelWipe|oneBlade","wheel clockwise ¨C 1 spoke");
		
		_effect_table.put("pinWheelWipe|twoBladeVertical","wheel clockwise ¨C 2 spoke");
		
		_effect_table.put("pinWheelWipe|threeBlade","wheel clockwise ¨C 3 spoke");
		
		_effect_table.put("pinWheelWipe|fourBlade","wheel clockwise ¨C 4 spoke");
		
		_effect_table.put("pinWheelWipe|eightBlade","wheel clockwise ¨C 8 spoke");

		_effect_table.put("slideWipe|fromTop|reverse","uncover down");
		
		_effect_table.put("slideWipe|fromRight|reverse","uncover left");
		
		_effect_table.put("slideWipe|fromLeft|reverse","uncover right");
		
		_effect_table.put("slideWipe|fromBottom|reverse","uncover up");
		
		_effect_table.put("slideWipe|fromTopRight|reverse","uncover left-down");
		
		_effect_table.put("slideWipe|fromBottomRight|reverse","uncover left-up");
		
		_effect_table.put("slideWipe|fromTopLeft|reverse","uncover right-down");
		
		_effect_table.put("slideWipe|fromBottomLeft|reverse","uncover right-up");
		
		_effect_table.put("randomBarWipe|vertical","random bars vertical");
		
		_effect_table.put("randomBarWipe|horizontal","random bars horizontal");
		
		_effect_table.put("checkerBoardWipe|down","checkerboard down");
		
		_effect_table.put("checkerBoardWipe|across","checkerboard across");
		
		_effect_table.put("fourBoxWipe|cornersOut","shape plus");
		
		_effect_table.put("irisWipe|diamond","shape diamond");
		
		_effect_table.put("ellipseWipe|circle","shape circle");
		
		_effect_table.put("irisWipe|rectangle","box out");
		
		_effect_table.put("irisWipe|rectangle|reverse","box in");
		
		_effect_table.put("fanWipe|centerTop","wedge");
		
		_effect_table.put("blindsWipe|vertical","blinds vertical");
		
		_effect_table.put("blindsWipe|horizontal","blinds horizontal");
		
		_effect_table.put("fade|fadeOverColor","fade through black");
		
		_effect_table.put("slideWipe|fromTop","cover down");
		
		_effect_table.put("slideWipe|fromRight","cover left");
		
		_effect_table.put("slideWipe|fromLeft","cover right");
		
		_effect_table.put("slideWipe|fromBottom","cover up");
		
		_effect_table.put("slideWipe|fromTopRight","cover left-down");
		
		_effect_table.put("slideWipe|fromBottomRight","cover left-up");
		
		_effect_table.put("slideWipe|fromTopLeft","cover right-down");
		
		_effect_table.put("slideWipe|fromBottomLeft","cover right-up");
		
		_effect_table.put("dissolve|","dissolve");
		
		_effect_table.put("random|","random transition");
		
		_effect_table.put("pushWipe|combHorizontal","comb horizontal");
		
		_effect_table.put("pushWipe|combVertical","comb vertical");
		
		_effect_table.put("fade|crossfade","fade smoothly");
		
		_effect_table.put("pushWipe|fromTop","push down");
		
		_effect_table.put("pushWipe|fromRight","push left");
		
		_effect_table.put("pushWipe|fromLeft","push right");
		
		_effect_table.put("pushWipe|fromBottom","push up");
		
		_effect_table.put("barnDoorWipe|horizontal|reverse","split horizontal in");
		
		_effect_table.put("barnDoorWipe|horizontal","split horizontal out");
		
		_effect_table.put("barnDoorWipe|vertical|reverse","split vertical in");
		
		_effect_table.put("barnDoorWipe|vertical","split vertical out");
		
		_effect_table.put("waterfallWipe|horizontalRight","strips left-down");
		
		_effect_table.put("waterfallWipe|horizontalLeft|reverse","strips left-up");
		
		_effect_table.put("waterfallWipe|horizontalLeft","strips right-down");
		
		_effect_table.put("waterfallWipe|horizontalRight|reverse","strips right-up");
	}
}
