package graphic_content;

import org.xml.sax.Attributes;

import spreadsheet.Cell_Address;

public class Chart_Local_Table {
	
	private static String _sheet_name = "";
	private static int _begin_col = 0;
	private static int _end_col = 0;
	private static int _begin_row = 0;
	private static int _end_row = 0;
	
	private static boolean _is_header_row = false;
	private static String _text = "";
	private static int _rows = 0;  //local table的行数，通过与起始行之差的比较可以判断是否一首行、首列为数据标志
	private static String[] _header_row = null;
	private static String[] _header_col = null;
	
	private static int _col_index = -1;
	private static int _row_index = -1;
	
	private static String _series_type = "";
	
	public static void set_area(String sheetName, int beginCol, int endCol, int beginRow, int endRow) {
		_sheet_name = sheetName;
		_begin_col = beginCol;
		_end_col = endCol;
		_begin_row = beginRow;
		_end_row = endRow;
		
		_header_row = new String[_end_col - _begin_col + 2];
		_header_col = new String[_end_row - _begin_row + 2];
	}
	
	public static void process_start(String qName,Attributes atts)
	{
		if (qName.equals("table:table-header-rows")) {
			_is_header_row = true;
		}
		else if (qName.equals("table:table-row")) {
			_rows++;
			_row_index++;
		}
		else if (qName.equals("table:table-cell")) {
			_col_index++;
		}
	}
	
	public static void process_end(String qName)
	{
		if (qName.equals("table:table")) {
			int offset = 0;
			if (Chart.get_type().equals("chart:scatter"))  //xy图较特殊,少第一个系列
				offset = 1;
			int rowNum = _end_row - _begin_row + 1;
			int colNum = _end_col - _begin_col + 1;
			if (_rows > rowNum) {  //首行、首列不作为数据标志
				if (_series_type.equals("col")) {
					for(int i = offset; i < colNum; i++) {
						String serie = "<表:系列 表:系列名=\"" + _header_row[i + 1] + "\" 表:系列值=\"='"
						+ _sheet_name + "'!" + Cell_Address.to_col_addr(_begin_col + i + 1) + _begin_row
						+ ":" + Cell_Address.to_col_addr(_begin_col + i + 1) + _end_row +"\"/>";
						Chart.add_data_source(serie);
					}
				}
				else {  //row
					for(int i = offset; i < rowNum; i++) {
						String serie = "<表:系列 表:系列名=\"" + _header_col[i + 1] + "\" 表:系列值=\"='"
						+ _sheet_name + "'!" + Cell_Address.to_col_addr(_begin_col + 1) + (_begin_row + i)
						+ ":" + Cell_Address.to_col_addr(_end_col + 1) + (_begin_row + i) +"\"/>";
						Chart.add_data_source(serie);
					}
				}
			}
			else {
				if (_series_type.equals("col")) {
					for(int i = offset; i < colNum - 1; i++) {
						String serie = "<表:系列 表:系列名=\"='" + _sheet_name + "'!"
						+ Cell_Address.to_col_addr(_begin_col + i + 2) + _begin_row + ":"
						+ Cell_Address.to_col_addr(_begin_col + i + 2) + _begin_row + "\" 表:系列值=\"='"
						+ _sheet_name + "'!" + Cell_Address.to_col_addr(_begin_col + i + 2) + (_begin_row + 1)
						+ ":" + Cell_Address.to_col_addr(_begin_col + i + 2) + _end_row + "\" 表:分类名=\"='" 
						+ _sheet_name + "'!" + Cell_Address.to_col_addr(_begin_col + 1) + (_begin_row + 1)
						+ ":" + Cell_Address.to_col_addr(_begin_col + 1) + _end_row + "\"/>";
						Chart.add_data_source(serie);
					}
				}
				else {  //row
					for(int i = offset; i < colNum - 1; i++) {
						String serie = "<表:系列 表:系列名=\"='" + _sheet_name + "'!"
						+ Cell_Address.to_col_addr(_begin_col + 1)  + (_begin_row + i + 1) + ":"
						+ Cell_Address.to_col_addr(_begin_col + 1)  + (_begin_row + i + 1) + "\" 表:系列值=\"='"
						+ _sheet_name + "'!" + Cell_Address.to_col_addr(_begin_col + 2) + (_begin_row + i + 1)
						+ ":" + Cell_Address.to_col_addr(_end_col + 1) + (_begin_row + i + 1)
						+"\" 表:分类名=\"='" + _sheet_name + "'!" + Cell_Address.to_col_addr(_begin_col + 2) + _begin_row
						+ ":" + Cell_Address.to_col_addr(_end_col + 1) + _begin_row + "\"/>";
						Chart.add_data_source(serie);
					}
				}
			}
			
			clear();
		}
		else if (qName.equals("table:table-header-rows")) {
			_is_header_row = false;
		}
		else if (qName.equals("table:table-row")) {
			_col_index = -1;
		}
		else if (qName.equals("table:table-cell")) {
			if (_is_header_row) {
				_header_row[_col_index] = _text;
			}
			
			if (_col_index == 0) {
				_header_col[_row_index] = _text;
			}
		}
	}
	
	public static void process_chars(char[] ch, int start, int length) 
	{
		_text = new String(ch,start,length);
	}
	
	private static void clear() {
		_sheet_name = "";
		_begin_col = 0;
		_end_col = 0;
		_begin_row = 0;
		_end_row = 0;
		_is_header_row = false;
		_text = "";
		_rows = 0;
		_header_row = null;
		_header_col = null;
		_col_index = -1;
		_row_index = -1;
		_series_type = "";
	}
	
	public static void set_series_type(String type) {
		_series_type = type;
	}
}
