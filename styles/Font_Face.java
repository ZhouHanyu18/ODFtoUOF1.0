package styles;

import java.util.Set;
import java.util.TreeSet;
import org.xml.sax.Attributes;

/**
 * 处理<office:font-face-decls> 到 <uof:字体集>的转换。
 * 
 * @author xie
 *
 */
public class Font_Face {
	//the result
	private static String _fonts = "";
	//font name
	private static String _font_name = "";
	//font family
	private static String _font_family = "";
	//to guarantee no duplicate font
	private static Set<String> _font_set = new TreeSet<String>();
		
	
	public static String get_result(){
		String rst = "";
		
		rst = "<uof:字体集>";
		rst += _fonts;
		rst += "</uof:字体集>";
		
		_fonts = "";
		_font_set.clear();
		return rst;
	}
	
	//add a new font
	public static void add_font(String name,String family){
		String font = "";

		if(name.equals("")){
			name = family;
		}
		
		if(family.equals("")||_font_set.contains(name+family)){
			return ;
		}
		
		_font_set.add(name + family);
		
		font = "<uof:字体声明";
		font += " uof:标识符=\"" + name + "\"";
		font += " uof:名称=\"" + name + "\"";
		font += " uof:字体族=\"" + family + "\"";
		font += "/>";
		_fonts += font;
	}
	
	public static void process_atts(Attributes atts){
		String attVal = "";
		
		attVal = atts.getValue("style:name");
		_font_name = (attVal==null) ? "" : attVal;
		
		attVal = atts.getValue("svg:font-family");
		_font_family = (attVal==null) ? "" : attVal;
		
		add_font(_font_name, _font_family);
		_font_name = "";
		_font_family = "";
	}
}
