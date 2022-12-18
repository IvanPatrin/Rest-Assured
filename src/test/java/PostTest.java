
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;


public class PostTest {
    public static String sucLoginJson="{\n" +
            "    \"email\": \"eve.holt@reqres.in\",\n"+
            "    \"password\": \"cityslicka\"\n"+
            "}";

    public String URL="https://reqres.in/";

    public static String unSucLoginJson="{\n" +
            "    \"email\": \"peter@klaven\",\n"+
            "}";

    public static String sucCreate="{\n" +
            "    \"name\": \"morpheus\",\n"+
            "    \"job\": \"leader\"\n"+
            "}";
    public static String unSucCreate="{\n" +
            "    \"name\": \"morpheus\",\n"+
            "}";


    @Test
    public void LoginOk200() {
        RequestSpecification request = given();
        request.baseUri(URL);
        request.expect().statusCode(200);
        request.contentType(ContentType.JSON);
        request.body(sucLoginJson);
        request.log().all();
        Response response = request.post("/api/login");
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.assertThat().body("token",containsString("QpwL5tke4Pnpja7X4"));
        validatableResponse.log().body();
        validatableResponse.log().status();
    }

    @Test
    public void LoginError400() {
        RequestSpecification request = given();
        request.baseUri(URL);
        request.expect().statusCode(400);
        request.contentType(ContentType.JSON);
        request.body(unSucLoginJson);
        request.log().all();
        Response response = request.post("/api/login");
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(400);
        validatableResponse.log().status();
    }

    @Test
    public void CreateOk200() {
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationOK201());
        RestAssured
                .given()
                .body(sucCreate)
                .log().all()
                .when()
                .post("/api/users")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(201);
    }
    @Test
    public void CreateError400() {
        Specifications.installSpecification(Specifications.requestSpecification(URL),Specifications.responseSpecificationError400());
        RestAssured
                .given()
                .body(unSucCreate)
                .log().all()
                .when()
                .post("/api/users")
                .then()
                .log()
                .ifStatusCodeIsEqualTo(400);
    }
}
