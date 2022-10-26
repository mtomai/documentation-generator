package eu.example.poc.documentation.generator.core;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
	* The type Table component.
	*/
@Service
public class TableComponent {

	/**
		*
		* @param document the document where to add table
		* @param tableData a matrix rows x column
		*/
	public void makeTable(XWPFDocument document, List<List<String>> tableData) {

		int columnSize = tableData.get(0).size();
		int rowSize = tableData.size();

		//create table
		XWPFTable table = document.createTable(rowSize, columnSize);

		for(int k = 0; k < rowSize; k++) {
			for (int i = 0; i < columnSize; i++) {
				String value = tableData.get(k).get(i);
				if(k == 0) {
					//create table headers
					makeHeaderTable(table, i, value);
				} else {
					table.getRow(k).getCell(i).setText(value);
				}
			}
		}

	}

	private static void makeHeaderTable(XWPFTable table, int cell, String value) {
		// write to first row, first column
		XWPFParagraph p1 = table.getRow(0).getCell(cell).getParagraphs().get(0);
		p1.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun r1 = p1.createRun();
		r1.setBold(true);
		r1.setText(value);
	}
}
