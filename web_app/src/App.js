import React, { Component } from 'react';
import './App.css';
import FormContainer from "./components/recipe-capture/FormContainer"
import { Provider } from 'react-redux';
import { createStore } from 'redux';

class App extends Component {

  render() {

    function reducer() {
      return {
          count: 42
      };
    }

    const store = createStore(reducer);

    return (
        <Provider store={store}>
            <div className="App">
                <FormContainer/>
            </div>
        </Provider>
    );
  }

}

export default App;
