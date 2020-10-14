package text;

import org.xml.sax.Attributes;

import spreadsheet.Cell_Address;

import convertor.IDGenerator;

/**
 * 处理<text:bookmark> 到 <uof:书签>的转换。
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
			rst = "<uof:书签集>" + _bookmarks + "</uof:书签集>";
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
		
		bk = "<uof:书签 uof:名称=\"" + IDGenerator.get_toc_bk_name() + "\">";
		bk += "<uof:文本位置 字:区域引用=\"" + IDGenerator.get_toc_bk_id() + "\"/>";
		bk += "</uof:书签>";
		
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
			
			bk = "<uof:书签 uof:名称=\"Print_Area\">";
			bk += "<uof:命名表达式 uof:行列区域=\"" + area + "\" uof:工作表名=\"" + taName +"\"/>";
			bk += "</uof:书签>";
		}
		
		_bookmarks += bk;
	}
	
	//add a new bookmark
	public static void process(Attributes atts){
		String onebk = "";
		String name = atts.getValue("text:name");
		
		onebk += "<uof:书签 uof:名称=\"" + name + "\">";
		onebk += "<uof:文本位置 字:区域引用=\"" + name + "\"/>";
		onebk += "</uof:书签>";
			
		_bookmarks += onebk;
	}	
}
