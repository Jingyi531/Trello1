package com0.trellobackend.repository;

import com0.trellobackend.model.Board;
import com0.trellobackend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByBoardId(@Param("boardId") int BoardId);;

}
