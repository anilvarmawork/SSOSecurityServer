package com.cognibank.ssoResourceServer.Repository;


import com.cognibank.ssoResourceServer.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

@Repository
@Transactional
public class UserRedisRepository {

    @Autowired
    private RedisTemplate<String, User> redisTemplate;


    private HashOperations hashOperations;
    public static final String KEY = "USER";


    public UserRedisRepository(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }



    /*Adding a user into redis database*/
    public void addUser(User user){
        hashOperations.put(KEY,user.getUserId(),user);
    }


    /*Getting a specific user by user id from table*/
    public User getUser(String userId){
        return (User) hashOperations.get(KEY,userId);
    }








//    @Resource(name="redisTemplate")
//    private SetOperations<String, User> setOps;


    //    public void addUser(User user) {
//        System.out.println(user.getEmail() + "repo");
//        System.out.println(user.getUserId() + "repo");
//        redisTemplate.opsForList().leftPush("pooja", "anil");
//    }
//    public void removeUser(User user) {
//        redisTemplate.opsForList().remove(user.getUserId(), 1, user);
//    }

//    public void addUser(User user) {
//        setOps.add(user.getUserId(), user);
//    }
//
//    public Set<User> getNumberOfFamilyMembers(String id) {
//
//        return setOps.members(id);
//    }




}
