package styles;

import org.xml.sax.Attributes;

import convertor.Unit_Converter;
import convertor.Common_Pro;

/**
 * @author xie
 *
 *
 */
public class Cell_Pro extends Common_Pro{
	//the result
	private static String _cell_pro = "";
	//
//	private static boolean _padding_trans = false;	
	
	private static void clear(){
//		_padding_trans = false;
		_cell_pro = "";
	}
	
	public static String get_result(){
		String result = _cell_pro;

		clear();
		return result;
	}
	
	public static void process_start(String qName, Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:style")){		

		}
		
		else if(qName.equals("style:background-image")){
			String padding = Sent_Style.deal_with_bg_image(atts);
			if(!padding.equals("")){
				_cell_pro +=  "<字:填充 uof:locID=\"t0153\">" + padding + "</字:填充>";
			}
		}
		
		else if(qName.equals("style:table-cell-properties")){
					
			_cell_pro += get_padding(atts);					//单元格边距
					
			_cell_pro += get_borders("text",atts).replace("t0065","t0152");		//边框
					
			attVal = atts.getValue("fo:background-color");	//填充
			if(attVal != null) {						
				/*if (!attVal.equals("transparent")){
					_cell_pro += "<字:填充 uof:locID=\"t0153\">";
					_cell_pro +=  "<uof:颜色>" + attVal + "</uof:颜色>";
					_cell_pro += "</字:填充>";;
				}*/
				if (!attVal.equals("transparent")){		
					_cell_pro += "<字:填充 uof:locID=\"t0153\">";
					_cell_pro += "<图:图案 图:类型=\"清除\"";
					_cell_pro += " 图:前景色=\"auto\" 图:背景色=\"" + attVal + "\"/>";
					_cell_pro += "</字:填充>";
				}
			}
				
			attVal=atts.getValue("style:vertical-align");	//垂直对齐方式
			if (attVal != null) {		
					_cell_pro += "<字:垂直对齐方式>"; 
					_cell_pro += conv_vertical_align(attVal);
					_cell_pro += "</字:垂直对齐方式>";
			}	

			attVal=atts.getValue("fo:wrap-option");			//自动换行
			if (attVal != null) {
				if (attVal.equals("wrap")){
					_cell_pro += "<字:自动换行 字:值=\"false\"/>";
				}
				else{
					_cell_pro += "<字:自动换行 字:值=\"true\"/>";
				}
			}
				
			attVal=atts.getValue("style:shrink-to-fit");//适应文字
			if (attVal != null) {
				_cell_pro += "<字:适应文字 字:值=\"" + attVal + "\"/>";
			}
		}
	}
	
	private static String get_padding(Attributes atts){
		String attVal = "";
		String margin = "";	
		
		if((attVal=atts.getValue("fo:padding")) != null) {
			attVal = Unit_Converter.convert(attVal);
			margin += " 字:上=\"" + attVal + "\" 字:左=\"" + attVal
					+ "\" 字:右=\"" + attVal + "\" 字:下=\"" + attVal + "\"";
		}
		if((attVal = atts.getValue("fo:padding-top")) != null){
			margin += " 字:上=\"" + Unit_Converter.convert(attVal) + "\""; 
		}
		if((attVal = atts.getValue("fo:padding-left")) != null){
			margin += " 字:左=\"" + Unit_Converter.convert(attVal) + "\""; 
		}
		if((attVal = atts.getValue("fo:padding-right")) != null){
			margin += " 字:右=\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal = atts.getValue("fo:padding-bottom")) != null){
			margin += " 字:下=\"" + Unit_Converter.convert(attVal) + "\"";
		}
		
		if(margin.length() != 0){
			margin = "<字:单元格边距" + margin + "/>";
		}
		
		return margin;
	}
	
	private static String conv_vertical_align(String val){
		//"style:vertical-align"属性值：UOF中both和ODF中automatic分别在对方找不到对应
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
