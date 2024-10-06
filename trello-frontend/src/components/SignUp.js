import React, { useState } from 'react';
import '../css/authentication.css';

function SignUp(props) {
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [securityQuestion, setSecurityQuestion] = React.useState(1);
  const [securityAnswer, setSecurityAnswer] = React.useState('');
  const [showPassword, setShowPassword] = useState(false);

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (password.length < 8) {
      alert('Password must be at least 8 characters');
    } 
    else if (!/[A-Z]/.test(password)) {
      alert('Password must contain at least one uppercase character');
    } 
    else if (!/[a-z]/.test(password)) {
      alert('Password must contain at least one lowercase character');
    }
    else if (!/\d/.test(password)) {
      alert('Password must contain at least one number');
    }
    else if (!/\W/.test(password)) {
      alert('Password must contain at least one special character');
    }
    else {
      createUser();
    }
  };

  function createUser() {
    // JSON object to send to backend
    const data = {
      email,
      password,
      securityQuestion,
      securityAnswer
    };

    // Call to backend to create a new user, pass data
    fetch('http://localhost:9900/users/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(
        response => {
          if (response.ok) {
            return response.json();
          } 
          else {
            throw new Error('Failed to create account');
          }
        }
      )
      .then(data => {
        console.log(data);
        console.log("user id is",data.userID);
        props.onSignUp(data.userID);
      })
      .catch(error => alert("That email address is already being used." + error));
  }

  return (
    <div className="signup-login">
        <h4 className="signup-login-header">Create a Trello account</h4>
        <form onSubmit={handleSubmit}>
            <div className="form-group" class="mb-3">
                <label htmlFor="email" class="form-label">Email address</label>
                <input type="email" className="form-control" id="email" name="email" value={email} onChange={(event) => setEmail(event.target.value)} required />
            </div>

            <div className="form-group" class="mb-3">
                <label htmlFor="password" class="form-label">Password</label>
                {/* <input type="password" className="form-control" id="password" name="password" value={password} onChange={(event) => setPassword(event.target.value)} required /> */}
                <div className="password-field">
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

           

            <div className="form-group" id="security-question" class="mb-3">
                <label htmlFor="security-question" class="form-label">Security question</label>
                <select class="form-select" aria-label="Default select example" defaultValue={securityQuestion} onChange={(event) => setSecurityQuestion(event.target.value)}>
                  <option value="1">What was the name of your first pet?</option>
                  <option value="2">What is your mother's middle name?</option>
                  <option value="3">What is your father's middle name?</option>
                  <option value="4">What is your oldest sibling's middle name?</option>
                  <option value="5">What is your youngest sibling's middle name?</option>
                  <option value="6">In what town or city did your parents meet?</option>
                  <option value="7">In what town or city was your first job?</option>
                </select>
            </div>

            <div className="form-group" class="mb-3">
                <label htmlFor="security-answer" class="form-label">Answer</label>
                <input type="text" className="form-control" id="security-answer" name="security-answer" value={securityAnswer} onChange={(event) => setSecurityAnswer(event.target.value)} required />
            </div>

            <button type="submit" className="btn btn-success form-button">Sign Up</button>
      </form>
    </div>
  );
}

export default SignUp;