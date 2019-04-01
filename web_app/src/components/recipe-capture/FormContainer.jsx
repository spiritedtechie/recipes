import React, { Component } from "react";
import { connect } from 'react-redux';

import Input from "../common/Input";
import Button from "../common/Button";
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
                            id={index}
                            name={ingredient.name}
                            optional={ingredient.optional}
                            quantity_unit={ingredient.quantity.units}
                            quantity_value={ingredient.quantity.value}
                            preparation={ingredient.preparation}/>
            ));
        }
        else return [];
    }

    renderInstructions = (instructionSteps) => {
        if (instructionSteps.length > 0) {
            return instructionSteps.map((stepText, index) => (
                <InstructionStep key={index} id={index} stepText={stepText} />
            ));
        }
        else return [];
    }

    render() {
        const ingredients = this.renderIngredients(this.props.recipe.ingredients);
        const instructions = this.renderInstructions(this.props.recipe.instructions.steps);

        return (
            <form className="container-fluid" onSubmit={this.handleFormSubmit}>
                <Input
                    inputtype={"text"}
                    name={"Recipe name"}
                    title={"Recipe name"}
                    value={this.props.recipe.name}
                    placeholder={"Enter Recipe name"}
                    onChange={this.handleRecipeName.bind(this)}
                />{" "}

                <br/>

                {ingredients}
                <br/>

                {instructions}
                <br/>

                <Button
                    action={this.handleAddInstructionStep}
                    type={"primary"}
                    title={"Add Next Step"}
                    style={buttonStyle}
                />{" "}
                <Button
                    action={this.handleAddIngredient}
                    type={"primary"}
                    title={"Add New Ingredient"}
                    style={buttonStyle}
                />{" "}
                <Button
                    action={this.handleFormSubmit}
                    type={"primary"}
                    title={"Submit"}
                    style={buttonStyle}
                />{" "}
                <Button
                    action={this.handleClearForm}
                    type={"secondary"}
                    title={"Clear"}
                    style={buttonStyle}
                />{" "}
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

const buttonStyle = {
    margin: "10px 10px 10px 10px"
};

function mapStateToProps(state) {
    return {
        recipe: state.recipe
    };
}

export default connect(mapStateToProps)(FormContainer);
