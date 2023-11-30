package com.fskroes.stub;

import com.fskroes.entity.RecipeEntity;

import java.util.List;

public class RecipeModelMock {
    public static List<RecipeEntity> getStubbedRecipies() {

        var recipe1 = new RecipeEntity();
        recipe1.setRecipeName("Potato Mash");
        recipe1.setIngredients("Garlic:1,Carrot:4,Unions:2,Potatoes:6'");
        recipe1.setSpecificIngredients("Potatoes:6");
        recipe1.setIsVegetarian("true");
        recipe1.setNumberOfServings("4");
        recipe1.setCookInstructions("First place pan on the stove and ready all the vegatables");
        recipe1.setCookingAppliances("Oven");

        var recipe2 = new RecipeEntity();
        recipe2.setRecipeName("Sweet Potato Mash");
        recipe2.setIngredients("Garlic:1,Carrot:4,Unions:2,Potatoes:6'");
        recipe2.setSpecificIngredients("Sweet potatoes:6");
        recipe2.setIsVegetarian("true");
        recipe2.setNumberOfServings("4");
        recipe2.setCookInstructions("First place pan on the stove and ready all the vegatables");
        recipe2.setCookingAppliances("Oven");

        return List.of(recipe1, recipe2);
    }
}
