package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;
    private RecipeRepository recipeRepository;

    public DataLoader(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
    }

    public List<Recipe> getRecipes (){
        List<Recipe> recipes = new ArrayList<>(2);

        Category mexican = categoryRepository.findByDescription("Mexican").orElse(null);
        Category american = categoryRepository.findByDescription("American").orElse(null);

        UnitOfMeasure teaspoon = unitOfMeasureRepository.findByDescription("Teaspoon").orElse(null);
        UnitOfMeasure tablespoon = unitOfMeasureRepository.findByDescription("Tablespoon").orElse(null);
        UnitOfMeasure dash = unitOfMeasureRepository.findByDescription("Dash").orElse(null);
        UnitOfMeasure clove = unitOfMeasureRepository.findByDescription("Clove").orElse(null);

        Recipe guacamoleRecipe = new Recipe();
        guacamoleRecipe.setPrepTime(10);
        guacamoleRecipe.setCookTime(0);
        guacamoleRecipe.setServings(4);
        guacamoleRecipe.setDescription("Guacamole");
        guacamoleRecipe.setSource("Simply Recipes");
        guacamoleRecipe.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamoleRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n"+
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.");

        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes("MAKING GUACAMOLE IS EASY\n" +
                "All you really need to make guacamole is ripe avocados and salt. After that, a little lime or lemon juice—a splash of acidity—will help to balance the richness of the avocado. Then if you want, add chopped cilantro, chiles, onion, and/or tomato.\n" +
                "\n" +
                "Once you have basic guacamole down, feel free to experiment with variations including strawberries, peaches, pineapple, mangoes, even watermelon. You can get creative with homemade guacamole!\n" +
                "\n" +
                "GUACAMOLE TIP: USE RIPE AVOCADOS\n" +
                "The trick to making perfect guacamole is using ripe avocados that are just the right amount of ripeness. Not ripe enough and the avocado will be hard and tasteless. Too ripe and the taste will be off.\n" +
                "\n" +
                "Check for ripeness by gently pressing the outside of the avocado. If there is no give, the avocado is not ripe yet and will not taste good. If there is a little give, the avocado is ripe. If there is a lot of give, the avocado may be past ripe and not good. In this case, taste test first before using.");
        guacamoleRecipe.setNotes(guacamoleNotes);

        guacamoleRecipe.setDifficulty(Difficulty.EASY);

        guacamoleRecipe.getCategories().add(mexican);
        guacamoleRecipe.getCategories().add(american);

        guacamoleRecipe.addIngredient(new Ingredient("ripe avocado", BigDecimal.valueOf(2), null))
                .addIngredient(new Ingredient("Kosher salt", BigDecimal.valueOf(0.5), teaspoon))
                .addIngredient(new Ingredient("fresh lime juice or lemon juice", BigDecimal.valueOf(1), tablespoon))
                .addIngredient(new Ingredient("minced red onion", BigDecimal.valueOf(2), tablespoon))
                .addIngredient(new Ingredient("serrano chiles", BigDecimal.valueOf(2), null))
                .addIngredient(new Ingredient("cilantro", BigDecimal.valueOf(2), tablespoon))
                .addIngredient(new Ingredient("freshly grated black pepper", BigDecimal.valueOf(1), dash))
                .addIngredient(new Ingredient("ripe tomato", BigDecimal.valueOf(0.5), null));

        recipes.add(guacamoleRecipe);
        log.debug("Guacamole Recipe loaded");

        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setCookTime(15);
        tacosRecipe.setServings(6);
        tacosRecipe.setDescription("Spicy Grilled Chicken Tacos");
        tacosRecipe.setSource("Simply Recipes");
        tacosRecipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacosRecipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");

        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                "\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "\n" +
                "\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!");
        tacosRecipe.setNotes(tacoNotes);

        tacosRecipe.setDifficulty(Difficulty.MODERATE);

        tacosRecipe.getCategories().add(mexican);
        tacosRecipe.getCategories().add(american);

        tacosRecipe.addIngredient(new Ingredient("ancho chili powder", BigDecimal.valueOf(2), tablespoon))
                .addIngredient(new Ingredient("dried oregano", BigDecimal.valueOf(1), teaspoon))
                .addIngredient(new Ingredient("dried cumin", BigDecimal.valueOf(1), teaspoon))
                .addIngredient(new Ingredient("sugar", BigDecimal.valueOf(1), teaspoon))
                .addIngredient(new Ingredient("salt", BigDecimal.valueOf(0.5), teaspoon))
                .addIngredient(new Ingredient("garlic, finely chopped", BigDecimal.valueOf(1), clove))
                .addIngredient(new Ingredient("finely grated orange zest", BigDecimal.valueOf(1), tablespoon))
                .addIngredient(new Ingredient("fresh-squeezed orange juice", BigDecimal.valueOf(3), tablespoon))
                .addIngredient(new Ingredient("olive oil", BigDecimal.valueOf(2), tablespoon))
                .addIngredient(new Ingredient("skinless, bnoeless chicken thighs", BigDecimal.valueOf(6), null));

        recipes.add(tacosRecipe);
        log.debug("Tacos Recipe loaded");

        return recipes;
    }
}
