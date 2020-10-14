package text;

import java.util.*;
import org.xml.sax.Attributes;
import styles.Sent_Style;
import java.util.ArrayList;
import convertor.Unit_Converter;
import convertor.Common_Pro;

/**
 * 
 * @author xie
 *
 */
public class Page_Layout extends Common_Pro{	
	//attributes of <��:����>
	private static String _columns_pro = "";
	//column gap
	private static float _column_gap = 0.0f;
	//number of columns
	//private static int _column_count = 0;
	//<������>
	private static Sec_Style _sec_style = null;	
	
	private static float _page_width = 0.0f;
	private static float _margin_left = 0.0f;
	private static float _margin_right = 0.0f;
	private static float _space_width = 0.0f;
	//
	private static ArrayList<Integer> rel_width_list = new ArrayList<Integer>();	
	//
	private static Map<String,Sec_Style> _sec_style_map = new TreeMap<String,Sec_Style>();
	
	
	//initialize
	public static void init(){
		_sec_style = null;
		rel_width_list.clear();
		_sec_style_map.clear();
	}
	
	private static void clear(){
		_columns_pro = "";
		_column_gap = 0.0f;
		//_column_count = 0;
		_page_width = 0.0f;
		_margin_left = 0.0f;
		_margin_right = 0.0f;
		rel_width_list.clear();
	}
	
