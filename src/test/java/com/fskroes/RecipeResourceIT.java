package com.fskroes;

import com.fskroes.model.RecipeModel;
import groovy.json.JsonBuilder;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

@QuarkusIntegrationTest
@TestMethodOrder(MethodOrderer.class)
public class RecipeResourceIT {

    @Order(1)
    @Test
    void shouldListRecipe() {
        given().accept(ContentType.JSON)
                .when()
                .get("/recipeIntegration")
                .then()
                .statusCode(200)
                .body(CoreMatchers.is("[RecipeModel(recipeName=Potato Mash, ingredients={Carrot=4, Potatoes=6, Garlic=1, Unions=2}, specificIngredients={Potatoes=6}, isVegetarian=true, numberOfServings=4, cookInstructions=First place pan on the stove and ready all the vegatables, cookingAppliances=Oven), RecipeModel(recipeName=Sweet Potato Mash, ingredients={Carrot=4, Sweet potatoes=6, Garlic=1, Unions=2}, specificIngredients={Sweet potatoes=6}, isVegetarian=true, numberOfServings=4, cookInstructions=First place pan on the stove and ready all the vegatables, cookingAppliances=Oven)]"));
    }

    @Order(2)
    @Test
    void shouldGetRecipeByName() {
        given()
                .when()
                .get("/recipeIntegration/Potato Mash")
                .then().statusCode(200)
                .body(hasSize(1));
    }

    @Order(3)
    @Test
    void shouldCreateNewRecipe() {
        JsonBuilder jsonObject = new JsonBuilder();
        jsonObject.call(RecipeModel.builder().recipeName("The new Recipe").build());

        given()
                .when()
                .body(jsonObject.toString())
                .post("/recipeIntegration")
                .then().statusCode(200)
                .body(containsString("\"recipeName\"=\"New potato recipe\""));
    }

    @Order(4)
    @Test
    void shouldDeleteRecipe() {
        given()
                .when()
                .delete("/recipeIntegration/1")
                .then().statusCode(201)
                .body(containsString("1"));
    }
}
