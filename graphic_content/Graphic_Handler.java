package graphic_content;

import org.xml.sax.Attributes;

import presentation.Draw_Page;
import presentation.Presentation_Page_Layout;
import presentation.Presentation_Style;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import stored_data.*;
import convertor.IDGenerator;
import styles.*;
import tables.Draw_Type_Table;
import text.Text_Content;
import text.Text_P;
import convertor.Unit_Converter;
import spreadsheet.Anchor_Pos;

public class Graphic_Handler {	
	private static float _width = 0;   //��ȵ�����ֵ
	private static float _height = 0;   //�߶ȵ�����ֵ
	
	private static float _svgX = 0; 
	private static float _svgY = 0;
	private static float _endX = 0;

	//viewBox����Ϣ
	private static int _view_box_left = 0;
	private static int _view_box_top = 0;
	private static int _view_box_right = 0;
	private static int _view_box_bottom = 0;
	private static int _view_box_width = 0;
	private static int _view_box_height = 0;
	
	private static float _current_x = 0;  //path�е�ǰ�������,ȡ����ֵ
	private static float _current_y = 0;

	private static String _frame_type = "";   			//����<draw:frame>����Ԫ��ʱ��Ҫ�ж�
	private static String _current_drawing_id = "";   	//��ǰͼ��ID
//	private static String _current_formula_id = "";  	//��ǰ��ѧ��ʽID
//	private static String _current_otherobj_id = "";   	//��ǰ��������ID
	private static String _pre_uofAnchor_id = "";   //����presentation�е�uof:ê��
	private static boolean _is_anchor_set = false;   	//��ʶ<uof:ê��>�Ƿ�������
	
	private static String _geo_width = "";   //����custom-shape,enhanced_geometry
	private static String _geo_height = "";
	
	private static boolean _in_enhanced_geo = false;
	
	private static String _text_content = ""; 	 		//����ı�����
	private static boolean _text_mark = false;
	
	private static int _group_level = 0;   				//��ʶ�Ƿ���draw:g��,�Լ��ڼ���Ƕ����
	private static Stack<String> _group_stack = new Stack<String>();   //�洢��ǰgroup�ڵ�drawing ids.
	private static Stack<String> _g_id_stack = new Stack<String>();   //�洢��ǰgroup��drawing id
	
	private static Vector<String> _sheet_g_include = new Vector<String>();   //spreadsheet�������draw:g�µ���Ԫ�ص�drawing id�б�
	private static float _g_eCellX = 0;  //group����cell��x����
	private static float _g_sCellY = 0;  //group��ʼcell��y����
	
	private static String _text_anchor_id = "";
	
	//���ڼ�¼���ʱ��ê��λ�á���С
	private static float _group_x_s = -1;   //"x_start"
	private static float _group_y_s = -1;
	private static float _group_x_e = -1;   //"x_end"
	private static float _group_y_e = -1;
	
	private static float _uofAnchorX = 0;
	private static float _uofAnchorY = 0;
	private static int _g_chart_num = 0;  //number of charts within outmost <draw:g>
	private static boolean _tableshapes_tag = false;
	private static String _presen_class = "";
	
	public static void init(){
		_width = 0; 
		_height = 0; 
		_svgX = 0; 
		_svgY = 0;
		_endX = 0;
		_view_box_left = 0;
		_view_box_top = 0;
		 _view_box_right = 0;
		 _view_box_bottom = 0;
		 _view_box_width = 0;
		 _view_box_height = 0;	
		 _current_x = 0; 
		 _current_y = 0;
		_frame_type = ""; 
		 _current_drawing_id = ""; 
		 _pre_uofAnchor_id = ""; 
		 _is_anchor_set = false; 
		
		_geo_width = ""; 
		_geo_height = "";	
		_in_enhanced_geo = false;	
		_text_content = ""; 
		_text_mark = false;	
		_group_level = 0; 
		_group_stack.clear();
		_g_id_stack.clear();
		
		_sheet_g_include.clear();
		_g_eCellX = 0; 
		_g_sCellY = 0; 
		_text_anchor_id = "";
		_group_x_s = -1; 
		_group_y_s = -1;
		_group_x_e = -1; 
		_group_y_e = -1;
		_uofAnchorX = 0;
		_uofAnchorY = 0;
		_g_chart_num = 0;
		_tableshapes_tag = false;
		_presen_class = "";
	}
	
