package model.head_hunter;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class Salary {
    @Expose
    private int from;
    @Expose
    private int to;
    @Expose
    String currency;
    private boolean gross;
}
