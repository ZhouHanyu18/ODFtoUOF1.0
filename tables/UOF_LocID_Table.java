package tables;

import java.util.HashMap;
import java.util.Map;

public class UOF_LocID_Table {
	//
	private static Map<String,String> 
		_hashmap = new HashMap<String,String>();
	
	//locID table for children of <演:退出>
	private static Map<String,String>
		_exit_eff_map = new HashMap<String,String>();
	
	
	public UOF_LocID_Table() { 
		
	}
	
	public static String locID_attrList(String qName){
		return _hashmap.get(qName);
	}
	
	public static void create_map() {
		create_exit_eff_map();
		
		_hashmap.put("字:水平","t0176.相对于");
		_hashmap.put("字:垂直","t0179.相对于");		
		_hashmap.put("uof:UOF","u0000");
		_hashmap.put("uof:字体声明","u0041.标识符 名称 字体族");
		_hashmap.put("uof:元数据","u0001");
		_hashmap.put("uof:书签集","u0027");
		_hashmap.put("uof:书签","u0028.名称");
		_hashmap.put("uof:链接集","u0031");
		_hashmap.put("uof:超级链接","u0032.标识符 目标 书签 式样引用 已访问式样引用 提示 链源");
		_hashmap.put("uof:对象集","u0033");
		_hashmap.put("uof:数学公式","u0034 标识符"); 
		_hashmap.put("uof:其他对象","u0036.标识符 内嵌 公共类型 私有类型");
		_hashmap.put("uof:数据","u0037");
		_hashmap.put("uof:路径","u0038.标识符 内嵌 公共类型 私有类型");//这个路径是对象集的路径,在其他地方仍然有路径
		_hashmap.put("uof:用户数据集","u0068");
		_hashmap.put("uof:式样集","u0039");
		_hashmap.put("uof:文字处理","u0047");
		_hashmap.put("uof:演示文稿","u0048");
		_hashmap.put("uof:电子表格","u0049");
		_hashmap.put("uof:扩展区","u0050");
		_hashmap.put("uof:扩展","u0051");
		_hashmap.put("uof:软件名称","u0052");
		_hashmap.put("uof:软件版本","u0053");
		_hashmap.put("uof:扩展内容","u0054");
		//_hashmap.put("uof:路径","u0065");//这个路径是扩展区的路径,扩展内容的子元素
		//_hashmap.put("uof:路径","u0067");//uof:停止引用 的路径
		_hashmap.put("uof:内容","u0056");
		_hashmap.put("uof:锚点","u0064.x坐标 y坐标 宽度 高度 图形引用 随动方式 缩略图 占位符");
		_hashmap.put("uof:文本位置","u0029.区域引用");
		_hashmap.put("uof:命名表达式","u0030.行列区域 工作表名");
		_hashmap.put("uof:标题","u0002");
		_hashmap.put("uof:主题","u0003");
		_hashmap.put("uof:创建者","u0004");
		_hashmap.put("uof:作者","u0005");
		_hashmap.put("uof:最后作者","u0006");
		_hashmap.put("uof:摘要","u0007");
		_hashmap.put("uof:创建日期","u0008");
		_hashmap.put("uof:编辑次数","u0009");
		_hashmap.put("uof:编辑时间","u0010");
		_hashmap.put("uof:创建应用程序","u0011");
		_hashmap.put("uof:分类","u0012");
		_hashmap.put("uof:文档模板","u0013");
		_hashmap.put("uof:关键字集","u0014");
		_hashmap.put("uof:关键字","u0015");
		_hashmap.put("uof:用户自定义元数据集","u0016");
		_hashmap.put("uof:用户自定义元数据","u0017.名称 类型");
		_hashmap.put("uof:公司名称","u0018");
		_hashmap.put("uof:经理名称","u0019");
		_hashmap.put("uof:页数","u0020");
		_hashmap.put("uof:字数","u0021");
		_hashmap.put("uof:英文字符数","u0022");
		_hashmap.put("uof:中文字符数","u0023");
		_hashmap.put("uof:行数","u0024");
		_hashmap.put("uof:段落数","u0025");
		_hashmap.put("uof:对象数","u0026");
		_hashmap.put("uof:字体集","u0040");
		_hashmap.put("uof:字体声明","u0041.标识符 名称 字体族");
		_hashmap.put("uof:自动编号集","u0042");
		_hashmap.put("uof:句式样","u0043.标识符 名称 类型 别名 基式样引用");
		_hashmap.put("uof:段落式样","u0044.标识符 名称 类型 别名 基式样引用");

		_hashmap.put("uof:文字表式样","u0045.标识符 名称 类型 别名 基式样引用");
		_hashmap.put("uof:单元格式样","u0046.标识符 名称 类型 别名 基式样引用");
		_hashmap.put("uof:左","u0057.类型 宽度 边距 颜色 阴影");//
		_hashmap.put("uof:上","u0058.类型 宽度 边距 颜色 阴影");
		_hashmap.put("uof:右","u0059.类型 宽度 边距 颜色 阴影");
		_hashmap.put("uof:下","u0060.类型 宽度 边距 颜色 阴影");
		_hashmap.put("uof:对角线1","u0061.类型 宽度 边距 颜色 阴影");
		_hashmap.put("uof:对角线2","u0062.类型 宽度 边距 颜色 阴影");

		_hashmap.put("字:句","t0085");
		_hashmap.put("字:文字表","t0128.类型");
		_hashmap.put("字:段落","t0051.标识符");

		_hashmap.put("字:句属性","t0086.式样引用");
		_hashmap.put("字:字体","t0088.西文字体引用 中文字体引用 特殊字体引用 西文绘制 字号 相对字号 颜色");
		_hashmap.put("字:粗体","t0089.值");
		_hashmap.put("字:斜体","t0090.值");
		_hashmap.put("字:突出显示","t0091");
//		_hashmap.put("字:边框","t0092.类型 宽度 边距 颜色 阴影");
//		_hashmap.put("字:填充","t0093");
		_hashmap.put("字:删除线","t0094.类型");
		_hashmap.put("字:下划线","t0095.类型 颜色 字下划线");
		_hashmap.put("字:着重号","t0096.类型 颜色 字着重号");
		_hashmap.put("字:隐藏文字","t0097.值");
		_hashmap.put("字:空心","t0098.值");
		_hashmap.put("字:浮雕","t0099.类型");
		_hashmap.put("字:阴影","t0100.值");
		_hashmap.put("字:醒目字体","t0101.类型");
//		_hashmap.put("字:句式样/字:位置","t0102");
//   	_hashmap.put("字:缩放","t0103");
//		_hashmap.put("字:缩放","t0003");
		_hashmap.put("字:字符间距","t0104");
		_hashmap.put("字:调整字间距","t0105");
		_hashmap.put("字:字符对齐网格","t0106.值");
		_hashmap.put("字:上下标","t0205.值");
		
		//_hashmap.put("字:宽度","t0130.绝对宽度 相对宽度");//文字表式样的子元素
		_hashmap.put("字:列宽集","t0131");
		_hashmap.put("字:列宽","t0132");
		//_hashmap.put("字:对齐","t0133");
		_hashmap.put("字:左缩进","t0134");
		_hashmap.put("字:绕排","t0135.值");
		_hashmap.put("字:绕排边距","t0135.上 左 下 右");
//		_hashmap.put("字:位置","t0136");
		//_hashmap.put("字:边框","t0137");
		_hashmap.put("字:自动调整大小","t0140.值");
		_hashmap.put("字:默认单元格边距","t0141.上 左 右 下");
		_hashmap.put("字:默认单元格间距","t0142");

		_hashmap.put("uof:停止引用","u0066");

        //_hashmap.put("字:格式修订","");//格式修订,这个是段落式样的子元素
        _hashmap.put("字:大纲级别","t0054");
        //_hashmap.put("字:对齐","t0055.水平对齐 文字对齐");
        //_hashmap.put("字:缩进","t0056");//段落式样子元素

		_hashmap.put("字:左","t0182");
		_hashmap.put("字:右","t0183");
		_hashmap.put("字:首行","t0184");//以上三个元素每个都分别有两个子元素,字:绝对和字:相对,但是有不同的localid,因此需要写在程序里
        _hashmap.put("字:行距","t0057.类型 值");
		_hashmap.put("字:段间距","t0058");
		_hashmap.put("字:段前距","t0196");
		_hashmap.put("字:段后距","t0197");

        _hashmap.put("字:段间距","t0058");
        _hashmap.put("字:自动编号信息","t0059.编号引用 编号级别 重新编号 起始编号");
        _hashmap.put("字:孤行控制","t0060");
        _hashmap.put("字:寡行控制","t0061");
        _hashmap.put("字:段中不分页","t0062.值");
        _hashmap.put("字:与下段同页","t0063.值");
        _hashmap.put("字:段前分页","t0064.值");
        //_hashmap.put("字:边框","t0065");//段落式样的子元素
        //_hashmap.put("字:填充","t0066");
        _hashmap.put("字:制表位设置","t0067");
        _hashmap.put("字:制表位","t0068.位置 类型 前导符 制表位字符");
        _hashmap.put("字:对齐网格","t0069.值");
		_hashmap.put("字:首字下沉","t0070.类型 字体引用 字符数 行数 间距");

		_hashmap.put("字:取消断字","t0071.值");
		_hashmap.put("字:取消行号","t0072.值");
		_hashmap.put("字:允许单词断字","t0073.值");
		_hashmap.put("字:行首尾标点控制","t0074.值");
		_hashmap.put("字:是否行首标点压缩","t0075.值");
		_hashmap.put("字:中文习惯首尾字符","t0076.值");
		_hashmap.put("字:自动调整中英文字符间距","t0077.值");
		_hashmap.put("字:自动调整中文与数字间距","t0078.值");
		_hashmap.put("字:有网格自动调整右缩进","t0195.值");//新增的
		
		_hashmap.put("字:主体","t0016");
		_hashmap.put("字:分节","t0017.名称");
		_hashmap.put("字:逻辑章节","t0050");

		_hashmap.put("字:文字表","t0128.式样引用");
		_hashmap.put("字:公用处理规则","t0000");
		_hashmap.put("字:当前视图","t0002");
		_hashmap.put("字:文档设置","t0001");
		_hashmap.put("字:修订","t0005.值");
		_hashmap.put("字:度量单位","t0006");
		_hashmap.put("字:标点禁则","t0007");
		_hashmap.put("字:行首字符","t0008");
		_hashmap.put("字:行尾字符","t0009");
		_hashmap.put("字:用户集","t0010");
		_hashmap.put("字:用户","t0011.标识符 姓名");
		_hashmap.put("字:修订信息集","t0012");
		_hashmap.put("字:修订信息","t0013.标识符 作者 日期");
		_hashmap.put("字:批注集","t0014");
		_hashmap.put("字:批注","t0015");
		_hashmap.put("字:分栏","t0049.栏数 等宽 分隔线 分隔线宽度 分隔线颜色");
		_hashmap.put("字:栏","t0050.宽度 间距");
		_hashmap.put("字:节属性","t0018");
		_hashmap.put("字:删除","t0084.修订信息引用");

		//_hashmap.put("字:宽度","t0150.绝对值 相对值");
		//_hashmap.put("字:宽度","t0150");//单元格属性的子元素
		//_hashmap.put("字:边框","t0152");//单元格属性的子元素

		_hashmap.put("字:单元格边距","t0151.上 左 右 下");
		//_hashmap.put("字:填充","t0153");//单元格属性的子元素

		_hashmap.put("字:垂直对齐方式","t0154");
		_hashmap.put("字:跨行","t0155.值");
		_hashmap.put("字:跨列","t0156.值");
		_hashmap.put("字:自动换行","t0157.值");
		_hashmap.put("字:适应文字","t0158.值");

		_hashmap.put("字:单元格","t0148");
		_hashmap.put("字:单元格属性","t0149");
		//_hashmap.put("字:格式修订","t0087");//句属性的子元素
		//_hashmap.put("字:格式修订","t0169");//句式样的子元素
		_hashmap.put("字:句式样","t0170");
		_hashmap.put("字:句","t0085");
		_hashmap.put("字:脚注","t0107.引文体");
		_hashmap.put("字:尾注","t0108.引文体");
		_hashmap.put("字:文本串","t0109.udsPath");

		_hashmap.put("字:区域开始","t0121.标识符 名称 类型");
		_hashmap.put("字:区域结束","t0122.标识符引用");
		_hashmap.put("字:制表符","t0123");
		_hashmap.put("字:换行符","t0124");
		_hashmap.put("字:分栏符","t0125");
		_hashmap.put("字:空格符","t0126.个数");
		_hashmap.put("字:分页符","t0127");
		_hashmap.put("字:批注","t0015.区域引用 作者 日期");
        _hashmap.put("字:批注集","t0014");
		_hashmap.put("字:插入结束","t0083");
		_hashmap.put("字:文字处理","t0047");
		_hashmap.put("字:公用处理规则","t0000");
		_hashmap.put("字:文字表式样","u0045.标识符 名称 类型 别名 基式样引用");
		_hashmap.put("字:文字表","t0128");
		_hashmap.put("字:文字表属性","t0129.式样引用");
		_hashmap.put("字:文字设置","t");
		//_hashmap.put("字:格式修订","t0053");//段落属性的子元素
		_hashmap.put("字:段落属性","t0052.式样引用");

		_hashmap.put("字:段落式样类型","t");
		//_hashmap.put("字:格式修订","t0173");//段落式样子元素
		_hashmap.put("字:段落式样","t0174");//段落式样中的格式修订的子元素...
		_hashmap.put("字:句","t0085");
		_hashmap.put("字:域开始","t0079.类型 锁定");
		_hashmap.put("字:域代码","t0080");
		_hashmap.put("字:域结束","t0081");
		_hashmap.put("字:插入开始","t0082.修订信息引用");
		_hashmap.put("字:插入结束","t0083");
		_hashmap.put("字:用户","t0011.标识符 姓名");
        _hashmap.put("字:用户集","t0010");
		_hashmap.put("字:自动编号","t0169.标识符 名称 父编号引用 多级编号");
		_hashmap.put("字:自动编号集","t0042");
		_hashmap.put("字:级别","t0159.级别值 编号对齐方式 尾随字符");
		_hashmap.put("字:项目符号","t0171");
		_hashmap.put("字:符号字体","t0160.式样引用");
		_hashmap.put("字:链接式样引用","t0161");
		_hashmap.put("字:编号格式","t0162");
		_hashmap.put("字:编号格式表示","t0163");
		_hashmap.put("字:图片符号引用","t0164");
		//_hashmap.put("字:缩进","t0165");//自动编号的子元素

		_hashmap.put("字:制表符位置","t0166");
		_hashmap.put("字:起始编号","t0167");
		_hashmap.put("字:正规格式","t0168");
		_hashmap.put("字:节属性","t0018");
		//_hashmap.put("字:格式修订","t0019");
		_hashmap.put("字:节类型","t0020");
		_hashmap.put("字:页边距","t0021.左 上 右 下");
		_hashmap.put("字:纸张","t0022.纸型 宽度 高度");
		_hashmap.put("字:奇偶页页眉页脚不同","t0023.值");
		_hashmap.put("字:首页页眉页脚不同","t0024.值");
		_hashmap.put("字:页眉位置","t0025");
		_hashmap.put("字:页脚位置","t0026");
		_hashmap.put("字:页眉","t0027");
		_hashmap.put("字:奇数页页眉","t0028");
		_hashmap.put("字:偶数页页眉","t0029");
		_hashmap.put("字:首页页眉","t0030");
		_hashmap.put("字:页脚","t0031");
		_hashmap.put("字:奇数页页脚","t0032");
		_hashmap.put("字:偶数页页脚","t0033");
		_hashmap.put("字:首页页眉","t0034");
		_hashmap.put("字:装订线","t0035");
		_hashmap.put("字:对称页边距","t0036");
		_hashmap.put("字:拼页","t0037");
		_hashmap.put("字:纸张方向","t0038");
		_hashmap.put("字:纸张来源","t0039");
		_hashmap.put("字:脚注设置","t0040.位置 格式 起始编号 编号方式");
		_hashmap.put("字:尾注设置","t0041.位置 格式 起始编号 编号方式");	
		_hashmap.put("字:页码设置","t0042.首页显示 格式 包含章节号 章节起始样式引用 分隔符 起始编号");
		_hashmap.put("字:行号设置","t0043.使用行号 编号方式 起始编号 距边界 行号间隔");
		_hashmap.put("字:网格设置","t0044.网格类型 宽度 高度 显示网格 打印网格");
		//_hashmap.put("字:垂直对齐方式","t0045");
		_hashmap.put("字:文字排列方向","t0046");
		//_hashmap.put("字:边框","t0047");//节属性的子元素
		//_hashmap.put("字:填充","t0048");
		//_hashmap.put("字:分栏","t0049");
		//_hashmap.put("字:高度","t0145");
		_hashmap.put("字:跨页","t0146.值");
		_hashmap.put("字:表头行","t0147.值");
		_hashmap.put("字:行","t0143");
		_hashmap.put("字:表行属性","t0144");
		_hashmap.put("字:逻辑章节","t0050");
		_hashmap.put("字:锚点","t0110.标识符 类型");
		_hashmap.put("字:锚点属性","t0111");
		//_hashmap.put("字:宽度","t0112");//锚点属性的子元素
		//_hashmap.put("字:高度","t0113");
//		_hashmap.put("字:位置","t0114");//锚点的子元素
		_hashmap.put("字:绕排","t0115.绕排方式 环绕文字 绕排顶点");
		_hashmap.put("字:边距","t0116.上 左 右 下");
		_hashmap.put("字:锁定","t0117");
		_hashmap.put("字:保护","t0118");
		_hashmap.put("字:图形","t0120.图形引用");
		_hashmap.put("字:允许重叠","t0119");
		_hashmap.put("字:页眉页脚","t0111");
//		_hashmap.put("字:位置","t0136");//这个是文字表式样的子元素

		_hashmap.put("表:图形","s0054");
		_hashmap.put("表:图表","s0055.类型 子类型 宽度 高度 x坐标 y坐标 随动方式");
		_hashmap.put("表:填充","s0058");
		_hashmap.put("表:字体","s0059");
		//_hashmap.put("表:对齐","s0020");
		_hashmap.put("表:操作码","s0009");
		_hashmap.put("表:第一操作数","s0010");
		_hashmap.put("表:第二操作数","s0011");
		_hashmap.put("表:线性","s0062");//？？？
	//	_hashmap.put("表:边框","s0057");
		_hashmap.put("表:度量单位","s0001");
		_hashmap.put("表:精确度以显示值为准","s0002");
		_hashmap.put("表:日期系统－1904","s0003");
		_hashmap.put("表:计算设置","s0004");
		_hashmap.put("表:数据有效性集","s0005");
		_hashmap.put("表:数据有效性","s0006");
		_hashmap.put("表:条件格式化集","s0016");
		_hashmap.put("表:是否RC引用","s0124.值");
		_hashmap.put("表:条件格式化","s0017");
		_hashmap.put("表:批注","s0053.是否显示");
		_hashmap.put("表:最小值","s0065");
		_hashmap.put("表:最大值","s0066");
		_hashmap.put("表:主单位","s0067");
		_hashmap.put("表:次单位","s0068");
		_hashmap.put("表:分类交叉点","s0069");
		_hashmap.put("表:单位","s0070");
		_hashmap.put("表:显示单位","s0071");
		_hashmap.put("表:对数","s0072");
		_hashmap.put("表:数值次序反转","s0073");
		_hashmap.put("表:分类标签数","s0075");
		_hashmap.put("表:数值交叉点","s0074");
		_hashmap.put("表:分类刻度数","s0076");
		_hashmap.put("表:分类次序反转","s0077");
		
		_hashmap.put("表:数字格式","s0021.分类名称 格式码");
//		_hashmap.put("表:边框","s0022");			//单元格式样的子元素
		_hashmap.put("表:数据","s0051.数据类型");
		//_hashmap.put("表:公式","s0052");
		_hashmap.put("表:图表区","s0056");
		_hashmap.put("表:绘图区","s0060");
		_hashmap.put("表:分类轴","s0061.主刻度类型 次刻度类型 刻度线标志");
		_hashmap.put("表:数值轴","s0082.主刻度类型 次刻度类型 刻度线标志");
		_hashmap.put("表:图例","s0083.位置");
		_hashmap.put("表:图例项","s0084");
		_hashmap.put("表:数据表","s0085");
		_hashmap.put("表:数据系列集","s0086");
		_hashmap.put("表:数据系列","s0087.系列");
		_hashmap.put("表:数据点集","s0090");
		_hashmap.put("表:数据点","s0091.系列 点");
		_hashmap.put("表:网格线集","s0092");
		_hashmap.put("表:网格线","s0093.类型 宽度 边距 颜色 阴影 位置");
		_hashmap.put("表:数据源","s0094.数据区域 系列产生");
		_hashmap.put("表:系列","s0095.系列名 系列值 分类名");
		_hashmap.put("表:标题集","s0096");
		_hashmap.put("表:标题","s0097.名称 位置");
		_hashmap.put("表:坐标轴类型","s0098");
		_hashmap.put("表:数值","s0063.链接到源 分类名称 格式码");
		_hashmap.put("表:刻度","s0064");
		_hashmap.put("表:线型","s0062.类型 宽度 边距 颜色 阴影");
//		_hashmap.put("表:对齐","s0078");
//		_hashmap.put("表:文字方向","s0079");
		_hashmap.put("表:旋转角度","s0080");
		_hashmap.put("表:偏移量","s0081");
		
		_hashmap.put("表:单元格式样","u0046.标识符 名称 类型");
		_hashmap.put("表:字体格式","s0113.式样引用");
		_hashmap.put("表:对齐格式","s0114");
		_hashmap.put("表:水平对齐方式","s0115");
		_hashmap.put("表:垂直对齐方式","s0116");
		_hashmap.put("表:缩进","s0117");//对齐格式的子元素
//		_hashmap.put("表:文字方向","s0118");
		_hashmap.put("表:文字旋转角度","s0119");
		_hashmap.put("表:自动换行","s0120");
		_hashmap.put("表:缩小字体填充","s0121.值");
		//_hashmap.put("表:列","s0048");
		//_hashmap.put("表:行","s0049.行号 隐藏 行高 式样引用 跨度");
		_hashmap.put("表:单元格","s0050.列号 式样引用 超链接引用 合并列数 合并行数");
		_hashmap.put("表:分组集","s0098");
		//_hashmap.put("表:列","s0099");//分组集的子元素
		//_hashmap.put("表:行","s0100");//分组集的子元素
		_hashmap.put("表:标签前景色","s0027");
		_hashmap.put("表:标签背景色","s0028");
		_hashmap.put("表:页面设置","s0029");
		_hashmap.put("表:视图","s0035.窗口标识符");
		_hashmap.put("表:选中","s0036.值");
		_hashmap.put("表:拆分","s0037");
		_hashmap.put("表:冻结","s0038");
		_hashmap.put("表:最上行","s0039");
		_hashmap.put("表:最左列","s0040");
		_hashmap.put("表:当前视图","s0041");
		//_hashmap.put("表:公式","s0042.值");
		_hashmap.put("表:网格","s0043.值");
		_hashmap.put("表:网格颜色","s0044");
		_hashmap.put("表:缩放","s0045");//工作表属性的子元素
		_hashmap.put("表:分页缩放","s0046");
		_hashmap.put("表:选中区域","s0047");
		_hashmap.put("表:工作表属性","s0026");
		_hashmap.put("表:工作表内容","s0018.最大行 最大列 缺省行高 缺省列宽");
		_hashmap.put("表:筛选","s0101");
		_hashmap.put("表:分页符集","s0111");
		_hashmap.put("表:分页符","s0112");
		_hashmap.put("表:数值有效性","s");
		_hashmap.put("表:校验类型","s0008");
		_hashmap.put("表:忽略空格","s0012.值");
		_hashmap.put("表:下拉箭头","s0013");
		_hashmap.put("表:输入提示","s0014.显示 标题 内容");
		_hashmap.put("表:错误提示","s0015.显示 类型 标题 内容");
		_hashmap.put("表:数据标志","s");
		_hashmap.put("表:显示标志","s0088.系列名 类别名 数值 百分数 分隔符 图例标志");
		_hashmap.put("表:系列名","s0089");
		//_hashmap.put("表:条件","s0019");
		_hashmap.put("表:格式","s0023.表:式样引用");
		_hashmap.put("表:校验条件","s");
		_hashmap.put("表:值","s0107");
		_hashmap.put("表:电子表格","u0049");
		_hashmap.put("表:公用处理规则","s0000");
		_hashmap.put("表:主体","s0024");
		_hashmap.put("表:工作表","s0025.标识符 名称 隐藏 背景 式样引用");
		
		_hashmap.put("表:筛选","s0101.类型");
		_hashmap.put("表:范围","s0102");
		//_hashmap.put("表:条件","s0103.列号");
		_hashmap.put("表:普通","s0104.类型 值");
		_hashmap.put("表:自定义","s0105.类型");
		_hashmap.put("表:操作条件","s0106");
		
		_hashmap.put("表:条件区域","s0108");
		_hashmap.put("表:结果区域","s0109");
		_hashmap.put("表:页面设置","s0029.名称");
		_hashmap.put("表:纸张","s0030.纸型 宽度 高度");
		_hashmap.put("表:纸张方向","s0031.");
		_hashmap.put("表:缩放","s0032");
		_hashmap.put("表:页边距","s0033.左 上 右 下");  
		_hashmap.put("表:页眉页脚","s0034.位置");
		_hashmap.put("表:偏移量","s");
		_hashmap.put("表:刻度标志","s");
		_hashmap.put("表:刻度线类型","s");          
		_hashmap.put("表:区域","s0007");
		_hashmap.put("表:单元格值","s");     
		_hashmap.put("表:单元格数据类型","s");
		_hashmap.put("表:命名表达式","s"); 
		_hashmap.put("表:图例位置","s");  
		_hashmap.put("表:垂直对齐","s"); 
		_hashmap.put("表:数值分类名称","s");
		_hashmap.put("表:显示单位","s0071.值"); 
		_hashmap.put("表:普通筛选类型","s");  
		_hashmap.put("表:条件操作符","s");
		_hashmap.put("表:标题名","s");
		//_hashmap.put("表:校验类型","s");
		_hashmap.put("表:水平对齐","s");
		_hashmap.put("表:系列产生类型","s");
		_hashmap.put("表:缩进量","s");
		_hashmap.put("表:网格线类型","s");
		_hashmap.put("表:随动方式","s");
		_hashmap.put("表:页眉页脚位置","s");
		//_hashmap.put("演:单元格值","s");
		_hashmap.put("演:母版集","p0035");
		_hashmap.put("表:单元格值","s");         
		_hashmap.put("演:母版","p0036.标识符 名称 类型 页面设置引用 配色方案引用 页面版式引用 文本式样引用"); 
		_hashmap.put("演:幻灯片集","p0039");
		_hashmap.put("演:幻灯片","p0040.名称 标识符 母版引用 配色方案引用 页面版式引用 显示 显示背景 显示背景对象");
		_hashmap.put("演:动画","p0042");
		_hashmap.put("演:序列","p0043.段落引用 动画对象");
		_hashmap.put("演:定时","p0067.事件 延时 速度 重复 回卷");
		_hashmap.put("演:增强","p0068");
		_hashmap.put("演:效果","p0069");
		_hashmap.put("演:动画播放后","p0070.颜色 变暗 播放后隐藏 单击后隐藏");
		_hashmap.put("演:动画文本","p0071.发送 间隔 动画形状 相反顺序");
		_hashmap.put("演:进入","p0073");
		_hashmap.put("演:退出","p0074");
		_hashmap.put("演:强调","p0075");

		_hashmap.put("演:百叶窗","p0080.速度 方向");
		_hashmap.put("演:演:出现","p0081");
		_hashmap.put("演:盒状","p0082.速度 方向");
		_hashmap.put("演:阶梯状","p0083.速度 方向");
		_hashmap.put("演:轮子","p0084.速度 辐射状");
		_hashmap.put("演:棋盘","p0085.速度 方向");
		_hashmap.put("演:闪烁一次","p0086.速度");
		_hashmap.put("演:十字形扩展","p0087.速度 方向");
		_hashmap.put("演:随机效果","p0088");
		_hashmap.put("演:圆形扩展","p0089.速度 方向");
		_hashmap.put("演:擦除","p0090.速度 方向");
		_hashmap.put("演:飞入","p0091.速度 方向");
		_hashmap.put("演:缓慢进入","p0092.速度 方向");
		_hashmap.put("演:菱形","p0093.速度 方向");
		_hashmap.put("演:劈裂","p0094.速度 方向");
		_hashmap.put("演:切入","p0095.速度 方向");
		_hashmap.put("演:扇形展开","p0096.速度");
		_hashmap.put("演:随机线条","p0097.速度 方向");
		_hashmap.put("演:向内溶解","p0098.速度");
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
		_hashmap.put("演:更改填充颜色","p0124.速度 颜色");
		_hashmap.put("演:更改线条颜色","p0121.速度 颜色");
		_hashmap.put("","");
		_hashmap.put("演:动作路径","p0133.路径");
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
		
		_hashmap.put("演:声音","p0061.预定义声音 自定义声音");
		//_hashmap.put("演:背景","p0056");
		_hashmap.put("演:幻灯片备注","p0054.备注母版引用");
		_hashmap.put("演:度量单位","p0055");
		_hashmap.put("演:背景","p0057");
		_hashmap.put("演:切换","p0058.效果 速度");
		_hashmap.put("演:方式","p0062");
		_hashmap.put("演:单击鼠标","p0065");
		_hashmap.put("演:时间间隔","p0066");
		_hashmap.put("演:幻灯片序列","p0022.标识符 名称 自定义");
		_hashmap.put("演:放映顺序","p0023.名称 序列引用");
		_hashmap.put("演:全屏放映","p0024");
		_hashmap.put("演:循环放映","p0025");
		_hashmap.put("演:放映间隔","p0026");
		_hashmap.put("演:手动方式","p0027");
		_hashmap.put("演:导航帮助","p0029");
		_hashmap.put("演:放映动画","p0030");
		_hashmap.put("演:前端显示","p0031");
		_hashmap.put("演:单击跳转","p0032");
		_hashmap.put("演:背景音乐","p0033");
		//_hashmap.put("演:背景","p0038");
		_hashmap.put("演:公用处理规则","p0000");
		_hashmap.put("演:页面设置集","p0001");
		_hashmap.put("演:页面设置","p0002.标识符 名称");
		_hashmap.put("演:配色方案集","p0007");
		_hashmap.put("演:配色方案","p0008.标识符 名称 类");
		_hashmap.put("演:页面版式集","p0017");
		_hashmap.put("演:页面版式","p0018.标识符 名称");
		_hashmap.put("演:布局","p0129.类型");
		_hashmap.put("演:占位符","p0130.类型");
		_hashmap.put("演:显示比例","p0020");
		_hashmap.put("演:放映设置","p0021");
		_hashmap.put("演:主体","p0034");
		_hashmap.put("演:背景色","p0009");
		_hashmap.put("演:文本和线条","p0010");
		_hashmap.put("演:阴影","p0011");
		_hashmap.put("演:标题文本","p0012");
		_hashmap.put("演:填充","p0013");
		//_hashmap.put("演:强调","p0014");
		_hashmap.put("演:强调和超级链接","p0015");
		_hashmap.put("演:强调和尾随超级链接","p0016");
		_hashmap.put("演:纸张","p0003.宽度 高度 纸型");
		_hashmap.put("演:页边距","p0004.左 上 右 下");
		_hashmap.put("演:页码格式","p0005");
		_hashmap.put("演:纸张方向","p0006");
		_hashmap.put("演:文本式样集","p0131");
		_hashmap.put("演:文本式样","p0132.标识符 名称");
		_hashmap.put("演:段落式样","p0135.标识符 名称 类型 别名 基式样引用");
		
		_hashmap.put("图:图形","g0000.层次 标识符 组合列表 其他对象");
		_hashmap.put("图:填充","g0012");
		_hashmap.put("图:线颜色","g0013");
		_hashmap.put("图:线型","g0014");
		_hashmap.put("图:线粗细","g0016");
		_hashmap.put("图:前端箭头","g0017");
//		_hashmap.put("图:式样","g0018");
//		_hashmap.put("图:大小","g0019");
		_hashmap.put("图:后端箭头","g0020");
//		_hashmap.put("图:式样","g0021");                                         
//		_hashmap.put("图:大小","g0022");
		_hashmap.put("图:宽度","g0023");
		_hashmap.put("图:高度","g0024");
		_hashmap.put("图:旋转角度","g0025");
		_hashmap.put("图:X－缩放比例","g0026");
		_hashmap.put("图:Y－缩放比例","g0027");
		_hashmap.put("图:锁定纵横比","g0028");
		_hashmap.put("图:相对原始比例","g0029");
		_hashmap.put("图:打印对象","g0032");
		_hashmap.put("图:Web文字","g0033");
		_hashmap.put("图:透明度","g0038");
		_hashmap.put("图:svg图形对象","g0005");
		_hashmap.put("图:预定义图形","g0005");
		_hashmap.put("图:类别","g0006");
		_hashmap.put("图:名称","g0007");
		_hashmap.put("图:生成软件","g0008");
		_hashmap.put("图:关键点坐标","g0009.路径");
		_hashmap.put("图:坐标","g0010.x坐标 y坐标");
		_hashmap.put("图:属性","g0011");
		_hashmap.put("图:文本内容","g0002.文本框 左边距 右边距 上边距 下边距 水平对齐 垂直对齐 文字排列方向 自动换行 大小适应文字 前一链接 后一链接");
		_hashmap.put("图:控制点","g0003");
		_hashmap.put("图:颜色","g0034");
		_hashmap.put("图:图片","g0035.位置 图形引用 类型 名称");
		_hashmap.put("图:图案","g0036.类型 图形引用 前景色 背景色");
		_hashmap.put("图:渐变","g0037.起始色 终止色 种子类型 起始浓度 终止浓度 渐变方向 边界 种子X位置 种子Y位置 类型");
		_hashmap.put("图:组合位置","g0041.x坐标 y坐标");
		_hashmap.put("图:翻转","g0040.方向");
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
		_exit_eff_map.put("演:百叶窗","p0100.速度 方向");
		_exit_eff_map.put("演:飞出","p0101.速度 方向");
		_exit_eff_map.put("演:缓慢移出","p0102.速度 方向");
		_exit_eff_map.put("演:菱形","p0103.速度 方向");
		_exit_eff_map.put("演:劈裂","p0104.速度 方向");
		_exit_eff_map.put("演:切出","p0105.速度 方向");
		_exit_eff_map.put("演:扇形展开","p0106.速度");
		_exit_eff_map.put("演:随机线条","p0107.速度 方向");
		_exit_eff_map.put("演:向外溶解","p0108.速度");
		_exit_eff_map.put("演:圆形扩展","p0109.速度 方向");
		_exit_eff_map.put("演:擦除","p0110.速度 方向");
		_exit_eff_map.put("演:盒状","p0111.速度 方向");
		_exit_eff_map.put("演:阶梯状","p0112.速度 方向");
		_exit_eff_map.put("演:轮子","p0113.速度 轮辐");
		_exit_eff_map.put("演:棋盘","p0114.速度 方向");
		_exit_eff_map.put("演:闪烁一次","p0115.速度");
		_exit_eff_map.put("演:十字形扩展","p0116.速度 方向");
		_exit_eff_map.put("演:随机效果","p0117");
		_exit_eff_map.put("演:消失","p0118");
		_exit_eff_map.put("演:其他","p0119");
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