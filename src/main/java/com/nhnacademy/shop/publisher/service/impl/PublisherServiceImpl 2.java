package com.nhnacademy.shop.publisher.service.impl;

import com.nhnacademy.shop.publisher.domain.Publisher;
import com.nhnacademy.shop.publisher.dto.PublisherRequestDto;
import com.nhnacademy.shop.publisher.dto.PublisherResponseDto;
import com.nhnacademy.shop.publisher.exception.AlreadyExistPublisherNameException;
import com.nhnacademy.shop.publisher.exception.NotFoundPublisherException;
import com.nhnacademy.shop.publisher.exception.NotFoundPublisherNameException;
import com.nhnacademy.shop.publisher.repository.PublisherRepository;
import com.nhnacademy.shop.publisher.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * PublisherService 구현체
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;


    @Override
    public PublisherResponseDto getPublisherById(Long publisherId) {
        Optional<Publisher> optionalPublisher = publisherRepository.findById(publisherId);

        if(optionalPublisher.isEmpty()) {
            throw new NotFoundPublisherException(publisherId);
        }

        return new PublisherResponseDto(optionalPublisher.get());
    }

    @Override
    public PublisherResponseDto getPublisherByName(String publisherName) {
        Optional<Publisher> optionalPublisher = publisherRepository.findPublisherByPublisherName(publisherName);

        if(optionalPublisher.isEmpty()) {
            throw new NotFoundPublisherNameException(publisherName);
        }

        return new PublisherResponseDto(optionalPublisher.get());
    }

    @Override
    public PublisherResponseDto savePublisher(PublisherRequestDto publisherRequestDto) {
        String publisherName = publisherRequestDto.getPublisherName();
        Optional<Publisher> optionalPublisher = publisherRepository.findPublisherByPublisherName(publisherName);

        if(optionalPublisher.isPresent()) {
            throw new AlreadyExistPublisherNameException(publisherName);
        }

        Publisher publisher = new Publisher(null, publisherName);

        return new PublisherResponseDto(publisherRepository.save(publisher));
    }

    @Override
    public PublisherResponseDto modifyPublisher(Long publisherId, PublisherRequestDto publisherRequestDto) {
        Optional<Publisher> optionalPublisher = publisherRepository.findById(publisherId);

        if(optionalPublisher.isEmpty()) {
            throw new NotFoundPublisherException(publisherId);
        }

        Publisher publisher = new Publisher(publisherId, publisherRequestDto.getPublisherName());

        return new PublisherResponseDto(publisherRepository.save(publisher));
    }

    @Override
    public void deletePublisherByName(String publisherName) {
        Optional<Publisher> optionalPublisher = publisherRepository.findPublisherByPublisherName(publisherName);

        if(optionalPublisher.isEmpty()) {
            throw new NotFoundPublisherNameException(publisherName);
        }

        publisherRepository.delete(optionalPublisher.get());
    }

    @Override
    public void deletePublisherById(Long publisherId) {
        Optional<Publisher> optionalPublisher = publisherRepository.findById(publisherId);

        if(optionalPublisher.isEmpty()) {
            throw new NotFoundPublisherException(publisherId);
        }

        publisherRepository.deleteById(publisherId);
    }
}
