package styles;

import stored_data.*;
import org.xml.sax.Attributes;

import presentation.Draw_Padding;
import convertor.Unit_Converter;

public class Graphic_Pro {
	
	private String _id = "";
	private final float _PI_ = (float)3.1415927; 
	private String _svgx = "";
	private String _svgy = "";
	
	//<预定义图形>中<属性>的子元素组
	private String _padding = "";   			//填充  
	private String _line_color = "";   			//线颜色
	private String _line_type = "";   			//线型 
	private String _line_width = "";   			//线粗细
	private String _for_arrow = "";   			//前端箭头 未处理。对应draw:marker-start
	private String _back_arrow = "";   			//后端箭头 未处理。对应draw:marker-end
	private String _width = "";   				//宽度 
	private String _height = "";   				//高度 
	private String _rotate_angle = "";   		//旋转角度
	private String _x_scale = "";   			//X-缩放比例 ？？
	private String _y_scale = "";   			//Y-缩放比例 ？？
	private String _lock_asp_ratio = "";   		//锁定纵横比  无
	private String _rel_ratio = "";   			//相对原始比例  无
	private String _print = "";   				//打印对象
	private String _web_ch = "";   				//Web文字 无
	private String _opacity = "";   			//透明度
	private String _border = "";                //文本框时线颜色 线型 线粗细的处理
	
	//把文本内容的开始元素也存在这里面
//	private String _text_begin = "";
	private String _left_margin = "";
	private String _right_margin = "";
	private String _top_margin = "";
	private String _bottom_margin = "";
	private String _hori_align = "";
	private String _vert_align = "";
	private String _writing_mode = "";
	private String _wrap = "";
	private String _fit = "";

	//
	private String _margin_string = "";			//左边距 右边距 上边距 下边距
	
	//存储锚点属性
	private String _anchor_width = "";   			//锚点宽度
	private String _anchor_height = "";   			//锚点高度
//	private String _anchor_position = "";   		//锚点位置
	private String _anchor_horirel = "";   			//锚点水平相对于
	private String _anchor_horipos = "";   			//锚点水平位置
	private String _anchor_vertrel = "";   			//锚点垂直相对于
	private String _anchor_vertpos = "";   			//锚点垂直位置
	private String _anchor_wrap = "";   			//锚点绕排
	private String _anchor_margin = "";   			//锚点边距
	private String _anchor_lock = "";   			//锚点锁定  ？？
	private String _anchor_protection = "";   		//锚点保护
	private String _anchor_allow_overlap = "";  	//允许重叠  无	
	private String _anchor_type = "";	
	private String _parent_style = "";   			//用于处理frame
	//private Para_Style _para_style = new Para_Style();
	private String _parapro = ""; 
	
	private boolean _hasstroke = false;
	private boolean _hasfill = false;
	
	public Graphic_Pro() {	
	}
	
	//SPECIAL=====for OOo2 draw:polyline======
	public void clear_padding() {
		if (_padding.contains("#99ccff"))
			_padding = "";
	}
	
	public void set_padding(String padding) {
		_padding = padding;
	}
	
	public boolean get_hasstroke() {
		return _hasstroke;
	}
	
	public boolean get_hasfill() {
		return _hasfill;
	}
	
	public void set_id(String graphicProID)
	{
		_id = graphicProID;
	}
	
	public String get_id()
	{
		return _id;
	}
	
	public void set_linecolor(String color) {
		_line_color = color;
	}
	
	public void add_lock(String string) 
	{
		_lock_asp_ratio += string;
	}
	
	public void add_rel_ratio(String string)
	{
		_rel_ratio += string;
	}
	
	public void add_web_ch(String string) 
	{
		_web_ch += string;
	}
	
	public String get_text_begin() 
	{
		return _hori_align + _vert_align + _wrap + _fit;
	}
	
	public String getLeftMargin() 
	{
		return _left_margin;
	}
	
	public String getRightMargin() 
	{
		return _right_margin;
	}
	
	public String getTopMargin() 
	{
		return _top_margin;
	}
	
	public String getBottomMargin() 
	{
		return _bottom_margin;
	}
	
	public String get_margin_string(){
		return _margin_string;
	}
	
	public String getWritingMode() 
	{
		return _writing_mode;
	}
	
	public void set_parent_style(String parent_style)
	{
		_parent_style = parent_style;
	}
	
