import React, { useEffect, useState } from "react";
import axios from "axios";
import { useHistory } from "react-router-dom";

function AddTodo({ isAuthenticated, setIsAuthenticated }) {
  // State variables to store form data, success and error messages
  const [title, setTitle] = useState(""); // Title of the todo
  const [description, setDescription] = useState(""); // Description of the todo
  const [message, setMessage] = useState(""); // Success message
  const [errorMessage, setErrorMessage] = useState(""); // Error message
  let history = useHistory(); // History object for navigation

  // Effect to redirect to login if not authenticated
  useEffect(() => {
    if (!isAuthenticated) {
      history.push("/");
    }
  }, [isAuthenticated, history]); // Dependencies: isAuthenticated, history

  // Function to create a delay
  function timeout(delay) {
    return new Promise((res) => setTimeout(res, delay));
  }

  // Function to handle form submission
  const onSubmit = async (e) => {
    e.preventDefault();

    try {
      // Send POST request to create a new todo
      await axios.post(
        "http://localhost:8080/api/todo",
        { title, description },
        {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        }
      );
    } catch (error) {
      // Reset success message and set error message if request fails
      setMessage("");
      if (error.response) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage("Error: something happened");
      }
      return;
    }

    // Reset form fields and messages
    setTitle("");
    setDescription("");
    setErrorMessage("");
    setMessage("Todo successfully created");
    await timeout(1000); // Wait for 1 second
    history.push("/todo"); // Redirect to todo page
  };

  // Effect to handle changes in title and description
  useEffect(() => {}, [title, description]); // Dependencies: title, description

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
    <div className="container">
      <form onSubmit={onSubmit}>
        <h1>Add New Todo</h1>
        <div className="form-group">
          <label>Title</label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Title"
            className="form-control"
          ></input>
        </div>
        <div className="form-group">
          <label>Description</label>
          <input
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Description"
            className="form-control"
          ></input>
        </div>
        <button className="btn btn-primary">Add Todo</button>
      </form>
      <br />
      {showMessage()}
      {showErrorMessage()}
    </div>
  );
}

export default AddTodo;
