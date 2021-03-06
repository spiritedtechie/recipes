import produce from "immer"

const initialState = {
    recipe: {
        name: "",
        ingredients: [],
        instructions: {
            steps: []
        }
    },
    ingredient_quantity_units: [],
    ingredient_preparation_methods: [],
    ingredient_preparation_styles: [],
    saving_flag: false
};

const reducer = (state=initialState, action) => {
    return produce(state, draft => {
        // eslint-disable-next-line
        switch(action.type) {
            case "SET_PREPARATION_METHODS_LOOKUPS":
                draft.ingredient_preparation_methods = action.data
                return
            case "SET_PREPARATION_STYLES_LOOKUPS":
                draft.ingredient_preparation_styles = action.data
                return
            case "SET_QUANTITY_UNITS_LOOKUP":
                draft.ingredient_quantity_units = action.data
                return
            case "CHANGE_NAME":
                draft.recipe.name = action.value
                return
            case "CHANGE_IMAGE":
                draft.recipe.rawImageBase64 = action.value
                return
            case "DELETE_INGREDIENT":
                draft.recipe.ingredients.splice(action.index, 1)
                return
            case "ADD_NEW_INGREDIENT":
                draft.recipe.ingredients.push({
                    name: "",
                    optional: false,
                    quantity: {
                      value: 1,
                      unit: "GRAMS"
                    }
                })
                return
            case "SET_INGREDIENT_NAME":
                draft.recipe.ingredients[action.index].name = action.value
                return
            case "SET_INGREDIENT_QUANTITY":
                draft.recipe.ingredients[action.index].quantity.value = action.value
                return
            case "SET_INGREDIENT_UNIT":
                draft.recipe.ingredients[action.index].quantity.unit = action.value
                return
            case "SET_INGREDIENT_OPTIONAL":
                draft.recipe.ingredients[action.index].optional = action.value
                return
            case "SET_DEFAULT_PREPARATION":
                if (!draft.recipe.ingredients[action.index].preparation) {
                    draft.recipe.ingredients[action.index].preparation =  {
                        style: "FINE",
                        method: "GRATE"
                    }
                }
                return
            case "SET_PREPARATION_METHOD":
                if (!draft.recipe.ingredients[action.index].preparation) {
                    draft.recipe.ingredients[action.index].preparation =  {}
                }
                draft.recipe.ingredients[action.index].preparation.method = action.value
                return
            case "SET_PREPARATION_STYLE":
                if (!draft.recipe.ingredients[action.index].preparation) {
                    draft.recipe.ingredients[action.index].preparation =  {}
                }
                draft.recipe.ingredients[action.index].preparation.style = action.value
                return
            case "DELETE_INSTRUCTION":
                draft.recipe.instructions.steps.splice(action.index, 1)
                return
            case "ADD_NEW_INSTRUCTION_STEP":
                draft.recipe.instructions.steps.push({
                    description: ""
                })
                return
            case "SET_INSTRUCTION":
                draft.recipe.instructions.steps[action.index] = {
                    description: action.value
                }
                return
            case "SAVING_RECIPE":
                draft.saving_flag = true
                return
            case "RECIPE_SAVED":
                draft.recipe = action.data
                draft.saving_flag = false
                return
            case "RECIPE_DELETED":
                draft.recipe = initialState.recipe
                return
        }
    })
}

export default reducer