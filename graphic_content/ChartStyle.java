package graphic_content;

import org.xml.sax.Attributes;

import styles.*;
import convertor.Common_Pro;
import convertor.Unit_Converter;

public class ChartStyle {

	private String _id = "";
	private String _font = "";   				//<表:字体>，对应text-properties
	private String _border = "";   		//<表:边框>，在paragraph-properties中提取？
	private String _padding = "";   		//<表:填充>，无对应，UOF定义是否有误？
	
	//<表:对齐>，用于标题集
	private String _hori_align = "";
	private String _vert_align = "";
	private String _indent = "";
	private String _direction = "";
	private String _rotation_angle = "";
	private String _auto_newline = "";
	private String _shrink_to_fit = "";   		//无对应
	
	//<对齐>，用于坐标轴
	private String _axis_word_direction = "";
	private String _axis_word_rot_angle = "";
	private String _offset = "";   				//无对应
	
	private String _axis_line_type = "";   		//<表:线型>，用于坐标轴，在graphic-properties中提取。网格线也可利用线型的内容
	private String _axis_data_style = "";   //<表:数值>，用于坐标轴
	
	//坐标轴属性,“刻度线标志”属性未找到对应
	private String _major_tick_type = "";
	private String _minor_tick_type = "";
	
	//<刻度>
	//数值轴部分
	private String _y_min_value = "";   		//数值轴最小值
	private String _y_max_value = "";   		//数值轴最大值
	private String _y_major_internal = "";   	//数值轴主单位
	private String _y_minor_internal = "";   	//数值轴次单位
	private String _x_cross_point = "";   		//数值轴的分类交叉点
	private String _y_unit = "";   				//数值轴单位
	private String _display_unit = "";   		//显示单位
	private String _y_logarithm = "";   		//数值轴，对数
	private String _y_sequence_rev = "";   		//数值次序反转
	//分类轴部分
	private String _y_cross_point = "";   		//分类轴的数值交叉点
	private String _x_lable_num = "";   		//分类标签数
	private String _x_mark_num = "";   			//分类刻度数
	private String _x_sequence_rev = "";   		//分类次序反转
	
	private String _display_symbol = ""; //<显示标志>，用于数据系列和数据点
	private String _serie_name = "";   			//<系列名>，用于数据系列和数据点，未找到对应
	
	public ChartStyle() {		
	}
	
	public void set_id(String string) 
	{
		_id = string;
	}
	
	public String get_id()
	{
		return _id;
	}
	
	public void process_chart_pro(Attributes atts) 
	{
		String value = "";
		
		if((value = atts.getValue("chart:lines")) != null && value.equals("true")) {
			Chart.set_lines(true);
		}
		
		if((value = atts.getValue("chart:symbol-type")) != null && !value.equals("none")) {
			Chart.set_symbol(true);
		}
		
		if((value = atts.getValue("chart:pie-offset")) != null) {
			Chart.set_pieOffset(true);
		}
		
		if((value = atts.getValue("chart:three-dimensional")) != null && value.equals("true")) {
			Chart.set_3D(true);
		}
		
		if((value = atts.getValue("chart:vertical")) != null && value.equals("true")) {
			Chart.set_vertical(true);
		}
		
		if((value = atts.getValue("chart:stacked")) != null && value.equals("true")) {
			Chart.set_stacked(true);
		}
		if((value = atts.getValue("chart:percentage")) != null && value.equals("true")) {
			Chart.set_percentage(true);
		}
		
		if((value = atts.getValue("chart:display-label")) != null) {
			_display_unit = "<表:显示单位 表:值=\"" + value + "\"/>";
			System.out.print("...\n\n");
		}
		
		if((value = atts.getValue("chart:series-source")) != null) {
			if (value.equals("columns")) {
				Chart.add_data_source(" 表:系列产生=\"col\"");
				Chart_Local_Table.set_series_type("col");
			}
			else if (value.equals("rows")) {
				Chart.add_data_source(" 表:系列产生=\"row\"");
				Chart_Local_Table.set_series_type("row");
			}
		}
		
		if ((value = atts.getValue("style:direction")) != null) {
			if (value.equals("ltr")) {
				_direction += "<表:文字方向>horizontal</表:文字方向>";
				_axis_word_direction += "<表:文字方向>horizontal</表:文字方向>";
			}
			else if (value.equals("ttb")) {
				_direction += "<表:文字方向>vertical</表:文字方向>";
				_axis_word_direction += "<表:文字方向>vertical</表:文字方向>";
			}
		}
		
		if ((value = atts.getValue("chart:link-data-style-to-source")) != null) {
			_axis_data_style += " 表:链接到源=\"" + value + "\"";
		}
		
		if ((value = atts.getValue("chart:tick-marks-major-inner")) != null && value.equals("true")) {
			_major_tick_type = "inside";
		}
		if ((value = atts.getValue("chart:tick-marks-major-outer")) != null && value.equals("true")) {
			_major_tick_type = "outside";
		}
		if ((value = atts.getValue("chart:tick-marks-minor-inner")) != null && value.equals("true")) {
			_minor_tick_type = "inside";
		}
		if ((value = atts.getValue("chart:tick-marks-minor-outer")) != null && value.equals("true")) {
			_minor_tick_type = "outside";
		}
		
		if ((value = atts.getValue("chart:axis-logarithmic")) != null) {
			if (value.equals("true"))
				_y_logarithm = "<表:对数 表:值=\"true\">";
			else
				_y_logarithm = "<表:对数 表:值=\"false\">";
		}
		
		if ((value = atts.getValue("chart:minimum")) != null)
			_y_min_value = "<表:最小值>" + value + "</表:最小值>";
		if ((value = atts.getValue("chart:maximum")) != null)
			_y_max_value = "<表:最大值>" + value + "</表:最大值>";
		
		if ((value = atts.getValue("chart:origin")) != null)
			_y_cross_point = "<表:数值交叉点>" + value + "</表:数值交叉点>";
		
		if ((value = atts.getValue("chart:interval-major")) != null) {
			double major = Double.valueOf(value);
			_y_major_internal = "<表:主单位>" + major + "</表:主单位>";
			if ((value = atts.getValue("chart:interval-minor-divisor")) != null) {
				int divisor = Integer.valueOf(value);
				double minor = major/divisor;
				_y_minor_internal = "<表:次单位>" + minor + "</表:次单位>";
			}
		}
		
		if ((value = atts.getValue("chart:data-label-number")) != null) {
			if (value.equals("value"))
				_display_symbol += " 表:数值=\"true\"";
			else if (value.equals("percentage"))
				_display_symbol += " 表:百分数=\"true\"";
			else if (value.equals("none"))
				_display_symbol += " 表:数值=\"false\"";
		}
		
		if ((value = atts.getValue("chart:data-label-text")) != null && value.equals("true"))
			_display_symbol += " 表:系列名=\"true\"";
	
		if ((value = atts.getValue("chart:data-label-symbol")) != null && value.equals("true"))
			_display_symbol += " 表:图例标志=\"true\"";
	}
	
