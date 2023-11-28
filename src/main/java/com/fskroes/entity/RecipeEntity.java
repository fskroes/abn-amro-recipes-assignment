package com.fskroes.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "recipes")
public class RecipeEntity extends PanacheEntity {

    @Column(nullable = false)
    public String recipeName;

    // Garlic:1,Carrot:4,Unions:2,Potatoes:6
    // All ingredients are in one String with delimiter
    @Column(nullable = false)
    public String ingredients;

    // Sweet potato
    // All ingredients are in one String with delimiter
    @Column(nullable = false)
    public String specificIngredients;

    // True
    @Column(nullable = false)
    public String isVegetarian;

    // 4
    @Column(nullable = false)
    public String numberOfServings;

    // First place pan on the stove and ready all the vegatables
    @Column(nullable = false)
    public String cookInstructions;

    // Oven
    @Column(nullable = false)
    public String cookingAppliances;

    public RecipeEntity() {}

    public RecipeEntity(
            Long id,
            String recipeName,
            String ingredients,
            String specificIngredients,
            String isVegetarian,
            String numberOfServings,
            String cookInstructions,
            String cookingAppliances) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.specificIngredients = specificIngredients;
        this.isVegetarian = isVegetarian;
        this.numberOfServings = numberOfServings;
        this.cookInstructions = cookInstructions;
        this.cookingAppliances = cookingAppliances;
    }

}