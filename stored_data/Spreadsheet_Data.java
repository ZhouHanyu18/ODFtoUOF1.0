package stored_data;

import java.util.*;

public class Spreadsheet_Data {
	private static String _common_rules = "";			//��:���ô������
	
	private static Map<String,String> 
		_filter_map = new HashMap<String,String>();		//��:ɸѡ
	
	private static ArrayList<String> 
		_map_style_name_list = new ArrayList<String>(); //������ʽ��ʽ��
	
	private static Map<String,Float> _columnWidth_map = new HashMap<String,Float>();  //���ڼ���ê��λ��
	private static Map<String,Float> _rowHeight_map = new HashMap<String,Float>();  //���ڼ���ê��λ��
	private static Map<String,String> _chart_map = new HashMap<String,String>();	//�洢�������ڵ�ͼ��
	
	
	//initialize
	public static void init(){
		_common_rules = "";
		_filter_map.clear();
		_map_style_name_list.clear();
		_columnWidth_map.clear();
		_rowHeight_map.clear();
		_chart_map.clear();
	}
	
	public static void addColumnWidth(String styleName,float cloumnWidth){
		_columnWidth_map.put(styleName,cloumnWidth);
	}
	
	public static float getColumnWidth(String styleName){
		return _columnWidth_map.get(styleName);
	}
	
	public static void addRowHeight(String styleName,float rowHeight){
		_rowHeight_map.put(styleName,rowHeight);
	}
	
	public static float getRowHeight(String styleName){
		return _rowHeight_map.get(styleName);
	}
	
	public static void addCommonRule(String rule){
		_common_rules += rule;
	}
	public static String getCommonRules(){
		return _common_rules;
	}
	
	
//	**********************����<��:ɸѡ>*************************
//	*
	public static void add_filter(String styleName,String fil) {
		_filter_map.put(styleName,fil);
	}
	public static String get_filter(String styleName) {
		return _filter_map.get(styleName);
	}
	
//	**********************������ʽ��ʽ��*************************
//	*
	public static void add_style_name(String name){
		_map_style_name_list.add(name);
	}
	public static boolean in_map_styles(String name){
		return _map_style_name_list.contains(name);
	}
	
	public static void add_chart(String sheetID, String chartStr) {
		String charts = "";
		if (_chart_map.containsKey(sheetID))
			charts = _chart_map.get(sheetID);
		charts += chartStr;
		_chart_map.put(sheetID,charts);
	}
	
	public static String get_charts(String sheetID) {
		return _chart_map.get(sheetID);
	}
	
	public static void set_charts(String sheetID, String charts) {
		_chart_map.put(sheetID,charts);
	}
}
