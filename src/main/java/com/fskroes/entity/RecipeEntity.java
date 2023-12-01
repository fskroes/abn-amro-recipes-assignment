package com.fskroes.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "recipes")
public class RecipeEntity extends PanacheEntity {

    @Column(nullable = false)
    private String recipeName;

    // Garlic:1,Carrot:4,Unions:2,Potatoes:6
    // All ingredients are in one String with delimiter
    @Column(nullable = false)
    private String ingredients;

    // Sweet potato
    // All ingredients are in one String with delimiter
    @Column(nullable = false)
    private String specificIngredients;

    // True
    @Column(nullable = false)
    private String isVegetarian;

    // 4
    @Column(nullable = false)
    private String numberOfServings;

    // First place pan on the stove and ready all the vegatables
    @Column(nullable = false)
    private String cookInstructions;

    // Oven
    @Column(nullable = false)
    private String cookingAppliances;

    public RecipeEntity() {}
}