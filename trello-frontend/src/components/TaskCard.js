import React, { useEffect, useState } from "react";
import { DatePicker, Space } from 'antd';
import dayjs from 'dayjs';
import "../css/board.css";

function TaskCard(props) {
    const [taskStatus, setTaskStatus] = useState(props.status);
    const [dueDate, setDueDate] = useState(props.dueDate);
    const [users, assignUser] = useState(props.users);

    const onChange = (date, dateString) => {
        console.log("OnChange: " + date, dateString);
        console.log(date, dateString);
        setDueDate(dateString);
        };

    useEffect(() => {
        const updateStatus = () => {
            fetch(`http://localhost:9900/tasks/updateState/${props.taskId}?status=${taskStatus}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
                .then(response => response.json())
                .then(console.log("Successfully updated task status."))
                .catch(error => alert("We couldn't update the task status." + error));
        };

        // Call function when taskStatus changes
        updateStatus();
    }, [taskStatus]);

    useEffect(() => {
        const updateDate = () => {
            fetch(`http://localhost:9900/tasks/updateDueDate/${props.taskId}?newDate=${dueDate}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
                .then(response => response.json())
                .then(console.log("Successfully updated task due date."))
                .catch(error => alert("We couldn't update the task due date." + error));
        };

        // Call function when dueDate changes
        updateDate();
    }, [dueDate]);

      const addNewUser = () => {
        var newUserEmail = prompt("Enter an email:");
                        fetch(`http://localhost:9900/tasks/assignUser/${props.taskId}?email=${newUserEmail}`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                        })
                            .then(
                                response => {
                                    if (response.ok) {
                                        return response.json();
                                    }
                                    else {
                                        return response.json().then(data => {
                                          throw new Error(data.message || "Something went wrong."); 
                                        });
                                      }
                                }
                            )

                            .then( 
                                newTask => {
                                    console.log(newTask)
                                    console.log("Successfully updated users.")
                                }
                                
                                
                            )
                            .catch(error => alert("We couldn't update the users." + error));
                            
      };

    return(
        <div id="task-cards">
            <p class="task-text" id="task-name">{props.name}</p>
            <p class="task-text" id="status-text">Status: </p>
            <select class="form-select" id="task-select" defaultValue={taskStatus} onChange={(event) => setTaskStatus(event.target.value)}>
                <option value="todo">To-do</option>
                <option value="doing">Doing</option>
                <option value="done">Done</option>
            </select>
            <p class="task-text" id="date-text" >Due date: </p>
            <Space direction="vertical" id="date-picker">
                <DatePicker defaultValue = {dayjs(dueDate)} onChange={onChange} />
            </Space>
             <button
                            id="new-user-button"
                            className="btn btn-light"
                            onClick={addNewUser} >
                            Add New User
                          </button>
            <p class = "task-text" id="task-users">{props.users}</p>
        </div>
    );
}

export default TaskCard;