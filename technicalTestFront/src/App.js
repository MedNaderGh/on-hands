import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import Header from "./components/header/Header";
import About from "./components/page/About";
import Todos from "./components/todo/ViewTodos";
import AddTodo from "./components/todo/AddTodo";
import UpdateTodo from "./components/todo/UpdateTodo";
import Signin from "./components/authentification/Signin";
import Signup from "./components/authentification/Signup";
import Signout from "./components/authentification/Signout";
import Landing from "./components/page/Landing";
import NotFound from "./components/page/NotFound";

import "./bootstrap.min.css";
import "./App.css";

function App() {
  // State to manage authentication status
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check if user is authenticated on initial render
  useEffect(() => {
    if (sessionStorage.getItem("token") !== null) {
      setIsAuthenticated(true);
    }
  }, []);

  return (
    <Router>
      <div className="App">
        {/* Header component with authentication props */}
        <Header
          isAuthenticated={isAuthenticated}
          setIsAuthenticated={setIsAuthenticated}
        />
        <div>
          <Switch>
            {/* Route for landing page */}
            <Route
              exact
              path="/"
              render={(props) => (
                <Landing
                  {...props}
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                />
              )}
            />
            {/* Route for sign in page */}
            <Route
              exact
              path="/signin"
              render={(props) => (
                <Signin
                  {...props}
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                />
              )}
            />
            {/* Route for sign up page */}
            <Route
              exact
              path="/signup"
              render={(props) => (
                <Signup
                  {...props}
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                />
              )}
            />
            {/* Route for sign out page */}
            <Route
              exact
              path="/signout"
              render={(props) => (
                <Signout
                  {...props}
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                />
              )}
            />
            {/* Route for todos page */}
            <Route
              exact
              path="/todo"
              render={(props) => (
                <Todos
                  {...props}
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                />
              )}
            />
            {/* Route for adding a new todo */}
            <Route
              exact
              path="/add"
              render={(props) => (
                <AddTodo
                  {...props}
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                />
              )}
            />
            {/* Route for updating a specific todo */}
            <Route
              exact
              path="/update/:id"
              render={(props) => (
                <UpdateTodo
                  {...props}
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                />
              )}
            />
            {/* Route for about page */}
            <Route exact path="/about" component={About} />
            {/* Route for any other path, showing not found page */}
            <Route component={NotFound} />
          </Switch>
        </div>
      </div>
    </Router>
  );
}

export default App;
