package com.fskroes.service;

import com.fskroes.entity.RecipeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class RecipeService {

    public List<RecipeEntity> getAll() {
        return RecipeEntity.listAll();
    }

    public RecipeEntity create(RecipeEntity recipe) {
        recipe.persist();
        return recipe;
    }

    public Optional<RecipeEntity> findByRecipeName(String recipeName) {
        return RecipeEntity
                .find("recipeName", recipeName)
                .firstResultOptional();
    }

    public Optional<RecipeEntity> findRecipeByNumberOfServings(Integer numberOfServings) {
        return RecipeEntity
                .find("numberOfServings", String.valueOf(numberOfServings))
                .firstResultOptional();
    }

}
