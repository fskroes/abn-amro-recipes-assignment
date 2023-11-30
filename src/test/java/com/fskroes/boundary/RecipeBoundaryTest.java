package com.fskroes.boundary;

import com.fskroes.control.RecipeControl;
import com.fskroes.model.RecipeModel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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

    @Test
    void getRecipeIsVegetarian_givenCorrectParameters_returnRecipe() {
        var isVegetarian = true;
        var expectedRecipe = List.of(
                RecipeModel.builder()
                        .recipeName("Potato Mash")
                        .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Potatoes", "6"))
                        .specificIngredients(Map.of("Potatoes", "6"))
                        .isVegetarian(true)
                        .numberOfServings(4)
                        .cookInstructions("First place pan on the stove and ready all the vegatables")
                        .cookingAppliances("Oven")
                        .build(),
                RecipeModel.builder()
                        .recipeName("Sweet Potato Mash")
                        .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Sweet potatoes", "6"))
                        .specificIngredients(Map.of("Sweet potatoes", "6"))
                        .isVegetarian(true)
                        .numberOfServings(4)
                        .cookInstructions("First place pan on the stove and ready all the vegatables")
                        .cookingAppliances("Oven")
                        .build()
        );

        doReturn(expectedRecipe)
                .when(recipeControl)
                .getRecipeVegetarian(isVegetarian);

        var response = recipeBoundary.getRecipeIsVegetarian(true)
                .await()
                .atMost(Duration.ofSeconds(5));

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(true, response.get(0).getIsVegetarian());
    }

    @Test
    void getRecipeIsVegetarian_givenCorrectParametersFalse_returnEmptyList() {
        var isVegetarian = true;
        var expectedRecipe = List.of();

        doReturn(expectedRecipe)
                .when(recipeControl)
                .getRecipeVegetarian(isVegetarian);

        var response = recipeBoundary.getRecipeIsVegetarian(true)
                .await()
                .atMost(Duration.ofSeconds(5));

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void getRecipeBasedOnServingAndIngredient_givenCorrectParameters_returnsRecipes() {
        var servings = 4;
        var ingredient = "Unions";
        var expectedRecipe = List.of(
                RecipeModel.builder()
                        .recipeName("Potato Mash")
                        .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Potatoes", "6"))
                        .specificIngredients(Map.of("Potatoes", "6"))
                        .isVegetarian(true)
                        .numberOfServings(4)
                        .cookInstructions("First place pan on the stove and ready all the vegatables")
                        .cookingAppliances("Oven")
                        .build(),
                RecipeModel.builder()
                        .recipeName("Sweet Potato Mash")
                        .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Sweet potatoes", "6"))
                        .specificIngredients(Map.of("Sweet potatoes", "6"))
                        .isVegetarian(true)
                        .numberOfServings(4)
                        .cookInstructions("First place pan on the stove and ready all the vegatables")
                        .cookingAppliances("Oven")
                        .build()
        );

        doReturn(expectedRecipe)
                .when(recipeControl)
                .getRecipeOnServingAndIngredient(servings, ingredient);

        var response = recipeBoundary.getRecipeBasedOnServingAndIngredient(servings, ingredient)
                .await()
                .atMost(Duration.ofSeconds(5));

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(4, response.get(0).getNumberOfServings());
        assertTrue(response.get(0).getIngredients().containsKey(ingredient));
    }

    @Test
    void getRecipeBasedOnServingAndIngredient_givenIncorrectParameters_returnsRecipes() {
        var servings = 2;
        var ingredient = "Unions";
        var expectedRecipe = List.of();

        doReturn(expectedRecipe)
                .when(recipeControl)
                .getRecipeOnServingAndIngredient(servings, ingredient);

        var response = recipeBoundary.getRecipeBasedOnServingAndIngredient(servings, ingredient)
                .await()
                .atMost(Duration.ofSeconds(5));

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void getRecipeBasedOnInstructionAndIngredient_givenCorrectParameters_returnsRecipe() {
        var instruction = "stove";
        var ingredient = "Unions";
        var expectedRecipe = List.of(
                RecipeModel.builder()
                        .recipeName("Potato Mash")
                        .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Potatoes", "6"))
                        .specificIngredients(Map.of("Potatoes", "6"))
                        .isVegetarian(true)
                        .numberOfServings(4)
                        .cookInstructions("First place pan on the stove and ready all the vegatables")
                        .cookingAppliances("Oven")
                        .build(),
                RecipeModel.builder()
                        .recipeName("Sweet Potato Mash")
                        .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Sweet potatoes", "6"))
                        .specificIngredients(Map.of("Sweet potatoes", "6"))
                        .isVegetarian(true)
                        .numberOfServings(4)
                        .cookInstructions("First place pan on the stove and ready all the vegatables")
                        .cookingAppliances("Oven")
                        .build()
        );

        doReturn(expectedRecipe)
                .when(recipeControl)
                .getRecipeOnInstructionAndIngredient(instruction, ingredient);

        var response = recipeBoundary.getRecipeBasedOnInstructionAndIngredient(instruction, ingredient)
                .await()
                .atMost(Duration.ofSeconds(5));

        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.get(0).getCookInstructions().contains(instruction));
        assertTrue(response.get(0).getIngredients().containsKey(ingredient));
    }

    @Test
    void getRecipeBasedOnInstructionAndIngredient_givenNonExistingParameters_returnsEmptyList() {
        var instruction = "breakfast";
        var ingredient = "Unions";
        var expectedRecipe = List.of();

        doReturn(expectedRecipe)
                .when(recipeControl)
                .getRecipeOnInstructionAndIngredient(instruction, ingredient);

        var response = recipeBoundary.getRecipeBasedOnInstructionAndIngredient(instruction, ingredient)
                .await()
                .atMost(Duration.ofSeconds(5));

        assertNotNull(response);
        assertEquals(0, response.size());

        verify(recipeControl).getRecipeOnInstructionAndIngredient(instruction, ingredient);
    }

    @Test
    void delete_givenCorrectParameter_deleteRecipeReturnTrue() {
        var idParameter = 1L;
        var expected = true;

        doReturn(expected)
                .when(recipeControl)
                .deleteRecipe(idParameter);

        var response = recipeBoundary.delete(idParameter);

        verify(recipeControl).deleteRecipe(1L);

        assertNotNull(response);
        assertEquals(response.getStatus(), 201);
        assertEquals(response.getEntity(), true);
    }

    @Test
    void delete_givenInCorrectParameter_deleteRecipeReturnFalse() {
        var idParameter = -1L;
        var expected = false;

        doReturn(expected)
                .when(recipeControl)
                .deleteRecipe(idParameter);

        var response = recipeBoundary.delete(idParameter);

        verify(recipeControl).deleteRecipe(-1L);

        assertNotNull(response);
        assertEquals(response.getStatus(), 201);
        assertEquals(response.getEntity(), false);
    }
}
