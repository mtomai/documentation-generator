package eu.example.poc.documentation.generator.core;

import eu.example.poc.documentation.generator.consts.StyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class SectionService {


	public void makeSection(XWPFDocument document, StyleEnum styleEnum, int pt, String text) {
		XWPFParagraph paragraph = document.createParagraph();

		if(styleEnum != null) {
			addCustomHeadingStyle(document, styleEnum.getStyle());
			paragraph.setStyle(styleEnum.getStyle());
		}

		XWPFRun run = paragraph.createRun();
		run.setFontSize(pt);

		if(text.contains("\n"))  {
			List<String> splittedText = Arrays.asList(text.split("\n"));
			run.setText(splittedText.get(0), 0);
			IntStream.range(1, splittedText.size()).forEach(i -> {
				run.addBreak();
				run.setText(splittedText.get(i));
			});
		} else {
			run.setText(text);
		}


	}

	//https://stackoverflow.com/questions/2643822/how-can-i-use-predefined-formats-in-docx-with-poi/27864752#27864752
	private static void addCustomHeadingStyle(XWPFDocument document, String strStyleId) {
		XWPFStyles styles =  document.createStyles();

		CTStyle ctStyle = CTStyle.Factory.newInstance();
		ctStyle.setStyleId(strStyleId);

		CTString styleName = CTString.Factory.newInstance();
		styleName.setVal(strStyleId);
		ctStyle.setName(styleName);

		CTOnOff nonnull = CTOnOff.Factory.newInstance();
		ctStyle.setUnhideWhenUsed(nonnull);
		ctStyle.setQFormat(nonnull);

		XWPFStyle style = new XWPFStyle(ctStyle);
		style.setType(STStyleType.PARAGRAPH);
		styles.addStyle(style);

	}

}
