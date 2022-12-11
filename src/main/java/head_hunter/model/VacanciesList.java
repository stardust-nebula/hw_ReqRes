package head_hunter.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.ArrayList;

@Data
public class VacanciesList {
    @Expose
    private ArrayList<Vacancy> items;



}
