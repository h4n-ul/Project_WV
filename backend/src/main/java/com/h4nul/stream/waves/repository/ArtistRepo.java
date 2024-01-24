package com.h4nul.stream.waves.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h4nul.stream.waves.entities.Artist;

public interface ArtistRepo extends JpaRepository<Artist, UUID>{
    public Artist findByArtistID(String artistID);
    public Artist findByArtistName(String artistName);
}
