package text;

import java.net.URI;
import org.xml.sax.Attributes;
import convertor.IDGenerator;

/**
 * 处理<text:a> 到 <uof:超级链接>的转换。
 * 
 * @author xie
 *
 */
public class Hyperlink {
	//the result
	private static String _links = "";
	//text style name for hyperlink in <spreadsheet>
	private static String _link_style_name = "T_Link";
	
	
	public static String get_result(){
		String rst = "";
		
		if(!_links.equals("")){
			rst = "<uof:链接集>";
			rst += _links;
			rst += "</uof:链接集>";
			_links = "";
		}	
		
		return rst;
	}
	
	//create the text style for hyperlink
	public static String link_style(){
		String ls = "";
		
		ls += "<uof:句式样";
		ls += " 字:标识符=\"" + _link_style_name + "\"";
		ls += " 字:名称=\"" + _link_style_name + "\"";
		ls += " 字:类型=\"auto\">";
		
		ls += "<字:字体 字:字号=\"12.0\" 字:颜色=\"#0000ff\"/>";
		ls += "<字:下划线 字:类型=\"single\" 字:颜色=\"auto\"/>";	
		ls += "</uof:句式样>";
		
		return ls ;
	}
	
	//return the text style for hyperlink in spreadsheet
	public static String sheet_link_style(){
		String ls = "";
		
		ls += "<字:字体 字:字号=\"12.0\" 字:颜色=\"#0000ff\"/>";
		ls += "<字:下划线 字:类型=\"single\" 字:颜色=\"auto\"/>";
		
		return ls;
	}
	
	//return the link style name
	public static String style_name(){
		return _link_style_name;
	}
	
	//add a new link
	public static void process(Attributes atts){
		String attVal = "";
		String oneLink = "<uof:超级链接";
		String linkID = IDGenerator.get_hyperlink_id();
		
		oneLink += " uof:标识符=\"" + "ID_" + linkID + "\"";
		
		if((attVal=atts.getValue("xlink:href"))!=null){
			oneLink += " uof:目标=\"" + parse_href(attVal) + "\"";
		}
		
		if((attVal=atts.getValue("text:style-name"))!=null){
			oneLink += " uof:式样引用=\"" + attVal + "\"";
		}
		else {
			//oneLink += " uof:式样引用=\"" + _unvisited + "\"";
		}
		
		if((attVal=atts.getValue("text:visited-style-name"))!=null){
			oneLink += " uof:已访问式样引用=\"" + attVal + "\"";
		}
		else {
			//oneLink += " uof:已访问式样引用=\"" + _visited + "\"";
		}
		
		oneLink += " uof:链源=\"" + linkID + "\"";
		oneLink += "/>";
		
		_links += oneLink;
	}
	
	private static String parse_href(String rawHref){
		String href = "";
		
		href = rawHref;
		
		if(rawHref.contains("../")){
			int ind = href.lastIndexOf("../");
			
			href = href.substring(0,ind) + href.substring(ind+3);
		}
		
		if(rawHref.startsWith("#")){
			href = rawHref.replace(".","!").substring(1);
		}
		else if(!(rawHref.startsWith("http:") || rawHref.startsWith("ftp:") 
			|| rawHref.startsWith("telnet:") || rawHref.startsWith("mailto:")
			|| rawHref.startsWith("news:")) && rawHref.contains("%")){
			
			try{
				URI uri = new URI(rawHref);	
				if(uri.getPath() != null){
					href = uri.getPath();
				}				
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return href;
	}
}
