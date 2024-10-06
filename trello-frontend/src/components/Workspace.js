import React, {useEffect, useState} from "react";
import { Link } from "wouter";
import "../css/workspace.css";
import BoardCard from "./BoardCard";
import Board from "./Board";

function Workspace(props) {
    const workspaceName = props.params.workspaceName;//get from database
    const [boardCards, setBoardCards] = useState([]);
    const [activeBoardId, setActiveBoardId] = useState(-1);
    const [activeBoardName, setActiveBoardName] = useState("");
    const id = props.params.id;



    useEffect(function(){
        getBoards()
    }, [])

  // get boards, board data from backend — this will be initial value of boards


  // pass board data as props to Board component
  function getBoards(){
    fetch( `http://localhost:9900/boards/getBoards/${id}`)
      .then(response => response.json())
      .then(data => {
        if (Array.isArray(data)) {
          setBoardCards(data);
        }
        else {
          throw new Error("Workspaces is not an array.");
        }
    })
    .catch(error => alert("We couldn't get workspaces." + error));

  }

    function addBoard() {

      const data = {
          name: "New Board",
      }

      fetch(`http://localhost:9900/boards/create/${id}`, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
      })
          .then(response => response.json())
          .then( newBoardCard=> {
              setBoardCards(boardCards =>[...boardCards, newBoardCard]);
              getBoards();
          })
          .catch(error => alert("We couldn't create the boardCard." + error));


  };

  //delete board
    function deleteBoard(id) {
        fetch(`http://localhost:9900/boards/delete/${id}`, {
            method: 'Get',
            headers: {
                'Content-Type': 'application/json',
            }

        })
            .then(response => response.json())
            .then(data => {
                const newCards = boardCards.filter((card) => id !== card.id);
                setBoardCards(newCards);
                getBoards(id);
            })
            .catch(error => alert("We couldn't delete the boardCard." + error));
    }

    function getBoardData(id, boardName){
      setActiveBoardId(id);
      setActiveBoardName(boardName);
    }

    const boardCardList =
        boardCards.map((board) => (
            <BoardCard
                deleteBoard={deleteBoard}
                id={board.id}
                name={board.name}
                getBoardData={getBoardData}
            />
        ));

  return (
    <div>
      <nav id="sidebar">
        <div id="sidebar-content">
          <h4>{workspaceName}</h4>
          <Link href="/home" class="btn" className="change-workspace-button">
            &#x2190; Change Workspace
          </Link>
          <h5 id="your-boards">Your Boards</h5>
          <div id="boards" class="board-button">{boardCardList}</div>
            <button type="button" class="btn" id="new-board-button" onClick={addBoard}>
              Create New Board
            </button>
        </div>
      </nav>
      <Board
        activeBoardId={activeBoardId}
        activeBoardName={activeBoardName}
      />
    </div>
  );
}

export default Workspace;
