package spreadsheet;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import convertor.IDGenerator;
import convertor.Unit_Converter;
import stored_data.Style_Data;
import styles.Graphic_Pro;
import text.Text_Content;

/**
 * ����<office:annotation> �� <��:��ע>��ת����
 * 
 * @author xie
 *
 */
public class Anno_In_Cell {
	//the drawing objs
	private static String _anno_drawings = "";
	//draw:style-name
	private static String _draw_style = "";
	//some attributes for drawing obj
	private static String _draw_atts = "";
	//id of the drawing obj
	private static String _obj_id = "";
	//text content
	private static String _text_content = ""; 
	//tag for text content
	private static boolean _content_tag = false;
	//to store the <uof:ê��>-elements
	private static ArrayList<String> _anno_anchors = new ArrayList<String>();
	
	
	//initialize
	public static void init(){
		_anno_anchors.clear();
		_anno_drawings = "";
	}
	//return one <uof:ê��>-element
	public static String get_anno_anchor(int ind){
		return _anno_anchors.get(ind);
	}
	
	private static void clear(){
		_draw_style = "";
		_draw_atts = "";
		_obj_id = "";
		_text_content = "";
	}
	
	//
	private static String gen_anno_drawing(){
		String drawing = "";
		
		Graphic_Pro gPro = Style_Data.get_graphic_pro(_draw_style);	
		if(gPro == null){
			gPro = new Graphic_Pro();
		}
		_draw_atts = _draw_atts + gPro.get_drawing_pro_string();
		
		drawing = "<ͼ:ͼ�� ͼ:���=\"0\" ͼ:��ʶ��=\"" + _obj_id + "\">";
		
		drawing += "<ͼ:Ԥ����ͼ��>";
		drawing += "<ͼ:���>11</ͼ:���><ͼ:����>Rectangle</ͼ:����>";
		//drawing += "<ͼ:�������>EIOffice</ͼ:�������>"	
		drawing += "<ͼ:����>" + _draw_atts + "</ͼ:����>";
		drawing += "</ͼ:Ԥ����ͼ��>";
		
		drawing += "<ͼ:�ı�����" + gPro.get_margin_string() + ">";
		drawing += _text_content;
		drawing += "</ͼ:�ı�����>";
		
		drawing += "</ͼ:ͼ��>";
		
		clear();
		return drawing;
	}
	
	//return the drawing objs
	public static String get_anno_drawings(){
		String rst = "";
		
		rst = _anno_drawings;
		_anno_drawings = "";
		
		return rst;
	}
	
	private static String add_att(String attName,String attVal){
		return " " + attName + "=\"" + attVal + "\"";
	}
	public static void process_start(String qName,Attributes atts){
		if(_content_tag){
			Text_Content.process_start(qName,atts);
		}
		else if(qName.equals("office:annotation")){
			String anchorAtts = "";	
			
			_draw_style = atts.getValue("draw:style-name");
			//generate a id for the annotation
			_obj_id = IDGenerator.get_anno_obj_id();
			
			//add attributes to uof:ê��
			anchorAtts += add_att("uof:x����",measure_val(atts.getValue("svg:x")));
			anchorAtts += add_att("uof:y����",measure_val(atts.getValue("svg:y")));
			anchorAtts += add_att("uof:���",measure_val(atts.getValue("svg:width")));
			anchorAtts += add_att("uof:�߶�",measure_val(atts.getValue("svg:height")));
			anchorAtts += add_att("uof:ͼ������", _obj_id);
			anchorAtts += add_att("uof:�涯��ʽ", "none");
			
			//
			_draw_atts += "<ͼ:���>" + measure_val(atts.getValue("svg:width")) + "</ͼ:���>";
			_draw_atts += "<ͼ:�߶�>" + measure_val(atts.getValue("svg:height")) + "</ͼ:�߶�>";
			
			//put one element into the list
			_anno_anchors.add("<uof:ê��" + anchorAtts + "/>");
			
			_content_tag = true;
		}
	}
	
	public static void process_chars(String chs){
		if(_content_tag){
			Text_Content.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		if(_content_tag){
			Text_Content.process_end(qName);
			if(qName.equals("office:annotation")){
				_content_tag = false;
				_text_content += Text_Content.get_result();
			
				//add a new drawing obj
				_anno_drawings += gen_anno_drawing();
			}
		}
	}
	
	private static String measure_val(String attVal){
		String val = "";
		
		if(attVal != null){
			val = String.valueOf(Unit_Converter.convert_gra(attVal));
		}
		
		return val;
	}
}
