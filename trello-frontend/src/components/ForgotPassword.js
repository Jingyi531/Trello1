import React from 'react';

function ForgotPassword(props) {
    const [securityQuestion, setSecurityQuestion] = React.useState(1);
    const [securityAnswer, setSecurityAnswer] = React.useState('');

    const handleSubmit = (event) => {
        event.preventDefault();

        const email = props.email;
        const password = props.password;

        // JSON object to send to backend
        const data = {
            email,
            password,
            securityQuestion,
            securityAnswer
        };

        // Call to backend to create a new user, pass data
        fetch('http://localhost:9900/users/reset-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.text())
            .then(data => {
                alert(data);
                // Switch to log in view
                props.handleForgotPasswordClick();
            })
            .catch(error => alert(
                "Failed to reset password. Make sure the email, password, security question and security answer are correct."
                + error));
    };

    return (
        <div id="forgot-password-form">
            <h4 className="signup-login-header">Reset your password</h4>
            <form onSubmit={handleSubmit}>
                <div className="form-group" class="mb-3">
                    <label htmlFor="email" class="form-label">Email address</label>
                    <input type="email" className="form-control" id="email" name="email" value={props.email} onChange={(event) => props.setEmail(event.target.value)} required />
                </div>
                <div className="form-group" class="mb-3">
                    <label htmlFor="password" class="form-label">New password</label>
                    <input type="password" className="form-control" id="password" name="password" value={props.password} onChange={(event) => props.setPassword(event.target.value)} required />
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
                <button type="submit" className="btn btn-primary form-button">Reset Password</button>
            </form>
            <button type="button" className="btn" id="go-back" onClick={props.handleForgotPasswordClick}>Go back</button>
        </div>
    );
}

export default ForgotPassword;