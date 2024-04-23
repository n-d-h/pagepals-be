package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SettingRepository extends JpaRepository<Setting, UUID> {

    List<Setting> findByKeyIn(List<String> key);

    Optional<Setting> findByKey(String key);
}
