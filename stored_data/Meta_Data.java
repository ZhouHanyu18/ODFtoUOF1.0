package stored_data;

public class Meta_Data{
	private static String _udm_set = "";   //用户自定义元数据集
	private static String _keyword_set = "";   //关键字集
	
//	**********************操作_udm_set*************************
//	*	
	public static void add_udm(String udm) 
	{
		_udm_set += udm;
	}
	
	public static String get_udm_set() {
		String udmset = "";
		
		if (_udm_set.length() != 0){
			udmset = "<uof:用户自定义元数据集>" + _udm_set + "</uof:用户自定义元数据集>";
		}
		
		_udm_set = "";
		return udmset;
	}
//	*	
//	**************************************
	
//	**********************操作_keyword_set*************************
//	*	
	public static void add_keyword(String keyword) {
		_keyword_set += keyword;
	}
	
	public static String get_keyword_set() {
		String kws = "";
		
		if (_keyword_set.length() != 0){
			kws = "<uof:关键字集>" + _keyword_set + "</uof:关键字集>";
		}
		
		_keyword_set = "";
		return kws;
	}
//	*	
//	**************************************
}
