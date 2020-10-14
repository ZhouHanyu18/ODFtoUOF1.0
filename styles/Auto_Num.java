package styles;

import org.xml.sax.Attributes;
import stored_data.Common_Data;
import stored_data.Style_Data;
import tables.Numformat_Table;
import convertor.Unit_Converter;
import graphic_content.Media_Obj;

/** 
 * 处理<text:list-style>和<text:outline-style> 到 <uof:自动编号>的转换。
 *  
 * @author xie
 *
 */
public class Auto_Num {
	//the result
	private static String _result = "";
	//
	private static String _list_name = "";
	//
	private static String _levels = "";
	//
	private static String _font = "";
	//
	private static String _ele_atts = "";				//<级别>的属性
	private static String _symbol = "";   				//项目符号
	private static String _symbol_font = "";   			//符号字体
	private static String _link_ref = "";   			//链接式样引用(无对应)
	private static String _num_format = "";   			//编号格式														
	private static String _num_format_render = "";   	//编号格式表示
	private static String _image_ref = "";   			//图片符号引用														
	private static String _indent = "";   				//缩进 
	private static String _tab_position = "";   		//制表符位置														
	private static String _start_num = "";   			//起始编号
	private static String _regular_format = "";   		//正规格式
	private static int _level = 0;
	
	private static void clear(){
		_font = "";
		_ele_atts = "";
		_symbol = "";   
		_symbol_font = "";   
		_link_ref = "";   
		_num_format = "";  
		_num_format_render = "";  
		_image_ref = ""; 
		_indent = "";  
		_tab_position = "";  
		_start_num = ""; 
		_regular_format = ""; 
		_level = 0;
	}
	
	private static String get_one_level(){
		String oneLevel = "";
		
		if(_symbol_font.equals("")){
			_symbol_font += "<字:符号字体>";
		}
		_symbol_font += _font + "</字:符号字体>";
		
		oneLevel ="<字:级别" + _ele_atts + ">";
		oneLevel += _symbol + _symbol_font + _link_ref 
			  + _num_format + _num_format_render
			  + _image_ref + _indent + _tab_position 
			  + _start_num + _regular_format;
		oneLevel += "</字:级别>";

		//The level range in UOF is 0-8, so
		//level 9 is just dropped.
		if(_level == 9){
			oneLevel = "";
		}
		clear();		
		return oneLevel;
	}
	
	//return <自动编号集>
	public static String get_result(){
		String rst = "";
		
		rst = "<uof:自动编号集>";
		if(Common_Data.get_file_type().equals("presentation")){
			rst += add_present_list_style();
		}
		rst += _result;
		rst += "</uof:自动编号集>";

		_result = "";
		return rst;
	}
	
	//set list name
	public static void set_list_name(String name){
		_list_name = name;
	}
	
	private static String skip_null(String val){
		return (val==null) ? "" : val;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("text:list-style")){
			if((attVal=atts.getValue("style:name")) != null){
				attVal = Style_Data.rename(attVal);
				_list_name = attVal;
			}
		}
		
		else if(qName.equals("text:outline-style")){
			_list_name = "outline";
		}
		
		else if (qName.equals("style:text-properties")) {
			//Here the element's only attribute is
			//style:font-name, and the default font-size
			//is 9pt
			attVal = atts.getValue("style:font-name");
			if(attVal == null){
				attVal = atts.getValue("fo:font-family");
				if(attVal != null){
					Font_Face.add_font(attVal,attVal);
				}
			}
			if(attVal != null){				
				_font = "<字:字体 字:字号=\"9\" 字:西文字体引用=\"" + attVal + "\"/>";
			}
		}
		
		else if(qName.equals("text:list-level-style-image")){
			attVal = atts.getValue("text:level");
			_level = Integer.valueOf(attVal) - 1;			
			_ele_atts += " 字:级别值=\"" + _level + "\"";
			
			attVal = atts.getValue("text:style-name");
			if (attVal != null) {
				_symbol_font = "<字:符号字体 字:式样引用=\"" + attVal + "\">";
			}
			
			if ((attVal = atts.getValue("xlink:href")) != null) {
				String objID = "";

				objID = Media_Obj.process_href(attVal);
				_image_ref = "<字:图片符号引用>" + objID + "</字:图片符号引用>";
			}
		}
		else if((qName.equals("text:list-level-style-number")
				||qName.equals("text:list-level-style-bullet")
				||qName.equals("text:outline-level-style"))){		

			attVal = atts.getValue("text:level");
			_level = Integer.valueOf(attVal) - 1;			
			_ele_atts += " 字:级别值=\"" + _level + "\"";
			
			attVal = atts.getValue("text:style-name");
			if (attVal != null) {
				_symbol_font = "<字:符号字体 字:式样引用=" + "\"" + attVal + "\">";
			}
			
			String suffix = skip_null(atts.getValue("style:num-suffix"));
			String prefix = skip_null(atts.getValue("style:num-prefix"));
			String displays = skip_null(atts.getValue("text:display-levels"));
			
			_num_format_render = "<字:编号格式表示>";
			_num_format_render += prefix + get_render_levels(displays,_level) + suffix;
			_num_format_render += "</字:编号格式表示>";
			
			attVal=atts.getValue("text:bullet-char");
			if (attVal != null) {
				_symbol = "<字:项目符号>" + attVal + "</字:项目符号>";
			}
			
			attVal = skip_null(atts.getValue("style:num-format"));
			if (!attVal.equals("")) {			
				_num_format = "<字:编号格式>" + Numformat_Table.get_name(attVal)+ "</字:编号格式>";
			}
			
			//字:起始编号
			attVal = atts.getValue("text:start-value");
			if (attVal != null) {
				_start_num += "<字:起始编号>" + attVal + "</字:起始编号>"; 
			}
		}
		
