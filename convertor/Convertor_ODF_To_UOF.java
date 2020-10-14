package convertor;

import graphic_content.Graphic_Handler;
import graphic_content.Media_Obj;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;

import tables.*;
import text.*;
import spreadsheet.*;
import stored_data.*;
import styles.*;
import presentation.*;

/**
 * 主程序，负责初始化静态表，分配SAX解析器，并调用内容处理程序对文档进行解析。
 * 
 * @author xie
 *
 */
public class Convertor_ODF_To_UOF extends JFrame implements ActionListener{
	private static final long serialVersionUID = 200611111102L;
	
	//A file filter for ODF source files.
	static class ODFFileFilter extends FileFilter
	{
		public String getDescription(){
			return "ODF Source Files (.odt/.ods/.odp)";
		}
		
		public boolean accept(File f){
			boolean bool = false;
			
			if (f != null){
				String fn = f.getName();
				bool = fn.endsWith(".odt") || fn.endsWith("ods") || fn.endsWith("odp")|| f.isDirectory();
			}
			
			return bool;
		}
	}
	
	private static JTextField _src_path_field;
	private static JTextField _rst_path_field;
	private static JTextArea _source_area;
	private static JTextArea _result_area;
	private static JButton convertButton;
	private static JTextField _state_field;
	
	private JPanel create_src_panel(JComponent comp1, JComponent comp2){
		JPanel srcPl = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		srcPl.add(comp1, c);	
		c.gridwidth = 2;
		srcPl.add(comp2, c);
		
		return srcPl;
	}
	
	private void add_content(JComponent comp,Insets is, int gridx, int gridy, int gridwidth){
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = is;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		
		getContentPane().add(comp,c);
	}
	
	public Convertor_ODF_To_UOF(String title){
		super(title);
		getContentPane().setLayout(new GridBagLayout());
		
		JButton chooserButton = new JButton("Choose ODF File...");
		chooserButton.setBorder(BorderFactory.createEmptyBorder(3,4,3,4));
		chooserButton.setActionCommand("OPEN");
		chooserButton.addActionListener(this);
	    _src_path_field = new JTextField(System.getProperty("user.dir"));
	    _src_path_field.setEditable(false);
	    _src_path_field.setFont(new Font(" ", Font.PLAIN, 13));
	    _src_path_field.setColumns(37);   
	    add_content(create_src_panel(chooserButton, _src_path_field),new Insets(25,5,5,5), 0, 0, 2);

	    convertButton = new JButton("Convert");
	    convertButton.setBorder(BorderFactory.createEmptyBorder(3,4,3,4));
	    convertButton.setActionCommand("CONVERT");
	    convertButton.addActionListener(this);
	    JPanel butPn = new JPanel();
	    butPn.add(convertButton);
	    butPn.setBorder(BorderFactory.createEmptyBorder(0,0,0,37));
	    _rst_path_field = new JTextField(System.getProperty("user.dir") + "\\");
	    _rst_path_field.setEditable(false);
	    _rst_path_field.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
	    _rst_path_field.setBackground(Color.LIGHT_GRAY);
	    _rst_path_field.setColumns(32);
	    add_content(create_src_panel(butPn, _rst_path_field),new Insets(5,5,5,5), 0, 1, 2);
		
	    add_content(new JLabel("解析源文件: "),new Insets(5,5,0,35), 0, 2, 1);
		
	    add_content(new JLabel("结果输出:   "),new Insets(5,0,0,55), 1, 2, 1);
	    
	    _source_area = new JTextArea("",20,25);
	    _source_area.setEditable(false);
	    _source_area.setLineWrap(true);
	    _source_area.setForeground(Color.gray);
	    add_content(new JScrollPane(_source_area),new Insets(0,25,5,0), 0, 3, 1);
	    
	    _result_area = new JTextArea("",20,25);
	    _result_area.setEditable(false);
	    _result_area.setLineWrap(true);
	    _result_area.setForeground(Color.blue);
	    add_content(new JScrollPane(_result_area),new Insets(0,0,5,25), 1, 3, 1);
	    
	    _state_field = new JTextField(""); 
	    _state_field.setColumns(30);
	    _state_field.setBorder(BorderFactory.createEmptyBorder());
	    _state_field.setForeground(Color.RED);
	    _state_field.setBackground(Color.LIGHT_GRAY);
	    _state_field.setFont(new Font("",Font.BOLD,14));
	    add_content(_state_field, new Insets(5,5,10,5),0,4,2);
	}
	
