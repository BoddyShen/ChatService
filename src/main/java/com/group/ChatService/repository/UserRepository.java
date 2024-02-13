package com.group.ChatService.repository;

import com.group.ChatService.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    List<User> findByIsActiveTrue();
    Optional<User> findUserByEmail(String email);

//    @Aggregation(pipeline = { "{ $sample: { size: ?0 } }" })
//    List<User> findRandomUsers(int limit);

//    @Aggregation(pipeline = {
//        "{ $match: { 'id': { $ne: ?1 } } }",
//        "{ $sample: { size: ?0 } }"
//    })
//    List<User> findRandomUsers(int limit, String user_id);

    @Query(value = "{ '_id': { $nin: ?0 } }")
    List<User> findByIdNotIn(List<ObjectId> ids, Pageable pageable);
}
