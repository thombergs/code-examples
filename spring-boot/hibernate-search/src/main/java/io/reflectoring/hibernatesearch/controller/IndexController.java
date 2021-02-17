package io.reflectoring.hibernatesearch.controller;

import io.reflectoring.hibernatesearch.controller.dto.PostResponse;
import io.reflectoring.hibernatesearch.controller.dto.UserResponse;
import io.reflectoring.hibernatesearch.controller.mapper.PostMapper;
import io.reflectoring.hibernatesearch.controller.mapper.UserMapper;
import io.reflectoring.hibernatesearch.service.IndexingService;
import io.reflectoring.hibernatesearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
class IndexController {
    private final IndexingService indexingService;
    private final SearchService searchService;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @PostMapping("/reindex")
    public void reindex() throws InterruptedException {
        indexingService.initiateIndexing();
    }

    @GetMapping("/user")
    public List<UserResponse> getUser(@RequestParam String first,
                                      @RequestParam Integer max,
                                      @RequestParam Integer page){
        return searchService.getUserByFirst(first, max, page)
                .stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/user/projection")
    public List<UserResponse> getProjectUser(@RequestParam String first,
                                             @RequestParam Integer max,
                                             @RequestParam Integer page){
        return searchService.getUserByFirstWithProjection(first, max, page)
                .stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/post")
    public List<PostResponse> getPost(@RequestParam Long likeCount,
                                      @RequestParam(required = false) String hashTags,
                                      @RequestParam(required = false) String tag){
        return searchService.getBasedOnLikeCountTags(likeCount, hashTags, tag)
                .stream().map(postMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/post/word")
    public List<PostResponse> getPostByWord(@RequestParam String word){
        return searchService.getPostBasedOnWord(word)
                .stream().map(postMapper::toResponse).collect(Collectors.toList());
    }

}
