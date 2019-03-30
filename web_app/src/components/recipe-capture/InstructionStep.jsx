import React from "react";

import Input from "../common/Input";

const InstructionStep = (props) => {
    return (
        <div>
            <Input
                inputtype={"text"}
                name={"Add step details"}
                title={props.stepNumber}
                value={props.step}
                placeholder={"Add step details"}
                onChange={props.handleInstructionStepChange}
            />{" "}
        </div>
    )
}

export default InstructionStep;