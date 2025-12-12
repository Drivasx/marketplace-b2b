package com.davidrivas.companyservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "company")
@Data
public class Company {

    @MongoId
    private UUID id;

    private String name;

    private String nit;

    @Field(name = "owner_user_id")
    private UUID ownerUserId;

    private String plan;

    private Settings settings;

    @Field(name = "created_at")
    private String createdAt;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Settings {
        private String currency;

        private String language;
    }
}
