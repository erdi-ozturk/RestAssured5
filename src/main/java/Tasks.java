import Model.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {
    @Test
    public void Task1(){
        /* Task 1
         * create a request to https://jsonplaceholder.typicode.com/todos/2
         * expect status 200
         * Converting Into POJO
         */
        ToDo toDo=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
               // .log().all()
                .extract().body().as(ToDo.class)

        ;

        System.out.println("toDo = " + toDo);
        System.out.println("toDo.getTitle() = " + toDo.getTitle());
    }

    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void Task3(){
        /**
         * Task 3
         * create a request to https://jsonplaceholder.typicode.com/todos/2
         * expect status 200
         * expect content type JSON
         * expect title in response body to be "quis ut nam facilis et officia qui"
         */
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .statusCode(200)
                        .contentType((ContentType.JSON))
                        // .log().all()
                        .body("title",equalTo("quis ut nam facilis et officia qui"))
                ;

    }
    @Test
    public void task4()
    {
        //1.yöntem (hamcrest)
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
        ;

        //2.yöntem (testNG)
        Boolean completed=
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().path("completed")
                ;

        Assert.assertFalse(completed);
    }
}
