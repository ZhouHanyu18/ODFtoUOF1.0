package spreadsheet;

/**
 * ������ӱ���ַ��ת������ΪODF��UOF�б���ַ�ı�ʾ��ʽ��һ������
 * 
 * @author xie
 *
 */
public class Cell_Address {
	
	//1 >>> A, 26>>>Z,27>>>AA...
	public static String to_col_addr(int colNum){
		int i1, i2;
		char c = 'A';
		String rst = "";
		
		i2 = colNum;
		while(i2 > 0){
			i1 = i2 % 26;
			i2 = i2 / 26;
			
			if(i1 == 0){
				c = 'Z';
				i2 --;
			}else{
				c = 'A';
				c += i1 - 1;
			}
			
			rst = String.valueOf(c) + rst;
		}	
		
		return rst;
	}
	
	//1,1>>>$A$1
	public static String to_addr(int colNum, int rowNum){
		return "$" + to_col_addr(colNum) + "$" + rowNum;
	}
	
	//$������1.$C$9>>>������1 or ������1.C9>>>������1
	public static String get_table_name(String val){
		int index1 = 0;
		int index2 = 0;
		String name = "";
		
		try{
			if(val.contains("$")){
				index1 = val.indexOf("$")+1;
			}
			index2 = val.indexOf(".");
			name = val.substring(index1,index2);
		}catch(Exception e){
			name = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not get table name.");
		}
		return name;
	}
	
	//������1.B2:������1.E18>>>$B$2:$E$18 or ������1.C9>>>$C$9
	public static String get_cell_range(String val){
		int index = 0;
		String range = "";
		
		try{
			index = val.indexOf(":");
			if(index == -1){
				range = get_cell_address(val);
			}else{
				range += get_cell_address(val.substring(0,index));
				range += ":";
				range += get_cell_address(val.substring(index+1));
			}
		}catch(Exception e){
			range = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not get cell range.");
		}
		
		return range;
	}
	
	//$������1.$C$9>>>$C$9 or ������1.C9>>>$C$9
	public static String get_cell_address(String val){
		int index = 0;
		String cellName = "";
		String address = "";
		
		try{
			index = val.lastIndexOf(".");
			cellName = val.substring(index+1);
			
			if(!cellName.contains("$")){		
				for(int i=1; i <= cellName.length(); i++){
					char c = cellName.charAt(i);
					if(Character.isDigit(c)){
						address += "$" + cellName.substring(0,i);
						address += "$" + cellName.substring(i);
						break;
					}
				}		
			}else {
				address = cellName;
			}
		}catch(Exception e){
			address = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not get cell address.");
		}
		
		return address;
	}
}
