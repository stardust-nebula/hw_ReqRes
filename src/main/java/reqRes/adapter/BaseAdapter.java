package reqRes.adapter;

import com.google.gson.Gson;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static reqRes.util.StringConstants.*;

public class BaseAdapter {
    protected Gson converter = new Gson();

    protected Response get(String url) {
        return given()
                .when()
                .get(BASE_URL + url)
                .then()
                .log().all()
                .extract().response();
    }

    protected Response post(String url, String body){
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body(body)
                .when()
                .post(BASE_URL + url)
                .then()
                .log().all()
                .extract().response();
    }

    protected Response put(String url, String body){
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body(body)
                .when()
                .put(BASE_URL + url)
                .then()
                .log().all()
                .extract().response();
    }

    protected Response patch(String url, String body){
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body(body)
                .when()
                .patch(BASE_URL + url)
                .then()
                .log().all()
                .extract().response();
    }

    protected Response delete(String url){
        return given()
                .when()
                .delete(BASE_URL + url)
                .then()
                .log().all()
                .extract().response();
    }
}
