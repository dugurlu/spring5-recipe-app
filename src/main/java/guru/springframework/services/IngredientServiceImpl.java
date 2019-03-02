package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndId(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        if (recipe == null) {
            // TODO error handling
            log.error("Recipe " + recipeId + "not found");
        }

        IngredientCommand ingredientCommand = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst().orElse(null);

        if (ingredientCommand == null) {
            // TODO error handling
            log.error("Ingredient " + ingredientId + " not found");
        }

        return ingredientCommand;
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Recipe recipe = recipeRepository.findById(command.getRecipeId()).orElse(null);
        if(recipe == null) {
            // TODO error handling
            log.error("Recipe not found for ID: " + command.getRecipeId());
            return new IngredientCommand();
        }

        Ingredient ingredient = recipe.getIngredients().stream()
                .filter(ingr -> ingr.getId().equals(command.getId()))
                .findFirst().orElse(null);
        if(ingredient == null) {
            Ingredient ingredient1 = ingredientCommandToIngredient.convert(command);
            ingredient1.setRecipe(recipe);
            recipe.addIngredient(ingredient1);

        } else {
            ingredient.setDescription(command.getDescription());
            ingredient.setAmount(command.getAmount());
            ingredient.setUom(unitOfMeasureRepository
                    .findById(command.getUnitOfMeasure().getId())
                    .orElseThrow(() -> new RuntimeException("UOM not found"))); // TODO error handling
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        // TODO check for fail
        Ingredient result = savedRecipe.getIngredients().stream()
                .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                .findFirst()
                // not 100% safe - best guess
                .orElse(savedRecipe.getIngredients().stream()
                        .filter(recipeIngredient -> recipeIngredient.getDescription() == null || recipeIngredient.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredient -> recipeIngredient.getAmount() == null || recipeIngredient.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredient -> recipeIngredient.getUom() == null || recipeIngredient.getUom().getId().equals(command.getUnitOfMeasure().getId())).findFirst().orElse(null));

        return ingredientToIngredientCommand.convert(result);

    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);

        if(recipe == null) {
            log.debug("Recipe ID not found: " + recipeId);
        } else {
            Ingredient ingredient = recipe.getIngredients().stream()
                    .filter(ing -> ing.getId().equals(ingredientId))
                    .findFirst().orElse(null);
            if(ingredient == null) {
                log.debug("Ingredient ID not found: " + ingredientId);
            } else {
                ingredient.setRecipe(null);
                recipe.getIngredients().remove(ingredient);
                recipeRepository.save(recipe);
            }
        }

    }
}
