package com.github.Service;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.Model.GithubRepository;
import com.github.Repository.GithubRepositoryRepository;

@Service
public class GithubRepositoryService {
    @Autowired
    private GithubRepositoryRepository repo;	

    public List<GithubRepository> searchAndSaveRepos(String query, String language, String sort) {
        String url = "https://api.github.com/search/repositories?q=" + query +
                     (language != null && !language.isEmpty() ? "+language:" + language : "") +
                     "&sort=" + sort + "&order=desc";
        RestTemplate rest = new RestTemplate();
        String response = rest.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);	
        JSONArray items = json.getJSONArray("items");
        List<GithubRepository> saved = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            GithubRepository repoEntity = GithubRepository.builder()		
                .id(item.getLong("id"))
                .name(item.getString("name"))
                .description(item.optString("description", ""))
                .owner(item.getJSONObject("owner").getString("login"))
                .language(item.optString("language", ""))
                .stars(item.getInt("stargazers_count"))
                .forks(item.getInt("forks_count"))
                .lastUpdated(ZonedDateTime.parse(item.getString("updated_at")))
                .build();
            // upsert
            repo.save(repoEntity);
            saved.add(repoEntity);
        }
        return saved;
    }

    public List<GithubRepository> getRepos(String language, Integer minStars, String sort) {
        List<GithubRepository> repos = repo.findAll();
        // basic manual filtering/sorting
        return repos.stream()
            .filter(r -> language == null || language.isEmpty() || language.equalsIgnoreCase(r.getLanguage()))
            .filter(r -> minStars == null || r.getStars() >= minStars)
            .sorted((a, b) -> {
                if ("forks".equals(sort)) return b.getForks().compareTo(a.getForks());
                if ("updated".equals(sort)) return b.getLastUpdated().compareTo(a.getLastUpdated());
                return b.getStars().compareTo(a.getStars());
            })
            .toList();
    }
}