	public void process_graphic_pro(Attributes atts) 
	{
		String value = "";
		
		if ((value = atts.getValue("fo:wrap-option")) != null) {
			if (value.equals("wrap"))
				_auto_newline += "<表:自动换行 值=\"true\"/>";
			else
				_auto_newline += "<表:自动换行 值=\"true\"/>";
		}
		
		//<表:线型>,边距和阴影属性未找到对应
		if ((value = atts.getValue("draw:stroke")) != null) {
			if (value.equals("solid"))
				_axis_line_type += " 表:类型=\"single\"";
			else if (value.equals("dash"))
				_axis_line_type += " 表:类型=\"dash\"";
			else
				_axis_line_type += " 表:类型=\"none\"";
		}
		else _axis_line_type += " 表:类型=\"none\"";   //类型属性是required的
		if ((value = atts.getValue("svg:stroke-width")) != null)
			_axis_line_type += " 表:宽度=\"" + Unit_Converter.convert_gra(value) + "\"";
		if ((value = atts.getValue("svg:stroke-color")) != null)
			_axis_line_type += " 表:颜色=\"" + value + "\"";	
		
		if ((value = atts.getValue("draw:fill-color")) != null)
//			_padding = "<图:颜色>" + value + "</图:颜色>";
			_padding = "<图:颜色>auto</图:颜色>";
	}
	
	public void process_para_pro(Attributes atts)
	{
		String value = "";
		
		//边框
		if ((value = atts.getValue("fo:border")) != null) {
			_border += Common_Pro.tranBorderValue(value);
		}
		
		//对齐
		if ((value = atts.getValue("fo:text-align")) != null) {
			_hori_align += "<表:水平对齐方式>";
			if (value.equals("left"))
				_hori_align += "left";
			else if (value.equals("right"))
				_hori_align += "right";
			else if (value.equals("center"))
				_hori_align += "center";
			else if (value.equals("justify"))
				_hori_align += "justified";
			/*start和end没有对应
			 else if (value.equals("start"))
			 align += "left";
			 else if (value.equals("end"))
			 align += "right";
			 */
			_hori_align += "</表:水平对齐方式>";
		}
		if ((value = atts.getValue("style:vertical-align")) != null) {
			_vert_align += "<表:垂直对齐方式>";
			if (value.equals("top"))
				_vert_align += "top";
			else if (value.equals("middle"))
				_vert_align += "center";
			else if (value.equals("bottom"))
				_vert_align += "bottom";
			/*auto没有对应
			 else if (value.equals("auto"))
			 align += "";
			 */
			_vert_align += "</表:垂直对齐方式>";
		}
		//To do.此处需要判断缩进是取length而不是percent
		if ((value = atts.getValue("fo:text-indent")) != null) {
			_indent += "<表:缩进>" + value + "</表:缩进>";
		}
	}
	
