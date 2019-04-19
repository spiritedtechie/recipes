import React, { Component } from "react";
import { connect } from 'react-redux';

import { get } from "../common/HttpRest"

class RecipeList extends Component {

    constructor(props) {
        super(props);
        get()("/recipes", (data) =>
            {
                props.dispatch({ type: "SET_RECIPES", data: data });
            }
        )
    }

    renderRecipes = (recipes) => {
        if (recipes) {
            return recipes.map((recipe, index) => (
                <div className="recipe"
                     key={index}>
                    <p>{recipe.name}</p>
                </div>
            ));
        }
    }

    render() {
        const recipes = this.renderRecipes(this.props.recipes)

        return (
            <div className="recipes">
                {recipes}
            </div>
        )
    }
}

function mapStateToProps(state) {
    return {
        recipes: state.recipeList.recipes
    }
}

export default connect(mapStateToProps)(RecipeList);



