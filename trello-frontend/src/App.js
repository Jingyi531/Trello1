import logo from "./trello_logo.png";
import "./css/index.css";
import React, { useState, useEffect } from "react";
import { Route, Switch, Redirect, useLocation } from "wouter";
import Home from "./components/Home.js";
import Authentication from "./components/Authentication.js";
import Workspace from "./components/Workspace.js";
import Unauthorized from "./components/Unauthorized.js";

function App() {
  const [location, setLocation] = useLocation();

  const [isLoggedIn, setIsLoggedIn] = useState(
    JSON.parse(localStorage.getItem("isLoggedIn"))
  );

  // Save isLoggedIn to local storage. This way user can reload pages while logged in.
  useEffect(() => {
    localStorage.setItem("isLoggedIn", JSON.stringify(isLoggedIn));
  }, [isLoggedIn]);

  // Redirect to /home when isLoggedIn is set to true
  useEffect(() => {
    if (isLoggedIn) {
      setLocation("/home");
    }
  }, [isLoggedIn, setLocation]);

  const handleLogin = (userid) => {
    setIsLoggedIn(true);
    localStorage.setItem("userId", JSON.stringify(userid));
  };

  return (
    <div className="App">
      <header className="App-header">
        <div id="nav-bar">
          <img id="nav-bar-logo" src={logo} alt="Trello Logo"></img>
          <h1 id="nav-bar-title">Trello</h1>
          {isLoggedIn &&
            (location === "/home" || location === "/workspace") && (
              <button
                type="button"
                id="sign-out-button"
                className="btn"
                onClick={() => setIsLoggedIn(false)}
              >
                Sign Out
              </button>
            )}
        </div>
      </header>
      <main>
        <Switch>
          <Route path="/home">
            {isLoggedIn ? <Home handleLogin={handleLogin} /> : <Redirect to="/" />}
          </Route>
          <Route
              path="/workspace/:workspaceName/:id"
              component={(props) => isLoggedIn ? <Workspace {...props} /> : <Redirect to="/" />}
          />
          <Route path="/">
            <Authentication handleLogin={handleLogin}/>
          </Route>
          <Route path="/unauthorized" component={Unauthorized} />
        </Switch>
      </main>
    </div>
  );
}

export default App;
