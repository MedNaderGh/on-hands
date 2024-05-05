import React, { useEffect, useState } from "react";
import axios from "axios";
import { useHistory } from "react-router-dom";
import { MDBContainer, MDBInput } from "mdb-react-ui-kit";

function Signin({ isAuthenticated, setIsAuthenticated }) {
  // State variables
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  let history = useHistory(); // Function to delay execution

  function timeout(delay) {
    return new Promise((res) => setTimeout(res, delay));
  } // Handle form submission

  const onSubmit = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/auth/signin",
        { username, password }
      );
      sessionStorage.setItem("token", response.data.token);
      sessionStorage.setItem("name", response.data.username);
      setIsAuthenticated(true);
    } catch (error) {
      setMessage("");
      if (error.response) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage("Error: something happened");
      }
      setIsAuthenticated(false);
      return;
    }

    setUsername("");
    setPassword("");
    setErrorMessage("");
    setMessage("Sign in successful");
    await timeout(1000);
    history.push("/");
  }; // Reset message on username or password change

  useEffect(() => {
    setMessage("");
  }, [username, password]); // Display success message

  const showMessage = () => {
    if (message === "") {
      return <div></div>;
    }
    return (
      <div className="alert alert-success" role="alert">
                {message}     {" "}
      </div>
    );
  }; // Display error message

  const showErrorMessage = () => {
    if (errorMessage === "") {
      return <div></div>;
    }

    return (
      <div className="alert alert-danger" role="alert">
                {errorMessage}     {" "}
      </div>
    );
  }; // Render sign-in form

  return (
    <MDBContainer className="p-3 my-5 d-flex flex-column w-50">
            <div className="text-center"> SIGN IN</div>     {" "}
      <MDBInput
        wrapperClass="mb-4"
        placeholder="username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        id="form1"
      />
           {" "}
      <MDBInput
        wrapperClass="mb-4"
        placeholder="Password"
        id="form2"
        value={password}
        type="password"
        onChange={(e) => setPassword(e.target.value)}
      />
           {" "}
      <button className="mb-4 btn" onClick={() => onSubmit()}>
                Sign in      {" "}
      </button>
           {" "}
      <div className="text-center">
                {showMessage()}        {showErrorMessage()}     {" "}
      </div>
         {" "}
    </MDBContainer>
  );
}

export default Signin;
