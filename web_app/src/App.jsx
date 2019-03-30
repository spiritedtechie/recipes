import React from 'react';
import './App.css';
import FormContainer from "./components/recipe-capture/FormContainer"
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import reducer from './components/recipe-capture/recipeCaptureReducer'

const App = () => {

    const store = createStore(reducer);

    return (
        <Provider store={store}>
            <div className="App">
                <FormContainer/>
            </div>
        </Provider>
    );
}

export default App;
