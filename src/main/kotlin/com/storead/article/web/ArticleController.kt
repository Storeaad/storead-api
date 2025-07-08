package com.storead.article.web

import com.storead.article.UserIdentifier
import com.storead.article.application.ArticleService
import com.storead.article.application.response.ArticleCreateServiceResponse
import com.storead.article.application.response.ArticlePageServiceResponse
import com.storead.article.application.response.ArticleUpdateServiceResponse
import com.storead.article.web.request.*
import com.storead.article.web.response.ArticleCreateResponse
import com.storead.article.web.response.ArticleDetailResponse
import com.storead.article.web.response.ArticlePageResponse
import com.storead.article.web.response.ArticleUpdateResponse
import com.storead.common.web.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/articles")
class ArticleController(
    val articleService: ArticleService
) {

    /**
     * 내가 작성한 게시글 조회
     *
     * @param userProfileId 사용자 프로필의 고유 식별자입니다.
     * @param request 페이지네이션 파라미터를 포함하는 요청 페이로드입니다.
     * @return 사용자의 아티클 목록을 페이지네이션하여 담은 ApiResponse를 포함하는 ResponseEntity입니다.
     */
    @GetMapping("me")
    fun getMyArticles(
        @UserIdentifier userProfileId: UUID,
        request: MyArticlesRequest
    ): ResponseEntity<ApiResponse<ArticlePageResponse>> {
        val myArticles: ArticlePageServiceResponse =
            articleService.getMyArticles(request.toServiceRequest(userProfileId))
        return ApiResponse.success(
            data = ArticlePageResponse.from(myArticles),
            message = "Successfully retrieved user's articles"
        )
    }

    /**
     * 모든 게시글 조회
     *
     * @param request limit 및 cursor와 같은 페이지네이션 파라미터를 포함하는 요청 페이로드입니다.
     * @return 아티클 목록을 페이지네이션하여 담은 ApiResponse를 포함하는 ResponseEntity입니다.
     */
    @PreAuthorize("permitAll()")
    @GetMapping
    fun getAllArticles(request: ArticlePageRequest): ResponseEntity<ApiResponse<ArticlePageResponse>> {
        val articles: ArticlePageServiceResponse = articleService.getAllArticles(request.toServiceRequest())
        return ApiResponse.success(
            data = ArticlePageResponse.from(articles),
            message = "Successfully retrieved articles"
        )
    }

    /**
     * 상세 게시글 조회
     *
     * @param request 가져올 아티클의 ID를 포함하는 요청 객체입니다.
     * @return 지정된 아티클의 상세 정보를 담은 ApiResponse를 포함하는 ResponseEntity입니다.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    fun getArticleDetail(request: ArticleDetailRequest): ResponseEntity<ApiResponse<ArticleDetailResponse>> {
        val serviceResponse = articleService.getArticleDetail(request.toServiceRequest())
        return ApiResponse.success(
            data = ArticleDetailResponse.from(serviceResponse),
            message = "Successfully retrieved article detail"
        )
    }

    /**
     * 게시글 작성
     *
     * @param userprofileId 사용자 프로필의 고유 식별자입니다.
     * @param request 아티클 생성에 필요한 정보(제목, 설명, 본문, 발행 상태, 연관된 책 ID, 썸네일 이미지 ID, 태그)를 포함하는 요청 페이로드입니다.
     * @return 생성된 아티클의 ID와 생성 타임스탬프를 담은 ApiResponse를 포함하는 ResponseEntity입니다.
     */
    @PostMapping
    fun createArticle(
        @UserIdentifier userprofileId: UUID,
        @RequestBody request: ArticleCreateRequest
    ): ResponseEntity<ApiResponse<ArticleCreateResponse>> {
        val article: ArticleCreateServiceResponse =
            articleService.createArticle(request.toServiceRequest(userprofileId))
        return ApiResponse.success(data = ArticleCreateResponse.from(article), message = "Successfully created article")
    }

    /**
     * 게시글 수정
     *
     * @param articleId 업데이트할 아티클의 고유 식별자입니다.
     * @param userprofileId 사용자 프로필의 고유 식별자입니다.
     * @param request 업데이트된 아티클 정보(제목, 설명, 본문, 발행 상태, 연관된 책 ID, 썸네일 이미지 파일, 태그)를 포함하는 요청 페이로드입니다.
     * @return 업데이트된 아티클 상세 정보와 성공 메시지를 담은 ApiResponse를 포함하는 ResponseEntity입니다.
     */
    @PatchMapping("/{articleId}")
    fun updateArticle(
        @PathVariable articleId: UUID,
        @UserIdentifier userprofileId: UUID,
        @RequestBody request: ArticleUpdateRequest
    ): ResponseEntity<ApiResponse<ArticleUpdateResponse>> {
        val articleResponse: ArticleUpdateServiceResponse =
            articleService.updateArticle(request.toServiceRequest(userprofileId, articleId))
        return ApiResponse.success(
            data = ArticleUpdateResponse.from(articleResponse),
            message = "Successfully updated article"
        )
    }

    /**
     * 게시글 삭제
     *
     * @param articleId 삭제할 아티클의 고유 식별자입니다.
     * @param userprofileId 사용자 프로필의 고유 식별자입니다.
     * @return 아티클이 삭제되었음을 나타내는 성공 메시지를 담은 ApiResponse를 포함하는 ResponseEntity입니다.
     */
    @DeleteMapping("/{articleId}")
    fun deleteArticle(
        @PathVariable articleId: UUID,
        @UserIdentifier userprofileId: UUID,
    ): ResponseEntity<ApiResponse<String>> {
        articleService.deleteArticle(request = ArticleDeleteRequest(articleId, userprofileId).toServiceRequest())
        return ApiResponse.success(data = null, message = "Successfully deleted article")
    }
}