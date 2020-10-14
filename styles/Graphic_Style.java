package styles;

import org.xml.sax.Attributes;
import stored_data.*;

/**
 * ����style:family="graphic"��<style:style>��ת����
 * 
 * @author chenyuan
 *
 */
public class Graphic_Style {
	private static String _graphic_id = "";   		 //��ǰͼ������ID
	private static boolean _is_default_style = false;//default style����û��style:name���ԣ���Ҫ����id	
	//Ĭ��ͼ��ʽ����������
	private static Attributes _def_gra_pro = null;
	private static Attributes _def_para_pro = null;
	private static Attributes _def_text_pro = null;
	private static boolean _list_tag = false;   //���ڴ���graphic��presentation style����Զ����
	
	
	//initialize
	public static void init(){
		_graphic_id = "";
		_is_default_style = false;
		_def_gra_pro = null;
		_def_para_pro = null;
		_def_text_pro = null;
	}
	
	public static Graphic_Pro get_def() {
		Graphic_Pro graphicpro = new Graphic_Pro();
		if(_def_gra_pro != null){
			graphicpro.process_graphic_atts(_def_gra_pro);
		}
		if(_def_para_pro != null){
			graphicpro.process_graphic_atts(_def_para_pro);
		}
		if(_def_text_pro != null){
			graphicpro.process_graphic_atts(_def_text_pro);
		}
		return graphicpro;
	}
	
	public static void process_start(String qName,Attributes atts){			
		String attVal="";
		
		if (_list_tag) {
			Auto_Num.process_start(qName, atts);
		}
		
		if(qName.equals("style:style")||qName.equals("style:default-style")){
			if(qName.equals("style:default-style")){
				_is_default_style = true;
			}
			if((attVal = atts.getValue("style:name")) != null) {
				//����һ��GraphicPro��ʵ������ID,������currentGraphicProID
				//�Ա㴦��style:graphic-propertiesʱʹ��
				
				attVal = Style_Data.rename(attVal);
				
				Graphic_Pro graphicpro = new Graphic_Pro();		
				if (atts.getValue("style:parent-style-name") != null) {
					String parent = atts.getValue("style:parent-style-name");
					graphicpro.set_parent_style(parent);
				}
				else {   //����Ĭ�ϵ�graphicʽ��
					graphicpro = get_def();
				}
				
				graphicpro.set_id(attVal);
				Style_Data.add_graphic_pro(attVal,graphicpro);
				_graphic_id = attVal;
			}
		}	
		
		if (qName.equals("style:paragraph-properties")) {
			if (_graphic_id.length() != 0){
				(Style_Data.get_graphic_pro(_graphic_id)).process_para_atts(atts);
			}
			else if (_is_default_style) {   //�洢ͼ�ε�Ĭ��ʽ������Ϊ��UOF���޷�����
				_def_para_pro = atts;
			}
		}
		
		else if (qName.equals("style:text-properties")) {
			if (_graphic_id.length() != 0)
				(Style_Data.get_graphic_pro(_graphic_id)).process_sent_atts(atts);
			else if (_is_default_style) {   //�洢ͼ�ε�Ĭ��ʽ������Ϊ��UOF���޷�����
				_def_text_pro = atts;
			}
		}		
		//����currentGraphicProID����ӦGraphicProʵ���д���Ԥ����ͼ�ε������Լ�<�ı�����>Ԫ�ص�ĳЩ���ԡ�
		//���洢ê�������һЩ���ԡ�ע:draw:frame���õ�Ҳ��graphic style.
		else if (qName.equals("style:graphic-properties")) {
			if (_graphic_id.length() != 0)
				(Style_Data.get_graphic_pro(_graphic_id)).process_graphic_atts(atts);
			else if (_is_default_style) {   //�洢ͼ�ε�Ĭ��ʽ������Ϊ��UOF���޷�����
				_def_gra_pro = atts;
			}
		}
		
		else if (qName.equals("text:list-style")) {
			_list_tag = true;
			Auto_Num.process_start(qName, atts);
		}
		
		else if (qName.equals("style:background-image")) {
			String padding = Sent_Style.deal_with_bg_image(atts);
			if(padding.length() != 0) {
				padding = "<ͼ:���>" + Sent_Style.deal_with_bg_image(atts) + "</ͼ:���>";
				if (_graphic_id.length() != 0)
					Style_Data.get_graphic_pro(_graphic_id).set_padding(padding);
			}
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("style:default-style")){
			_is_default_style = false;
		}	
		
		else if (qName.equals("text:list-style")) {
			_list_tag = false;
			Auto_Num.process_end(qName);
		}
		
		if (_list_tag) {
			Auto_Num.process_end(qName);
		}
	}
}