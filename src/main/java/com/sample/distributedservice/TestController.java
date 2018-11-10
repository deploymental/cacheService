package com.sample.distributedservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
public class TestController {
    final Logger logger = LoggerFactory.getLogger(TestController.class);

    CacheService cacheService;

    @Autowired
    public TestController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PutMapping
    public ResponseEntity<?> put(@RequestParam int value){
        try {
            cacheService.putInteger(value);
        }catch (Exception e){
            logger.error("Exception while put in cache " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Success");
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam int value){
        try{
            return ResponseEntity.ok(cacheService.getInteger(value));
        }catch (Exception e){
            logger.error("Exception while get in cache " + e.getMessage());
            return ResponseEntity.badRequest().body("Not found");
        }
    }
}
