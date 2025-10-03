package org.owleebr.professions.FoxCore.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonMapBuilder {
    File jfile;
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Map<String, Object>> data;
    private Map<String, Object> newdata;

    public JsonMapBuilder(File jfie) throws IOException {
        this.jfile = jfie;
        if (jfie.exists() && jfile.length() > 0) {
            data = mapper.readValue(jfile, new TypeReference<>() {});
        } else {
            data = new HashMap<>();
        }
    }

    public JsonMapBuilder add(String key, Object value) {
        newdata.put(key, value);
        return this;
    }

    public JsonMapBuilder edit(String name,String key , Object value) {
        if (data.containsKey(name)) {
            Map<String, Object> chair = data.get(name);
            chair.put(key, value); // новое значение
        }
        return this;
    }

    public void write(String name) throws IOException {
        data.put(name, newdata);
        mapper.writerWithDefaultPrettyPrinter().writeValue(jfile, data);
    }

    public void remove(String name) throws IOException {
        data.remove(name);
        mapper.writerWithDefaultPrettyPrinter().writeValue(jfile, data);
    }

    public void update() throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(jfile, data);
    }


}