	public static void process_start(String qName,Attributes atts){
		String value = "";
		
		if (_text_mark) {
			Text_Content.process_start(qName,atts);
		} 
		
		if (_in_enhanced_geo){
			Enhanced_Geo_Handler.process_start(qName,atts);
		}
		
		if (qName.equals("draw:rect")||qName.equals("draw:line")||qName.equals("draw:polygon")
				||qName.equals("draw:regular-polygon")||qName.equals("draw:circle")
				||qName.equals("draw:ellipse")||qName.equals("draw:connector")
				||qName.equals("draw:caption")||qName.equals("draw:custom-shape")
				||qName.equals("draw:g") || qName.equals("draw:polyline")
				||qName.equals("draw:path")) {				
			_text_mark = true;
			Common_Data.set_draw_text_tag(true);
			
			Drawing drawing = new Drawing();
			
			//��ͼ��ID
			String drawingID = IDGenerator.get_drawing_id();
			drawing.set_id(drawingID);
			_current_drawing_id = drawingID;
			
			if (qName.equals("draw:g")) {
				if (Common_Data.get_file_type().equals("spreadsheet")) {
					if (_group_level == 0) {
						_endX = Unit_Converter.convert_gra(atts.getValue("table:end-x"));
						String endCell = atts.getValue("table:end-cell-address");
						int index = endCell.indexOf(".");
						String colName = endCell.substring(index + 1, index + 2);
						_g_eCellX = Anchor_Pos.getColStartX(colName);	
						_g_sCellY = Anchor_Pos.getCurrentRowStartY();
					}
					else
						_sheet_g_include.add(drawingID);
				}
				_g_id_stack.push(drawingID);
				_group_stack.push("g");  //���Ա�ʶ��ǰgroup
				_group_level++;
			}
			else calculateGeo(atts);
			
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
			
			if (atts.getValue("svg:x1") != null) {   //Line�����
				float x1 = Unit_Converter.convert_gra(atts.getValue("svg:x1"));
				float x2 = Unit_Converter.convert_gra(atts.getValue("svg:x2"));
				float y1 = Unit_Converter.convert_gra(atts.getValue("svg:y1"));
				float y2 = Unit_Converter.convert_gra(atts.getValue("svg:y2"));
				if (x1 > x2 && y1 < y2 || x1 < x2 && y1 > y2) {			
					drawing.set_overturn("<ͼ:��ת ͼ:����=\"y\"/>");
				}
				
			}
			
			if (_group_level > 0 && !qName.equals("draw:g")){
				_group_stack.push(drawingID);
				if (!Common_Data.get_file_type().equals("spreadsheet"))
					drawing.set_group_pos("<ͼ:���λ�� ͼ:x����=\"" + _svgX + "\" ͼ:y����=\"" + _svgY + "\"/>");
				else {
					_sheet_g_include.add(drawingID);
					drawing.set_sheet_gPos(Float.valueOf(_svgX), Float.valueOf(_svgY));
				}
			}
			
			if (qName.equals("draw:custom-shape")) {
				if (atts.getValue("svg:width") != null)
					_geo_width = atts.getValue("svg:width");
				if (atts.getValue("svg:height") != null)
					_geo_height = atts.getValue("svg:height");
			}
			
			//��<ͼ:ͼ��>Ԫ�����������.
			if (atts.getValue("draw:z-index") != null) {
				drawing.add_begin_element(" ͼ:���=\"" + atts.getValue("draw:z-index") + "\"");
			}
			drawing.add_begin_element(" ͼ:��ʶ��=\"" + drawingID + "\"");  
			
			//�洢<Ԥ����ͼ��>�е�<���>��<����>
			if (qName.equals("draw:g")) {
				drawing.add_type("<ͼ:���>11</ͼ:���>");
				drawing.add_name("<ͼ:����>Rectangle</ͼ:����>");
			}
			else if (qName.equals("draw:path")) {
				drawing.add_type("<ͼ:���>64</ͼ:���>");
				drawing.add_name("<ͼ:����>Curve</ͼ:����>");	
				String path = calculate_path(atts);
				drawing.add_key_coors("<ͼ:�ؼ������� ͼ:·��=\"" + path + "\"/>");
			}
			else if (qName.equals("draw:rect")) {
				if ((value = atts.getValue("draw:corner-radius")) != null && !value.equals("0")) {
					drawing.add_type("<ͼ:���>15</ͼ:���>");
					drawing.add_name("<ͼ:����>Rounded Rectangle</ͼ:����>");
				}
				else {
					drawing.add_type("<ͼ:���>11</ͼ:���>");
					drawing.add_name("<ͼ:����>Rectangle</ͼ:����>");
				}
			}
			else if (qName.equals("draw:line")) {
				drawing.add_type("<ͼ:���>61</ͼ:���>");
				drawing.add_name("<ͼ:����>Line</ͼ:����>");	
			}
			else if (qName.equals("draw:polyline")) {
				drawing.add_type("<ͼ:���>64</ͼ:���>");
				drawing.add_name("<ͼ:����>Curve</ͼ:����>");	
				String polyline = calculate_polyline(atts);
				drawing.add_key_coors("<ͼ:�ؼ������� ͼ:·��=\"" + polyline + "\"/>");
			}
			else if (qName.equals("draw:polygon")) {
				drawing.add_type("<ͼ:���>65</ͼ:���>");
				drawing.add_name("<ͼ:����>Freeform</ͼ:����>");	
			}
			else if (qName.equals("draw:regular-polygon")) {
				if ((value = atts.getValue("draw:concave")) != null) {
					int corners = 0;
					if (atts.getValue("draw:corners") != null)
						corners = Integer.valueOf(atts.getValue("draw:corners"));
					//͹�������
					if (value.equals("false")) {
						if (corners == 8) {
							drawing.add_type("<ͼ:���>16</ͼ:���>");
							drawing.add_name("<ͼ:����>Octagon</ͼ:����>");
						}
						if (corners == 6) {
							drawing.add_type("<ͼ:���>110</ͼ:���>");
							drawing.add_name("<ͼ:����>Hexagon</ͼ:����>");
						}
						if (corners == 5) {
							drawing.add_type("<ͼ:���>112</ͼ:���>");
							drawing.add_name("<ͼ:����>Regular Pentagon</ͼ:����>");
						}
					}
					//���������
					else {
						if (corners == 8) {
							drawing.add_type("<ͼ:���>43</ͼ:���>");
							drawing.add_name("<ͼ:����>4-Point Star</ͼ:����>");
						}
						if (corners == 10) {
							drawing.add_type("<ͼ:���>44</ͼ:���>");
							drawing.add_name("<ͼ:����>5-Point Star</ͼ:����>");
						}
						if (corners == 16) {
							drawing.add_type("<ͼ:���>45</ͼ:���>");
							drawing.add_name("<ͼ:����>8-Point Star</ͼ:����>");
						}
						if (corners == 32) {
							drawing.add_type("<ͼ:���>46</ͼ:���>");
							drawing.add_name("<ͼ:����>16-Point Star</ͼ:����>");
						}
						if (corners == 48) {
							drawing.add_type("<ͼ:���>47</ͼ:���>");
							drawing.add_name("<ͼ:����>24-Point Star</ͼ:����>");
						}
						if (corners == 64) {
							drawing.add_type("<ͼ:���>48</ͼ:���>");
							drawing.add_name("<ͼ:����>32-Point Star</ͼ:����>");
						}
					}
				}
			}
			else if (qName.equals("draw:circle") || qName.equals("draw:ellipse")) {
				if ((value=atts.getValue("draw:kind"))!=null && value.equals("arc")){
					drawing.add_type("<ͼ:���>125</ͼ:���>");
					drawing.add_name("<ͼ:����>Arc</ͼ:����>");
				}
				else{
					drawing.add_type("<ͼ:���>19</ͼ:���>");
					drawing.add_name("<ͼ:����>Oval</ͼ:����>");
				}
				//circle��ellipse�����������δ����
			}
			else if (qName.equals("draw:connector")) {
				value = atts.getValue("draw:type");
				if (value.equals("standard") || value.equals("lines")) {
					drawing.add_type("<ͼ:���>74</ͼ:���>");
					drawing.add_name("<ͼ:����>Elbow Connector</ͼ:����>");
				}
				else if (value.equals("line")) {
					drawing.add_type("<ͼ:���>71</ͼ:���>");
					drawing.add_name("<ͼ:����>Straight Connector</ͼ:����>");
				}
				else if (value.equals("curve")) {
					drawing.add_type("<ͼ:���>77</ͼ:���>");
					drawing.add_name("<ͼ:����>Curved Connector</ͼ:����>");
				}	
			}
			else if (qName.equals("draw:caption")) {
				if ((value = atts.getValue("draw:corner-radius")) != null && !value.equals("0")) {
					drawing.add_type("<ͼ:���>52</ͼ:���>");
					drawing.add_name("<ͼ:����>Rounded Rectangular Callout</ͼ:����>");
				}
				else {
					drawing.add_type("<ͼ:���>51</ͼ:���>");
					drawing.add_name("<ͼ:����>Rectangular Callout</ͼ:����>");
				}
			}	
			//To do.��Ӷ�����ͼ�����͵Ĵ���
			
			//����ʽ�������ҵ���Ӧ��GraphicPro���洢<����>�еĿ�ȡ��߶ȡ���ת�Ƕȡ����ű�������Ԫ�ء�
			Graphic_Pro graphicpro = new Graphic_Pro();
			if ((value = atts.getValue("draw:style-name")) != null) {
				value = Style_Data.rename(value);
				graphicpro = Style_Data.get_graphic_pro(value);
				if (graphicpro.get_hasfill())
					Enhanced_Geo_Handler.set_hasfill(true);
				if (graphicpro.get_hasstroke())
					Enhanced_Geo_Handler.set_hasstroke(true);
			}
			graphicpro.process_draw_atts(atts);
			
			//�����draw:id����洢�����Թ�����ʱ����
			//To do.��δ��������RefID�����
			if ((value = atts.getValue("draw:id")) != null) {
				drawing.set_ref_id(value);
			}
			
			//��Ԥ����ͼ�ε����Ժ�<�ı�����>Ԫ��ȡ����д��Drawingʵ���С�������洢��drawingSet�С�
//			=====SPECIAL for OOo2  draw:polyline========	
			if (qName.equals("draw:polyline"))
				graphicpro.clear_padding();
//			============================================
			
			//��������ö���ʽ��������ܴӸö���ʽ������ȡ��һЩ����д��<�ı�����>Ԫ��
			String lmargin = graphicpro.getLeftMargin(),
			       rmargin = graphicpro.getRightMargin(),
			       tmargin = graphicpro.getTopMargin(),
			       bmargin = graphicpro.getBottomMargin(),
			       wmode = graphicpro.getWritingMode();
			if ((value = atts.getValue("draw:text-style-name")) != null) {
				value = Style_Data.rename(value);
				if(Style_Data.get_paraGraPro_part(value,"leftMargin").length() > 0)
					lmargin = Style_Data.get_paraGraPro_part(value,"leftMargin");
				if(Style_Data.get_paraGraPro_part(value,"rightMargin").length() > 0)
					rmargin = Style_Data.get_paraGraPro_part(value,"rightMargin");
				if(Style_Data.get_paraGraPro_part(value,"topMargin").length() > 0)
					tmargin = Style_Data.get_paraGraPro_part(value,"topMargin");
				if(Style_Data.get_paraGraPro_part(value,"bottomMargin").length() > 0)
					bmargin = Style_Data.get_paraGraPro_part(value,"bottomMargin");
				if(Style_Data.get_paraGraPro_part(value,"writingMode").length() > 0)
					wmode = Style_Data.get_paraGraPro_part(value,"writingMode");
			}
			drawing.add_text(lmargin + rmargin + tmargin + bmargin + wmode);
			
			drawing.add_text(graphicpro.get_text_begin());
			drawing.set_atts(graphicpro.get_drawing_pro_string());
			Content_Data.add_drawing(drawingID,drawing);
			
			//�洢ê�㡣
			if (_group_level == 0 || (_group_level == 1 && qName.equals("draw:g"))) {
				//�ĵ�����Ϊtextʱ��<��:ê��>
				//��Ҫȡ���洢��graphicpro�е�ĳЩ����д��textanchor
				if (Common_Data.get_file_type().equals("text")) {
					TextAnchor textanchor = new TextAnchor();
					_text_anchor_id = IDGenerator.get_text_anchor_id();
					textanchor.set_id(_text_anchor_id);
					textanchor.process_atts(atts);
					textanchor.set_anchor_atts(graphicpro.get_anchor_atts());
					if(graphicpro.get_anchortype().length() != 0) {
						textanchor.set_type(graphicpro.get_anchortype());
					}
					textanchor.add_drawing("<��:ͼ�� ��:ͼ������=\"" + drawingID + "\"/>");
					Content_Data.add_text_anchor(_text_anchor_id,textanchor);
				}
				//�ĵ�����Ϊspreadsheet��presentationʱ��ê����ʽΪ<uof:ê��>
				else if (Common_Data.get_file_type().equals("spreadsheet") && _group_level == 0){
					float uofAnchorX, uofAnchorY;
					if (_tableshapes_tag) {
						uofAnchorX = _svgX;
						uofAnchorY = _svgY;
					}
					else {
						if ((value = atts.getValue("table:end-x")) != null) {
							_endX = Unit_Converter.convert_gra(value);
						}
						String endCell = atts.getValue("table:end-cell-address");
						int index = endCell.indexOf(".");
						String colName = endCell.substring(index + 1, index + 2);
						float colStartX = Anchor_Pos.getColStartX(colName);	
						uofAnchorX = colStartX + _endX - _width;
						uofAnchorY = Anchor_Pos.getCurrentRowStartY() + _svgY;
					}
					
					String uofAnchor = "<uof:ê�� uof:x����=\"" + uofAnchorX + "\" uof:y����=\"" + uofAnchorY 
					+ "\" uof:���=\"" + _width + "\" uof:�߶�=\"" + _height
					+ "\" uof:ͼ������=\"" + _current_drawing_id + "\"/>";					
					String tableName = Anchor_Pos.getTableName();
					Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
				}
				else if (Common_Data.get_file_type().equals("presentation") && _group_level == 0) {  //presentation
					_pre_uofAnchor_id = IDGenerator.get_graphic_anchor_id();
					String uofAnchor = "<uof:ê�� uof:x����=\"" + _svgX + "\" uof:y����=\"" + _svgY 
					+ "\" uof:���=\"" + _width + "\" uof:�߶�=\"" + _height
					+ "\" uof:ͼ������=\"" + drawingID + "\"/>";
					Content_Data.add_presentation_anchor(_pre_uofAnchor_id,uofAnchor);
					Draw_Page.add_draw_id(_pre_uofAnchor_id,drawingID);
				}
			}
		}
		
		else if (qName.equals("draw:frame")) {	
			calculateGeo(atts);
			
			Graphic_Pro graphicpro = new Graphic_Pro();
			if ((value=atts.getValue("draw:style-name")) != null){
				value = Style_Data.rename(value);
				graphicpro = Style_Data.get_graphic_pro(value);
			}
			if (graphicpro==null &&
					(value=atts.getValue("presentation:style-name")) != null){
				value = Style_Data.rename(value);
				graphicpro = Style_Data.get_graphic_pro(value);
			}
			if (Common_Data.get_file_type().equals("text")) {
				//text��frame�϶���draw:style-name���ԣ�
				//�Ҹ��������õ�ʽ���϶���style:parent-style-name����
				_frame_type = graphicpro.get_parent_style();
				if (!_frame_type.equals("Frame"))
					graphicpro.set_linecolor("");
			}
			else if (Common_Data.get_file_type().equals("presentation") || graphicpro != null) {
				graphicpro.set_linecolor("");
				
				if((value=atts.getValue("presentation:class"))!=null){
					_presen_class = value;	
				}
				else {
					_presen_class = "";
				}
				Text_P.set_presen_class(_presen_class);
			}
				
			graphicpro.process_draw_atts(atts);
			graphicpro.clear_padding();
						
			if (!_frame_type.equals("Formula")/*�����������������*/) {
				Drawing drawing = new Drawing();
				String drawingID = IDGenerator.get_drawing_id();
				_current_drawing_id = drawingID;
				drawing.set_id(drawingID);
				drawing.add_begin_element(" ͼ:��ʶ��=\"" + drawingID + "\"");  
				if (atts.getValue("draw:z-index") != null) {
					drawing.add_begin_element(" ͼ:���=\"" + atts.getValue("draw:z-index") + "\"");
				}
				if ((value = atts.getValue("draw:id")) != null) {
					drawing.set_ref_id(value);
				}
				if (_group_level > 0){
					if (Common_Data.get_file_type().equals("spreadsheet")) {
						_group_stack.push(drawingID);
						_sheet_g_include.add(drawingID);
						float gX = Float.valueOf(Unit_Converter.convert_gra(atts.getValue("svg:x")));
						float gY = Float.valueOf(Unit_Converter.convert_gra(atts.getValue("svg:y")));
						drawing.set_sheet_gPos(gX, gY);
					}
					else if (Common_Data.get_file_type().equals("presentation")) {
						drawing.set_group_pos("<ͼ:���λ�� ͼ:x����=\"" + _svgX + "\" ͼ:y����=\"" + _svgY + "\"/>");
						_group_stack.push(drawingID);
					}
					else {
						//text
					}
				}
				
				String lmargin = graphicpro.getLeftMargin(),
			       rmargin = graphicpro.getRightMargin(),
			       tmargin = graphicpro.getTopMargin(),
			       bmargin = graphicpro.getBottomMargin(),
			       wmode = graphicpro.getWritingMode();
				if ((value = atts.getValue("draw:text-style-name")) != null) {
					value = Style_Data.rename(value);
					if(Style_Data.get_paraGraPro_part(value,"leftMargin").length() > 0)
						lmargin = Style_Data.get_paraGraPro_part(value,"leftMargin");
					if(Style_Data.get_paraGraPro_part(value,"rightMargin").length() > 0)
						rmargin = Style_Data.get_paraGraPro_part(value,"rightMargin");
					if(Style_Data.get_paraGraPro_part(value,"topMargin").length() > 0)
						tmargin = Style_Data.get_paraGraPro_part(value,"topMargin");
					if(Style_Data.get_paraGraPro_part(value,"bottomMargin").length() > 0)
						bmargin = Style_Data.get_paraGraPro_part(value,"bottomMargin");
					if(Style_Data.get_paraGraPro_part(value,"writingMode").length() > 0)
						wmode = Style_Data.get_paraGraPro_part(value,"writingMode");
				}
				drawing.add_text(lmargin + rmargin + tmargin + bmargin + wmode);
				
				drawing.add_text(graphicpro.get_text_begin());	
				drawing.set_atts(graphicpro.get_drawing_pro_string());
				drawing.add_type("<ͼ:���>11</ͼ:���>");
				drawing.add_name("<ͼ:����>Rectangle</ͼ:����>");
				Content_Data.add_drawing(drawingID,drawing);
			}
			
			if (Common_Data.get_file_type().equals("text")) {					
				//�ı�����Ϊtextʱ���洢һ��<��:ê��>��
				TextAnchor textanchor = new TextAnchor();
				_text_anchor_id = IDGenerator.get_text_anchor_id();
				textanchor.set_id(_text_anchor_id);
				textanchor.process_atts(atts);
				textanchor.set_anchor_atts(graphicpro.get_anchor_atts());
				if(graphicpro.get_anchortype().length() != 0) {
					textanchor.set_type(graphicpro.get_anchortype());
				}
				
				//�������ʽ����parent-styleΪFrame��OLE��Graphics����洢һ��<ͼ:ͼ��>�����󼯣������ê������
				//ΪFrameʱ�����ı������ں�������<draw:text-box>ʱ��ӡ�
				//ΪOLE��Graphicsʱ���ں�������<draw:image>ʱ�洢һ��<��������>/<����>����ͼ�ε�"��������"��������
				if (_frame_type.equals("Frame") || _frame_type.equals("OLE") || _frame_type.equals("Graphics"))
					textanchor.add_drawing("<��:ͼ�� ��:ͼ������=\"" + _current_drawing_id + "\"/>");
/*				//parent-styleΪFormula���ݲ�����
				else if (_frame_type.equals("Formula")) {
					String formulaID = IDGenerator.get_formula_id();
					_current_formula_id = formulaID;
					textanchor.add_drawing("<��:ͼ�� ��:ͼ������=\"" + formulaID + "\"/>");
				}*/
				//??�Ƿ������������
				
				Content_Data.add_text_anchor(_text_anchor_id,textanchor);
			}
			else if (Common_Data.get_file_type().equals("spreadsheet")) {	
				if (_group_level == 0) {
					//�洢frame��x,y���꣬��spreadsheet�������chart�Ļ����õ�
					if ((value = atts.getValue("table:end-x")) != null) {
						_endX = Unit_Converter.convert_gra(value);
					}
					String endCell = atts.getValue("table:end-cell-address");
					int index = endCell.indexOf(".");
					String colName = endCell.substring(index + 1, index + 2);
					float colStartX = Anchor_Pos.getColStartX(colName);	
					_uofAnchorX = colStartX + _endX - _width;
					_uofAnchorY = Anchor_Pos.getCurrentRowStartY() + _svgY;
					Chart.set_frameX(_uofAnchorX);
					Chart.set_frameY(_uofAnchorY);		
				}
				else {  //_group_level > 0
					//Only keep relative position here.Absolute position will be calculated later.
					Chart.set_frameX(_svgX);
					Chart.set_frameY(_svgY);	
				}
			}
			else {  //presentation.				
				if (_group_level == 0) {
					String placeholder = "";
					if ((value = atts.getValue("presentation:class")) != null)
						placeholder = value;
					_pre_uofAnchor_id = IDGenerator.get_graphic_anchor_id();
					String uofAnchor = "<uof:ê�� uof:x����=\"" + _svgX + "\" uof:y����=\"" + _svgY 
					+ "\" uof:���=\"" + _width + "\" uof:�߶�=\"" + _height
					+ "\" uof:ͼ������=\"" + _current_drawing_id + "\"";
					if (placeholder.length() != 0)
						uofAnchor += " uof:ռλ��=\"" + Presentation_Page_Layout.conv_PH_type(placeholder) + "\"";
					uofAnchor += "/>";
					Content_Data.add_presentation_anchor(_pre_uofAnchor_id,uofAnchor);
					Draw_Page.add_draw_id(_pre_uofAnchor_id,_current_drawing_id);
				}
			}
		}
		
		//draw:text-box��ֻ�ܴ�����<draw:frame>�С�����Ӧ��ͼ�ζ����д洢ĳЩ��Ϣ��
		else if (qName.equals("draw:text-box")) {
			_text_mark = true;  
			Common_Data.set_draw_text_tag(true);
			String string = " ͼ:�ı���=\"true\"";
			if (atts.getValue("draw:chain-next-name") != null)
				string += (" ͼ:��һ����=\"" + atts.getValue("draw:chain-next-name") + "\"");
			Content_Data.get_drawing(_current_drawing_id).add_text(string);
			
			//�ı���߶�
			if (atts.getValue("fo:min-height") != null) {
				float height = Unit_Converter.convert_gra(atts.getValue("fo:min-height"));
				
				String anchorHeight = "<��:�߶� uof:locID=\"t0113\">" + height + "</��:�߶�>";	
				String anchorAtts = Content_Data.get_text_anchor(_text_anchor_id).get_anchor_atts();
				int i = anchorAtts.indexOf("<��:λ��");
				anchorAtts = anchorAtts.substring(0, i) + anchorHeight + anchorAtts.substring(i);
				Content_Data.get_text_anchor(_text_anchor_id).set_anchor_atts(anchorAtts);
				
				String drawHeight = "<ͼ:�߶�>" + height + "</ͼ:�߶�>";
				String drawAtts = Content_Data.get_drawing(_current_drawing_id).get_atts();
				i = drawAtts.indexOf("<ͼ:���");
				drawAtts = drawAtts.substring(0, i) + drawHeight + drawAtts.substring(i);
				Content_Data.get_drawing(_current_drawing_id).set_atts(drawAtts);
			}
		} 
		
		else if (qName.equals("draw:object") && atts.getValue("xlink:href") != null) {			
			String xLink = atts.getValue("xlink:href").substring(1);
			
			if (Common_Data.get_file_type().equals("text") && _frame_type.equals("Formula")) {
/*				//��ȡ��ѧ��ʽ��������Ӧ����
				String formula = ObjectProcessor.process_formula(xLink);
				Content_Data.add_formula(_current_formula_id,formula);*/
			}
			else if (Common_Data.get_file_type().equals("spreadsheet")) {
				//spreadsheet����Ҫ��ȡͼ����ͼ�����ݺͶ�Ӧ��ê��ID��������ڵڶ���parseʱ��ȡ������
				if (Common_Data.get_file_type().equals("spreadsheet") && ObjectProcessor.get_obj_type(xLink).equals("chart")) {
					String chart = ObjectProcessor.process_chart(xLink);
					Spreadsheet_Data.add_chart(Anchor_Pos.getTableName(),chart);
					_is_anchor_set = true;
					_g_chart_num++;
				} 
				/*				else if (ObjectProcessor.get_obj_type(xLink).equals("formula")) {
				 String formulaID = IDGenerator.get_formula_id();
				 String formula = ObjectProcessor.process_formula(xLink);;
				 Content_Data.add_formula(formulaID,formula);
				 String graphicAnchor = "<uof:ê�� uof:x����=\"" + _svgX + "\" uof:y����=\"" + _svgY 
				 + "\" uof:���=\"" + _width_pt + "\" uof:�߶�=\"" + _height_pt
				 + "\" uof:ͼ������=\"" + _current_drawing_id + "\"/>";
				 Content_Data.add_graphic_anchor(_current_graphic_anchor_id,graphicAnchor);
				 _is_anchor_set = true;
				 }*/
			}
			else {
				//presentation
			}
		}
		
		else if (qName.equals("draw:image")) {	
			if (Common_Data.get_file_type().equals("text") && (_frame_type.equals("OLE") || _frame_type.equals("Graphics"))
					|| Common_Data.get_file_type().equals("spreadsheet") && !_is_anchor_set || Common_Data.get_file_type().equals("presentation")) {
				
				String href = atts.getValue("xlink:href");
				String otherobj_id = Media_Obj.process_href(href);
				
				Content_Data.get_drawing(_current_drawing_id).add_begin_element(" ͼ:��������=\""
						+ otherobj_id + "\"");
				
				//ͼ�ο����ɫҪ��Ϊ͸��
				String drawAtts = Content_Data.get_drawing(_current_drawing_id).get_atts();
				int i = drawAtts.indexOf("<ͼ:����ɫ");
				int j = drawAtts.indexOf("</ͼ:����ɫ");
				if(i > 0 && j > 0) {
					drawAtts = drawAtts.substring(0, i) + drawAtts.substring(j + 8);
					Content_Data.get_drawing(_current_drawing_id).set_atts(drawAtts);
				}
				
				if (Common_Data.get_file_type().equals("spreadsheet") && _group_level == 0) {		
					String uofAnchor = "<uof:ê�� uof:x����=\"" + _uofAnchorX + "\" uof:y����=\"" + _uofAnchorY 
					+ "\" uof:���=\"" + _width + "\" uof:�߶�=\"" + _height
					+ "\" uof:ͼ������=\"" + _current_drawing_id + "\"/>";					
					String tableName = Anchor_Pos.getTableName();
					Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
				}
			}
		}
		
		else if (qName.equals("draw:floating-frame")) {
			//To do.
		}
		
		//�洢Draw_Layer��draw:layer����ֻ����Ի�ͼ����ʾ�ĸ���У�λ��<master-styles>��<draw-layer-set>
		else if (qName.equals("draw:layer")) {
			DrawLayer layer = new DrawLayer();
			layer.process_atts(atts);
			layer.set_num(IDGenerator.get_draw_layer_id());
			Style_Data.add_draw_layer(atts.getValue("draw:name"),layer);
		}
		
		//����custom-shape����Ԫ��
		else if (qName.equals("draw:enhanced-geometry") && (value = atts.getValue("draw:type")) != null) {
			_in_enhanced_geo = true;
			float geoWidth = Unit_Converter.convert_gra(_geo_width);
			float geoHeight = Unit_Converter.convert_gra(_geo_height);
			Enhanced_Geo_Handler.set_paras(_current_drawing_id, geoWidth, geoHeight);
			
			String type = "";
			String name = "";
			String typeString = "";
			String nameString = "";
			
			type = Draw_Type_Table.get_draw_num(value);
			name = Draw_Type_Table.get_draw_name(value);
			if (type.length() != 0)
				typeString = "<ͼ:���>" + type + "</ͼ:���>";
			if (name.length() != 0)
				nameString = "<ͼ:����>" + name + "</ͼ:����>";		
			Content_Data.get_drawing(_current_drawing_id).add_type(typeString);
			Content_Data.get_drawing(_current_drawing_id).add_name(nameString);
			Enhanced_Geo_Handler.process_start(qName,atts);
		}
	}
	
