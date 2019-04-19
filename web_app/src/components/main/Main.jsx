import React, { Component } from "react";
import { connect } from 'react-redux';
import { Route, NavLink, HashRouter } from "react-router-dom";
import RecipeEdit from "../recipe-edit/RecipeEdit";
import RecipeList from "../recipe-list/RecipeList"

class Main extends Component {

    render() {
        return (
            <HashRouter>
                <div>
                    <ul className="header">
                        <li><NavLink to="/recipe/new">Create Recipe</NavLink></li>
                    </ul>
                    <div className="content">
                        <Route exact path="/" component={RecipeList}/>
                        <Route exact path="/recipe/new" component={RecipeEdit}/>
                    </div>
                </div>
            </HashRouter>
        )
    }
}

function mapStateToProps(state) {
    return {
        
    };
}

export default connect(mapStateToProps)(Main);



