package com0.trellobackend.repository;

import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {

}
