package MultiplayerServer.DataModel;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MessageUtils {
    private static ObjectMapper objectMapper;

    public static String serialize(Message message) {
        MessageUtils.initObjectMapper();

        try {
            String s = objectMapper.writeValueAsString(message);
            System.out.println("Serialized: "+s);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Message deserialize(String message) {
        MessageUtils.initObjectMapper();

        try {
            System.out.println("Deserialize: "+message);
            return objectMapper.readValue(message, Message.class);

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }
}
