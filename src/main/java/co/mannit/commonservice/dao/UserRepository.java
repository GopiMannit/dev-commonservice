package co.mannit.commonservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.mannit.commonservice.po.User;


//@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
