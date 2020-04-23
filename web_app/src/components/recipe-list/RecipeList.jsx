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

    renderImagePreview = (image) => {
        if (image) {
          return (<img className="image" alt="image" src={image} />);
        } else {
          return (<div className="text">Image unavailable</div>);
        }
    }

    renderRecipes = (recipes) => {
        if (recipes) {
            return recipes.map((recipe, index) => {
                const image = this.renderImagePreview(recipe.imageUrl)
                return (
                    <div className="recipe"
                        key={index}>
                        <div className="picture">{image}</div>
                        <div className="name">{recipe.name}</div>
                    </div>
                )
            });
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



