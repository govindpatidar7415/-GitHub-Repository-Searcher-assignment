package com.github.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Model.GithubRepository;

@Repository
public interface GithubRepositoryRepository extends JpaRepository<GithubRepository, Long> {
}
	