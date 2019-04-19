import React from 'react';
import './App.css';
import Main from "./components/main/Main"
import { Provider } from 'react-redux';
import { combineReducers, createStore } from 'redux';
import recipeEdit from './components/recipe-edit/recipeEditReducer'
import recipeList from './components/recipe-list/recipeListReducer'

const App = () => {

    const combined = combineReducers({recipeList, recipeEdit})
    const store = createStore(combined);

    return (
        <Provider store={store}>
            <div className="App">
                <Main/>
            </div>
        </Provider>
    );
}

export default App;
