package com.h4nul.stream.waves.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.h4nul.stream.waves.dto.MixtapeCreateDTO;
import com.h4nul.stream.waves.entities.Artist;
import com.h4nul.stream.waves.entities.Mixtape;
import com.h4nul.stream.waves.repository.ArtistRepo;
import com.h4nul.stream.waves.services.MixtapeSvc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
@RequestMapping("/backend/mix")
public class MixtapeContr {
    private final ArtistRepo artistRepo;
    private final MixtapeSvc mixsvc;

    @GetMapping(value = "/{hall}/{mixtape}")
    public Mixtape getMixtape(@PathVariable("hall") String hall, @PathVariable("mixtape") String mixtapeID) {
        return mixsvc.getMixtapeByIDandHall(hall, mixtapeID);
    }
    @GetMapping(value = "/{hall}")
    public List<Mixtape> getHall(@PathVariable("hall") String hall) {
        return mixsvc.getMixtapeListByHall(hall);
    }

    @PostMapping("/create")
    public void postMethodName(@RequestBody MixtapeCreateDTO entity) {
        Artist artist = artistRepo.findByArtistID(entity.getCreatedBy());
        mixsvc.createMixtape(artist, entity.getHall(), entity.getTitle(), entity.getContents(), entity.getMixtype());
    }
}
