package tests.onliner;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;

public class OnlinerTest {

    @Test
    public void getCurrencyUsdRateTest() {
        Response response = given()
                .log().all()
                .when()
                .get("https://www.onliner.by/sdapi/kurs/api/bestrate?currency=USD&type=nbrb")
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), HTTP_OK);

    }

    @Test
    public void getCurrencyRateTest() {
        given()
                .log().all()
                .when()
                .get("https://www.onliner.by/sdapi/kurs/api/bestrate?currency=USD&type=nbrb")
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("amount", Matchers.equalTo("2,4466"));
    }

}
