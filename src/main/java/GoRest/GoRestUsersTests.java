package GoRest;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class GoRestUsersTests {
    Faker faker=new Faker();
    int userID;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/users";  // baseUri RequestSpecification den önce tanımlanmalı.
        //baseURI ="https://test.gorest.co.in/public/v2/users/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 8c15cdf48ee9ea7ab5f17aa899db5cc65d2b4367d128ee26d5c2c2fe4dbd3e63")
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test(enabled = false)
    public void createUserJson(){
        // POST https://gorest.co.in/public/v2/users
        // "Authorization: Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d"
        // {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
        String randomFullName=faker.name().fullName();
        String randomEmail=faker.internet().emailAddress();

         userID=
                given()
                        .header("Authorization"," Bearer 8c15cdf48ee9ea7ab5f17aa899db5cc65d2b4367d128ee26d5c2c2fe4dbd3e63")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\""+randomFullName+"\", \"gender\":\"male\", \"email\":\""+randomEmail+"\", \"status\":\"active\"}")
                       // .log().uri()
                       // .log().body()
                        .when()
                        .post("")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
    }


    @Test
    public void createUserMap(){
        // POST https://gorest.co.in/public/v2/users
        // "Authorization: Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d"
        // {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
        String randomFullName=faker.name().fullName();
        String randomEmail=faker.internet().emailAddress();

        Map<String,String> newUser=new HashMap<>();
        newUser.put("name",randomFullName);
        newUser.put("gender","male");
        newUser.put("email",randomEmail);
        newUser.put("status","active");

        userID=
                given()
                        .header("Authorization"," Bearer 8c15cdf48ee9ea7ab5f17aa899db5cc65d2b4367d128ee26d5c2c2fe4dbd3e63")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        // .log().uri()
                        // .log().body()
                        .when()
                        .post("")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }

    @Test(enabled = false)
    public void createUserClass(){
        String randomFullName=faker.name().fullName();
        String randomEmail=faker.internet().emailAddress();

        User newUser = new User();
        newUser.name = randomFullName;
        newUser.gender = "male";
        newUser.email = randomEmail;
        newUser.status = "active";

        userID=
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        // .log().uri()
                        // .log().body()
                        .when()
                        .post("")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }
    @Test(dependsOnMethods = "createUserMap")
    public void getUserById(){
        given()
                .header("Authorization","Bearer 8c15cdf48ee9ea7ab5f17aa899db5cc65d2b4367d128ee26d5c2c2fe4dbd3e63")
                .when()
                .get(""+userID)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "getUserById")
    public void updateUser(){
        Map<String,String> updateUser=new HashMap<>();
        updateUser.put("name","erasd asdaw");

        given()
                .spec(reqSpec)
                .body(updateUser)
                .when()
                .put(""+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id",equalTo(userID))
                .body("name",equalTo("erasd asdaw"));
    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser(){
        given()
                .spec(reqSpec)
                .when()
                .delete(""+userID)

                .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative(){
        given()
                .spec(reqSpec)
                .when()
                .delete(""+userID)

                .then()
                .log().all()
                .statusCode(404)
        ;
    }
}
