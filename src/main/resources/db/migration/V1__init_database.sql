create sequence recipes_seq start with 1 increment by 50;

create table recipes
(
    id    bigint DEFAULT nextval('recipes_seq') not null,
    recipeName  varchar                                 not null,
    ingredients varchar                                 not null,
    specificIngredients varchar                         not null,
    isVegetarian varchar                                not null,
    numberOfServings varchar                            not null,
    cookInstructions varchar                            not null,
    cookingAppliances varchar                           not null,
    primary key (id)
);

insert into recipes(recipeName, ingredients, specificIngredients, isVegetarian, numberOfServings, cookInstructions, cookingAppliances)
values ('Potato Mash', 'Garlic:1,Carrot:4,Unions:2,Potatoes:6', 'Potatoes', 'true', '4', 'First place pan on the stove and ready all the vegatables', 'Oven'),
       ('Sweet Potato Mash', 'Garlic:1,Carrot:4,Unions:2,Sweet potatoes:6', 'Sweet potatoes', 'true', '4', 'First place pan on the stove and ready all the vegatables', 'Oven');