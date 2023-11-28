package com.fskroes.control;

import com.fskroes.model.RecipeModel;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.Test;

import java.util.List;

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
                        .ingredients(List.of("Garlic:1", "Carrot:4", "Unions:2", "Potatoes:6"))
                        .specificIngredients(List.of("Potatoes"))
                        .isVegetarian(true)
                        .numberOfServings(4)
                        .cookInstructions("First place pan on the stove and ready all the vegatables")
                        .cookingAppliances("Oven")
                        .build(),
                RecipeModel.builder()
                        .recipeName("Sweet Potato Mash")
                        .ingredients(List.of("Garlic:1", "Carrot:4", "Unions:2", "Sweet potatoes:6"))
                        .specificIngredients(List.of("Sweet potatoes"))
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
                .ingredients(List.of("Garlic:1", "Carrot:4", "Unions:2", "Potatoes:6"))
                .specificIngredients(List.of("Potatoes"))
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
                .ingredients(List.of("Garlic:1", "Carrot:4", "Unions:2", "Sweet potatoes:6"))
                .specificIngredients(List.of("Sweet potatoes"))
                .isVegetarian(true)
                .numberOfServings(4)
                .cookInstructions("First place pan on the stove and ready all the vegatables")
                .cookingAppliances("Oven")
                .build();

        var response = recipeControl.getRecipe("Sweet Potato Mash");

        assertNotNull(response);
        assertEquals(expectedRecipe, response);
    }

    @Test
    void getRecipe_findNonExistingRecipeBasedOnName_throws() {
        assertThrows(NotFoundException.class, () -> recipeControl.getRecipe("Pathfinder"));
    }
}
