package pt.ual.meit.Model;

import org.springframework.web.multipart.MultipartFile;

public class MappingConfigurationOntology {
	private String name;
	private MultipartFile file;
	private String url;
	private String language;

	public MappingConfigurationOntology(String name, MultipartFile file, String url, String language) {
		super();
		this.name = name;
		this.file = file;
		this.url = url;
		this.language = language;
	}

	public MappingConfigurationOntology() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