	public String get_parent_style() 
	{
		return _parent_style;
	}
	
//	public Para_Style get_parastyle() {
//		return _para_style;
//	}
	
	public String get_parapro() {
		return _parapro;
	}
	
	public void process_graphic_atts(Attributes atts)
	{
		String value = "";
			
		//图案(hatch)填充差异太大，未考虑
		if ((value = atts.getValue("draw:fill")) != null) {
			if (!value.equals("none"))
				_hasfill = true;
			
			_padding = "<图:填充>";
			if (value.equals("solid"))
				_padding += ("<图:颜色>" + atts.getValue("draw:fill-color") + "</图:颜色>");
			else if (value.equals("bitmap")) {
				_padding += Draw_Padding.get_fill_image(atts.getValue("draw:fill-image-name"));
				if((value = atts.getValue("style:repeat")) != null){
					if(value.equals("no-repeat"))
						_padding += " 图:对齐方式=\"center\"/>";
					else if(value.equals("repeat"))
						_padding += " 图:对齐方式=\"tile\"/>";
					else if(value.equals("stretch"))
						_padding += " 图:对齐方式=\"stretch\"/>";
				}
				_padding += "/>";
			}
			else if (value.equals("gradient")) {
				_padding += Draw_Padding.get_gradient(atts.getValue("draw:fill-gradient-name"));
			}
			_padding += "</图:填充>";
		}
		else if ((value = atts.getValue("fo:background-color")) != null) {
			_padding = "<图:填充><图:颜色>" + value + "</图:颜色></图:填充>";
		}
		//====OOo2默认有填充颜色#99ccff.但是draw:polyline除外=====
		else
			_padding = "<图:填充><图:颜色>#99ccff</图:颜色></图:填充>";

		
		if ((value = atts.getValue("draw:stroke")) != null) {
			if (!value.equals("none"))
				_hasstroke = true;
			
			_line_type = "<图:线型>";
			if (value.equals("none"))
				_line_type += "none";
			else if (value.equals("solid"))
				_line_type += "single";
			else if (value.equals("dash")) {
				_line_type += "dash";   //此时本来应对应draw:stroke-dash属性引用的dash式样，但两个标准差异太大	
			}
			_line_type += "</图:线型>";
		}
	//对文本框的处理
		if ((atts.getValue("fo:border")) != null||(atts.getValue("fo:border-left")) != null||(atts.getValue("fo:border-right")) != null
				||(atts.getValue("fo:border-top")) != null||(atts.getValue("fo:border-bottom")) != null){
		      _border = get_borders(atts);
		}
		
		if ((value = atts.getValue("svg:stroke-color")) != null)
			_line_color = "<图:线颜色>" + value + "</图:线颜色>";
		//==============================================
		//========永中必须要有线颜色，否则默认为透明=======
		else if(_border.equals(""))
			_line_color = "<图:线颜色>#000000</图:线颜色>";
		//==============================================
		//==============================================
		
		if ((value = atts.getValue("svg:stroke-width")) != null)
			_line_width = "<图:线粗细>" + Unit_Converter.convert(value) + "</图:线粗细>";
		
		if ((value = atts.getValue("draw:opacity")) != null) {   //取10%的10
			value = value.substring(0,value.indexOf("%"));
			_opacity = "<图:透明度>" + value + "</图:透明度>";
		}
		if ((value = atts.getValue("style:background-transparency")) != null) {
			value = value.substring(0,value.indexOf("%"));
			_opacity = "<图:透明度>" + value + "</图:透明度>";
		}
		
		//向<文本内容..中添加某些属性。
		if ((value = atts.getValue("fo:wrap-option")) != null) {
			if (value.equals("wrap"))
				_wrap = " 图:自动换行=\"false\"";
			else
				_wrap = " 图:自动换行=\"true\"";
		}
		
/*		if ((atts.getValue("draw:auto-grow-width")) != null && atts.getValue("draw:auto-grow-width").equals("true")
				|| (atts.getValue("draw:auto-grow-height")) != null && atts.getValue("draw:auto-grow-height").equals("true"))
			_fit = " 图:大小适应文字=\"true\"";*/
		
		if((value = atts.getValue("draw:textarea-horizontal-align")) != null){
			_hori_align = " 图:水平对齐=\"" + value + "\"";
		}
		if((value = atts.getValue("draw:textarea-vertical-align")) != null){
			_vert_align = " 图:垂直对齐=\"" + value + "\"";
		}
		
		
//		==================以下处理锚点属性===================
		String margin_val = "";
		if ((value = atts.getValue("fo:margin")) != null && !value.contains("%")) {
			value = Unit_Converter.convert(value);
			margin_val += (" 字:左=\"" + value + "\" 字:右=\"" + value + "\" 字:上=\"" + value + "\" 字:下=\"" + value + "\"");
		}
		else {
			if ((value = atts.getValue("fo:margin-left")) != null && !value.contains("%"))
				margin_val += (" 字:左=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-right")) != null && !value.contains("%"))
				margin_val += (" 字:右=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-top")) != null && !value.contains("%"))
				margin_val += (" 字:上=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-bottom")) != null && !value.contains("%"))
				margin_val += (" 字:下=\"" + Unit_Converter.convert(value) + "\"");
		}
		if(margin_val.length() != 0){
			_anchor_margin = "<字:边距" + margin_val + "/>";
		}
		
		//ODF中style:protect的值有可能为none,content,size,position，对应得不大好
		if ((value = atts.getValue("style:protect")) != null) {
			if (value.equals("none"))
				_anchor_protection = "<字:保护 字:值=\"false\"/>";
			else   
				_anchor_protection = "<字:保护 字:值=\"true\"/>";
		}
		
		//位置
		if ((value = atts.getValue("style:horizontal-rel")) != null) {
			if (value.equals("char"))
				_anchor_horirel = " 字:相对于=\"char\"";
			else if (value.contains("margin"))
				_anchor_horirel = " 字:相对于=\"margin\"";
			else if (value.contains("page"))
				_anchor_horirel = " 字:相对于=\"page\"";		
			//UOF中的column没有对应,ODF中含有paragraph而不含margin的没有对应
			
			//default?
			else
				_anchor_horirel = " 字:相对于=\"column\"";	
		}

		if ((value = atts.getValue("style:horizontal-pos")) != null && 
				(value.equals("left") || value.equals("right") || value.equals("center"))) {
					_anchor_horipos = "<字:相对 uof:locID=\"t0178\" uof:attrList=\"值\" 字:值=\"" + value + "\"/>"; 
			//ODF中某些值无对应
		}
		
		if ((value = atts.getValue("style:vertical-rel")) != null) {
			if (value.equals("line"))
				_anchor_vertrel = " 字:相对于=\"line\"";
			else if (value.contains("paragraph"))
				_anchor_vertrel = " 字:相对于=\"paragraph\"";
			//======永中取paragraph才能显示正常======
			else if (value.contains("page"))
				//_anchor_vertrel = " 字:相对于=\"page\"";
				_anchor_vertrel = " 字:相对于=\"paragraph\"";
			//======================================
			
			//UOF中的margin没有对应,ODF中frame、frame-content、char、baseline、text没有对应
			
			
			//default?
			else
				_anchor_vertrel = " 字:相对于=\"paragraph\"";
		}

		if ((value = atts.getValue("style:vertical-pos")) != null) {
			if (value.equals("middle"))
				_anchor_vertpos = "<字:相对 uof:locID=\"t0181\" uof:attrList=\"值\" 字:值=\"center\"/>";
			else if (value.equals("top") || value.equals("bottom"))
				_anchor_vertpos = "<字:相对 uof:locID=\"t0181\" uof:attrList=\"值\" 字:值=\"" + value + "\"/>";
			//ODF中某些值无对应
		}
		
		//绕排
		if ((value = atts.getValue("style:wrap")) != null) {
			_anchor_wrap = "<字:绕排";
			if (value.equals("run-through")) {
				if(atts.getValue("style:run-through") != null && atts.getValue("style:run-through").equals("background"))
					_anchor_wrap += " 字:绕排方式=\"behindtext\" 字:环绕文字=\"both\"";
				else
					_anchor_wrap += " 字:绕排方式=\"infrontoftext\" 字:环绕文字=\"both\"";
			}
			else if (value.equals("left"))
				_anchor_wrap += " 字:绕排方式=\"square\" 字:环绕文字=\"left\"";
			else if (value.equals("right"))
				_anchor_wrap += " 字:绕排方式=\"square\" 字:环绕文字=\"right\"";
			else if (value.equals("parallel"))
				_anchor_wrap += " 字:绕排方式=\"square\" 字:环绕文字=\"both\"";
			else if (value.equals("biggest"))
				_anchor_wrap += " 字:环绕文字=\"largest\"";
			else if (value.equals("none")) {
				_anchor_type = "inline";
				_anchor_wrap += " 字:环绕文字=\"both\"";
			}
			_anchor_wrap += "/>";
		}
		else {
			_anchor_wrap = "<字:绕排 字:绕排方式=\"square\" 字:环绕文字=\"both\"/>";
		}
		
		//锁定。是否正确？？
		if ((value = atts.getValue("style:flow-with-text")) != null) {
			_anchor_lock = "<字:锁定 字:值=\"" + value + "\"/>";		
		}
		
		//used in Anno_In_Cell
		if((value=atts.getValue("fo:padding-left")) != null){
			_margin_string += " 图:左边距=\"" + Unit_Converter.convert(value) + "\"";
		}
		if((value=atts.getValue("fo:padding-right")) != null){
			_margin_string += " 图:右边距=\"" + Unit_Converter.convert(value) + "\"";
		}
		if((value=atts.getValue("fo:padding-top")) != null){
			_margin_string += " 图:上边距=\"" + Unit_Converter.convert(value) + "\"";
		}
		if((value=atts.getValue("fo:padding-bottom")) != null){
			_margin_string += " 图:下边距=\"" + Unit_Converter.convert(value) + "\"";
		}
		
		if((value = atts.getValue("style:writing-mode")) != null) {
			if(value.equals("lr-tb"))
				_writing_mode = " 图:文字排列方向=\"hori-l2r\"";
			else if(value.equals("rl-tb"))
				_writing_mode = " 图:文字排列方向=\"hori-r2l\"";
			else if(value.equals("tb-rl"))
				_writing_mode = " 图:文字排列方向=\"vert-l2r\"";
			else if(value.equals("tb-lr"))
				_writing_mode = " 图:文字排列方向=\"vert-r2l\"";
		}
	}
	
	public void process_draw_atts(Attributes atts)
	{
		String value = "";
		
		if ((value = atts.getValue("svg:x")) != null) {
			_svgx = value;
		}
		else if (atts.getValue("svg:x1") != null && atts.getValue("svg:x2") != null) {   //Line的情况
			float x1 = Unit_Converter.convert_gra(atts.getValue("svg:x1"));
			float x2 = Unit_Converter.convert_gra(atts.getValue("svg:x2"));
			if (x1 >= x2) {
				_svgx = atts.getValue("svg:x2");
				_width = "<图:宽度>" + (x1 - x2) + "</图:宽度>";
				_anchor_width = "<字:宽度 uof:locID=\"t0112\">" + (x1 - x2) + "</字:宽度>";
			}
			else {
				_svgx = atts.getValue("svg:x1");
				_width = "<图:宽度>" + (x2 - x1) + "</图:宽度>";
				_anchor_width = "<字:宽度 uof:locID=\"t0112\">" + (x2 - x1) + "</字:宽度>";
			}
		}
		
		if ((value = atts.getValue("svg:y")) != null) {
			_svgy = value;
		}
		else if (atts.getValue("svg:y1") != null && atts.getValue("svg:y2") != null) {   //Line的情况
			float y1 = Unit_Converter.convert_gra(atts.getValue("svg:y1"));
			float y2 = Unit_Converter.convert_gra(atts.getValue("svg:y2"));
			if (y1 >= y2) {
				_svgy = atts.getValue("svg:y2");
				_height = "<图:高度>" + (y1 - y2) + "</图:高度>";
				_anchor_height = "<字:高度 uof:locID=\"t0113\">" + (y1 - y2) + "</字:高度>";
			}
			else {
				_svgy = atts.getValue("svg:y1");
				_height = "<图:高度>" + (y2 - y1) + "</图:高度>";
				_anchor_height = "<字:高度 uof:locID=\"t0113\">" + (y2 - y1) + "</字:高度>";
			}
		}
		
		if (_svgx.length() != 0) {  	
			_anchor_horipos = "<字:绝对 uof:locID=\"t0177\" uof:attrList=\"值\" 字:值=\"" + Unit_Converter.convert_gra(_svgx) + "\"/>";
		}

		if (_svgy.length() != 0) {
			_anchor_vertpos = "<字:绝对 uof:locID=\"t0180\" uof:attrList=\"值\" 字:值=\"" + Unit_Converter.convert_gra(_svgy) + "\"/>";
		}
		
//		double widthValue = 0,heightValue = 0;
//		int index = 0;
//		String ratioString = "";
		if ((value = atts.getValue("svg:width")) != null) {
			_width = "<图:宽度>" + Unit_Converter.convert_gra(value) + "</图:宽度>";
			_anchor_width = "<字:宽度 uof:locID=\"t0112\">" + Unit_Converter.convert_gra(value) + "</字:宽度>";
//			index = value.indexOf("cm");
//			widthValue = Double.parseDouble(value.substring(0,index));
		}
		else if ((value = atts.getValue("fo:min-width")) != null) {
			_width = "<图:宽度>" + Unit_Converter.convert_gra(value) + "</图:宽度>";
			_anchor_width = "<字:宽度 uof:locID=\"t0112\">" + Unit_Converter.convert_gra(value) + "</字:宽度>";
		}
		if ((value = atts.getValue("svg:height")) != null) {
			_height = "<图:高度>" + Unit_Converter.convert_gra(value) + "</图:高度>";
			_anchor_height = "<字:高度 uof:locID=\"t0113\">" + Unit_Converter.convert_gra(value) + "</字:高度>";
//			index = value.indexOf("cm");
//			heightValue = Double.parseDouble(value.substring(0,index));
//			double ratio = widthValue/heightValue;
//			ratioString = String.valueOf(ratio);
//			_rel_ratio = ("<图:相对原始比例>" + ratioString + "</图:相对原始比例>");
		}
		if ((value = atts.getValue("draw:layer")) != null) {
			if (Style_Data.get_draw_layer(value).get_display().equals("printer"))
				_print = ("<图:打印对象>true</图:打印对象>");
//			else
//				_print = ("<图:打印对象>false</图:打印对象>");
		}
		if ((value = atts.getValue("draw:transform")) != null) {
			//rotate(<rotate-angle>)  两个标准的旋转角度对应见docs包下的"新标准处理....txt"
			if (value.contains("rotate")) {
				int index1 = value.indexOf("rotate");
				String substring1 = value.substring(index1);
				int index2 = substring1.indexOf("<");
				int index3 = substring1.indexOf(">");
				double rotAngle = Double.valueOf(substring1.substring(index2 + 1,index3))*180/_PI_;
				if (rotAngle > 0)
					rotAngle = 360 - rotAngle;
				else if (rotAngle < 0)
					rotAngle = -rotAngle;
				_rotate_angle = ("<图:旋转角度>" + rotAngle + "</图:旋转角度>");
			}
			//scale(<sx> [<sy>])   缩放比例应该是怎样取值？
			if (value.contains("scale")) {
				int index1 = value.indexOf("(");
				int index2 = value.indexOf(")");
				String substring = value.substring(index1 + 1,index2);
				if (substring.contains(" ")) {
					int index3 = substring.indexOf("<");
					int index4 = substring.indexOf(">");
					String XScale = substring.substring(index3 + 1,index4);
					_x_scale = ("<图:X-缩放比例>" + XScale + "</图:X-缩放比例>");
					String substring2 = substring.substring(index4 + 1);
					int index5 = substring2.indexOf("<");
					int index6 = substring2.indexOf(">");
					String YScale = substring.substring(index5 + 1,index6);
					_y_scale = ("<图:Y-缩放比例>" + YScale + "</图:Y-缩放比例>");
				}
				else {
					int index3 = substring.indexOf("<");
					int index4 = substring.indexOf(">");
					String XYScale = substring.substring(index3 + 1,index4);
					_x_scale = ("<图:X-缩放比例>" + XYScale + "</图:X-缩放比例>");
					_y_scale = ("<图:Y-缩放比例>" + XYScale + "</图:Y-缩放比例>");
				}
			}
		}
		
		if ((value = atts.getValue("style:print-content")) != null) {
			_print = "<图:打印对象>" + value + "</图:打印对象>";
		}
	}
	
	public void process_para_atts(Attributes atts)
	{
		_parapro += Para_Style.process_para_atts(atts);
		
		String value = "";
		
		if((value = atts.getValue("fo:margin")) != null) {
			value = Unit_Converter.convert(value);
			_left_margin = " 图:左边距=\"" + value + "\"";
			_right_margin = " 图:右边距=\"" + value + "\"";
			_top_margin = " 图:上边距=\"" + value + "\"";
			_bottom_margin = " 图:下边距=\"" + value + "\"";
		}
		else {
			if ((value = atts.getValue("fo:margin-left")) != null)
				_left_margin = (" 图:左边距=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-right")) != null)
				_right_margin = (" 图:右边距=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-top")) != null)
				_top_margin = (" 图:上边距=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-bottom")) != null)
				_bottom_margin = (" 图:下边距=\"" + Unit_Converter.convert(value) + "\"");
		}
		
		if((value = atts.getValue("style:writing-mode")) != null) {
			if(value.equals("lr-tb"))
				_writing_mode = " 图:文字排列方向=\"hori-l2r\"";
			else if(value.equals("rl-tb"))
				_writing_mode = " 图:文字排列方向=\"hori-r2l\"";
			else if(value.equals("tb-rl"))
				_writing_mode = " 图:文字排列方向=\"vert-l2r\"";
			else if(value.equals("tb-lr"))
				_writing_mode = " 图:文字排列方向=\"vert-r2l\"";
		}
	}
	
	//处理边框：从"0.002cm solid #000000"形式的字符串提取出各种值 
	public static String tranBorderValue(String border)
	{
		int index1 = border.indexOf(' ');
		int index2 = border.lastIndexOf(' ');
		String str = "";
		if(border.equals("none")){
			str = "";
		}
		else if(border.substring(index1+1,index2).equals("solid")){////single" + border.substring(index1+1,index2) +
			str = "<图:线粗细>" + Unit_Converter.convert(border.substring(0,index1)) +"</图:线粗细>" +  
			"<图:线型 uof:locID=\"g0014\">single</图:线型>" +
			"<图:线颜色>"+ border.substring(index2+1) +"</图:线颜色>";
		}
		else if(border.substring(index1+1,index2).equals("double")){////single" + border.substring(index1+1,index2) +
			str = "<图:线粗细>" + Unit_Converter.convert(border.substring(0,index1)) +"</图:线粗细>" +  
			"<图:线型 uof:locID=\"g0014\">double</图:线型>" +
			"<图:线颜色>"+ border.substring(index2+1) +"</图:线颜色>";
		}
		
		return str;	
	}
	
	//处理 边框 的转换
	protected static String get_borders(Attributes atts){
		String border = "";
		String value = "";
		if((value = atts.getValue("fo:border")) != null) { 
			border +=  tranBorderValue(value);
		}
		else{
			String tempBorder = "";
			if((value = atts.getValue("fo:border-left")) != null){
				tempBorder = value;
			}
			if((value = atts.getValue("fo:border-top")) != null){
				tempBorder = value;
			}
			if((value = atts.getValue("fo:border-right")) != null){
				tempBorder = value;
			}
			if((value = atts.getValue("fo:border-bottom")) != null){
				tempBorder = value;
			}
			border +=  tranBorderValue(tempBorder);
		}
		return border;
	}
	public void process_sent_atts(Attributes atts)
	{	
		_parapro += "<字:句属性>" + Sent_Style.process_text_atts(atts) + "</字:句属性>";
	}
	
	public String get_anchor_atts()
	{		
		String str = _anchor_width + _anchor_height + "<字:位置 uof:locID=\"t0114\"><字:水平" + _anchor_horirel + ">" 
		+ _anchor_horipos + "</字:水平><字:垂直" + _anchor_vertrel + ">" + _anchor_vertpos + "</字:垂直>" + "</字:位置>"
		+ _anchor_wrap + _anchor_margin + _anchor_lock
		+ _anchor_protection + _anchor_allow_overlap;
		
		return str;
	}
	
	public String get_drawing_pro_string() 
	{
		String str = _padding + _line_color + _line_type + _line_width  + _border + _for_arrow + _back_arrow
		+ _width + _height + _rotate_angle + _x_scale + _y_scale + _lock_asp_ratio 
		+ _rel_ratio + _print + _web_ch + _opacity;
		
		return str;
	}
	
	public String get_anchortype() {
		return _anchor_type;
	}
}
