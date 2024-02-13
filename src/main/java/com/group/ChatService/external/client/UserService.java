package com.group.ChatService.external.client;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import com.group.ChatService.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "account", url = "http://account-service-svc")
public interface UserService {

    @GetMapping("/api/v1/users/{id}")
    User getSingleUser(@PathVariable String id);

    @PostMapping("/api/v1/users/by-ids")
    List<User> getUsersByIds(@RequestBody List<String> ids);
}
