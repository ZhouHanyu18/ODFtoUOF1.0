package styles;

import org.xml.sax.Attributes;

import convertor.Common_Pro;
import convertor.Unit_Converter;
import stored_data.Spreadsheet_Data;
import stored_data.Style_Data;
import stored_data.Common_Data;

/**
 * ����style:family="table-cell"��<style:style> �� <uof:��Ԫ��ʽ��>��ת����
 * 
 * @author xie
 *
 */
public class Cell_Style extends Common_Pro{
	private static String _style_name = "";
	
	//attributes of <uof:��Ԫ��ʽ��>
	private static String _ele_atts = "";
	private static String _format_change = "";   			//��ʽ�޶�
	private static String _sent_pro = "";   				//�����ʽ
	
	private static String _hori_align = "";   				//ˮƽ���뷽ʽ
	private static String _vert_align = "";					//��ֱ���뷽ʽ
	private static String _indent = "";   					//����
	private static String _direction = "";					//���ַ���
	private static String _rotation_angle = "";				//������ת�Ƕ�
	private static String _auto_newline = "";   			//�Զ�����
	private static String _contract_padding = "";  			//��С�������
	
	private static String _num_style = "";   				//���ָ�ʽ
	private static String _border = "";   					//�߿�
	private static String _padding = "";   					//���
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
			//_sent_pro = "<��:���� ��:�ֺ�=\"10\"/>";
		}
		
		result += "<uof:��Ԫ��ʽ��" + _ele_atts + ">";
		result += "<��:�����ʽ>" + _format_change + _sent_pro + "</��:�����ʽ>";
		result += "<��:�����ʽ>" + _hori_align + _vert_align + _indent + _direction 
				+ _rotation_angle + _auto_newline + _contract_padding + "</��:�����ʽ>";
		result += _num_style + _border + _padding;
		result += "</uof:��Ԫ��ʽ��>";
		
		if(_mapped_style){
			result = "";
		}
		clear();
		
		return  result;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:default-style")){
			_ele_atts = " ��:��ʽ������=\"defaultc\"  ��:����=\"auto\"";
		}
		
		else if(qName.equals("style:style")){
			String styleType = "";
			styleType = Style_Data.is_auto_style() ? "auto" : "custom";
			
			if((attVal=atts.getValue("style:name"))!=null){
				_style_name = attVal;
				_ele_atts += " ��:��ʶ��=\"" + _style_name + "\"";
				_ele_atts += " ��:����=\"" + _style_name + "\"";
			}
			
			if((attVal=atts.getValue("style:parent-style-name")) != null){
				_ele_atts += " ��:��ʽ������=\"" + attVal + "\"";
			}else {
				_ele_atts += " ��:��ʽ������=\"defaultc\"";
			}
			
			if((attVal=atts.getValue("style:data-style-name")) != null){
				//_num_style = Style_Data.get_data_style(attVal);
			}
			
			_ele_atts += " ��:����=\"" + styleType + "\"";
		}
		
		else if(qName.equals("style:table-cell-properties")){
			if((attVal=atts.getValue("style:text-align-source")) != null){
				_text_align_source = attVal;
			}
			
			if((attVal=atts.getValue("style:repeat-content")) != null){
				if(attVal.equals("true")){
					_hori_align = "<��:ˮƽ���뷽ʽ>fill</��:ˮƽ���뷽ʽ>";
				}
			}
			
			if((attVal=atts.getValue("style:vertical-align")) != null){
				_vert_align = "<��:��ֱ���뷽ʽ>";
				_vert_align += conv_vertical_align(attVal);
				_vert_align += "</��:��ֱ���뷽ʽ>";
			}
			
			if ((attVal=atts.getValue("style:direction")) != null) {
				if (attVal.equals("ltr")){
					_direction = "<��:���ַ��� uof:locID=\"s0118\">horizontal</��:���ַ���>";
				}
				else if (attVal.equals("ttb")){
					_direction = "<��:���ַ��� uof:locID=\"s0118\">vertical</��:���ַ���>";
				}
			}
			
			//��Ҫ�Ľ���ODF��������ת�Ƕ��Ǹ��Ǹ�������UOF������-90��90֮�䡣
			if ((attVal=atts.getValue("style:rotation-angle")) != null) {
				_rotation_angle = "<��:������ת�Ƕ�>";
				_rotation_angle += conv_rotation_angle(attVal);
				_rotation_angle += "</��:������ת�Ƕ�>";
			}
			
			if ((attVal=atts.getValue("fo:wrap-option")) != null) {
				if(attVal.equals("wrap")){
					_auto_newline = "<��:�Զ����� ��:ֵ=\"true\"/>";
				}
			}	
			if ((attVal=atts.getValue("style:shrink-to-fit")) != null) {		//?????????
				_contract_padding = "<��:��С������� ��:ֵ=\"" + attVal + "\"/>";
			}
			
			//��:�߿�
			_border = get_borders("spreadsheet", atts);
			
			//��:���
			if((attVal=atts.getValue("fo:background-color")) != null) {
				if (!attVal.equals("transparent")){
					String padding = "<ͼ:��ɫ>" + attVal + "</ͼ:��ɫ>";
					_padding = "<��:���>" + padding + "</��:���>";
				}
			}
		}
		
		else if(qName.equals("style:paragraph-properties")){
			if((attVal=atts.getValue("fo:text-align")) != null){
				if(_text_align_source.equals("fix") && !_hori_align.equals("")){
					_hori_align = "<��:ˮƽ���뷽ʽ>";
					_hori_align += conv_text_align(attVal);
					_hori_align += "</��:ˮƽ���뷽ʽ>";
				}
			}
			if((attVal=atts.getValue("fo:margin-left")) !=null ){	//<��:����>�ĵ�λΪһ���ַ���ȣ��ݶ�Ϊ10pt��
				float fVal = Float.parseFloat(Unit_Converter.convert(attVal))/10.0f;
				int iVal = new Float(fVal).intValue();
				_indent += "<��:����>" + iVal + "</��:����>";
			}
		}
		
		else if(qName.equals("style:background-image")){
			_padding = "<��:���>";
			_padding += Sent_Style.deal_with_bg_image(atts);
			_padding += "</��:���>";
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
