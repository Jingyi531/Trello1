import React, { useEffect, useState } from "react";
import "../css/home.css";
import WorkspaceCard from "./WorkspaceCard";

function Home(props) {
  const userId = localStorage.getItem("userId");

  // const [workspaceCards, setWorkspaceCards] = useState([{id: `todo-${nanoid()}`}]);
  const [workspaceCards, setWorkspaceCards] = useState([]);
  useEffect(function(){
    getWorkspaces()
  },[])

  // get workspaces, workspace data from backend — this will be initial value of workspaceCards
  function getWorkspaces(){
    fetch( `http://localhost:9900/users/getWorkspaces/${userId}`)
    .then(response => response.json())
    .then(data => {
      if (Array.isArray(data)) {
        console.log(data)
        setWorkspaceCards(data);
      } 
      else {
        throw new Error("Workspaces is not an array.");
      }
    })
    .catch(error => alert("We couldn't get workspaces." + error));

  }
   
  // pass workspace data as props to WorkspaceCard component
  function addWorkspace() {
    // const workspaceCard = { id: `todo-${nanoid()}`}
    

    const data = {
      name: "New Workspace",
      description : "Add a description"
    }
    
    fetch(`http://localhost:9900/users/addWorkspace/${userId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response => response.json())
      .then( newWorkspaceCard=> {
        console.log(newWorkspaceCard)
        setWorkspaceCards(workspaceCards =>[...workspaceCards, newWorkspaceCard]);
        getWorkspaces();
     })
      .catch(error => alert("We couldn't create the workspaceCard." + error));
  
    
    
  };

  function deleteWorkspace(id) {
    

    fetch(`http://localhost:9900/workspace/deleteWorkspace/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      }
      
    })
      .then(response => response.json())
      .then(data => {
        const newCards = workspaceCards.filter((card) => id !== card.id);
        setWorkspaceCards(newCards);
        // const workspaceCard = data;
        
      getWorkspaces();
     })
      .catch(error => alert("We couldn't  the workspaceCard." + error));
  }

  function assignMembers(email, workspaceId){

    fetch(`http://localhost:9900/users/assignWorkspace/${workspaceId}?email=${email}`, {
      
      method: 'PUT',
    })
      .then(
        response => {
          if (response.ok) {
            return response.json();
          } 
          else {
            throw new Error('Fail to assign member');
          }
        }
      )
      .then( newWorkspaceCard=> {
        console.log(newWorkspaceCard)
        getWorkspaces()
        
     })
      .catch(error => alert("We couldn't assign member with the email." + error));
  
    
   
  }

  const workspaceCardList = 
   workspaceCards.map((card) => (
      <WorkspaceCard 
        deleteWorkspace={deleteWorkspace} 
        assignMembers = {assignMembers}
        id={card.workspace_id}
        key={card.workspace_id}
        name = {card.name}
        description = {card.description}
      />
    ));

  return (
    <div>
      <div id="home-container">
        <div id="workspaces-header" class="row justify-content-end">
          <div class="col-4">
            <h3 id="workspaces-header-text">Your Workspaces</h3>
          </div>
          <div class="col-4">
            <button
              id="new-workspace-button"
              className="btn btn-light"
              onClick={addWorkspace}
            >
              Create a Workspace
            </button>
          </div>
        </div>
        <div id="workspaces">{workspaceCardList}</div>
      </div>
    </div>
  );
}

export default Home;
