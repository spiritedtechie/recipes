import React from "react";
import { connect } from 'react-redux';

import Input from "../common/Input";

const InstructionStep = (props) => {
    const handleInstructionStepChange = (i, e) => {
        props.dispatch({ type: "CHANGE_INSTRUCTION", index: i, value: e.target.value})
    }

    return (
        <div>
            <Input
                inputtype={"text"}
                name={"Add step details"}
                title={props.id}
                value={props.stepText}
                placeholder={"Add step details"}
                onChange={handleInstructionStepChange.bind(this, props.id)}
            />{" "}
        </div>
    )
}

function mapStateToProps(state, props) {
    return {}
}

export default connect(mapStateToProps)(InstructionStep);