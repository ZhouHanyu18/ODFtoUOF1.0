package graphic_content;

import org.xml.sax.Attributes;
import java.util.*;
import stored_data.Content_Data;

public class Enhanced_Geo_Handler {
	//���ڵ�ͼ�ε���Ϣ
	private static float _width_pt = 0;   //��ȵ�����ֵ
	private static float _height_pt = 0;   //�߶ȵ�����ֵ
	private static String _drawing_id = "";
	
	//viewBox����Ϣ
	private static int _view_box_left = 0;
	private static int _view_box_top = 0;
	private static int _view_box_right = 0;
	private static int _view_box_bottom = 0;
	private static int _view_box_width = 0;
	private static int _view_box_height = 0;
	
	//stretch-point��Ϣ
	private static int _xstretch = 0;
	private static int _ystretch = 0;
	
	private static Map<Integer,Float> _modifiers = new HashMap<Integer,Float>();   //�洢��modifier��double��ʽ��ֵ
	private static String _enhanced_path = "";   //�洢enhanced:path����ֵ�����ڹ�ʽ���������з���
	private static Map<String,Float> _formula_val_map = new HashMap<String,Float>();   //�洢��formula��double��ʽ��ֵ
	private static Map<String,Set<String>> _formula_ref_list = new HashMap<String,Set<String>>();   //�洢��δ���ֵ��formula���õ�formula�б�
	private static Map<String,Set<String>> _formula_referred_list = new HashMap<String,Set<String>>();   //�洢��δ���ֵ��formula����Щformula����
	private static Map<String,String> _formula_map = new HashMap<String,String>();   //�洢δ���ֵ��formula��String��ʽ
	
	private static boolean _hasstroke = false;
	private static boolean _hasfill = false;
	
	private static final float _PT_to_MM = 2.835f;
	
//	private static float _current_x = 0;  //enhanced-path�е�ǰ�������
//	private static float _current_y = 0;
	
	public static void set_hasstroke(boolean bool) {
		_hasstroke = bool;
	}
	
	public static void set_hasfill(boolean bool) {
		_hasfill = bool;
	}
	
