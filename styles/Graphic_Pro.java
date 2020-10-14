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
	
	//<Ԥ����ͼ��>��<����>����Ԫ����
	private String _padding = "";   			//���  
	private String _line_color = "";   			//����ɫ
	private String _line_type = "";   			//���� 
	private String _line_width = "";   			//�ߴ�ϸ
	private String _for_arrow = "";   			//ǰ�˼�ͷ δ������Ӧdraw:marker-start
	private String _back_arrow = "";   			//��˼�ͷ δ������Ӧdraw:marker-end
	private String _width = "";   				//��� 
	private String _height = "";   				//�߶� 
	private String _rotate_angle = "";   		//��ת�Ƕ�
	private String _x_scale = "";   			//X-���ű��� ����
	private String _y_scale = "";   			//Y-���ű��� ����
	private String _lock_asp_ratio = "";   		//�����ݺ��  ��
	private String _rel_ratio = "";   			//���ԭʼ����  ��
	private String _print = "";   				//��ӡ����
	private String _web_ch = "";   				//Web���� ��
	private String _opacity = "";   			//͸����
	private String _border = "";                //�ı���ʱ����ɫ ���� �ߴ�ϸ�Ĵ���
	
	//���ı����ݵĿ�ʼԪ��Ҳ����������
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
	private String _margin_string = "";			//��߾� �ұ߾� �ϱ߾� �±߾�
	
	//�洢ê������
	private String _anchor_width = "";   			//ê����
	private String _anchor_height = "";   			//ê��߶�
//	private String _anchor_position = "";   		//ê��λ��
	private String _anchor_horirel = "";   			//ê��ˮƽ�����
	private String _anchor_horipos = "";   			//ê��ˮƽλ��
	private String _anchor_vertrel = "";   			//ê�㴹ֱ�����
	private String _anchor_vertpos = "";   			//ê�㴹ֱλ��
	private String _anchor_wrap = "";   			//ê������
	private String _anchor_margin = "";   			//ê��߾�
	private String _anchor_lock = "";   			//ê������  ����
	private String _anchor_protection = "";   		//ê�㱣��
	private String _anchor_allow_overlap = "";  	//�����ص�  ��	
	private String _anchor_type = "";	
	private String _parent_style = "";   			//���ڴ���frame
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
			
		//ͼ��(hatch)������̫��δ����
		if ((value = atts.getValue("draw:fill")) != null) {
			if (!value.equals("none"))
				_hasfill = true;
			
			_padding = "<ͼ:���>";
			if (value.equals("solid"))
				_padding += ("<ͼ:��ɫ>" + atts.getValue("draw:fill-color") + "</ͼ:��ɫ>");
			else if (value.equals("bitmap")) {
				_padding += Draw_Padding.get_fill_image(atts.getValue("draw:fill-image-name"));
				if((value = atts.getValue("style:repeat")) != null){
					if(value.equals("no-repeat"))
						_padding += " ͼ:���뷽ʽ=\"center\"/>";
					else if(value.equals("repeat"))
						_padding += " ͼ:���뷽ʽ=\"tile\"/>";
					else if(value.equals("stretch"))
						_padding += " ͼ:���뷽ʽ=\"stretch\"/>";
				}
				_padding += "/>";
			}
			else if (value.equals("gradient")) {
				_padding += Draw_Padding.get_gradient(atts.getValue("draw:fill-gradient-name"));
			}
			_padding += "</ͼ:���>";
		}
		else if ((value = atts.getValue("fo:background-color")) != null) {
			_padding = "<ͼ:���><ͼ:��ɫ>" + value + "</ͼ:��ɫ></ͼ:���>";
		}
		//====OOo2Ĭ���������ɫ#99ccff.����draw:polyline����=====
		else
			_padding = "<ͼ:���><ͼ:��ɫ>#99ccff</ͼ:��ɫ></ͼ:���>";

		
		if ((value = atts.getValue("draw:stroke")) != null) {
			if (!value.equals("none"))
				_hasstroke = true;
			
			_line_type = "<ͼ:����>";
			if (value.equals("none"))
				_line_type += "none";
			else if (value.equals("solid"))
				_line_type += "single";
			else if (value.equals("dash")) {
				_line_type += "dash";   //��ʱ����Ӧ��Ӧdraw:stroke-dash�������õ�dashʽ������������׼����̫��	
			}
			_line_type += "</ͼ:����>";
		}
	//���ı���Ĵ���
		if ((atts.getValue("fo:border")) != null||(atts.getValue("fo:border-left")) != null||(atts.getValue("fo:border-right")) != null
				||(atts.getValue("fo:border-top")) != null||(atts.getValue("fo:border-bottom")) != null){
		      _border = get_borders(atts);
		}
		
		if ((value = atts.getValue("svg:stroke-color")) != null)
			_line_color = "<ͼ:����ɫ>" + value + "</ͼ:����ɫ>";
		//==============================================
		//========���б���Ҫ������ɫ������Ĭ��Ϊ͸��=======
		else if(_border.equals(""))
			_line_color = "<ͼ:����ɫ>#000000</ͼ:����ɫ>";
		//==============================================
		//==============================================
		
		if ((value = atts.getValue("svg:stroke-width")) != null)
			_line_width = "<ͼ:�ߴ�ϸ>" + Unit_Converter.convert(value) + "</ͼ:�ߴ�ϸ>";
		
		if ((value = atts.getValue("draw:opacity")) != null) {   //ȡ10%��10
			value = value.substring(0,value.indexOf("%"));
			_opacity = "<ͼ:͸����>" + value + "</ͼ:͸����>";
		}
		if ((value = atts.getValue("style:background-transparency")) != null) {
			value = value.substring(0,value.indexOf("%"));
			_opacity = "<ͼ:͸����>" + value + "</ͼ:͸����>";
		}
		
		//��<�ı�����..�����ĳЩ���ԡ�
		if ((value = atts.getValue("fo:wrap-option")) != null) {
			if (value.equals("wrap"))
				_wrap = " ͼ:�Զ�����=\"false\"";
			else
				_wrap = " ͼ:�Զ�����=\"true\"";
		}
		
