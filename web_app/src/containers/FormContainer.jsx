import React, { Component } from "react";

import Input from "../components/Input";
import Button from "../components/Button";
import Select from "../components/Select";

const Ingredient = (props) => {
    return (
        <div key={props.key}>
             <Input
                key={props.key}
                inputType={"text"}
                name={"Name"}
                title={"Name"}
                value={props.ingredient.name}
                placeholder={"Enter ingredient name"}
                handleChange={props.handleName}
            />{" "}
            <Input
                key={props.key}
                inputType={"number"}
                name={"Quantity"}
                title={"Quantity"}
                value={props.ingredient.quantity.value}
                placeholder={"Enter quantity"}
                handleChange={props.handleQuantity}
            />{" "}
            <Select
                key={props.key}
                title={'Unit'}
                name={'unit'}
                options = {props.possibleUnits}
                value = {props.ingredient.quantity.unit}
                placeholder = {'Select unit'}
                handleChange = {props.handleUnit}
            /> { }
        </div>
    );
}


class FormContainer extends Component {
    constructor(props) {
        super(props);

        this.state = {
            recipe: {
                name: "",
                ingredients: [

                ]
            },
            ingredient_quantity_units: []
        };

        this.recipeApiUrl = "http://localhost:8080"

        this.setStaticLookups()

        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.handleClearForm = this.handleClearForm.bind(this);
        this.handleAddIngredient = this.handleAddIngredient.bind(this);
    }

    setStaticLookups() {
        fetch(this.recipeApiUrl + "/ingredient/quantity/units", {
            method: "GET",
            headers: {
                Accept: "application/json",
            }
        }).then(response => {
            response.json().then(data => {
                this.setState({
                        ingredient_quantity_units: data
                    }
                );
            });
        });
    }

    handleFormSubmit(e) {
        e.preventDefault();

        let recipeData = this.state.recipe;

        console.log(JSON.stringify(recipeData))

        fetch(this.recipeApiUrl + "/recipes", {
            method: "POST",
            body: JSON.stringify(recipeData),
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json"
            }
        }).then(response => {
            console.log(response)

            response.text().then(data => {
                console.log("Response body: " + data);
            });
        });
    }

    handleRecipeName(e) {
        let value = e.target.value;
        this.updateState(
            prevState => ({
                recipe: {
                    ...prevState.recipe,
                    name: value
                }
            })
        );
    }

    handleIngredientName(i, e) {
        let value = e.target.value;
        this.updateIngredient(i, ingredient => {
            return {
                key: 'name',
                value: value
            }
        })
    }

    handleIngredientQuantity(i, e) {
        let value = e.target.value;
        this.updateIngredient(i, ingredient => {
            return {
                key: 'quantity',
                value: {
                  ...ingredient.quantity,
                  value: value
               }
            }
        })
    }

    handleIngredientQuantityUnit(i, e) {
        let value = e.target.value;
        this.updateIngredient(i, ingredient => {
            return {
                key: 'quantity',
                value: {
                  ...ingredient.quantity,
                  unit: value
               }
            }
        })
    }

    updateIngredient = (index, getChangeForIngredient) => {
        const ingredients = [ ...this.state.recipe.ingredients ];
        let change = getChangeForIngredient(ingredients[index])
        let ingredient = {
            ...ingredients[index]
        }
        ingredient[change.key] = change.value;
        ingredients[index] = ingredient;
        this.updateIngredients(ingredients)
    }

    updateIngredients = (ingredients) => {
        this.updateState(prevState => (
            {
                recipe: {
                    ...prevState.recipe,
                    ingredients: ingredients
                }
            })
        );
    }

    updateState = getNewState => {
        this.setState(prevState => getNewState(prevState),
            () => console.log(this.state)
        );
    }

    handleAddIngredient(e) {
        const ingredients = [ ...this.state.recipe.ingredients ];

        ingredients.push({
            quantity: {
                value: 1,
                unit: "unknown"
            }
        })

        this.updateIngredients(ingredients)
    }

    handleClearForm(e) {
        e.preventDefault();
        this.setState( {
            recipe: {
                name: "",
                ingredients: []
            }
        });
    }

    renderIngredients = (ingredients) => {
        if (ingredients.length > 0) {
            return ingredients.map((ingredient, index) => (
                <Ingredient ingredient={ingredient}
                            possibleUnits={this.state.ingredient_quantity_units}
                            handleName={this.handleIngredientName.bind(this, index)}
                            handleQuantity={this.handleIngredientQuantity.bind(this, index)}
                            handleUnit={this.handleIngredientQuantityUnit.bind(this, index)} />
            ));
        }
        else return [];
    }

    render() {
        const ingredients = this.renderIngredients(this.state.recipe.ingredients);
        return (
            <form className="container-fluid" onSubmit={this.handleFormSubmit}>
                <Input
                    inputType={"text"}
                    name={"Recipe name"}
                    title={"Recipe name"}
                    value={this.state.recipe.name}
                    placeholder={"Enter Recipe name"}
                    handleChange={this.handleRecipeName.bind(this)}
                />{" "}

                <br/>

                {ingredients}

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
                {/*Submit */}
                <Button
                    action={this.handleClearForm}
                    type={"secondary"}
                    title={"Clear"}
                    style={buttonStyle}
                />{" "}
                {/* Clear the form */}
            </form>
        );
    }
}

const buttonStyle = {
    margin: "10px 10px 10px 10px"
};

export default FormContainer;
