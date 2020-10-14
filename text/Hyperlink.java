package text;

import java.net.URI;
import org.xml.sax.Attributes;
import convertor.IDGenerator;

/**
 * ����<text:a> �� <uof:��������>��ת����
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
			rst = "<uof:���Ӽ�>";
			rst += _links;
			rst += "</uof:���Ӽ�>";
			_links = "";
		}	
		
		return rst;
	}
	
	//create the text style for hyperlink
	public static String link_style(){
		String ls = "";
		
		ls += "<uof:��ʽ��";
		ls += " ��:��ʶ��=\"" + _link_style_name + "\"";
		ls += " ��:����=\"" + _link_style_name + "\"";
		ls += " ��:����=\"auto\">";
		
		ls += "<��:���� ��:�ֺ�=\"12.0\" ��:��ɫ=\"#0000ff\"/>";
		ls += "<��:�»��� ��:����=\"single\" ��:��ɫ=\"auto\"/>";	
		ls += "</uof:��ʽ��>";
		
		return ls ;
	}
	
	//return the text style for hyperlink in spreadsheet
	public static String sheet_link_style(){
		String ls = "";
		
		ls += "<��:���� ��:�ֺ�=\"12.0\" ��:��ɫ=\"#0000ff\"/>";
		ls += "<��:�»��� ��:����=\"single\" ��:��ɫ=\"auto\"/>";
		
		return ls;
	}
	
	//return the link style name
	public static String style_name(){
		return _link_style_name;
	}
	
	//add a new link
	public static void process(Attributes atts){
		String attVal = "";
		String oneLink = "<uof:��������";
		String linkID = IDGenerator.get_hyperlink_id();
		
		oneLink += " uof:��ʶ��=\"" + "ID_" + linkID + "\"";
		
		if((attVal=atts.getValue("xlink:href"))!=null){
			oneLink += " uof:Ŀ��=\"" + parse_href(attVal) + "\"";
		}
		
		if((attVal=atts.getValue("text:style-name"))!=null){
			oneLink += " uof:ʽ������=\"" + attVal + "\"";
		}
		else {
			//oneLink += " uof:ʽ������=\"" + _unvisited + "\"";
		}
		
		if((attVal=atts.getValue("text:visited-style-name"))!=null){
			oneLink += " uof:�ѷ���ʽ������=\"" + attVal + "\"";
		}
		else {
			//oneLink += " uof:�ѷ���ʽ������=\"" + _visited + "\"";
		}
		
		oneLink += " uof:��Դ=\"" + linkID + "\"";
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
