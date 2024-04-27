package com.nhnacademy.shop.wrap.controller;


import com.nhnacademy.shop.wrap.dto.request.ModifyWrapRequestDto;
import com.nhnacademy.shop.wrap.dto.request.WrapRequestDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDto;
import com.nhnacademy.shop.wrap.dto.response.WrapResponseDtoList;
import com.nhnacademy.shop.wrap.exception.AlreadyExistWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapException;
import com.nhnacademy.shop.wrap.exception.NotFoundWrapNameException;
import com.nhnacademy.shop.wrap.service.WrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 포장(Wrap) RestController 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-29
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class WrapController {
    private final WrapService wrapService;


    /**
     * 포장 전체 조회 요청 시 사용되는 메소드입니다.
     *
     * @return 성공했을 때 응답코드 200 OK 반환하고 body에 WrapResponseDto list.
     */
    @GetMapping("/wraps")
    public ResponseEntity<WrapResponseDtoList> getWraps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<WrapResponseDto> wrapPage = wrapService.getWraps(pageable);
        WrapResponseDtoList wrapResponseDtoList = WrapResponseDtoList.builder()
                .wrapResponseDtoList(wrapPage.getContent())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(wrapResponseDtoList);
    }


    /**
     * 포장 아이디로 포장 단건 조회 요청 시 사용되는 메소드입니다.
     *
     * @param wrapId 조회를 위한 해당 포장 아이디 입니다.
     * @throws NotFoundWrapException 포장id로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/wraps/id/{wrapId}")
    public ResponseEntity<WrapResponseDto> getWrapById(@PathVariable Long wrapId){
        try{
            WrapResponseDto wrapResponseDto = wrapService.getWrapById(wrapId);
            return ResponseEntity.ok().body(wrapResponseDto);
        }catch (NotFoundWrapException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 포장 이름로 포장 단건 조회 요청 시 사용되는 메소드입니다.
     *
     * @param wrapName 조회를 위한 해당 포장 이름 입니다.
     * @throws NotFoundWrapNameException 포장이름으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/wraps/name/{wrapName}")
    public ResponseEntity<WrapResponseDto> getWrapByName(@PathVariable String wrapName){
        try{
            WrapResponseDto wrapResponseDto = wrapService.getWrapByName(wrapName);
            return ResponseEntity.ok().body(wrapResponseDto);
        }catch (NotFoundWrapNameException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 포장 셍상 요청 시 사용되는 메소드입니다.
     *
     * @param wrapRequestDto 생성을 위한 해당 정보 입니다.
     * @throws AlreadyExistWrapException 이미 포장이 존재할때 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 201 CREATE 반환합니다.
     */
    @PostMapping("/wraps")
    public ResponseEntity<WrapResponseDto> saveWrap(@RequestBody WrapRequestDto wrapRequestDto){
        try{
            WrapResponseDto wrapResponseDto = wrapService.saveWrap(wrapRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(wrapResponseDto);
        }catch (AlreadyExistWrapException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 포장 수정 요청 시 사용되는 메소드입니다.
     *
     * @param modifyWrapRequestDto 수정를 위한 해당 정보 입니다.
     * @throws NotFoundWrapException 포장id로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @PutMapping("/wraps/")
    public ResponseEntity<WrapResponseDto> modifyWrap(@RequestBody ModifyWrapRequestDto modifyWrapRequestDto){
        try{
            WrapResponseDto wrapResponseDto = wrapService.modifyWrap(modifyWrapRequestDto);
            return ResponseEntity.ok().body(wrapResponseDto);
        }catch (NotFoundWrapException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 포장 아이디로 삭제 요청 시 사용되는 메소드입니다.
     *
     * @param wrapId 포장 아이디 입니다.
     * @throws NotFoundWrapException 포장id로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @DeleteMapping("/wraps/id/{wrapId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long wrapId){
        try {
            wrapService.deleteWrapById(wrapId);
            return ResponseEntity.ok().build();
        }catch (NotFoundWrapException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 포장 이름으로 삭제 요청 시 사용되는 메소드입니다.
     *
     * @param wrapName 포장 아이디 입니다.
     * @throws NotFoundWrapNameException 포장이름으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @DeleteMapping("/wraps/name/{wrapName}")
    public ResponseEntity<Void> deleteByName(@PathVariable String wrapName){
        try {
            wrapService.deleteWrapByName(wrapName);
            return ResponseEntity.ok().build();
        }catch (NotFoundWrapNameException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
