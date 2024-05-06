import React, { useEffect, useState } from "react";
import axios from "axios";
import { useHistory } from "react-router-dom";

function UpdateTodo({ isAuthenticated, setIsAuthenticated, match }) {
  // State variables
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
  }, [isAuthenticated, history]);

  // Function to create a delay
  function timeout(delay) {
    return new Promise((res) => setTimeout(res, delay));
  }

  // Function to handle form submission
  const onSubmit = async (e) => {
    e.preventDefault();

    try {
      await axios.put(
        `http://localhost:8080/api/todo/${match.params.id}`,
        { title, description },
        {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        }
      );
    } catch (error) {
      setMessage("");
      if (error.response) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage("Error: something happened");
      }
      return;
    }

    setErrorMessage("");
    setMessage("Todo successfully updated");
    await timeout(1000); // Wait for 1 second
    history.push("/todo"); // Redirect to todo page
  };

  // Effect to load data for the todo
  useEffect(() => {
    const loadData = async () => {
      let response = null;
      try {
        response = await axios.get(
          `http://localhost:8080/api/todo/${match.params.id}`,
          {
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
          }
        );
      } catch (error) {
        setMessage("");
        if (error.response) {
          setErrorMessage(error.response.data.message);
        } else {
          setErrorMessage("Error: something happened");
        }
        return;
      }
      setErrorMessage("");
      setTitle(response.data.title);
      setDescription(response.data.description);
    };

    loadData();
  }, [match.params.id]); // Dependency: match.params.id

  // Effect to clear message when title or description changes
  useEffect(() => {
    setMessage("");
  }, [title, description]);

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
        <h1>Update Todo</h1>
        <div className="form-group">
          <label>Title</label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="form-control"
          ></input>
        </div>
        <div className="form-group">
          <label>Description</label>
          <input
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="form-control"
          ></input>
        </div>
        <button className="btn btn-primary">Update Todo</button>
      </form>
      {showMessage()}
      {showErrorMessage()}
    </div>
  );
}

export default UpdateTodo;
