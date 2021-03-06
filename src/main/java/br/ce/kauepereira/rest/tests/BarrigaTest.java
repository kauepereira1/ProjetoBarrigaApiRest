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
                .body("error", is("J?? existe uma conta com esse nome!"))
                ;
    }

    @Test
    public void deveInserirMovimentacaoComSucesso(){
        Movimentacao mov = new Movimentacao();

        mov.setConta_id(516443);
//        mov.setUsuario_id();
        mov.setDescricao("Descricao da Movimentacao");
        mov.setEnvolvido("Envolvido na mov");
        mov.setTipo("REC");
        mov.setData_transacao("01/01/2000");
        mov.setData_pagamento("10/10/2010");
        mov.setValor(100f);
        mov.setStatus(true);


        given()

                // Se autenticando na API
                .header("Authorization", "JWT " + TOKEN)

                // Passando o objeto mov para enviar os dados da transacao
                .body(mov)
        .when()
                .post("/transacoes")

        .then()
                .statusCode(201)
                ;
    }
}
