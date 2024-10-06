import React, { useState, useEffect } from "react";
import TaskCard from "./TaskCard";
import "../css/board.css";

function Board(props) {
  const boardName = props.activeBoardName;
  const boardID = props.activeBoardId;
  const [searchQuery, setSearchQuery] = useState("");
  const [taskCards, setTaskCards] = useState([]);
  const [selectedFilter, setSelectedFilter] = useState("all");

  useEffect(function() {
    if (boardID !== -1) {
      getTasks();
    }
  }, [boardID, selectedFilter]);

  function getTasks() {
    fetch( `http://localhost:9900/tasks/get/${boardID}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
    })
    .then(response => response.json())
    .then(data => {
      if (Array.isArray(data)) {
        console.log(data);
        setTaskCards(data);
      } 
      else {
        throw new Error("Tasks is not an array.");
      }
    })
    .catch(error => alert("We couldn't get tasks." + error));
  }

  function addTask() {
    const name = prompt("Enter a task: ");
    const data = { name: name };

    fetch(`http://localhost:9900/tasks/create/${boardID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(newTaskCard=> {
        console.log(newTaskCard)
        setTaskCards(taskCards =>[...taskCards, newTaskCard]);
        getTasks();
    })
    .catch(error => alert("We couldn't create the task card." + error));
  }

  const handleSearch = (event) => {
    setSearchQuery(event.target.value);
  };

  const searchTaskCards = taskCards.filter((card) =>
    card.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const setRealDueDate = (dateString) => {
    const date = new Date(dateString);
    const realTodayString = date.toLocaleDateString('en-US', { timeZone: 'America/Halifax' });
    const realDate = new Date(realTodayString).setHours(0, 0, 0, 0)
    return realDate
  }

  function filterTaskCards() { 
    let current = new Date();
    const realTodayString = current.toLocaleDateString('en-US', { timeZone: 'America/Halifax' });
    const today = new Date(realTodayString).setHours(0, 0, 0, 0)

    if (selectedFilter === "all") {
      return searchTaskCards;
    }
    else if (selectedFilter === "due-today") {
      const todayString = today.toISOString().slice(0, 10);
      const filteredCards = searchTaskCards.filter((card) => card.dueDate === todayString);
      return filteredCards;
    }
    else if (selectedFilter === "due-week") {
      var nextWeek = setRealDueDate(today.getTime() + 7 * 24 * 60 * 60 * 1000);
      const filteredCards = searchTaskCards.filter((card) => {
        const dueDate = setRealDueDate(card.dueDate);
        return dueDate <= nextWeek;
      });
      return filteredCards;
    }
    else if (selectedFilter === "overdue") {
      const filteredCards = searchTaskCards.filter((card) => {
        const dueDate = setRealDueDate(card.dueDate);
        return dueDate < today;
      });
      return filteredCards;
    }
  };

  const taskCardList =
    filterTaskCards().map((card) => 
        <TaskCard 
          name={card.name}
          taskId={card.taskId}
          status={card.status}
          dueDate={card.dueDate}
          users={card.users}
        />
  );

  return (
    <div id="board-container">
      {props.activeBoardId !== -1 ? (
        <div>
          <div id="boards-header" class="row justify-content-end">
            <div class="col-4">
              <h3 id="board-header-text">{boardName}</h3>
            </div>
            <div class="col-4">
              <button
                id="new-board-button"
                className="btn btn-light"
                onClick={addTask} 
              >
                Create a task
              </button>
            </div>
          </div>
          <div id="search-filter-container">
            <p id="search-text">Search: </p>
            <input
                id="search-input"
                type="text"
                placeholder="Search tasks by name"
                value={searchQuery}
                onChange={handleSearch}
            />
            <div id="filter-container">
              <p id="filter-text">Filter by: </p>
              <select id="filter-select" class="form-select" defaultValue={selectedFilter} onChange={(event) => setSelectedFilter(event.target.value)}>
                <option value="all">All</option>
                <option value="due-today">Due today</option>
                <option value="due-week">Due this week</option>
                <option value="overdue">Overdue</option>
              </select>
          </div>
          </div>
          
          <div id="task-card-container">
            {taskCardList}
          </div>
        </div>
      ) : 
      (
        <div id="boards-header" class="row justify-content-end">
            <h3 id="board-header-text">Please select a board</h3>
        </div>
      )}
      </div>
  );
}

export default Board;
