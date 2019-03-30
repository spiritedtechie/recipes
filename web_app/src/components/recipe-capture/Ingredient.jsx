import React from "react";

import Preparation from './Preparation'
import Input from '../common/Input'
import Select from '../common/Select'
import Button from '../common/Button'

const Ingredient = (props) => {
    return (
        <div>
            <Input
                inputtype={"text"}
                name={"Name"}
                title={"Name"}
                value={props.ingredient.name}
                placeholder={"Enter ingredient name"}
                onChange={props.handleName}
            />{" "}
            <Input
                inputtype={"number"}
                name={"Quantity"}
                title={"Quantity"}
                value={props.ingredient.quantity.value}
                placeholder={"Enter quantity"}
                onChange={props.handleQuantity}
            />{" "}
            <Select
                title={'Unit'}
                name={'unit'}
                options = {props.possibleUnits}
                value = {props.ingredient.quantity.unit}
                placeholder = {'Select unit'}
                onChange = {props.handleUnit}
            /> { }
            <Button
                action={props.handleEnablePreparation}
                type={"secondary"}
                title={"Preparation"}
                style={buttonStyle}
            />{" "}
            <Preparation
                ingredient={props.ingredient}
                possiblePreparationMethods={props.possiblePreparationMethods}
                possiblePreparationStyles={props.possiblePreparationStyles}
                handlePreparationMethod={props.handlePreparationMethod}
                handlePreparationStyle={props.handlePreparationStyle}
            />
        </div>
    );
}

const buttonStyle = {
    margin: "10px 10px 10px 10px"
};

export default Ingredient;
