
//============用于计算锚点位置==================

package spreadsheet;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;

import stored_data.Spreadsheet_Data;

public class Anchor_Pos {

	private static Map<String,Float> _colStartX_map = new HashMap<String,Float>();  //各列起始x坐标 
	private static float _nextColStartX = 0;  //下列起始x坐标
	private static float _currentRowStartY = 0;  //当前行起始y坐标

	private static int _colIndex = 1;
	private static float _rowHeightAdd = 0;
	private static String _tableName = "";
	private static final float _DEFcolWidth = (float)85.6926;  //默认单元格宽度(按图形比例2.267*37.8)
	
	
	private static void clear() {
		_colStartX_map.clear();
		_nextColStartX = 0;  
		_currentRowStartY = 0;  
		_colIndex = 1;
		_rowHeightAdd = 0;
		_tableName = "";
	}
	
	public static void process_start(String qName,Attributes atts){
		if (qName.equals("table:table")) {
			_tableName = atts.getValue("table:name");
		}
		else if(qName.equals("table:table-column")) {
			int repeat = 1;
			float colWidth = Spreadsheet_Data.getColumnWidth(atts.getValue("table:style-name"));
			if(atts.getValue("table:number-columns-repeated") != null) {
				repeat = Integer.valueOf(atts.getValue("table:number-columns-repeated"));
			}
			for(int i = 0; i < repeat; i++) {
				_nextColStartX += colWidth;
				_colIndex++;
				_colStartX_map.put(Cell_Address.to_col_addr(_colIndex), _nextColStartX);
			}
		}
		else if(qName.equals("table:table-row")) {			
			_currentRowStartY += _rowHeightAdd;
			int repeat = 1;
			float rowHeight = Spreadsheet_Data.getRowHeight(atts.getValue("table:style-name"));
			if(atts.getValue("table:number-rows-repeated") != null) {
				repeat = Integer.valueOf(atts.getValue("table:number-rows-repeated"));
			}
			_rowHeightAdd = rowHeight * repeat;
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("table:table")) {
			clear();
		}
	}
	
	public static String getTableName() {
		return _tableName;
	}
	
	public static float getCurrentRowStartY() {
		return _currentRowStartY;
	}
	
	public static float getColStartX(String colName) {
		if (!_colStartX_map.containsKey("A"))
			_colStartX_map.put("A", (float)0);
		if (!_colStartX_map.containsKey(colName)) {
			float colStartX = _nextColStartX + _DEFcolWidth * (colName_to_int(colName) - _colIndex + 1);
			_colStartX_map.put(colName,colStartX);
		}
		return _colStartX_map.get(colName);
	}
	
	//A->1,...,Z->26,AA->27,...
	public static int colName_to_int(String colName) {
		int colNum = 0;
		byte[] nameBytes = colName.getBytes();
		int n = nameBytes.length;
		for (int i = 0; i < n; i++) {
			colNum += Math.pow(26,n - 1 - i) * (nameBytes[i] - 65);
		}
		return colNum;
	}
}
