package eu.example.poc.documentation.generator.core.parser;

import eu.example.poc.documentation.generator.consts.StyleEnum;
import eu.example.poc.documentation.generator.core.SectionService;
import eu.example.poc.documentation.generator.core.TableComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
public class DocumentParser {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private TableComponent tableComponent;


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

		createServerSection(document, root);

	}


	private void createServerSection(XWPFDocument document, JSONObject info) {

		sectionService.makeSection(document, StyleEnum.TITLE, 16, "Server Name");

		List<List<String>> tableServer = new ArrayList<>();
		List<String> tableHeaders = Arrays.asList("Host", "Description");
		tableServer.add(tableHeaders);


		if(info.has("servers")) {

			JSONArray servers = info.getJSONArray("servers");

			IntStream.range(0, servers.length()).mapToObj(i -> (JSONObject) servers.get(i)).forEach(server -> {
				List<String> content = new ArrayList<>();
				content.add(server.getString("url"));
				if (server.has("description")) {
					content.add(server.getString("description"));
				} else {
					content.add("N/A");
				}
				tableServer.add(content);
			});

		}
		tableComponent.makeTable(document, tableServer);

	}

}
