package com.fskroes.control;

import com.fskroes.model.RecipeModel;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class RecipeControlTest {
    @Inject
    RecipeControl recipeControl;

    @Test
    void getAllRecipes_returnsAllRecipes() {
        var expectedRecipes = List.of(
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

        var response = recipeControl.getAllRecipes();

        assertNotNull(response);
        assertEquals(expectedRecipes, response);
    }

    @Test
    void createRecipe_givenEntityAreExist_throwsDetachedEntityPassedToPersistException() {
        var expectedRecipe = RecipeModel.builder()
                .recipeName("Potato Mash")
                .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Potatoes", "6"))
                .specificIngredients(Map.of("Potatoes", "6"))
                .isVegetarian(true)
                .numberOfServings(4)
                .cookInstructions("First place pan on the stove and ready all the vegatables")
                .cookingAppliances("Oven")
                .build();

        assertThrows(PersistentObjectException.class, () -> recipeControl.createRecipe(expectedRecipe));
    }

    @Test
    void getRecipe_findRecipeBasedOnName_returnFoundRecipe() {
        var expectedRecipe = RecipeModel.builder()
                .recipeName("Sweet Potato Mash")
                .ingredients(Map.of("Garlic", "1", "Carrot", "4", "Unions", "2", "Potatoes", "6"))
                .specificIngredients(Map.of("Potatoes", "6"))
                .isVegetarian(true)
                .numberOfServings(4)
                .cookInstructions("First place pan on the stove and ready all the vegatables")
                .cookingAppliances("Oven")
                .build();

        var response = recipeControl.getRecipe("Sweet Potato Mash");

        assertNotNull(response);
        assertEquals(expectedRecipe.getRecipeName(), response.getRecipeName());
    }

    @Test
    void getRecipeVegetarian_recipeIsNotVegetarian_returnsEmptyList() {
        var isVegetarian = false;
        var expectedRecipes = List.of();

        var response = recipeControl.getRecipeVegetarian(isVegetarian);

        assertNotNull(response);
        assertEquals(expectedRecipes, response);
    }

    @Test
    void getRecipeVegetarian_recipeIsVegetarian_returnsOnlyVegetarianRecipes() {
        var isVegetarian = true;

        var response = recipeControl.getRecipeVegetarian(isVegetarian);

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void getRecipeOnServing_findRecipeBasedServing_returnFoundRecipes() {
        var servingAmount = 4;

        var response = recipeControl.getRecipeOnServing(servingAmount);

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void getRecipeOnServing_findNonExistingServingAmount_returnEmptyList() {
        var servingAmount = -1;
        var expectedRecipes = List.of();

        var response = recipeControl.getRecipeOnServing(servingAmount);

        assertNotNull(response);
        assertEquals(expectedRecipes, response);
    }

    @Test
    void findRecipeWithIngredient_givenIngredient_returnRecipeWithGivenIngredient() {
        var ingredient = "Carrot";

        var response = recipeControl.findRecipeWithIngredient(ingredient);

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void findRecipeWithIngredient_givenNonExistingIngredient_returnEmptyList() {
        var ingredient = "Spinach";

        var response = recipeControl.findRecipeWithIngredient(ingredient);

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void searchCookingInstructions_withExistingInstruction_returnsRecipeModel() {
        var searchQuery = "stove";

        var response = recipeControl.searchCookingInstructions(searchQuery);

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void searchCookingInstructions_withNonExistingInstruction_returnsEmtyList() {
        var searchQuery = "bla";

        var response = recipeControl.searchCookingInstructions(searchQuery);

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void getReceipeOnServingAndIngredient_givenCorrectParameters_returnsCorrectRecipies() {
        var servings = 4;
        var ingredient = "Carrot";

        var response = recipeControl.getRecipeOnServingAndIngredient(servings, ingredient);

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void getReceipeOnServingAndIngredient_givenNotExistingParameters_returnsEmptyList() {
        var servings = 2;
        var ingredient = "Tomato";

        var response = recipeControl.getRecipeOnServingAndIngredient(servings, ingredient);

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void getReceipeOnServingAndIngredient_givenOneExistingAndOneNonExistingParameters_returnsEmptyList() {
        var servings = 2;
        var ingredient = "Carrot";

        var response = recipeControl.getRecipeOnServingAndIngredient(servings, ingredient);

        assertNotNull(response);
        assertEquals(0, response.size());

        servings = 4;
        ingredient = "Tomato";
        response = recipeControl.getRecipeOnServingAndIngredient(servings, ingredient);

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void getRecipeOnInstructionAndIngredient_givenCorrectParameters_returnsCorrectRecipies() {
        var instruction = "stove";
        var ingredient = "Carrot";

        var response = recipeControl.getRecipeOnInstructionAndIngredient(instruction, ingredient);

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void getRecipeOnInstructionAndIngredient_givenNonExistingParameters_returnsEmptyList() {
        var instruction = "Oven";
        var ingredient = "Carrot";

        var response = recipeControl.getRecipeOnInstructionAndIngredient(instruction, ingredient);

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void getRecipe_findNonExistingRecipeBasedOnName_throws() {
        assertThrows(NotFoundException.class, () -> recipeControl.getRecipe("Pathfinder"));
    }
}
