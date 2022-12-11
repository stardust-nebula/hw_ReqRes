package reqRes.adapter;

import io.restassured.response.Response;
import reqRes.model.User;

import static reqRes.util.StringConstants.USERS_ENDPOINT;

public class UsersAdapter extends BaseAdapter {
    //    private static final String PARAMETER_SIGN_START = "?";
    private static final String PAGE_PARAMETER = "page=";
    private static final String DELAY_PARAMETER = "delay=";


    public Response getListUserByPageNumber(int pageNumber) {
        return get(USERS_ENDPOINT + "?" + PAGE_PARAMETER + pageNumber);
    }

    public Response getSingleUserById(int userId) {
        return get(USERS_ENDPOINT + "/" + userId);
    }

    public Response createUser(User user) {
        return post(USERS_ENDPOINT, converter.toJson(user));
    }

    public Response putUpdateUser(int userId, User user) {
        return put(USERS_ENDPOINT + "/" + userId, converter.toJson(user));
    }

    public Response deleteUser(int userId) {
        return delete(USERS_ENDPOINT + "/" + userId);
    }

    public Response getUserDelayed(int userId) {
        return get(USERS_ENDPOINT + "?" + DELAY_PARAMETER + userId);
    }
}
