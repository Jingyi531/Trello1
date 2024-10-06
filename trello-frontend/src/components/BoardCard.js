import React, {useEffect, useState} from "react";
import "../css/board.css";

function BoardCard(props) {

    const [boardCardName, setBoardCardName] = useState(props.name);
    const [id, setId] = useState(props.id);


    useEffect(function(){
        setBoardCardName(props.name)
        setId(props.id)
    }, [props.name, props.id]);

    function renameBoard(){
        const newName = prompt("Enter a new name for your workspace:")

        fetch(`http://localhost:9900/boards/edit/${id}?name=${newName}`, {
            method: 'PUT',
        })
            .then(
                response => {
                    if (response.ok) {
                        return response.json();
                    }
                    else {
                        throw new Error('Failed to rename the board.');
                    }
                }
            )
            .then( renamedWorkspaceCard=> {
                console.log(renamedWorkspaceCard)
                setBoardCardName(newName);

            })
            .catch(error => alert("We couldn't rename the board." + error));
    }

    function sendBoardData(){
        props.getBoardData(id, boardCardName);
    }

  return (
    <div className="board-card">
        <div id="board-card-header">
            <button class="board-header" id="board-header-button" onClick={sendBoardData}>{boardCardName}</button>
            <div class="btn-group" id="btn-group-board">
                <button
                    id="options-button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                &#8943;
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li>
                        <button class="dropdown-item" type="button" onClick={renameBoard}>
                            Rename Board
                        </button>
                    </li>
                    <li>
                        <button class="dropdown-item" type="button" onClick={() => props.deleteBoard(props.id)}>
                            Delete Board
                        </button>
                    </li>
                </ul>
            </div>
      </div>
    </div>
  );
}

export default BoardCard;
