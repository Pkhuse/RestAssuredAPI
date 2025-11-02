package PayloadBuilder;
import org.json.JSONObject;
import Utils.testDataGenerator;


public class WeatherStationPayload {


        public static JSONObject createStationPayload() {
            // Use the generator for random data
            String externalId = testDataGenerator.generateExternalId();
            String name = testDataGenerator.generateStationName();

            // Default coordinates
            double latitude = 37.76;
            double longitude = -122.43;
            int altitude = 150;


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("external_id", externalId);
            jsonObject.put("name", name);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("altitude", altitude);
            return jsonObject;
        }

    }

