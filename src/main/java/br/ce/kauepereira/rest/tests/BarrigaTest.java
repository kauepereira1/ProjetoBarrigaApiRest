package br.ce.kauepereira.rest.tests;

import br.ce.kauepereira.rest.core.BaseTest;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest {

    private String TOKEN;

    @Before
    public void login(){
        Map<String, String > login = new HashMap<>();
        login.put("email", "kauepereira001@gmail.com");
        login.put("senha", "123456");

        TOKEN = given()
                .body(login)
        .when()
                .post("/signin")

        .then()
                .statusCode(200)
                .extract().path("token")
        ;
    }

    @Test
    public void naoDeveAcessarAPISemToken(){
        given()

        .when()
                .get("/contas")
        .then()
                .statusCode(401)
        ;
    }

    @Test
    public void deveIncluirContaComSucesso(){

        given()

                // Se autenticando na API
                .header("Authorization", "JWT " + TOKEN)

                // Enviando os valores para incluir conta
                .body("{\"nome\": \"Conta qualquer\"}")
        .when()
                .post("/contas")

        .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso(){

        given()

                // Se autenticando na API
                .header("Authorization", "JWT " + TOKEN)

                // Enviando os valores para incluir conta
                .body("{\"nome\": \"Conta Alterada\"}")
        .when()
                .put("/contas/516443")

        .then()
                .statusCode(200)
                .body("nome", is("Conta Alterada"))
                .body("id", is(516443))
        ;
    }

    @Test
    public void naoDeveInserirContaMesmoNome(){

        given()

                // Se autenticando na API
                .header("Authorization", "JWT " + TOKEN)

                // Enviando os valores para incluir conta
                .body("{\"nome\": \"Conta Alterada\"}")
        .when()
                .post("/contas")

        .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
                ;
    }
}


