package com.h4nul.stream.waves.services;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.Random;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.h4nul.stream.waves.entities.Artist;
import com.h4nul.stream.waves.entities.Mixtape;
import com.h4nul.stream.waves.repository.MixtapeRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MixtapeSvc {
    private final MixtapeRepo mixrepo;
    public List<Mixtape> getMixtapeListByHall(String hall) {
        List<Mixtape> hallmixtapes = this.mixrepo.findByHall(hall);
        return hallmixtapes;
    }

    public Mixtape getMixtapeByIDandHall(String hall, String mixtapeID) {
        List<Mixtape> hallmixtapes = this.getMixtapeListByHall(hall);
        Optional<Mixtape> targetMixtape = hallmixtapes.stream()
        .filter(mixtape -> mixtape.getUid().equals(mixtapeID))
        .findFirst();

        if (targetMixtape.isPresent()) {
            return targetMixtape.get();
        }
        else {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "믹스테이프를 찾을 수 없습니다."
            );
        }
    }

    public void createMixtape(Artist createdBy, String hall, String title, String contents, String mixtype) {
        Mixtape targetMixtape = new Mixtape();
        Random random = ThreadLocalRandom.current();
        byte[] r = new byte[256];
        random.nextBytes(r);
        String uid = Base64.encodeBase64String(r).replaceAll("/", "_");
        targetMixtape.setArtist(createdBy);
        targetMixtape.setUid(uid);
        targetMixtape.setHall(hall);
        targetMixtape.setTitle(title);
        targetMixtape.setMixtype(mixtype);
        targetMixtape.setContents(contents);
        targetMixtape.setCreatedDate(LocalDateTime.now());
        this.mixrepo.save(targetMixtape);
    }
}
