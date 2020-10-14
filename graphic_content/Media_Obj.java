package graphic_content;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import stored_data.Content_Data;
import convertor.IDGenerator;
import convertor.Unzip;

public class Media_Obj {
	//
	private static Map<String,String> _href_id_map = new HashMap<String,String>();
	//
	private static byte BaseTable[] = { 
		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P', 
		'Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f', 
		'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v', 
		'w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/' 
	};
	
	//initialize
	public static void init(){
		_href_id_map.clear();
	}
	
	public static String process_href(String href){
		 href = href.replace("%20"," ");
		String objID = "";
		
		objID = _href_id_map.get(href);
		
		if(objID == null){
			String obj = "";
			String path = "";
			String type = "";
			
			objID = IDGenerator.get_otherobj_id();
			_href_id_map.put(href,objID);

			type = href.substring(href.lastIndexOf(".") + 1);
			if(href.contains(":")){
				//if(href.startsWith("\\"));
				path = href;
			}else {
				path = Unzip.get_temp_path() + href;
			}
			
			obj = "<uof:其他对象";
			obj += " uof:标识符=\"" + objID + "\"";
			obj += " uof:公共类型=\""+ type + "\"";
			obj += " uof:内嵌=\"false\">";
			obj += "<uof:数据>" + base64Enc(path) + "</uof:数据>";
			obj += "</uof:其他对象>";
			
			Content_Data.add_other_obj(objID,obj);
		}
		
		return objID;
	}
	
	public static String base64Enc(String filename) { 
		byte out[] = null;
		try { 
			File f = new File(filename); 
			FileInputStream fin = new FileInputStream(filename); 
			
			//读文件到BYTE数组 
			byte bytes[] = new byte[(int)(f.length())]; 
			int n = fin.read(bytes); 
			
			byte buf[] = new byte[4]; // base64 字符数组 
			
			int n3byt = n / 3; // 3 bytes 组数
			out = new byte[n3byt * 4 + 4];
			int nrest = n % 3; // 分组后剩余 bytes 
			int k = n3byt * 3; 
			int i = 0, j = 0; // 指针 
			
			//处理每个3-bytes分组 
			for (; i < k; i += 3) {
				buf[0] = (byte)((bytes[i] & 0xFC) >> 2); 
				buf[1] = (byte)(((bytes[i] & 0x03) << 4) | 
						((bytes[i+1] & 0xF0) >> 4)); 
				buf[2] = (byte)(((bytes[i+1] & 0x0F) << 2) | 
						((bytes[i+2] & 0xC0) >> 6)); 
				buf[3] = (byte)( bytes[i+2] & 0x3F); 
				out[j++] = BaseTable[buf[0]];
				out[j++] = BaseTable[buf[1]];
				out[j++] = BaseTable[buf[2]];
				out[j++] = BaseTable[buf[3]];
			} 
			
			//处理尾部 
			if (nrest==2) { //2 bytes left 
				buf[0] = (byte)(( bytes[k] & 0xFC) >> 2); 
				buf[1] = (byte)(((bytes[k] & 0x03) << 4) | 
						((bytes[k+1] & 0xF0) >> 4)); 
				buf[2] = (byte)(( bytes[k+1] & 0x0F) << 2); 
			} 
			else if (nrest==1) { //1 byte left 
				buf[0] = (byte)((bytes[k] & 0xFC) >> 2); 
				buf[1] = (byte)((bytes[k] & 0x03) << 4); 
			} 
			if (nrest > 0) { //发送尾部 
				out[j++] = BaseTable[buf[0]];
				out[j++] = BaseTable[buf[1]];
				if (nrest==2)
					out[j++] = BaseTable[buf[2]];
				else
					out[j++] = '=';
				out[j++] = '=';
			}
		} 
		catch (Exception e) { 		
			System.err.println(e);
			e.printStackTrace();
		} 
		
		String rst = "";
		if(out != null){
			rst = new String(out).trim();
		}
		return rst;
	}
}
