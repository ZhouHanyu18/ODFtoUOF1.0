package convertor;

import org.xml.sax.Attributes;
/**
 * 处理 纸张方向、纸张、页边距、边框 的转换。
 * 
 * @author xie
 *
 */
public class Common_Pro {
	
	private static String get_pre(String fileType){
		String pre = "字:";
		
		if(fileType.equals("text")){
			pre = "字:";
		}
		else if(fileType.equals("spreadsheet")){
			pre = "表:";
		}
		else if(fileType.equals("presentation")){
			pre = "演:";
		}
		
		return pre;
	}
	
	//处理<纸张方向>的转换
	protected static String get_orientation(String fileType, Attributes atts){
		String str = "";
		String attVal = "";
		
		if((attVal=atts.getValue("style:print-orientation")) != null){
			str =  attVal;
		}
		
		if(str.length() != 0){
			str = "<" + get_pre(fileType) + "纸张方向>" + str + "</" + get_pre(fileType) + "纸张方向>";
		}
		
		return str;
	}
	
	//处理<纸张>的转换
	protected static String get_page(String fileType, Attributes atts){
		String str = "";
		String attVal = "";

		if((attVal=atts.getValue("fo:page-width")) != null){
			String width = Unit_Converter.convert(attVal);
			if (fileType.equals("presentation")){ //永中演示文稿的纸张长宽必须为整数
				str += " uof:宽度=" + "\"" + Math.round(Float.valueOf(width)) + "\"";
			}
			else{
				str += " uof:宽度=" + "\"" + width + "\"";
			}
		}
		if((attVal=atts.getValue("fo:page-height")) != null){
			String height = Unit_Converter.convert(attVal);
			if (fileType.equals("presentation")){
				str += " uof:高度=" + "\"" + Math.round(Float.valueOf(height)) + "\"";
			}
			else{
				str += " uof:高度=" + "\"" + height + "\"";
			}
		}
		if((attVal=atts.getValue("style:paper-tray-name")) != null){
			str += " uof:纸型=" + "\"" + attVal + "\"";
		}
		
		if(str.length() != 0){
			str = "<" + get_pre(fileType) + "纸张" + str + "/>";
		}
		
		return str;
	}
	
	//处理<页边距>的转换
	protected static String get_margins(String fileType, Attributes atts){
		String str = "";
		String attVal = "";
		
		if((attVal=atts.getValue("fo:margin-top")) != null){
			str += " uof:上=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal=atts.getValue("fo:margin-left")) != null){
			str += " uof:左=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal=atts.getValue("fo:margin-bottom")) != null){
			str += " uof:下=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal=atts.getValue("fo:margin-right")) != null){
			str += " uof:右=" + "\"" + Unit_Converter.convert(attVal) + "\"";
		}
		
		if(str.length() != 0){
			str = "<" + get_pre(fileType) + "页边距" + str + "/>";
		}

		return str;
	}
	
	//处理边框：从"0.002cm solid #000000"形式的字符串提取出各种值 
	public static String tranBorderValue(String border)
	{
		int index1 = border.indexOf(' ');
		int index2 = border.lastIndexOf(' ');
		String str = "";
		
		if(border.equals("none")){
			str = " uof:类型=\"none\"";
		}
		else {
			String width = border.substring(0,index1);
			String type = border.substring(index1+1,index2);
			String color = border.substring(index2+1);
			
			if(type.equals("solid")){
				str = " uof:宽度=\"" + Unit_Converter.convert(width) + 
					"\" uof:类型=\"single\" uof:颜色=\"" + color + "\"";
			}
			else if(type.equals("double")){
				float widVal = Float.valueOf(Unit_Converter.convert(width));
				
				widVal = (widVal > 4) ? widVal/2 : widVal;
				str = " uof:宽度=\"" + widVal + "\" uof:类型=\"double\" uof:颜色=\"" + color + "\"";
			}
		}
		
		return str;	
	}
	
	//处理<边框>的转换
	protected static String get_borders(String fileType, Attributes atts){
		String border = "";
		String value = "";
		
		value = atts.getValue("fo:border");
		if(value != null && !value.equals("none")) { 
			border += "<uof:左" + tranBorderValue(value) + "/>";
			border += "<uof:上" + tranBorderValue(value) + "/>";
			border += "<uof:右" + tranBorderValue(value) + "/>";
			border += "<uof:下" + tranBorderValue(value) + "/>";
		}
		else{
			if((value = atts.getValue("fo:border-left")) != null){
				border += "<uof:左" + tranBorderValue(value) + "/>";
			}
			if((value = atts.getValue("fo:border-top")) != null){
				border += "<uof:上" + tranBorderValue(value) + "/>";
			}
			if((value = atts.getValue("fo:border-right")) != null){
				border += "<uof:右" + tranBorderValue(value) + "/>";
			}
			if((value = atts.getValue("fo:border-bottom")) != null){
				border += "<uof:下" + tranBorderValue(value) + "/>";
			}
		}
		if((value = atts.getValue("style:diagonal-tl-br")) != null){   //单元格有对角线
			border += "<uof:对角线1" + tranBorderValue(value) + "/>";
		}
		if((value = atts.getValue("style:diagonal-bl-tr")) != null){
			border += "<uof:对角线2" + tranBorderValue(value) + "/>";
		}
		
		if (border.length() != 0){
			if(fileType.equals("text")){
				border = "<字:边框 uof:locID=\"t0065\">" + border + "</字:边框>";
			}
			else if(fileType.equals("spreadsheet")){
				border = "<表:边框 uof:locID=\"s0022\">" + border + "</表:边框>";
			}
			else {
				border = "<演:边框>" + border + "</演:边框>";
			}
		}
		
		return border;
	}
}
