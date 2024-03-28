package com.nhnacademy.shop.tag.dto;

import com.nhnacademy.shop.tag.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateTagResponseDto
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetTagResponseDto {
    private Long tagId;
    private String tagName;

    public GetTagResponseDto(Tag tag) {
        this.tagId = tag.getTagId();
        this.tagName = tag.getTagName();
    }
}
