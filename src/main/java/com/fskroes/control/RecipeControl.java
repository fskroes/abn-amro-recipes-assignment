package com.fskroes.control;

import com.fskroes.entity.RecipeEntity;
import com.fskroes.model.RecipeModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

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
        var recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName(recipeModel.getRecipeName());
        recipeEntity.setIngredients(recipeModel.getIngredients().toString());
        recipeEntity.setSpecificIngredients(recipeModel.getSpecificIngredients().toString());
        recipeEntity.setIsVegetarian(recipeModel.getIsVegetarian().toString().toLowerCase());
        recipeEntity.setNumberOfServings(recipeModel.getNumberOfServings().toString());
        recipeEntity.setCookInstructions(recipeModel.getCookInstructions());
        recipeEntity.setCookingAppliances(recipeModel.getCookingAppliances());

        var createdRecipe = recipeRepository.create(recipeEntity);

        return RecipeModel(createdRecipe);
    }

    public Boolean deleteRecipe(Long id) {
        return recipeRepository.remove(id);
    }

    public List<RecipeModel> findRecipeWithIngredient(String ingredient) {
        var allRecipes = getAllRecipes();
        return allRecipes
                .stream()
                .filter(model -> model.getIngredients().containsKey(ingredient))
                .toList();
    }

    public List<RecipeModel> searchCookingInstructions(String searchQuery) {
        var recipes = recipeRepository.searchInstruction(searchQuery);
        return recipes
                .stream()
                .map(this::RecipeModel)
                .toList();
    }

    public List<RecipeModel> getRecipeOnServingAndIngredient(Integer servings, String ingredient) {
        var allRecipes = getAllRecipes();
        return allRecipes
                .stream()
                .filter(model ->
                        model.getIngredients().containsKey(ingredient) && model.getNumberOfServings().equals(servings)
                )
                .toList();
    }

    public List<RecipeModel> getRecipeOnInstructionAndIngredient(String instruction, String ingredient) {
        var allRecipes = getAllRecipes();
        return allRecipes
                .stream()
                .filter(model ->
                        model.getIngredients().containsKey(ingredient) && model.getCookInstructions().contains(instruction)
                )
                .toList();
    }

    private RecipeModel RecipeModel(@NotNull RecipeEntity recipeEntity) {

        return RecipeModel.builder()
                .recipeName(recipeEntity.getRecipeName())
                .ingredients(ingredientsToMap(recipeEntity.getIngredients()))
                .specificIngredients(ingredientsToMap(recipeEntity.getSpecificIngredients()))
                .isVegetarian(Boolean.valueOf(recipeEntity.getIsVegetarian()))
                .numberOfServings(Integer.valueOf(recipeEntity.getNumberOfServings()))
                .cookInstructions(recipeEntity.getCookInstructions())
                .cookingAppliances(recipeEntity.getCookingAppliances())
                .build();
    }

    private Map<String, String> ingredientsToMap(String ingredients) {
        var ingredientsWithAmount = Arrays.stream(ingredients.split(",")).toList();
        return ingredientsWithAmount
                .stream()
                .map(ingredientString -> ingredientString
                        .split(":")
                )
                .collect(toMap(arr -> arr[0], arr -> arr[1], (v1, v2) -> v1));
    }
}
