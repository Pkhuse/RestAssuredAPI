package Tests;

import PayloadBuilder.WeatherStationPayload;
import Utils.LoggerUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.json.JSONObject;

import static Common.commonTestData.*;
import static io.restassured.RestAssured.given;

public class WeatherTest {

    // 👇 Shared variables for all test methods
    private static String createdStationId;
    private static JSONObject createdPayload;


    // tiny helper to read ID regardless of casing
    private String extractId(io.restassured.response.Response r) {
        String v = r.jsonPath().getString("ID");
        if (v == null || v.isBlank()) v = r.jsonPath().getString("id");
        return v;
    }

    @Test(priority = 1)
    public void registerWeatherStation() {
        RestAssured.baseURI = "http://api.openweathermap.org/data/3.0";

        // Build payload (auto-generates random values)
        createdPayload = WeatherStationPayload.createStationPayload();

        // POST /stations
        Response createResp = given()
                .header("Content-Type", "application/json")
                .queryParam("appid", "04cf376ab989c810984e51ab54bb9969")
                .body(createdPayload.toString())
                .when()
                .post("/stations")
                .then()
                .statusCode(create_success_status_code)
                .extract()
                .response();

        System.out.println("CREATE body:\n" + createResp.asPrettyString());

        createdStationId = extractId(createResp);   // <-- works for ID or id
        Assert.assertNotNull(createdStationId, "POST did not return an ID/id");


    }

    @Test(priority = 2, dependsOnMethods = "registerWeatherStation")
    public void getCreatedStationById() {
        Response getResp = given()
                .header("Content-Type", "application/json")
                .queryParam("appid", "04cf376ab989c810984e51ab54bb9969")
                .when()
                .get("/stations/{id}", createdStationId)
                .then()
                .statusCode(success_status_code)
                .extract()
                .response();


        String returnedId = extractId(getResp);     // <-- picks lowercase 'id' from your body
        Assert.assertNotNull(returnedId, "GET body lacked ID/id");
        Assert.assertEquals(returnedId, createdStationId, "Returned ID must match created ID");

        Assert.assertNotNull(returnedId,
                "GET /stations/{id} did not contain 'ID'. Full body:\n" + getResp.asPrettyString());
        Assert.assertEquals(returnedId, createdStationId,
                "Returned ID must match created ID");
        System.out.println("GET body:\n" + getResp.asPrettyString());

        // Verify all fields match what was created
        Assert.assertEquals(getResp.jsonPath().getString("name"), createdPayload.getString("name"));
        Assert.assertEquals(getResp.jsonPath().getDouble("latitude"), createdPayload.getDouble("latitude"));
        Assert.assertEquals(getResp.jsonPath().getDouble("longitude"), createdPayload.getDouble("longitude"));
        Assert.assertEquals(getResp.jsonPath().getInt("altitude"), createdPayload.getInt("altitude"));
    }

    @Test(priority = 3)
    public void getAllStations() {
        RestAssured.baseURI = "http://api.openweathermap.org/data/3.0";

        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("appid", "04cf376ab989c810984e51ab54bb9969")
                .when()
                .get("/stations")
                .then()
                .statusCode(success_status_code)
                .extract()
                .response();

        System.out.println("GET All Stations Response:");
        System.out.println(response.getBody().asPrettyString());

        // Basic validations
        Assert.assertTrue(response.getBody().asString().contains("id") ||
                        response.getBody().asString().contains("ID"),
                "Expected at least one station object in the response");
    }

    @Test(priority = 4, dependsOnMethods = "getCreatedStationById")
    public void updateCreatedStation() {
        RestAssured.baseURI = "http://api.openweathermap.org/data/3.0";

        // Make sure we have a station ID
        Assert.assertNotNull(createdStationId, "Cannot update station because ID is null");

        // Build an updated payload
        JSONObject updatedPayload = new JSONObject();
        updatedPayload.put("external_id", "EXT_UPDATED_" + new java.util.Random().nextInt(100000));
        updatedPayload.put("name", "Station_Updated_" + new java.util.Random().nextInt(100000));
        updatedPayload.put("latitude", createdPayload.getDouble("latitude"));
        updatedPayload.put("longitude", createdPayload.getDouble("longitude"));
        updatedPayload.put("altitude", createdPayload.getInt("altitude"));

        // Perform PUT /stations/{id}
        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("appid", "04cf376ab989c810984e51ab54bb9969")
                .body(updatedPayload.toString())
                .when()
                .put("/stations/{id}", createdStationId)
                .then()
                .statusCode(success_status_code)
                .extract()
                .response();

        System.out.println("UPDATE body:\n" + response.asPrettyString());

        // Validate updates
        String newName = response.jsonPath().getString("name");
        String newExternalId = response.jsonPath().getString("external_id");

        Assert.assertEquals(newName, updatedPayload.getString("name"), "Station name should update");
        Assert.assertEquals(newExternalId, updatedPayload.getString("external_id"), "External ID should update");
    }

    @Test(priority = 5, dependsOnMethods = "getCreatedStationById")
    public void deleteCreatedStation() {
        Response deleteResp = given()
                .header("Content-Type", "application/json")
                .queryParam("appid", "04cf376ab989c810984e51ab54bb9969")
                .when()
                .delete("/stations/{id}", createdStationId)
                .then()
                .statusCode(delete_success_status_code)   //
                .extract()
                .response();

        System.out.println("DELETE status: " + deleteResp.statusCode());

    }

    @Test(priority = 6, dependsOnMethods = "deleteCreatedStation")
    public void getDeletedStation() {
        RestAssured.baseURI = "http://api.openweathermap.org/data/3.0";

        Response getResp = given()
                .header("Content-Type", "application/json")
                .queryParam("appid", "04cf376ab989c810984e51ab54bb9969")
                .when()
                .get("/stations/{id}", createdStationId)
                .then()
                .statusCode(not_found_status_code)   // ✅ not found after delete
                .extract()
                .response();

        System.out.println("GET-after-delete body:\n" + getResp.asPrettyString());

    }

    @Test(priority = 7)
    public void createStation_withEmptyExternalId() {
        RestAssured.baseURI = "http://api.openweathermap.org/data/3.0";
        LoggerUtil.info("=== Starting test: createStation_withEmptyExternalId_shouldFail ===");

        JSONObject payload = new JSONObject();
        payload.put("external_id", ""); // intentionally blank
        payload.put("name", "Station_EmptyExtId");
        payload.put("latitude", 37.76);
        payload.put("longitude", -122.43);
        payload.put("altitude", 150);


        Response response = given()
                .header("Content-Type", "application/json")
                .queryParam("appid", "04cf376ab989c810984e51ab54bb9969")
                .body(payload.toString())
                .when()
                .post("/stations")
                .then()
                .statusCode(bad_request_status_code)
                .extract()
                .response();

        int status = response.getStatusCode();
        String body = response.getBody().asPrettyString();

        LoggerUtil.info("Response status: " + status);

        LoggerUtil.info("Response body:\n" + body);
        Assert.assertEquals(status, 400, "Expected status code 400 for empty external_id");
        System.out.println("Empty External ID" + body);
    }
}
