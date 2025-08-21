package com.identity.utils;

import com.identity.cache.BaseCacheManager;
import com.identity.entity.User;
import com.identity.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CacheUtility {
    BaseCacheManager baseCacheManager;
    UserRepository userRepository;

    public void setUser(User user, String type){
        if(type.equals("DELETE")){
            baseCacheManager.delete("User:"+user.getId());
        }else{
            baseCacheManager.setCache("User:"+user.getId(), user, 30, TimeUnit.DAYS );
        }
    }

    public User getUser(String id) {
        User user = (User) baseCacheManager.getObject("User:" + id, User.class);
        if(DataUtils.isNull(user)) {
            user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            this.setUser(user, "UPDATE");
        }
        return user;
    }
}

