import produce from "immer"

const initialState = {
    recipes: []
}

const reducer = (state, action) => {
    return produce(state=initialState, draft => {
        // eslint-disable-next-line
        switch(action.type) {
            case "SET_RECIPES":
                draft.recipes = action.data
                return
        }
    })

}

export default reducer