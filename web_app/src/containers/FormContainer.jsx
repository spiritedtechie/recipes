import React, { Component } from "react";

import Input from "../components/Input";
import Button from "../components/Button";
import Select from "../components/Select";

class FormContainer extends Component {
    constructor(props) {
        super(props);

        this.state = {
            recipe: {
                name: "",
                ingredients: [
                    {
                        quantity: {
                            value: 1,
                            unit: "unknown"
                        }
                    }
                ]
            },
            ingredient_quantity_units: []
        };

        this.recipeApiUrl = "http://localhost:8080"

        this.setStaticLookups()

        this.handleRecipeName = this.handleRecipeName.bind(this);
        this.handleQuantity = this.handleQuantity.bind(this);
        this.handleUnit = this.handleUnit.bind(this);
        this.handleFormSubmit = this.handleFormSubmit.bind(this);
        this.handleClearForm = this.handleClearForm.bind(this);
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
        this.setState(
            prevState => ({
                recipe: {
                    ...prevState.recipe,
                    name: value
                }
            })
        );
    }

    handleQuantity(e) {
        let value = e.target.value;

        const ingredients = [ ...this.state.recipe.ingredients ];

        let newIngredient = {
            ...ingredients[0],
            quantity: {
                unit: ingredients[0].quantity.units,
                value: value
            }
        }

        ingredients[0] = newIngredient;


        this.setState(prevState => (
                {
                    recipe: {
                        ...prevState.recipe,
                        ingredients: ingredients
                    }
                })
        );
    }

    handleUnit(e) {
        let value = e.target.value;

        const ingredients = [ ...this.state.recipe.ingredients ];

        let ingredient = {
            ...ingredients[0],
            quantity: {
                unit: value,
                value: ingredients[0].quantity.value

            }
        }

        ingredients[0] = ingredient;


        this.setState(prevState => (
            {
                recipe: {
                    ...prevState.recipe,
                    ingredients: ingredients
                }
            }), d => {
                console.log(this.state)
            }
        );
    }

    handleClearForm(e) {
        e.preventDefault();
        this.setState( {
            recipe: {
                name: "",
                ingredients: [
                    {
                        quantity: {
                            value: 1,
                            units: "unknown"
                        }
                    }
                ]
            }
        });
    }

    render() {
        return (
            <form className="container-fluid" onSubmit={this.handleFormSubmit}>
                <Input
                    inputType={"text"}
                    name={"Recipe name"}
                    title={"Recipe name"}
                    value={this.state.recipe.name}
                    placeholder={"Enter Recipe name"}
                    handleChange={this.handleRecipeName}
                />{" "}
                Ingredients
                { }
                <br/>

                <Input
                    inputType={"number"}
                    name={"Quantity"}
                    title={"Quantity"}
                    value={this.state.recipe.ingredients[0].quantity.value}
                    placeholder={"Enter quantity"}
                    handleChange={this.handleQuantity}
                />{" "}
                <Select title={'Unit'}
                        name={'unit'}
                        options = {this.state.ingredient_quantity_units}
                        value = {this.state.recipe.ingredients[0].quantity.unit}
                        placeholder = {'Select unit'}
                        handleChange = {this.handleUnit}
                /> { }

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
