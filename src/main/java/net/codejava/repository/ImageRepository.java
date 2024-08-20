package net.codejava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.codejava.domain.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {}
