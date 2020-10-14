package stored_data;

import java.util.TreeMap;
import java.util.Map;

import styles.*;
import graphic_content.DrawLayer;


public class Style_Data{
	//name of style existing in <automatic-styles> of 
	//style.xml should be renamed to avoid collision
	//with that existing in content.xml
	private static boolean _renaming = false;
	//"auto" type of style
	private static boolean _is_autostyle = false; 
	//��ʽ�� ����ʽ�� ��Ԫ��ʽ��
	private static String _styles = "";	
	//
	private static Map<String,String> _data_style_map = new TreeMap<String,String>();  
	//���ֱ�Ԫ������ģ��
	private static Map<String,String> _cell_pro_set = new TreeMap<String,String>();	
	//�洢ͼ�ζ����һЩ��Ԫ��
	private static Map<String,Graphic_Pro> _graphic_pro_set = new TreeMap<String,Graphic_Pro>();  
	//�洢ͼ�ε�layerset
	private static Map<String,DrawLayer> _draw_layer_set = new TreeMap<String,DrawLayer>();  	
	//���ڴ����������
	private static String _parent_style = "";
	private static Map<String,Float> _font_size_map = new TreeMap<String,Float>();
	//
	private static Map<String,String> _para_grapro_map = new TreeMap<String,String>();

	
	//initialize
	public static void init(){
		_renaming = false;
		_is_autostyle = false;
		_styles = "";
		_data_style_map.clear();
		_cell_pro_set.clear();
		_graphic_pro_set.clear();
		_draw_layer_set.clear();
		_parent_style = "";
		_font_size_map.clear();
		_para_grapro_map.clear();
	}
	
	public static void set_renaming(boolean bval){
		_renaming = bval;
	}
	public static boolean is_renaming(){
		return _renaming;
	}
	//change the name by adding "_re"
	public static String rename(String name){
		if(_renaming){
			name = name + "_re";
		}
		
		return name;
	}
	
	public static void add_styles(String style){
		_styles += style;
	}
	public static String get_styles(){
		return _styles; 
	}
	
//***********************************
	public static void add_data_style(String id, String style){
		_data_style_map.put(id, style);
	}
	public static String get_data_style(String id){
		return _data_style_map.get(id);
	}
	
//	******************����isAutoStyle**************************
//	*	
	public static void set_autostyle(boolean bool) {
		_is_autostyle = bool;
	}
	
	public static boolean is_auto_style() {
		return _is_autostyle;
	}
//	*	                                                            
//	**************************************
	
	
//	******************����cellProSet****************************
//	*		
	public static void add_cell_pro(String ID,String cellpro) {
		_cell_pro_set.put(ID,cellpro);
	}
	
	public static String get_cell_pro(String ID) {	
		return _cell_pro_set.get(ID);
	}	
//	*	
//	***************************************
	
//	*******************����graphicProSet****************************
//	*		
	public static void add_graphic_pro(String ID,Graphic_Pro graphicPro) {
		_graphic_pro_set.put(ID,graphicPro);
	}
	
	public static Graphic_Pro get_graphic_pro(String ID) {	
		return _graphic_pro_set.get(ID);
	}	
//	*	
//	***************************************
	
	
//	*******************����drawLayerSet****************************
//	*		
	public static void add_draw_layer(String ID,DrawLayer layer) {
		_draw_layer_set.put(ID,layer);
	}
	
	public static DrawLayer get_draw_layer(String ID) {	
		return _draw_layer_set.get(ID);
	}	
//	*	
//	***************************************
	
	
//  ***************************************
	public static void set_parent_style(String id) 
	{
		_parent_style = id;
	}
	
	public static String get_parent_style() 
	{
		return _parent_style;
	}
	
//  ***************************************		
	public static void add_para_grapro(String id, String str) {
		_para_grapro_map.put(id,str);
	}	
	
	public static String get_paraGraPro_part(String id, String choose) {
		String paraGraPro = _para_grapro_map.get(id);
		
		if(paraGraPro == null) return "";
		
		int index1 = 0, index2 = 0;
		if (choose.equals("leftMargin")) {
			index2 = paraGraPro.indexOf("һ");
		}
		else if (choose.equals("rightMargin")) {
			index1 = paraGraPro.indexOf("һ") + 1;
			index2 = paraGraPro.indexOf("��");
		}
		else if (choose.equals("topMargin")) {
			index1 = paraGraPro.indexOf("��") + 1;
			index2 = paraGraPro.indexOf("��");
		}
		else if (choose.equals("bottomMargin")) {
			index1 = paraGraPro.indexOf("��") + 1;
			index2 = paraGraPro.indexOf("��");
		}
		else if (choose.equals("writingMode")) {
			index1 = paraGraPro.indexOf("��") + 1;
			index2 = paraGraPro.indexOf("��");
		}
		if (index1 == index2 - 1)
			return "";
		return paraGraPro.substring(index1,index2);
	}
	
	public static void add_font_size(String id, float size) {
		_font_size_map.put(id,size);
	}	
	public static float get_font_size(String id) {
		float size = 0;
		if(_font_size_map.containsKey(id)){
			size = _font_size_map.get(id);
		}
		
		return size;
	}
}
