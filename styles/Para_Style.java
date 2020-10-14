package styles;

import org.xml.sax.Attributes;

import stored_data.Style_Data;
import convertor.Common_Pro;
import convertor.Unit_Converter;

/**
 * ����style:family="paragraph"��<style:style> �� <uof:����ʽ��>��ת����
 * 
 * @author xie
 *
 */
public class Para_Style extends Common_Pro{
	private static String _id = "";
	//the result
	private static String _result = "";
	//attributes of <uof:����ʽ��>
	private static String _ele_atts = "";
	//<��:������>
	private static String _sent_pro = "";			
	//�Ʊ�λ����
	private static String _tab_stops = "";			
	//tag for filtration
	private static boolean _filter_tag = false;
	
	//Ϊ��<ͼ��ͼ��>�������Ҫ
	private static String _leftMargin = "";
	private static String _rightMargin = "";
	private static String _topMargin = "";
	private static String _bottomMargin = "";
	private static String _writing_mode = "";
	
	
	private static void clear(){
		_result = "";
		_ele_atts = "";
		_sent_pro = "";
		_tab_stops = "";
	}
	
	public static String get_result(){
		String rst = "";
		
		rst = "<uof:����ʽ��" + _ele_atts + ">";
		rst += "<��:������>" + _sent_pro + "</��:������>";
		rst += _result;
		rst += "</uof:����ʽ��>";
		
		clear();
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(_filter_tag){
			return ;
		}
		else if(qName.equals("style:graphic-properties")){
			_filter_tag = true;
		}
		
		else if(qName.equals("style:default-style")){
			_ele_atts = " ��:��ʶ��=\"defaultp\"" + " ��:����=\"defaultp\"" + " ��:����=\"auto\"";
			_id = "defaultp";
		}
		
		else if(qName.equals("style:style")){
			String styleType = "";
			styleType = Style_Data.is_auto_style() ? "auto" : "custom";
			
			if((attVal = atts.getValue("style:name")) != null){
				attVal = Style_Data.rename(attVal);
				_ele_atts += " ��:��ʶ��=\"" + attVal + "\"";
				_ele_atts += " ��:����=\"" + attVal + "\"";
				_id = attVal;
			}
			
			attVal = atts.getValue("style:parent-style-name");
			attVal = (attVal==null) ? "defaultp" : attVal;
			_ele_atts += " ��:��ʽ������=\"" + attVal + "\"";
			Style_Data.set_parent_style(attVal);
			
			if((attVal = atts.getValue("style:display-name")) != null){
				_ele_atts += " ��:����=\"" + attVal + "\"";
					
			}
			_ele_atts += " ��:����=\"" + styleType + "\"";
			
			if ((attVal=atts.getValue("style:default-outline-level"))!=null 
					&& !attVal.equals("10")) {
				int level = Integer.valueOf(attVal) -1;
				_result += "<��:��ټ���>" + level + "</��:��ټ���>";	
				//_result += "<��:�Զ������Ϣ ��:�������=\"outline\"" +
					//	" ��:��ż���=\"" + level + "\"/>";
			}
		}
		
		else if(qName.equals("style:text-properties")){
			_sent_pro = Sent_Style.process_text_atts(atts);		
			
			if (atts.getValue("fo:hyphenate") != null) {
				_result += "<��:�����ʶ��� ��:ֵ=\"" + atts.getValue("fo:hyphenate") + "\"/>";
			}
		}
		
		else if(qName.equals("style:paragraph-properties")){
			_result += process_para_atts(atts);
		}
		
		else if(qName.equals("style:background-image")){
			String pad = Sent_Style.deal_with_bg_image(atts);
			if(!pad.equals("")){
				_result += "<��:��� uof:locID=\"t0066\">" + pad + "</��:���>";
			}
		}
		
		//====�����³�====
		else if(qName.equals("style:drop-cap")){
			String dropCap ="";
			
			dropCap += " <��:�����³� ��:����=\"dropped\""; 
			if((attVal = atts.getValue("style:style-name")) != null){	
				dropCap +=" ��:��������=\"" + attVal + "\"";
			}
			if((attVal = atts.getValue("style:length")) != null){
				if(attVal.equals("word")){
					dropCap +=" ��:�ַ���=\"1\"" ; 
				}
				else{ 
					dropCap +=" ��:�ַ���=\"" + attVal + "\"";
				}
			}
			//��������
			if((attVal = atts.getValue("style:lines")) != null){
				dropCap +=" ��:����=\"" + attVal + "\"";
			}
			else{
				dropCap +=" ��:����=\"1\"";
			}
			//������
			if((attVal = atts.getValue("style:distance")) != null){
				dropCap +=" ��:���=\"" + Unit_Converter.convert(attVal) + "\"";
			}
			else{
				dropCap +=" ��:���=\"0\"";
			}		
			dropCap += "/>";		
			_result += dropCap;
		}
		
		else if(qName.equals("style:tab-stops")){
			_tab_stops +="<��:�Ʊ�λ����>";
		}
		else if(qName.equals("style:tab-stop")){
			String tab = "<��:�Ʊ�λ";
			
			if((attVal = atts.getValue("style:position")) != null){
				tab += " ��:λ��=\"" + Unit_Converter.convert(attVal) +"\"";
			}
			if((attVal = atts.getValue("style:type")) != null
				&& (attVal.equals("left") || attVal.equals("center") 
						|| attVal.equals("right"))) {
				tab +=" ��:����=\"" + attVal +"\"";
			}
			if((attVal=atts.getValue("style:leader-style")) != null){
				int style = 0;			//ǰ���������֣�1-5
								
				if(attVal.equals("none")){
					style = 0;
				}
				else if(attVal.equals("dash")){
					style = 2;
				}
				else if(attVal.equals("solid")){
					style = 3;
				}
				else if(attVal.equals("dotted")){
					style = 4;
				}
				else {
					style = 1;
				}
				
				tab +=" ��:ǰ����=\"" + style +"\"";
			}

			tab += "/>";		
			_tab_stops += tab;

		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("style:graphic-properties")){
			_filter_tag = false;
		}
		else if(_filter_tag){
			return ;
		}
		
		else if(qName.equals("style:tab-stops")){
			_tab_stops +="</��:�Ʊ�λ����>";
		}
		else if (qName.equals("style:default-style") || qName.equals("style:style")) {
			float fontSize = Sent_Style.get_fontsize();
			if(fontSize != 0){
				Style_Data.add_font_size(_id,fontSize);
			}
			
			Style_Data.add_para_grapro(_id,_leftMargin + "һ" + _rightMargin + "��"
					+ _topMargin + "��" + _bottomMargin + "��" + _writing_mode + "��");    //��Ϊ����ֶ�,"һ������"���ڼ���
			_id = "";

			_leftMargin = "";
			_rightMargin = "";
			_topMargin = "";
			_bottomMargin = "";
			_writing_mode = "";
		}
		
		_result += _tab_stops;
		_tab_stops = "";
	}
	
