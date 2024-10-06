import React from 'react';
import { Link } from 'wouter';
import "../css/unauthorized.css"

function Unauthorized() {
    return(
        <div id="unauthorized-div">
            <h3 id="unauthorized-header">You're not authorized to view this page</h3>
            <Link href="/" id="take-me-home">Take me home</Link>
        </div>
    );
}

export default Unauthorized;