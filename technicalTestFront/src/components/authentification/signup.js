import React, { useEffect, useState } from "react";
import axios from "axios";
import { useHistory } from "react-router-dom";
import { MDBContainer, MDBInput } from "mdb-react-ui-kit";

function Signup({ isAuthenticated, setIsAuthenticated }) {
  // State variables to store form data, success and error messages
  const [username, setUsername] = useState(""); // Username input
  const [password, setPassword] = useState(""); // Password input
  const [message, setMessage] = useState(""); // Success message
  const [errorMessage, setErrorMessage] = useState(""); // Error message
  let history = useHistory(); // History object for navigation

  // Function to create a delay
  function timeout(delay) {
    return new Promise((res) => setTimeout(res, delay));
  }

  // Function to handle form submission
  const onSubmit = async () => {
    try {
      // Send POST request to sign up
      const response = await axios.post(
        "http://localhost:8080/api/auth/signup",
        { username, password }
      );
      // Store token and username in session storage
      sessionStorage.setItem("token", response.data.token);
      sessionStorage.setItem("name", response.data.username);
      setIsAuthenticated(true); // Set isAuthenticated to true
    } catch (error) {
      // Reset success message and set error message if request fails
      setMessage("");
      if (error.response) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage("Error: something happened");
      }
      setIsAuthenticated(false); // Set isAuthenticated to false
      return;
    }

    // Reset form fields and messages
    setUsername("");
    setPassword("");
    setErrorMessage("");
    setMessage("Sign up successful");
    await timeout(1000); // Wait for 1 second
    history.push("/"); // Redirect to home page
  };

  // Effect to clear message when username or password changes
  useEffect(() => {
    setMessage("");
  }, [username, password]); // Dependencies: username, password

  // Function to display success message
  const showMessage = () => {
    if (message === "") {
      return <div></div>;
    }
    return (
      <div className="alert alert-success" role="alert">
        {message}
      </div>
    );
  };

  // Function to display error message
  const showErrorMessage = () => {
    if (errorMessage === "") {
      return <div></div>;
    }

    return (
      <div className="alert alert-danger" role="alert">
        {errorMessage}
      </div>
    );
  };

  // Rendering the form
  return (
    <MDBContainer className="p-3 my-5 d-flex flex-column w-50">
      <div className="text-center"> SIGN UP</div>
      {/* Username input */}
      <MDBInput
        wrapperClass="mb-4"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        id="form1"
      />
      {/* Password input */}
      <MDBInput
        wrapperClass="mb-4"
        placeholder="Password"
        id="form2"
        value={password}
        type="password"
        onChange={(e) => setPassword(e.target.value)}
      />

      {/* Submit button */}
      <button className="mb-4 btn" onClick={onSubmit}>
        Sign up
      </button>

      <div className="text-center">
        {/* Display success and error messages */}
        {showMessage()}
        {showErrorMessage()}
      </div>
    </MDBContainer>
  );
}

export default Signup;