	public static void process_start(String qName,Attributes atts)
	{
		String value = "";
		
		if (qName.equals("draw:enhanced-geometry")) {
			if ((value = atts.getValue("svg:viewBox")) != null) {
				int index1,index2,index3;
				index1 = value.indexOf(" ");
				index2 = value.indexOf(" ",index1 + 1);
				index3 = value.indexOf(" ",index2 + 1);
				_view_box_left = Integer.valueOf(value.substring(0,index1));
				_view_box_top = Integer.valueOf(value.substring(index1 + 1,index2));
				_view_box_right = Integer.valueOf(value.substring(index2 + 1,index3));
				_view_box_bottom = Integer.valueOf(value.substring(index3 + 1));
				_view_box_width = _view_box_right - _view_box_left;
				_view_box_height = _view_box_bottom - _view_box_top;
			}
			//��stretchpoint��Ϊ���Ƶ�
			if (atts.getValue("draw:path-stretchpoint-x") != null && atts.getValue("draw:path-stretchpoint-y") != null) {
/*				String xStretch = atts.getValue("draw:path-stretchpoint-x");
				_xstretch = Integer.valueOf(xStretch);
				String yStretch = atts.getValue("draw:path-stretchpoint-y");
				_ystretch = Integer.valueOf(yStretch);	
				process_ctrl_coors(xStretch,yStretch);*/
			}
			if ((value = atts.getValue("draw:modifiers")) != null) {
				distill_modifiers(value);	
			}
			if ((value = atts.getValue("draw:enhanced-path")) != null) {
				_enhanced_path = value;
			}
		}
		else if (qName.equals("draw:equation") && atts.getValue("draw:name") != null) {
			String name = atts.getValue("draw:name");
			String formulaStr = atts.getValue("draw:formula");
			
			if (!formulaStr.contains("?")) {
				float formulaVal = get_formula_value(formulaStr);
				_formula_val_map.put(name,formulaVal);
				trace_back(name);   //�ݹ����ǰ������Լ����ֵ�Ĺ�ʽ
			}
			else {
				System.out.print(formulaStr + "\n");
				Set<String> refSet = new HashSet<String>();
				int index = formulaStr.indexOf("?");
				String formulaName = "";
				while (index >= 0) {
					int index1 = index + 2;
					while (index1 < formulaStr.length() && is_num(formulaStr.charAt(index1)))
						index1++;
					formulaName = formulaStr.substring(index + 1,index1);   //ȡ��"?"�����õĹ�ʽ��
					index = formulaStr.indexOf("?",index + 1);
					if (_formula_val_map.get(formulaName) == null) {   //��������õĹ�ʽֵδ֪	
						refSet.add(formulaName);   //�ڵ�ǰ��ʽ�����ü���������һ��
						//�ڶ�Ӧ�����ù�ʽ�ı����ü���������һ��
						if (_formula_referred_list.get(formulaName) != null)
							_formula_referred_list.get(formulaName).add(name);
						else {
							Set<String> refedSet = new HashSet<String>();
							refedSet.add(name);
							_formula_referred_list.put(formulaName,refedSet);
						}
					}
				}
				if (!refSet.isEmpty())   //��ǰ��ʽ�����������ֵ����ĳЩ���õĹ�ʽֵδ֪��
				{
					System.out.print("cant\n");
					_formula_ref_list.put(name,refSet);
					_formula_map.put(name,formulaStr);		
				}
				else {  //���򣨴�ʱ�������ù�ʽ��ֵ����֪��
					System.out.print("can\n");
					float formulaVal = get_formula_value(formulaStr);
					System.out.print(formulaVal + "\n");
					_formula_val_map.put(name,formulaVal);	
					trace_back(name);   //�ݹ����ǰ������Լ����ֵ�Ĺ�ʽ
				}
			}
		}
		else if (qName.equals("draw:handle") && atts.getValue("draw:handle-position") != null) {		
/*			String posStr = atts.getValue("draw:handle-position");
			int index = posStr.indexOf(" ");
			String xCoorStr = posStr.substring(0,index);
			String yCoorStr = posStr.substring(index + 1);
			process_ctrl_coors(xCoorStr,yCoorStr);*/
		}
	}
	
	public static void process_end(String qName) 
	{
		if (qName.equals("draw:enhanced-geometry")) {
			if (_enhanced_path.length() > 0)
				deal_with_enhanced_path();   //��process_end()�д�������Ϊ��ʱ����formula��ֵ�Ѿ��������
			
			_width_pt = 0;
			_height_pt = 0;
			_drawing_id = "";
			_view_box_left = 0;
			_view_box_top = 0;
			_view_box_right = 0;
			_view_box_bottom = 0;
			_xstretch = 0;
			_ystretch = 0;
			_modifiers.clear();
			_formula_val_map.clear();
			_formula_ref_list.clear();
			_formula_referred_list.clear();
			_formula_map.clear();
			_hasstroke = false;
			_hasfill = false;
//			_current_x = 0;
//			_current_y =0;
		}
	}
	
	public static void set_paras(String id, float width, float height)
	{
		_width_pt = width;
		_height_pt = height;
		_drawing_id = id;
	}
	
	private static void distill_modifiers(String modifiers)
	{
		float modifier = 0;
		int num = 0;
		int index = modifiers.indexOf(" ");
		while (index >= 0) {
			modifier = Float.valueOf(modifiers.substring(0,index));
			_modifiers.put(num,modifier);
			num++;
			modifiers = modifiers.substring(index + 1);
			index = modifiers.indexOf(" ");
		}
		modifier = Float.valueOf(modifiers);
		_modifiers.put(num,modifier);
	}
	
