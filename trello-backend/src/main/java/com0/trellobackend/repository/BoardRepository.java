package com0.trellobackend.repository;

import com0.trellobackend.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query("SELECT b FROM Board b WHERE b.workspace.workspace_id = :workspaceId")
    List<Board> findByWorkspaceWorkspaceId(@Param("workspaceId") int workspaceId);
}
