package com.github.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.Service.GithubRepositoryService;

@RestController
@RequestMapping("/api/github")	
public class GithubRepositoryController {
    @Autowired
    private GithubRepositoryService service;

    @PostMapping("/search")
    public Map<String, Object> search(@RequestBody Map<String, String> req) {
        var repos = service.searchAndSaveRepos(req.get("query"), req.get("language"), req.getOrDefault("sort", "stars"));
        var resp = new HashMap<String, Object>();
        resp.put("message", "Repositories fetched and saved successfully");
        resp.put("repositories", repos);
        return resp;
    }

    @GetMapping("/repositories")
    public Map<String, Object> getRepos(
        @RequestParam(required = false) String language,
        @RequestParam(required = false) Integer minStars,
        @RequestParam(required = false, defaultValue = "stars") String sort) {
        var repos = service.getRepos(language, minStars, sort);
        var resp = new HashMap<String, Object>();
        resp.put("repositories", repos);
        return resp;
    }
}
