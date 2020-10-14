package presentation;

import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;

import convertor.Unit_Converter;
import graphic_content.Media_Obj;

/**
 * 处理<draw:fill-image>到<图:图片>、<draw:gradient>到<图:渐变>的转换。
 * 
 * @author xie
 *
 */
public class Draw_Padding {
	//
	private static Map<String,String> _gradient_map = new TreeMap<String,String>();
	//
	private static Map<String,String> _fill_image_map = new TreeMap<String,String>();
	
	
	//initialize
	public static void init(){
		_gradient_map.clear();
		_fill_image_map.clear();
	}
	
	public static String get_gradient(String name){
		return _gradient_map.get(name);
	}
	
	public static String get_fill_image(String name){
		return _fill_image_map.get(name);
	}
	
	public static void process(String qName, Attributes atts){
		String attVal = "";
		
		if(qName.equals("draw:fill-image")){
			String href = "";		
			String image = "";
			
			href = atts.getValue("xlink:href");
			if(href != null){
				String objID = Media_Obj.process_href(href);
				image += " 图:图形引用=\"" + objID + "\"";
			}
			image += " 图:位置=\"tile\"";
			
			String name = atts.getValue("draw:name");
			image = "<图:图片" + image + "/>";
			_fill_image_map.put(name,image);
		}
		
		else if (qName.equals("draw:gradient")){
			String gradient = "";
			String name = "";
			
			gradient = "<图:渐变";
			if ((attVal = atts.getValue("draw:start-color")) != null){
				gradient += " 图:起始色=\"" + attVal + "\"";
			}
			if ((attVal = atts.getValue("draw:end-color")) != null){
				gradient += " 图:终止色=\"" + attVal + "\"";
			}
			if ((attVal = atts.getValue("draw:style")) != null){
				gradient += " 图:种子类型=\"" + conv_style(attVal) + "\"";
			}
			if ((attVal = atts.getValue("draw:start-intensity")) != null){				
				gradient += " 图:起始浓度=\"" + Unit_Converter.from_percent(attVal) + "\"";
			}
			if ((attVal = atts.getValue("draw:end-intensity")) != null){
				gradient += " 图:终止浓度=\"" + Unit_Converter.from_percent(attVal) + "\"";
			}
			if ((attVal = atts.getValue("draw:angle")) != null){		
				gradient += " 图:渐变方向=\"" + (Integer.parseInt(attVal) / 10) + "\"";
			}
			if ((attVal = atts.getValue("draw:border")) != null){
				gradient += " 图:边界=\"" + attVal.substring(0,attVal.length()-1) + "\"";
			}
			if ((attVal = atts.getValue("draw:cx")) != null){
				gradient += " 图:种子X位置=\"" + attVal.substring(0,attVal.length()-1) + "\"";
			}
			if ((attVal = atts.getValue("draw:cy")) != null){
				gradient += " 图:种子Y位置=\"" + attVal.substring(0,attVal.length()-1) + "\"";
			}				
			gradient += "/>";
			
			name = atts.getValue("draw:name");
			_gradient_map.put(name,gradient);
		}
	}
	
	private static String conv_style(String val){
		String convVal = "linear";
		
		if(val.equals("linear")){
			convVal = "linear";
		}
		else if(val.equals("axial")){
			convVal = "radar";
		}
		else if(val.equals("radial")){
			convVal = "radar";
		}
		else if(val.equals("ellipsoid")){
			convVal = "oval";
		}
		else if(val.equals("square")){
			convVal = "square";
		}
		else if(val.equals("rectangular")){
			convVal = "rectangle";
		}
		
		return convVal;
	}
	
}
