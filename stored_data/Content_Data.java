package stored_data;

import graphic_content.Drawing;

import java.util.*;

import spreadsheet.Anno_In_Cell;
import graphic_content.*;

public class Content_Data {
	
	//====<����>����Ԫ��========
	private static Map<String,Drawing> _drawing_set = new TreeMap<String,Drawing>();   		//ͼ�ζ���
	private static Map<String,String> _other_obj_set = new TreeMap<String,String>();    	//��������
	private static Map<String,String> _formula_set = new TreeMap<String,String>();     		//��ѧ��ʽ

	private static Map<String,TextAnchor> 
			_text_anchor_set = new TreeMap<String,TextAnchor>();  	//�洢<��:ê��>����
	private static Map<String,String> 
			_spreadsheet_anchor_set = new TreeMap<String,String>(); //�洢spreadsheet�е�<uof:ê��>����
	private static Map<String,String> 
			_presentation_anchor_set = new TreeMap<String,String>();//�洢presentation�е�<uof:ê��>����
	
	
	//initialize
	public static void init(){
		_drawing_set.clear();
		_other_obj_set.clear();
		_formula_set.clear();
		_text_anchor_set.clear();
		_spreadsheet_anchor_set.clear();
		_presentation_anchor_set.clear();
	}
	
//	*******************����drawingSet****************************
//	*		
	public static void add_drawing(String ID,Drawing drawing) {
		_drawing_set.put(ID,drawing);
	}
	
	public static boolean is_drawing_set_empty() {
		return (_drawing_set.isEmpty());
	}
	
	public static Drawing get_drawing(String ID) {	
		return _drawing_set.get(ID);
	}	
//	*	
//	***************************************
	
//	*******************����otherObjSet****************************
//	*		
	public static void add_other_obj(String ID,String obj) {
		_other_obj_set.put(ID,obj);
	}
	
	public static boolean is_otherobj_set_empty() {
		return _other_obj_set.isEmpty();
	}
	
	public static String get_other_obj(String ID) {	
		return _other_obj_set.get(ID);
	}	
//	*	
//	***************************************
	
//	*******************����formulaSet****************************
//	*		
	public static void add_formula(String ID,String formula) {
		_formula_set.put(ID,formula);
	}
	
	public static String get_formula(String ID) {	
		return _formula_set.get(ID);
	}	
//	*	
//	***************************************
	
//	*******************����objectSet****************************
//	*To do��������Ӷ���ѧ��ʽ�Ĵ���	
	public static boolean is_object_set_empty() {
		return (_drawing_set.isEmpty()
				&& _other_obj_set.isEmpty()
				&& _formula_set.isEmpty());
	}
	
	private static String process_drawing(Drawing drawing)
	{
		String drawStr = "";
		
		Stack<String> groupIDstack = drawing.get_group_id_stack();
		String id = "";
		while (!groupIDstack.empty()) {
			id = groupIDstack.pop();
			Drawing tempDraw = _drawing_set.get(id);
			if (tempDraw != null) {
				drawStr += process_drawing(tempDraw);
				drawStr += tempDraw.get_drawing_string();
			}
			_drawing_set.remove(id);
		}
		return drawStr;
	}
	
	public static String get_object_set() {
		String objectSet = "";
		
		while (!_drawing_set.keySet().isEmpty()) {
			Iterator iterator = _drawing_set.keySet().iterator();
			String id = (String)iterator.next();
			Drawing drawing = _drawing_set.get(id);
			objectSet += process_drawing(drawing);
			objectSet += drawing.get_drawing_string();
			_drawing_set.remove(id);
		}
		
		Collection<String> othehObjValues = _other_obj_set.values();
		for (Iterator iterator = othehObjValues.iterator(); iterator.hasNext(); ) {
			objectSet += (String)iterator.next();
		}
		
		Collection<String> formulaValues = _formula_set.values();
		for (Iterator iterator = formulaValues.iterator(); iterator.hasNext(); ) {
			objectSet += (String)iterator.next();
		}

		objectSet += Anno_In_Cell.get_anno_drawings();
		return objectSet;
	}	
//	*	
//	***************************************
	
	
//	*******************����textAnchorSet****************************
//	*		
	public static void add_text_anchor(String ID,TextAnchor textanchor) {
		_text_anchor_set.put(ID,textanchor);
	}
	
	public static TextAnchor get_text_anchor(String ID) {	
		return _text_anchor_set.get(ID);
	}	
//	*	
//	***************************************
	
//	*******************����_spreadsheet_anchor_set****************************
//	*		
	public static void add_spreadsheet_anchor(String tableName,String uofAnchor) {
		String anchors = "";
		if (_spreadsheet_anchor_set.containsKey(tableName))
			anchors = _spreadsheet_anchor_set.get(tableName);
		
		_spreadsheet_anchor_set.put(tableName,anchors + uofAnchor);
	}
	
	public static String get_spreadsheet_anchors(String tableName) {	
		return _spreadsheet_anchor_set.get(tableName);
	}	
//	*	
//	***************************************
	
//	*******************����_presentation_anchor_set****************************
//	*		
	public static void add_presentation_anchor(String ID,String uofAnchor) {
		_presentation_anchor_set.put(ID, uofAnchor);
	}
	
	public static String get_presentation_anchor(String ID) {	
		return _presentation_anchor_set.get(ID);
	}	
//	*	
//	***************************************
}
