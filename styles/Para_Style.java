package styles;

import org.xml.sax.Attributes;

import stored_data.Style_Data;
import convertor.Common_Pro;
import convertor.Unit_Converter;

/**
 * 处理style:family="paragraph"的<style:style> 到 <uof:段落式样>的转换。
 * 
 * @author xie
 *
 */
public class Para_Style extends Common_Pro{
	private static String _id = "";
	//the result
	private static String _result = "";
	//attributes of <uof:段落式样>
	private static String _ele_atts = "";
	//<字:句属性>
	private static String _sent_pro = "";			
	//制表位设置
	private static String _tab_stops = "";			
	//tag for filtration
	private static boolean _filter_tag = false;
	
	//为了<图：图形>处理的需要
	private static String _leftMargin = "";
	private static String _rightMargin = "";
	private static String _topMargin = "";
	private static String _bottomMargin = "";
	private static String _writing_mode = "";
	
	
	private static void clear(){
		_result = "";
		_ele_atts = "";
		_sent_pro = "";
		_tab_stops = "";
	}
	
	public static String get_result(){
		String rst = "";
		
		rst = "<uof:段落式样" + _ele_atts + ">";
		rst += "<字:句属性>" + _sent_pro + "</字:句属性>";
		rst += _result;
		rst += "</uof:段落式样>";
		
		clear();
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(_filter_tag){
			return ;
		}
		else if(qName.equals("style:graphic-properties")){
			_filter_tag = true;
		}
		
		else if(qName.equals("style:default-style")){
			_ele_atts = " 字:标识符=\"defaultp\"" + " 字:名称=\"defaultp\"" + " 字:类型=\"auto\"";
			_id = "defaultp";
		}
		
		else if(qName.equals("style:style")){
			String styleType = "";
			styleType = Style_Data.is_auto_style() ? "auto" : "custom";
			
			if((attVal = atts.getValue("style:name")) != null){
				attVal = Style_Data.rename(attVal);
				_ele_atts += " 字:标识符=\"" + attVal + "\"";
				_ele_atts += " 字:名称=\"" + attVal + "\"";
				_id = attVal;
			}
			
			attVal = atts.getValue("style:parent-style-name");
			attVal = (attVal==null) ? "defaultp" : attVal;
			_ele_atts += " 字:基式样引用=\"" + attVal + "\"";
			Style_Data.set_parent_style(attVal);
			
			if((attVal = atts.getValue("style:display-name")) != null){
				_ele_atts += " 字:别名=\"" + attVal + "\"";
					
			}
			_ele_atts += " 字:类型=\"" + styleType + "\"";
			
			if ((attVal=atts.getValue("style:default-outline-level"))!=null 
					&& !attVal.equals("10")) {
				int level = Integer.valueOf(attVal) -1;
				_result += "<字:大纲级别>" + level + "</字:大纲级别>";	
				//_result += "<字:自动编号信息 字:编号引用=\"outline\"" +
					//	" 字:编号级别=\"" + level + "\"/>";
			}
		}
		
		else if(qName.equals("style:text-properties")){
			_sent_pro = Sent_Style.process_text_atts(atts);		
			
			if (atts.getValue("fo:hyphenate") != null) {
				_result += "<字:允许单词断字 字:值=\"" + atts.getValue("fo:hyphenate") + "\"/>";
			}
		}
		
		else if(qName.equals("style:paragraph-properties")){
			_result += process_para_atts(atts);
		}
		
		else if(qName.equals("style:background-image")){
			String pad = Sent_Style.deal_with_bg_image(atts);
			if(!pad.equals("")){
				_result += "<字:填充 uof:locID=\"t0066\">" + pad + "</字:填充>";
			}
		}
		
		//====首字下沉====
		else if(qName.equals("style:drop-cap")){
			String dropCap ="";
			
			dropCap += " <字:首字下沉 字:类型=\"dropped\""; 
			if((attVal = atts.getValue("style:style-name")) != null){	
				dropCap +=" 字:字体引用=\"" + attVal + "\"";
			}
			if((attVal = atts.getValue("style:length")) != null){
				if(attVal.equals("word")){
					dropCap +=" 字:字符数=\"1\"" ; 
				}
				else{ 
					dropCap +=" 字:字符数=\"" + attVal + "\"";
				}
			}
			//行数必须
			if((attVal = atts.getValue("style:lines")) != null){
				dropCap +=" 字:行数=\"" + attVal + "\"";
			}
			else{
				dropCap +=" 字:行数=\"1\"";
			}
			//间距必须
			if((attVal = atts.getValue("style:distance")) != null){
				dropCap +=" 字:间距=\"" + Unit_Converter.convert(attVal) + "\"";
			}
			else{
				dropCap +=" 字:间距=\"0\"";
			}		
			dropCap += "/>";		
			_result += dropCap;
		}
		
		else if(qName.equals("style:tab-stops")){
			_tab_stops +="<字:制表位设置>";
		}
		else if(qName.equals("style:tab-stop")){
			String tab = "<字:制表位";
			
			if((attVal = atts.getValue("style:position")) != null){
				tab += " 字:位置=\"" + Unit_Converter.convert(attVal) +"\"";
			}
			if((attVal = atts.getValue("style:type")) != null
				&& (attVal.equals("left") || attVal.equals("center") 
						|| attVal.equals("right"))) {
				tab +=" 字:类型=\"" + attVal +"\"";
			}
			if((attVal=atts.getValue("style:leader-style")) != null){
				int style = 0;			//前导符是数字：1-5
								
				if(attVal.equals("none")){
					style = 0;
				}
				else if(attVal.equals("dash")){
					style = 2;
				}
				else if(attVal.equals("solid")){
					style = 3;
				}
				else if(attVal.equals("dotted")){
					style = 4;
				}
				else {
					style = 1;
				}
				
				tab +=" 字:前导符=\"" + style +"\"";
			}

			tab += "/>";		
			_tab_stops += tab;

		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("style:graphic-properties")){
			_filter_tag = false;
		}
		else if(_filter_tag){
			return ;
		}
		
		else if(qName.equals("style:tab-stops")){
			_tab_stops +="</字:制表位设置>";
		}
		else if (qName.equals("style:default-style") || qName.equals("style:style")) {
			float fontSize = Sent_Style.get_fontsize();
			if(fontSize != 0){
				Style_Data.add_font_size(_id,fontSize);
			}
			
			Style_Data.add_para_grapro(_id,_leftMargin + "一" + _rightMargin + "二"
					+ _topMargin + "三" + _bottomMargin + "四" + _writing_mode + "五");    //分为五个字段,"一二三四"便于检索
			_id = "";

			_leftMargin = "";
			_rightMargin = "";
			_topMargin = "";
			_bottomMargin = "";
			_writing_mode = "";
		}
		
		_result += _tab_stops;
		_tab_stops = "";
	}
	
