package Basics;

import PayloadBuilder.WeatherStationPayload;
import RequestBuilder.APIRequestBuilder;
import Utils.JsonUtils;
import Utils.LoggerUtil;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;



public class CreateAPITests {

    public class CreateStationTest {

        public static String createdStationId;

        @Test
        public void createStation() {

            JSONObject payload = WeatherStationPayload.createStationPayload();

            Response response = APIRequestBuilder.getRequestSpec()
                    .body(payload.toString())
                    .when()
                    .post("/stations")
                    .then()
                    .statusCode(201)
                    .extract()
                    .response();

            LoggerUtil.info("Create Station Response:\n" + JsonUtils.prettyPrint(new JSONObject(response.asString())));

            // Extract the generated ID from the response for chaining
            createdStationId = response.jsonPath().getString("ID");
            Assert.assertNotNull(createdStationId, "Station ID should not be null");
            Assert.assertTrue(createdStationId.matches("[A-Za-z0-9]+"), "Unexpected ID format: " + createdStationId);
        }
    }
}
