package Utils;

import org.json.simple.JSONObject;

    public class JsonUtils {

        public static JSONObject toJson(String jsonString) {
            return new JSONObject();
        }

        public static String prettyPrint(JSONObject json) {
            return json.toString();
        }

        public static String prettyPrint(org.json.JSONObject jsonObject) {
            return null;
        }
    }