	public static Sec_Style get_sec_style(String name){
		return _sec_style_map.get(name);
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("style:page-layout")){
			//create a new struct
			_sec_style = new Sec_Style();
			
			attVal = atts.getValue("style:name");
			_sec_style.set_id(attVal);
			//add to the map
			_sec_style_map.put(attVal,_sec_style);
		}
		else if(qName.equals("style:page-layout-properties")){
			_sec_style.set_margins(get_margins("text",atts));
			_sec_style.set_page(get_page("text", atts));
			_sec_style.set_orientation(get_orientation("text", atts));
			_sec_style.set_numFormat(get_numFormat(atts));
			_sec_style.set_grid(get_grid(atts));
			_sec_style.set_writingMode(get_writingMode(atts));
			_sec_style.set_borders(get_borders("text",atts).replace("t0065","t0047"));
			
			if((attVal=atts.getValue("fo:page-width"))!=null){
				_page_width = Float.parseFloat(Unit_Converter.convert(attVal));
			}
			if((attVal=atts.getValue("fo:margin-left"))!=null){
				_margin_left = Float.parseFloat(Unit_Converter.convert(attVal));
			}
			if((attVal=atts.getValue("fo:margin-right"))!=null){
				_margin_right = Float.parseFloat(Unit_Converter.convert(attVal));
			}
			if((attVal=atts.getValue("fo:background-color"))!=null && !attVal.equals("none")){
				_sec_style.set_padding("<��:��� uof:locID=\"t0153\"><ͼ:��ɫ>" + attVal + "</ͼ:��ɫ></��:���>");
			}
		}
		else if(qName.equals("style:columns")){
			String str = "";
			
			if((attVal=atts.getValue("fo:column-count"))!=null){
				str += " ��:����=\"" + attVal + "\"";
				//_column_count = Integer.parseInt(attVal);
			}
			
			str += " ��:�ȿ�=\"true\"";
			if((attVal=atts.getValue("fo:column-gap"))!=null){
				_column_gap = Float.parseFloat(Unit_Converter.convert(attVal));			
			}
			
			_columns_pro += str;
		}
		else if(qName.equals("style:column-sep")){
			String str = "";
			
			if((attVal=atts.getValue("style:style"))!=null){
				str += " ��:�ָ���=\"" + tranLineStyle(attVal) + "\"";
			}
			else{
				str += " ��:�ָ���=\"single\"";
			}
			if((attVal=atts.getValue("style:width"))!=null){
				str += " ��:�ָ��߿��=\"" + Unit_Converter.convert(attVal) + "\"";
			}
			if((attVal=atts.getValue("style:color"))!=null){
				str += " ��:�ָ�����ɫ=\"" + attVal + "\"";
			}
			
			_columns_pro += str;
		}
		else if(qName.equals("style:column")){
			int index = 0;
			Integer the_rel = 0;
			float startIndent = 0.0f;
			float endIndent = 0.0f;

			
			if((attVal=atts.getValue("style:rel-width"))!=null){
				index = attVal.indexOf("*");
				the_rel = new Integer(attVal.substring(0, index));
				rel_width_list.add(the_rel);
			}
			if((attVal=atts.getValue("fo:start-indent"))!=null){
				startIndent =  Float.parseFloat(Unit_Converter.convert(attVal));
			}
			if((attVal=atts.getValue("fo:end-indent"))!=null){
				endIndent =  Float.parseFloat(Unit_Converter.convert(attVal));
			}

			if(startIndent == endIndent){
				_space_width += startIndent;
			}
			else{
				_space_width += Math.abs(startIndent-endIndent);
			}
		}
		else if(qName.equals("style:header-footer-properties")){
			String str = "";
			
			if((attVal=atts.getValue("fo:margin-bottom")) != null){
				str = "<��:ҳüλ�� ��:���о=\"" + Unit_Converter.convert(attVal) + "\"/>";
				_sec_style.set_header_position(str);
			}
			else if((attVal=atts.getValue("fo:margin-top")) != null){
				str = "<��:ҳ��λ�� ��:���о=\"" + Unit_Converter.convert(attVal) + "\"/>";
				_sec_style.set_footer_position(str);
			}
		}
		else if (qName.equals("style:background-image")) {
			String padding = Sent_Style.deal_with_bg_image(atts);
			if (padding.length() != 0)
				_sec_style.set_padding("<��:��� uof:locID=\"t0153\">" + padding + "</��:���>");
		}
	}
	
	public static void process_end(String qName){
		
		if(qName.equals("style:columns")){
			String columns = "<��:����" + _columns_pro + ">" + get_columns() + "</��:����>";
			_sec_style.set_columns(columns);
		}
		else if(qName.equals("style:page-layout")){
			clear();
		}
	}

	private static String get_columns(){
		String cols = "";
		int sum = 0;
		float width = 0.0f;

		width = _page_width - _margin_left - _margin_right - _space_width;
		for(int i=0; i < rel_width_list.size(); i++){
			sum += rel_width_list.get(i).intValue();		
		}
		
		for(int i=0; i < rel_width_list.size(); i++){
			float theWidth = 0.0f;
			
			cols += "<��:��";
			theWidth = (rel_width_list.get(i).intValue() * width / sum);
			cols += " ��:���=\"" + Unit_Converter.convert(Float.toString(theWidth) + "cm") + "\"";			
			if(i == 0){
				cols += " ��:���=\"" + _column_gap + "\"";
			}
			cols += "/>";
		}

		return cols;
	}
	
	//����<��:ҳ������>��ת��
	private static String get_numFormat(Attributes atts){
		String str = "";
		String attVal = "";
		
		str += "<��:ҳ������";
		if((attVal=atts.getValue("style:num-format"))!=null){
			str += " ��:��ʽ=\"" + Text_Config.conv_format(attVal) + "\"";
		}
		if((attVal=atts.getValue("style:first-page-number"))!=null){
			str += " ��:��ʼ���=\"" + attVal + "\"";
		}
		str += "/>";
		
		return str;
	}
	
	//����<��:��������>��ת��
	private static String get_grid(Attributes atts){
		String str = "";
		String attVal = "";
		
		if((attVal=atts.getValue("style:layout-grid-mode"))!=null){
			str += " ��:��������=\"" + tranGridMode(attVal) + "\"";
		}
		if((attVal=atts.getValue("style:layout-grid-base-height"))!=null){
			str += " ��:�߶�=\"" + Unit_Converter.convert(attVal) + "\"";
		}
		if((attVal=atts.getValue("style:layout-grid-display"))!=null){
			str += " ��:��ʾ����=\"" + attVal + "\"";
		}
		if((attVal=atts.getValue("style:layout-grid-print"))!=null){
			str += " ��:��ӡ����=\"" + attVal + "\"";
		}
		
		if(str.length()!=0){
			str = "<��:��������" + str + "/>";
		}
		
		return str;
	}
	
	//uof��odf���������͵Ķ��岻һ������Ҫ����ת��
	private static String tranLineStyle(String val){
		String str = "single";	//Ĭ�ϵ���������
		
		if(val.equals("none")){
			str = "none";
		}
		else if(val.equals("solid")){
			str = "single";
		}
		else if(val.equals("dotted")){
			str = "dotted";
		}
		else if(val.equals("dashed")){
			str = "dash";
		}
		else if(val.equals("dot-dashed")){
			str = "dot-dash";
		}
		
		return str;
	}
	
	//����<��:�������з���>��ת��
	private static String get_writingMode(Attributes atts){
		String str = "";
		String attVal = "";
		
		if((attVal=atts.getValue("style:writing-mode"))!=null){
		//	str += "<��:�������з���>";
		//	str += tranWritingMode(attVal);
		//	str += "</��:�������з���>";
		}
		
		return str;
	}
	
	//uof��odf��writing-mode��ֵ�Ķ��岻һ������Ҫת��
	private static String tranWritingMode(String val){
		String str = "vert-r2l";		//Ĭ��ֵ
		
		if(val.equals("lr-tb")){       //lr-tb����left to right-top to bottom
			//str = "hori-l2r";
			str = "vert-r2l";
		}
		else if(val.equals("rl-tb")){
			str = "hori-r2l";
		}
		else if(val.equals("tb-rl")){
			str = "vert-r2l";
		}
		else if(val.equals("tb-lr")){
			str = "vert-l2r";
		}
		
		return str;
	}
	
	private static String tranGridMode(String val){
		String str = "none";
		
		if(val.equals("none")){
			str = "none";
		}
		else if(val.equals("line")){
			str = "line";
		}
		else if(val.equals("both")){
			str = "line-char";
		}
		
		return str;
	}
}