	//����draw:enhanced-path,��д<ͼ:�ؼ�������>
	private static void deal_with_enhanced_path()
	{
		String ins= "MLCZNFSTUABWVXYQ";   //path�п��ܳ��ֵ�instruction�ļ���
		int index1 = 0;
		int index2 = 0;
		
		//������$��?�����ֵȡ����ʹ��enhanced_pathֻ����ָ�����ֵ����
		int num = 0;  //modifier��id
		String formulaName = "";  //formula��ID
		
		index1 = _enhanced_path.indexOf("$");
		while (index1 >= 0) {
			index2 = _enhanced_path.indexOf(" ",index1);
			if (index2 >= 0)
				num = Integer.valueOf(_enhanced_path.substring(index1 + 1,index2));
			else
				num = Integer.valueOf(_enhanced_path.substring(index1 + 1));
			_enhanced_path = _enhanced_path.substring(0,index1) + String.valueOf(_modifiers.get(num)) + _enhanced_path.substring(index2);
			index1 = _enhanced_path.indexOf("$");
		}
		
		index1 = _enhanced_path.indexOf("?");
		while (index1 >= 0) {
			index2 = _enhanced_path.indexOf(" ",index1);
			if (index2 >= 0)
				formulaName = _enhanced_path.substring(index1 + 1,index2);
			else
				formulaName = _enhanced_path.substring(index1 + 1);
			_enhanced_path = _enhanced_path.substring(0,index1) + String.valueOf(_formula_val_map.get(formulaName)) + _enhanced_path.substring(index2);
			index1 = _enhanced_path.indexOf("?");
		}
		
		System.out.print("?" + _enhanced_path + "\n");
		
		//��_enhanced_path��ÿ��ָ��ֲ����������ת��
		String enhancedPath = "";
		String insStr = "";
		index1 = 0;
		index2 = 1;
		while (index2 < _enhanced_path.length()) {
			if (ins.contains(String.valueOf(_enhanced_path.charAt(index2)))) {
				insStr = _enhanced_path.substring(index1,index2);
				enhancedPath += deal_with_ins(insStr);
				index1 = index2;
				index2++;
			}
			else
				index2++;
		}
		insStr = _enhanced_path.substring(index1);
		enhancedPath += deal_with_ins(insStr);
		
		Content_Data.get_drawing(_drawing_id).add_key_coors("<ͼ:�ؼ������� ͼ:·��=\"" + enhancedPath + "\"/>");
	}
	
