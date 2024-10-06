import React, { useEffect } from "react";
import { Link } from "wouter";
import { useState } from "react";
import "../css/home.css";

function WorkspaceCard(props) {
  const [workspaceName, setWorkspaceName] = useState(props.name);
  const [description, setDescription] = useState(props.description);

  const [id, setId] = useState(props.id);

  useEffect(function(){
    setWorkspaceName(props.name)
    setDescription(props.description)
    setId(props.id)
  }, [props]);

  function renameWorkspace(){
    const newName = prompt("Enter a new name for your workspace:")
    
    fetch(`http://localhost:9900/workspace/rename/${props.id}?newName=${newName}`, {
      method: 'PUT',
    })
      .then(
        response => {
          if (response.ok) {
            return response.json();
          } 
          else {
            throw new Error('Failed to rename workspace.');
          }
        }
      )
      .then( renamedWorkspaceCard=> {
        console.log(renamedWorkspaceCard)
        setWorkspaceName(newName);
        
     })
      .catch(error => alert("We couldn't rename the workspaceCard." + error));
  

    
  }

  function updateDescription(){
    const newDescription = prompt("Enter your new workspace description:")
    
    fetch(`http://localhost:9900/workspace/updateDescription/${props.id}?newDescription=${newDescription}`, {
      method: 'PUT',
    })
      .then(
        response => {
          if (response.ok) {
            return response.json();
          } 
          else {
            throw new Error('Fail to update description');
          }
        }
      )
      .then( renamedWorkspaceCard=> {
        console.log(renamedWorkspaceCard)
        setDescription(newDescription); 
        
     })
      .catch(error => alert("We couldn't update description." + error));
    
  }

  return (
    <div className="workspace-card">
      <div id="workspace-card-header">
        <h4 id="workspace-card-h4">
          <Link to={`/workspace/${workspaceName}/${id}`} id="link">
            {workspaceName} &#x2192;
          </Link>
        </h4>
        <div class="btn-group">
          <button
            id="options-button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            &#8943;
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li>
              <button class="dropdown-item" type="button" onClick={renameWorkspace}>
                Rename Workspace
              </button>
            </li>
            <li>
              <button class="dropdown-item" type="button" onClick={updateDescription}>
                Edit Description
              </button>
            </li>
            <li>
              <button class="dropdown-item" type="button" onClick={() => props.assignMembers(prompt("Enter an email address to assign to that member:"), props.id)}>
                Assign Members
              </button>
            </li>
            <li>
              <button class="dropdown-item" type="button" onClick={() => props.deleteWorkspace(props.id)}>
                Delete Workspace
              </button>
            </li>
          </ul>
        </div>
      </div>
      <p id="workspace-card-desc">{description}</p>
    </div>
  );
}

export default WorkspaceCard;
