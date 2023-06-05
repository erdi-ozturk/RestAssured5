import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseLogSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void test(){
        given()
            //Hazirlik islemleri : (token, send body, parametreler)

                .when()
                //end point (url) metodu
                .then();
                //assertion, test, data islemleri
    }
    @Test
    public void statusCodeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body() // donen kod json datasi log.all()
                .statusCode(200) //donus kodu 200 mu
                .contentType(ContentType.JSON)

        ;

    }

    @Test
    public void checkCountryInResponseBody(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body() // donen kod json datasi log.all()
                .statusCode(200) //donus kodu 200 mu
                .body("country",equalTo("United States"))

        ;

    }

    //    PM                            RestAssured
//    body.country                  body("country")
//    body.'post code'              body("post code")
//    body.places[0].'place name'   body("places[0].'place name'")
//    body.places.'place name'      body("places.'place name'")
//    bütün place nameleri bir arraylist olarak verir
//    https://jsonpathfinder.com/

    @Test
    public void checkstateInResponseBody(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))
        ;
    }

    @Test
    public void checkHasItem(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")


                .then()
                //.log().body() // donen kod json datasi log.all()
                .statusCode(200) //donus kodu 200 mu
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))
                // butun place namelerin herhangi birinde var mi

        ;

    }

    @Test
    public void bodyArrayHasSize(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body() // donen kod json datasi log.all()
                .statusCode(200) //donus kodu 200 mu
                .body("places",hasSize(1))

        ;

    }

    @Test
    public void combiningTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body() // donen kod json datasi log.all()
                .statusCode(200) //donus kodu 200 mu
                .body("places",hasSize(1))
                .body("places.state",hasItem("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))

        ;

    }
    @Test
    public  void pathParamTest(){
        given()
                .pathParam("ulke","us")
                .pathParam("postaKod",90210)
                .log().uri() // request url
                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200)

        ;
    }

    @Test
    public  void queryParamTest(){
        //https://gorest.co.in/public/v1/users?page=2
        given()
                .param("page",1)
                .log().uri() // request url
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(200)

        ;
    }

    @Test
    public  void queryParamTest2(){
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
        for (int i = 1; i < 10; i++) {
            given()
                    .param("page",i)
                    .log().uri() // request url
                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    .body("meta.pagination.page",equalTo(i))
            ;
        }
    }
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void Setup(){
        baseURI="https://gorest.co.in/public/v1";
        requestSpecification=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpecification=new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }


    @Test
    public  void requestResponseSpecification(){
        // https://gorest.co.in/public/v1/users?page=3
        for (int i = 1; i < 10; i++) {
            given()
                    .param("page",i)
                    .spec(requestSpecification)
                    .when()
                    .get("/users")

                    .then()
                    .spec(responseSpecification)
            ;
        }
    }


    @Test
    public void extractingJsonPath(){

        String countryName=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .log().body()
                .extract().path("country")
        ;
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName,"United States");

    }

    @Test
    public void extractingJsonPath1(){

        String placeName=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")


                        .then()
                        .log().body()
                        .extract().path("places[0].'place name'")
                ;
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName,"Beverly Hills");

    }

    @Test
    public void extractingJsonPath2(){

        int limit=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")


                        .then()
                        // .log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);

    }

    @Test
    public void extractingJsonPath3(){

        int limit=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")


                        .then()
                        // .log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);

    }
@Test
    public void extractingJsonPath4(){
        List<Integer> idler=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.id")
                ;

        System.out.println("idler = " + idler);
    }

    @Test
    public void extractingJsonPath5(){
        List<String> names=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.name")
                ;

        System.out.println("idler = " + names);
    }

    @Test
    public void extractingJsonPathResponseAll(){
        Response response =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().response()
                ;
        List<Integer> idler=response.path("data.id");
        List<String> names=response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Amaresh Devar"));
        Assert.assertTrue(idler.contains(1516232));
        Assert.assertEquals(limit,10,"test sonucu hatali");
    }

    @Test
    public void extractJsonAll_POJO(){
        //POJO JSON nesnesi Location NeSNESI
        Location location=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                //.log().body()
                .extract().body().as(Location.class)
        ;

        System.out.println("location = " + location.getCountry());
        for (Place p: location.getPlaces())
            System.out.println("p = " + p);

        System.out.println("location.getPlaces().get(0).getPlacename() = " + location.getPlaces().get(0).getPlacename());
    }

    @Test
    public void extractPOJO_Soru(){
        // aşağıdaki endpointte(link)  Dörtağaç Köyü ait diğer bilgileri yazdırınız
        Location location =
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().body().as(Location.class)
        ;

        for (Place p: location.getPlaces())
            if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü")){
                System.out.println("p = " + p);
            }
    }
}
