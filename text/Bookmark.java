package text;

import org.xml.sax.Attributes;

import spreadsheet.Cell_Address;

import convertor.IDGenerator;

/**
 * ����<text:bookmark> �� <uof:��ǩ>��ת����
 * 
 * @author xie
 *
 */
public class Bookmark {
	//the result
	private static String _bookmarks = "";
	
	
	public static String get_result(){
		String rst = "";
		
		if(!_bookmarks.equals("")){
			rst = "<uof:��ǩ��>" + _bookmarks + "</uof:��ǩ��>";
			_bookmarks = "";
		}
		
		return rst;
	}
	
	//add a bookmark for <table:named-expressions>
	public static void add_nex_bk(String bk){
		_bookmarks += bk;
	}
	
	//add a bookmark for toc
	public static void add_toc_bk(){
		String bk = "";
		
		bk = "<uof:��ǩ uof:����=\"" + IDGenerator.get_toc_bk_name() + "\">";
		bk += "<uof:�ı�λ�� ��:��������=\"" + IDGenerator.get_toc_bk_id() + "\"/>";
		bk += "</uof:��ǩ>";
		
		_bookmarks += bk;
	}
	
	//add a bookmark for Print_Area in spreadsheet
	public static void add_print_area(Attributes atts){
		String area = "";
		String bk = "";
		
		String pr = atts.getValue("table:print-ranges");
		if(pr != null){
			String taName = atts.getValue("table:name");
			area = "='" + taName + "'!" + Cell_Address.get_cell_range(pr);
			
			bk = "<uof:��ǩ uof:����=\"Print_Area\">";
			bk += "<uof:�������ʽ uof:��������=\"" + area + "\" uof:��������=\"" + taName +"\"/>";
			bk += "</uof:��ǩ>";
		}
		
		_bookmarks += bk;
	}
	
	//add a new bookmark
	public static void process(Attributes atts){
		String onebk = "";
		String name = atts.getValue("text:name");
		
		onebk += "<uof:��ǩ uof:����=\"" + name + "\">";
		onebk += "<uof:�ı�λ�� ��:��������=\"" + name + "\"/>";
		onebk += "</uof:��ǩ>";
			
		_bookmarks += onebk;
	}	
}
