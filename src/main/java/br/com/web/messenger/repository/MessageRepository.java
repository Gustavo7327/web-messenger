package br.com.web.messenger.repository;

import br.com.web.messenger.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    Page<Message> findByGroupId(Long groupId, Pageable pageable);

    @Query("{ '$or': [ { 'senderId': ?0, 'recipientId': ?1 }, { 'senderId': ?1, 'recipientId': ?0 } ] }")
    Page<Message> findDirectMessages(Long userId1, Long userId2, Pageable pageable);

    @Query("{ 'senderId': ?0, 'recipientId': ?1, 'message_read': false }")
    List<Message> findUnreadMessages(Long senderId, Long recipientId);

    @Query("{ 'recipientId': ?0, 'message_read': false }")
    long countUnreadMessagesByRecipient(Long recipientId);
}