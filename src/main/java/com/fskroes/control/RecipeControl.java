package com.fskroes.control;

import com.fskroes.entity.RecipeEntity;
import com.fskroes.model.RecipeModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class RecipeControl {

    @Inject
    private RecipeRepository recipeRepository;

    public List<RecipeModel> getAllRecipes() {
        var recipes = recipeRepository.findAllRecipes();

        return recipes.stream().map(this::RecipeModel).toList();
    }

    public List<RecipeModel> getRecipeOnServing(Integer serving) {
        var recipes = recipeRepository.findServings(String.valueOf(serving));
        return recipes
                .stream()
                .map(this::RecipeModel)
                .toList();
    }

    public List<RecipeModel> getRecipeVegetarian(Boolean isVegetarian) {
        return recipeRepository.findIsVegetarian(String.valueOf(isVegetarian))
                .stream()
                .map(this::RecipeModel)
                .toList();
    }

    public RecipeModel getRecipe(String recipeName) {

        var recipeEntity = recipeRepository.findRecipe(recipeName);

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
        var createdRecipe = recipeRepository.create(recipeEntity);

        return RecipeModel(createdRecipe);
    }

    private RecipeModel RecipeModel(@NotNull RecipeEntity recipeEntity) {

        return RecipeModel.builder()
                .recipeName(recipeEntity.getRecipeName())
                .ingredients(Arrays.stream(recipeEntity.getIngredients().split(",")).toList())
                .specificIngredients(Arrays.stream(recipeEntity.getSpecificIngredients().split(",")).toList())
                .isVegetarian(Boolean.valueOf(recipeEntity.getIsVegetarian()))
                .numberOfServings(Integer.valueOf(recipeEntity.getNumberOfServings()))
                .cookInstructions(recipeEntity.getCookInstructions())
                .cookingAppliances(recipeEntity.getCookingAppliances())
                .build();
    }
}
