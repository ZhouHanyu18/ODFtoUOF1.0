package styles;

import org.xml.sax.Attributes;
import stored_data.*;

/**
 * 处理style:family="graphic"的<style:style>的转换。
 * 
 * @author chenyuan
 *
 */
public class Graphic_Style {
	private static String _graphic_id = "";   		 //当前图形属性ID
	private static boolean _is_default_style = false;//default style可能没有style:name属性，需要生成id	
	//默认图形式样的属性组
	private static Attributes _def_gra_pro = null;
	private static Attributes _def_para_pro = null;
	private static Attributes _def_text_pro = null;
	private static boolean _list_tag = false;   //用于处理graphic或presentation style里的自动编号
	
	
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
				//生成一个GraphicPro的实例，存ID,并设置currentGraphicProID
				//以便处理style:graphic-properties时使用
				
				attVal = Style_Data.rename(attVal);
				
				Graphic_Pro graphicpro = new Graphic_Pro();		
				if (atts.getValue("style:parent-style-name") != null) {
					String parent = atts.getValue("style:parent-style-name");
					graphicpro.set_parent_style(parent);
				}
				else {   //引用默认的graphic式样
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
			else if (_is_default_style) {   //存储图形的默认式样，因为在UOF中无法引用
				_def_para_pro = atts;
			}
		}
		
		else if (qName.equals("style:text-properties")) {
			if (_graphic_id.length() != 0)
				(Style_Data.get_graphic_pro(_graphic_id)).process_sent_atts(atts);
			else if (_is_default_style) {   //存储图形的默认式样，因为在UOF中无法引用
				_def_text_pro = atts;
			}
		}		
		//根据currentGraphicProID向相应GraphicPro实例中储存预定义图形的属性以及<文本内容>元素的某些属性。
		//并存储锚点所需的一些属性。注:draw:frame引用的也是graphic style.
		else if (qName.equals("style:graphic-properties")) {
			if (_graphic_id.length() != 0)
				(Style_Data.get_graphic_pro(_graphic_id)).process_graphic_atts(atts);
			else if (_is_default_style) {   //存储图形的默认式样，因为在UOF中无法引用
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
				padding = "<图:填充>" + Sent_Style.deal_with_bg_image(atts) + "</图:填充>";
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