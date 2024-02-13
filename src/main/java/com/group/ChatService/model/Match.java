package com.group.ChatService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.group.ChatService.constants.MatchConstants;
import com.group.ChatService.service.TypeUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matches")
public class Match {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type="string")
    @Id
    private ObjectId id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type="string")
    private List<ObjectId> userIds;

    private Integer status;
    private Boolean isDeleted;
    private Instant createdTime;
    private Instant updatedTime;

    @DocumentReference
    private List<MatchHistory> matchHistories;

    public Match(Map<String, Object> matchMap) {
        Class<?> matchClass = Match.class;
        Field[] fields = matchClass.getDeclaredFields();
        List<String> fieldsToSkip = Arrays.asList("userIds", "id", "isDeleted", "createdTime", "updatedTime");

        for (Field field: fields) {
            String fieldName = field.getName();
            if (!fieldsToSkip.contains(fieldName)) {
                TypeUtil.setField(this, field, matchMap.get(fieldName));
            }
        }

        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
        this.isDeleted = false;
        this.userIds = TypeUtil.objectIdArray(matchMap.get("userIds").toString());
        this.status = MatchConstants.STATUS.AWAIT.ordinal();
    }

    public Match(List<ObjectId> userIds) {
        this.userIds = userIds;
        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
        this.isDeleted = false;
        this.status = MatchConstants.STATUS.AWAIT.ordinal();
    }
}
