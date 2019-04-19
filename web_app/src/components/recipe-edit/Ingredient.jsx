import React from "react";
import { connect } from 'react-redux';

const Ingredient = (props) => {

    const handlePreparationMethod = (i, e) => {
        props.dispatch({ type: "SET_PREPARATION_METHOD", index: i, value: e.target.value });
    }

    const handlePreparationStyle = (i, e) => {
        props.dispatch({ type: "SET_PREPARATION_STYLE", index: i, value: e.target.value });
    }

    const handleName = (i, e) => {
        props.dispatch({ type: "SET_INGREDIENT_NAME", index: i, value: e.target.value });
    }

    const handleQuantity = (i, e) => {
        props.dispatch({ type: "SET_INGREDIENT_QUANTITY", index: i, value: e.target.value });
    }

    const handleUnit = (i, e) => {
        props.dispatch({ type: "SET_INGREDIENT_UNIT", index: i, value: e.target.value });
    }

    const handleOptional = (i,e) => {
        props.dispatch({ type: "SET_INGREDIENT_OPTIONAL", index: i, value: e.target.checked});
    }

    const handleDelete = (i,e) => {
        props.dispatch({ type: "DELETE_INGREDIENT", index: i});
    }

    const renderPreparation = (index, preparation) => {
        return (
        <>
            <div className="prep-method-group form-group ">
                <label
                    className="form-label"
                    htmlFor="prep-method-select">
                    Method
                </label>
                <select
                    className="prep-method-select form-control"
                    value={preparation ? preparation.method : ""}
                    onChange={handlePreparationMethod.bind(this, index)}>
                        <option value="" disabled>Select method</option>
                        {props.possiblePreparationMethods.map(option => {
                            return (
                                <option
                                    key={option}
                                    value={option}
                                    label={option}>{option}</option>
                            );
                        })}
                    }
                </select>
            </div>
            <div className="prep-style-group form-group">
                <label
                    className="form-label"
                    htmlFor="prep-style-select">
                    Style
                </label>
                <select
                    className="prep-style-select form-control"
                    value={preparation ? preparation.style : ""}
                    onChange={handlePreparationStyle.bind(this, index)}>
                        <option value="" disabled>Select style</option>
                        {props.possiblePreparationStyles.map(option => {
                            return (
                                <option
                                    key={option}
                                    value={option}
                                    label={option}>{option}</option>
                            );
                        })}
                    }
                </select>
            </div>
            <div className="delete" onClick={handleDelete.bind(this, index)}>&nbsp;</div>
            </>
        );
    }

    const preparation = renderPreparation(props.index, props.preparation)

    return (
        <div id={"ingredient-" + props.index} className="ingredient">
            <div className="optional-group form-group">
                <label
                    className="form-label"
                    htmlFor="optional-checkbox">
                    Optional
                </label>
                <input
                    className="optional-checkbox form-check-input"
                    type="checkbox"
                    defaultChecked={props.optional}
                    onClick={handleOptional.bind(this, props.index)}
                />
            </div>
            <div className="name-group form-group">
                <label
                    className="form-label"
                    htmlFor="name-input">
                    Ingredient Name
                </label>
                <input
                    className="name-input form-control"
                    type="text"
                    value={props.name}
                    placeholder="Enter ingredient name"
                    onChange={handleName.bind(this, props.index)}
                />
            </div>
            <div className="quantity-group form-group">
                <label
                    className="form-label"
                    htmlFor="quantity-input">
                    Quantity
                </label>
                <input
                    className="quantity-input form-control"
                    type="number"
                    value={props.quantity_value}
                    placeholder="Enter quantity"
                    onChange={handleQuantity.bind(this, props.index)}
                />
            </div>
            <div className="unit-group form-group">
                <label
                    className="form-label"
                    htmlFor="unit-select">
                    Unit
                </label>
                <select
                    className="unit-select form-control"
                    value={props.quantity_unit}
                    onChange={handleUnit.bind(this, props.index)}>
                        <option value="" disabled>Select Unit</option>
                        {props.possibleUnits.map(option => {
                            return (
                                <option
                                    key={option}
                                    value={option}
                                    label={option}>{option}</option>
                            );
                        })}
                    }
                </select>
            </div>

            {preparation}

        </div>
    );
}

function mapStateToProps(state, props) {
    return {
        possibleUnits: state.recipeEdit.ingredient_quantity_units,
        possiblePreparationMethods: state.recipeEdit.ingredient_preparation_methods,
        possiblePreparationStyles: state.recipeEdit.ingredient_preparation_styles
    }
}

export default connect(mapStateToProps)(Ingredient);
