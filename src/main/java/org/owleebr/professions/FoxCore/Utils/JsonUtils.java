package org.owleebr.professions.FoxCore.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class JsonUtils {
    public static <T> T readJsonOrCreate(
            File file,
            TypeReference<T> typeRef,
            ObjectMapper mapper,
            Supplier<T> defaultSupplier
    ) {
        try {
            if (!file.exists() || file.length() == 0) {
                T def = defaultSupplier.get();
                mapper.writeValue(file, def);
                return def;
            }
            return mapper.readValue(file, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
            return defaultSupplier.get();
        }
    }
}
