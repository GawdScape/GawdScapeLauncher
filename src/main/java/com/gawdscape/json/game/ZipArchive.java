package com.gawdscape.json.game;

import com.gawdscape.launcher.util.Constants;

/**
 *
 * @author Vinnie
 */
public class ZipArchive {

    private String name;
    private ExtractRules extract;
    private String url;

    public ZipArchive() {
    }

    public ZipArchive(String name) {
	if ((name == null) || (name.length() == 0)) {
	    throw new IllegalArgumentException("Archive name cannot be null or empty");
	}
	this.name = name;
    }

    public ZipArchive(ZipArchive zip) {
	name = zip.name;
	url = zip.url;
	if (zip.extract != null) {
	    extract = new ExtractRules(zip.extract);
	}
    }

    public String getName() {
	return name;
    }

    public ExtractRules getExtractRules() {
	return extract;
    }

    public ZipArchive setExtractRules(ExtractRules rules) {
	extract = rules;
	return this;
    }

    public String getArtifactBaseDir() {
	if (name == null) {
	    throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
	}
	String[] parts = name.split(":", 3);
	return String.format("%s/%s/%s", parts[0].replaceAll("\\.", "/"), parts[1], parts[2]);
    }

    public String getArtifactPath() {
	return getArtifactPath(null);
    }

    public String getArtifactPath(String classifier) {
	if (name == null) {
	    throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
	}
	return String.format("%s/%s", getArtifactBaseDir(), getArtifactFilename(classifier));
    }

    public String getArtifactFilename(String classifier) {
	if (name == null) {
	    throw new IllegalStateException("Cannot get artifact filename of empty/blank artifact");
	}
	if (classifier != null) {
	    classifier = "-" + classifier;
	} else {
	    classifier = "";
	}
	String extension = "zip";
	String[] parts = name.split(":", 3);
	return String.format("%s-%s%s.%s", parts[1], parts[2], classifier, extension);
    }

    @Override
    public String toString() {
	return "Archive{name='" + name + "', extract=" + extract + "}";
    }

    public boolean hasCustomUrl() {
	return url != null;
    }

    public String getDownloadUrl() {
	if (url != null) {
	    return url;
	}
	return Constants.GS_STORAGE_URL + "Archives/";
    }
}
