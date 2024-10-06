import React from 'react';
import SignUp from './SignUp';
import LogIn from './LogIn';

function Authentication(props) {
    function handleLogin(userId) {
        // if authenticated, then...
        props.handleLogin(userId); // set isLoggedIn to true in App.js
    }

    return (
        <div id="authentication">
            <SignUp onSignUp={handleLogin}/>
            <LogIn onLogin={handleLogin}/>
        </div>
    );
}

export default Authentication;