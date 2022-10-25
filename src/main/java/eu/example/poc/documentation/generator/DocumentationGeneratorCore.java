package eu.example.poc.documentation.generator;

import eu.example.poc.documentation.generator.consts.StyleEnum;
import eu.example.poc.documentation.generator.section.SectionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@Slf4j
public class DocumentationGeneratorCore {

	@Autowired
	private SectionService sectionService;

	@SneakyThrows
	public void generateDocumentation() {
		XWPFDocument document = new XWPFDocument();

		JSONTokener tokener;
		try (InputStream is = DocumentationGeneratorCore.class.getResourceAsStream("/toConvert.json")) {
			tokener = new JSONTokener(Objects.requireNonNull(is));

			JSONObject root = new JSONObject(tokener);
			JSONObject info = (JSONObject) root.get("info");
			String title = info.getString("title");

			sectionService.madeSection(document, StyleEnum.TITLE, 28, title);
		}


		sectionService.madeSection(document, null, 12, "Body \n acapo");


		//TODO: TAKE THIS FROM PROP
		creatDocxFile(document, "example.docx");
	}

	private static void creatDocxFile(XWPFDocument document, String name) throws IOException {
		FileOutputStream out = new FileOutputStream(name);
		document.write(out);
		out.close();
	}
}
