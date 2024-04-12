package com.nhnacademy.shop.publisher.controller;

import com.nhnacademy.shop.publisher.dto.PublisherRequestDto;
import com.nhnacademy.shop.publisher.dto.PublisherResponseDto;
import com.nhnacademy.shop.publisher.exception.AlreadyExistPublisherNameException;
import com.nhnacademy.shop.publisher.exception.NotFoundPublisherException;
import com.nhnacademy.shop.publisher.exception.NotFoundPublisherNameException;
import com.nhnacademy.shop.publisher.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Publisher Controller
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
@RestController
@RequestMapping("/shop/publisher")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/id/{publisherId}")
    public ResponseEntity<PublisherResponseDto> getPublisherById(@PathVariable Long publisherId) {
        try {
            PublisherResponseDto responseDto = publisherService.getPublisherById(publisherId);
            return ResponseEntity.ok(responseDto);
        }catch(NotFoundPublisherException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/name/{publisherName}")
    public ResponseEntity<PublisherResponseDto> getPublisherByPublisherName(@PathVariable String publisherName) {
        try {
            PublisherResponseDto responseDto = publisherService.getPublisherByName(publisherName);
            return ResponseEntity.ok(responseDto);
        } catch(NotFoundPublisherNameException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/modify/{publisherId}")
    public ResponseEntity<PublisherResponseDto> modifyPublisher(@PathVariable Long publisherId,
                                                                @RequestBody PublisherRequestDto publisherRequestDto) {
        try {
            PublisherResponseDto responseDto = publisherService.modifyPublisher(publisherId, publisherRequestDto);
            return ResponseEntity.ok(responseDto);
        } catch(NotFoundPublisherException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PublisherResponseDto> savePublisher(@RequestBody PublisherRequestDto publisherRequestDto) {
        try {
            PublisherResponseDto responseDto = publisherService.savePublisher(publisherRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch(AlreadyExistPublisherNameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/delete/id/{publisherId}")
    public ResponseEntity<Void> deletePublisherByPublisherId(@PathVariable Long publisherId) {
        try {
            publisherService.deletePublisherById(publisherId);
            return ResponseEntity.ok().build();
        } catch(NotFoundPublisherException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/name/{publisherName}")
    public ResponseEntity<Void> deletePublisherByPublisherName(@PathVariable String publisherName) {
        try {
            publisherService.deletePublisherByName(publisherName);
            return ResponseEntity.ok().build();
        } catch(NotFoundPublisherNameException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}