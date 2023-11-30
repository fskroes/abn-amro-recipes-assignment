package com.fskroes.control;

import com.fskroes.model.RecipeModel;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.fskroes.stub.RecipeModelMock.getStubbedRecipies;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@QuarkusTest
public class RecipeControlTest {
    @Inject
    RecipeControl recipeControl;

    @InjectMock
    RecipeRepository recipeRepository;

    @BeforeEach
    void restoreDatabase() {
        if (recipeControl.getAllRecipes().size() == 1) {
            System.out.println("SIZE IS 1");
        }
    }

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

        doReturn(getStubbedRecipies())
                .when(recipeRepository)
                .findAllRecipes();

        var response = recipeControl.getAllRecipes();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(expectedRecipes.get(0).getRecipeName(), response.get(0).getRecipeName());
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

        doThrow(PersistentObjectException.class)
                .when(recipeRepository)
                .create(any());

        assertThrows(PersistentObjectException.class, () -> recipeControl.createRecipe(expectedRecipe));
    }

    @Test
    void deleteRecipe_givenCorrectParameter_returnTrueOfsuccessfullDeletion() {
        var idToDelete = 1L;
        var expectedValue = true;

        doReturn(true)
                .when(recipeRepository)
                .remove(idToDelete);

        var response = recipeControl.deleteRecipe(idToDelete);

        assertNotNull(response);
        assertEquals(expectedValue, response);
    }

    @Test
    void deleteRecipe_givenInvalidParameter_returnFalseOfNoDeletion() {
        var idToDelete = -1L;
        var expectedValue = false;

        var response = recipeControl.deleteRecipe(idToDelete);

        assertNotNull(response);
        assertEquals(expectedValue, response);
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

        doReturn(Optional.of(getStubbedRecipies().get(1)))
                .when(recipeRepository)
                .findRecipe("Sweet Potato Mash");

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

        doReturn(getStubbedRecipies())
                .when(recipeRepository)
                .findIsVegetarian("true");

        var response = recipeControl.getRecipeVegetarian(isVegetarian);

        assertNotNull(response);
        assertEquals(true, response.get(0).getIsVegetarian());
        assertEquals(2, response.size());
    }

    @Test
    void getRecipeOnServing_findRecipeBasedServing_returnFoundRecipes() {
        var servingAmount = 4;

        doReturn(getStubbedRecipies())
                .when(recipeRepository)
                .findServings("4");

        var response = recipeControl.getRecipeOnServing(servingAmount);

        assertNotNull(response);
        assertEquals(servingAmount, response.get(0).getNumberOfServings());
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

        doReturn(getStubbedRecipies())
                .when(recipeRepository)
                .findAllRecipes();

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

        doReturn(getStubbedRecipies())
                .when(recipeRepository)
                .searchInstruction(searchQuery);

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

        doReturn(getStubbedRecipies())
                .when(recipeRepository)
                .findAllRecipes();

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

        doReturn(getStubbedRecipies())
                .when(recipeRepository)
                .findAllRecipes();

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
