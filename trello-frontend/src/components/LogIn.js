import React, { useState } from 'react';
import '../css/authentication.css';
import ForgotPassword from './ForgotPassword';

function LogIn(props) {
  const [forgotPasswordClicked, setForgotPasswordClicked] = useState(false); 
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [showPassword, setShowPassword] = useState(false);

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  // Invert forgotPasswordClicked when "Forgot your password?" or "Go back" is clicked. 
  // This toggles the view between default Log In and Forgot Password.
  const handleForgotPasswordClick = () => {
    setForgotPasswordClicked(!forgotPasswordClicked);
  };

  function handleLoginSubmit(event) {
    event.preventDefault();

    const data = {
      email,
      password
    }

    fetch('http://localhost:9900/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response => response.json())
      .then(data => {
        console.log("user id is",data.userID);
        // localStorage.setItem("userId", JSON.stringify(data.userID))
        props.onLogin(data.userID);
      })
      .catch(error => alert("We couldn't find an account with that email and password." + error));
  }

  return (
    <div className="signup-login">
      {/*If forgotPasswordClicked is false, render the default Log In view. */}
      {!forgotPasswordClicked &&
      <div id="log-in-form">
        <form onSubmit={handleLoginSubmit}>
            <h4 className="signup-login-header">Log in to Trello</h4>
            <div className="form-group" class="mb-3">
              <label htmlFor="email" class="form-label">Email address</label>
              <input type="email" className="form-control" id="email" name="email" value={email} onChange={(event) => setEmail(event.target.value)} required />
            </div>
            <div className="form-group" class="mb-3">
            
              <label htmlFor="password" class="form-label">Password</label>
              <div class="password-field">
                <input type={showPassword ? 'text' : 'password'} className="form-control" id="password" name="password" value={password} onChange={(event) => setPassword(event.target.value)} required />
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={togglePasswordVisibility}
                >
                  {showPassword ? 'Hide' : 'Show'}
                </button>
              </div>
            </div>
            <button type="submit" className="btn btn-primary form-button">Log In</button>
        </form>
        <button type="button" class="btn" id="forgot-password" onClick={handleForgotPasswordClick}>Forgot your password?</button>
      </div>
      }       
      
      {/*If forgotPasswordClicked is true, render the Forgot Password view. */}
      {forgotPasswordClicked &&
        <ForgotPassword
          handleForgotPasswordClick={handleForgotPasswordClick}
          email={email}
          setEmail={setEmail}
          password={password}
          setPassword={setPassword}
        />
      }
    </div>
  );
}

export default LogIn;