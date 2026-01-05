package br.com.web.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.web.messenger.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long>{
    
}
