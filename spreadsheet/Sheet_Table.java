package spreadsheet;

import org.xml.sax.Attributes;
import stored_data.*;
import styles.Table_Row;
import styles.Table_Column;
import styles.Table_Style;

/**
 * 处理 <table:table> 到 <表:工作表>的转换。
 * 
 * @author xie
 *
 */
public class Sheet_Table {
	private static String _result = "";
	
	private static String _table_name = "";				//表:标识符
	private static int _row_num = 0;					//行号记数
	private static int _col_num = 0;					//列数记数
	private static String _table_groups = "";			//分组集
	private static boolean _cell_tag = false;
	
	
	public static String get_result(){
		String str = _result;		
		clear();		
		return str;
	}
	
	private static void clear(){
		_result = "";
		_table_name = "";
		_row_num = 0;
		_col_num = 0;
		_table_groups = "";
	}
	
	private static String get_view(){
		String view = "";
		
		view += "<表:视图 表:窗口标识符=\"0\">";
		view += "<表:选中 表:值=\"true\"/>";
		view += "<表:最上行>1</表:最上行>";
		view += "<表:最左列>1</表:最左列>";
		view += "<表:公式 表:值=\"false\"/>";
		view += "<表:网格 表:值=\"true\"/>";
		view += "<表:缩放>1.0</表:缩放>";
		view += "<表:分页缩放>0.6</表:分页缩放>";
		view += "<表:选中区域>$A$1</表:选中区域>";
		view += "</表:视图>";
		
		return view;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		String styleName = "";
		
		if(qName.equals("table:table")){
			styleName = atts.getValue("table:style-name");
			_table_name = atts.getValue("table:name");
			Table_Cell.set_table_name(_table_name);
			
			_result += "<表:工作表";
			_result += " 表:标识符=\"" + _table_name + "\"";
			_result += " 表:名称=\"" + _table_name + "\"";
			if(Table_Style.get_sheet_att(styleName)!=null){
				_result += Table_Style.get_sheet_att(styleName);
			}
			_result += ">";

			_result += "<表:工作表属性>";
			_result += Page_Layout_S.get_result() + get_view();
			_result += "</表:工作表属性>";
			_result += "<表:工作表内容 表:缺省行高=\"14.25\" 表:缺省列宽=\"64.3545\">";	
			
			//输出锚点
			if (Content_Data.get_spreadsheet_anchors(_table_name) != null){
				_result += Content_Data.get_spreadsheet_anchors(_table_name);
			}
		}
		
		else if(qName.equals("table:table-row")){
			//reset 
			Table_Cell.see_new_row();
			
			styleName = atts.getValue("table:style-name");
			
			_row_num ++;			
			_result += "<表:行 uof:locID=\"s0049\"";
			_result += " uof:attrList=\"行号 隐藏 行高 式样引用 跨度\"";
			_result += " 表:行号=\"" + _row_num + "\"";
			//表:隐藏
			if((attVal=atts.getValue("table:visibility")) != null){
				if(!(attVal.equals("visible"))){
					_result += " 表:隐藏=\"true\"";
				}
			}
			//表:行高
			if(Table_Row.get_row_height(styleName) != null){
				_result += Table_Row.get_row_height(styleName);
			}
			//表:跨度
			if((attVal=atts.getValue("table:number-rows-repeated")) != null){
				_result += " 表:跨度=\"" + attVal + "\"";
				_row_num += Integer.parseInt(attVal) - 1;
			}
			_result += ">";
		}
		
		else if(qName.equals("table:table-column")){
			styleName = atts.getValue("table:style-name");
			
			_col_num++;
			_result += "<表:列 uof:locID=\"s0048\"";
			_result += " uof:attrList=\"列号 隐藏 列宽 式样引用 跨度\"";
			_result += " 表:列号=\"" + _col_num + "\"";
			//表:隐藏
			if((attVal=atts.getValue("table:visibility")) != null){
				if(!(attVal.equals("visible"))){
					_result += " 表:隐藏=\"true\"";
				}
			}
			//表:列宽
			if(Table_Column.get_sheet_width(styleName) != null){
				_result += Table_Column.get_sheet_width(styleName);
			}
			//表:式样引用
			if((attVal=atts.getValue("table:default-cell-style-name")) != null){
				if(!Spreadsheet_Data.in_map_styles(attVal)){
					_result += " 表:式样引用=\"" + attVal + "\"";
				}
			}
			//表:跨度
			if((attVal=atts.getValue("table:number-columns-repeated")) != null){
				_result += " 表:跨度=\"" + attVal + "\"";
				_col_num += Integer.parseInt(attVal) - 1;
			}

			_result += "/>";
		}
		
		else if(_cell_tag){
			Table_Cell.process_start(qName, atts);
		}
		else if(qName.equals("table:table-cell")
				||qName.equals("table:covered-table-cell")){
			_cell_tag = true;
			Table_Cell.process_start(qName, atts);
		}
		
		else if(qName.equals("table:table-column-group")){
			_table_groups += "<表:列";
			if((attVal=atts.getValue("table:display"))!=null){
				_table_groups += " 表:隐藏=\"" + attVal + "\"";
			}
			_table_groups += " 表:起始=\"" + (_col_num+1) + "\"" + " 表:终止=/>";
														//此处的<表:终止>尚未赋值,等下处理
		}										
		
		else if(qName.equals("table:table-row-group")){
			_table_groups += "<表:行";
			if((attVal=atts.getValue("table:display"))!=null){
				_table_groups += " 表:隐藏=\"" + attVal + "\"";
			}
			_table_groups += " 表:起始=\"" + (_row_num+1) + "\"" + " 表:终止=/>";
														//此处的<表:终止>尚未赋值,等下处理
		}
	}
	
	public static void process_chars(String chs){
		if(_cell_tag){
			Table_Cell.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		
		if(qName.equals("table:table")){
			if(_table_groups.length() != 0){
				_result += "<表:分组集>";
				_result += _table_groups;
				_result += "</表:分组集>";
			}
			
			//输出图表
			String chart = Spreadsheet_Data.get_charts(_table_name);
			_result += (chart==null) ? "" : chart;
			
			_result += "</表:工作表内容>";
			
			if(Spreadsheet_Data.get_filter(_table_name) != null){				
				_result += Spreadsheet_Data.get_filter(_table_name);
			}
			_result += "</表:工作表>";
		}
		
		else if(qName.equals("table:table-row")){
			_result += "</表:行>";
		}
		
		else if(qName.equals("table:table-cell")
				||qName.equals("table:covered-table-cell")){
			_cell_tag = false;
			_result += Table_Cell.get_result();
		}
		else if(_cell_tag){
			Table_Cell.process_end(qName);
		}
		
		else if(qName.equals("table:table-column-group")){
			//插入属性表:终止 的 值
			_table_groups = insert_endNum(_table_groups, _col_num);	
		}
		
		else if(qName.equals("table:table-row-group")){
			//插入属性表:终止 的 值
			_table_groups = insert_endNum(_table_groups, _row_num);	
		}	
	}
	
	private static String insert_endNum(String groups, int num){
		int index = groups.lastIndexOf("表:终止=/>");
		StringBuffer strBuf = new StringBuffer(groups);
		
		if(index == -1)		return groups;
		index = index + new String("表:终止=/>").length() - 2;
		strBuf.insert(index, "\""+num+"\"");
		
		return strBuf.toString();
	}
}
