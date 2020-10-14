package styles;

import graphic_content.Media_Obj;

import org.xml.sax.Attributes;

import stored_data.*;
import convertor.Unit_Converter;

/**
 * ����style:family="text"��<style:style> �� <uof:��ʽ��>��ת����
 * 
 * @author xie
 *
 */
public class Sent_Style {
	private static String _id = "";
	private static String _result = "";
	private static String _padding = "";			//���
	private static float _font_size = 0;
	
	public static String get_result(){
		String result = "";
		
		if (_padding.length() != 0){
			_padding = "<��:��� uof:locID=\"t0093\">" + _padding + "</��:���>";
		}
		result = _result + _padding + "</uof:��ʽ��>";
		
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
			_result = "<uof:��ʽ��  ��:��ʶ��=\"defaultf\" ��:����=\"defaultf\" ��:����=\"auto\">";
			_id = "defaultf";
		}
		else if(qName.equals("style:style")){
			String styleType = "";
			styleType = Style_Data.is_auto_style() ? "auto" : "custom";
			
			_result = "<uof:��ʽ��";
			if((attVal = atts.getValue("style:name")) != null){
				attVal = Style_Data.rename(attVal);
				_result += " ��:��ʶ��=\"" + attVal + "\"";
				_result += " ��:����=\"" + attVal + "\"";
				_id = attVal;
			}
			if((attVal = atts.getValue("style:parent-style-name")) != null){
				_result += " ��:��ʽ������=\"" + attVal + "\"";
				Style_Data.set_parent_style(attVal);
			}
			else {
				_result += " ��:��ʽ������=\"defaultf\"";
				Style_Data.set_parent_style("defaultf");
			}
			_result += " ��:����=\"" + styleType + "\">";
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
		
		//======����=====
		attVal = atts.getValue("fo:font-family");
		if(attVal != null){
			Font_Face.add_font(attVal,attVal);
		}
		
		attVal = atts.getValue("style:font-family-asian");
		if(attVal != null){
			Font_Face.add_font(attVal,attVal);
		}
		
		if(!get_font_val(atts).equals("")){
			result += "<��:����" + get_font_val(atts) + "/>";
		}
		
		//======����=====
		if((attVal = atts.getValue("fo:font-weight")) != null
				&& !attVal.equals("normal")) {
			result += "<��:���� ��:ֵ=\"true\"/>";
		}
		else if ((attVal = atts.getValue("style:font-weight-asian")) != null 
				&& !attVal.equals("normal")){ 
			result += "<��:���� ��:ֵ=\"true\"/>";
		}
		else if ((attVal = atts.getValue("style:font-weight-complex")) != null 
				&& !attVal.equals("normal")){ 
			result += "<��:���� ��:ֵ=\"true\"/>";
		}
		
		//======б��=====
		if((attVal = atts.getValue("fo:font-style")) != null
				&& !attVal.equals("normal")){
			result += "<��:б�� ��:ֵ=\"true\"/>";
		}
		else if((attVal = atts.getValue("style:font-style-asian")) != null
				&& !attVal.equals("normal")){
			result += "<��:б�� ��:ֵ=\"true\"/>";
		}
		else if((attVal = atts.getValue("style:font-style-complex")) != null
				&& !attVal.equals("normal")){
			result += "<��:б�� ��:ֵ=\"true\"/>";
		}
		
		//======ͻ����ʾ=====
		
		//======�߿�=====
		
		//======���=====
		if((attVal=atts.getValue("fo:background-color")) != null) {
			if (!attVal.equals("transparent")){
				_padding += "<ͼ:��ɫ>" + attVal + "</ͼ:��ɫ>";
			}
		}
		
		//======ɾ����=====
		if((attVal=atts.getValue("style:text-line-through-type"))!=null
				|| (attVal=atts.getValue("style:text-line-through-style"))!=null) {
			result +=  "<��:ɾ���� ��:����=\"" + conv_line_type(attVal) + "\"/>";
		}
		
		//======�»���=====
		if((attVal=atts.getValue("style:text-underline-type")) != null) {
			lineTypeVal = attVal.equals("none") ? "" : attVal;
		}
		if((attVal=atts.getValue("style:text-underline-style")) != null) {
			lineStyleVal = attVal.equals("none") ? "" : attVal;
		}
		if(!get_line_type(lineTypeVal, lineStyleVal).equals("")){
			underlineVal +=  " ��:����=\"" + get_line_type(lineTypeVal, lineStyleVal) + "\"";
		}
		if((attVal=atts.getValue("style:text-underline-color")) != null) {
			if(attVal.equals("font-color")){
				attVal = "auto";
			}
			underlineVal += " ��:��ɫ=\"" + attVal + "\"";
		}
		if ((attVal=atts.getValue("style:text-underline-mode")) != null) {
			if(attVal.equals("skip-white-space")){
				underlineVal += "  ��:���»���=\"true\"";
			}
		}
		if(!underlineVal.equals("")){
			result += "<��:�»���" + underlineVal + "/>";
		}
		
		//======���غ�=====
		if((attVal=atts.getValue("style:text-emphasize")) != null && !attVal.equals("none")) {
			result += "<��:���غ� ��:����=\"dot\"/>";
		}
		
		//======��������=====
		if((attVal=atts.getValue("text:display"))!= null) {
			if (attVal.equals("true")){ 
				result += "<��:�������� ��:ֵ=\"false\"/>";
			}
			else if (attVal.equals("none")){
				result += "<��:�������� ��:ֵ=\"true\"/>";
			}
		}
		
		//======����=====???
		if((attVal=atts.getValue("style:text-outline")) != null) {
			result += "<��:���� ��:ֵ=\"" + attVal + "\"/>";
		}
		
		//======����=====
		if((attVal=atts.getValue("style:font-relief")) != null) {
			if (attVal.equals("embossed")){ 
				result += "<��:���� ��:����=\"emboss\"/>";	
			}
			else if(attVal.equals("engraved")){
				result += "<��:���� ��:����=\"engrave\"/>";
			}
			else{ 
				result += "<��:���� ��:����=\"none\"/>";
			}
		}
		
		//======��Ӱ=====
		if((attVal=atts.getValue("fo:text-shadow")) != null && !attVal.equals("none")){
			result += " <��:��Ӱ ��:ֵ=\"true\"/>";
		}
		//======��Ŀ����=====
		if((attVal = atts.getValue("fo:text-transform")) != null && !attVal.equals("none")){ 
			result += " <��:��Ŀ���� ��:����=\"";
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
			result += "<��:��Ŀ���� ��:����=\"small-caps\"/>";
		}
		
		//======λ��=====
		
		//======����=====
		if((attVal=atts.getValue("style:text-scale")) != null) {
			int index = attVal.indexOf("%"); 
			
			if(index != -1){
				attVal = attVal.substring(0,index);
				result += "<��:���� uof:locID=\"t0103\">" + attVal + "</��:����>";
			}
		}
		
		//======�ַ����=====
		if((attVal=atts.getValue("fo:letter-spacing")) != null){
			if(!attVal.equals("normal")){
				result += "<��:�ַ����>";
				result += Unit_Converter.convert(attVal);
				result += "</��:�ַ����>";
			}
		}
		
		//======���±�=====
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
			else if(attVal.contains("-")) {  //Ϊһ���ٷ���ֵ
				supOrSub = "sub";
			}	
			
			if(!supOrSub.equals("")){
				result += "<��:���±� ��:ֵ=\"" + supOrSub + "\"/>";
			}
		}
		
