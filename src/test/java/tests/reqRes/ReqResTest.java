package tests.reqRes;

import com.google.gson.Gson;
import io.restassured.response.Response;
import model.reqres.Resource;
import model.reqres.ResourceList;
import model.reqres.User;
import model.reqres.UserAccount;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class ReqResTest {

    private static String baseUrl = "https://reqres.in/";

    @Test(testName = "GET LIST USERS", description = "Verify opened page number")
    public void checkGetListUsersTest() {
        int requestedPageNumber = 2;
        String requestedJsonBodyPath = "page";
        String endPoint = "api/users?page=" + requestedPageNumber;
        Response response = given()
                .when()
                .get(baseUrl + endPoint)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract().response();
        int actualPageNumber = response.path(requestedJsonBodyPath);
        Assert.assertEquals(actualPageNumber, requestedPageNumber);
    }

    @Test(testName = "GET SINGLE USER", description = "Verify user's email")
    public void checkGetSingleUserTest() {
        int userId = 2;
        String endPoint = "api/users/" + userId;
        String expectedUsersEmail = "janet.weaver@reqres.in";

        Response response = given()
                .when()
                .get(baseUrl + endPoint)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract().response();
        String actualUserEmail = response.jsonPath().getString("data.email");
        Assert.assertEquals(actualUserEmail, expectedUsersEmail);
    }

    @Test(testName = "GET SINGLE USER NOT FOUND", description = "Verify 404 Not Found if try to get not existing user")
    public void checkGetSingleUserNotFoundTest() {
        int userId = 23;
        String endpoint = "api/users/" + userId;
        Response response = given()
                .when()
                .get(baseUrl + endpoint)
                .then()
                .log().all()
                .statusCode(HTTP_NOT_FOUND)
                .extract().response();
        Assert.assertEquals(response.getStatusCode(), HTTP_NOT_FOUND);
    }

    @Test(testName = "GET LIST<RESOURCE>", description = "Verify # per page >= # of elements on the page")
    public void checkGetListResourceTest() {
        String endpoint = "api/unknown";
        String responseBody = given()
                .when()
                .get(baseUrl + endpoint)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract().body().asString();

        ResourceList resourceList = new Gson().fromJson(responseBody, ResourceList.class);
        int numberPerPageItems = resourceList.getPerPage();
        int numberRecourceItemsOnPage = resourceList.getData().size();
        boolean isNumberRecourceItemLessEqualsAllowed = numberPerPageItems >= numberRecourceItemsOnPage;
        Assert.assertTrue(isNumberRecourceItemLessEqualsAllowed);
    }

    @Test(testName = "GET SINGLE<RESOURCE>")
    public void checkGetSingleResourceTest() {
        int resourceId = 2;
        String endpoint = "api/unknown/";
        int expected_id = 2;
        String expected_name = "fuchsia rose";
        int expected_year = 2001;
        String expected_color = "#C74375";
        String expected_pantoneValue = "17-2031";
        String actualIdPath = "data.id";
        String actualNamePath = "data.name";
        String actualYearPath = "data.year";
        String actualColorPath = "data.color";
        String actualPantoneValuePath = "data.pantone_value";

        Resource expectedResource = Resource.builder()
                .id(expected_id)
                .name(expected_name)
                .year(expected_year)
                .color(expected_color)
                .pantoneValue(expected_pantoneValue)
                .build();
        Response response = given()
                .when()
                .get(baseUrl + endpoint + resourceId)
                .then()
                .log().all()
                .extract().response();
        Resource actualResource = Resource.builder()
                .id(response.path(actualIdPath))
                .name(response.path(actualNamePath))
                .year(response.path(actualYearPath))
                .color(response.path(actualColorPath))
                .pantoneValue(response.path(actualPantoneValuePath))
                .build();
        Assert.assertEquals(actualResource, expectedResource);
    }

    @Test(testName = "SINGLE <RESOURCE> NOT FOUND")
    public void checkGetSingleResourceNotFound() {
        int resourceId = 23;
        String endpoint = "api/unknown/";
        Response response = given()
                .when()
                .get(baseUrl + endpoint + resourceId)
                .then()
                .log().all()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(), HTTP_NOT_FOUND);
    }

    @Test(testName = "POST CREATE", description = "Verify 201 CREATED status code")
    public void checkPostCreateUserTest() {
        String url = "https://reqres.in/api/users";
        User user = User.builder()
                .name("morpheus")
                .job("leader")
                .build();
        Response response = given()
                .body(user)
                .when()
                .post(url)
                .then()
                .log().all()
                .extract().response();
        Assert.assertEquals(response.statusCode(), HTTP_CREATED);
    }

    @Test(testName = "PUT UPDATE")
    public void checkPutUpdateTest() {
        int resourceId = 2;
        String endpoint = "api/users/";
        String newName = "morpheus";
        String newJob = "zion resident";
        User user = User.builder()
                .name(newName)
                .job(newJob)
                .build();
        System.out.println("** User expected");
        System.out.println(user);
        Response response = given()
                .body(user)
                .log().body()
                .when()
                .put(baseUrl + endpoint + resourceId)
                .then()
                .log().body()
                .extract().response();
        Assert.assertEquals(response.statusCode(), HTTP_OK);
    }

    @Test(testName = "DELETE USER")
    public void checkDeleteUserTest() {
        int resourceId = 2;
        String endpoint = "api/users/";
        Response response = given()
                .when()
                .delete(baseUrl + endpoint + resourceId)
                .then()
                .extract().response();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), HTTP_NO_CONTENT);
        softAssert.assertTrue(response.body().asString().isEmpty());
        softAssert.assertAll();
    }

    @Test(testName = "POST - REGISTER USER")
    public void checkPostRegisterUserTest() {
        String endpoint = "api/register";
        String contentType = "application/json";
        String email = "eve.holt@reqres.in";
        String password = "pistol";

        String idLabel = "id";
        String tokenLabel = "token";
        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .password(password)
                .build();

        Response response = given()
                .body(userAccount)
                .contentType(contentType)
                .log().body()
                .when()
                .post(baseUrl + endpoint)
                .then()
                .extract().response();
        String body = response.body().asString();

        boolean isIdEmpty = response.body().path(idLabel).toString().isEmpty();
        boolean isTokenEmpty = response.body().path(tokenLabel).toString().isEmpty();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(body.contains(idLabel), "'id' label is missing");
        softAssert.assertFalse(isIdEmpty, "'id' is empty'");
        softAssert.assertTrue(body.contains(tokenLabel), "'token' label is missing");
        softAssert.assertFalse(isTokenEmpty, "'token' is empty");
        softAssert.assertAll();
    }

    @Test(testName = "POST REGISTER - UNSUCCESSFUL")
    public void checkPostRegisterUnsuccessfulTest() {
        String endpoint = "api/register";
        String contentType = "application/json";
        String email = "tests@mail";
        String errorLabel = "error";
        String errorMessage = "Missing password";
        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .build();

        Response response = given()
                .body(userAccount)
                .contentType(contentType)
                .when()
                .post(baseUrl + endpoint)
                .then()
                .extract().response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(response.body().asString().contains(errorLabel), "'error' label is missing");
        softAssert.assertEquals(response.path(errorLabel), errorMessage, "Error message doesn't match");
        softAssert.assertEquals(response.getStatusCode(), HTTP_BAD_REQUEST, "Status code doesn't match");
    }

    @Test(testName = "POST LOGIN - SUCCESSFUL")
    public void checkPostLoginSuccessfulTest() {
        String endpoint = "api/login";
        String contentType = "application/json";
        String email = "eve.holt@reqres.in";
        String password = "cityslicka";
        String tokenLabel = "token";

        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .password(password)
                .build();

        Response response = given()
                .body(userAccount)
                .contentType(contentType)
                .when()
                .post(baseUrl + endpoint)
                .then()
                .extract().response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), HTTP_OK);
        softAssert.assertTrue(response.body().asString().contains(tokenLabel), "'token' label is missing");
        softAssert.assertFalse(response.body().path(tokenLabel).toString().isEmpty(), "'token' value is missing");
        softAssert.assertAll();
    }

    @Test(testName = "POST LOGIN - UNSUCCESSFUL")
    public void checkPostLoginUnsuccessfulTest() {
        String endpoint = "api/login";
        String contentType = "application/json";
        String email = "peter@klaven";
        String errorLabel = "error";
        String errorMessage = "Missing password";

        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .build();

        Response response = given()
                .body(userAccount)
                .contentType(contentType)
                .when()
                .post(baseUrl + endpoint)
                .then()
                .extract().response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), HTTP_BAD_REQUEST, "Status code doesn't match");
        softAssert.assertTrue(response.body().asString().contains(errorLabel), "'error' label is missing");
        softAssert.assertEquals(response.path(errorLabel), errorMessage, "Error message doesn't match");
    }

    @Test(testName = "GET DELAYED RESPONSE")
    public void checkGetDelayedResponseTest() {
        String endpoint = "/api/users?delay=3";
        Response response = given()
                .when()
                .get(baseUrl + endpoint)
                .then()
                .extract().response();
        Assert.assertEquals(response.statusCode(), HTTP_OK);
    }
}
