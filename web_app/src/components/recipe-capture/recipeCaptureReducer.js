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
    ingredient_preparation_styles: []
};

const reducer = (state = initialState, action) => {
    console.log(action)
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
            case "RESET_RECIPE":
                draft.recipe = initialState.recipe
                return
            case "CHANGE_NAME":
                draft.recipe.name = action.value
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
            case "ADD_NEW_INSTRUCTION_STEP":
                draft.recipe.instructions.steps.push("")
                return
            case "SET_INSTRUCTION":
                draft.recipe.instructions.steps[action.index] = action.value
                return

        }
    })
}

export default reducer