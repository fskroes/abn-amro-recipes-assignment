package com.fskroes.boundary;


import com.fskroes.control.RecipeControl;
import com.fskroes.model.RecipeModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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
    @Path("/recipes")
    public Uni<List<RecipeModel>> getRecipes() {

        return Uni
                .createFrom()
                .item(recipeControl.getAllRecipes());
    }

    @GET
    @Path("/recipe")
    public Uni<RecipeModel> getRecipe(@QueryParam("") String recipeName) {
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
}