	//����draw:enhanced-path��ÿ��ָ���ֵ
	//????���ʣ���������????��ʱδ�Ӷ�������
	private static String deal_with_ins(String insStr) 
	{
		System.out.print(insStr + "\n");
		String result = "";
		String xCoor = "";
		String yCoor = "";
		char insChar = insStr.charAt(0);   //ָ����
		if (insStr.trim().length() > 1) {
			String paraStr = insStr.substring(1).trim();   //ָ�����
			if (insChar == 'M') {
				int index1 = 0, index2 = 0;
				index1 = paraStr.indexOf(" ");
				index2 = paraStr.indexOf(" ",index1 + 1);
				xCoor = paraStr.substring(0,index1);
				if (index2 < 0) {
					yCoor = paraStr.substring(index1 + 1);
					result = "M " + get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
				}
				else {
					yCoor = paraStr.substring(index1 + 1,index2);
					result = "M " + get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
					paraStr = paraStr.substring(index2 + 1);
					while (index2 >= 0) {
						index1 = paraStr.indexOf(" ");
						index2 = paraStr.indexOf(" ",index1 + 1);
						xCoor = paraStr.substring(0,index1);
						if (index2 < 0) {
							yCoor = paraStr.substring(index1 + 1);
						}
						else {
							yCoor = paraStr.substring(index1 + 1,index2);
							paraStr = paraStr.substring(index2 + 1);
						}
						result += "L " + get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
					}
				}
			}
			else if (insChar == 'L') {
				int index1 = 0, index2 = 0;
				while (index2 >= 0) {
					index1 = paraStr.indexOf(" ");
					index2 = paraStr.indexOf(" ",index1 + 1);
					xCoor = paraStr.substring(0,index1);
					if (index2 < 0) {
						yCoor = paraStr.substring(index1 + 1);
					}
					else {
						yCoor = paraStr.substring(index1 + 1,index2);
						paraStr = paraStr.substring(index2 + 1);
					}
					result += "L " + get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
				}
			}
			else if (insChar == 'C') {
				int index1 = 0,index2 = 0,index3 = 0,index4 = 0,index5 = 0,index6 = 0;
				while (index6 >= 0) {
					result += "C ";
					
					index1 = paraStr.indexOf(" ");
					index2 = paraStr.indexOf(" ",index1 + 1);
					index3 = paraStr.indexOf(" ",index2 + 1);
					index4 = paraStr.indexOf(" ",index3 + 1);
					index5 = paraStr.indexOf(" ",index4 + 1);
					index6 = paraStr.indexOf(" ",index5 + 1);
					
					xCoor = paraStr.substring(0,index1);
					yCoor = paraStr.substring(index1 + 1,index2);
					result += get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
					
					xCoor = paraStr.substring(index2 + 1,index3);
					yCoor = paraStr.substring(index3 + 1,index4);
					result += get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
					
					xCoor = paraStr.substring(index4 + 1,index5);
					if (index6 < 0) {
						yCoor = paraStr.substring(index5 + 1);
					}
					else {
						yCoor = paraStr.substring(index5 + 1,index6);
						paraStr = paraStr.substring(index6 + 1);
					}
					result += get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
				}
			}
			else if (insChar == 'T' || insChar == 'U') {
				//To do
			}
			else if (insChar == 'A' || insChar == 'B' || insChar == 'W' || insChar == 'V') {
				//To do
			}
			else if (insChar == 'X' || insChar == 'Y') {
				//To do
			}
			else if (insChar == 'Q') {
				//To do
			}
		}
		else {
			if (insChar == 'Z') {
				result = "Z";
			}
			//N��F��S�޶�Ӧ
		}
		return result.trim();
	}

	private static float get_x_pt(String xCoor) {
		return (Float.valueOf(xCoor) - _view_box_left)/_view_box_width * _width_pt;
	}
	
	private static float get_y_pt(String yCoor) {
		return (Float.valueOf(yCoor) - _view_box_top)/_view_box_height * _height_pt;
	}
	
//	There are some problems with <ͼ:���Ƶ�> in EIOffice.
/*	private static void process_ctrl_coors(String xCoorStr,String yCoorStr)
	{   
		float xCoor = get_handle_coor(xCoorStr);
		xCoor = (xCoor - _view_box_left)/_view_box_width * _width_pt;
		float yCoor = get_handle_coor(yCoorStr);
		yCoor = (yCoor - _view_box_top)/_view_box_height * _height_pt;
		Content_Data.get_drawing(_drawing_id).add_ctrl_points("<ͼ:���Ƶ� ͼ:x����=\"" + xCoor 
				+ "\" ͼ:y����=\"" + yCoor + "\"/>");
	}
	
	private static float get_handle_coor(String coorStr)
	{
		float coor = 0;
		if (coorStr.startsWith("?")) {
			String name = coorStr.substring(1);
			coor = _formula_val_map.get(name);
		}
		else if (coorStr.startsWith("$")) {
			int num = Integer.valueOf(coorStr.substring(1));
			coor = _modifiers.get(num);
		}
		else if (coorStr.equals("left")) {
			coor = _view_box_left;
		}
		else if (coorStr.equals("top")) {
			coor = _view_box_top;
		}
		else if (coorStr.equals("right")) {
			coor = _view_box_right;
		}
		else if (coorStr.equals("bottom")) {
			coor = _view_box_bottom;
		}
		else if (coorStr.equals("xstretch")) {
			coor = _xstretch;
		}
		else if (coorStr.equals("ystretch")) {
			coor = _ystretch;
		}
		else if (coorStr.equals("hasstroke")) {
			if(_hasstroke)
				coor = 1;
			else
				coor = 0;
		}
		else if (coorStr.equals("hasfill")) {
			if(_hasfill)
				coor = 1;
			else 
				coor = 0;
		}
		else if (coorStr.equals("width")) {
			coor = _view_box_width;
		}
		else if (coorStr.equals("height")) {
			coor = _view_box_height;
		}
		else if (coorStr.equals("logwidth")) {
			coor = _width_pt/_PT_to_MM * 100;
		}
		else if (coorStr.equals("logheight")) {
			coor = _height_pt/_PT_to_MM * 100;
		}
		else {
			coor = Float.valueOf(coorStr);
		}
		
		return coor;
	}*/

