package com.example.urlshortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.example.urlshortener.entity.Url;
import com.example.urlshortener.service.UrlService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    // Endpoint to shorten a URL
    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("originalUrl");

        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
        }

        // Add protocol if missing
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        Url url = urlService.shortenUrl(originalUrl);
        return ResponseEntity.ok(url);
    }


    // Endpoint to redirect using short URL
    @GetMapping("/r/{shortCode}")
    public ResponseEntity<?> redirectToOriginal(@PathVariable String shortCode) {
        // Skip non-shortcode paths
        if (shortCode.equals("favicon.ico") || shortCode.equals("api")) {
            return ResponseEntity.notFound().build();
        }

        Optional<Url> url = urlService.getOriginalUrl(shortCode);

        if (url.isPresent()) {
            return ResponseEntity.status(302)
                    .header("Location", url.get().getOriginalUrl())
                    .build();
        }

        return ResponseEntity.notFound().build();
    }
}
