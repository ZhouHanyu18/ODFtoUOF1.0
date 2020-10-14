package styles;

import org.xml.sax.Attributes;

import convertor.Common_Pro;
import convertor.Unit_Converter;
import stored_data.Spreadsheet_Data;
import stored_data.Style_Data;
import stored_data.Common_Data;

/**
 * 处理style:family="table-cell"的<style:style> 到 <uof:单元格式样>的转换。
 * 
 * @author xie
 *
 */
public class Cell_Style extends Common_Pro{
	private static String _style_name = "";
	
	//attributes of <uof:单元格式样>
	private static String _ele_atts = "";
	private static String _format_change = "";   			//格式修订
	private static String _sent_pro = "";   				//字体格式
	
	private static String _hori_align = "";   				//水平对齐方式
	private static String _vert_align = "";					//垂直对齐方式
	private static String _indent = "";   					//缩进
	private static String _direction = "";					//文字方向
	private static String _rotation_angle = "";				//文字旋转角度
	private static String _auto_newline = "";   			//自动换行
	private static String _contract_padding = "";  			//缩小字体填充
	
	private static String _num_style = "";   				//数字格式
	private static String _border = "";   					//边框
	private static String _padding = "";   					//填充
	private static String _text_align_source = "";
	
	private static boolean _mapped_style = false;
	
	private static void clear(){
		_style_name = "";
		_ele_atts = "";
		_format_change = "";
		_sent_pro = "";
		_hori_align = "";
		_vert_align = "";
		_indent = "";
		_direction = "";
		_rotation_angle = "";
		_auto_newline = "";
		_contract_padding = "";
		_num_style = "";
		_border = "";
		_padding = "";
		_text_align_source = "";
		_mapped_style = false;
	}
	
	public static String get_result(){
		String result = "";
		
		if(_sent_pro.equals("")){
			//_sent_pro = "<字:字体 字:字号=\"10\"/>";
		}
		
		result += "<uof:单元格式样" + _ele_atts + ">";
		result += "<表:字体格式>" + _format_change + _sent_pro + "</表:字体格式>";
		result += "<表:对齐格式>" + _hori_align + _vert_align + _indent + _direction 
				+ _rotation_angle + _auto_newline + _contract_padding + "</表:对齐格式>";
		result += _num_style + _border + _padding;
		result += "</uof:单元格式样>";
		
		if(_mapped_style){
			result = "";
		}
		clear();
		
		return  result;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:default-style")){
			_ele_atts = " 表:基式样引用=\"defaultc\"  表:类型=\"auto\"";
		}
		
		else if(qName.equals("style:style")){
			String styleType = "";
			styleType = Style_Data.is_auto_style() ? "auto" : "custom";
			
			if((attVal=atts.getValue("style:name"))!=null){
				_style_name = attVal;
				_ele_atts += " 表:标识符=\"" + _style_name + "\"";
				_ele_atts += " 表:名称=\"" + _style_name + "\"";
			}
			
			if((attVal=atts.getValue("style:parent-style-name")) != null){
				_ele_atts += " 表:基式样引用=\"" + attVal + "\"";
			}else {
				_ele_atts += " 字:基式样引用=\"defaultc\"";
			}
			
			if((attVal=atts.getValue("style:data-style-name")) != null){
				//_num_style = Style_Data.get_data_style(attVal);
			}
			
			_ele_atts += " 表:类型=\"" + styleType + "\"";
		}
		
		else if(qName.equals("style:table-cell-properties")){
			if((attVal=atts.getValue("style:text-align-source")) != null){
				_text_align_source = attVal;
			}
			
			if((attVal=atts.getValue("style:repeat-content")) != null){
				if(attVal.equals("true")){
					_hori_align = "<表:水平对齐方式>fill</表:水平对齐方式>";
				}
			}
			
			if((attVal=atts.getValue("style:vertical-align")) != null){
				_vert_align = "<表:垂直对齐方式>";
				_vert_align += conv_vertical_align(attVal);
				_vert_align += "</表:垂直对齐方式>";
			}
			
			if ((attVal=atts.getValue("style:direction")) != null) {
				if (attVal.equals("ltr")){
					_direction = "<表:文字方向 uof:locID=\"s0118\">horizontal</表:文字方向>";
				}
				else if (attVal.equals("ttb")){
					_direction = "<表:文字方向 uof:locID=\"s0118\">vertical</表:文字方向>";
				}
			}
			
			//需要改进。ODF的文字旋转角度是个非负整数，UOF的则是-90到90之间。
			if ((attVal=atts.getValue("style:rotation-angle")) != null) {
				_rotation_angle = "<表:文字旋转角度>";
				_rotation_angle += conv_rotation_angle(attVal);
				_rotation_angle += "</表:文字旋转角度>";
			}
			
			if ((attVal=atts.getValue("fo:wrap-option")) != null) {
				if(attVal.equals("wrap")){
					_auto_newline = "<表:自动换行 表:值=\"true\"/>";
				}
			}	
			if ((attVal=atts.getValue("style:shrink-to-fit")) != null) {		//?????????
				_contract_padding = "<表:缩小字体填充 表:值=\"" + attVal + "\"/>";
			}
			
			//表:边框
			_border = get_borders("spreadsheet", atts);
			
			//表:填充
			if((attVal=atts.getValue("fo:background-color")) != null) {
				if (!attVal.equals("transparent")){
					String padding = "<图:颜色>" + attVal + "</图:颜色>";
					_padding = "<表:填充>" + padding + "</表:填充>";
				}
			}
		}
		
		else if(qName.equals("style:paragraph-properties")){
			if((attVal=atts.getValue("fo:text-align")) != null){
				if(_text_align_source.equals("fix") && !_hori_align.equals("")){
					_hori_align = "<表:水平对齐方式>";
					_hori_align += conv_text_align(attVal);
					_hori_align += "</表:水平对齐方式>";
				}
			}
			if((attVal=atts.getValue("fo:margin-left")) !=null ){	//<表:缩进>的单位为一个字符宽度（暂定为10pt）
				float fVal = Float.parseFloat(Unit_Converter.convert(attVal))/10.0f;
				int iVal = new Float(fVal).intValue();
				_indent += "<表:缩进>" + iVal + "</表:缩进>";
			}
		}
		
		else if(qName.equals("style:background-image")){
			_padding = "<表:填充>";
			_padding += Sent_Style.deal_with_bg_image(atts);
			_padding += "</表:填充>";
		}
		
		else if(qName.equals("style:text-properties")){
			_sent_pro = Sent_Style.process_text_atts(atts);
		}
		
		else if(qName.equals("style:map")){
			if(!_mapped_style){
				_mapped_style = true;
				Spreadsheet_Data.add_style_name(_style_name);
			}
			
			if(Common_Data.get_file_type().equals("spreadsheet")){
				Style_Map.process_ele(_style_name, atts);
				
			}
		}
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
			convVal = "justify";
		}
		else if(val.equals("center")){
			convVal = "center";
		}
		
		return convVal;
	}
	
	private static String conv_vertical_align(String val){
		//default value is "center" in uof
		String convVal = "center";
		
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

	//0~360>>>-90~90
	private static String conv_rotation_angle(String val){
		int i = Integer.parseInt(val);
		
		if(i>90 && i<=270){
			i = 180-i;
		}
		if(i>270 && i<=360){
			i = i-360;
		}
		
		return Integer.toString(i);	
	}
}
