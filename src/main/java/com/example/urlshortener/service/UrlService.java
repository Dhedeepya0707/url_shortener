package com.example.urlshortener.service;

import com.example.urlshortener.entity.Url;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private final Random random = new Random();

    public String generateShortUrl() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public Url shortenUrl(String originalUrl) {
        String shortCode = generateShortUrl();
        while (urlRepository.findByShortCode(shortCode) != null) {
            shortCode = generateShortUrl();
        }
        Url url = new Url(originalUrl, shortCode);
        return urlRepository.save(url);
    }

    public Optional<Url> getOriginalUrl(String shortCode) {
        return Optional.ofNullable(urlRepository.findByShortCode(shortCode));
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
}