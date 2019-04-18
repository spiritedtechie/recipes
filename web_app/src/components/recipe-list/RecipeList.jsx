import React, { Component } from "react";
import { connect } from 'react-redux';

class RecipeList extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <b>Recipe List</b>
        )
    }
}

function mapStateToProps(state) {
    return {
        
    };
}

export default connect(mapStateToProps)(RecipeList);