		else if (qName.equals("style:list-level-properties")){
			//@字:编号对齐方式
			attVal=atts.getValue("fo:text-align");
			if (attVal != null) {
				_ele_atts += " 字:编号对齐方式=\"" + conv_align_val(attVal) + "\"";
			}else {
				_ele_atts += " 字:编号对齐方式=\"left\"";
			}
			
			//缩进/制表符位置
			attVal = atts.getValue("text:space-before");
			attVal = (attVal==null||attVal.equals("")) ? "0" : Unit_Converter.convert(attVal);
			float space = Float.parseFloat(attVal);
			
			attVal = atts.getValue("text:min-label-width");
			attVal = (attVal==null||attVal.equals("")) ? "0" : Unit_Converter.convert(attVal);
			
			attVal = atts.getValue("text:min-label-distance");
			attVal = (attVal==null||attVal.equals("")) ? "0" : Unit_Converter.convert(attVal);
			float labelDis = Float.parseFloat(attVal);
			
			_indent = "<字:缩进 uof:locID=\"t0165\">";
			_indent += "<字:左><字:绝对 uof:locID=\"t0185\" uof:attrList=\"值\""
				+ " 字:值=\"" + space + "\"/></字:左>";
			//The default value is -21.0pt
			_indent += "<字:首行><字:绝对 uof:locID=\"t0189\" uof:attrList=\"值\""
				+ " 字:值=\"-21.0\"/></字:首行>";
			_indent += "</字:缩进>";
			
			_tab_position  = "<字:制表符位置>";
			_tab_position += labelDis;
			//_tab_position  += (space + labelWid + labelDis);
			_tab_position  += "</字:制表符位置>";
		}
	}
	
	public static void process_end(String qName){
		if(qName.contains("text:list-level-style")
			||qName.equals("text:outline-level-style")){
			
			_levels += get_one_level();
		}
		
		else if(qName.equals("text:list-style")
				||qName.equals("text:outline-style")){
			String style = "";
			
			style = "<字:自动编号";
			style += " 字:标识符=\"" + _list_name + "\"";
			style += " 字:名称=\"" + _list_name + "\">";
			style += _levels;
			style += "</字:自动编号>";
			
			//list name connot be empty
			if(!_list_name.equals("")){
				_result += style;
			}
			
			_levels = "";
			_list_name = "";
		}
	}
	
	private static String get_render_levels(String displays, int level){
		String rls = "";
		int dis = 1;
		
		if(!displays.equals("")){
			dis = Integer.parseInt(displays);
		}
		
		for(int i = level+1; dis > 0; dis--,i--){
			rls = "%" + i + "." + rls;
		}
		rls = rls.substring(0,rls.length()-1);
		
		return rls;
	}
	
	private static String conv_align_val(String val){
		String convVal = "left";
		
		if (val.equals("left")){
			convVal = "left";
		}
		else if(val.equals("start")){
			convVal = "left";
		}
		else if (val.equals("right")){
			convVal = "right";
		}
		else if(val.equals("end")){
			convVal = "right";
		}
		else if (val.equals("center")){
			convVal = "center";
		}
		else if (val.equals("justify")){
			convVal = "justify";
		}
		
		return convVal;
	}
	
	private static String add_present_list_style(){
		String presenls = "";
		
		presenls = 
			"<字:自动编号 字:标识符=\"bn0\" 字:名称=\"bn0\" 字:多级编号=\"false\">"
				+ "<字:级别 字:级别值=\"0\" 字:编号对齐方式=\"left\" 字:尾随字符=\"none\">"
				+	"<字:项目符号>–</字:项目符号>"
				+	"<字:符号字体>"
				+ "<字:字体 字:字号=\"13\" 字:西文字体引用=\"StarSymbol\"/>"
				//+		"<字:字体 字:西文字体引用=\"font_2\" 字:中文字体引用=\"font_2\"/>"
				+	"</字:符号字体>"
				+	"<字:制表符位置>0.0</字:制表符位置>"
				+	"<字:起始编号>1</字:起始编号>"
				+"</字:级别>"
			+"</字:自动编号>"
			+"<字:自动编号 字:标识符=\"bn1\" 字:名称=\"bn1\" 字:多级编号=\"false\">"
				+"<字:级别 字:级别值=\"0\" 字:编号对齐方式=\"left\" 字:尾随字符=\"none\">"
				+	"<字:项目符号>●</字:项目符号>"
				+	"<字:符号字体>"
				+ "<字:字体 字:字号=\"13\" 字:西文字体引用=\"StarSymbol\"/>"
				//+		"<字:字体 字:西文字体引用=\"font_1\" 字:中文字体引用=\"font_1\"/>"
				+	"</字:符号字体>"
				+	"<字:制表符位置>0.0</字:制表符位置>"
				+	"<字:起始编号>1</字:起始编号>"
				+"</字:级别>"
			+"</字:自动编号>";
		
		return presenls;
	}
}
