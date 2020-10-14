package spreadsheet;

import org.xml.sax.Attributes;
import stored_data.*;
import styles.Table_Row;
import styles.Table_Column;
import styles.Table_Style;

/**
 * ���� <table:table> �� <��:������>��ת����
 * 
 * @author xie
 *
 */
public class Sheet_Table {
	private static String _result = "";
	
	private static String _table_name = "";				//��:��ʶ��
	private static int _row_num = 0;					//�кż���
	private static int _col_num = 0;					//��������
	private static String _table_groups = "";			//���鼯
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
		
		view += "<��:��ͼ ��:���ڱ�ʶ��=\"0\">";
		view += "<��:ѡ�� ��:ֵ=\"true\"/>";
		view += "<��:������>1</��:������>";
		view += "<��:������>1</��:������>";
		view += "<��:��ʽ ��:ֵ=\"false\"/>";
		view += "<��:���� ��:ֵ=\"true\"/>";
		view += "<��:����>1.0</��:����>";
		view += "<��:��ҳ����>0.6</��:��ҳ����>";
		view += "<��:ѡ������>$A$1</��:ѡ������>";
		view += "</��:��ͼ>";
		
		return view;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		String styleName = "";
		
		if(qName.equals("table:table")){
			styleName = atts.getValue("table:style-name");
			_table_name = atts.getValue("table:name");
			Table_Cell.set_table_name(_table_name);
			
			_result += "<��:������";
			_result += " ��:��ʶ��=\"" + _table_name + "\"";
			_result += " ��:����=\"" + _table_name + "\"";
			if(Table_Style.get_sheet_att(styleName)!=null){
				_result += Table_Style.get_sheet_att(styleName);
			}
			_result += ">";

			_result += "<��:����������>";
			_result += Page_Layout_S.get_result() + get_view();
			_result += "</��:����������>";
			_result += "<��:���������� ��:ȱʡ�и�=\"14.25\" ��:ȱʡ�п�=\"64.3545\">";	
			
			//���ê��
			if (Content_Data.get_spreadsheet_anchors(_table_name) != null){
				_result += Content_Data.get_spreadsheet_anchors(_table_name);
			}
		}
		
		else if(qName.equals("table:table-row")){
			//reset 
			Table_Cell.see_new_row();
			
			styleName = atts.getValue("table:style-name");
			
			_row_num ++;			
			_result += "<��:�� uof:locID=\"s0049\"";
			_result += " uof:attrList=\"�к� ���� �и� ʽ������ ���\"";
			_result += " ��:�к�=\"" + _row_num + "\"";
			//��:����
			if((attVal=atts.getValue("table:visibility")) != null){
				if(!(attVal.equals("visible"))){
					_result += " ��:����=\"true\"";
				}
			}
			//��:�и�
			if(Table_Row.get_row_height(styleName) != null){
				_result += Table_Row.get_row_height(styleName);
			}
			//��:���
			if((attVal=atts.getValue("table:number-rows-repeated")) != null){
				_result += " ��:���=\"" + attVal + "\"";
				_row_num += Integer.parseInt(attVal) - 1;
			}
			_result += ">";
		}
		
		else if(qName.equals("table:table-column")){
			styleName = atts.getValue("table:style-name");
			
			_col_num++;
			_result += "<��:�� uof:locID=\"s0048\"";
			_result += " uof:attrList=\"�к� ���� �п� ʽ������ ���\"";
			_result += " ��:�к�=\"" + _col_num + "\"";
			//��:����
			if((attVal=atts.getValue("table:visibility")) != null){
				if(!(attVal.equals("visible"))){
					_result += " ��:����=\"true\"";
				}
			}
			//��:�п�
			if(Table_Column.get_sheet_width(styleName) != null){
				_result += Table_Column.get_sheet_width(styleName);
			}
			//��:ʽ������
			if((attVal=atts.getValue("table:default-cell-style-name")) != null){
				if(!Spreadsheet_Data.in_map_styles(attVal)){
					_result += " ��:ʽ������=\"" + attVal + "\"";
				}
			}
			//��:���
			if((attVal=atts.getValue("table:number-columns-repeated")) != null){
				_result += " ��:���=\"" + attVal + "\"";
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
			_table_groups += "<��:��";
			if((attVal=atts.getValue("table:display"))!=null){
				_table_groups += " ��:����=\"" + attVal + "\"";
			}
			_table_groups += " ��:��ʼ=\"" + (_col_num+1) + "\"" + " ��:��ֹ=/>";
														//�˴���<��:��ֹ>��δ��ֵ,���´���
		}										
		
		else if(qName.equals("table:table-row-group")){
			_table_groups += "<��:��";
			if((attVal=atts.getValue("table:display"))!=null){
				_table_groups += " ��:����=\"" + attVal + "\"";
			}
			_table_groups += " ��:��ʼ=\"" + (_row_num+1) + "\"" + " ��:��ֹ=/>";
														//�˴���<��:��ֹ>��δ��ֵ,���´���
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
				_result += "<��:���鼯>";
				_result += _table_groups;
				_result += "</��:���鼯>";
			}
			
			//���ͼ��
			String chart = Spreadsheet_Data.get_charts(_table_name);
			_result += (chart==null) ? "" : chart;
			
			_result += "</��:����������>";
			
			if(Spreadsheet_Data.get_filter(_table_name) != null){				
				_result += Spreadsheet_Data.get_filter(_table_name);
			}
			_result += "</��:������>";
		}
		
		else if(qName.equals("table:table-row")){
			_result += "</��:��>";
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
			//�������Ա�:��ֹ �� ֵ
			_table_groups = insert_endNum(_table_groups, _col_num);	
		}
		
		else if(qName.equals("table:table-row-group")){
			//�������Ա�:��ֹ �� ֵ
			_table_groups = insert_endNum(_table_groups, _row_num);	
		}	
	}
	
	private static String insert_endNum(String groups, int num){
		int index = groups.lastIndexOf("��:��ֹ=/>");
		StringBuffer strBuf = new StringBuffer(groups);
		
		if(index == -1)		return groups;
		index = index + new String("��:��ֹ=/>").length() - 2;
		strBuf.insert(index, "\""+num+"\"");
		
		return strBuf.toString();
	}
}
