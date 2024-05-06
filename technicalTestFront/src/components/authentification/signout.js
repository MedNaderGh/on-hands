import React, { useEffect } from "react";
import { useHistory } from "react-router-dom";

function Signout({ isAuthenticated, setIsAuthenticated }) {
  let history = useHistory(); // History object for navigation

  // Effect to clear session storage, set isAuthenticated to false, and redirect to home page
  useEffect(() => {
    sessionStorage.removeItem("token"); // Remove token from session storage
    sessionStorage.removeItem("name"); // Remove name from session storage
    setIsAuthenticated(false); // Set isAuthenticated to false
    history.push("/"); // Redirect to home page
  }, [history, setIsAuthenticated]); // Dependencies: history, setIsAuthenticated

  // Rendering a message for successful sign out
  return (
    <div className="text-center">
      <h1>Successfully sign out</h1>
    </div>
  );
}

export default Signout;
