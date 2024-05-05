import React from "react";
import { Link } from "react-router-dom";

function Header({ isAuthenticated, setIsAuthenticated }) {
  // Header component displaying navigation links based on authentication status
  return (
    <header>
      <nav className="navbar navbar-expand-md navbar-dark bg-dark sticky-top">
        <div className="navbar-brand container">ToDoList App</div>
        <ul className="navbar-nav justify-content-end container">
          {/* Home link */}
          <li className="nav-link px-4">
            <Link to="/">Home</Link>
          </li>
          {/* Add Todo link, shown only when authenticated */}
          {isAuthenticated && (
            <li className="nav-link px-4">
              <Link to="/add">Add Todo</Link>
            </li>
          )}
          {/* Sign in link, shown only when not authenticated */}
          {!isAuthenticated && (
            <li className="nav-link px-4">
              <Link to="/signin">Sign in</Link>
            </li>
          )}
          {/* Sign up link, shown only when not authenticated */}
          {!isAuthenticated && (
            <li className="nav-link px-4">
              <Link to="/signup">Sign up</Link>
            </li>
          )}
          {/* Sign out link, shown only when authenticated */}
          {isAuthenticated && (
            <li className="nav-link px-4">
              <Link to="/signout">Sign out</Link>
            </li>
          )}
          {/* About link */}
          <li className="nav-link px-4">
            <Link to="/about">About</Link>
          </li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;
