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
	private static float _width = 0;   //宽度的像素值
	private static float _height = 0;   //高度的像素值
	
	private static float _svgX = 0; 
	private static float _svgY = 0;
	private static float _endX = 0;

	//viewBox的信息
	private static int _view_box_left = 0;
	private static int _view_box_top = 0;
	private static int _view_box_right = 0;
	private static int _view_box_bottom = 0;
	private static int _view_box_width = 0;
	private static int _view_box_height = 0;
	
	private static float _current_x = 0;  //path中当前点的坐标,取像素值
	private static float _current_y = 0;

	private static String _frame_type = "";   			//处理<draw:frame>的子元素时需要判断
	private static String _current_drawing_id = "";   	//当前图形ID
//	private static String _current_formula_id = "";  	//当前数学公式ID
//	private static String _current_otherobj_id = "";   	//当前其他对象ID
	private static String _pre_uofAnchor_id = "";   //用于presentation中的uof:锚点
	private static boolean _is_anchor_set = false;   	//标识<uof:锚点>是否已设置
	
	private static String _geo_width = "";   //用于custom-shape,enhanced_geometry
	private static String _geo_height = "";
	
	private static boolean _in_enhanced_geo = false;
	
	private static String _text_content = ""; 	 		//存放文本内容
	private static boolean _text_mark = false;
	
	private static int _group_level = 0;   				//标识是否在draw:g中,以及在几层嵌套中
	private static Stack<String> _group_stack = new Stack<String>();   //存储当前group内的drawing ids.
	private static Stack<String> _g_id_stack = new Stack<String>();   //存储当前group的drawing id
	
	private static Vector<String> _sheet_g_include = new Vector<String>();   //spreadsheet中最外层draw:g下的子元素的drawing id列表
	private static float _g_eCellX = 0;  //group结束cell的x坐标
	private static float _g_sCellY = 0;  //group开始cell的y坐标
	
	private static String _text_anchor_id = "";
	
	//用于记录组合时的锚点位置、大小
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
			
			//存图形ID
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
				_group_stack.push("g");  //用以标识当前group
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
			
			if (atts.getValue("svg:x1") != null) {   //Line的情况
				float x1 = Unit_Converter.convert_gra(atts.getValue("svg:x1"));
				float x2 = Unit_Converter.convert_gra(atts.getValue("svg:x2"));
				float y1 = Unit_Converter.convert_gra(atts.getValue("svg:y1"));
				float y2 = Unit_Converter.convert_gra(atts.getValue("svg:y2"));
				if (x1 > x2 && y1 < y2 || x1 < x2 && y1 > y2) {			
					drawing.set_overturn("<图:翻转 图:方向=\"y\"/>");
				}
				
			}
			
			if (_group_level > 0 && !qName.equals("draw:g")){
				_group_stack.push(drawingID);
				if (!Common_Data.get_file_type().equals("spreadsheet"))
					drawing.set_group_pos("<图:组合位置 图:x坐标=\"" + _svgX + "\" 图:y坐标=\"" + _svgY + "\"/>");
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
			
			//向<图:图形>元素中添加属性.
			if (atts.getValue("draw:z-index") != null) {
				drawing.add_begin_element(" 图:层次=\"" + atts.getValue("draw:z-index") + "\"");
			}
			drawing.add_begin_element(" 图:标识符=\"" + drawingID + "\"");  
			
			//存储<预定义图形>中的<类别>、<名称>
			if (qName.equals("draw:g")) {
				drawing.add_type("<图:类别>11</图:类别>");
				drawing.add_name("<图:名称>Rectangle</图:名称>");
			}
			else if (qName.equals("draw:path")) {
				drawing.add_type("<图:类别>64</图:类别>");
				drawing.add_name("<图:名称>Curve</图:名称>");	
				String path = calculate_path(atts);
				drawing.add_key_coors("<图:关键点坐标 图:路径=\"" + path + "\"/>");
			}
			else if (qName.equals("draw:rect")) {
				if ((value = atts.getValue("draw:corner-radius")) != null && !value.equals("0")) {
					drawing.add_type("<图:类别>15</图:类别>");
					drawing.add_name("<图:名称>Rounded Rectangle</图:名称>");
				}
				else {
					drawing.add_type("<图:类别>11</图:类别>");
					drawing.add_name("<图:名称>Rectangle</图:名称>");
				}
			}
			else if (qName.equals("draw:line")) {
				drawing.add_type("<图:类别>61</图:类别>");
				drawing.add_name("<图:名称>Line</图:名称>");	
			}
			else if (qName.equals("draw:polyline")) {
				drawing.add_type("<图:类别>64</图:类别>");
				drawing.add_name("<图:名称>Curve</图:名称>");	
				String polyline = calculate_polyline(atts);
				drawing.add_key_coors("<图:关键点坐标 图:路径=\"" + polyline + "\"/>");
			}
			else if (qName.equals("draw:polygon")) {
				drawing.add_type("<图:类别>65</图:类别>");
				drawing.add_name("<图:名称>Freeform</图:名称>");	
			}
			else if (qName.equals("draw:regular-polygon")) {
				if ((value = atts.getValue("draw:concave")) != null) {
					int corners = 0;
					if (atts.getValue("draw:corners") != null)
						corners = Integer.valueOf(atts.getValue("draw:corners"));
					//凸正多边形
					if (value.equals("false")) {
						if (corners == 8) {
							drawing.add_type("<图:类别>16</图:类别>");
							drawing.add_name("<图:名称>Octagon</图:名称>");
						}
						if (corners == 6) {
							drawing.add_type("<图:类别>110</图:类别>");
							drawing.add_name("<图:名称>Hexagon</图:名称>");
						}
						if (corners == 5) {
							drawing.add_type("<图:类别>112</图:类别>");
							drawing.add_name("<图:名称>Regular Pentagon</图:名称>");
						}
					}
					//凹正多边形
					else {
						if (corners == 8) {
							drawing.add_type("<图:类别>43</图:类别>");
							drawing.add_name("<图:名称>4-Point Star</图:名称>");
						}
						if (corners == 10) {
							drawing.add_type("<图:类别>44</图:类别>");
							drawing.add_name("<图:名称>5-Point Star</图:名称>");
						}
						if (corners == 16) {
							drawing.add_type("<图:类别>45</图:类别>");
							drawing.add_name("<图:名称>8-Point Star</图:名称>");
						}
						if (corners == 32) {
							drawing.add_type("<图:类别>46</图:类别>");
							drawing.add_name("<图:名称>16-Point Star</图:名称>");
						}
						if (corners == 48) {
							drawing.add_type("<图:类别>47</图:类别>");
							drawing.add_name("<图:名称>24-Point Star</图:名称>");
						}
						if (corners == 64) {
							drawing.add_type("<图:类别>48</图:类别>");
							drawing.add_name("<图:名称>32-Point Star</图:名称>");
						}
					}
				}
			}
			else if (qName.equals("draw:circle") || qName.equals("draw:ellipse")) {
				if ((value=atts.getValue("draw:kind"))!=null && value.equals("arc")){
					drawing.add_type("<图:类别>125</图:类别>");
					drawing.add_name("<图:名称>Arc</图:名称>");
				}
				else{
					drawing.add_type("<图:类别>19</图:类别>");
					drawing.add_name("<图:名称>Oval</图:名称>");
				}
				//circle和ellipse还有两种情况未处理。
			}
			else if (qName.equals("draw:connector")) {
				value = atts.getValue("draw:type");
				if (value.equals("standard") || value.equals("lines")) {
					drawing.add_type("<图:类别>74</图:类别>");
					drawing.add_name("<图:名称>Elbow Connector</图:名称>");
				}
				else if (value.equals("line")) {
					drawing.add_type("<图:类别>71</图:类别>");
					drawing.add_name("<图:名称>Straight Connector</图:名称>");
				}
				else if (value.equals("curve")) {
					drawing.add_type("<图:类别>77</图:类别>");
					drawing.add_name("<图:名称>Curved Connector</图:名称>");
				}	
			}
			else if (qName.equals("draw:caption")) {
				if ((value = atts.getValue("draw:corner-radius")) != null && !value.equals("0")) {
					drawing.add_type("<图:类别>52</图:类别>");
					drawing.add_name("<图:名称>Rounded Rectangular Callout</图:名称>");
				}
				else {
					drawing.add_type("<图:类别>51</图:类别>");
					drawing.add_name("<图:名称>Rectangular Callout</图:名称>");
				}
			}	
			//To do.添加对其他图形类型的处理。
			
			//根据式样引用找到相应的GraphicPro，存储<属性>中的宽度、高度、旋转角度、缩放比例等子元素。
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
			
			//如果有draw:id，则存储起来以供引用时查找
			//To do.尚未处理引用RefID的情况
			if ((value = atts.getValue("draw:id")) != null) {
				drawing.set_ref_id(value);
			}
			
			//把预定义图形的属性和<文本内容>元素取出来写到Drawing实例中。并将其存储到drawingSet中。
//			=====SPECIAL for OOo2  draw:polyline========	
			if (qName.equals("draw:polyline"))
				graphicpro.clear_padding();
//			============================================
			
			//如果有引用段落式样，则可能从该段落式样中提取出一些属性写入<文本内容>元素
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
			
			//存储锚点。
			if (_group_level == 0 || (_group_level == 1 && qName.equals("draw:g"))) {
				//文档类型为text时是<字:锚点>
				//需要取出存储在graphicpro中的某些属性写入textanchor
				if (Common_Data.get_file_type().equals("text")) {
					TextAnchor textanchor = new TextAnchor();
					_text_anchor_id = IDGenerator.get_text_anchor_id();
					textanchor.set_id(_text_anchor_id);
					textanchor.process_atts(atts);
					textanchor.set_anchor_atts(graphicpro.get_anchor_atts());
					if(graphicpro.get_anchortype().length() != 0) {
						textanchor.set_type(graphicpro.get_anchortype());
					}
					textanchor.add_drawing("<字:图形 字:图形引用=\"" + drawingID + "\"/>");
					Content_Data.add_text_anchor(_text_anchor_id,textanchor);
				}
				//文档类型为spreadsheet或presentation时，锚点形式为<uof:锚点>
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
					
					String uofAnchor = "<uof:锚点 uof:x坐标=\"" + uofAnchorX + "\" uof:y坐标=\"" + uofAnchorY 
					+ "\" uof:宽度=\"" + _width + "\" uof:高度=\"" + _height
					+ "\" uof:图形引用=\"" + _current_drawing_id + "\"/>";					
					String tableName = Anchor_Pos.getTableName();
					Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
				}
				else if (Common_Data.get_file_type().equals("presentation") && _group_level == 0) {  //presentation
					_pre_uofAnchor_id = IDGenerator.get_graphic_anchor_id();
					String uofAnchor = "<uof:锚点 uof:x坐标=\"" + _svgX + "\" uof:y坐标=\"" + _svgY 
					+ "\" uof:宽度=\"" + _width + "\" uof:高度=\"" + _height
					+ "\" uof:图形引用=\"" + drawingID + "\"/>";
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
				//text中frame肯定有draw:style-name属性，
				//且该属性引用的式样肯定有style:parent-style-name属性
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
						
			if (!_frame_type.equals("Formula")/*有无其他特殊情况？*/) {
				Drawing drawing = new Drawing();
				String drawingID = IDGenerator.get_drawing_id();
				_current_drawing_id = drawingID;
				drawing.set_id(drawingID);
				drawing.add_begin_element(" 图:标识符=\"" + drawingID + "\"");  
				if (atts.getValue("draw:z-index") != null) {
					drawing.add_begin_element(" 图:层次=\"" + atts.getValue("draw:z-index") + "\"");
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
						drawing.set_group_pos("<图:组合位置 图:x坐标=\"" + _svgX + "\" 图:y坐标=\"" + _svgY + "\"/>");
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
				drawing.add_type("<图:类别>11</图:类别>");
				drawing.add_name("<图:名称>Rectangle</图:名称>");
				Content_Data.add_drawing(drawingID,drawing);
			}
			
			if (Common_Data.get_file_type().equals("text")) {					
				//文本类型为text时，存储一个<字:锚点>。
				TextAnchor textanchor = new TextAnchor();
				_text_anchor_id = IDGenerator.get_text_anchor_id();
				textanchor.set_id(_text_anchor_id);
				textanchor.process_atts(atts);
				textanchor.set_anchor_atts(graphicpro.get_anchor_atts());
				if(graphicpro.get_anchortype().length() != 0) {
					textanchor.set_type(graphicpro.get_anchortype());
				}
				
				//如果引用式样的parent-style为Frame、OLE或Graphics，则存储一个<图:图形>到对象集，供这个锚点引用
				//为Frame时，其文本内容在后面遇到<draw:text-box>时添加。
				//为OLE或Graphics时，在后面遇到<draw:image>时存储一个<其他对象>/<数据>供该图形的"其他对象"属性引用
				if (_frame_type.equals("Frame") || _frame_type.equals("OLE") || _frame_type.equals("Graphics"))
					textanchor.add_drawing("<字:图形 字:图形引用=\"" + _current_drawing_id + "\"/>");
/*				//parent-style为Formula，暂不考虑
				else if (_frame_type.equals("Formula")) {
					String formulaID = IDGenerator.get_formula_id();
					_current_formula_id = formulaID;
					textanchor.add_drawing("<字:图形 字:图形引用=\"" + formulaID + "\"/>");
				}*/
				//??是否还有其他情况？
				
				Content_Data.add_text_anchor(_text_anchor_id,textanchor);
			}
			else if (Common_Data.get_file_type().equals("spreadsheet")) {	
				if (_group_level == 0) {
					//存储frame的x,y坐标，在spreadsheet中如果是chart的话会用到
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
					String uofAnchor = "<uof:锚点 uof:x坐标=\"" + _svgX + "\" uof:y坐标=\"" + _svgY 
					+ "\" uof:宽度=\"" + _width + "\" uof:高度=\"" + _height
					+ "\" uof:图形引用=\"" + _current_drawing_id + "\"";
					if (placeholder.length() != 0)
						uofAnchor += " uof:占位符=\"" + Presentation_Page_Layout.conv_PH_type(placeholder) + "\"";
					uofAnchor += "/>";
					Content_Data.add_presentation_anchor(_pre_uofAnchor_id,uofAnchor);
					Draw_Page.add_draw_id(_pre_uofAnchor_id,_current_drawing_id);
				}
			}
		}
		
		//draw:text-box，只能存在于<draw:frame>中。往相应的图形对象中存储某些信息。
		else if (qName.equals("draw:text-box")) {
			_text_mark = true;  
			Common_Data.set_draw_text_tag(true);
			String string = " 图:文本框=\"true\"";
			if (atts.getValue("draw:chain-next-name") != null)
				string += (" 图:后一链接=\"" + atts.getValue("draw:chain-next-name") + "\"");
			Content_Data.get_drawing(_current_drawing_id).add_text(string);
			
			//文本框高度
			if (atts.getValue("fo:min-height") != null) {
				float height = Unit_Converter.convert_gra(atts.getValue("fo:min-height"));
				
				String anchorHeight = "<字:高度 uof:locID=\"t0113\">" + height + "</字:高度>";	
				String anchorAtts = Content_Data.get_text_anchor(_text_anchor_id).get_anchor_atts();
				int i = anchorAtts.indexOf("<字:位置");
				anchorAtts = anchorAtts.substring(0, i) + anchorHeight + anchorAtts.substring(i);
				Content_Data.get_text_anchor(_text_anchor_id).set_anchor_atts(anchorAtts);
				
				String drawHeight = "<图:高度>" + height + "</图:高度>";
				String drawAtts = Content_Data.get_drawing(_current_drawing_id).get_atts();
				i = drawAtts.indexOf("<图:宽度");
				drawAtts = drawAtts.substring(0, i) + drawHeight + drawAtts.substring(i);
				Content_Data.get_drawing(_current_drawing_id).set_atts(drawAtts);
			}
		} 
		
		else if (qName.equals("draw:object") && atts.getValue("xlink:href") != null) {			
			String xLink = atts.getValue("xlink:href").substring(1);
			
			if (Common_Data.get_file_type().equals("text") && _frame_type.equals("Formula")) {
/*				//提取数学公式，更新相应内容
				String formula = ObjectProcessor.process_formula(xLink);
				Content_Data.add_formula(_current_formula_id,formula);*/
			}
			else if (Common_Data.get_file_type().equals("spreadsheet")) {
				//spreadsheet中需要提取图表，将图表内容和对应的锚点ID相关联，在第二遍parse时提取出来。
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
				 String graphicAnchor = "<uof:锚点 uof:x坐标=\"" + _svgX + "\" uof:y坐标=\"" + _svgY 
				 + "\" uof:宽度=\"" + _width_pt + "\" uof:高度=\"" + _height_pt
				 + "\" uof:图形引用=\"" + _current_drawing_id + "\"/>";
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
				
				Content_Data.get_drawing(_current_drawing_id).add_begin_element(" 图:其他对象=\""
						+ otherobj_id + "\"");
				
				//图形框的颜色要设为透明
				String drawAtts = Content_Data.get_drawing(_current_drawing_id).get_atts();
				int i = drawAtts.indexOf("<图:线颜色");
				int j = drawAtts.indexOf("</图:线颜色");
				if(i > 0 && j > 0) {
					drawAtts = drawAtts.substring(0, i) + drawAtts.substring(j + 8);
					Content_Data.get_drawing(_current_drawing_id).set_atts(drawAtts);
				}
				
				if (Common_Data.get_file_type().equals("spreadsheet") && _group_level == 0) {		
					String uofAnchor = "<uof:锚点 uof:x坐标=\"" + _uofAnchorX + "\" uof:y坐标=\"" + _uofAnchorY 
					+ "\" uof:宽度=\"" + _width + "\" uof:高度=\"" + _height
					+ "\" uof:图形引用=\"" + _current_drawing_id + "\"/>";					
					String tableName = Anchor_Pos.getTableName();
					Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
				}
			}
		}
		
		else if (qName.equals("draw:floating-frame")) {
			//To do.
		}
		
		//存储Draw_Layer，draw:layer好像只是针对绘图和演示文稿才有？位于<master-styles>的<draw-layer-set>
		else if (qName.equals("draw:layer")) {
			DrawLayer layer = new DrawLayer();
			layer.process_atts(atts);
			layer.set_num(IDGenerator.get_draw_layer_id());
			Style_Data.add_draw_layer(atts.getValue("draw:name"),layer);
		}
		
		//处理custom-shape的子元素
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
				typeString = "<图:类别>" + type + "</图:类别>";
			if (name.length() != 0)
				nameString = "<图:名称>" + name + "</图:名称>";		
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
				Content_Data.get_drawing(_current_drawing_id).add_begin_element(" 图:组合列表=\"" + groupStr + "\"");

				_group_level--;
				if (_group_level > 0) { //如果在其他group内则把这个draw:g的id也放入_group_stack
					_group_stack.push(_current_drawing_id);
					if (Common_Data.get_file_type().equals("spreadsheet")) { //spreadsheet的draw:g的组合位置也需要算
						Content_Data.get_drawing(_current_drawing_id).set_sheet_gPos(gx, gy);
					}
				}
				else  {   //_group_level == 0. 要往锚点增加相应信息,注意，此处覆盖掉group中的子图形元素可能写入的位置信息
					if (Common_Data.get_file_type().equals("text")) {
						String anchorAttStr = Content_Data.get_text_anchor(_text_anchor_id).get_anchor_atts();
						int index = anchorAttStr.indexOf("<字:位置");
						String width_height = "<字:宽度 uof:locID=\"t0112\">" + (_group_x_e - _group_x_s) + "</字:宽度>"
						+ "<字:高度 uof:locID=\"t0113\">" + (_group_y_e - _group_y_s) + "</字:高度>";
						anchorAttStr = anchorAttStr.substring(0,index) + width_height + anchorAttStr.substring(index);
						
						index = anchorAttStr.indexOf("</字:水平>");
						int index2 = anchorAttStr.indexOf("<字:相对 uof:locID=\"t0178\"");
						String hori = "<字:绝对 uof:locID=\"t0177\" uof:attrList=\"值\" 字:值=\"" + _group_x_s + "\"/>";
						if (index2 < 0) {
							anchorAttStr = anchorAttStr.substring(0,index) + hori + anchorAttStr.substring(index);
						}
						else {
							anchorAttStr = anchorAttStr.substring(0,index2) + hori + anchorAttStr.substring(index);
						}
						
						index = anchorAttStr.indexOf("</字:垂直>");
						index2 = anchorAttStr.indexOf("<字:相对 uof:locID=\"t0181\"");
						String vert = "<字:绝对 uof:locID=\"t0180\" uof:attrList=\"值\" 字:值=\"" + _group_y_s + "\"/>";
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
						
						String uofAnchor = "<uof:锚点 uof:x坐标=\"" + uofAnchorX + "\" uof:y坐标=\"" + uofAnchorY 
						+ "\" uof:宽度=\"" + gWidth + "\" uof:高度=\"" + gHeight
						+ "\" uof:图形引用=\"" + _current_drawing_id + "\"/>";					
						String tableName = Anchor_Pos.getTableName();
						Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
						
						//向group中每个图形添加组合位置,except charts.
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
								int i = charts.lastIndexOf("表:x坐标");
								int xs = charts.indexOf("\"", i);
								int xe = charts.indexOf("\"", xs + 1);
								float svgX = Float.valueOf(charts.substring(xs + 1, xe)) + uofAnchorX - _group_x_s;
								int j = charts.lastIndexOf("表:y坐标");
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
						String uofAnchor = "<uof:锚点 uof:x坐标=\"" + _group_x_s + "\" uof:y坐标=\"" + _group_y_s 
						+ "\" uof:宽度=\"" + (_group_x_e - _group_x_s) + "\" uof:高度=\"" + (_group_y_e - _group_y_s)
						+ "\" uof:图形引用=\"" + _current_drawing_id + "\"/>";
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
			Content_Data.get_drawing(_current_drawing_id).add_text(">" + _text_content + "</图:文本内容>");
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
		//currentDrawingID是在draw:frame里设置的，所以draw:text-box的处理略有不同
		else if (qName.equals("draw:text-box")) {
			_text_content += Text_Content.get_result();
			if(_text_content.equals("")){
				_text_content = empty_para(_presen_class);
			}
			Content_Data.get_drawing(_current_drawing_id).add_text(">" + _text_content + "</图:文本内容>");
			_text_content = "";
			_text_mark = false;
			Common_Data.set_draw_text_tag(false);
		}
		else if (qName.equals("draw:frame")) {	
			if (!Content_Data.get_drawing(_current_drawing_id).get_drawing_string().contains("</图:文本")) {
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
		pathdata = pathdata.replace("-"," -");  //在所有"-"前面加上空格，便于处理
		
		//分拆svg:d的每条指令，逐条处理
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
		char insChar = insStr.charAt(0);   //指令名
		if (insStr.trim().length() > 1) {
			String paraStr = insStr.substring(1).trim();   //指令参数
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
			/*To do:处理其他指令
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
		
		else if (atts.getValue("svg:x1") != null) {   //Line的情况
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
		
		String uofAnchor = "<uof:锚点 uof:x坐标=\"" + uofAnchorX + "\" uof:y坐标=\"" + uofAnchorY 
		+ "\" uof:宽度=\"" + _width + "\" uof:高度=\"" + _height
		+ "\" uof:图形引用=\"" + _current_drawing_id + "\"/>";					
		String tableName = Anchor_Pos.getTableName();
		Content_Data.add_spreadsheet_anchor(tableName,uofAnchor);
	}*/
	
	public static void set_tableshapes_tag(boolean bool) {
		_tableshapes_tag = bool;
	}
	
	private static String empty_para(String presenCls){
		String para = "";
		String name = Presentation_Style.style_name(presenCls,0);
		
		para = "<字:段落>";
		para += "<字:段落属性" + " 字:式样引用=\"" + name + "\">";
		if(presenCls.equals("title")){
			para += "<字:大纲级别>0</字:大纲级别>";
			para += "<uof:停止引用><uof:路径 uof:locID=\"u0067\">自动编号信息</uof:路径></uof:停止引用>";
		}
		else {
			para += "<字:大纲级别>1</字:大纲级别>";
		}
		para += "</字:段落属性>";
		para += "<字:句><字:句属性/></字:句>";
		para += "</字:段落>";
		
		return para;
	}
}
