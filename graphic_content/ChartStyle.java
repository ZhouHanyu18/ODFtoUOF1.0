package graphic_content;

import org.xml.sax.Attributes;

import styles.*;
import convertor.Common_Pro;
import convertor.Unit_Converter;

public class ChartStyle {

	private String _id = "";
	private String _font = "";   				//<��:����>����Ӧtext-properties
	private String _border = "";   		//<��:�߿�>����paragraph-properties����ȡ��
	private String _padding = "";   		//<��:���>���޶�Ӧ��UOF�����Ƿ�����
	
	//<��:����>�����ڱ��⼯
	private String _hori_align = "";
	private String _vert_align = "";
	private String _indent = "";
	private String _direction = "";
	private String _rotation_angle = "";
	private String _auto_newline = "";
	private String _shrink_to_fit = "";   		//�޶�Ӧ
	
	//<����>������������
	private String _axis_word_direction = "";
	private String _axis_word_rot_angle = "";
	private String _offset = "";   				//�޶�Ӧ
	
	private String _axis_line_type = "";   		//<��:����>�����������ᣬ��graphic-properties����ȡ��������Ҳ���������͵�����
	private String _axis_data_style = "";   //<��:��ֵ>������������
	
	//����������,���̶��߱�־������δ�ҵ���Ӧ
	private String _major_tick_type = "";
	private String _minor_tick_type = "";
	
	//<�̶�>
	//��ֵ�Ჿ��
	private String _y_min_value = "";   		//��ֵ����Сֵ
	private String _y_max_value = "";   		//��ֵ�����ֵ
	private String _y_major_internal = "";   	//��ֵ������λ
	private String _y_minor_internal = "";   	//��ֵ��ε�λ
	private String _x_cross_point = "";   		//��ֵ��ķ��ཻ���
	private String _y_unit = "";   				//��ֵ�ᵥλ
	private String _display_unit = "";   		//��ʾ��λ
	private String _y_logarithm = "";   		//��ֵ�ᣬ����
	private String _y_sequence_rev = "";   		//��ֵ����ת
	//�����Ჿ��
	private String _y_cross_point = "";   		//���������ֵ�����
	private String _x_lable_num = "";   		//�����ǩ��
	private String _x_mark_num = "";   			//����̶���
	private String _x_sequence_rev = "";   		//�������ת
	
