import React from "react";

import Select from '../common/Select'

const Preparation = (props) => {
    if (props.ingredient.preparation) {
        return (
           <div>
                <Select
                    title={'Method'}
                    name={'unit'}
                    options = {props.possiblePreparationMethods}
                    value = {props.ingredient.preparation.method}
                    placeholder = {'Select method'}
                    onChange = {props.handlePreparationMethod}
                />{ }
                <Select
                    title={'Style'}
                    name={'style'}
                    options = {props.possiblePreparationStyles}
                    value = {props.ingredient.preparation.style}
                    placeholder = {'Select style'}
                    onChange = {props.handlePreparationStyle}
                />{ }
            </div>
        );
    }
    else return []
}

export default Preparation;
