package reqRes.adapter;

import io.restassured.response.Response;

import static reqRes.util.StringConstants.RESOURCE_ENDPOINT;

public class ResourceAdapter extends BaseAdapter{

    public Response getListResources(){
        return get(RESOURCE_ENDPOINT);
    }

    public Response getSingleResourceById(int resourceId){
        return get(RESOURCE_ENDPOINT + "/" + resourceId);
    }

}
