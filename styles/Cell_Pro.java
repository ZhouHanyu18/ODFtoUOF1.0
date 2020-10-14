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
				_cell_pro +=  "<��:��� uof:locID=\"t0153\">" + padding + "</��:���>";
			}
		}
		
		else if(qName.equals("style:table-cell-properties")){
					
			_cell_pro += get_padding(atts);					//��Ԫ��߾�
					
			_cell_pro += get_borders("text",atts).replace("t0065","t0152");		//�߿�
					
			attVal = atts.getValue("fo:background-color");	//���
			if(attVal != null) {						
				/*if (!attVal.equals("transparent")){
					_cell_pro += "<��:��� uof:locID=\"t0153\">";
					_cell_pro +=  "<uof:��ɫ>" + attVal + "</uof:��ɫ>";
					_cell_pro += "</��:���>";;
				}*/
				if (!attVal.equals("transparent")){		
					_cell_pro += "<��:��� uof:locID=\"t0153\">";
					_cell_pro += "<ͼ:ͼ�� ͼ:����=\"���\"";
					_cell_pro += " ͼ:ǰ��ɫ=\"auto\" ͼ:����ɫ=\"" + attVal + "\"/>";
					_cell_pro += "</��:���>";
				}
			}
				
			attVal=atts.getValue("style:vertical-align");	//��ֱ���뷽ʽ
			if (attVal != null) {		
					_cell_pro += "<��:��ֱ���뷽ʽ>"; 
					_cell_pro += conv_vertical_align(attVal);
					_cell_pro += "</��:��ֱ���뷽ʽ>";
			}	

			attVal=atts.getValue("fo:wrap-option");			//�Զ�����
			if (attVal != null) {
				if (attVal.equals("wrap")){
					_cell_pro += "<��:�Զ����� ��:ֵ=\"false\"/>";
				}
				else{
					_cell_pro += "<��:�Զ����� ��:ֵ=\"true\"/>";
				}
			}
				
			attVal=atts.getValue("style:shrink-to-fit");//��Ӧ����
			if (attVal != null) {
				_cell_pro += "<��:��Ӧ���� ��:ֵ=\"" + attVal + "\"/>";
			}
		}
	}
	
	private static String get_padding(Attributes atts){
		String attVal = "";
		String margin = "";	
		
		if((attVal=atts.getValue("fo:padding")) != null) {
			attVal = Unit_Converter.convert(attVal);
			margin += " ��:��=\"" + attVal + "\" ��:��=\"" + attVal
					+ "\" ��:��=\"" + attVal + "\" ��:��=\"" + attVal + "\"";
		}
		if((attVal = atts.getValue("fo:padding-top")) != null){
			margin += " ��:��=\"" + Unit_Converter.convert(attVal) + "\""; 
		}
		if((attVal = atts.getValue("fo:padding-left")) != null){
			margin += " ��:��=\"" + Unit_Converter.convert(attVal) + "\""; 
		}
		if((attVal = atts.getValue("fo:padding-right")) != null){
			margin += " ��:��=\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal = atts.getValue("fo:padding-bottom")) != null){
			margin += " ��:��=\"" + Unit_Converter.convert(attVal) + "\"";
		}
		
		if(margin.length() != 0){
			margin = "<��:��Ԫ��߾�" + margin + "/>";
		}
		
		return margin;
	}
	
	private static String conv_vertical_align(String val){
		//"style:vertical-align"����ֵ��UOF��both��ODF��automatic�ֱ��ڶԷ��Ҳ�����Ӧ
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
