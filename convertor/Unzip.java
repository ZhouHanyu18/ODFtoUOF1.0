package convertor;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * 用于将ODF类型文档进行解压得到content.xml、styles.xml、meta.xml等xml源文件。
 * 
 * @author xie
 *
 */
public class Unzip {
	private static String _tmp_file_dir = "testfile\\";

	
	public static String get_temp_path(){
		return _tmp_file_dir;
	}
	
	public static void copyInputStream(InputStream in, OutputStream out) throws IOException{
		byte[] buffer = new byte[1024];
		int len;
		
		while((len = in.read(buffer)) >= 0){
			out.write(buffer, 0, len);
		}
		
		in.close();
		out.close();
	}
	
	public static void unzip(String originName) throws Exception{
		Enumeration entries;
		ZipFile zipFile;
		
		if(!originName.endsWith(".odt") && !originName.endsWith(".ods") && !originName.endsWith(".odp")){
			throw new Exception("Error: The file type is wrong!!!");
		}
		
		File src = new File(originName);		
		if(!src.exists()){
			throw new Exception("Error: The source file name is not present!!!");
		}
		
		try {		
			zipFile = new ZipFile(src);
			
			entries = zipFile.entries();
			
			while(entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry)entries.nextElement();
				String fileName = _tmp_file_dir + entry.getName();
				
				if(entry.isDirectory()) {
					(new File(fileName)).mkdir();
				}
				else{
					String parent = new File(fileName).getParent();
					//to make sure its parent directories are present.
					(new File(parent)).mkdirs();
					
					copyInputStream(zipFile.getInputStream(entry),
							new BufferedOutputStream(new FileOutputStream(fileName)));
				}
			}
			
			zipFile.close();
			
		} catch (IOException ioe) {			
			ioe.printStackTrace();
			throw new Exception("Unhandled exception: 文件解压出现问题！");
		}
	}
	
}
