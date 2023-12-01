package com.fskroes;

import com.fskroes.control.RecipeControl;
import com.fskroes.model.RecipeModel;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/recipeIntegration")
public class RecipeResource {

    private final RecipeControl recipeControl;

    public RecipeResource(RecipeControl recipeControl) {
        this.recipeControl = recipeControl;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<RecipeModel>> recipe() {
        return Uni.createFrom().item(recipeControl.getAllRecipes());
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RecipeModel> getRecipeByName(@PathParam("name") String name) {
        return Uni.createFrom().item(recipeControl.getRecipe(name));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRecipe(RecipeModel model) {
        var savedCustomer = recipeControl.createRecipe(model);
        return Response.status(Response.Status.CREATED).entity(savedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRecipe(@PathParam("id") String id) {
        return Response
                .status(Response.Status.CREATED)
                .entity(recipeControl.deleteRecipe(Long.valueOf(id)))
                .build();
    }
}
