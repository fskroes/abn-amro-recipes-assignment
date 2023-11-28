package com.fskroes.boundary;

import com.fskroes.control.RecipeControl;
import com.fskroes.model.RecipeModel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
public class RecipeBoundaryTest {

    @Inject
    RecipeBoundary recipeBoundary;

    @InjectMock
    RecipeControl recipeControl;

    @Test
    void getRecipes_given_returnsAllRecipes() {
        var expectedRecipes = List.of(
                RecipeModel.builder()
                        .recipeName("Sweet Potato Mash")
                        .build(),
                RecipeModel.builder()
                        .recipeName("Potato Mash")
                        .build()
        );

        doReturn(expectedRecipes)
             .when(recipeControl)
             .getAllRecipes();

        var response = recipeBoundary.getRecipes();

        assertNotNull(response);
        var awaitedResponse = response.await().atMost(Duration.ofSeconds(5));
        assertEquals(expectedRecipes, awaitedResponse);
    }

    @Test
    void getRecipe_givenCorrectRecipeName_returnsRecipeOfGivenName() {
        var recipeName = "Potato Mash";
        var expectedRecipes = RecipeModel.builder()
                .recipeName(recipeName)
                .build();

        doReturn(expectedRecipes)
             .when(recipeControl)
                .getRecipe(recipeName);

        var response = recipeBoundary.getRecipe(recipeName);

        assertNotNull(response);

        var awaitedResponse = response.await().atMost(Duration.ofSeconds(5));
        assertEquals(expectedRecipes, awaitedResponse);
    }

    @Test
    void createRecipe_givenRecipeModel_createAndReturnsGivenRecipe() {
        var recipeName = "Potato Mash";
        var expectedRecipe = RecipeModel.builder()
                .recipeName(recipeName)
                .build();

        doReturn(expectedRecipe)
                .when(recipeControl)
                .createRecipe(expectedRecipe);

        var response = recipeBoundary.createRecipe(expectedRecipe);

        assertNotNull(response);

        assertEquals(response.getStatus(), 201);
    }
}