	public static void process_end(String qName){
		if (_in_enhanced_geo){
			Enhanced_Geo_Handler.process_end(qName);
		}	
		if (qName.equals("draw:rect")||qName.equals("draw:line")||qName.equals("draw:polygon")
				||qName.equals("draw:regular-polygon")||qName.equals("draw:circle")
				||qName.equals("draw:ellipse")||qName.equals("draw:connector")
				||qName.equals("draw:caption")||qName.equals("draw:custom-shape")
				||qName.equals("draw:g") || qName.equals("draw:polyline")
				||qName.equals("draw:path")){			
			if (qName.equals("draw:g")) {
				float gx = -1, gy = -1;
				_current_drawing_id = _g_id_stack.pop();
				String groupStr = "";
				while (!_group_stack.empty()) {
					String id = _group_stack.pop();
					if (id.equals("g"))
						break;
					groupStr = id + " " + groupStr;
					Content_Data.get_drawing(_current_drawing_id).add_group_id(id);
					
					float x = Content_Data.get_drawing(id).get_gx();
					float y = Content_Data.get_drawing(id).get_gy();
					if (gx == -1 || gx > x)
						gx = x;
					if (gy == -1 || gy > y)
						gy = y;
				}
				groupStr = groupStr.trim();
				Content_Data.get_drawing(_current_drawing_id).add_begin_element(" ͼ:����б�=\"" + groupStr + "\"");

				_group_level--;
				if (_group_level > 0) { //���������group��������draw:g��idҲ����_group_stack
					_group_stack.push(_current_drawing_id);
					if (Common_Data.get_file_type().equals("spreadsheet")) { //spreadsheet��draw:g�����λ��Ҳ��Ҫ��
						Content_Data.get_drawing(_current_drawing_id).set_sheet_gPos(gx, gy);
					}
				}
				else  {   //_group_level == 0. Ҫ��ê��������Ӧ��Ϣ,ע�⣬�˴����ǵ�group�е���ͼ��Ԫ�ؿ���д���λ����Ϣ
					if (Common_Data.get_file_type().equals("text")) {
						String anchorAttStr = Content_Data.get_text_anchor(_text_anchor_id).get_anchor_atts();
						int index = anchorAttStr.indexOf("<��:λ��");
						String width_height = "<��:��� uof:locID=\"t0112\">" + (_group_x_e - _group_x_s) + "</��:���>"
						+ "<��:�߶� uof:locID=\"t0113\">" + (_group_y_e - _group_y_s) + "</��:�߶�>";
						anchorAttStr = anchorAttStr.substring(0,index) + width_height + anchorAttStr.substring(index);
						
						index = anchorAttStr.indexOf("</��:ˮƽ>");
						int index2 = anchorAttStr.indexOf("<��:��� uof:locID=\"t0178\"");
						String hori = "<��:���� uof:locID=\"t0177\" uof:attrList=\"ֵ\" ��:ֵ=\"" + _group_x_s + "\"/>";
						if (index2 < 0) {
							anchorAttStr = anchorAttStr.substring(0,index) + hori + anchorAttStr.substring(index);
						}
						else {
							anchorAttStr = anchorAttStr.substring(0,index2) + hori + anchorAttStr.substring(index);
						}
						
						index = anchorAttStr.indexOf("</��:��ֱ>");
						index2 = anchorAttStr.indexOf("<��:��� uof:locID=\"t0181\"");
						String vert = "<��:���� uof:locID=\"t0180\" uof:attrList=\"ֵ\" ��:ֵ=\"" + _group_y_s + "\"/>";
						if (index2 < 0) {
							anchorAttStr = anchorAttStr.substring(0,index) + vert + anchorAttStr.substring(index);
						}
						else {
							anchorAttStr = anchorAttStr.substring(0,index2) + vert + anchorAttStr.substring(index);
						}
						
						Content_Data.get_text_anchor(_text_anchor_id).set_anchor_atts(anchorAttStr);
					}
					else if (Common_Data.get_file_type().equals("spreadsheet")){
						float gWidth = _group_x_e - _group_x_s;
						float gHeight = _group_y_e - _group_y_s;
						float uofAnchorX = _g_eCellX + _endX - gWidth;
						float uofAnchorY = _g_sCellY + _group_y_s;
						
						String uofAnchor = "<uof:ê�� uof:x����=\"" + uofAnchorX + "\" uof:y����=\"" + uofAnchorY 
						+ "\" uof:���=\"" + gWidth + "\" uof:�߶�=\"" + gHeight
						+ "\" uof:ͼ������=\"" + _current_drawing_id + "\"/>";					
						String tableName = Anchor_Pos.getTableName();
						Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
						
						//��group��ÿ��ͼ��������λ��,except charts.
						for (Iterator iterator = _sheet_g_include.iterator(); iterator.hasNext(); ) {
							if (_tableshapes_tag)
								Content_Data.get_drawing((String)iterator.next())._cal_sheet_gPos(0, 0);
							else
								Content_Data.get_drawing((String)iterator.next())._cal_sheet_gPos(uofAnchorX - _group_x_s, uofAnchorY - _group_y_s);
						}
						
						//calculate charts' absolute position.
						if (_g_chart_num > 0) {
							String charts = Spreadsheet_Data.get_charts(Anchor_Pos.getTableName());
							String resultCharts = "";
							while (_g_chart_num > 0) {
								int i = charts.lastIndexOf("��:x����");
								int xs = charts.indexOf("\"", i);
								int xe = charts.indexOf("\"", xs + 1);
								float svgX = Float.valueOf(charts.substring(xs + 1, xe)) + uofAnchorX - _group_x_s;
								int j = charts.lastIndexOf("��:y����");
								int ys = charts.indexOf("\"", j);
								int ye = charts.indexOf("\"", ys + 1);
								float svgY = Float.valueOf(charts.substring(ys + 1, ye)) + uofAnchorY - _group_y_s;
								resultCharts = charts.substring(i, xs + 1) + svgX + charts.substring(xe, ys + 1)
								+ svgY + charts.substring(ye) + resultCharts;
								charts = charts.substring(0, i);
								_g_chart_num--;
							}
							resultCharts = charts + resultCharts;
							Spreadsheet_Data.set_charts(Anchor_Pos.getTableName(), resultCharts);
						}
						
						_sheet_g_include.clear();
						_g_eCellX = 0;
						_g_sCellY = 0;
					}
					else {
						//presentation
						_pre_uofAnchor_id = IDGenerator.get_graphic_anchor_id();
						String uofAnchor = "<uof:ê�� uof:x����=\"" + _group_x_s + "\" uof:y����=\"" + _group_y_s 
						+ "\" uof:���=\"" + (_group_x_e - _group_x_s) + "\" uof:�߶�=\"" + (_group_y_e - _group_y_s)
						+ "\" uof:ͼ������=\"" + _current_drawing_id + "\"/>";
						Content_Data.add_presentation_anchor(_pre_uofAnchor_id,uofAnchor);
						Draw_Page.add_draw_id(_pre_uofAnchor_id,_current_drawing_id);
					}
					_group_x_s = -1;
					_group_y_s = -1;
					_group_x_e = -1;
					_group_y_e = -1;
				}
			}
			else if (qName.equals("draw:custom-shape")) {
				_geo_width = "";
				_geo_height = "";
			}
			
			_text_content += Text_Content.get_result();
			Content_Data.get_drawing(_current_drawing_id).add_text(">" + _text_content + "</ͼ:�ı�����>");
			_text_content = "";
			_text_mark = false;
			Common_Data.set_draw_text_tag(false);
			_current_drawing_id = "";
			
			_width = 0;
			_height = 0;
			_view_box_left = 0;
			_view_box_top = 0;
			_view_box_right = 0;
			_view_box_bottom = 0;
			_view_box_width = 0;
			_view_box_height = 0;
			_current_x = 0;
			_current_y = 0;
		}
		//currentDrawingID����draw:frame�����õģ�����draw:text-box�Ĵ������в�ͬ
		else if (qName.equals("draw:text-box")) {
			_text_content += Text_Content.get_result();
			if(_text_content.equals("")){
				_text_content = empty_para(_presen_class);
			}
			Content_Data.get_drawing(_current_drawing_id).add_text(">" + _text_content + "</ͼ:�ı�����>");
			_text_content = "";
			_text_mark = false;
			Common_Data.set_draw_text_tag(false);
		}
		else if (qName.equals("draw:frame")) {	
			if (!Content_Data.get_drawing(_current_drawing_id).get_drawing_string().contains("</ͼ:�ı�")) {
				Content_Data.get_drawing(_current_drawing_id).add_text("/>");
			}
			
			Text_P.set_presen_class("");
			_frame_type = "";
			_current_drawing_id = "";
//			_current_formula_id = "";
			_is_anchor_set = false;
//			_otherobj_str = "";
		}
		else if (qName.equals("draw:enhanced-geometry")) {
			_in_enhanced_geo = false;
		}
		else if (_text_mark) {
			Text_Content.process_end(qName);
		}
	}
	
