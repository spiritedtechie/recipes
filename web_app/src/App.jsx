import React from 'react';
import './App.css';
import Main from "./components/main/Main"
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import reducer from './components/recipe-capture/recipeCaptureReducer'

const App = () => {

    const store = createStore(reducer);

    return (
        <Provider store={store}>
            <div className="App">
                <Main/>
            </div>
        </Provider>
    );
}

export default App;