	public static String process_para_atts(Attributes atts){
		String attVal = "";
		String result = "";
		
		//======对齐=====
		String alignVal = "";
		if((attVal=atts.getValue("fo:text-align")) != null){
			alignVal +=  " 字:水平对齐=\"" + conv_text_align(attVal) + "\"";
		}
		if((attVal=atts.getValue("style:vertical-align")) != null){
			alignVal += " 字:文字对齐=\"" + conv_vertical_align(attVal) + "\"";
		}
		if(!alignVal.equals("")){
			result += "<字:对齐  uof:locID=\"t0055\" uof:attrList=\"水平对齐 文字对齐\"" 
				+ alignVal + "/>";
		}
		
		//======缩进=====
		String indentVal = "";
		if((attVal=atts.getValue("fo:margin-left")) != null){
			indentVal += "<字:左>";
			indentVal += "<字:绝对 uof:locID=\"t0185\" uof:attrList=\"值\"" +
						" 字:值=\"" + Unit_Converter.convert(attVal) + "\"/>";;		
			indentVal += "</字:左>";
		}
		if((attVal=atts.getValue("fo:margin-right")) != null){
			indentVal += "<字:右>";
				indentVal += "<字:绝对 uof:locID=\"t0187\" uof:attrList=\"值\"" +
							" 字:值=\"" + Unit_Converter.convert(attVal) + "\"/>";;
			indentVal += "</字:右>";
		}
		if((attVal=atts.getValue("fo:text-indent")) != null){
			indentVal += "<字:首行>";
			indentVal += "<字:绝对 uof:locID=\"t0189\" uof:attrList=\"值\"" +
						" 字:值=\"" + Unit_Converter.convert(attVal) + "\"/>";;		
			indentVal += "</字:首行>";
		}
		if(!indentVal.equals("")){
			result += "<字:缩进 uof:locID=\"t0056\">" + indentVal + "</字:缩进>";
		}
		
		//======行距=====
		String rowSpace = "";
		if((attVal = atts.getValue("fo:line-height")) != null && !attVal.equals("normal")) {
			if(attVal.contains("%")) {
				rowSpace  += "<字:行距 字:类型=\"multi-lines\" 字:值=\"" 
					+ Unit_Converter.from_percent(attVal) + "\"/>";
			}
			else {
				rowSpace  += "<字:行距 字:类型=\"fixed\" 字:值=\"" 
					+ Unit_Converter.convert(attVal) + "\"/>";
			}
		}
		else if((attVal = atts.getValue("style:line-height-at-least")) != null){	
			rowSpace += "<字:行距 字:类型=\"at-least\" 字:值=\""
					+ Unit_Converter.convert(attVal) + "\"/>";
		}
		else if((attVal = atts.getValue("style:line-spacing")) != null){
			rowSpace += " <字:行距 字:类型=\"line-space\" 字:值=\"" 
					+ Unit_Converter.convert(attVal) + "\"/>";
		}
		result += rowSpace;	
		
		//======段前距=====
		String paraSpace = "";
		if ((attVal = atts.getValue("fo:margin-top")) != null) {
			paraSpace += "<字:段前距>";
			paraSpace += "<字:绝对值 uof:locID=\"t0199\" uof:attrList=\"值\"" +
					" 字:值=\"" + Unit_Converter.convert(attVal) + "\"/>";;
			paraSpace += "</字:段前距>";
		}
		if ((attVal = atts.getValue("fo:margin-bottom")) != null) {
			paraSpace += "<字:段后距>";
			paraSpace += "<字:绝对值 uof:locID=\"t0202\" uof:attrList=\"值\"" +
					" 字:值=\"" + Unit_Converter.convert(attVal) + "\"/>";;
			paraSpace += "</字:段后距>";
		}
		if(paraSpace.length() != 0){
			result += "<字:段间距>" + paraSpace + "</字:段间距>";
		}
		
		//======自动编号信息=====
		
		//======孤行控制=====
		if((attVal = atts.getValue("fo:orphans")) != null){
			result += "<字:孤行控制>" + attVal + "</字:孤行控制>";
		}
		
		//======寡行控制=====
		if((attVal = atts.getValue("fo:widows")) != null){
			result += "<字:寡行控制>" + attVal + "</字:寡行控制>";
		}
		
		//======段中不分页=====
		if((attVal = atts.getValue("fo:keep-together")) != null){
			if(attVal.equals("always")){
				result += "<字:段中不分页 字:值=\"true\"/>"; 
			}
		}
		
		//======与下段同页=====
		if((attVal = atts.getValue("fo:keep-with-next")) != null){
			if(attVal.equals("always")){
				result += "<字:与下段同页 字:值=\"true\"/>"; 
			}
		}
		
		//段前分页
		if((attVal = atts.getValue("fo:break-before")) != null){
			if(attVal.equals("page")){
				result += "<字:段前分页 字:值=\"true\"/>";
			}
		}
		
		//======边框=====
		result += get_borders("text", atts);
		
		//======填充=====
		if((attVal=atts.getValue("fo:background-color")) != null) {
			if (!attVal.equals("transparent")){
				String padding = "";
				
				padding = "<图:图案 图:类型=\"清除\"" +
						" 图:前景色=\"auto\" 图:背景色=\"" + attVal + "\"/>";
				result += "<字:填充 uof:locID=\"t0066\">" + padding + "</字:填充>";
			}
		}
		
		//======对齐网格=====
		if((attVal = atts.getValue("style:snap-to-layout-grid")) != null){
			if(attVal.equals("true")){
				result += "<字:对齐网格 字:值=\"" + attVal + "\"/>";
			}
		}
		
		//======取消断字=====对应关系有问题
		
		//======取消行号=====
		if((attVal = atts.getValue("text:number-lines")) != null){
			if(attVal.equals("true")){
				result += "<字:取消行号 字:值=\"false\"/>";
			}
			else {
				result += "<字:取消行号 字:值=\"true\"/>";
			}
		}
		
		//允许单词断字  fo:hyphenate(text-properties)
		
		//======是否行首标点压缩=====
		if((attVal = atts.getValue("style:punctuation-wrap")) != null){
			if(attVal.equals("hanging")){
				result += "<字:是否行首标点压缩 字:值=\"true\"/>";
			}
			else { 
				result += "<字:是否行首标点压缩 字:值=\"false\"/>";	
			}
		}
		
		//======自动调整中英文字符间距====
		if((attVal = atts.getValue("style:text-autospace")) != null){
			if(attVal.equals("ideograph-alpha"))
			{
				result += "<字:自动调整中英文字符间距 字:值=\"true\"/>";
				result += "<字:自动调整中文与数字间距 字:值=\"true\"/>";
			}
			else  {
				result += "<字:自动调整中英文字符间距 字:值=\"false\"/>";
				result += "<字:自动调整中文与数字间距 字:值=\"false\"/>";
			}
		}
		
		return result;
	}
	
	private static String conv_text_align(String val){
		String convVal = "general";
		
		if(val.equals("start")){
			convVal = "left";
		}
		else if(val.equals("end")){
			convVal = "right";
		}
		else if(val.equals("left")){
			convVal = "left";
		}
		else if(val.equals("right")){
			convVal = "right";
		}
		else if(val.equals("justify")){
			convVal = "justified";
		}
		else if(val.equals("center")){
			convVal = "center";
		}
		
		return convVal;
	}
	
	private static String conv_vertical_align(String val){
		String convVal = "bottom";
		
		if(val.equals("top")){
			convVal = "top";
		}
		else if(val.equals("middle")){
			convVal = "center";
		}
		else if(val.equals("bottom")){
			convVal = "bottom";
		}
		
		return convVal;
	}
}
