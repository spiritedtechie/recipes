import React from "react";
import { connect } from 'react-redux';

import Preparation from './Preparation'
import Input from '../common/Input'
import Select from '../common/Select'
import Button from '../common/Button'

const Ingredient = (props) => {

    const handleName = (i, e) => {
        props.dispatch({ type: "SET_INGREDIENT_NAME", index: i, value: e.target.value });
    }

    const handleQuantity = (i, e) => {
        props.dispatch({ type: "SET_INGREDIENT_QUANTITY", index: i, value: e.target.value });
    }

    const handleUnit = (i, e) => {
        props.dispatch({ type: "SET_INGREDIENT_UNIT", index: i, value: e.target.value });
    }

    const handleEnablePreparation = (i,e) => {
        e.preventDefault();
        props.dispatch({ type: "SET_DEFAULT_PREPARATION", index: i});
    }

    const renderPreparation = (id, preparation) => {
        if (preparation) {
            return <Preparation id={id}
                                method={preparation.method}
                                style={preparation.style} />
        } else return []
    }

    const preparation = renderPreparation(props.id, props.preparation)

    return (
        <div>
            <Input
                inputtype={"text"}
                name={"Name"}
                title={"Name"}
                value={props.name}
                placeholder={"Enter ingredient name"}
                onChange={handleName.bind(this, props.id)}
            />{" "}
            <Input
                inputtype={"number"}
                name={"Quantity"}
                title={"Quantity"}
                value={props.quantity_value}
                placeholder={"Enter quantity"}
                onChange={handleQuantity.bind(this, props.id)}
            />{" "}
            <Select
                title={'Unit'}
                name={'unit'}
                options = {props.possibleUnits}
                value = {props.quantity_unit}
                placeholder = {'Select unit'}
                onChange = {handleUnit.bind(this, props.id)}
            /> { }
            <Button
                action={handleEnablePreparation.bind(this, props.id)}
                type={"secondary"}
                title={"Preparation"}
                style={buttonStyle}
            />{" "}

            {preparation}

        </div>
    );
}

const buttonStyle = {
    margin: "10px 10px 10px 10px"
};

function mapStateToProps(state, props) {
    return {
        possibleUnits: state.ingredient_quantity_units
    }
}

export default connect(mapStateToProps)(Ingredient);
