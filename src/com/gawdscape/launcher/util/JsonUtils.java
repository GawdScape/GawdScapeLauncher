package com.gawdscape.launcher.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

/**
 *
 * @author Vinnie
 */
public class JsonUtils {

    private static final Gson gson;

    static {
	GsonBuilder builder = new GsonBuilder();
	builder.setPrettyPrinting();
	builder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
	builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
	builder.enableComplexMapKeySerialization();
	gson = builder.create();
    }

    public static Gson getGson() {
	return gson;
    }

    private static String readAll(Reader rd) throws IOException {
	StringBuilder sb = new StringBuilder();
	int cp;
	while ((cp = rd.read()) != -1) {
	    sb.append((char) cp);
	}
	return sb.toString();
    }

    public static String readJsonFromUrl(String url) throws IOException {
	InputStream is = new URL(url).openStream();
	try {
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    return readAll(rd);
	} finally {
	    is.close();
	}
    }

    public static String readJsonFromFile(File filePath) throws FileNotFoundException, IOException {
	FileReader rd = new FileReader(filePath);
	return readAll(rd);
    }

    public static void writeJsonToFile(String jsonText, File filePath) throws IOException {
	if (!filePath.exists()) {
	    filePath.getParentFile().mkdirs();
	}
	FileWriter file = new FileWriter(filePath);
	file.write(jsonText);
	file.flush();
	file.close();
    }
}
