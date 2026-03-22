package com.example.urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.urlshortener.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {

    // Custom query method to find URL by its short code
   Url findByShortCode(String shortCode);

}