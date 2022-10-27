package eu.example.poc.documentation.generator.core.parser;

import eu.example.poc.documentation.generator.consts.StyleEnum;
import eu.example.poc.documentation.generator.core.SectionService;
import eu.example.poc.documentation.generator.core.TableComponent;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ServerParser {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private TableComponent tableComponent;

	public void createServerSection(XWPFDocument document, JSONObject info) {

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
