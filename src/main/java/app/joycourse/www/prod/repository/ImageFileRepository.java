package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    Optional<ImageFile> findByHashedName(String hashedName);
}
