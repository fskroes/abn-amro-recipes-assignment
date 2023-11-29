package com.fskroes.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Data
@Getter
public class RecipeModel {
    private String recipeName;

    // 1. Garlic 2. Carrot 3. Unions 4. Potatoes
    private Map<String, String> ingredients;

    // Sweet potato
    private Map<String, String> specificIngredients;

    // True
    private Boolean isVegetarian;

    // 4
    private Integer numberOfServings;

    // First place pan on the stove and ready all the vegatables
    private String cookInstructions;

    // Oven
    private String cookingAppliances;
}
