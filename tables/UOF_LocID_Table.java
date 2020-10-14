package tables;

import java.util.HashMap;
import java.util.Map;

public class UOF_LocID_Table {
	//
	private static Map<String,String> 
		_hashmap = new HashMap<String,String>();
	
	//locID table for children of <��:�˳�>
	private static Map<String,String>
		_exit_eff_map = new HashMap<String,String>();
	
	
	public UOF_LocID_Table() { 
		
	}
	
	public static String locID_attrList(String qName){
		return _hashmap.get(qName);
	}
	
	public static void create_map() {
		create_exit_eff_map();
		
		_hashmap.put("��:ˮƽ","t0176.�����");
		_hashmap.put("��:��ֱ","t0179.�����");		
		_hashmap.put("uof:UOF","u0000");
		_hashmap.put("uof:��������","u0041.��ʶ�� ���� ������");
		_hashmap.put("uof:Ԫ����","u0001");
		_hashmap.put("uof:��ǩ��","u0027");
		_hashmap.put("uof:��ǩ","u0028.����");
		_hashmap.put("uof:���Ӽ�","u0031");
		_hashmap.put("uof:��������","u0032.��ʶ�� Ŀ�� ��ǩ ʽ������ �ѷ���ʽ������ ��ʾ ��Դ");
		_hashmap.put("uof:����","u0033");
		_hashmap.put("uof:��ѧ��ʽ","u0034 ��ʶ��"); 
		_hashmap.put("uof:��������","u0036.��ʶ�� ��Ƕ �������� ˽������");
		_hashmap.put("uof:����","u0037");
		_hashmap.put("uof:·��","u0038.��ʶ�� ��Ƕ �������� ˽������");//���·���Ƕ��󼯵�·��,�������ط���Ȼ��·��
		_hashmap.put("uof:�û����ݼ�","u0068");
		_hashmap.put("uof:ʽ����","u0039");
		_hashmap.put("uof:���ִ���","u0047");
		_hashmap.put("uof:��ʾ�ĸ�","u0048");
		_hashmap.put("uof:���ӱ��","u0049");
		_hashmap.put("uof:��չ��","u0050");
		_hashmap.put("uof:��չ","u0051");
		_hashmap.put("uof:�������","u0052");
		_hashmap.put("uof:����汾","u0053");
		_hashmap.put("uof:��չ����","u0054");
		//_hashmap.put("uof:·��","u0065");//���·������չ����·��,��չ���ݵ���Ԫ��
		//_hashmap.put("uof:·��","u0067");//uof:ֹͣ���� ��·��
		_hashmap.put("uof:����","u0056");
		_hashmap.put("uof:ê��","u0064.x���� y���� ��� �߶� ͼ������ �涯��ʽ ����ͼ ռλ��");
		_hashmap.put("uof:�ı�λ��","u0029.��������");
		_hashmap.put("uof:�������ʽ","u0030.�������� ��������");
		_hashmap.put("uof:����","u0002");
		_hashmap.put("uof:����","u0003");
		_hashmap.put("uof:������","u0004");
		_hashmap.put("uof:����","u0005");
		_hashmap.put("uof:�������","u0006");
		_hashmap.put("uof:ժҪ","u0007");
		_hashmap.put("uof:��������","u0008");
		_hashmap.put("uof:�༭����","u0009");
		_hashmap.put("uof:�༭ʱ��","u0010");
		_hashmap.put("uof:����Ӧ�ó���","u0011");
		_hashmap.put("uof:����","u0012");
		_hashmap.put("uof:�ĵ�ģ��","u0013");
		_hashmap.put("uof:�ؼ��ּ�","u0014");
		_hashmap.put("uof:�ؼ���","u0015");
		_hashmap.put("uof:�û��Զ���Ԫ���ݼ�","u0016");
		_hashmap.put("uof:�û��Զ���Ԫ����","u0017.���� ����");
		_hashmap.put("uof:��˾����","u0018");
		_hashmap.put("uof:��������","u0019");
		_hashmap.put("uof:ҳ��","u0020");
		_hashmap.put("uof:����","u0021");
		_hashmap.put("uof:Ӣ���ַ���","u0022");
		_hashmap.put("uof:�����ַ���","u0023");
		_hashmap.put("uof:����","u0024");
		_hashmap.put("uof:������","u0025");
		_hashmap.put("uof:������","u0026");
		_hashmap.put("uof:���弯","u0040");
		_hashmap.put("uof:��������","u0041.��ʶ�� ���� ������");
		_hashmap.put("uof:�Զ���ż�","u0042");
		_hashmap.put("uof:��ʽ��","u0043.��ʶ�� ���� ���� ���� ��ʽ������");
		_hashmap.put("uof:����ʽ��","u0044.��ʶ�� ���� ���� ���� ��ʽ������");

		_hashmap.put("uof:���ֱ�ʽ��","u0045.��ʶ�� ���� ���� ���� ��ʽ������");
		_hashmap.put("uof:��Ԫ��ʽ��","u0046.��ʶ�� ���� ���� ���� ��ʽ������");
		_hashmap.put("uof:��","u0057.���� ��� �߾� ��ɫ ��Ӱ");//
		_hashmap.put("uof:��","u0058.���� ��� �߾� ��ɫ ��Ӱ");
		_hashmap.put("uof:��","u0059.���� ��� �߾� ��ɫ ��Ӱ");
		_hashmap.put("uof:��","u0060.���� ��� �߾� ��ɫ ��Ӱ");
		_hashmap.put("uof:�Խ���1","u0061.���� ��� �߾� ��ɫ ��Ӱ");
		_hashmap.put("uof:�Խ���2","u0062.���� ��� �߾� ��ɫ ��Ӱ");

		_hashmap.put("��:��","t0085");
		_hashmap.put("��:���ֱ�","t0128.����");
		_hashmap.put("��:����","t0051.��ʶ��");

		_hashmap.put("��:������","t0086.ʽ������");
		_hashmap.put("��:����","t0088.������������ ������������ ������������ ���Ļ��� �ֺ� ����ֺ� ��ɫ");
		_hashmap.put("��:����","t0089.ֵ");
		_hashmap.put("��:б��","t0090.ֵ");
		_hashmap.put("��:ͻ����ʾ","t0091");
//		_hashmap.put("��:�߿�","t0092.���� ��� �߾� ��ɫ ��Ӱ");
//		_hashmap.put("��:���","t0093");
		_hashmap.put("��:ɾ����","t0094.����");
		_hashmap.put("��:�»���","t0095.���� ��ɫ ���»���");
		_hashmap.put("��:���غ�","t0096.���� ��ɫ �����غ�");
		_hashmap.put("��:��������","t0097.ֵ");
		_hashmap.put("��:����","t0098.ֵ");
		_hashmap.put("��:����","t0099.����");
		_hashmap.put("��:��Ӱ","t0100.ֵ");
		_hashmap.put("��:��Ŀ����","t0101.����");
//		_hashmap.put("��:��ʽ��/��:λ��","t0102");
//   	_hashmap.put("��:����","t0103");
//		_hashmap.put("��:����","t0003");
		_hashmap.put("��:�ַ����","t0104");
		_hashmap.put("��:�����ּ��","t0105");
		_hashmap.put("��:�ַ���������","t0106.ֵ");
		_hashmap.put("��:���±�","t0205.ֵ");
		
		//_hashmap.put("��:���","t0130.���Կ�� ��Կ��");//���ֱ�ʽ������Ԫ��
		_hashmap.put("��:�п�","t0131");
		_hashmap.put("��:�п�","t0132");
		//_hashmap.put("��:����","t0133");
		_hashmap.put("��:������","t0134");
		_hashmap.put("��:����","t0135.ֵ");
		_hashmap.put("��:���ű߾�","t0135.�� �� �� ��");
//		_hashmap.put("��:λ��","t0136");
		//_hashmap.put("��:�߿�","t0137");
		_hashmap.put("��:�Զ�������С","t0140.ֵ");
		_hashmap.put("��:Ĭ�ϵ�Ԫ��߾�","t0141.�� �� �� ��");
		_hashmap.put("��:Ĭ�ϵ�Ԫ����","t0142");

		_hashmap.put("uof:ֹͣ����","u0066");

        //_hashmap.put("��:��ʽ�޶�","");//��ʽ�޶�,����Ƕ���ʽ������Ԫ��
        _hashmap.put("��:��ټ���","t0054");
        //_hashmap.put("��:����","t0055.ˮƽ���� ���ֶ���");
        //_hashmap.put("��:����","t0056");//����ʽ����Ԫ��

		_hashmap.put("��:��","t0182");
		_hashmap.put("��:��","t0183");
		_hashmap.put("��:����","t0184");//��������Ԫ��ÿ�����ֱ���������Ԫ��,��:���Ժ���:���,�����в�ͬ��localid,�����Ҫд�ڳ�����
        _hashmap.put("��:�о�","t0057.���� ֵ");
		_hashmap.put("��:�μ��","t0058");
		_hashmap.put("��:��ǰ��","t0196");
		_hashmap.put("��:�κ��","t0197");

        _hashmap.put("��:�μ��","t0058");
        _hashmap.put("��:�Զ������Ϣ","t0059.������� ��ż��� ���±�� ��ʼ���");
        _hashmap.put("��:���п���","t0060");
        _hashmap.put("��:���п���","t0061");
        _hashmap.put("��:���в���ҳ","t0062.ֵ");
        _hashmap.put("��:���¶�ͬҳ","t0063.ֵ");
        _hashmap.put("��:��ǰ��ҳ","t0064.ֵ");
        //_hashmap.put("��:�߿�","t0065");//����ʽ������Ԫ��
        //_hashmap.put("��:���","t0066");
        _hashmap.put("��:�Ʊ�λ����","t0067");
        _hashmap.put("��:�Ʊ�λ","t0068.λ�� ���� ǰ���� �Ʊ�λ�ַ�");
        _hashmap.put("��:��������","t0069.ֵ");
		_hashmap.put("��:�����³�","t0070.���� �������� �ַ��� ���� ���");

		_hashmap.put("��:ȡ������","t0071.ֵ");
		_hashmap.put("��:ȡ���к�","t0072.ֵ");
		_hashmap.put("��:�����ʶ���","t0073.ֵ");
		_hashmap.put("��:����β������","t0074.ֵ");
		_hashmap.put("��:�Ƿ����ױ��ѹ��","t0075.ֵ");
		_hashmap.put("��:����ϰ����β�ַ�","t0076.ֵ");
		_hashmap.put("��:�Զ�������Ӣ���ַ����","t0077.ֵ");
		_hashmap.put("��:�Զ��������������ּ��","t0078.ֵ");
		_hashmap.put("��:�������Զ�����������","t0195.ֵ");//������
		
		_hashmap.put("��:����","t0016");
		_hashmap.put("��:�ֽ�","t0017.����");
		_hashmap.put("��:�߼��½�","t0050");

		_hashmap.put("��:���ֱ�","t0128.ʽ������");
		_hashmap.put("��:���ô������","t0000");
		_hashmap.put("��:��ǰ��ͼ","t0002");
		_hashmap.put("��:�ĵ�����","t0001");
		_hashmap.put("��:�޶�","t0005.ֵ");
		_hashmap.put("��:������λ","t0006");
		_hashmap.put("��:������","t0007");
		_hashmap.put("��:�����ַ�","t0008");
		_hashmap.put("��:��β�ַ�","t0009");
		_hashmap.put("��:�û���","t0010");
		_hashmap.put("��:�û�","t0011.��ʶ�� ����");
		_hashmap.put("��:�޶���Ϣ��","t0012");
		_hashmap.put("��:�޶���Ϣ","t0013.��ʶ�� ���� ����");
		_hashmap.put("��:��ע��","t0014");
		_hashmap.put("��:��ע","t0015");
		_hashmap.put("��:����","t0049.���� �ȿ� �ָ��� �ָ��߿�� �ָ�����ɫ");
		_hashmap.put("��:��","t0050.��� ���");
		_hashmap.put("��:������","t0018");
		_hashmap.put("��:ɾ��","t0084.�޶���Ϣ����");

		//_hashmap.put("��:���","t0150.����ֵ ���ֵ");
		//_hashmap.put("��:���","t0150");//��Ԫ�����Ե���Ԫ��
		//_hashmap.put("��:�߿�","t0152");//��Ԫ�����Ե���Ԫ��

		_hashmap.put("��:��Ԫ��߾�","t0151.�� �� �� ��");
		//_hashmap.put("��:���","t0153");//��Ԫ�����Ե���Ԫ��

		_hashmap.put("��:��ֱ���뷽ʽ","t0154");
		_hashmap.put("��:����","t0155.ֵ");
		_hashmap.put("��:����","t0156.ֵ");
		_hashmap.put("��:�Զ�����","t0157.ֵ");
		_hashmap.put("��:��Ӧ����","t0158.ֵ");

		_hashmap.put("��:��Ԫ��","t0148");
		_hashmap.put("��:��Ԫ������","t0149");
		//_hashmap.put("��:��ʽ�޶�","t0087");//�����Ե���Ԫ��
		//_hashmap.put("��:��ʽ�޶�","t0169");//��ʽ������Ԫ��
		_hashmap.put("��:��ʽ��","t0170");
		_hashmap.put("��:��","t0085");
		_hashmap.put("��:��ע","t0107.������");
		_hashmap.put("��:βע","t0108.������");
		_hashmap.put("��:�ı���","t0109.udsPath");

		_hashmap.put("��:����ʼ","t0121.��ʶ�� ���� ����");
		_hashmap.put("��:�������","t0122.��ʶ������");
		_hashmap.put("��:�Ʊ��","t0123");
		_hashmap.put("��:���з�","t0124");
		_hashmap.put("��:������","t0125");
		_hashmap.put("��:�ո��","t0126.����");
		_hashmap.put("��:��ҳ��","t0127");
		_hashmap.put("��:��ע","t0015.�������� ���� ����");
        _hashmap.put("��:��ע��","t0014");
		_hashmap.put("��:�������","t0083");
		_hashmap.put("��:���ִ���","t0047");
		_hashmap.put("��:���ô������","t0000");
		_hashmap.put("��:���ֱ�ʽ��","u0045.��ʶ�� ���� ���� ���� ��ʽ������");
		_hashmap.put("��:���ֱ�","t0128");
		_hashmap.put("��:���ֱ�����","t0129.ʽ������");
		_hashmap.put("��:��������","t");
		//_hashmap.put("��:��ʽ�޶�","t0053");//�������Ե���Ԫ��
		_hashmap.put("��:��������","t0052.ʽ������");

		_hashmap.put("��:����ʽ������","t");
		//_hashmap.put("��:��ʽ�޶�","t0173");//����ʽ����Ԫ��
		_hashmap.put("��:����ʽ��","t0174");//����ʽ���еĸ�ʽ�޶�����Ԫ��...
		_hashmap.put("��:��","t0085");
		_hashmap.put("��:��ʼ","t0079.���� ����");
		_hashmap.put("��:�����","t0080");
		_hashmap.put("��:�����","t0081");
		_hashmap.put("��:���뿪ʼ","t0082.�޶���Ϣ����");
		_hashmap.put("��:�������","t0083");
		_hashmap.put("��:�û�","t0011.��ʶ�� ����");
        _hashmap.put("��:�û���","t0010");
		_hashmap.put("��:�Զ����","t0169.��ʶ�� ���� ��������� �༶���");
		_hashmap.put("��:�Զ���ż�","t0042");
		_hashmap.put("��:����","t0159.����ֵ ��Ŷ��뷽ʽ β���ַ�");
		_hashmap.put("��:��Ŀ����","t0171");
		_hashmap.put("��:��������","t0160.ʽ������");
		_hashmap.put("��:����ʽ������","t0161");
		_hashmap.put("��:��Ÿ�ʽ","t0162");
		_hashmap.put("��:��Ÿ�ʽ��ʾ","t0163");
		_hashmap.put("��:ͼƬ��������","t0164");
		//_hashmap.put("��:����","t0165");//�Զ���ŵ���Ԫ��

		_hashmap.put("��:�Ʊ��λ��","t0166");
		_hashmap.put("��:��ʼ���","t0167");
		_hashmap.put("��:�����ʽ","t0168");
		_hashmap.put("��:������","t0018");
		//_hashmap.put("��:��ʽ�޶�","t0019");
		_hashmap.put("��:������","t0020");
		_hashmap.put("��:ҳ�߾�","t0021.�� �� �� ��");
		_hashmap.put("��:ֽ��","t0022.ֽ�� ��� �߶�");
		_hashmap.put("��:��żҳҳüҳ�Ų�ͬ","t0023.ֵ");
		_hashmap.put("��:��ҳҳüҳ�Ų�ͬ","t0024.ֵ");
		_hashmap.put("��:ҳüλ��","t0025");
		_hashmap.put("��:ҳ��λ��","t0026");
		_hashmap.put("��:ҳü","t0027");
		_hashmap.put("��:����ҳҳü","t0028");
		_hashmap.put("��:ż��ҳҳü","t0029");
		_hashmap.put("��:��ҳҳü","t0030");
		_hashmap.put("��:ҳ��","t0031");
		_hashmap.put("��:����ҳҳ��","t0032");
		_hashmap.put("��:ż��ҳҳ��","t0033");
		_hashmap.put("��:��ҳҳü","t0034");
		_hashmap.put("��:װ����","t0035");
		_hashmap.put("��:�Գ�ҳ�߾�","t0036");
		_hashmap.put("��:ƴҳ","t0037");
		_hashmap.put("��:ֽ�ŷ���","t0038");
		_hashmap.put("��:ֽ����Դ","t0039");
		_hashmap.put("��:��ע����","t0040.λ�� ��ʽ ��ʼ��� ��ŷ�ʽ");
		_hashmap.put("��:βע����","t0041.λ�� ��ʽ ��ʼ��� ��ŷ�ʽ");	
		_hashmap.put("��:ҳ������","t0042.��ҳ��ʾ ��ʽ �����½ں� �½���ʼ��ʽ���� �ָ��� ��ʼ���");
		_hashmap.put("��:�к�����","t0043.ʹ���к� ��ŷ�ʽ ��ʼ��� ��߽� �кż��");
		_hashmap.put("��:��������","t0044.�������� ��� �߶� ��ʾ���� ��ӡ����");
		//_hashmap.put("��:��ֱ���뷽ʽ","t0045");
		_hashmap.put("��:�������з���","t0046");
		//_hashmap.put("��:�߿�","t0047");//�����Ե���Ԫ��
		//_hashmap.put("��:���","t0048");
		//_hashmap.put("��:����","t0049");
		//_hashmap.put("��:�߶�","t0145");
		_hashmap.put("��:��ҳ","t0146.ֵ");
		_hashmap.put("��:��ͷ��","t0147.ֵ");
		_hashmap.put("��:��","t0143");
		_hashmap.put("��:��������","t0144");
		_hashmap.put("��:�߼��½�","t0050");
		_hashmap.put("��:ê��","t0110.��ʶ�� ����");
		_hashmap.put("��:ê������","t0111");
		//_hashmap.put("��:���","t0112");//ê�����Ե���Ԫ��
		//_hashmap.put("��:�߶�","t0113");
//		_hashmap.put("��:λ��","t0114");//ê�����Ԫ��
		_hashmap.put("��:����","t0115.���ŷ�ʽ �������� ���Ŷ���");
		_hashmap.put("��:�߾�","t0116.�� �� �� ��");
		_hashmap.put("��:����","t0117");
		_hashmap.put("��:����","t0118");
		_hashmap.put("��:ͼ��","t0120.ͼ������");
		_hashmap.put("��:�����ص�","t0119");
		_hashmap.put("��:ҳüҳ��","t0111");
//		_hashmap.put("��:λ��","t0136");//��������ֱ�ʽ������Ԫ��

		_hashmap.put("��:ͼ��","s0054");
		_hashmap.put("��:ͼ��","s0055.���� ������ ��� �߶� x���� y���� �涯��ʽ");
		_hashmap.put("��:���","s0058");
		_hashmap.put("��:����","s0059");
		//_hashmap.put("��:����","s0020");
		_hashmap.put("��:������","s0009");
		_hashmap.put("��:��һ������","s0010");
		_hashmap.put("��:�ڶ�������","s0011");
		_hashmap.put("��:����","s0062");//������
	//	_hashmap.put("��:�߿�","s0057");
		_hashmap.put("��:������λ","s0001");
		_hashmap.put("��:��ȷ������ʾֵΪ׼","s0002");
		_hashmap.put("��:����ϵͳ��1904","s0003");
		_hashmap.put("��:��������","s0004");
		_hashmap.put("��:������Ч�Լ�","s0005");
		_hashmap.put("��:������Ч��","s0006");
		_hashmap.put("��:������ʽ����","s0016");
		_hashmap.put("��:�Ƿ�RC����","s0124.ֵ");
		_hashmap.put("��:������ʽ��","s0017");
		_hashmap.put("��:��ע","s0053.�Ƿ���ʾ");
		_hashmap.put("��:��Сֵ","s0065");
		_hashmap.put("��:���ֵ","s0066");
		_hashmap.put("��:����λ","s0067");
		_hashmap.put("��:�ε�λ","s0068");
		_hashmap.put("��:���ཻ���","s0069");
		_hashmap.put("��:��λ","s0070");
		_hashmap.put("��:��ʾ��λ","s0071");
		_hashmap.put("��:����","s0072");
		_hashmap.put("��:��ֵ����ת","s0073");
		_hashmap.put("��:�����ǩ��","s0075");
		_hashmap.put("��:��ֵ�����","s0074");
		_hashmap.put("��:����̶���","s0076");
		_hashmap.put("��:�������ת","s0077");
		
		_hashmap.put("��:���ָ�ʽ","s0021.�������� ��ʽ��");
//		_hashmap.put("��:�߿�","s0022");			//��Ԫ��ʽ������Ԫ��
		_hashmap.put("��:����","s0051.��������");
		//_hashmap.put("��:��ʽ","s0052");
		_hashmap.put("��:ͼ����","s0056");
		_hashmap.put("��:��ͼ��","s0060");
		_hashmap.put("��:������","s0061.���̶����� �ο̶����� �̶��߱�־");
		_hashmap.put("��:��ֵ��","s0082.���̶����� �ο̶����� �̶��߱�־");
		_hashmap.put("��:ͼ��","s0083.λ��");
		_hashmap.put("��:ͼ����","s0084");
		_hashmap.put("��:���ݱ�","s0085");
		_hashmap.put("��:����ϵ�м�","s0086");
		_hashmap.put("��:����ϵ��","s0087.ϵ��");
		_hashmap.put("��:���ݵ㼯","s0090");
		_hashmap.put("��:���ݵ�","s0091.ϵ�� ��");
		_hashmap.put("��:�����߼�","s0092");
		_hashmap.put("��:������","s0093.���� ��� �߾� ��ɫ ��Ӱ λ��");
		_hashmap.put("��:����Դ","s0094.�������� ϵ�в���");
		_hashmap.put("��:ϵ��","s0095.ϵ���� ϵ��ֵ ������");
		_hashmap.put("��:���⼯","s0096");
		_hashmap.put("��:����","s0097.���� λ��");
		_hashmap.put("��:����������","s0098");
		_hashmap.put("��:��ֵ","s0063.���ӵ�Դ �������� ��ʽ��");
		_hashmap.put("��:�̶�","s0064");
		_hashmap.put("��:����","s0062.���� ��� �߾� ��ɫ ��Ӱ");
//		_hashmap.put("��:����","s0078");
//		_hashmap.put("��:���ַ���","s0079");
		_hashmap.put("��:��ת�Ƕ�","s0080");
		_hashmap.put("��:ƫ����","s0081");
		
		_hashmap.put("��:��Ԫ��ʽ��","u0046.��ʶ�� ���� ����");
		_hashmap.put("��:�����ʽ","s0113.ʽ������");
		_hashmap.put("��:�����ʽ","s0114");
		_hashmap.put("��:ˮƽ���뷽ʽ","s0115");
		_hashmap.put("��:��ֱ���뷽ʽ","s0116");
		_hashmap.put("��:����","s0117");//�����ʽ����Ԫ��
//		_hashmap.put("��:���ַ���","s0118");
		_hashmap.put("��:������ת�Ƕ�","s0119");
		_hashmap.put("��:�Զ�����","s0120");
		_hashmap.put("��:��С�������","s0121.ֵ");
		//_hashmap.put("��:��","s0048");
		//_hashmap.put("��:��","s0049.�к� ���� �и� ʽ������ ���");
		_hashmap.put("��:��Ԫ��","s0050.�к� ʽ������ ���������� �ϲ����� �ϲ�����");
		_hashmap.put("��:���鼯","s0098");
		//_hashmap.put("��:��","s0099");//���鼯����Ԫ��
		//_hashmap.put("��:��","s0100");//���鼯����Ԫ��
		_hashmap.put("��:��ǩǰ��ɫ","s0027");
		_hashmap.put("��:��ǩ����ɫ","s0028");
		_hashmap.put("��:ҳ������","s0029");
		_hashmap.put("��:��ͼ","s0035.���ڱ�ʶ��");
		_hashmap.put("��:ѡ��","s0036.ֵ");
		_hashmap.put("��:���","s0037");
		_hashmap.put("��:����","s0038");
		_hashmap.put("��:������","s0039");
		_hashmap.put("��:������","s0040");
		_hashmap.put("��:��ǰ��ͼ","s0041");
		//_hashmap.put("��:��ʽ","s0042.ֵ");
		_hashmap.put("��:����","s0043.ֵ");
		_hashmap.put("��:������ɫ","s0044");
		_hashmap.put("��:����","s0045");//���������Ե���Ԫ��
		_hashmap.put("��:��ҳ����","s0046");
		_hashmap.put("��:ѡ������","s0047");
		_hashmap.put("��:����������","s0026");
		_hashmap.put("��:����������","s0018.����� ����� ȱʡ�и� ȱʡ�п�");
		_hashmap.put("��:ɸѡ","s0101");
		_hashmap.put("��:��ҳ����","s0111");
		_hashmap.put("��:��ҳ��","s0112");
		_hashmap.put("��:��ֵ��Ч��","s");
		_hashmap.put("��:У������","s0008");
		_hashmap.put("��:���Կո�","s0012.ֵ");
		_hashmap.put("��:������ͷ","s0013");
		_hashmap.put("��:������ʾ","s0014.��ʾ ���� ����");
		_hashmap.put("��:������ʾ","s0015.��ʾ ���� ���� ����");
		_hashmap.put("��:���ݱ�־","s");
		_hashmap.put("��:��ʾ��־","s0088.ϵ���� ����� ��ֵ �ٷ��� �ָ��� ͼ����־");
		_hashmap.put("��:ϵ����","s0089");
		//_hashmap.put("��:����","s0019");
		_hashmap.put("��:��ʽ","s0023.��:ʽ������");
		_hashmap.put("��:У������","s");
		_hashmap.put("��:ֵ","s0107");
		_hashmap.put("��:���ӱ��","u0049");
		_hashmap.put("��:���ô������","s0000");
		_hashmap.put("��:����","s0024");
		_hashmap.put("��:������","s0025.��ʶ�� ���� ���� ���� ʽ������");
		
		_hashmap.put("��:ɸѡ","s0101.����");
		_hashmap.put("��:��Χ","s0102");
		//_hashmap.put("��:����","s0103.�к�");
		_hashmap.put("��:��ͨ","s0104.���� ֵ");
		_hashmap.put("��:�Զ���","s0105.����");
		_hashmap.put("��:��������","s0106");
		
		_hashmap.put("��:��������","s0108");
		_hashmap.put("��:�������","s0109");
		_hashmap.put("��:ҳ������","s0029.����");
		_hashmap.put("��:ֽ��","s0030.ֽ�� ��� �߶�");
		_hashmap.put("��:ֽ�ŷ���","s0031.");
		_hashmap.put("��:����","s0032");
		_hashmap.put("��:ҳ�߾�","s0033.�� �� �� ��");  
		_hashmap.put("��:ҳüҳ��","s0034.λ��");
		_hashmap.put("��:ƫ����","s");
		_hashmap.put("��:�̶ȱ�־","s");
		_hashmap.put("��:�̶�������","s");          
		_hashmap.put("��:����","s0007");
		_hashmap.put("��:��Ԫ��ֵ","s");     
		_hashmap.put("��:��Ԫ����������","s");
		_hashmap.put("��:�������ʽ","s"); 
		_hashmap.put("��:ͼ��λ��","s");  
		_hashmap.put("��:��ֱ����","s"); 
		_hashmap.put("��:��ֵ��������","s");
		_hashmap.put("��:��ʾ��λ","s0071.ֵ"); 
		_hashmap.put("��:��ͨɸѡ����","s");  
		_hashmap.put("��:����������","s");
		_hashmap.put("��:������","s");
		//_hashmap.put("��:У������","s");
		_hashmap.put("��:ˮƽ����","s");
		_hashmap.put("��:ϵ�в�������","s");
		_hashmap.put("��:������","s");
		_hashmap.put("��:����������","s");
		_hashmap.put("��:�涯��ʽ","s");
		_hashmap.put("��:ҳüҳ��λ��","s");
		//_hashmap.put("��:��Ԫ��ֵ","s");
		_hashmap.put("��:ĸ�漯","p0035");
		_hashmap.put("��:��Ԫ��ֵ","s");         
		_hashmap.put("��:ĸ��","p0036.��ʶ�� ���� ���� ҳ���������� ��ɫ�������� ҳ���ʽ���� �ı�ʽ������"); 
		_hashmap.put("��:�õ�Ƭ��","p0039");
		_hashmap.put("��:�õ�Ƭ","p0040.���� ��ʶ�� ĸ������ ��ɫ�������� ҳ���ʽ���� ��ʾ ��ʾ���� ��ʾ��������");
		_hashmap.put("��:����","p0042");
		_hashmap.put("��:����","p0043.�������� ��������");
		_hashmap.put("��:��ʱ","p0067.�¼� ��ʱ �ٶ� �ظ� �ؾ�");
		_hashmap.put("��:��ǿ","p0068");
		_hashmap.put("��:Ч��","p0069");
		_hashmap.put("��:�������ź�","p0070.��ɫ �䰵 ���ź����� ����������");
		_hashmap.put("��:�����ı�","p0071.���� ��� ������״ �෴˳��");
		_hashmap.put("��:����","p0073");
		_hashmap.put("��:�˳�","p0074");
		_hashmap.put("��:ǿ��","p0075");

		_hashmap.put("��:��Ҷ��","p0080.�ٶ� ����");
		_hashmap.put("��:��:����","p0081");
		_hashmap.put("��:��״","p0082.�ٶ� ����");
		_hashmap.put("��:����״","p0083.�ٶ� ����");
		_hashmap.put("��:����","p0084.�ٶ� ����״");
		_hashmap.put("��:����","p0085.�ٶ� ����");
		_hashmap.put("��:��˸һ��","p0086.�ٶ�");
		_hashmap.put("��:ʮ������չ","p0087.�ٶ� ����");
		_hashmap.put("��:���Ч��","p0088");
		_hashmap.put("��:Բ����չ","p0089.�ٶ� ����");
		_hashmap.put("��:����","p0090.�ٶ� ����");
		_hashmap.put("��:����","p0091.�ٶ� ����");
		_hashmap.put("��:��������","p0092.�ٶ� ����");
		_hashmap.put("��:����","p0093.�ٶ� ����");
		_hashmap.put("��:����","p0094.�ٶ� ����");
		_hashmap.put("��:����","p0095.�ٶ� ����");
		_hashmap.put("��:����չ��","p0096.�ٶ�");
		_hashmap.put("��:�������","p0097.�ٶ� ����");
		_hashmap.put("��:�����ܽ�","p0098.�ٶ�");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("��:���������ɫ","p0124.�ٶ� ��ɫ");
		_hashmap.put("��:����������ɫ","p0121.�ٶ� ��ɫ");
		_hashmap.put("","");
		_hashmap.put("��:����·��","p0133.·��");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		
		_hashmap.put("��:����","p0061.Ԥ�������� �Զ�������");
		//_hashmap.put("��:����","p0056");
		_hashmap.put("��:�õ�Ƭ��ע","p0054.��עĸ������");
		_hashmap.put("��:������λ","p0055");
		_hashmap.put("��:����","p0057");
		_hashmap.put("��:�л�","p0058.Ч�� �ٶ�");
		_hashmap.put("��:��ʽ","p0062");
		_hashmap.put("��:�������","p0065");
		_hashmap.put("��:ʱ����","p0066");
		_hashmap.put("��:�õ�Ƭ����","p0022.��ʶ�� ���� �Զ���");
		_hashmap.put("��:��ӳ˳��","p0023.���� ��������");
		_hashmap.put("��:ȫ����ӳ","p0024");
		_hashmap.put("��:ѭ����ӳ","p0025");
		_hashmap.put("��:��ӳ���","p0026");
		_hashmap.put("��:�ֶ���ʽ","p0027");
		_hashmap.put("��:��������","p0029");
		_hashmap.put("��:��ӳ����","p0030");
		_hashmap.put("��:ǰ����ʾ","p0031");
		_hashmap.put("��:������ת","p0032");
		_hashmap.put("��:��������","p0033");
		//_hashmap.put("��:����","p0038");
		_hashmap.put("��:���ô������","p0000");
		_hashmap.put("��:ҳ�����ü�","p0001");
		_hashmap.put("��:ҳ������","p0002.��ʶ�� ����");
		_hashmap.put("��:��ɫ������","p0007");
		_hashmap.put("��:��ɫ����","p0008.��ʶ�� ���� ��");
		_hashmap.put("��:ҳ���ʽ��","p0017");
		_hashmap.put("��:ҳ���ʽ","p0018.��ʶ�� ����");
		_hashmap.put("��:����","p0129.����");
		_hashmap.put("��:ռλ��","p0130.����");
		_hashmap.put("��:��ʾ����","p0020");
		_hashmap.put("��:��ӳ����","p0021");
		_hashmap.put("��:����","p0034");
		_hashmap.put("��:����ɫ","p0009");
		_hashmap.put("��:�ı�������","p0010");
		_hashmap.put("��:��Ӱ","p0011");
		_hashmap.put("��:�����ı�","p0012");
		_hashmap.put("��:���","p0013");
		//_hashmap.put("��:ǿ��","p0014");
		_hashmap.put("��:ǿ���ͳ�������","p0015");
		_hashmap.put("��:ǿ����β�泬������","p0016");
		_hashmap.put("��:ֽ��","p0003.��� �߶� ֽ��");
		_hashmap.put("��:ҳ�߾�","p0004.�� �� �� ��");
		_hashmap.put("��:ҳ���ʽ","p0005");
		_hashmap.put("��:ֽ�ŷ���","p0006");
		_hashmap.put("��:�ı�ʽ����","p0131");
		_hashmap.put("��:�ı�ʽ��","p0132.��ʶ�� ����");
		_hashmap.put("��:����ʽ��","p0135.��ʶ�� ���� ���� ���� ��ʽ������");
		
		_hashmap.put("ͼ:ͼ��","g0000.��� ��ʶ�� ����б� ��������");
		_hashmap.put("ͼ:���","g0012");
		_hashmap.put("ͼ:����ɫ","g0013");
		_hashmap.put("ͼ:����","g0014");
		_hashmap.put("ͼ:�ߴ�ϸ","g0016");
		_hashmap.put("ͼ:ǰ�˼�ͷ","g0017");
//		_hashmap.put("ͼ:ʽ��","g0018");
//		_hashmap.put("ͼ:��С","g0019");
		_hashmap.put("ͼ:��˼�ͷ","g0020");
//		_hashmap.put("ͼ:ʽ��","g0021");                                         
//		_hashmap.put("ͼ:��С","g0022");
		_hashmap.put("ͼ:���","g0023");
		_hashmap.put("ͼ:�߶�","g0024");
		_hashmap.put("ͼ:��ת�Ƕ�","g0025");
		_hashmap.put("ͼ:X�����ű���","g0026");
		_hashmap.put("ͼ:Y�����ű���","g0027");
		_hashmap.put("ͼ:�����ݺ��","g0028");
		_hashmap.put("ͼ:���ԭʼ����","g0029");
		_hashmap.put("ͼ:��ӡ����","g0032");
		_hashmap.put("ͼ:Web����","g0033");
		_hashmap.put("ͼ:͸����","g0038");
		_hashmap.put("ͼ:svgͼ�ζ���","g0005");
		_hashmap.put("ͼ:Ԥ����ͼ��","g0005");
		_hashmap.put("ͼ:���","g0006");
		_hashmap.put("ͼ:����","g0007");
		_hashmap.put("ͼ:�������","g0008");
		_hashmap.put("ͼ:�ؼ�������","g0009.·��");
		_hashmap.put("ͼ:����","g0010.x���� y����");
		_hashmap.put("ͼ:����","g0011");
		_hashmap.put("ͼ:�ı�����","g0002.�ı��� ��߾� �ұ߾� �ϱ߾� �±߾� ˮƽ���� ��ֱ���� �������з��� �Զ����� ��С��Ӧ���� ǰһ���� ��һ����");
		_hashmap.put("ͼ:���Ƶ�","g0003");
		_hashmap.put("ͼ:��ɫ","g0034");
		_hashmap.put("ͼ:ͼƬ","g0035.λ�� ͼ������ ���� ����");
		_hashmap.put("ͼ:ͼ��","g0036.���� ͼ������ ǰ��ɫ ����ɫ");
		_hashmap.put("ͼ:����","g0037.��ʼɫ ��ֹɫ �������� ��ʼŨ�� ��ֹŨ�� ���䷽�� �߽� ����Xλ�� ����Yλ�� ����");
		_hashmap.put("ͼ:���λ��","g0041.x���� y����");
		_hashmap.put("ͼ:��ת","g0040.����");
	}
	