	private static float get_formula_value(String formula)
	{
		formula = process_minus(formula);
		int index = 0;
		String subStr = "";
		Stack<String> stack1 = new Stack<String>();  //����ת��Ϊ��׺���ʽ
		Stack<Float> stack2 = new Stack<Float>();  //���ں�׺���ʽ��ֵ
		String tempExp = "";   //���ڴ�ź�׺���ʽ��ʽ�Ĺ�ʽ
//========ɨ��formula��ת��Ϊ��׺���ʽ���ұ�֤ת����ֻ�����ֺ�+��-��*��/����========	
		while (index < formula.length()) {
			subStr = formula.substring(index).trim();
			if (subStr.length() == 0)
				break;
			else if (is_num(subStr.charAt(0))) {
				int tempIndex = 0;
				while ((tempIndex < subStr.length()) && is_num(subStr.charAt(tempIndex))) {
					tempIndex ++;
				}
				String numStr = subStr.substring(0,tempIndex);
				tempExp += numStr + " ";   //��׺���ʽ������֮���ÿո�ָ�
				index += tempIndex;
			}
			else if (subStr.startsWith("pi")) {
				tempExp += "3.1416 ";
				index += 2;
			}
			else if (subStr.startsWith("left")) {
				tempExp += String.valueOf(_view_box_left) + " ";
				index += 4;
			}
			else if (subStr.startsWith("right")) {
				tempExp += String.valueOf(_view_box_right) + " ";
				index += 5;
			}
			else if (subStr.startsWith("top")) {
				tempExp += String.valueOf(_view_box_top) + " ";
				index += 3;
			}
			else if (subStr.startsWith("bottom")) {
				tempExp += String.valueOf(_view_box_bottom) + " ";
				index += 6;
			}
			else if (subStr.startsWith("xstretch")) {
				tempExp += String.valueOf(_xstretch) + " ";
				index += 8;
			}
			else if (subStr.startsWith("ystretch")) {
				tempExp += String.valueOf(_ystretch) + " ";
				index += 8;
			}
			else if (subStr.startsWith("hasstroke")) {
				if(_hasstroke)
					tempExp += "1 ";
				else
					tempExp += "0 ";
				index += 9;
			}
			else if (subStr.startsWith("hasfill")) {
				if(_hasfill)
					tempExp += "1 ";
				else
					tempExp += "0 ";
				index += 7;
			}
			else if (subStr.startsWith("width")) {
				tempExp += String.valueOf(_view_box_width) + " ";
				index += 5;
			}
			else if (subStr.startsWith("height")) {
				tempExp += String.valueOf(_view_box_height) + " ";
				index += 6;
			}
			else if (subStr.startsWith("logwidth")) {
				tempExp += String.valueOf(_width_pt/_PT_to_MM * 100) + " ";
				index += 8;
			}
			else if (subStr.startsWith("logheight")) {
				tempExp += String.valueOf(_height_pt/_PT_to_MM * 100) + " ";
				index += 9;
			}
			else if (subStr.startsWith("abs")) {
				String paras = get_func_paras(subStr.substring(4));   //��ȥ"abs("
				float value = Math.abs(get_formula_value(paras));   //����Ҳ��Ϊ��ʽ�Դ����ݹ�����ֵ
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 5;
			}
			else if (subStr.startsWith("sqrt")) {
				String paras = get_func_paras(subStr.substring(5));
				System.out.print(paras + "\n");
				float value = (float)Math.sqrt(get_formula_value(paras));
				System.out.print(value + "\n");
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 6;
			}
			else if (subStr.startsWith("sin")) {
				String paras = get_func_paras(subStr.substring(4));
				float value = (float)Math.sin(get_formula_value(paras));
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 5;
			}
			else if (subStr.startsWith("cos")) {
				String paras = get_func_paras(subStr.substring(4));
				System.out.print(paras + "cos\n");
				float value = (float)Math.cos(get_formula_value(paras));
				System.out.print(value + "cos\n");
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 5;
			}
			else if (subStr.startsWith("tan")) {
				String paras = get_func_paras(subStr.substring(4));
				float value = (float)Math.tan(get_formula_value(paras));
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 5;
			}
			else if (subStr.startsWith("atan")) {
				String paras = get_func_paras(subStr.substring(5));
				float value = (float)Math.atan(get_formula_value(paras));
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 6;
			}
			else if (subStr.startsWith("atan2")) {
				String paras = get_func_paras(subStr.substring(6));
				int tempIndex = 0, divideTag = 0;
				float paraA = 0, paraB = 0;
				while (tempIndex < paras.length()) {
					if (paras.charAt(tempIndex) == '(') {
						tempIndex++;
						divideTag++;
					}
					else if (paras.charAt(tempIndex) == ')') {
						tempIndex++;
						divideTag--;
					}
					else if (paras.charAt(tempIndex) == ',') {
						tempIndex++;
						if (divideTag == 0) {
							paraA = get_formula_value(paras.substring(0,tempIndex - 1));
							paraB = get_formula_value(paras.substring(tempIndex));
						}
					}
				}
				float value = (float)Math.atan2(paraA, paraB);
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 7;
			}
			else if (subStr.startsWith("min")) {
				String paras = get_func_paras(subStr.substring(4));
				int tempIndex = 0, divideTag = 0;
				float paraA = 0, paraB = 0;
				while (tempIndex < paras.length()) {
					if (paras.charAt(tempIndex) == '(') {
						tempIndex++;
						divideTag++;
					}
					else if (paras.charAt(tempIndex) == ')') {
						tempIndex++;
						divideTag--;
					}
					else if (paras.charAt(tempIndex) == ',') {
						tempIndex++;
						if (divideTag == 0) {
							paraA = get_formula_value(paras.substring(0,tempIndex - 1));
							paraB = get_formula_value(paras.substring(tempIndex));
						}
					}
				}
				float value = (float)Math.atan2(paraA, paraB);
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 5;
			}
			else if (subStr.startsWith("max")) {
				String paras = get_func_paras(subStr.substring(4));
				int tempIndex = 0, divideTag = 0;
				float paraA = 0, paraB = 0;
				while (tempIndex < paras.length()) {
					if (paras.charAt(tempIndex) == '(') {
						tempIndex++;
						divideTag++;
					}
					else if (paras.charAt(tempIndex) == ')') {
						tempIndex++;
						divideTag--;
					}
					else if (paras.charAt(tempIndex) == ',') {
						tempIndex++;
						if (divideTag == 0) {
							paraA = get_formula_value(paras.substring(0,tempIndex - 1));
							paraB = get_formula_value(paras.substring(tempIndex));
						}
					}
				}
				float value = (float)Math.atan2(paraA, paraB);
				tempExp += String.valueOf(value) + " ";
				index += paras.length() + 5;
			}
			else if (subStr.startsWith("if")) {
				String paras = get_func_paras(subStr.substring(3));
				int tempIndex = 0, divideTag = 0, paraLen = paras.length();
				float paraA = 0, paraB = 0, paraC = 0, value = 0;
				while (tempIndex < paras.length()) {
					if (paras.charAt(tempIndex) == '(') {
						tempIndex++;
						divideTag++;
					}
					else if (paras.charAt(tempIndex) == ')') {
						tempIndex++;
						divideTag--;
					}
					else if (paras.charAt(tempIndex) == ',') {
						tempIndex++;
						if (divideTag == 0) {
							paraA = get_formula_value(paras.substring(0,tempIndex - 1));   //ȡ��paraA
							//ȡif(,,)�еĺ���������
							paras = paras.substring(tempIndex);   
							tempIndex = 0;
							while (tempIndex < paras.length()) {
								if (paras.charAt(tempIndex) == '(') {
									tempIndex++;
									divideTag++;
								}
								else if (paras.charAt(tempIndex) == ')') {
									tempIndex++;
									divideTag--;
								}
								else if (paras.charAt(tempIndex) == ',') {
									tempIndex++;
									if (divideTag == 0) {
										paraB = get_formula_value(paras.substring(0,tempIndex - 1));
										paraC = get_formula_value(paras.substring(tempIndex));
									}
								}
								else
									tempIndex++;
							}
						}
					}
					else 
						tempIndex++;
				}
				if (paraA != 0)
					value = paraB;
				else
					value = paraC;
				tempExp += String.valueOf(value) + " ";
				index += paraLen + 4;
			}
			else if (subStr.startsWith("?")) {
				int tempIndex = 2;
				if (tempIndex < subStr.length() && is_num(subStr.charAt(tempIndex)))
					tempIndex++;	
				String formulaName = "";
				if (tempIndex >= 0) {
					formulaName = subStr.substring(1,tempIndex);
					if (tempIndex < subStr.length() && subStr.charAt(tempIndex) == ' ')
						index += tempIndex + 1;
					else
						index += tempIndex;
				}
				else {
					formulaName = subStr.substring(1);
					index += subStr.length();
				}
				tempExp += String.valueOf(_formula_val_map.get(formulaName)) + " ";
			}
			else if (subStr.startsWith("$")) {
				int tempIndex = 2;
				if (tempIndex < subStr.length() && is_num(subStr.charAt(tempIndex)))
					tempIndex++;
				int modifierNum = 0;
				if (tempIndex >= 0) {
					modifierNum = Integer.valueOf(subStr.substring(1,tempIndex));
					if (tempIndex < subStr.length() && subStr.charAt(tempIndex) == ' ')
						index += tempIndex + 1;
					else
						index += tempIndex;
				}
				else {
					modifierNum = Integer.valueOf(subStr.substring(1));
					index += subStr.length();
				}
				tempExp += String.valueOf(_modifiers.get(modifierNum)) + " ";
			}
			else if (subStr.startsWith("(")) {
				stack1.push("(");
				index++;
			}
			else if (subStr.startsWith(")")) {
				String topStr = stack1.pop();
				while (!topStr.equals("(")) {
					tempExp += topStr;
					topStr = stack1.pop();
				}
				index++;
			}
			else if (subStr.startsWith("+")) {
				if (!stack1.empty()) {
					String topStr = stack1.peek();
					while (!topStr.equals("(")) {
						tempExp += topStr;
						stack1.pop();
						if (stack1.empty())
							break;
						else
							topStr = stack1.peek();
					}
				}
				stack1.push("+");
				index++;
			}
			else if (subStr.startsWith("-")) {
				if (!stack1.empty()) {
					String topStr = stack1.peek();
					while (!topStr.equals("(")) {
						tempExp += topStr;
						stack1.pop();
						if (stack1.empty())
							break;
						else
							topStr = stack1.peek();
					}
				}
				stack1.push("-");
				index++;
			}
			else if (subStr.startsWith("*")) {
				if (!stack1.empty()) {
					String topStr = stack1.peek();
					while (topStr.equals("*") || topStr.equals("/")) {
						tempExp += topStr;
						stack1.pop();
						if (stack1.empty())
							break;
						else
							topStr = stack1.peek();
					}
				}
				stack1.push("*");
				index++;
			}
			else if (subStr.startsWith("/")) {
				if (!stack1.empty()) {
					String topStr = stack1.peek();
					while (topStr.equals("*") || topStr.equals("/")) {
						tempExp += topStr;
						stack1.pop();
						if (stack1.empty())
							break;
						else
							topStr = stack1.peek();
					}
				}
				stack1.push("/");
				index++;
			}
			else index++;
			System.out.print(tempExp + "~~~\n");
		}
		while (!stack1.empty()) {
			tempExp += stack1.pop();
			System.out.print(tempExp + "++++\n");
		}
		
		System.out.print(tempExp + "~~~\n");
//==============================================================
		
//=======================��׺���ʽ��ֵ==========================
		int tempIndex = 0;
		boolean minusTag = false; 
		while (tempIndex < tempExp.length()) {
			char ch = tempExp.charAt(tempIndex);
			if (is_num(ch)) {
				int end = tempExp.indexOf(" ",tempIndex);
				stack2.push(minusTag?-Float.valueOf(tempExp.substring(tempIndex,end)):Float.valueOf(tempExp.substring(tempIndex,end)));
				tempIndex = end + 1;
				minusTag = false;
			}
			else if (ch == '+') {
				stack2.push(stack2.pop() + stack2.pop());
				tempIndex++;
			}
			else if (ch == '-') {
				if ((tempIndex + 1) < tempExp.length() && is_num(tempExp.charAt(tempIndex + 1)))  //����
					minusTag = true;
				else
					stack2.push(-stack2.pop() + stack2.pop());
				tempIndex++;
			}
			else if (ch == '*') {
				stack2.push(stack2.pop() * stack2.pop());
				tempIndex++;
			}
			else if (ch == '/') {
				Float temp = stack2.pop();
				stack2.push(stack2.pop() / temp);
				tempIndex++;
			}
		}
//==============================================================

		return stack2.pop();   //����ֵ
	}
	
