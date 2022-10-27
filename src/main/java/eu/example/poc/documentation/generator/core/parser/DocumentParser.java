package eu.example.poc.documentation.generator.core.parser;

import eu.example.poc.documentation.generator.consts.StyleEnum;
import eu.example.poc.documentation.generator.core.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Locale;

@Service
@Slf4j
public class DocumentParser {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private ServerParser serverParser;


	public void createIntroduction(XWPFDocument document, JSONObject root) {
		JSONObject info = (JSONObject) root.get("info");

		//Title
		String title = info.getString("title");
		sectionService.makeSection(document, StyleEnum.TITLE, 28, title);

		if (info.has("description")) {
			//description
			String description = info.getString("description");
			sectionService.makeSection(document, StyleEnum.TEXT, 12, description);
		}
	}

	public void createApiDefinition(XWPFDocument document, JSONObject root) {

		sectionService.makeSection(document, StyleEnum.TITLE, 20, "API Definition");

		serverParser.createServerSection(document, root);

		createEndpoint(document, root);

	}

	public void createEndpoint(XWPFDocument document, JSONObject root) {
		sectionService.makeSection(document, StyleEnum.TITLE, 18, "Endpoint");

		JSONObject paths = (JSONObject) root.get("paths");
		paths.keys().forEachRemaining(path -> {
			JSONObject pathObject = (JSONObject) paths.get(path);
			pathObject.keys().forEachRemaining(method -> {

				sectionService.makeSection(document, StyleEnum.TEXT, 16, method.toUpperCase(Locale.ROOT)+ " "+ path);
				JSONObject definition = (JSONObject) pathObject.get(method);

				if(definition.has("summary")) {
					sectionService.makeSection(document, StyleEnum.TEXT, 12, definition.getString("summary"));
				}
				if(definition.has("description")) {
					sectionService.makeSection(document, StyleEnum.TEXT, 12, definition.getString("description"));
				}
				if(definition.has("requestBody")) {
					JSONObject requestBody = (JSONObject) definition.get("requestBody");
					JSONObject content = (JSONObject) requestBody.get("content");
					String contentType = content.keys().next();
					JSONObject schema = ((JSONObject) content.get(contentType)).getJSONObject("schema");

					if(schema.has("type")) {
						String type = schema.getString("type");

						if(schema.has("items")) {
							JSONObject items = (JSONObject) schema.get("items");

							String[] classesPath = items.getString("$ref").split("/");
							String className = classesPath[classesPath.length - 1];

							sectionService.makeSection(document, StyleEnum.TEXT, 12, "Request Body: "+ type + "of" + className);
						} else if (schema.has("additionalProperties")) {

						}

					} else {
						String[] classesPath = schema.getString("$ref").split("/");
						String className = classesPath[classesPath.length - 1];


						sectionService.makeSection(document, StyleEnum.TEXT, 12, "Request Body: "+ className);
					}


				}

			});



		});


	}


}
