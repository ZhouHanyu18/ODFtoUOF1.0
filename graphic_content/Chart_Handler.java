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
	private String _title_begin = "";   		//������ʼԪ��
	private String _title_content = "";   			//��������
	private boolean _title_mark = false;   			//����<text:p>���ı��ڵ���жϣ������Ƿ�������������������
	private ChartStyle _plot_area_style = null;   	//���plot-area����Ԫ��δ��������style�����ʹ�����style
	private String _axis_type = "";   				//���������ߺͱ�����ж�
	private boolean _in_plot_area = false;   		//���ڱ�ʶ����plot-area����Ԫ����û��ʽ��������ʹ��plotAreaStyle
	
	private int _data_serie_num = 0;   				//ϵ�кţ���Ҫ����
	private int _data_point_num = 0;   				//��ţ���Ҫ����
	private String _data_source = "";
	
	//Ŀǰֻ��������һ��������������
	private boolean _data_src_tag = false;   //��ʶdata_source�ĵ�һ�������Ƿ��Ѵ������Ľ�
	
	private boolean _local_table_tag = false;
	
	public Chart_Handler() { 
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException 
	{
		String value = "";
		//��ֵʽ��
		if (qName.equals("number:number-style") && (value = atts.getValue("style:name")) != null) {
			Chart.add_data_style(value,"number");
		}
		/*To do:�����������ֵ����
		else if (qName.equals("number:-style") && atts.getValue("style:name") != null) {
			
		}
		*/
		
		//style:family�Ƿ�϶���chart?
		else if (qName.equals("style:style") && atts.getValue("style:family").equals("chart")) {
			_current_chart_style_id = atts.getValue("style:name");
			ChartStyle chartstyle = new ChartStyle();
			
			//һЩ��ֵ��Ϣ.<��:��ֵ>�ĸ�ʽ������̫���ӣ���ʱδ����
			if ((value = atts.getValue("style:data-style-name")) != null) {
				chartstyle.add_axis_datastyle(" ��:��������=\"" + Chart.get_data_style(value) + "\"");
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
			//��������ʱ������һ��
			Chart.set_type(atts.getValue("chart:class"));
			
			Chart.add_begin_element( " ��:���=\"" + Unit_Converter.convert_gra(atts.getValue("svg:width")) 
					+ "\" ��:�߶�=\"" + Unit_Converter.convert_gra(atts.getValue("svg:height")) + "\"");
			if (atts.getValue("chart:style-name") != null)
				Chart.add_chart_area(Chart_Data.get_chart_style(atts.getValue("chart:style-name")).get_chart_area());
			//To do��
		}
		
		//���������������Ҫ����Ԫ��<text:p>���ı��ڵ�����ȡ
		else if (qName.equals("chart:title")) {
			_title_mark = true;
			
			if(_axis_type.equals("x"))
				_title_begin += " ��:λ��=\"category axis\"";
			else if(_axis_type.equals("y"))
				_title_begin += " ��:λ��=\"value axis\"";
			else
				_title_begin += " ��:λ��=\"chart\"";
			
			if ((value = atts.getValue("chart:style-name")) != null)
				_title_content = Chart_Data.get_chart_style(value).get_title();
			//���û��ʽ�����ã�������plot-area�У�������plotAreaStyle
			else if (_in_plot_area)
				_title_content = _plot_area_style.get_title();
			if(_axis_type.equals("y")) {
				int index = _title_content.indexOf("</��:����>");
				_title_content = _title_content.substring(0,index)
				+ "<��:������ת�Ƕ�>90</��:������ת�Ƕ�>" + "</��:����>";  //���Ľ�
			}
		}
/*		else if (qName.equals("chart:subtitle") || qName.equals("chart:footer")) {
			_title_mark = true;
			if ((value =atts.getValue("chart:style-name")) != null)
				_title_content = Chart_Data.get_chart_style(value).get_title();
		}
*/			
		else if (qName.equals("chart:legend")) {
			String legend = "<��:ͼ��";
			if ((value = atts.getValue("chart:legend-position")) != null) {
				legend += " ��:λ��=\"";
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
			
			//UOF��ͼ�������嶨�������ʣ����һ������
			if ((value = atts.getValue("chart:style-name")) != null)
				legend += Chart_Data.get_chart_style(value).get_legend();
			
			legend += "</��:ͼ��>";
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
				Chart.add_data_source(" ��:��������=\"" + _data_source + "\"");
			}

			Chart.add_data_source(">");
			//To do
		}
		
		else if (qName.equals("chart:axis") && (value = atts.getValue("chart:dimension")) != null) {
			if (value.equals("x")) {
				_axis_type = "x";
				if (atts.getValue("chart:style-name") != null) {
					String xAxis = "<��:������";
					ChartStyle chartstyle = Chart_Data.get_chart_style(atts.getValue("chart:style-name"));
					if (chartstyle.get_major_tick_type().length() != 0)
						xAxis += " ��:���̶�����=\"" + chartstyle.get_major_tick_type() + "\"";
					if (chartstyle.get_minor_tick_type().length() != 0)
						xAxis += " ��:�ο̶�����=\"" + chartstyle.get_minor_tick_type() + "\"";
					xAxis += " ��:�̶��߱�־=\"next to axis\">" + chartstyle.get_x_axis() + "</��:������>";
					Chart.add_x_axis(xAxis);
				}
			}
			else if (value.equals("y")) {
				_axis_type = "y";
				if (atts.getValue("chart:style-name") != null) {
					String yAxis = "<��:��ֵ��";
					ChartStyle chartstyle = Chart_Data.get_chart_style(atts.getValue("chart:style-name"));
					if (chartstyle.get_major_tick_type().length() != 0)
						yAxis += " ��:���̶�����=\"" + chartstyle.get_major_tick_type() + "\"";
					if (chartstyle.get_minor_tick_type().length() != 0)
						yAxis += " ��:�ο̶�����=\"" + chartstyle.get_minor_tick_type() + "\"";
					yAxis += " ��:�̶��߱�־=\"next to axis\">" + chartstyle.get_y_axis() + "</��:��ֵ��>";
					Chart.add_x_axis(yAxis);
				}
			}
		}
		
		else if (qName.equals("chart:grid")) {
			String gridLine = "";
			if ((value = atts.getValue("chart:style-name")) != null)
				gridLine = Chart_Data.get_chart_style(value).get_gridline();
			//û��ʽ��������ʹ��plotAreaStyle��
			else
				gridLine = _plot_area_style.get_gridline();
			
			if (_axis_type.equals("x") && atts.getValue("chart:class").equals("major")) //UOFĿǰֻ֧����������
				gridLine += " ��:λ��=\"category axis\"";
			else if (_axis_type.equals("y") && atts.getValue("chart:class").equals("major")) 
				gridLine += " ��:λ��=\"value axis\"";
			gridLine += "/>";
			Chart.add_gridline(gridLine);
		}
		
		else if (qName.equals("chart:series")) {
			_data_serie_num++;
			String serie = "<��:����ϵ�� ��:ϵ��=\"" + _data_serie_num + "\">";
			if ((value = atts.getValue("chart:style-name")) != null) 
				serie += Chart_Data.get_chart_style(value).get_data_point();
			else
				serie += _plot_area_style.get_data_point();
			serie += "</��:����ϵ��>";
			Chart.add_data_serie(serie);
			
			//To do.<����Դ>�м���<ϵ��>��Ԫ�أ�����δ�ҵ���Ӧ����
		}
		
		//======����:chart:series���õ�ʽ���Ƿ񸲸�chart:plot-area��ʽ��??======
		else if (qName.equals("chart:data-point")) {
/*			_data_point_num++;
			String point = "<��:���ݵ� ��:ϵ��=\"" + _data_serie_num + "\" ��:��=\"" + _data_point_num + "\">";
			if ((value = atts.getValue("chart:style-name")) != null) 
				point += Chart_Data.get_chart_style(value).get_data_point();
			else
				point += _plot_area_style.get_data_point();
			point += "</��:���ݵ�>";
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
			Chart.add_title("<��:����" + _title_begin + ">" + _title_content + "</��:����>");
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
			_title_begin += (" ��:����=\"" + new String(ch, start, length) + "\"");
		
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
//		������1.$A$1:.$B$3 --> '������1'!$A$1:$B$3
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