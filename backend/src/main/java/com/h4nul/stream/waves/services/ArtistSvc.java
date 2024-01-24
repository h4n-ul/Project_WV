package com.h4nul.stream.waves.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.h4nul.hancrypt.CryPackage;
import com.h4nul.hancrypt.HanCrypt;
import com.h4nul.stream.waves.entities.Artist;
import com.h4nul.stream.waves.repository.ArtistRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistSvc {
    private final ArtistRepo artistRepo;

    public void createArtist(String artistid, String artistname, String email, String password) {
        Artist artist = new Artist();
        HanCrypt hanCrypt = new HanCrypt();
        UUID uid = UUID.randomUUID();
        artist.setUuid(uid);
        artist.setArtistID(artistid);
        artist.setArtistName(artistname);
        artist.setEmail(email);
        CryPackage pack = hanCrypt.hash(uid.toString(), password, 96000, 512);
        artist.setHash(pack.getHash());
        artist.setPasses(pack.getPasses());
        artist.setSalt(pack.getSalt());
        // System.out.println(pack.getHash());
        // System.out.println(pack.getPasses());
        // System.out.println(pack.getSalt());
        this.artistRepo.save(artist);
    }

    public Artist login(String artistid, String password) {
        Artist target = artistRepo.findByArtistID(artistid);
        if (target == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "아이디를 찾을 수 없습니다."
            );
        }
        HanCrypt hanCrypt = new HanCrypt();
        Boolean match = hanCrypt.confirm(target.getUuid().toString(), password, target.getPasses(), target.getHash(), target.getSalt());
        if (!match) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "신원 증명에 실패했습니다."
            );
        }

        return target;
    }
}
