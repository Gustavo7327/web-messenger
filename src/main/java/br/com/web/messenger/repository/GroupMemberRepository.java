package br.com.web.messenger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.web.messenger.entity.GroupMember;
import br.com.web.messenger.entity.GroupMemberId;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    List<GroupMember> findByGroup_Id(Long groupId);

    List<GroupMember> findByUser_Id(Long userId);

    Optional<GroupMember> findByGroup_IdAndUser_Id(Long groupId, Long userId);

    void deleteByGroup_IdAndUser_Id(Long groupId, Long userId);
}
