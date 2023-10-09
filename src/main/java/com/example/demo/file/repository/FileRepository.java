package com.example.demo.file.repository;

import com.example.demo.file.domain.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, String> {

    FileInfo save(FileInfo file);

//    Optional<FileInfo> findById()

}