	public static String process_para_atts(Attributes atts){
		String attVal = "";
		String result = "";
		
		//======����=====
		String alignVal = "";
		if((attVal=atts.getValue("fo:text-align")) != null){
			alignVal +=  " ��:ˮƽ����=\"" + conv_text_align(attVal) + "\"";
		}
		if((attVal=atts.getValue("style:vertical-align")) != null){
			alignVal += " ��:���ֶ���=\"" + conv_vertical_align(attVal) + "\"";
		}
		if(!alignVal.equals("")){
			result += "<��:����  uof:locID=\"t0055\" uof:attrList=\"ˮƽ���� ���ֶ���\"" 
				+ alignVal + "/>";
		}
		
		//======����=====
		String indentVal = "";
		if((attVal=atts.getValue("fo:margin-left")) != null){
			indentVal += "<��:��>";
			indentVal += "<��:���� uof:locID=\"t0185\" uof:attrList=\"ֵ\"" +
						" ��:ֵ=\"" + Unit_Converter.convert(attVal) + "\"/>";;		
			indentVal += "</��:��>";
		}
		if((attVal=atts.getValue("fo:margin-right")) != null){
			indentVal += "<��:��>";
				indentVal += "<��:���� uof:locID=\"t0187\" uof:attrList=\"ֵ\"" +
							" ��:ֵ=\"" + Unit_Converter.convert(attVal) + "\"/>";;
			indentVal += "</��:��>";
		}
		if((attVal=atts.getValue("fo:text-indent")) != null){
			indentVal += "<��:����>";
			indentVal += "<��:���� uof:locID=\"t0189\" uof:attrList=\"ֵ\"" +
						" ��:ֵ=\"" + Unit_Converter.convert(attVal) + "\"/>";;		
			indentVal += "</��:����>";
		}
		if(!indentVal.equals("")){
			result += "<��:���� uof:locID=\"t0056\">" + indentVal + "</��:����>";
		}
		
		//======�о�=====
		String rowSpace = "";
		if((attVal = atts.getValue("fo:line-height")) != null && !attVal.equals("normal")) {
			if(attVal.contains("%")) {
				rowSpace  += "<��:�о� ��:����=\"multi-lines\" ��:ֵ=\"" 
					+ Unit_Converter.from_percent(attVal) + "\"/>";
			}
			else {
				rowSpace  += "<��:�о� ��:����=\"fixed\" ��:ֵ=\"" 
					+ Unit_Converter.convert(attVal) + "\"/>";
			}
		}
		else if((attVal = atts.getValue("style:line-height-at-least")) != null){	
			rowSpace += "<��:�о� ��:����=\"at-least\" ��:ֵ=\""
					+ Unit_Converter.convert(attVal) + "\"/>";
		}
		else if((attVal = atts.getValue("style:line-spacing")) != null){
			rowSpace += " <��:�о� ��:����=\"line-space\" ��:ֵ=\"" 
					+ Unit_Converter.convert(attVal) + "\"/>";
		}
		result += rowSpace;	
		
		//======��ǰ��=====
		String paraSpace = "";
		if ((attVal = atts.getValue("fo:margin-top")) != null) {
			paraSpace += "<��:��ǰ��>";
			paraSpace += "<��:����ֵ uof:locID=\"t0199\" uof:attrList=\"ֵ\"" +
					" ��:ֵ=\"" + Unit_Converter.convert(attVal) + "\"/>";;
			paraSpace += "</��:��ǰ��>";
		}
		if ((attVal = atts.getValue("fo:margin-bottom")) != null) {
			paraSpace += "<��:�κ��>";
			paraSpace += "<��:����ֵ uof:locID=\"t0202\" uof:attrList=\"ֵ\"" +
					" ��:ֵ=\"" + Unit_Converter.convert(attVal) + "\"/>";;
			paraSpace += "</��:�κ��>";
		}
		if(paraSpace.length() != 0){
			result += "<��:�μ��>" + paraSpace + "</��:�μ��>";
		}
		
		//======�Զ������Ϣ=====
		
		//======���п���=====
		if((attVal = atts.getValue("fo:orphans")) != null){
			result += "<��:���п���>" + attVal + "</��:���п���>";
		}
		
		//======���п���=====
		if((attVal = atts.getValue("fo:widows")) != null){
			result += "<��:���п���>" + attVal + "</��:���п���>";
		}
		
		//======���в���ҳ=====
		if((attVal = atts.getValue("fo:keep-together")) != null){
			if(attVal.equals("always")){
				result += "<��:���в���ҳ ��:ֵ=\"true\"/>"; 
			}
		}
		
		//======���¶�ͬҳ=====
		if((attVal = atts.getValue("fo:keep-with-next")) != null){
			if(attVal.equals("always")){
				result += "<��:���¶�ͬҳ ��:ֵ=\"true\"/>"; 
			}
		}
		
		//��ǰ��ҳ
		if((attVal = atts.getValue("fo:break-before")) != null){
			if(attVal.equals("page")){
				result += "<��:��ǰ��ҳ ��:ֵ=\"true\"/>";
			}
		}
		
		//======�߿�=====
		result += get_borders("text", atts);
		
		//======���=====
		if((attVal=atts.getValue("fo:background-color")) != null) {
			if (!attVal.equals("transparent")){
				String padding = "";
				
				padding = "<ͼ:ͼ�� ͼ:����=\"���\"" +
						" ͼ:ǰ��ɫ=\"auto\" ͼ:����ɫ=\"" + attVal + "\"/>";
				result += "<��:��� uof:locID=\"t0066\">" + padding + "</��:���>";
			}
		}
		
		//======��������=====
		if((attVal = atts.getValue("style:snap-to-layout-grid")) != null){
			if(attVal.equals("true")){
				result += "<��:�������� ��:ֵ=\"" + attVal + "\"/>";
			}
		}
		
		//======ȡ������=====��Ӧ��ϵ������
		
		//======ȡ���к�=====
		if((attVal = atts.getValue("text:number-lines")) != null){
			if(attVal.equals("true")){
				result += "<��:ȡ���к� ��:ֵ=\"false\"/>";
			}
			else {
				result += "<��:ȡ���к� ��:ֵ=\"true\"/>";
			}
		}
		
		//�����ʶ���  fo:hyphenate(text-properties)
		
		//======�Ƿ����ױ��ѹ��=====
		if((attVal = atts.getValue("style:punctuation-wrap")) != null){
			if(attVal.equals("hanging")){
				result += "<��:�Ƿ����ױ��ѹ�� ��:ֵ=\"true\"/>";
			}
			else { 
				result += "<��:�Ƿ����ױ��ѹ�� ��:ֵ=\"false\"/>";	
			}
		}
		
		//======�Զ�������Ӣ���ַ����====
		if((attVal = atts.getValue("style:text-autospace")) != null){
			if(attVal.equals("ideograph-alpha"))
			{
				result += "<��:�Զ�������Ӣ���ַ���� ��:ֵ=\"true\"/>";
				result += "<��:�Զ��������������ּ�� ��:ֵ=\"true\"/>";
			}
			else  {
				result += "<��:�Զ�������Ӣ���ַ���� ��:ֵ=\"false\"/>";
				result += "<��:�Զ��������������ּ�� ��:ֵ=\"false\"/>";
			}
		}
		
		return result;
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
			convVal = "justified";
		}
		else if(val.equals("center")){
			convVal = "center";
		}
		
		return convVal;
	}
	
	private static String conv_vertical_align(String val){
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
