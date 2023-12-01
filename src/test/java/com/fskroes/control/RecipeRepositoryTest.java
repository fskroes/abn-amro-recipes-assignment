package com.fskroes.control;

import com.fskroes.entity.RecipeEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class RecipeRepositoryTest {
    @Inject
    RecipeRepository recipeRepository;

    @Test
    void create_ok() {
        var recipe = new RecipeEntity();
        recipe.setRecipeName("The BEST Recipe");
        recipe.setIngredients("");
        recipe.setSpecificIngredients("");
        recipe.setNumberOfServings("1");
        recipe.setIsVegetarian("true");
        recipe.setCookingAppliances("");
        recipe.setCookInstructions("Be awesome");

        var result = recipeRepository.create(recipe);
        assertEquals(recipe.getRecipeName(), result.getRecipeName());
    }

    @Test
    void remove_ok() {
        var result = recipeRepository.remove(1L);
        assertEquals(true, result);
    }

    @Test
    void findAllRecipes_ok() {
        var result = recipeRepository.findAllRecipes();
        assertEquals(2, result.size());
    }

    @Test
    void findRecipe_ok() {
        var result = recipeRepository.findRecipe("Potato Mash");
        assertEquals("Potato Mash", result.get().getRecipeName());
    }

    @Test
    void findIsVegetarian_ok() {
        var result = recipeRepository.findIsVegetarian("true");
        assertEquals("true", result.get(0).getIsVegetarian());
    }

    @Test
    void findServings_ok() {
        var result = recipeRepository.findServings("4");
        assertEquals("4", result.get(0).getNumberOfServings());
    }

    @Test
    void searchInstruction_ok() {
        var result = recipeRepository.searchInstruction("stove");
        assertEquals(true, result.get(0).getCookInstructions().contains("stove"));
    }

}