/*		if ((atts.getValue("draw:auto-grow-width")) != null && atts.getValue("draw:auto-grow-width").equals("true")
				|| (atts.getValue("draw:auto-grow-height")) != null && atts.getValue("draw:auto-grow-height").equals("true"))
			_fit = " ͼ:��С��Ӧ����=\"true\"";*/
		
		if((value = atts.getValue("draw:textarea-horizontal-align")) != null){
			_hori_align = " ͼ:ˮƽ����=\"" + value + "\"";
		}
		if((value = atts.getValue("draw:textarea-vertical-align")) != null){
			_vert_align = " ͼ:��ֱ����=\"" + value + "\"";
		}
		
		
//		==================���´���ê������===================
		String margin_val = "";
		if ((value = atts.getValue("fo:margin")) != null && !value.contains("%")) {
			value = Unit_Converter.convert(value);
			margin_val += (" ��:��=\"" + value + "\" ��:��=\"" + value + "\" ��:��=\"" + value + "\" ��:��=\"" + value + "\"");
		}
		else {
			if ((value = atts.getValue("fo:margin-left")) != null && !value.contains("%"))
				margin_val += (" ��:��=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-right")) != null && !value.contains("%"))
				margin_val += (" ��:��=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-top")) != null && !value.contains("%"))
				margin_val += (" ��:��=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-bottom")) != null && !value.contains("%"))
				margin_val += (" ��:��=\"" + Unit_Converter.convert(value) + "\"");
		}
		if(margin_val.length() != 0){
			_anchor_margin = "<��:�߾�" + margin_val + "/>";
		}
		
		//ODF��style:protect��ֵ�п���Ϊnone,content,size,position����Ӧ�ò����
		if ((value = atts.getValue("style:protect")) != null) {
			if (value.equals("none"))
				_anchor_protection = "<��:���� ��:ֵ=\"false\"/>";
			else   
				_anchor_protection = "<��:���� ��:ֵ=\"true\"/>";
		}
		
		//λ��
		if ((value = atts.getValue("style:horizontal-rel")) != null) {
			if (value.equals("char"))
				_anchor_horirel = " ��:�����=\"char\"";
			else if (value.contains("margin"))
				_anchor_horirel = " ��:�����=\"margin\"";
			else if (value.contains("page"))
				_anchor_horirel = " ��:�����=\"page\"";		
			//UOF�е�columnû�ж�Ӧ,ODF�к���paragraph������margin��û�ж�Ӧ
			
			//default?
			else
				_anchor_horirel = " ��:�����=\"column\"";	
		}

		if ((value = atts.getValue("style:horizontal-pos")) != null && 
				(value.equals("left") || value.equals("right") || value.equals("center"))) {
					_anchor_horipos = "<��:��� uof:locID=\"t0178\" uof:attrList=\"ֵ\" ��:ֵ=\"" + value + "\"/>"; 
			//ODF��ĳЩֵ�޶�Ӧ
		}
		
		if ((value = atts.getValue("style:vertical-rel")) != null) {
			if (value.equals("line"))
				_anchor_vertrel = " ��:�����=\"line\"";
			else if (value.contains("paragraph"))
				_anchor_vertrel = " ��:�����=\"paragraph\"";
			//======����ȡparagraph������ʾ����======
			else if (value.contains("page"))
				//_anchor_vertrel = " ��:�����=\"page\"";
				_anchor_vertrel = " ��:�����=\"paragraph\"";
			//======================================
			
			//UOF�е�marginû�ж�Ӧ,ODF��frame��frame-content��char��baseline��textû�ж�Ӧ
			
			
			//default?
			else
				_anchor_vertrel = " ��:�����=\"paragraph\"";
		}

		if ((value = atts.getValue("style:vertical-pos")) != null) {
			if (value.equals("middle"))
				_anchor_vertpos = "<��:��� uof:locID=\"t0181\" uof:attrList=\"ֵ\" ��:ֵ=\"center\"/>";
			else if (value.equals("top") || value.equals("bottom"))
				_anchor_vertpos = "<��:��� uof:locID=\"t0181\" uof:attrList=\"ֵ\" ��:ֵ=\"" + value + "\"/>";
			//ODF��ĳЩֵ�޶�Ӧ
		}
		
		//����
		if ((value = atts.getValue("style:wrap")) != null) {
			_anchor_wrap = "<��:����";
			if (value.equals("run-through")) {
				if(atts.getValue("style:run-through") != null && atts.getValue("style:run-through").equals("background"))
					_anchor_wrap += " ��:���ŷ�ʽ=\"behindtext\" ��:��������=\"both\"";
				else
					_anchor_wrap += " ��:���ŷ�ʽ=\"infrontoftext\" ��:��������=\"both\"";
			}
			else if (value.equals("left"))
				_anchor_wrap += " ��:���ŷ�ʽ=\"square\" ��:��������=\"left\"";
			else if (value.equals("right"))
				_anchor_wrap += " ��:���ŷ�ʽ=\"square\" ��:��������=\"right\"";
			else if (value.equals("parallel"))
				_anchor_wrap += " ��:���ŷ�ʽ=\"square\" ��:��������=\"both\"";
			else if (value.equals("biggest"))
				_anchor_wrap += " ��:��������=\"largest\"";
			else if (value.equals("none")) {
				_anchor_type = "inline";
				_anchor_wrap += " ��:��������=\"both\"";
			}
			_anchor_wrap += "/>";
		}
		else {
			_anchor_wrap = "<��:���� ��:���ŷ�ʽ=\"square\" ��:��������=\"both\"/>";
		}
		
		//�������Ƿ���ȷ����
		if ((value = atts.getValue("style:flow-with-text")) != null) {
			_anchor_lock = "<��:���� ��:ֵ=\"" + value + "\"/>";		
		}
		
		//used in Anno_In_Cell
		if((value=atts.getValue("fo:padding-left")) != null){
			_margin_string += " ͼ:��߾�=\"" + Unit_Converter.convert(value) + "\"";
		}
		if((value=atts.getValue("fo:padding-right")) != null){
			_margin_string += " ͼ:�ұ߾�=\"" + Unit_Converter.convert(value) + "\"";
		}
		if((value=atts.getValue("fo:padding-top")) != null){
			_margin_string += " ͼ:�ϱ߾�=\"" + Unit_Converter.convert(value) + "\"";
		}
		if((value=atts.getValue("fo:padding-bottom")) != null){
			_margin_string += " ͼ:�±߾�=\"" + Unit_Converter.convert(value) + "\"";
		}
		
		if((value = atts.getValue("style:writing-mode")) != null) {
			if(value.equals("lr-tb"))
				_writing_mode = " ͼ:�������з���=\"hori-l2r\"";
			else if(value.equals("rl-tb"))
				_writing_mode = " ͼ:�������з���=\"hori-r2l\"";
			else if(value.equals("tb-rl"))
				_writing_mode = " ͼ:�������з���=\"vert-l2r\"";
			else if(value.equals("tb-lr"))
				_writing_mode = " ͼ:�������з���=\"vert-r2l\"";
		}
	}
	
	public void process_draw_atts(Attributes atts)
	{
		String value = "";
		
		if ((value = atts.getValue("svg:x")) != null) {
			_svgx = value;
		}
		else if (atts.getValue("svg:x1") != null && atts.getValue("svg:x2") != null) {   //Line�����
			float x1 = Unit_Converter.convert_gra(atts.getValue("svg:x1"));
			float x2 = Unit_Converter.convert_gra(atts.getValue("svg:x2"));
			if (x1 >= x2) {
				_svgx = atts.getValue("svg:x2");
				_width = "<ͼ:���>" + (x1 - x2) + "</ͼ:���>";
				_anchor_width = "<��:��� uof:locID=\"t0112\">" + (x1 - x2) + "</��:���>";
			}
			else {
				_svgx = atts.getValue("svg:x1");
				_width = "<ͼ:���>" + (x2 - x1) + "</ͼ:���>";
				_anchor_width = "<��:��� uof:locID=\"t0112\">" + (x2 - x1) + "</��:���>";
			}
		}
		
		if ((value = atts.getValue("svg:y")) != null) {
			_svgy = value;
		}
		else if (atts.getValue("svg:y1") != null && atts.getValue("svg:y2") != null) {   //Line�����
			float y1 = Unit_Converter.convert_gra(atts.getValue("svg:y1"));
			float y2 = Unit_Converter.convert_gra(atts.getValue("svg:y2"));
			if (y1 >= y2) {
				_svgy = atts.getValue("svg:y2");
				_height = "<ͼ:�߶�>" + (y1 - y2) + "</ͼ:�߶�>";
				_anchor_height = "<��:�߶� uof:locID=\"t0113\">" + (y1 - y2) + "</��:�߶�>";
			}
			else {
				_svgy = atts.getValue("svg:y1");
				_height = "<ͼ:�߶�>" + (y2 - y1) + "</ͼ:�߶�>";
				_anchor_height = "<��:�߶� uof:locID=\"t0113\">" + (y2 - y1) + "</��:�߶�>";
			}
		}
		
		if (_svgx.length() != 0) {  	
			_anchor_horipos = "<��:���� uof:locID=\"t0177\" uof:attrList=\"ֵ\" ��:ֵ=\"" + Unit_Converter.convert_gra(_svgx) + "\"/>";
		}

		if (_svgy.length() != 0) {
			_anchor_vertpos = "<��:���� uof:locID=\"t0180\" uof:attrList=\"ֵ\" ��:ֵ=\"" + Unit_Converter.convert_gra(_svgy) + "\"/>";
		}
		
//		double widthValue = 0,heightValue = 0;
//		int index = 0;
//		String ratioString = "";
		if ((value = atts.getValue("svg:width")) != null) {
			_width = "<ͼ:���>" + Unit_Converter.convert_gra(value) + "</ͼ:���>";
			_anchor_width = "<��:��� uof:locID=\"t0112\">" + Unit_Converter.convert_gra(value) + "</��:���>";
//			index = value.indexOf("cm");
//			widthValue = Double.parseDouble(value.substring(0,index));
		}
		else if ((value = atts.getValue("fo:min-width")) != null) {
			_width = "<ͼ:���>" + Unit_Converter.convert_gra(value) + "</ͼ:���>";
			_anchor_width = "<��:��� uof:locID=\"t0112\">" + Unit_Converter.convert_gra(value) + "</��:���>";
		}
		if ((value = atts.getValue("svg:height")) != null) {
			_height = "<ͼ:�߶�>" + Unit_Converter.convert_gra(value) + "</ͼ:�߶�>";
			_anchor_height = "<��:�߶� uof:locID=\"t0113\">" + Unit_Converter.convert_gra(value) + "</��:�߶�>";
//			index = value.indexOf("cm");
//			heightValue = Double.parseDouble(value.substring(0,index));
//			double ratio = widthValue/heightValue;
//			ratioString = String.valueOf(ratio);
//			_rel_ratio = ("<ͼ:���ԭʼ����>" + ratioString + "</ͼ:���ԭʼ����>");
		}
		if ((value = atts.getValue("draw:layer")) != null) {
			if (Style_Data.get_draw_layer(value).get_display().equals("printer"))
				_print = ("<ͼ:��ӡ����>true</ͼ:��ӡ����>");
//			else
//				_print = ("<ͼ:��ӡ����>false</ͼ:��ӡ����>");
		}
		if ((value = atts.getValue("draw:transform")) != null) {
			//rotate(<rotate-angle>)  ������׼����ת�Ƕȶ�Ӧ��docs���µ�"�±�׼����....txt"
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
				_rotate_angle = ("<ͼ:��ת�Ƕ�>" + rotAngle + "</ͼ:��ת�Ƕ�>");
			}
			//scale(<sx> [<sy>])   ���ű���Ӧ��������ȡֵ��
			if (value.contains("scale")) {
				int index1 = value.indexOf("(");
				int index2 = value.indexOf(")");
				String substring = value.substring(index1 + 1,index2);
				if (substring.contains(" ")) {
					int index3 = substring.indexOf("<");
					int index4 = substring.indexOf(">");
					String XScale = substring.substring(index3 + 1,index4);
					_x_scale = ("<ͼ:X-���ű���>" + XScale + "</ͼ:X-���ű���>");
					String substring2 = substring.substring(index4 + 1);
					int index5 = substring2.indexOf("<");
					int index6 = substring2.indexOf(">");
					String YScale = substring.substring(index5 + 1,index6);
					_y_scale = ("<ͼ:Y-���ű���>" + YScale + "</ͼ:Y-���ű���>");
				}
				else {
					int index3 = substring.indexOf("<");
					int index4 = substring.indexOf(">");
					String XYScale = substring.substring(index3 + 1,index4);
					_x_scale = ("<ͼ:X-���ű���>" + XYScale + "</ͼ:X-���ű���>");
					_y_scale = ("<ͼ:Y-���ű���>" + XYScale + "</ͼ:Y-���ű���>");
				}
			}
		}
		
		if ((value = atts.getValue("style:print-content")) != null) {
			_print = "<ͼ:��ӡ����>" + value + "</ͼ:��ӡ����>";
		}
	}
	
	public void process_para_atts(Attributes atts)
	{
		_parapro += Para_Style.process_para_atts(atts);
		
		String value = "";
		
		if((value = atts.getValue("fo:margin")) != null) {
			value = Unit_Converter.convert(value);
			_left_margin = " ͼ:��߾�=\"" + value + "\"";
			_right_margin = " ͼ:�ұ߾�=\"" + value + "\"";
			_top_margin = " ͼ:�ϱ߾�=\"" + value + "\"";
			_bottom_margin = " ͼ:�±߾�=\"" + value + "\"";
		}
		else {
			if ((value = atts.getValue("fo:margin-left")) != null)
				_left_margin = (" ͼ:��߾�=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-right")) != null)
				_right_margin = (" ͼ:�ұ߾�=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-top")) != null)
				_top_margin = (" ͼ:�ϱ߾�=\"" + Unit_Converter.convert(value) + "\"");
			if ((value = atts.getValue("fo:margin-bottom")) != null)
				_bottom_margin = (" ͼ:�±߾�=\"" + Unit_Converter.convert(value) + "\"");
		}
		
		if((value = atts.getValue("style:writing-mode")) != null) {
			if(value.equals("lr-tb"))
				_writing_mode = " ͼ:�������з���=\"hori-l2r\"";
			else if(value.equals("rl-tb"))
				_writing_mode = " ͼ:�������з���=\"hori-r2l\"";
			else if(value.equals("tb-rl"))
				_writing_mode = " ͼ:�������з���=\"vert-l2r\"";
			else if(value.equals("tb-lr"))
				_writing_mode = " ͼ:�������з���=\"vert-r2l\"";
		}
	}
	
	//����߿򣺴�"0.002cm solid #000000"��ʽ���ַ�����ȡ������ֵ 
	public static String tranBorderValue(String border)
	{
		int index1 = border.indexOf(' ');
		int index2 = border.lastIndexOf(' ');
		String str = "";
		if(border.equals("none")){
			str = "";
		}
		else if(border.substring(index1+1,index2).equals("solid")){////single" + border.substring(index1+1,index2) +
			str = "<ͼ:�ߴ�ϸ>" + Unit_Converter.convert(border.substring(0,index1)) +"</ͼ:�ߴ�ϸ>" +  
			"<ͼ:���� uof:locID=\"g0014\">single</ͼ:����>" +
			"<ͼ:����ɫ>"+ border.substring(index2+1) +"</ͼ:����ɫ>";
		}
		else if(border.substring(index1+1,index2).equals("double")){////single" + border.substring(index1+1,index2) +
			str = "<ͼ:�ߴ�ϸ>" + Unit_Converter.convert(border.substring(0,index1)) +"</ͼ:�ߴ�ϸ>" +  
			"<ͼ:���� uof:locID=\"g0014\">double</ͼ:����>" +
			"<ͼ:����ɫ>"+ border.substring(index2+1) +"</ͼ:����ɫ>";
		}
		
		return str;	
	}
	
	//���� �߿� ��ת��
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
		_parapro += "<��:������>" + Sent_Style.process_text_atts(atts) + "</��:������>";
	}
	
	public String get_anchor_atts()
	{		
		String str = _anchor_width + _anchor_height + "<��:λ�� uof:locID=\"t0114\"><��:ˮƽ" + _anchor_horirel + ">" 
		+ _anchor_horipos + "</��:ˮƽ><��:��ֱ" + _anchor_vertrel + ">" + _anchor_vertpos + "</��:��ֱ>" + "</��:λ��>"
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
