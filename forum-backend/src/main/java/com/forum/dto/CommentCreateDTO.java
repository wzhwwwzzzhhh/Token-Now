package com.forum.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class CommentCreateDTO {
    @NotEmpty(message = "评论内容不能为空")
    private String content;
}
