package com.cognibank.ssoResourceServer.Repository;


import com.cognibank.ssoResourceServer.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRedisRepository {

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    public void addUser(User user) {
        redisTemplate.opsForList().leftPush(user.getUserId(), user);
    }
    public void removeUser(User user) {
        redisTemplate.opsForList().remove(user.getUserId(), 1, user);
    }
}
