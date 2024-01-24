package com.h4nul.stream.waves.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.h4nul.stream.waves.entities.Mixtape;

public interface MixtapeRepo extends JpaRepository<Mixtape, String>{
    public List<Mixtape> findByHall(String hall);
}
