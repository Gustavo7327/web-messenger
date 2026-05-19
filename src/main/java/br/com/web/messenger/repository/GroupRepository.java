package br.com.web.messenger.repository;

import br.com.web.messenger.dto.group.GroupInfoDTO;
import br.com.web.messenger.dto.group.GroupMemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.web.messenger.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{
    @Query("""
        SELECT new br.com.web.messenger.dto.group.GroupInfoDTO(
            g.id, g.name, g.description, g.photoUrl, o.email
        )
        FROM Group g
        JOIN g.owner o
        WHERE g.id = :groupId
    """)
    Optional<GroupInfoDTO> findGroupInfoById(@Param("groupId") Long groupId);

    @Query("""
        SELECT new br.com.web.messenger.dto.group.GroupMemberDTO(
            m.id, u.name, u.photoUrl, m.role
        )
        FROM GroupMember m
        JOIN m.user u
        WHERE m.group.id = :groupId
    """)
    List<GroupMemberDTO> findGroupMembersById(@Param("groupId") Long groupId);
}