	public static void process_chars(String chs){	
		if(_text_mark){
			Text_Content.process_chars(chs);
		}
	}
	
	private static String calculate_polyline(Attributes atts) {		
		String polyline = "";
		String points = atts.getValue("draw:points");
		int index = points.indexOf(" ");
		String point = points.substring(0,index);
		points = points.substring(index + 1);
		index = point.indexOf(",");
		String xCoor = point.substring(0,index);
		String yCoor = point.substring(index + 1);
		polyline = "M " + get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
		while (points.contains(",")) {
			index = points.indexOf(" ");
			if (index < 0) {
				point = points;
				points = "";
			}
			else {
				point = points.substring(0,index);
				points = points.substring(index + 1);
			}
			index = point.indexOf(",");
			xCoor = point.substring(0,index);
			yCoor = point.substring(index + 1);
			polyline += "L " + get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
		}
		
		return polyline.trim();
	}
	
	private static String calculate_path(Attributes atts) {	
		String ins = "MmZzLlHhVvCcSsQqTtAa";
		String path = "";
		String pathdata = atts.getValue("svg:d");
		pathdata = pathdata.replace("-"," -");  //������"-"ǰ����Ͽո񣬱��ڴ���
		
		//�ֲ�svg:d��ÿ��ָ���������
		String insStr = "";
		int index1 = 0;
		int index2 = 1;
		while (index2 < pathdata.length()) {
			if (ins.contains(String.valueOf(pathdata.charAt(index2)))) {
				insStr = pathdata.substring(index1,index2);
				path += deal_with_ins(insStr);
				index1 = index2;
				index2++;
			}
			else
				index2++;
		}
		insStr = pathdata.substring(index1);
		path += deal_with_ins(insStr);
		
		return path.trim();
	}
	
