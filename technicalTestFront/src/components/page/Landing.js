import axios from "axios";
import React, { useEffect, useState } from "react";
import ViewTodos from "../todo/ViewTodos";
import { useHistory } from "react-router-dom";

export default function Landing({ isAuthenticated, setIsAuthenticated }) {
  // State variables to store messages and todo counts
  const [message, setMessage] = useState(""); // Welcome message or sign-in prompt
  const [numberAllTodoNotCompleted, setNumberAllTodoNotCompleted] = useState(0); // Number of incomplete todos
  const [numberAllTodo, setNumberAllTodo] = useState(0); // Total number of todos
  const [errorMessage, setErrorMessage] = useState(""); // Error message
  let history = useHistory(); // History object for navigation

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

  // Function to create a delay
  function timeout(delay) {
    return new Promise((res) => setTimeout(res, delay));
  }

  useEffect(() => {
    // Function to fetch and set the total number of todos
    async function getAndSetNumberAllTodo() {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/todo/count",
          {
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
          }
        );
        setNumberAllTodo(response.data.count);
      } catch (error) {
        setMessage("");
        if (error.response) {
          setErrorMessage(error.response.data.message);
        } else {
          setErrorMessage("Error: something happened");
        }
      }
    }

    // Function to fetch and set the number of incomplete todos
    async function getAndSetNumberAllTodoNotCompleted() {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/todo/count?isCompleted=false",
          {
            headers: {
              Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
          }
        );

        setNumberAllTodoNotCompleted(response.data.count);
      } catch (error) {
        setMessage("");
        if (error.response) {
          setErrorMessage(error.response.data.message);
        } else {
          setErrorMessage("Error: something happened");
        }
      }
    }

    // Check if the user is authenticated
    if (isAuthenticated) {
      // Fetch and set the total number of todos
      getAndSetNumberAllTodo();
      // Fetch and set the number of incomplete todos
      getAndSetNumberAllTodoNotCompleted();
      // Set the welcome message with user's name and todo counts
      setMessage(
        `Welcome, ${sessionStorage.getItem(
          "name"
        )}. You have ${numberAllTodoNotCompleted} todo not completed out of ${numberAllTodo} todo.`
      );
    } else {
      // Set sign-in prompt
      setMessage("Please sign in to continue");

      // Redirect to sign-in page after 1 second
      (async () => {
        await timeout(1000);
      })();
      history.push("/signin");
    }
  }, [isAuthenticated, numberAllTodo, numberAllTodoNotCompleted]);

  return (
    <div className="text-center">
      <h1>Todo List Application</h1>
      {showErrorMessage()}
      <b> {message} </b>
      <ViewTodos />
    </div>
  );
}
