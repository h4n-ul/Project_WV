package com.h4nul.stream.waves.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.h4nul.stream.waves.dto.ArtistCreateDTO;
import com.h4nul.stream.waves.dto.LoginDTO;
import com.h4nul.stream.waves.entities.Artist;
import com.h4nul.stream.waves.services.ArtistSvc;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
@RequestMapping("/backend/artist")
public class ArtistContr {
    private final ArtistSvc artistSvc;

    @PostMapping("register")
    public void createArtist(@RequestBody ArtistCreateDTO entity) {
        try {
            artistSvc.createArtist(entity.getId(), entity.getUname(), entity.getEmail(), entity.getPassword());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "아티스트가 이미 있습니다."
            );
        }catch(Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()
            );
        }
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO entity, HttpSession session) {
        System.out.println(session.getId());

        if (session.getAttribute("loginuid") != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "로그아웃하지 않고 로그인할 수 없습니다."
            );
        }

        Artist artist = artistSvc.login(entity.getLoginId(), entity.getPassword());
        session.setAttribute("loginuid", artist.getUuid());
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("loginuid", artist.getUuid().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if (session.getAttribute("loginuid") == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "로그인하지 않고 로그아웃할 수 없습니다."
            );
        }
        session.removeAttribute("loginuid");
        return new ResponseEntity<>("SUCCEEDED", HttpStatus.OK);
    }
    
    @RequestMapping("loggedin")
    public ResponseEntity<String> checkLoggedIn(HttpSession session) {
        if (session.getAttribute("loginuid") != null) {
            return new ResponseEntity<>("YES", HttpStatus.OK);
        }
        return new ResponseEntity<>("NO", HttpStatus.OK);
    }
}
