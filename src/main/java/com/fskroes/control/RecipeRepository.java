package com.fskroes.control;


import com.fskroes.entity.RecipeEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class RecipeRepository implements PanacheRepository<RecipeEntity> {

    public RecipeEntity create(RecipeEntity recipe) {
        PanacheEntityBase.persist(recipe);
        return findById(recipe.id);
    }

    public Boolean remove(Long id) {
        return deleteById(id);
    }

    public List<RecipeEntity> findAllRecipes() {
        return findAll()
                .stream()
                .toList();
    }

    public Optional<RecipeEntity> findRecipe(String recipeName) {
        return find("recipeName", recipeName)
                .firstResultOptional();
    }

    public List<RecipeEntity> findIsVegetarian(String isVegetarian){
        return list("isVegetarian", isVegetarian);
    }

    public List<RecipeEntity> findServings(String servings) {
        return list("numberOfServings", servings);
    }

    public List<RecipeEntity> searchInstruction(String searchInstruction) {
        var searchInput = "%" + searchInstruction + "%";
        return list("cookInstructions like ?1", searchInput);
    }

//    public List<RecipeEntity> findRecipeBasedOnIngredient(String ingredient) {
//        return list("ingredients", ingredient);
//    }
}
