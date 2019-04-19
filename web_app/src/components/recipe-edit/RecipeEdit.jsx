import React, { Component } from "react";
import { connect } from 'react-redux';

import Ingredient from "./Ingredient";
import InstructionStep from "./InstructionStep";

const recipeApiUrl = "http://localhost:8080"

class RecipeEdit extends Component {
    constructor(props) {
        super(props);
        get(recipeApiUrl + "/ingredient/preparation/methods", (data) => {
            props.dispatch({ type: "SET_PREPARATION_METHODS_LOOKUPS", data: data });
        })
        get(recipeApiUrl + "/ingredient/preparation/styles", (data) => {
            props.dispatch({ type: "SET_PREPARATION_STYLES_LOOKUPS", data: data });
        })
        get(recipeApiUrl + "/ingredient/quantity/units", (data) => {
            props.dispatch({ type: "SET_QUANTITY_UNITS_LOOKUP", data: data });
        })

        this.handleSave = this.handleSave.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleAddIngredient = this.handleAddIngredient.bind(this);
        this.handleAddInstructionStep = this.handleAddInstructionStep.bind(this);
    }

    handleSave(e) {
        e.preventDefault();

        this.props.dispatch({ type: "SAVING_RECIPE"})

        const recipe = this.props.recipe;
        const recipeJson = JSON.stringify(recipe)

        if (recipe.id) {
            save("PUT",
                 recipeApiUrl + "/recipes/" + recipe.id,
                 recipeJson,
                 (data) => {
                    this.props.dispatch({ type: "RECIPE_SAVED", data: data });
                 }
            )
        } else {
            save("POST",
                 recipeApiUrl + "/recipes",
                 recipeJson,
                 (data) => {
                    this.props.dispatch({ type: "RECIPE_SAVED", data: data });
                 }
            )
        }
    }

    handleDelete(e) {
        e.preventDefault();

        if (this.props.recipe.id) {
            del(recipeApiUrl + "/recipes/" + this.props.recipe.id,
                   data => {
                      this.props.dispatch({ type: "RECIPE_DELETED" });
                   }
            )
        } else {
            this.props.dispatch({ type: "RECIPE_DELETED" });
        }
    }

    handleRecipeName(e) {
        this.props.dispatch({ type: "CHANGE_NAME", value: e.target.value });
    }

    handleImageChange(e) {
        const reader = new FileReader();
        const file = e.target.files[0];
        reader.onloadend = () => {
           this.props.dispatch({ type: "CHANGE_IMAGE", value: reader.result });
        }
        reader.readAsDataURL(file)
    }

    handleAddIngredient(e) {
        e.preventDefault();
        this.props.dispatch({ type: "ADD_NEW_INGREDIENT" })
    }

    handleAddInstructionStep(e) {
        e.preventDefault();
        this.props.dispatch({ type: "ADD_NEW_INSTRUCTION_STEP" })
    }

    renderImagePreview = (image) => {
        if (image) {
          return (<img id="preview-image" alt="recipe" src={image} />);
        } else {
          return (<div id="preview-text">Please select an Image for Preview</div>);
        }
    }

    renderIngredients = (ingredients) => {
        if (ingredients.length > 0) {
            return ingredients.map((ingredient, index) => (
                <Ingredient key={index}
                            index={index}
                            name={ingredient.name}
                            optional={ingredient.optional}
                            quantity_unit={ingredient.quantity.unit}
                            quantity_value={ingredient.quantity.value}
                            preparation={ingredient.preparation}/>
            ));
        }
        else return [];
    }

    renderInstructions = (instructionSteps) => {
        if (instructionSteps.length > 0) {
            return instructionSteps.map((stepText, index) => (
                <InstructionStep
                    key={index}
                    index={index}
                    stepText={stepText} />
            ));
        }
        else return [];
    }

    render() {
        const image = this.props.recipe.rawImageBase64 ? this.props.recipe.rawImageBase64 : this.props.recipe.imageUrl
        const imagePreview = this.renderImagePreview(image)
        const ingredients = this.renderIngredients(this.props.recipe.ingredients);
        const instructions = this.renderInstructions(this.props.recipe.instructions.steps);

        return (
            <form id="recipe-form"
                  onSubmit={this.handleFormSubmit}>

                <div className="name-group form-group">
                  <label
                      className="form-label"
                      htmlFor="recipe-name">
                      Recipe Name
                  </label>
                  <input
                      id="recipe-name"
                      className="form-control"
                      type="text"
                      value={this.props.recipe.name}
                      placeholder={"Enter Recipe name"}
                      onChange={this.handleRecipeName.bind(this)}
                  />
                </div>

                <div id="image-upload">
                    <div className="form-group">
                        <label htmlFor="image-file-select">Save A Picture!</label>
                        <input
                            className="image-file-select form-control-file"
                            type="file"
                            onChange={(e)=>this.handleImageChange(e)} />
                    </div>
                    <div id="image-preview">
                        {imagePreview}
                    </div>
                </div>

                <div id="ingredients">
                    {ingredients}
                    <button
                        id="add-new-ingredient"
                        className="btn btn-primary"
                        label={"Add New Ingredient"}
                        onClick={this.handleAddIngredient}>Add New Ingredient</button>
                </div>

                <div id="instructions">
                    {instructions}
                    <button
                        id="add-next-instruction"
                        className="btn btn-primary"
                        label={"Add Next Step"}
                        onClick={this.handleAddInstructionStep}>Add Next Step</button>
                </div>

                <div id="finalise">
                    <button
                        id="save"
                        className="btn btn-primary"
                        disabled={this.props.saving}
                        onClick={this.handleSave}>Save</button>
                    <button
                        id="delete"
                        className="btn btn-secondary"
                        onClick={this.handleDelete}>{this.props.recipe.id ? "Delete" : "Clear"}</button>
                </div>
            </form>
        );
    }
}

const get = (url, callback) => {
    fetch(url, {
        method: "GET",
        headers: {
            Accept: "application/json",
        }
    }).then(response => {
        response.json().then(data => {
            callback(data)
        });
    });
}

const save = (method, url, body, callback) => {
    fetch(url, {
        method: method,
        body: body,
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        }
    }).then(response => {
        response.json().then(data => {
            callback(data)
        });
    });
}

const del = (url, callback) => {
   fetch(url, {
       method: "DELETE",
       headers: {
           Accept: "application/json",
       }
   }).then(response => {
       callback()
   });
}

function mapStateToProps(state) {
    return {
        recipe: state.recipe,
        saving: state.saving_flag
    };
}

export default connect(mapStateToProps)(RecipeEdit);
