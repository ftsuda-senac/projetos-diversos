import React from 'react';
import './App.css';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Lista from './Lista';
import Form from './Form';

function App() {
  return (
    <>
    <header>
      <nav className="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a className="navbar-brand" href="#">Pessoas</a>
      </nav>
    </header>
    <BrowserRouter>
      <Switch>
        <Route path="/" exact={true} component={Lista} />
        <Route path="/new" exact={true} component={Form} />
        <Route path="/edit/:id" exact={true} component={Form} />
      </Switch>
    </BrowserRouter>
    <footer className="footer mt-auto py-3">
      <div className="container">
        <span className="text-muted">&copy; Senac TADS 2020</span>
      </div>
    </footer>
    </>
  );
}

export default App;
