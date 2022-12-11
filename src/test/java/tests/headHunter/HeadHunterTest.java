package tests.headHunter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.head_hunter.VacanciesList;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;

public class HeadHunterTest {

    // https://api.hh.ru/vacancies


    @Test
    public void vacancyTest() {
        String body = given()
                .when()
                .get("https://api.hh.ru/vacancies?text=QA automation")
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract().body().asString();

        VacanciesList vacanciesList = new Gson().fromJson(body, VacanciesList.class);
        int salaryTo = vacanciesList.getItems().get(1).getSalary().getTo();
        System.out.println(salaryTo);

        VacanciesList vacanciesListWithoutExpose = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .fromJson(body, VacanciesList.class);
        System.out.println(vacanciesListWithoutExpose);
        System.out.println(vacanciesList);
    }


}
