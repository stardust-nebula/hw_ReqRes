package reqRes.adapter;

import io.restassured.response.Response;
import reqRes.model.UserAccount;

import static reqRes.util.StringConstants.LOGIN_ENDPOINT;
import static reqRes.util.StringConstants.REGISTER_ENDPOINT;

public class AccountAdapter extends BaseAdapter {

    public Response registerUser(UserAccount userAccount) {
        return post(REGISTER_ENDPOINT, converter.toJson(userAccount));
    }

    public Response login(UserAccount userAccount) {
        return post(LOGIN_ENDPOINT, converter.toJson(userAccount));
    }
}
