package com.nhnacademy.shop.review.controller;

import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.shop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.shop.review.dto.response.ReviewPageResponseDto;
import com.nhnacademy.shop.review.dto.response.ReviewResponseDto;
import com.nhnacademy.shop.review.exception.ReviewNotFoundException;
import com.nhnacademy.shop.review.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 리뷰(Review) RestController 입니다.
 *
 * @author : 강병구
 * @date : 2024-03-29
 */
@RestController
@RequestMapping("/shop")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 리뷰 단건 조회 요청 시 사용되는 메소드입니다.
     *
     * @param reviewId 조회를 위한 해당 리뷰 아이디 입니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     * @throws ResponseStatusException 리뷰를 찾을 수 없을 때 응답코드 404 NOT_FOUND 반환합니다.
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId) {
        ReviewResponseDto reviewResponseDto = reviewService.getReviewByReviewId(reviewId);
        if (Objects.isNull(reviewResponseDto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Review Not Found : %d", reviewId));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewResponseDto);
    }

    /**
     * 전체 리뷰 조회 요청 시 사용되는 메소드입니다.
     *
     * @param offset   페이지 구성을 위한 오프셋 입니다.
     * @param pageSize 페이지 구성을 위한 페이지 사이즈 입니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getReviews(@RequestParam Integer pageSize,
                                                              @RequestParam Integer offset) {
        Page<ReviewResponseDto> dtoList = reviewService.getReviews(pageSize, offset);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoList);
    }

    /**
     * 리뷰 목록 조회 요청 시 사용되는 메소드입니다.
     *
     * @param bookIsbn 조회를 위한 도서 고유 번호 입니다.
     * @param offset   페이지 구성을 위한 오프셋 입니다.
     * @param pageSize 페이지 구성을 위한 페이지 사이즈 입니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     * @throws ResponseStatusException 리뷰를 찾을 수 없을 때 응답코드 404 NOT_FOUND 반환합니다.
     */
    @GetMapping("/reviews/bookIsbn/{bookIsbn}")
    public ResponseEntity<ReviewPageResponseDto> getReviewsByBookIsbn(@PathVariable String bookIsbn,
                                                                        @RequestParam Integer offset,
                                                                        @RequestParam Integer pageSize) {
        try {
            Page<ReviewResponseDto> dtoList = reviewService.getReviewsByBookIsbn(bookIsbn, pageSize, offset);

            List<ReviewResponseDto> review = dtoList.getContent();

            ReviewPageResponseDto reviewPageResponseDto = ReviewPageResponseDto.builder()
                    .reviewList(review)
                    .build();

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reviewPageResponseDto);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found");
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviews Not Found");
        }
    }

    /**
     * 리뷰 목록 조회 요청 시 사용되는 메소드입니다.
     *
     * @param customerNo 조회를 위한 회원 번호 입니다.
     * @param offset     페이지 구성을 위한 오프셋 입니다.
     * @param pageSize   페이지 구성을 위한 페이지 사이즈 입니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     * @throws ResponseStatusException 리뷰를 찾을 수 없을 때 응답코드 404 NOT_FOUND 반환합니다.
     */
    @GetMapping("/reviews/customerNo/{customerNo}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByCustomerNo(@PathVariable Long customerNo,
                                                                          @RequestParam Integer offset,
                                                                          @RequestParam Integer pageSize) {
        try {
            Page<ReviewResponseDto> dtoList = reviewService.getReviewsByCustomerNo(customerNo, pageSize, offset);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dtoList);
        } catch (MemberNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Member Not Found : %d", customerNo));
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reviews Not Found");
        }
    }

    /**
     * 리뷰 생성 요청 시 사용되는 메소드 입니다..
     *
     * @param createReviewRequestDto 생성할 리뷰 정보를 담고 있는 dto 입니다.
     * @return 성공했을 때 응답코드 201 CREATED 반환합니다.
     */
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody @Valid CreateReviewRequestDto createReviewRequestDto) {
        ReviewResponseDto dto = reviewService.createReview(createReviewRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }

    /**
     * 리뷰 수정 요청 시 사용되는 메소드 입니다..
     *
     * @param modifyReviewRequestDto 수정할 리뷰 정보를 담고 있는 dto 입니다.
     * @return 성공했을 때 응답코드 201 CREATED 반환합니다.
     */
    @PutMapping("/reviews")
    public ResponseEntity<ReviewResponseDto> modifyReview(@RequestBody @Valid ModifyReviewRequestDto modifyReviewRequestDto) {
        ReviewResponseDto dto = reviewService.modifyReview(modifyReviewRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }

    /**
     * 리뷰 삭제 요청 시 사용되는 메소드 입니다..
     *
     * @param reviewId 삭제를 위한 해당 리뷰 아이디 입니다.
     * @return 성공했을 때 응답코드 201 CREATED 반환합니다.
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}
