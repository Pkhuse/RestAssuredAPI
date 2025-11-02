package RequestBuilder;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static Common.BasePaths.*;
import static Common.commonTestData.APP_ID;

public class APIRequestBuilder {

        public static RequestSpecification getRequestSpec() {
            return RestAssured.given()
                    .baseUri(WeatherBaseURl)
                    .header("Content-Type", "application/json")
                    .queryParam("appid", APP_ID);
        }
    }



