	//return @uof:locID and @uof:attrList 
	//for the specified element
	public static String get_locID_atts(String qName){
		String atts = "";
		int index = 0;
		String str = _hashmap.get(qName);
		
		if(str != null){
			index = str.indexOf(".");
			if(index != -1){
				atts += " uof:locID=\"" + str.substring(0,index) + "\"";
				atts += " uof:attrList=\"" + str.substring(index+1) + "\"";
			}
			else {
				atts += " uof:locID=\"" + str + "\"";
			}
		}
		
		return atts;
	}
	
	
	private static void create_exit_eff_map(){
		_exit_eff_map.put("��:��Ҷ��","p0100.�ٶ� ����");
		_exit_eff_map.put("��:�ɳ�","p0101.�ٶ� ����");
		_exit_eff_map.put("��:�����Ƴ�","p0102.�ٶ� ����");
		_exit_eff_map.put("��:����","p0103.�ٶ� ����");
		_exit_eff_map.put("��:����","p0104.�ٶ� ����");
		_exit_eff_map.put("��:�г�","p0105.�ٶ� ����");
		_exit_eff_map.put("��:����չ��","p0106.�ٶ�");
		_exit_eff_map.put("��:�������","p0107.�ٶ� ����");
		_exit_eff_map.put("��:�����ܽ�","p0108.�ٶ�");
		_exit_eff_map.put("��:Բ����չ","p0109.�ٶ� ����");
		_exit_eff_map.put("��:����","p0110.�ٶ� ����");
		_exit_eff_map.put("��:��״","p0111.�ٶ� ����");
		_exit_eff_map.put("��:����״","p0112.�ٶ� ����");
		_exit_eff_map.put("��:����","p0113.�ٶ� �ַ�");
		_exit_eff_map.put("��:����","p0114.�ٶ� ����");
		_exit_eff_map.put("��:��˸һ��","p0115.�ٶ�");
		_exit_eff_map.put("��:ʮ������չ","p0116.�ٶ� ����");
		_exit_eff_map.put("��:���Ч��","p0117");
		_exit_eff_map.put("��:��ʧ","p0118");
		_exit_eff_map.put("��:����","p0119");
	}

	public static String get_exit_eff_atts(String act){
		String atts = "";
		int index = 0;
		String qName = "";
		String str = "";
		
		if(act.contains(" ")){
			qName = act.substring(0,act.indexOf(" "));
		}
		else {
			qName = act;
		}
		
		str = _exit_eff_map.get(qName);
		
		if(str != null){
			index = str.indexOf(".");
			if(index != -1){
				atts += " uof:locID=\"" + str.substring(0,index) + "\"";
				atts += " uof:attrList=\"" + str.substring(index+1) + "\"";
			}
			else {
				atts += " uof:locID=\"" + str + "\"";
			}
		}
		
		return atts;
	}
}