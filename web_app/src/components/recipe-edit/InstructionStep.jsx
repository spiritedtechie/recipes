import React from "react";
import { connect } from 'react-redux';

const InstructionStep = (props) => {
    const handleInstructionStepChange = (i, e) => {
        props.dispatch({ type: "SET_INSTRUCTION", index: i, value: e.target.value})
    }

    const handleDelete = (i,e) => {
        props.dispatch({ type: "DELETE_INSTRUCTION", index: i});
    }

    return (
        <div id={"instruction-" + props.index} className="instruction form-group">
            <label
                className="form-label"
                htmlFor="instruction-text">
                {props.index + 1}
            </label>
            <input
                className="instruction-text form-control"
                type="text"
                placeholder="Add step details"
                value={props.stepText}
                onChange={handleInstructionStepChange.bind(this, props.index)}
            />
            <div className="delete" onClick={handleDelete.bind(this, props.index)}>&nbsp;</div>
        </div>
    )
}

function mapStateToProps(state, props) {
    return {}
}

export default connect(mapStateToProps)(InstructionStep);