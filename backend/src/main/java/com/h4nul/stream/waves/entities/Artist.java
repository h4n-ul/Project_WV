package com.h4nul.stream.waves.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Artist {
    @Id
    private UUID uuid;

    @Column(nullable = false, unique=true)
    private String artistID;

    @Column(nullable = false, length = 500)
    private String artistName;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(nullable = false, length = 1024)
    private String hash;

    @Column(nullable = false, length = 1024)
    private String salt;

    @Column(nullable = false)
    private int passes;
}