	private String _display_symbol = ""; //<��ʾ��־>����������ϵ�к����ݵ�
	private String _serie_name = "";   			//<ϵ����>����������ϵ�к����ݵ㣬δ�ҵ���Ӧ
	
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
			_display_unit = "<��:��ʾ��λ ��:ֵ=\"" + value + "\"/>";
			System.out.print("...\n\n");
		}
		
		if((value = atts.getValue("chart:series-source")) != null) {
			if (value.equals("columns")) {
				Chart.add_data_source(" ��:ϵ�в���=\"col\"");
				Chart_Local_Table.set_series_type("col");
			}
			else if (value.equals("rows")) {
				Chart.add_data_source(" ��:ϵ�в���=\"row\"");
				Chart_Local_Table.set_series_type("row");
			}
		}
		
		if ((value = atts.getValue("style:direction")) != null) {
			if (value.equals("ltr")) {
				_direction += "<��:���ַ���>horizontal</��:���ַ���>";
				_axis_word_direction += "<��:���ַ���>horizontal</��:���ַ���>";
			}
			else if (value.equals("ttb")) {
				_direction += "<��:���ַ���>vertical</��:���ַ���>";
				_axis_word_direction += "<��:���ַ���>vertical</��:���ַ���>";
			}
		}
		
		if ((value = atts.getValue("chart:link-data-style-to-source")) != null) {
			_axis_data_style += " ��:���ӵ�Դ=\"" + value + "\"";
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
				_y_logarithm = "<��:���� ��:ֵ=\"true\">";
			else
				_y_logarithm = "<��:���� ��:ֵ=\"false\">";
		}
		
		if ((value = atts.getValue("chart:minimum")) != null)
			_y_min_value = "<��:��Сֵ>" + value + "</��:��Сֵ>";
		if ((value = atts.getValue("chart:maximum")) != null)
			_y_max_value = "<��:���ֵ>" + value + "</��:���ֵ>";
		
		if ((value = atts.getValue("chart:origin")) != null)
			_y_cross_point = "<��:��ֵ�����>" + value + "</��:��ֵ�����>";
		
		if ((value = atts.getValue("chart:interval-major")) != null) {
			double major = Double.valueOf(value);
			_y_major_internal = "<��:����λ>" + major + "</��:����λ>";
			if ((value = atts.getValue("chart:interval-minor-divisor")) != null) {
				int divisor = Integer.valueOf(value);
				double minor = major/divisor;
				_y_minor_internal = "<��:�ε�λ>" + minor + "</��:�ε�λ>";
			}
		}
		
		if ((value = atts.getValue("chart:data-label-number")) != null) {
			if (value.equals("value"))
				_display_symbol += " ��:��ֵ=\"true\"";
			else if (value.equals("percentage"))
				_display_symbol += " ��:�ٷ���=\"true\"";
			else if (value.equals("none"))
				_display_symbol += " ��:��ֵ=\"false\"";
		}
		
		if ((value = atts.getValue("chart:data-label-text")) != null && value.equals("true"))
			_display_symbol += " ��:ϵ����=\"true\"";
	
		if ((value = atts.getValue("chart:data-label-symbol")) != null && value.equals("true"))
			_display_symbol += " ��:ͼ����־=\"true\"";
	}
	
	public void process_graphic_pro(Attributes atts) 
	{
		String value = "";
		
		if ((value = atts.getValue("fo:wrap-option")) != null) {
			if (value.equals("wrap"))
				_auto_newline += "<��:�Զ����� ֵ=\"true\"/>";
			else
				_auto_newline += "<��:�Զ����� ֵ=\"true\"/>";
		}
		
		//<��:����>,�߾����Ӱ����δ�ҵ���Ӧ
		if ((value = atts.getValue("draw:stroke")) != null) {
			if (value.equals("solid"))
				_axis_line_type += " ��:����=\"single\"";
			else if (value.equals("dash"))
				_axis_line_type += " ��:����=\"dash\"";
			else
				_axis_line_type += " ��:����=\"none\"";
		}
		else _axis_line_type += " ��:����=\"none\"";   //����������required��
		if ((value = atts.getValue("svg:stroke-width")) != null)
			_axis_line_type += " ��:���=\"" + Unit_Converter.convert_gra(value) + "\"";
		if ((value = atts.getValue("svg:stroke-color")) != null)
			_axis_line_type += " ��:��ɫ=\"" + value + "\"";	
		
		if ((value = atts.getValue("draw:fill-color")) != null)
//			_padding = "<ͼ:��ɫ>" + value + "</ͼ:��ɫ>";
			_padding = "<ͼ:��ɫ>auto</ͼ:��ɫ>";
	}
	
	public void process_para_pro(Attributes atts)
	{
		String value = "";
		
		//�߿�
		if ((value = atts.getValue("fo:border")) != null) {
			_border += Common_Pro.tranBorderValue(value);
		}
		
		//����
		if ((value = atts.getValue("fo:text-align")) != null) {
			_hori_align += "<��:ˮƽ���뷽ʽ>";
			if (value.equals("left"))
				_hori_align += "left";
			else if (value.equals("right"))
				_hori_align += "right";
			else if (value.equals("center"))
				_hori_align += "center";
			else if (value.equals("justify"))
				_hori_align += "justified";
			/*start��endû�ж�Ӧ
			 else if (value.equals("start"))
			 align += "left";
			 else if (value.equals("end"))
			 align += "right";
			 */
			_hori_align += "</��:ˮƽ���뷽ʽ>";
		}
		if ((value = atts.getValue("style:vertical-align")) != null) {
			_vert_align += "<��:��ֱ���뷽ʽ>";
			if (value.equals("top"))
				_vert_align += "top";
			else if (value.equals("middle"))
				_vert_align += "center";
			else if (value.equals("bottom"))
				_vert_align += "bottom";
			/*autoû�ж�Ӧ
			 else if (value.equals("auto"))
			 align += "";
			 */
			_vert_align += "</��:��ֱ���뷽ʽ>";
		}
		//To do.�˴���Ҫ�ж�������ȡlength������percent
		if ((value = atts.getValue("fo:text-indent")) != null) {
			_indent += "<��:����>" + value + "</��:����>";
		}
	}
	
	public void process_text_pro(Attributes atts)
	{
		String value = "";
		
		_font = "<��:����>" + Sent_Style.process_text_atts(atts) + "</��:����>";
				
		//��Ҫ�Ľ���ODF��������ת�Ƕ��Ǹ��Ǹ�������UOF������-90��90֮�䡣
		if ((value = atts.getValue("style:rotation-angle")) != null) {
			_rotation_angle += "<��:������ת�Ƕ�>" + value + "</��:������ת�Ƕ�>";
			_axis_word_rot_angle += "<��:��ת�Ƕ�>" + value + "</��:��ת�Ƕ�>";
		}
		
	}
	
	private String get_border() 
	{
		String border = "";
		if (_border.length() != 0)
			border = "<��:�߿�" + _border + "/>";
		return border;
	}
	
	private String get_padding() 
	{
		String padding = "";
		if (_padding.length() != 0)
			padding = "<��:���>" + _padding + "</��:���>";
		else if (Chart.get_type().equals("chart:line"))
			padding = "<��:���><ͼ:��ɫ>auto</ͼ:��ɫ></��:���>";
		return padding;
	}

	private String get_title_align() 
	{
		String titleAlign = _hori_align + _vert_align + _indent + _direction + _rotation_angle
		+ _auto_newline + _shrink_to_fit;
		if (titleAlign.length() != 0)
			titleAlign = "<��:���� uof:locID=\"s0020\">" + titleAlign + "</��:����>";
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
			border = "<��:�߿� uof:locID=\"s0057\" uof:attrList=\"���� ��� �߾� ��ɫ ��Ӱ\"" +
					" uof:����=\"none\"/>";
		}
		String padding = get_padding();
		return "<��:ͼ����>" + border + padding + _font + "</��:ͼ����>";
	}
	
	public String get_plot_area() 
	{
		String border = get_border();
		if (border.length() == 0) {
			border = "<��:�߿� uof:locID=\"s0057\" uof:attrList=\"���� ��� �߾� ��ɫ ��Ӱ\"" +
					" uof:����=\"none\"/>";
		}
		String padding = get_padding();
		return "<��:��ͼ��>" + border + padding + "</��:��ͼ��>";
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
		if (!_axis_data_style.contains("��:���ӵ�Դ"))
			_axis_data_style += " ��:���ӵ�Դ=\"true\"";
		
		return "<��:����" + _axis_line_type + "/><��:��ֵ" + _axis_data_style + "/>" + _font + 
		"<��:�̶�>" + _display_unit + _x_lable_num + _x_mark_num + _x_sequence_rev + "</��:�̶�>"
		+ "<��:���� uof:locID=\"s0078\">" + _axis_word_direction + _axis_word_rot_angle + _offset + "</��:����>";
	}
	
	public String get_y_axis() 
	{
		if (!_axis_data_style.contains("��:���ӵ�Դ"))
			_axis_data_style += " ��:���ӵ�Դ=\"true\"";
		
		return "<��:����" + _axis_line_type  + "/><��:��ֵ" + _axis_data_style + "/>" + _font + 
		"<��:�̶�>" + _y_min_value + _y_max_value + _y_major_internal + _y_minor_internal + _x_cross_point
		+ _y_unit + _display_unit + _y_logarithm + _y_sequence_rev + _y_cross_point + "</��:�̶�>"
		+ "<��:���� uof:locID=\"s0078\">" + _axis_word_direction + _axis_word_rot_angle + _offset + "</��:����>";
	}
	
	public String get_gridline() 
	{
		return "<��:������" + _axis_line_type;   //������ֻ�����������Ͷ��һ��λ�����ԡ�����<chart:grid>�����
	}
	
	public String get_data_point()
	{
		String border = get_border();
		String padding = get_padding();
		String displaySymbol = _display_symbol;
		
		//����Ĭ��Ϊfalse
		if (!_display_symbol.contains("��:��ֵ"))
			displaySymbol += " ��:��ֵ=\"false\"";
		if (!_display_symbol.contains("��:ϵ����"))
			displaySymbol += " ��:ϵ����=\"false\"";
		if (!_display_symbol.contains("��:ͼ����־"))
			displaySymbol += " ��:ͼ����־=\"false\"";
		
		displaySymbol += " ��:�����=\"false\"";
		
		return border + padding + "<��:��ʾ��־" + displaySymbol  + "/>" + _serie_name;
	}
}