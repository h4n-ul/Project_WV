package com.h4nul.stream.waves.entities;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Mixtape {
    @Id
    @Column(length = 400)
    private String uid;
    private String hall;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Artist artist;
    private String title;
    @Column(length = 32768)
    private String contents;
    private boolean markdown;
    private String mixtype;
    private LocalDateTime createdDate;
    @ManyToMany
    private Set<Artist> like;
    @ManyToMany
    private Set<Artist> dislike;
    @ManyToMany
    private Set<Artist> views;
}
