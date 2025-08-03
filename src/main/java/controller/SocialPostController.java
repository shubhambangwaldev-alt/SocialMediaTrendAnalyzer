package controller;

import model.SocialPost;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import service.SocialPostService;

import java.util.List;

/**
 * REST controller for managing social posts.
 */
@RestController
@RequestMapping("/api")
@Validated
public class SocialPostController {

    private final SocialPostService socialPostService;

    /**
     * Constructor for SocialPostController.
     * @param socialPostService the service to handle social post operations
     */
    @Autowired
    public SocialPostController(SocialPostService socialPostService) {
        this.socialPostService = socialPostService;
    }

    /**
     * Get all social posts, optionally paginated.
     * @return list of social posts
     */
    @GetMapping("/posts")
    public List<SocialPost> getAllPosts() {
        return socialPostService.getAllPosts();
    }

    /**
     * Fetch and save posts by keyword.
     * @param keyword fetch keyword
     */
    @PostMapping("/fetch")
    public void fetchAndSavePosts(@RequestParam("keyword") String keyword) {
        socialPostService.fetchAndSavePosts(keyword);
    }

    /**
     * Search posts by keyword.
     * @param q search query
     * @param pageable pagination information
     * @return list of matching social posts
     */
    @GetMapping("/search")
    public List<SocialPost> searchPosts(
            @RequestParam("q") String q,
            Pageable pageable) {
        return socialPostService.searchPosts(q, pageable);
    }
}