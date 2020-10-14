package spreadsheet;

public class Formula {
	
	//oooc:=[.C9] or oooc:=[������1.C9] >>> ?
	//oooc:=AVERAGE([.C3:.C8]) >>> =AVERAGE(C3:C8)
	//oooc:=����ƽ���ɼ� >>> =����ƽ���ɼ�
	public static String get_cell_formula(String val, String tableName){
		char[] ourChars = null;
		int val_len = 0;
		int chars_len = 0;
		String newVal = "";
				
		newVal = val.replaceAll(";", ",");				//�滻�����ָ���
		newVal = newVal.replaceAll(tableName,".");		//'.' ����ɾ��
		if(newVal.startsWith("oooc:")){
			newVal = newVal.substring(5);
		}
		
		val_len = newVal.length();
		ourChars = new char[val_len];
		
		for(int i=0; i<val_len; i++){
			char c = newVal.charAt(i);
			if(c!='.' && c!='$' && c!='[' && c!=']'){
				ourChars[chars_len] = c;
				chars_len++;
			}
		}
		
		return (new String(ourChars,0,chars_len));
	}
}
