package presentation;

import org.xml.sax.Attributes;

import stored_data.Style_Data;
import styles.Para_Style;
import styles.Sent_Style;

/**
 * 处理style:family="presentation"的<style:style> 到 <演:文本式样>的转换。
 * 
 * @author xie
 *
 */
public class Presentation_Style {	
	//the result
	private static String _result = "";
	//tag for filtration
	private static boolean _filter_tag = false;
	//@style:name
	private static String _style_name = "";
	//attributes of 演:段落式样
	private static String _ele_atts = "";
	//attributes of style:text-properties
	private static String _text_pro = "";
	//attributes of style:paragraph-properties
	private static String _para_pro = "";
	//name of the master-page
	private static String _master_name = "";
	
	
	public static void set_master_name(String name){
		if(_master_name.equals("")){
			_master_name = name;
		}
	}
	
	public static String title_name(){
		return _master_name + "-title";
	}
	
	public static String subtitle_name(){
		return _master_name + "-subtitle";
	}
	
	public static String outline_name(){
		return _master_name + "-outline";
	}
	
	public static String notes_name(){
		return _master_name + "-notes";
	}
	
	public static String background_name(){
		return _master_name + "-background";
	}
	
	//return style name of text:p according to its presentation class
	public static String style_name(String presenCls, int listLev){
		String styleName = "";
		
		if(presenCls.equals("title")){
			styleName = title_name();
		}	
		else if(presenCls.equals("subtitle")){
			styleName = outline_name() + "1";
		}	
		else if(presenCls.equals("outline")){
			styleName = outline_name() + (listLev+1);
		}
		else if(presenCls.equals("notes")){
			styleName = notes_name();
		}
		
		return styleName;
	}
	
	private static void clear(){
		_style_name = "";
		_ele_atts = "";
		_text_pro = "";
		_para_pro = "";
	}
	
	private static String get_one_style(){
		String style = "";		
		String level = "";
		String outline = "";
		String listInfo = "";
		
		if(_style_name.equals(title_name())){
			level = "0";
		}else{
			level = _style_name.substring(outline_name().length());
		}

		outline = "<字:大纲级别>" + level + "</字:大纲级别>";
		
		int lev = Integer.parseInt(level);
		String listName = (lev % 2 == 0) ? "bn0" : "bn1"; 
		listInfo = "<字:自动编号信息" + " 字:编号引用=\"" + listName + "\"" + " 字:编号级别=\"0\"/>";
		if(_style_name.equals(title_name())){
			listInfo += "<uof:停止引用><uof:路径 uof:locID=\"u0067\">自动编号信息</uof:路径></uof:停止引用>";
		}
		
		//新版scheama是<演:段落式样>
		style = "<uof:段落式样" + _ele_atts + ">";
		style += outline + _para_pro + listInfo;
		style += "<字:句属性>" + _text_pro + "</字:句属性>";
		style += "</uof:段落式样>";
		
		if(level.equals("9")){
			style = "";
		}
		return style;
	}
	
	private static String get_notes_style(){
		String style = "";
		
		style = "<uof:段落式样" + _ele_atts + ">";
		style += _para_pro;
		style += "<字:句属性>" + _text_pro + "</字:句属性>";
		style += "</uof:段落式样>";
		
		return style;
	}
	
	public static String get_result(){
		String rst = "";
		
		rst = "<演:文本式样集>";
		rst += "<演:文本式样 演:标识符=\"ps001\">";
		rst += _result;
		rst += "</演:文本式样>";
		rst += "</演:文本式样集>";
		
		_result = "";
		return rst;
	}
	
	public static void process_start(String qName, Attributes atts){
		String attVal = "";
		
		if(_filter_tag){
			if(_style_name.equals(background_name()) && qName.equals("style:graphic-properties")){
				attVal = atts.getValue("draw:fill-color");
				attVal = (attVal==null) ? "" : attVal;
				Master_Pane.set_bg_color(attVal);
			}
			return;
		}
		
		else if(qName.equals("style:style")){
			attVal = atts.getValue("style:name");
			_style_name = (attVal==null) ? "" : attVal;
			
			if(!_style_name.contains(outline_name()) 
			    && !_style_name.equals(title_name())
			    && !_style_name.equals(notes_name())){
				_filter_tag = true;
			}
			
			if(!_style_name.equals("")){
				_ele_atts += " 字:标识符=\"" + _style_name + "\"";
			}
			if((attVal=atts.getValue("style:parent-style-name")) != null){
				_ele_atts += " 字:基式样引用=\"" + attVal + "\"";
			}
			
			if(_style_name.equals(notes_name())){
				_ele_atts += " 字:名称=\"notes\"";
			}else{
				_ele_atts += " 字:名称=\"slide\"";
			}
			_ele_atts += " 字:类型=\"default\"";
		}
		
		else if(qName.equals("style:paragraph-properties")){
			_para_pro = Para_Style.process_para_atts(atts);
		}
		
		else if(qName.equals("style:text-properties")){
			_text_pro = Sent_Style.process_text_atts(atts);
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("style:style")){
			if(!_filter_tag){
				if(!_style_name.equals(notes_name())){
					_result += get_one_style();
				}else {
					Style_Data.add_styles(get_notes_style());
				}
			}
		
			clear();
			_filter_tag = false;
		}
		
		else if(_filter_tag){
			return;
		}
	}
}
