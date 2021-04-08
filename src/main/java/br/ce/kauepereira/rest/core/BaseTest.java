package br.ce.kauepereira.rest.core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;

public class BaseTest implements  Constantes{

    @BeforeClass
    public static void setup(){

        // Chamando todos atribudos que foram passados na classe Contantes
        RestAssured.baseURI = APP_BASE_URL;
        RestAssured.port = APP_PORT;
        RestAssured.basePath = APP_BASE_PATH;

        //Setando o contentType da minha aplicação que é JSON
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(APP_CONTENT_TYPE);
        RestAssured.requestSpecification = reqBuilder.build();


        // Limite maximo de timeout
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
        RestAssured.responseSpecification = resBuilder.build();

        // Vai habilitar o log se apenas eu tiver algum teste com erro
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }
}