	public void process_text_pro(Attributes atts)
	{
		String value = "";
		
		_font = "<表:字体>" + Sent_Style.process_text_atts(atts) + "</表:字体>";
				
		//需要改进。ODF的文字旋转角度是个非负整数，UOF的则是-90到90之间。
		if ((value = atts.getValue("style:rotation-angle")) != null) {
			_rotation_angle += "<表:文字旋转角度>" + value + "</表:文字旋转角度>";
			_axis_word_rot_angle += "<表:旋转角度>" + value + "</表:旋转角度>";
		}
		
	}
	
	private String get_border() 
	{
		String border = "";
		if (_border.length() != 0)
			border = "<表:边框" + _border + "/>";
		return border;
	}
	
	private String get_padding() 
	{
		String padding = "";
		if (_padding.length() != 0)
			padding = "<表:填充>" + _padding + "</表:填充>";
		else if (Chart.get_type().equals("chart:line"))
			padding = "<表:填充><图:颜色>auto</图:颜色></表:填充>";
		return padding;
	}

	private String get_title_align() 
	{
		String titleAlign = _hori_align + _vert_align + _indent + _direction + _rotation_angle
		+ _auto_newline + _shrink_to_fit;
		if (titleAlign.length() != 0)
			titleAlign = "<表:对齐 uof:locID=\"s0020\">" + titleAlign + "</表:对齐>";
		return titleAlign;
	}
	
	public String get_major_tick_type() 
	{
		if (_major_tick_type.length() == 0)
			_major_tick_type = "inside";
		return 	_major_tick_type;
	}
	
	public String get_minor_tick_type() 
	{
		if (_minor_tick_type.length() == 0)
			_minor_tick_type = "none";
		return 	_minor_tick_type;
	}
	
	public void add_axis_datastyle(String string) 
	{
		_axis_data_style += string;
	}
	
	public String get_chart_area()
	{
		String border = get_border();
		if (border.length() == 0) {
			border = "<表:边框 uof:locID=\"s0057\" uof:attrList=\"类型 宽度 边距 颜色 阴影\"" +
					" uof:类型=\"none\"/>";
		}
		String padding = get_padding();
		return "<表:图表区>" + border + padding + _font + "</表:图表区>";
	}
	
	public String get_plot_area() 
	{
		String border = get_border();
		if (border.length() == 0) {
			border = "<表:边框 uof:locID=\"s0057\" uof:attrList=\"类型 宽度 边距 颜色 阴影\"" +
					" uof:类型=\"none\"/>";
		}
		String padding = get_padding();
		return "<表:绘图区>" + border + padding + "</表:绘图区>";
	}
	
	public String get_legend()
	{
		String border = get_border();
		String padding = get_padding();
		return border + padding + _font;
	}
	
	public String get_title() 
	{
		String border = get_border();
		String padding = get_padding();
		String titleAlign = get_title_align();
		return border + padding + _font + titleAlign;
	}
	
	public String get_x_axis() 
	{
		if (!_axis_data_style.contains("表:链接到源"))
			_axis_data_style += " 表:链接到源=\"true\"";
		
		return "<表:线型" + _axis_line_type + "/><表:数值" + _axis_data_style + "/>" + _font + 
		"<表:刻度>" + _display_unit + _x_lable_num + _x_mark_num + _x_sequence_rev + "</表:刻度>"
		+ "<表:对齐 uof:locID=\"s0078\">" + _axis_word_direction + _axis_word_rot_angle + _offset + "</表:对齐>";
	}
	
	public String get_y_axis() 
	{
		if (!_axis_data_style.contains("表:链接到源"))
			_axis_data_style += " 表:链接到源=\"true\"";
		
		return "<表:线型" + _axis_line_type  + "/><表:数值" + _axis_data_style + "/>" + _font + 
		"<表:刻度>" + _y_min_value + _y_max_value + _y_major_internal + _y_minor_internal + _x_cross_point
		+ _y_unit + _display_unit + _y_logarithm + _y_sequence_rev + _y_cross_point + "</表:刻度>"
		+ "<表:对齐 uof:locID=\"s0078\">" + _axis_word_direction + _axis_word_rot_angle + _offset + "</表:对齐>";
	}
	
	public String get_gridline() 
	{
		return "<表:网格线" + _axis_line_type;   //网格线只比坐标轴线型多出一个位置属性。可在<chart:grid>中添加
	}
	
	public String get_data_point()
	{
		String border = get_border();
		String padding = get_padding();
		String displaySymbol = _display_symbol;
		
		//永中默认为false
		if (!_display_symbol.contains("表:数值"))
			displaySymbol += " 表:数值=\"false\"";
		if (!_display_symbol.contains("表:系列名"))
			displaySymbol += " 表:系列名=\"false\"";
		if (!_display_symbol.contains("表:图例标志"))
			displaySymbol += " 表:图例标志=\"false\"";
		
		displaySymbol += " 表:类别名=\"false\"";
		
		return border + padding + "<表:显示标志" + displaySymbol  + "/>" + _serie_name;
	}
}