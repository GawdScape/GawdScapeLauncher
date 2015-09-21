package com.gawdscape.launcher.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LowerCaseEnumTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
	Class rawType = type.getRawType();
	if (!rawType.isEnum()) {
	    return null;
	}

	final Map<String, T> lowercaseToConstant = new HashMap<>();
	for (Object constant : rawType.getEnumConstants()) {
	    lowercaseToConstant.put(toLowercase(constant), (T) constant);
	}

	return new TypeAdapter<T>() {
	    @Override
	    public void write(JsonWriter out, T value) throws IOException {
		if (value == null) {
		    out.nullValue();
		} else {
		    out.value(LowerCaseEnumTypeAdapterFactory.this.toLowercase(value));
		}
	    }

	    @Override
	    public T read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
		    reader.nextNull();
		    return null;
		}
		return lowercaseToConstant.get(reader.nextString());
	    }
	};
    }

    private String toLowercase(Object o) {
	return o.toString().toLowerCase(Locale.US);
    }
}