	private static float get_x_pt(String xCoor) {
		return (Float.valueOf(xCoor) - _view_box_left)/_view_box_width * _width;
	}
	
	private static float get_y_pt(String yCoor) {
		return (Float.valueOf(yCoor) - _view_box_top)/_view_box_height * _height;
	}
	
	private static String deal_with_ins(String insStr) {
		String result = "";
		String xCoor = "";
		String yCoor = "";
		char insChar = insStr.charAt(0);   //ָ����
		if (insStr.trim().length() > 1) {
			String paraStr = insStr.substring(1).trim();   //ָ�����
			if (insChar == 'M' || insChar == 'm') {
				int index1 = 0, index2 = 0;
				index1 = paraStr.indexOf(" ");
				index2 = paraStr.indexOf(" ",index1 + 1);
				xCoor = paraStr.substring(0,index1);
				if (index2 < 0) {
					yCoor = paraStr.substring(index1 + 1);
					result += "M " + get_coors(insChar,xCoor,yCoor);
				}
				else {
					yCoor = paraStr.substring(index1 + 1,index2);
					result += "M " + get_coors(insChar,xCoor,yCoor);
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
						result += "L " + get_coors(insChar,xCoor,yCoor);
					}
				}
			}
			else if (insChar == 'L' || insChar == 'l') {
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
					result += "L " + get_coors(insChar,xCoor,yCoor);
				}
			}
			else if (insChar == 'H' || insChar == 'h') {
				if (insChar == 'H')
					yCoor = String.valueOf(_current_y);
				else
					yCoor = "0";
				
				int index = 0;
				while (index >= 0) {
					index = paraStr.indexOf(" ");
					if (index < 0) {
						xCoor = paraStr;
					}
					else {
						xCoor = paraStr.substring(0,index);
						paraStr = paraStr.substring(index + 1);
					}
					result += "L " + get_coors(insChar,xCoor,yCoor);
				}
			}
			else if (insChar == 'V' || insChar == 'v') {
				if (insChar == 'V')
					xCoor = String.valueOf(_current_y);
				else
					xCoor = "0";
				
				int index = 0;
				while (index >= 0) {
					index = paraStr.indexOf(" ");
					if (index < 0) {
						yCoor = paraStr;
					}
					else {
						yCoor = paraStr.substring(0,index);
						paraStr = paraStr.substring(index + 1);
					}
					result += "L " + get_coors(insChar,xCoor,yCoor);
				}
			}
			else if (insChar == 'C' || insChar == 'c') {
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
					if (insChar == 'C')
						result += get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
					else
						result += (_current_x + get_x_pt(xCoor)) + " " + (_current_y + get_y_pt(yCoor)) + " ";
					
					xCoor = paraStr.substring(index2 + 1,index3);
					yCoor = paraStr.substring(index3 + 1,index4);
					if (insChar == 'C')
						result += get_x_pt(xCoor) + " " + get_y_pt(yCoor) + " ";
					else
						result += (_current_x + get_x_pt(xCoor)) + " " + (_current_y + get_y_pt(yCoor)) + " ";
					
					xCoor = paraStr.substring(index4 + 1,index5);
					if (index6 < 0) {
						yCoor = paraStr.substring(index5 + 1);
					}
					else {
						yCoor = paraStr.substring(index5 + 1,index6);
						paraStr = paraStr.substring(index6 + 1);
					}
					result += get_coors(insChar,xCoor,yCoor);
				}
			}
			/*To do:��������ָ��
			 else if () {
			 
			 }
			 */
		}
		else 
			result = "Z";
		
		return result; 
	}
	
	private static String get_coors(char insChar, String xCoor, String yCoor) {
		String coors = "";
		if (insChar == 'M'||insChar == 'L'||insChar == 'H'
			||insChar == 'V'||insChar == 'C') {
			_current_x = get_x_pt(xCoor);
			_current_y = get_y_pt(yCoor);
		}
		else {
			_current_x = _current_x + get_x_pt(xCoor);
			_current_y = _current_y + get_y_pt(yCoor);
		}
		coors += _current_x + " " + _current_y + " ";
		
		return coors;
	}

	private static void calculateGeo(Attributes atts) {
		String value = "";
		
		if ((value = atts.getValue("svg:width")) != null)
			_width = Unit_Converter.convert_gra(value);
		else if ((value = atts.getValue("fo:min-width")) != null)
			_width = Unit_Converter.convert_gra(value);
		if ((value = atts.getValue("svg:height")) != null)
			_height = Unit_Converter.convert_gra(value);
		if (atts.getValue("svg:x") != null) {
			_svgX = Unit_Converter.convert_gra(atts.getValue("svg:x"));
			if (atts.getValue("svg:y") != null)
				_svgY = Unit_Converter.convert_gra(atts.getValue("svg:y"));
		}
		
		else if (atts.getValue("svg:x1") != null) {   //Line�����
			float x1 = Unit_Converter.convert_gra(atts.getValue("svg:x1"));
			float x2 = Unit_Converter.convert_gra(atts.getValue("svg:x2"));
			if (x1 >= x2) {
				_svgX = x2;
				_width = x1 - x2;
			}
			else {
				_svgX = x1;
				_width = x2 - x1;
			}
					
			float y1 = Unit_Converter.convert_gra(atts.getValue("svg:y1"));
			float y2 = Unit_Converter.convert_gra(atts.getValue("svg:y2"));
			if (y1 >= y2) {
				_svgY = y2;
				_height = y1 - y2;
			}
			else {
				_svgY = y1;
				_height = y2 - y1;
			}
		}
			
		if (_group_level > 0) {
			float x_s = _svgX;
			float y_s = _svgY;
			float x_e = x_s + _width;
			float y_e = y_s + _height;	
			if (_group_x_s < 0) {
				_group_x_s = x_s;
				_group_y_s = y_s;
				_group_x_e = x_e;
				_group_y_e = y_e;
			}
			else {
				if (x_s < _group_x_s)
					_group_x_s = x_s;
				if (y_s < _group_y_s)
					_group_y_s = y_s;
				if (x_e > _group_x_e)
					_group_x_e = x_e;
				if (y_e > _group_y_e)
					_group_y_e = y_e;
			}
		}
		
	}
	
