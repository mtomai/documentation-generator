package eu.example.poc.documentation.generator.consts;

public enum StyleEnum {
	TITLE("Custom Title");


	private final String style;

	private StyleEnum(String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}
}
