package stored_data;

public class Meta_Data{
	private static String _udm_set = "";   //�û��Զ���Ԫ���ݼ�
	private static String _keyword_set = "";   //�ؼ��ּ�
	
//	**********************����_udm_set*************************
//	*	
	public static void add_udm(String udm) 
	{
		_udm_set += udm;
	}
	
	public static String get_udm_set() {
		String udmset = "";
		
		if (_udm_set.length() != 0){
			udmset = "<uof:�û��Զ���Ԫ���ݼ�>" + _udm_set + "</uof:�û��Զ���Ԫ���ݼ�>";
		}
		
		_udm_set = "";
		return udmset;
	}
//	*	
//	**************************************
	
//	**********************����_keyword_set*************************
//	*	
	public static void add_keyword(String keyword) {
		_keyword_set += keyword;
	}
	
	public static String get_keyword_set() {
		String kws = "";
		
		if (_keyword_set.length() != 0){
			kws = "<uof:�ؼ��ּ�>" + _keyword_set + "</uof:�ؼ��ּ�>";
		}
		
		_keyword_set = "";
		return kws;
	}
//	*	
//	**************************************
}
