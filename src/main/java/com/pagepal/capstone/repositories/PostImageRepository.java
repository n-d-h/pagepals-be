package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostImageRepository extends JpaRepository<PostImage, UUID> {
}