	//�ݹ����ǰ������Լ����ֵ�Ĺ�ʽ
	private static void trace_back(String formulaName)
	{
		if (_formula_referred_list.get(formulaName) != null) {
			Set<String> referredSet = _formula_referred_list.get(formulaName);
			String refName = "";   //�����˵�ǰ��ʽ�Ĺ�ʽ��
			for (Iterator iterator = referredSet.iterator(); iterator.hasNext(); ) {
				refName = (String)iterator.next();
				_formula_ref_list.get(refName).remove(formulaName);
				if (_formula_ref_list.get(refName).isEmpty()) {
					_formula_ref_list.remove(refName);
					String formulaStr = _formula_map.get(refName);
					float formulaVal = get_formula_value(formulaStr);
					_formula_val_map.put(refName,formulaVal);
					trace_back(refName);
				}
			}
			_formula_referred_list.remove(formulaName);
		}
		return;
	}
	
	private static boolean is_num(char ch)
	{
		return (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5'
			|| ch == '6' || ch == '7' || ch == '8' || ch == '9');
	}
	
	//ȡ��function��()�ڵ�����
	private static String get_func_paras(String string) {
		int index = 0;
		int match = 1;   //matchΪ0��ʾ�������ƥ��
		while (index < string.length()) {
			if (string.charAt(index) == '(') {
				match++;
				index++;
			}
			else if (string.charAt(index) == ')') {
				match--;
				index++;
				if (match == 0)
					return string.substring(0,index - 1);
			}
			else
				index++;
		}
		return "";
	}
	
	private static String process_minus(String formula)
	{
		int index = 0;
		while (index < formula.length()) {
			index = formula.indexOf("-",index);
			//���������"-"�ж�Ϊ���ţ�ǰ���0��ʹ���Ϊ���š�
			if (index < 0)
				break;
			if (index == 0 || formula.substring(0,index).trim().endsWith(",")
					|| formula.substring(0,index).trim().endsWith("(")) {
				formula = formula.substring(0,index) + "0" + formula.substring(index);
				index += 2;
			}
			else
				index++;
		}
		return formula;
	}
}