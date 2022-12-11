package model.head_hunter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Vacancy {
    @Expose
    private String name;
    @Expose
    private  Salary salary;
    @SerializedName("alternate_url")
    @Expose
    private String alternateUrl;
    // "created_at": "2022-12-01T14:18:38+0300" ?


}
