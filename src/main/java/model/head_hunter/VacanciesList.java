package model.head_hunter;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.ArrayList;

@Data
public class VacanciesList {
    @Expose
    private ArrayList<Vacancy> items;



}
