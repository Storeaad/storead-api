package com.storead.article.exception

import com.storead.common.exception.ErrorMessage

enum class ArticleError(
    override val detail: String
) : ErrorMessage {

    ARTICLE_NOT_FOUND("찾을 수 없는 게시글입니다."),
    REQUIRE_AUTHOR_ID("게시글 작성자의 아이디 값이 비어있습니다."),
    HAS_NOT_OWNER("해당 게시글의 작성자가 아닙니다."),
    ARTICLE_VIEW_NOT_FIND("조회수가 생성 되지 않은 게시글입니다."),

}