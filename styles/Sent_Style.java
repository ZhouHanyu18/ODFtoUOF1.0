package styles;

import graphic_content.Media_Obj;

import org.xml.sax.Attributes;

import stored_data.*;
import convertor.Unit_Converter;

/**
 * 处理style:family="text"的<style:style> 到 <uof:句式样>的转换。
 * 
 * @author xie
 *
 */
public class Sent_Style {
	private static String _id = "";
	private static String _result = "";
	private static String _padding = "";			//填充
	private static float _font_size = 0;
	
	public static String get_result(){
		String result = "";
		
		if (_padding.length() != 0){
			_padding = "<字:填充 uof:locID=\"t0093\">" + _padding + "</字:填充>";
		}
		result = _result + _padding + "</uof:句式样>";
		
		_padding = "";
		_result = "";
		
		return result;
	}
	
	public static float get_fontsize(){
		float fontsize = _font_size;
		_font_size = 0;
		return fontsize;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:default-style")){
			_result = "<uof:句式样  字:标识符=\"defaultf\" 字:名称=\"defaultf\" 字:类型=\"auto\">";
			_id = "defaultf";
		}
		else if(qName.equals("style:style")){
			String styleType = "";
			styleType = Style_Data.is_auto_style() ? "auto" : "custom";
			
			_result = "<uof:句式样";
			if((attVal = atts.getValue("style:name")) != null){
				attVal = Style_Data.rename(attVal);
				_result += " 字:标识符=\"" + attVal + "\"";
				_result += " 字:名称=\"" + attVal + "\"";
				_id = attVal;
			}
			if((attVal = atts.getValue("style:parent-style-name")) != null){
				_result += " 字:基式样引用=\"" + attVal + "\"";
				Style_Data.set_parent_style(attVal);
			}
			else {
				_result += " 字:基式样引用=\"defaultf\"";
				Style_Data.set_parent_style("defaultf");
			}
			_result += " 字:类型=\"" + styleType + "\">";
		}
		else if (qName.equals("style:text-properties")) {
			_result += process_text_atts(atts);
		}
		else if(qName.equals("style:background-image")){
			_padding += deal_with_bg_image(atts);
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("style:style") || qName.equals("style:default-style")) {
			Style_Data.set_parent_style("");
			Style_Data.add_font_size(_id,_font_size);
			_id = "";
			_font_size = 0;
		}
	}
	
	public static String process_text_atts(Attributes atts){
		String attVal = "";
		String lineTypeVal = "";
		String lineStyleVal = "";
		String underlineVal = "";
		String result = "";
		
		//======字体=====
		attVal = atts.getValue("fo:font-family");
		if(attVal != null){
			Font_Face.add_font(attVal,attVal);
		}
		
		attVal = atts.getValue("style:font-family-asian");
		if(attVal != null){
			Font_Face.add_font(attVal,attVal);
		}
		
		if(!get_font_val(atts).equals("")){
			result += "<字:字体" + get_font_val(atts) + "/>";
		}
		
		//======粗体=====
		if((attVal = atts.getValue("fo:font-weight")) != null
				&& !attVal.equals("normal")) {
			result += "<字:粗体 字:值=\"true\"/>";
		}
		else if ((attVal = atts.getValue("style:font-weight-asian")) != null 
				&& !attVal.equals("normal")){ 
			result += "<字:粗体 字:值=\"true\"/>";
		}
		else if ((attVal = atts.getValue("style:font-weight-complex")) != null 
				&& !attVal.equals("normal")){ 
			result += "<字:粗体 字:值=\"true\"/>";
		}
		
		//======斜体=====
		if((attVal = atts.getValue("fo:font-style")) != null
				&& !attVal.equals("normal")){
			result += "<字:斜体 字:值=\"true\"/>";
		}
		else if((attVal = atts.getValue("style:font-style-asian")) != null
				&& !attVal.equals("normal")){
			result += "<字:斜体 字:值=\"true\"/>";
		}
		else if((attVal = atts.getValue("style:font-style-complex")) != null
				&& !attVal.equals("normal")){
			result += "<字:斜体 字:值=\"true\"/>";
		}
		
		//======突出显示=====
		
		//======边框=====
		
		//======填充=====
		if((attVal=atts.getValue("fo:background-color")) != null) {
			if (!attVal.equals("transparent")){
				_padding += "<图:颜色>" + attVal + "</图:颜色>";
			}
		}
		
		//======删除线=====
		if((attVal=atts.getValue("style:text-line-through-type"))!=null
				|| (attVal=atts.getValue("style:text-line-through-style"))!=null) {
			result +=  "<字:删除线 字:类型=\"" + conv_line_type(attVal) + "\"/>";
		}
		
		//======下划线=====
		if((attVal=atts.getValue("style:text-underline-type")) != null) {
			lineTypeVal = attVal.equals("none") ? "" : attVal;
		}
		if((attVal=atts.getValue("style:text-underline-style")) != null) {
			lineStyleVal = attVal.equals("none") ? "" : attVal;
		}
		if(!get_line_type(lineTypeVal, lineStyleVal).equals("")){
			underlineVal +=  " 字:类型=\"" + get_line_type(lineTypeVal, lineStyleVal) + "\"";
		}
		if((attVal=atts.getValue("style:text-underline-color")) != null) {
			if(attVal.equals("font-color")){
				attVal = "auto";
			}
			underlineVal += " 字:颜色=\"" + attVal + "\"";
		}
		if ((attVal=atts.getValue("style:text-underline-mode")) != null) {
			if(attVal.equals("skip-white-space")){
				underlineVal += "  字:字下划线=\"true\"";
			}
		}
		if(!underlineVal.equals("")){
			result += "<字:下划线" + underlineVal + "/>";
		}
		
		//======着重号=====
		if((attVal=atts.getValue("style:text-emphasize")) != null && !attVal.equals("none")) {
			result += "<字:着重号 字:类型=\"dot\"/>";
		}
		
		//======隐藏文字=====
		if((attVal=atts.getValue("text:display"))!= null) {
			if (attVal.equals("true")){ 
				result += "<字:隐藏文字 字:值=\"false\"/>";
			}
			else if (attVal.equals("none")){
				result += "<字:隐藏文字 字:值=\"true\"/>";
			}
		}
		
		//======空心=====???
		if((attVal=atts.getValue("style:text-outline")) != null) {
			result += "<字:空心 字:值=\"" + attVal + "\"/>";
		}
		
		//======浮雕=====
		if((attVal=atts.getValue("style:font-relief")) != null) {
			if (attVal.equals("embossed")){ 
				result += "<字:浮雕 字:类型=\"emboss\"/>";	
			}
			else if(attVal.equals("engraved")){
				result += "<字:浮雕 字:类型=\"engrave\"/>";
			}
			else{ 
				result += "<字:浮雕 字:类型=\"none\"/>";
			}
		}
		
		//======阴影=====
		if((attVal=atts.getValue("fo:text-shadow")) != null && !attVal.equals("none")){
			result += " <字:阴影 字:值=\"true\"/>";
		}
		//======醒目字体=====
		if((attVal = atts.getValue("fo:text-transform")) != null && !attVal.equals("none")){ 
			result += " <字:醒目字体 字:类型=\"";
			if(attVal.equals("lowercase")){
				result += "lowercase";
			}
			else if(attVal.equals("uppercase")){
				result += "uppercase";
			}
			else if(attVal.equals("capitalize")){
				result += "capital";
			}
			else if(attVal.equals("none")){
				result += "none";
			}
			result += "\"/>";
		}
		else if((attVal=atts.getValue("fo:font-variant")) != null && attVal.equals("small-caps")){
			result += "<字:醒目字体 字:类型=\"small-caps\"/>";
		}
		
		//======位置=====
		
		//======缩放=====
		if((attVal=atts.getValue("style:text-scale")) != null) {
			int index = attVal.indexOf("%"); 
			
			if(index != -1){
				attVal = attVal.substring(0,index);
				result += "<字:缩放 uof:locID=\"t0103\">" + attVal + "</字:缩放>";
			}
		}
		
		//======字符间距=====
		if((attVal=atts.getValue("fo:letter-spacing")) != null){
			if(!attVal.equals("normal")){
				result += "<字:字符间距>";
				result += Unit_Converter.convert(attVal);
				result += "</字:字符间距>";
			}
		}
		
		//======上下标=====
		if ((attVal = atts.getValue("style:text-position")) != null) {
			String supOrSub = "sup";
			
			if (attVal.startsWith("super")) {
				supOrSub = "sup";
			}
			else if (attVal.startsWith("sub")) {
				supOrSub = "sub";
			}
			else if(attVal.startsWith("0%")){
				supOrSub = "";
			}
			else if(attVal.contains("-")) {  //为一个百分数值
				supOrSub = "sub";
			}	
			
			if(!supOrSub.equals("")){
				result += "<字:上下标 字:值=\"" + supOrSub + "\"/>";
			}
		}
		
		//======调整字间距=====
		
		//======字符对齐网格=====
		
		return result;
	}
	
	public static String deal_with_bg_image(Attributes atts){	
		String attVal = "";
		String padding = "";
		String repeat = "";
		
		if((attVal = atts.getValue("style:repeat")) != null){
			if(attVal.equals("no-repeat")){
				repeat += "center";
			}
			else if(attVal.equals("repeat")){
				repeat += "tile";
			}
			else if(attVal.equals("stretch")){
				repeat += "stretch";
			}
			padding += " 图:位置=\"" + repeat + "\"";
		}
		
		if ((attVal = atts.getValue("xlink:href")) != null) {
			String objID = "";
			
			objID = Media_Obj.process_href(attVal);
			padding += (" 图:图形引用=\"" + objID + "\"");
		}
		
		if ((attVal = atts.getValue("draw:name")) != null){ 
			padding += (" 图:名称=\"" + attVal + "\"");
		}
		
		if (padding.length() != 0){
			padding = "<图:图片" + padding + "/>";
		}
		
		return padding;
	}
	
	private static String get_font_val(Attributes atts){
		String attVal = "";
		String font = "";
		
		attVal = atts.getValue("style:font-name-asian");
		if(attVal == null){
			attVal = atts.getValue("style:font-family-asian");
		}
		if(attVal != null){
			font += " 字:中文字体引用=\"" + attVal + "\"";
		}
		
		attVal = atts.getValue("style:font-name");
		if(attVal == null){
			attVal = atts.getValue("fo:font-family");
		}
		if(attVal != null){
			font += " 字:西文字体引用=\"" + attVal + "\"";
		}
		
		attVal = atts.getValue("style:font-name-complex");
		if(attVal != null){
			font += " 字:特殊字体引用=\"" + attVal +"\"";
		}
		
		if((attVal = atts.getValue("fo:color")) != null){
			font += " 字:颜色=\"" + attVal + "\"";
		}
		
		if((attVal = atts.getValue("fo:font-size")) != null) {
			_font_size = font_size(attVal);
			font += " 字:字号=\"" + _font_size + "\"";
		}
		else if((attVal = atts.getValue("style:font-size-asian")) != null) {
			_font_size = font_size(attVal);
			font += " 字:字号=\"" + _font_size + "\"";
		}
		else if((attVal = atts.getValue("style:font-size-complex")) != null) {
			//_font_size = font_size(attVal); //有问题
			//font += " 字:字号=\"" + _font_size + "\"";
		}	
		else if(!font.equals("")){
			//在单元格式样中，如果只存在中文字体引用的话，显示会出现问题
			String fileType = Common_Data.get_file_type();
			if(fileType.equals("text")){
				font += " 字:字号=\"12\"";			//(odf)text默认字号设为12
			}
			else if(fileType.equals("spreadsheet")){
				font += " 字:字号=\"10\"";			//(odf)spreadsheet默认字号设为10
			}
		}	
		
		return font;
	}
	
	private static float font_size(String attVal){
		float size = 0;
		
		if (!attVal.contains("%")) {
			size = Float.valueOf(Unit_Converter.convert(attVal));
		}
		else { 
			String p = Style_Data.get_parent_style();
			String centVal = attVal.substring(0,attVal.indexOf("%"));
			
			if (!p.equals("")) {
				float parentSize = Style_Data.get_font_size(p);
				float percent = Float.valueOf(centVal);
				
				size = parentSize * percent / 100;
			}
		}
		
		return size;
	}
	
	private static String conv_line_type(String val){
		String type = "single";
		
		if(val.equals("none")){
			type = "none";
		}
		else if(val.equals("single")){
			type = "single";
		}
		else if(val.equals("double")){
			type = "double";
		}
		
		return type;
	}
	
	private static String get_line_type(String typeVal, String styleVal){
		String type = "single";
		
		if(typeVal.equals("") && styleVal.equals("")){
			type = "";
		}
		
		if(styleVal.equals("solid")){
			type = "single";
		}
		else if(styleVal.equals("dotted")){
			type = "dotted";
		}
		else if(styleVal.equals("long-dash")){
			type = "dash-long";
		}
		else if(styleVal.equals("dot-dash")){
			type = "dot-dash";
		}
		else if(styleVal.equals("dot-dot-dash")){
			type = "dot-dot-dash";
		}
		else if(styleVal.equals("wave") && typeVal.equals("double")){
			type = "wave-double";
		}
		
		if(typeVal.equals("double") && !type.equals("wave-double")){
			type = "double";
		}
		
		return type;
	}
}