		//======�����ּ��=====
		
		//======�ַ���������=====
		
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
			padding += " ͼ:λ��=\"" + repeat + "\"";
		}
		
		if ((attVal = atts.getValue("xlink:href")) != null) {
			String objID = "";
			
			objID = Media_Obj.process_href(attVal);
			padding += (" ͼ:ͼ������=\"" + objID + "\"");
		}
		
		if ((attVal = atts.getValue("draw:name")) != null){ 
			padding += (" ͼ:����=\"" + attVal + "\"");
		}
		
		if (padding.length() != 0){
			padding = "<ͼ:ͼƬ" + padding + "/>";
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
			font += " ��:������������=\"" + attVal + "\"";
		}
		
		attVal = atts.getValue("style:font-name");
		if(attVal == null){
			attVal = atts.getValue("fo:font-family");
		}
		if(attVal != null){
			font += " ��:������������=\"" + attVal + "\"";
		}
		
		attVal = atts.getValue("style:font-name-complex");
		if(attVal != null){
			font += " ��:������������=\"" + attVal +"\"";
		}
		
		if((attVal = atts.getValue("fo:color")) != null){
			font += " ��:��ɫ=\"" + attVal + "\"";
		}
		
		if((attVal = atts.getValue("fo:font-size")) != null) {
			_font_size = font_size(attVal);
			font += " ��:�ֺ�=\"" + _font_size + "\"";
		}
		else if((attVal = atts.getValue("style:font-size-asian")) != null) {
			_font_size = font_size(attVal);
			font += " ��:�ֺ�=\"" + _font_size + "\"";
		}
		else if((attVal = atts.getValue("style:font-size-complex")) != null) {
			//_font_size = font_size(attVal); //������
			//font += " ��:�ֺ�=\"" + _font_size + "\"";
		}	
		else if(!font.equals("")){
			//�ڵ�Ԫ��ʽ���У����ֻ���������������õĻ�����ʾ���������
			String fileType = Common_Data.get_file_type();
			if(fileType.equals("text")){
				font += " ��:�ֺ�=\"12\"";			//(odf)textĬ���ֺ���Ϊ12
			}
			else if(fileType.equals("spreadsheet")){
				font += " ��:�ֺ�=\"10\"";			//(odf)spreadsheetĬ���ֺ���Ϊ10
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