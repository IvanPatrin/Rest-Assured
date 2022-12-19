import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;


@Execution(CONCURRENT)
public class GetTest {
    public final String URL = "https://www.freetogame.com/api/";

    @Test
    public void RequestError404() {
        RestAssured
                .given()
                .baseUri(URL)
                .contentType(ContentType.JSON)
                .queryParam("platform", "mobile")
                .queryParam("tag", "mmorpg")
                .log().all()
                .when()
                .get("/games")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(404);
    }

    @Test
    public void RequestOK200() {
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK200());
        RestAssured
                .given()
                .queryParam("platform", "pc")
                .queryParam("sort-by", "popularity")
                .log().all()
                .when()
                .get("/games")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(200);
    }

    @Test
    public void AssertTitleOk200() {
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK200());
        RequestSpecification request = RestAssured.given();
        request.contentType("application/json");
        request.queryParam("platform", "pc");
        request.queryParam("sort-by", "alphabetical");
        request.log().all();
        Response response = request.get("/games");
        ValidatableResponse validatableResponse = response.then();
        if ((response.header("Content-Type")).contains("json"))
            validatableResponse.log().headers();
        validatableResponse.assertThat().body("[0].title",containsString("4Story"));
        validatableResponse.log().body();
    }

    @Test
    public void AssertSecondTitleOk200() {
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK200());
        RequestSpecification request = RestAssured.given();
        request.contentType("application/json");
        request.queryParam("platform", "pc");
        request.queryParam("sort-by", "popularity");
        request.log().all();
        Response response = request.get("/games");
        ValidatableResponse validatableResponse = response.then();
        if ((response.header("Content-Type")).contains("json"))
            validatableResponse.log().headers();
        validatableResponse.assertThat().body("[0].title",containsString("Genshin Impact"));
        validatableResponse.log().body();
    }

}