package com.pagepal.capstone.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MobileDeeplink {
    @Qualifier("webApplicationContext")
    private final ResourceLoader resourceLoader;

    @GetMapping("/.well-known/assetlinks.json")
    public ResponseEntity<?> getAssetLinks() {
        Resource classPathResource = resourceLoader.getResource("classpath:.well-known/assetlinks.json");
        return ResponseEntity.ok(classPathResource);
    }
}
