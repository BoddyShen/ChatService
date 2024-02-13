package com.group.ChatService.external.client;

import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.group.ChatService.model.User;

import java.util.List;

@FeignClient(name="match", url="http://match-service-svc")
public interface MatchService {

    @GetMapping("/api/v1/match/get-all-matched-users-ids/{id}")
    List<String> getMatchedUsersId(@PathVariable String id);

}
