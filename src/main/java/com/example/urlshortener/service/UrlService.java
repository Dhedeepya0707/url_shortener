package com.example.urlshortener.service;

import com.example.urlshortener.entity.Url;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private Random random = new Random();

    // Generate a random short URL
    public String generateShortUrl() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    // Save original URL and generate short URL
    public Url shortenUrl(String originalUrl) {
        String shortUrl = generateShortUrl();

        // Ensure unique short URL
        while (urlRepository.findByShortCode(shortUrl) != null) {
    shortUrl = generateShortUrl();
}


        Url url = new Url(originalUrl, shortUrl);
        return urlRepository.save(url);
    }

    // Retrieve original URL by short URL
    public Optional<Url> getOriginalUrl(String shortUrl) {
        return Optional.ofNullable(urlRepository.findByShortCode(shortUrl));
    }
}