package convertor;

/**
 * 处理长度单位的换算，主要是将cm/mm等转换到pt。
 * 
 * @author xie
 *
 */
public class Unit_Converter {
	private static float _CM_to_PT = 28.35f;
	private static float _MM_to_PT = 2.835f;
	private static String _FORMAT = "#.###";
	
	private static final float _CM_to_PT_gra = (float)37.8;
	private static final float _MM_to_PT_gra = (float)3.78;
	private static final float _PT_to_PT_gra = (float)4/3;
	

	public static String convert(String lengthVal){
		int index = 0;
		float res = 0;
		String len = "";
		
		java.text.DecimalFormat df = new java.text.DecimalFormat(_FORMAT);
		lengthVal = lengthVal.toLowerCase();
		
		if(lengthVal.contains("pt")){
			index = lengthVal.indexOf("pt");
			len = lengthVal.substring(0,index);
			res = Float.parseFloat(len);
		}
		
		else if(lengthVal.contains("cm")){
			index = lengthVal.indexOf("cm");
			len = lengthVal.substring(0,index);
			res = Float.parseFloat(len) * _CM_to_PT;
		}
		
		else if(lengthVal.contains("mm")){
			index = lengthVal.indexOf("mm");
			res = Float.parseFloat(lengthVal.substring(0,index)) * _MM_to_PT;
		}
		
		return df.format(res);
	}
	
	public static float convert_gra(String length_val){
		int index = 0;
		float res = 0;
		length_val = length_val.toLowerCase();
		if(length_val.contains("pt")){
			index = length_val.indexOf("pt");
			res = Float.valueOf(length_val.substring(0,index)) * _PT_to_PT_gra;
		}
		else if(length_val.contains("cm")){
			index = length_val.indexOf("cm");
			res = Float.valueOf(length_val.substring(0,index)) * _CM_to_PT_gra;
		}
		else if(length_val.contains("mm")){
			index = length_val.indexOf("mm");
			res = Float.valueOf(length_val.substring(0,index)) * _MM_to_PT_gra;
		}
		
		return res;
	}
	
	public static float from_percent(String percentVal){
		float val = 0;
		
		if(percentVal.contains("%")){
			int index = percentVal.indexOf("%");
			
			percentVal = percentVal.substring(0,index);
			val = Float.valueOf(percentVal)/100;
		}
		
		return val;
	}
}
