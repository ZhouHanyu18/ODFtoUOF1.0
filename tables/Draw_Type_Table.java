package tables;

import java.util.*;

public class Draw_Type_Table {
	//
	private static ArrayList<String> _draw_list = new ArrayList<String>();
	
	private static Map<String,String> _hashmap = new HashMap<String,String>();
	
	
	public Draw_Type_Table() {
		
	}
	
	public static boolean _in_list(String drawName){
		return _draw_list.contains(drawName);
	}
	
	private static void create_draw_list(){
		_draw_list.add("draw:frame");
		_draw_list.add("draw:rect");
		_draw_list.add("draw:line");
		_draw_list.add("draw:polygon");
		_draw_list.add("draw:regular-polygon");
		_draw_list.add("draw:circle");
		_draw_list.add("draw:ellipse");
		_draw_list.add("draw:connector");
		_draw_list.add("draw:caption");
		_draw_list.add("draw:custom-shape");
		_draw_list.add("draw:g");
		_draw_list.add("draw:polyline");
		_draw_list.add("draw:path");
	}
	
	public static void create_map() {
		create_draw_list();
		
		_hashmap.put("rectangle","11.Rectangle");
		_hashmap.put("round-rectangle","15.Rounded Rectangle");
		_hashmap.put("ellipse","19.Oval");
		_hashmap.put("isosceles-triangle","17.Isosceles Triangle");
		_hashmap.put("right-triangle","18.Right Triangle");
		_hashmap.put("trapezoid","13.Trapezoid");
		_hashmap.put("parallelogram","12.Parallelogram");
		_hashmap.put("diamond","14.Diamond");
		_hashmap.put("pentagon1","12.Parallelogram");
		_hashmap.put("cross","111.Cross");
		_hashmap.put("hexagon","110.Hexagon");
		_hashmap.put("octagon","16.Octagon");
		_hashmap.put("can","113.Can");
		_hashmap.put("cube","114.Cube");
		_hashmap.put("block-arc","120.Block Arc");
		_hashmap.put("ring","118.Donut");
		_hashmap.put("paper","116.Folded Corner");
		_hashmap.put("smiley","117.Smile Face");
		_hashmap.put("sun","123.Sun");
		_hashmap.put("moon","124.Moon");
		_hashmap.put("heart","121.Heart");
		_hashmap.put("non-primitive","122.Lightning");
		_hashmap.put("forbidden","119.\"No\" Symbol");
		_hashmap.put("bracket-pair","126.Double Bracket");
		_hashmap.put("left-bracket","129.Left Bracket");
		_hashmap.put("right-bracket","130.Right Bracket");
		_hashmap.put("brace-pair","127.Double Brace");
		_hashmap.put("left-brace","131.Left Brace");
		_hashmap.put("right-brace","132.Right Brace");
		_hashmap.put("quad-bevel","115.Bevel");
		_hashmap.put("left-arrow","22.Left Arrow");
		_hashmap.put("right-arrow","21.Right Arrow");
		_hashmap.put("up-arrow","23.Up Arrow");
		_hashmap.put("down-arrow","24.Down Arrow");
		_hashmap.put("left-right-arrow","25.Left-Right Arrow");
		_hashmap.put("up-down-arrow","26.Up-Down Arrow");
		_hashmap.put("quad-arrow","27.Quad Arrow");
		_hashmap.put("mso-spt100","217.Striped Right Arrow");
		_hashmap.put("notched-right-arrow","218.Notched Right Arrow");
		_hashmap.put("pentagon-right","219.Pentagon Arrow");
		_hashmap.put("chevron","220.Chevron Arrow");
		_hashmap.put("right-arrow-callout","221.Right Arrow Callout");
		_hashmap.put("left-arrow-callout","222.Left Arrow Callout");
		_hashmap.put("up-arrow-callout","223.Up Arrow Callout");
		_hashmap.put("down-arrow-callout","224.Down Arrow Callout");
		_hashmap.put("left-right-arrow-callout","225.Left-Right Arrow Calout");
		_hashmap.put("up-down-arrow-callout","226.Up-Down Arrow Callout");
		_hashmap.put("quad-arrow-callou","227.Quad Arrow Callout");
		_hashmap.put("pentagon","112.Regual Pentagon");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
	}
	
	public static String get_draw_num(String drawType) {
		String string = _hashmap.get(drawType);
		String num = "";
		if (string != null) {
			int index = string.indexOf(".");
			num = string.substring(0,index);
		}
		else
			num = "64";
		return num;
	}
	
	public static String get_draw_name(String drawType) {
		String string = _hashmap.get(drawType);
		String name = "";
		if (string != null) {
			int index = string.indexOf(".");
			name = string.substring(index + 1);
		}
		else 
			name = "Curve";
		return name;
	}
}
