package com.fskroes.boundary;


import com.fskroes.control.RecipeControl;
import com.fskroes.model.RecipeModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeBoundary {

    @Inject
    RecipeControl recipeControl;

    @GET
    @Path("/recipe")
    public Uni<List<RecipeModel>> getRecipes() {
        return Uni
                .createFrom()
                .item(recipeControl.getAllRecipes());
    }

    @GET
    @Path("/recipe")
    public Uni<RecipeModel> getRecipe(@QueryParam("name") String recipeName) {
        var specificRecipe = recipeControl.getRecipe(recipeName);

        return Uni
                .createFrom()
                .item(specificRecipe);
    }

    @POST
    @Path("/recipe")
    public Response createRecipe(RecipeModel recipeModel) {
        var savedCustomer = recipeControl.createRecipe(recipeModel);
        return Response.status(Response.Status.CREATED).entity(savedCustomer).build();
    }

    @DELETE
    @Path("recipe/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response
                .status(Response.Status.CREATED)
                .entity(recipeControl.deleteRecipe(id))
                .build();
    }

    // ------------- EXAMPLE FROM ASSIGNMENT -------------
    @GET
    @Path("/vegetarian")
    public Uni<List<RecipeModel>> getRecipeIsVegetarian(@QueryParam("") Boolean isVegetarian) {
        var vegetarianRecipies = recipeControl.getRecipeVegetarian(isVegetarian);

        return Uni
                .createFrom()
                .item(vegetarianRecipies);
    }

    @GET
    @Path("/recipe")
    public Uni<List<RecipeModel>> getRecipeBasedOnServingAndIngredient(
            @QueryParam("") Integer servings,
            @QueryParam("") String ingredient
    ) {
        var recipies = recipeControl.getRecipeOnServingAndIngredient(servings, ingredient);

        return Uni
                .createFrom()
                .item(recipies);
    }

    @GET
    @Path("/recipe")
    public Uni<List<RecipeModel>> getRecipeBasedOnInstructionAndIngredient(
            @QueryParam("") String instruction,
            @QueryParam("") String ingredient
    ) {
        var recipies = recipeControl.getRecipeOnInstructionAndIngredient(instruction, ingredient);

        return Uni
                .createFrom()
                .item(recipies);
    }
}