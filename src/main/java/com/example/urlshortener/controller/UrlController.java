
package com.example.urlshortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.example.urlshortener.entity.Url;
import com.example.urlshortener.repository.UrlRepository;

import java.util.Random;
@CrossOrigin(origins = "*") // Allows the frontend to talk to the backend
@RestController
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;

    // Endpoint to shorten a URL
    @PostMapping("/shorten")
    public Url shortenUrl(@RequestBody Url url) {

        String shortCode = generateShortUrl();

        // Ensure short code is unique
        while (urlRepository.findByShortCode(shortCode) != null) {
            shortCode = generateShortUrl();
        }

        url.setShortCode(shortCode);

        return urlRepository.save(url);
    }

    // Endpoint to redirect using short URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginal(@PathVariable String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode);

        if (url != null) {
            return ResponseEntity.status(302)
                    .header("Location", url.getOriginalUrl())
                    .build();
        }

        return ResponseEntity.notFound().build();
    }

    // Method to generate random 6-character short URL
    private String generateShortUrl() {

        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrl = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }
}