/*	private static void add_sheet_anchor(Attributes atts) {
		String value = "";
		if ((value = atts.getValue("table:end-x")) != null) {
			_endX = Unit_Converter.convert_gra(value);
		}
		String endCell = atts.getValue("table:end-cell-address");
		int index = endCell.indexOf(".");
		String colName = endCell.substring(index + 1, index + 2);
		float colStartX = Anchor_Pos.getColStartX(colName);	
		float uofAnchorX = colStartX + _endX - _width;
		float uofAnchorY = Anchor_Pos.getCurrentRowStartY() + _svgY;
		
		String uofAnchor = "<uof:ê�� uof:x����=\"" + uofAnchorX + "\" uof:y����=\"" + uofAnchorY 
		+ "\" uof:���=\"" + _width + "\" uof:�߶�=\"" + _height
		+ "\" uof:ͼ������=\"" + _current_drawing_id + "\"/>";					
		String tableName = Anchor_Pos.getTableName();
		Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
	}*/
	
	public static void set_tableshapes_tag(boolean bool) {
		_tableshapes_tag = bool;
	}
	
	private static String empty_para(String presenCls){
		String para = "";
		String name = Presentation_Style.style_name(presenCls,0);
		
		para = "<��:����>";
		para += "<��:��������" + " ��:ʽ������=\"" + name + "\">";
		if(presenCls.equals("title")){
			para += "<��:��ټ���>0</��:��ټ���>";
			para += "<uof:ֹͣ����><uof:·�� uof:locID=\"u0067\">�Զ������Ϣ</uof:·��></uof:ֹͣ����>";
		}
		else {
			para += "<��:��ټ���>1</��:��ټ���>";
		}
		para += "</��:��������>";
		para += "<��:��><��:������/></��:��>";
		para += "</��:����>";
		
		return para;
	}
}
