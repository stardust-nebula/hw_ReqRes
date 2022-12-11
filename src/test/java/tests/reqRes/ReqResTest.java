package tests.reqRes;

import reqRes.adapter.AccountAdapter;
import reqRes.adapter.ResourceAdapter;
import reqRes.adapter.UsersAdapter;
import com.google.gson.Gson;
import io.restassured.response.Response;
import reqRes.model.Resource;
import reqRes.model.ResourceList;
import reqRes.model.User;
import reqRes.model.UserAccount;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static java.net.HttpURLConnection.*;

public class ReqResTest {

    @Test(testName = "GET LIST USERS", description = "Verify opened page number")
    public void checkGetListUsersTest() {
        int requestedPageNumber = 2;
        String requestedJsonBodyPath = "page";
        int actualPageNumber = new UsersAdapter()
                .getListUserByPageNumber(requestedPageNumber)
                .path(requestedJsonBodyPath);
        Assert.assertEquals(actualPageNumber, requestedPageNumber);
    }

    @Test(testName = "GET SINGLE USER", description = "Verify user's email")
    public void checkGetSingleUserTest() {
        int userId = 2;
        String expectedUsersEmail = "janet.weaver@reqres.in";
        String actualUserEmail = new UsersAdapter().getSingleUserById(userId).jsonPath().getString("data.email");
        Assert.assertEquals(actualUserEmail, expectedUsersEmail);
    }

    @Test(testName = "GET SINGLE USER NOT FOUND", description = "Verify 404 Not Found if try to get not existing user")
    public void checkGetSingleUserNotFoundTest() {
        int userId = 23;
        Assert.assertEquals(new UsersAdapter().getSingleUserById(userId).statusCode(), HTTP_NOT_FOUND);
    }

    @Test(testName = "GET LIST<RESOURCE>", description = "Verify # per page >= # of elements on the page")
    public void checkGetListResourceTest() {
        String responseBody = new ResourceAdapter().getListResources().body().asString();
        ResourceList resourceList = new Gson().fromJson(responseBody, ResourceList.class);
        int numberPerPageItems = resourceList.getPerPage();
        int numberResourceItemsOnPage = resourceList.getData().size();
        boolean isNumberResourceItemLessEqualsAllowed = numberPerPageItems >= numberResourceItemsOnPage;
        Assert.assertTrue(isNumberResourceItemLessEqualsAllowed);
    }

    @Test(testName = "GET SINGLE<RESOURCE>")
    public void checkGetSingleResourceTest() {
        int resourceId = 2;
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
                .id(resourceId)
                .name(expected_name)
                .year(expected_year)
                .color(expected_color)
                .pantoneValue(expected_pantoneValue)
                .build();
        Response response = new ResourceAdapter().getSingleResourceById(resourceId);
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
        Response response = new ResourceAdapter().getSingleResourceById(resourceId);
        Assert.assertEquals(response.getStatusCode(), HTTP_NOT_FOUND);
    }

    @Test(testName = "POST CREATE", description = "Verify 201 CREATED status code")
    public void checkPostCreateUserTest() {
        String userName = "morpheus";
        String userJob = "leader";
        User user = User.builder()
                .name(userName)
                .job(userJob)
                .build();
        Assert.assertEquals(new UsersAdapter().createUser(user).statusCode(), HTTP_CREATED);
    }

    @Test(testName = "PUT UPDATE", description = "Verify 200 OK status code")
    public void checkPutUpdateTest() {
        int userId = 2;
        String newName = "morpheus";
        String newJob = "zion resident";
        User user = User.builder()
                .name(newName)
                .job(newJob)
                .build();
        Response response = new UsersAdapter().putUpdateUser(userId, user);
        Assert.assertEquals(response.statusCode(), HTTP_OK);
    }

    @Test(testName = "DELETE USER", description = "Verify status code 204, response body is empty on delete user")
    public void checkDeleteUserTest() {
        int userId = 2;
        Response response = new UsersAdapter().deleteUser(userId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), HTTP_NO_CONTENT);
        softAssert.assertTrue(response.body().asString().isEmpty());
        softAssert.assertAll();
    }

    @Test(testName = "POST - REGISTER USER", description = "Verify response body on register user")
    public void checkPostRegisterUserTest() {
        String email = "eve.holt@reqres.in";
        String password = "pistol";

        String idLabel = "id";
        String tokenLabel = "token";

        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .password(password)
                .build();

        Response response = new AccountAdapter().registerUser(userAccount);
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

    @Test(testName = "POST REGISTER - UNSUCCESSFUL", description = "Verify error message in response on " +
            "unsuccessful registration")
    public void checkPostRegisterUnsuccessfulTest() {
        String email = "tests@mail";
        String errorLabel = "error";
        String errorMessage = "Missing password";
        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .build();
        Response response = new AccountAdapter().registerUser(userAccount);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(response.body().asString().contains(errorLabel), "'error' label is missing");
        softAssert.assertEquals(response.path(errorLabel), errorMessage, "Error message doesn't match");
        softAssert.assertEquals(response.getStatusCode(), HTTP_BAD_REQUEST, "Status code doesn't match");
    }

    @Test(testName = "POST LOGIN - SUCCESSFUL", description = "Verify response on successful login")
    public void checkPostLoginSuccessfulTest() {
        String email = "eve.holt@reqres.in";
        String password = "cityslicka";
        String tokenLabel = "token";
        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .password(password)
                .build();
        Response response = new AccountAdapter().login(userAccount);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), HTTP_OK);
        softAssert.assertTrue(response.body().asString().contains(tokenLabel), "'token' label is missing");
        softAssert.assertFalse(response.body().path(tokenLabel).toString().isEmpty(), "'token' value is missing");
        softAssert.assertAll();
    }

    @Test(testName = "POST LOGIN - UNSUCCESSFUL")
    public void checkPostLoginUnsuccessfulTest() {
        String email = "peter@klaven";
        String errorLabel = "error";
        String errorMessage = "Missing password";
        UserAccount userAccount = UserAccount.builder()
                .email(email)
                .build();
        Response response = new AccountAdapter().login(userAccount);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), HTTP_BAD_REQUEST, "Status code doesn't match");
        softAssert.assertTrue(response.body().asString().contains(errorLabel), "'error' label is missing");
        softAssert.assertEquals(response.path(errorLabel), errorMessage, "Error message doesn't match");
    }

    @Test(testName = "GET DELAYED RESPONSE")
    public void checkGetDelayedResponseTest() {
        int userId = 3;
        Response response = new UsersAdapter().getUserDelayed(userId);
        Assert.assertEquals(response.statusCode(), HTTP_OK);
    }
}