	//do some initiations
	private static void global_init(){
		UOF_LocID_Table.create_map();
		Meta_Table.create_map();
		Extension_Ele_Set.createSet();
		Text_Field.create_set();
		Draw_Type_Table.create_map();
		Numformat_Table.create_map();
		Draw_Page_Style.init_effect_table();
		Anim_Par.init_effect_table();
	}
	
	private void loc_init(){
		Page_Layout.init();
		Text_P.init();
		Text_Table.init();
		Tracked_Change.init();
		Graphic_Style.init();
		Table_Column.init();
		Table_Row.init();
		Table_Style.init();
		
		Chart_Data.init();
		Content_Data.init();
		Spreadsheet_Data.init();
		Style_Data.init();
		
		Anno_In_Cell.init();
		Cell_Range.init();
		Table_Cell.init();
		Validation.init();
		
		Anim_Par.init();
		Draw_Padding.init();
		Draw_Page_Style.init();
		Draw_Page.init();
		
		Styles.init();
		Media_Obj.init();
		IDGenerator.restart();
		Graphic_Handler.init();
	}
	
	public void actionPerformed(ActionEvent event){
		if (event.getActionCommand().equals("CONVERT")){
			String srcFile = _src_path_field.getText().trim();
			
			if(!srcFile.endsWith(".odt") && !srcFile.endsWith(".ods") && !srcFile.endsWith(".odp")){
				_source_area.setText("错误! 源文件必须是odf类型，请重新输入！");
			}
			else {
				try{
					int ind = srcFile.lastIndexOf(".");

					String rstFile = srcFile.substring(0,ind) + "_result.uof";
					_rst_path_field.setText(rstFile);
					_source_area.setText("");
					_result_area.setText("");
					
					do_convert(srcFile, rstFile);
					loc_init();
					//convertButton.setEnabled(false);
				}catch (Exception e){
					System.err.println(e.getMessage());
				}
			}
		}
	    else if (event.getActionCommand().equals("OPEN")){
	        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
	        chooser.updateUI();
	        chooser.setAcceptAllFileFilterUsed(false);
	        chooser.setFileFilter(new ODFFileFilter());
	        
	        int option = chooser.showOpenDialog(this);        
	        if(option == JFileChooser.APPROVE_OPTION){
	        	File chFile = chooser.getSelectedFile();
	        	_src_path_field.setText(chFile.getAbsolutePath());
	        	_rst_path_field.setText(chFile.getParentFile().getAbsolutePath());
	        }
	    }
	}
	
	public static void write_source_ta(String str){
		_source_area.append(str);
	}
	
	public static void write_result_ta(String element){
		_result_area.append(element);
	}
	
