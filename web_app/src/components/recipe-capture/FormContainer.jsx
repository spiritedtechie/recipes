import React, { Component } from "react";
import { connect } from 'react-redux';

import Ingredient from "./Ingredient";
import InstructionStep from "./InstructionStep";

const recipeApiUrl = "http://localhost:8080"

class FormContainer extends Component {
    constructor(props) {
        super(props);
        getData(recipeApiUrl + "/ingredient/preparation/methods", (data) => {
            props.dispatch({ type: "SET_PREPARATION_METHODS_LOOKUPS", data: data });
        })
        getData(recipeApiUrl + "/ingredient/preparation/styles", (data) => {
            props.dispatch({ type: "SET_PREPARATION_STYLES_LOOKUPS", data: data });
        })
        getData(recipeApiUrl + "/ingredient/quantity/units", (data) => {
            props.dispatch({ type: "SET_QUANTITY_UNITS_LOOKUP", data: data });
        })

        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.handleClearForm = this.handleClearForm.bind(this);
        this.handleAddIngredient = this.handleAddIngredient.bind(this);
        this.handleAddInstructionStep = this.handleAddInstructionStep.bind(this);
    }

    handleFormSubmit(e) {
        e.preventDefault();

        let recipeData = this.props.recipe;
        console.log(JSON.stringify(recipeData))

//        fetch(this.recipeApiUrl + "/recipes", {
//            method: "POST",
//            body: JSON.stringify(recipeData),
//            headers: {
//                Accept: "application/json",
//                "Content-Type": "application/json"
//            }
//        }).then(response => {
//            console.log(response)
//
//            response.text().then(data => {
//                console.log("Response body: " + data);
//            });
//        });
    }

    handleRecipeName(e) {
        this.props.dispatch({ type: "CHANGE_NAME", value: e.target.value });
    }

    handleAddIngredient(e) {
        e.preventDefault();
        this.props.dispatch({ type: "ADD_NEW_INGREDIENT" })
    }

    handleAddInstructionStep(e) {
        e.preventDefault();
        this.props.dispatch({ type: "ADD_NEW_INSTRUCTION_STEP" })
    }

    handleClearForm(e) {
        e.preventDefault();
        this.props.dispatch({ type: "RESET_RECIPE" })
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
                        id="submit"
                        className="btn btn-primary"
                        onClick={this.handleFormSubmit}>Submit</button>
                    <button
                        id="clear"
                        className="btn btn-secondary"
                        onClick={this.handleClearForm}>Clear</button>
                </div>
            </form>
        );
    }
}

const getData = (url, callback) => {
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

function mapStateToProps(state) {
    return {
        recipe: state.recipe
    };
}

export default connect(mapStateToProps)(FormContainer);
