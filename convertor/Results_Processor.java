package convertor;

import java.io.*;

/**
 * 用于输出结果的处理，把转换过程中的输出写到UOF结果文件中。
 * 
 * @author xie
 *
 */
public class Results_Processor {
	private static FileOutputStream _outstream = null;
	private static OutputStreamWriter _outstream_writer = null;
	private static FileOutputStream _outstream_final = null;
	private static OutputStreamWriter _outstream_writer_final = null;
	
	public Results_Processor() {
		
	}
	
	public static void close() throws IOException{
		_outstream.close();
		_outstream_writer.close();
		_outstream_final.close();
		_outstream_writer_final.close();
	}
	
	public static String initialize(String uofName) throws IOException{
		File uofFile = new File(uofName);
		
		if(uofFile.exists()){
			String error = "Error: File name:" + uofName 
						+ " exists already！Please input a new file name for the result！";
			
			Convertor_ODF_To_UOF.write_source_ta(error);
			throw new IOException(error);
		}
		
		uofFile.createNewFile();
		File tmpFile = File.createTempFile("odf_to_uof",".xml");
		tmpFile.deleteOnExit();
		
		_outstream = new FileOutputStream(tmpFile);	
		_outstream_writer = new OutputStreamWriter(_outstream,"UTF8");
		
		_outstream_final = new FileOutputStream(uofName);	
		_outstream_writer_final = new OutputStreamWriter(_outstream_final,"UTF8");
		
		System.out.println(tmpFile.getAbsolutePath());
		return tmpFile.getAbsolutePath();
	}
	
	public static void process_result(String result){		
		try {
			_outstream_writer.write(result,0,result.length());
			_outstream_writer.flush();
			
			if(!result.equals("")){
				Convertor_ODF_To_UOF.write_result_ta(result.replace(">",">\n"));
			}
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	 
	public static void write_final_file(String result) {
		//=============================
		int last = 0;
		int current = result.indexOf(">");
		
		if(result.length()==0) return;
		
		if(result.indexOf("\n")!=-1){
			System.out.print(result);
		}
		else{
			while(current != -1){
				System.out.println(result.substring(last,current+1));
				last = current + 1;
				current = result.indexOf(">",last);
			}
		}
		//=============================
		
		try {
			_outstream_writer_final.write(result,0,result.length());
			_outstream_writer_final.flush();
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
}