	private void do_convert(String srcFileName, String rstFileName){
		String state = "";
		
		try {	
			String tmpPath = Unzip.get_temp_path();
			String tmpFileName = Results_Processor.initialize(rstFileName);
			
			//unzip the source file
			_state_field.setText("unzipping the source file...");
			Unzip.unzip(srcFileName);
			XMLReader xmlReader = null;
			
			//从mimetype文件获取文档类型.
			BufferedReader typeReader = new BufferedReader(new FileReader(tmpPath + "mimetype"));
			String fileType = "";
			
			while ((fileType = typeReader.readLine()) != null) {
				if (fileType.contains("text")){
					Common_Data.set_file_type("text");
				}
				else if (fileType.contains("spreadsheet")){
					Common_Data.set_file_type("spreadsheet");
				}
				else if (fileType.contains("presentation")){
					Common_Data.set_file_type("presentation");
				}
			}
			
			_state_field.setText("creating SAX parser for this conversion...");
			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			spfactory.setValidating(false);    //非验证解析器，用于格式良好的文档.
			SAXParser saxParser = spfactory.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			
			InputSource metaSource = new InputSource(tmpPath + "meta.xml");
			InputSource styleSource = new InputSource(tmpPath + "styles.xml");
			InputSource contentSource = new InputSource(tmpPath + "content.xml");
			
			//第一次扫描源文档，提取出需要存储的数据.
			//first parse of meta.xml
			_state_field.setText("doing the first parse of meta.xml...");
			DefaultHandler firstMetaHandler = new First_Meta_Handler();
			xmlReader.setContentHandler(firstMetaHandler);	
			xmlReader.setErrorHandler(firstMetaHandler);	
			xmlReader.parse(metaSource);
			
			//first parse of style.xml
			_state_field.setText("doing the first parse of style.xml...");
			DefaultHandler firstStyleHandler = new First_Style_Handler();
			xmlReader.setContentHandler(firstStyleHandler);	
			xmlReader.setErrorHandler(firstStyleHandler);	
			xmlReader.parse(styleSource);
			
			//first parse of content.xml
			_state_field.setText("doing the first parse of content.xml...");
			DefaultHandler firstContentHandler = new First_Content_Handler();
			xmlReader.setContentHandler(firstContentHandler);	
			xmlReader.setErrorHandler(firstContentHandler);	
			xmlReader.parse(contentSource);
			
			//第二次扫描源文档，把扩展区之外的内容写入结果文档result.xml.
			IDGenerator.restart();
			Text_P.set_parsenum(false);
			//second parse of meta.xml
			DefaultHandler secondMetaHandler = new Second_Meta_Handler();
			xmlReader.setContentHandler(secondMetaHandler);
			xmlReader.setErrorHandler(secondMetaHandler);
			xmlReader.parse(metaSource);
			
			//second parse of style.xml
			DefaultHandler secondStyleHandler = new Second_Style_Handler();
			xmlReader.setContentHandler(secondStyleHandler);
			xmlReader.setErrorHandler(secondStyleHandler);
			xmlReader.parse(styleSource);
			
			//second parse of content.xml
			DefaultHandler secondContentHandler = new Second_Content_Handler();
			xmlReader.setContentHandler(secondContentHandler);
			xmlReader.setErrorHandler(secondContentHandler);
			xmlReader.parse(contentSource);		
			
			//扫描结果文档，向每个元素追加uof:locID和uof:AttList属性.
			InputSource result = new InputSource(tmpFileName);
			DefaultHandler thirdHandler = new Third_Handler();	
			xmlReader.setContentHandler(thirdHandler);	
			xmlReader.setErrorHandler(thirdHandler);	
			xmlReader.parse(result);
			
			Results_Processor.close();
			state = "                Convert successfully!!!";
		} catch (Exception e) {	
			System.err.println(e);
			e.printStackTrace();
			
			if(state.equals("")){
				state = "Something wrong happened in this conversion!!!";
			}
		}
		
		_state_field.setText(state);
	}
	
	public static void main(String args[]){
		global_init();
		
		Convertor_ODF_To_UOF convApp = new Convertor_ODF_To_UOF("ODF-UOF Converter");
		
		convApp.pack();
		convApp.setLocation(450,230);
		convApp.setVisible(true);
		convApp.setResizable(false);
		convApp.setDefaultCloseOperation(EXIT_ON_CLOSE);
		System.setErr(new PrintStream(new JTextAreaStream(_result_area)));
	}
}
