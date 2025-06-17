package utils

import com.google.gson.*

import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat

class datasJson implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(src));
    }

    @Override
    Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return dateFormat.parse(json.getAsString());
        } catch (ParseException e) {
            throw new JsonParseException("Não foi possível converter a data: " + json.getAsString(), e);
        }
    }
}