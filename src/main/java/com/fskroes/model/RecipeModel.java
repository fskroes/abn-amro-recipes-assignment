package com.fskroes.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Data
@Getter
public class RecipeModel {
    private String recipeName;

    // 1. Garlic 2. Carrot 3. Unions 4. Potatoes
    private List<String> ingredients;

    // Sweet potato
    private List<String> specificIngredients;

    // True
    private Boolean isVegetarian;

    // 4
    private Integer numberOfServings;

    // First place pan on the stove and ready all the vegatables
    private String cookInstructions;

    // Oven
    private String cookingAppliances;
}
