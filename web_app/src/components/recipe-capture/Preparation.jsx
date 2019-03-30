import React from "react";
import { connect } from 'react-redux';

import Select from '../common/Select'

const Preparation = (props) => {

    const handlePreparationMethod = (i, e) => {
        props.dispatch({ type: "SET_PREPARATION_METHOD", index: i, value: e.target.value });
    }

    const handlePreparationStyle = (i, e) => {
        props.dispatch({ type: "SET_PREPARATION_STYLE", index: i, value: e.target.value });
    }

    return (
       <div>
            <Select
                title={'Method'}
                name={'unit'}
                options = {props.possiblePreparationMethods}
                value = {props.method}
                placeholder = {'Select method'}
                onChange = {handlePreparationMethod.bind(this, props.id)}
            />{ }
            <Select
                title={'Style'}
                name={'style'}
                options = {props.possiblePreparationStyles}
                value = {props.style}
                placeholder = {'Select style'}
                onChange = {handlePreparationStyle.bind(this, props.id)}
            />{ }
        </div>
    );
}

function mapStateToProps(state, props) {
    return {
        possiblePreparationMethods: state.ingredient_preparation_methods,
        possiblePreparationStyles: state.ingredient_preparation_styles
    }
}

export default connect(mapStateToProps)(Preparation);
