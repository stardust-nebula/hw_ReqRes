package model.reqres;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Resource {
    private int id;
    private String name;
    private int year;
    private String color;
    @SerializedName("pantone_value")
    private String pantoneValue;
}
