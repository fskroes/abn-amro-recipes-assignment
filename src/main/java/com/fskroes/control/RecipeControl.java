package com.fskroes.control;

import com.fskroes.entity.RecipeEntity;
import com.fskroes.model.RecipeModel;
import com.fskroes.service.RecipeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class RecipeControl {

    private final RecipeService recipeService;

    public RecipeControl(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public List<RecipeModel> getAllRecipes() {
        var recipes = recipeService.getAll();

        return recipes.stream().map(this::RecipeModel).toList();
    }

    public RecipeModel getRecipe(String recipeName) {

        var recipeEntity = recipeService.findByRecipeName(recipeName);

        return recipeEntity
                .map(this::RecipeModel)
                .orElseThrow(NotFoundException::new);
    }

    public RecipeModel createRecipe(RecipeModel recipeModel) {
        RecipeEntity recipeEntity = new RecipeEntity(
                1L,
                recipeModel.getRecipeName(),
                recipeModel.getIngredients().toString(),
                recipeModel.getSpecificIngredients().toString(),
                recipeModel.getIsVegetarian().toString(),
                recipeModel.getNumberOfServings().toString(),
                recipeModel.getCookInstructions(),
                recipeModel.getCookingAppliances());
        var createdRecipe = recipeService.create(recipeEntity);

        return RecipeModel(createdRecipe);
    }

    private RecipeModel RecipeModel(@NotNull RecipeEntity recipeEntity) {

        return RecipeModel.builder()
                .recipeName(recipeEntity.recipeName)
                .ingredients(Arrays.stream(recipeEntity.ingredients.split(",")).toList())
                .specificIngredients(Arrays.stream(recipeEntity.specificIngredients.split(",")).toList())
                .isVegetarian(Boolean.valueOf(recipeEntity.isVegetarian))
                .numberOfServings(Integer.valueOf(recipeEntity.numberOfServings))
                .cookInstructions(recipeEntity.cookInstructions)
                .cookingAppliances(recipeEntity.cookingAppliances)
                .build();
    }
}
