package graphic_content;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import stored_data.*;
import convertor.Unit_Converter;
import spreadsheet.Anchor_Pos;

public class Chart_Handler extends DefaultHandler {
	
	private String _current_chart_style_id = "";
	private String _title_begin = "";   		//标题起始元素
	private String _title_content = "";   			//标题内容
	private boolean _title_mark = false;   			//用于<text:p>中文本节点的判断，决定是否向标题中添加名称属性
	private ChartStyle _plot_area_style = null;   	//如果plot-area的子元素未单独引用style，则会使用这个style
	private String _axis_type = "";   				//用于网格线和标题的判断
	private boolean _in_plot_area = false;   		//用于标识进入plot-area，子元素若没有式样引用则使用plotAreaStyle
	
	private int _data_serie_num = 0;   				//系列号，需要生成
	private int _data_point_num = 0;   				//点号，需要生成
	private String _data_source = "";
	
	//目前只处理来自一个矩形区域的情况
	private boolean _data_src_tag = false;   //标识data_source的第一个部分是否已处理，待改进
	
	private boolean _local_table_tag = false;
	
	public Chart_Handler() { 
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException 
	{
		String value = "";
		//数值式样
		if (qName.equals("number:number-style") && (value = atts.getValue("style:name")) != null) {
			Chart.add_data_style(value,"number");
		}
		/*To do:添加其他的数值分类
		else if (qName.equals("number:-style") && atts.getValue("style:name") != null) {
			
		}
		*/
		
		//style:family是否肯定是chart?
		else if (qName.equals("style:style") && atts.getValue("style:family").equals("chart")) {
			_current_chart_style_id = atts.getValue("style:name");
			ChartStyle chartstyle = new ChartStyle();
			
			//一些数值信息.<表:数值>的格式码属性太复杂，暂时未处理。
			if ((value = atts.getValue("style:data-style-name")) != null) {
				chartstyle.add_axis_datastyle(" 表:分类名称=\"" + Chart.get_data_style(value) + "\"");
			}
			
			Chart_Data.add_chart_style(_current_chart_style_id,chartstyle);	
		}
		else if (qName.equals("style:chart-properties") && _current_chart_style_id.length() != 0) {
			Chart_Data.get_chart_style(_current_chart_style_id).process_chart_pro(atts);
		}
		else if (qName.equals("style:graphic-properties") && _current_chart_style_id.length() != 0) {
			Chart_Data.get_chart_style(_current_chart_style_id).process_graphic_pro(atts);
		}	
		else if (qName.equals("style:paragraph-properties") && _current_chart_style_id.length() != 0) {
			Chart_Data.get_chart_style(_current_chart_style_id).process_para_pro(atts);
		}
		else if (qName.equals("style:text-properties") && _current_chart_style_id.length() != 0) {
			Chart_Data.get_chart_style(_current_chart_style_id).process_text_pro(atts);
		}
		
		else if (qName.equals("chart:chart")) {
			//子类型暂时和类型一样
			Chart.set_type(atts.getValue("chart:class"));
			
			Chart.add_begin_element( " 表:宽度=\"" + Unit_Converter.convert_gra(atts.getValue("svg:width")) 
					+ "\" 表:高度=\"" + Unit_Converter.convert_gra(atts.getValue("svg:height")) + "\"");
			if (atts.getValue("chart:style-name") != null)
				Chart.add_chart_area(Chart_Data.get_chart_style(atts.getValue("chart:style-name")).get_chart_area());
			//To do？
		}
		
		//标题的名称属性需要到子元素<text:p>的文本节点中提取
		else if (qName.equals("chart:title")) {
			_title_mark = true;
			
			if(_axis_type.equals("x"))
				_title_begin += " 表:位置=\"category axis\"";
			else if(_axis_type.equals("y"))
				_title_begin += " 表:位置=\"value axis\"";
			else
				_title_begin += " 表:位置=\"chart\"";
			
			if ((value = atts.getValue("chart:style-name")) != null)
				_title_content = Chart_Data.get_chart_style(value).get_title();
			//如果没有式样引用，而又在plot-area中，则引用plotAreaStyle
			else if (_in_plot_area)
				_title_content = _plot_area_style.get_title();
			if(_axis_type.equals("y")) {
				int index = _title_content.indexOf("</表:对齐>");
				_title_content = _title_content.substring(0,index)
				+ "<表:文字旋转角度>90</表:文字旋转角度>" + "</表:对齐>";  //待改进
			}
		}
/*		else if (qName.equals("chart:subtitle") || qName.equals("chart:footer")) {
			_title_mark = true;
			if ((value =atts.getValue("chart:style-name")) != null)
				_title_content = Chart_Data.get_chart_style(value).get_title();
		}
*/			
		else if (qName.equals("chart:legend")) {
			String legend = "<表:图例";
			if ((value = atts.getValue("chart:legend-position")) != null) {
				legend += " 表:位置=\"";
				if (value.equals("start"))
					legend += "left";
				else if (value.equals("end"))
					legend += "right";
				else if (value.equals("top"))
					legend += "top";
				else if (value.equals("bottom"))
					legend += "bottom";
				else if (value.equals("top-start")||value.equals("top-end")||value.equals("bottom-start")||value.equals("bottom-end"))
					legend += "corner";
				legend += "\"";
			}
			legend += ">";
			
			//UOF中图例的字体定义有疑问，须进一步考虑
			if ((value = atts.getValue("chart:style-name")) != null)
				legend += Chart_Data.get_chart_style(value).get_legend();
			
			legend += "</表:图例>";
			Chart.add_legend(legend);
		}
		
		else if (qName.equals("chart:plot-area")) {
			if ((value = atts.getValue("chart:style-name")) != null) {
				_in_plot_area = true;
				Chart.add_plot_area(Chart_Data.get_chart_style(value).get_plot_area());
				_plot_area_style = Chart_Data.get_chart_style(value);
			}
			
			if ((value = atts.getValue("table:cell-range-address")) != null) {
				process_data_sources(value);
				Chart.add_data_source(" 表:数据区域=\"" + _data_source + "\"");
			}

			Chart.add_data_source(">");
			//To do
		}
		
		else if (qName.equals("chart:axis") && (value = atts.getValue("chart:dimension")) != null) {
			if (value.equals("x")) {
				_axis_type = "x";
				if (atts.getValue("chart:style-name") != null) {
					String xAxis = "<表:分类轴";
					ChartStyle chartstyle = Chart_Data.get_chart_style(atts.getValue("chart:style-name"));
					if (chartstyle.get_major_tick_type().length() != 0)
						xAxis += " 表:主刻度类型=\"" + chartstyle.get_major_tick_type() + "\"";
					if (chartstyle.get_minor_tick_type().length() != 0)
						xAxis += " 表:次刻度类型=\"" + chartstyle.get_minor_tick_type() + "\"";
					xAxis += " 表:刻度线标志=\"next to axis\">" + chartstyle.get_x_axis() + "</表:分类轴>";
					Chart.add_x_axis(xAxis);
				}
			}
			else if (value.equals("y")) {
				_axis_type = "y";
				if (atts.getValue("chart:style-name") != null) {
					String yAxis = "<表:数值轴";
					ChartStyle chartstyle = Chart_Data.get_chart_style(atts.getValue("chart:style-name"));
					if (chartstyle.get_major_tick_type().length() != 0)
						yAxis += " 表:主刻度类型=\"" + chartstyle.get_major_tick_type() + "\"";
					if (chartstyle.get_minor_tick_type().length() != 0)
						yAxis += " 表:次刻度类型=\"" + chartstyle.get_minor_tick_type() + "\"";
					yAxis += " 表:刻度线标志=\"next to axis\">" + chartstyle.get_y_axis() + "</表:数值轴>";
					Chart.add_x_axis(yAxis);
				}
			}
		}
		
		else if (qName.equals("chart:grid")) {
			String gridLine = "";
			if ((value = atts.getValue("chart:style-name")) != null)
				gridLine = Chart_Data.get_chart_style(value).get_gridline();
			//没有式样引用则使用plotAreaStyle。
			else
				gridLine = _plot_area_style.get_gridline();
			
			if (_axis_type.equals("x") && atts.getValue("chart:class").equals("major")) //UOF目前只支持主网格线
				gridLine += " 表:位置=\"category axis\"";
			else if (_axis_type.equals("y") && atts.getValue("chart:class").equals("major")) 
				gridLine += " 表:位置=\"value axis\"";
			gridLine += "/>";
			Chart.add_gridline(gridLine);
		}
		
		else if (qName.equals("chart:series")) {
			_data_serie_num++;
			String serie = "<表:数据系列 表:系列=\"" + _data_serie_num + "\">";
			if ((value = atts.getValue("chart:style-name")) != null) 
				serie += Chart_Data.get_chart_style(value).get_data_point();
			else
				serie += _plot_area_style.get_data_point();
			serie += "</表:数据系列>";
			Chart.add_data_serie(serie);
			
			//To do.<数据源>中加入<系列>子元素，但是未找到对应属性
		}
		
		//======疑问:chart:series引用的式样是否覆盖chart:plot-area的式样??======
		else if (qName.equals("chart:data-point")) {
/*			_data_point_num++;
			String point = "<表:数据点 表:系列=\"" + _data_serie_num + "\" 表:点=\"" + _data_point_num + "\">";
			if ((value = atts.getValue("chart:style-name")) != null) 
				point += Chart_Data.get_chart_style(value).get_data_point();
			else
				point += _plot_area_style.get_data_point();
			point += "</表:数据点>";
			Chart.add_data_point(point);*/
		}
		
		else if (qName.equals("table:table")) {
			_local_table_tag = true;
		}
		
		if (_local_table_tag) {
			Chart_Local_Table.process_start(qName,atts);
		}
			
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException 
	{
		if (_local_table_tag) {
			Chart_Local_Table.process_end(qName);
		}
		
		if (qName.equals("style:style"))
			_current_chart_style_id = "";
		else if (qName.equals("text:p")) {
			if (_title_mark)
				_title_mark = false;
		}
		else if (qName.equals("chart:title")) {
			Chart.add_title("<表:标题" + _title_begin + ">" + _title_content + "</表:标题>");
			_title_begin = "";
			_title_content = "";
		}
		else if (qName.equals("chart:plot-area"))
			_in_plot_area = false;
		else if (qName.equals("chart:axis"))
			_axis_type = "";
		
		else if (qName.equals("table:table")) {
			_local_table_tag = false;
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  
	{
		if (_title_mark)
			_title_begin += (" 表:名称=\"" + new String(ch, start, length) + "\"");
		
		if (_local_table_tag) {
			Chart_Local_Table.process_chars(ch,start,length);
		}
	}
	
	public void error(SAXParseException exception) 
	{
		System.err.println("Error parsing the file: "+exception.getMessage());
	}
	
	public void warning(SAXParseException exception) 
	{
		System.err.println("Warning parsing the file: "+exception.getMessage());
	}
	
	public void fatalError(SAXParseException exception) 
	{
		System.err.println("Fatal error parsing the file: "+exception.getMessage());
		System.err.println("Cannot continue.");
	}
	
	private void process_data_sources(String dataSource) {
		String tempDataSrc = dataSource;
		String dataSrcPart = "";
		int index = tempDataSrc.indexOf(" ");
		while (index > 0) {
			dataSrcPart = tempDataSrc.substring(0,index);
			_data_source += process_dataSrcPart(dataSrcPart) + ",";
			tempDataSrc = tempDataSrc.substring(index + 1);
			index = tempDataSrc.indexOf(" ");
		}
		dataSrcPart = tempDataSrc;
		_data_source += process_dataSrcPart(dataSrcPart);
	}
	
	private String process_dataSrcPart(String dataSrcPart) {
//		工作表1.$A$1:.$B$3 --> '工作表1'!$A$1:$B$3
		int index1 = dataSrcPart.indexOf(".");
		String sheetName = dataSrcPart.substring(0,index1);
		int index2 = dataSrcPart.indexOf(".",index1 + 1);
		String result = "'" + dataSrcPart.substring(0, index1) + "'!"
		+ dataSrcPart.substring(index1 + 1, index2) + dataSrcPart.substring(index2 + 1);
		
		if (!_data_src_tag) {
			_data_src_tag = true;
			index1 = dataSrcPart.indexOf("$");
			index2 = dataSrcPart.indexOf("$",index1 + 1);
			int beginCol = Anchor_Pos.colName_to_int(dataSrcPart.substring(index1 + 1, index2));
			int beginRow = Integer.valueOf(dataSrcPart.substring(index2 + 1, dataSrcPart.indexOf(":")));
			index1 = dataSrcPart.indexOf("$",index2 + 1);
			index2 = dataSrcPart.indexOf("$",index1 + 1);
			int endCol = Anchor_Pos.colName_to_int(dataSrcPart.substring(index1 + 1, index2));
			int endRow = Integer.valueOf(dataSrcPart.substring(index2 + 1));
			
			Chart_Local_Table.set_area(sheetName,beginCol,endCol,beginRow,endRow);
		}
		
		return result;
	}
}