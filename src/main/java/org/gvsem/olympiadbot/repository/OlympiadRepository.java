package org.gvsem.olympiadbot.repository;

import org.gvsem.olympiadbot.model.Olympiad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OlympiadRepository extends JpaRepository<Olympiad, Long> {

    //List<Olympiad> findAll(Pageable pageable);

    List<Olympiad> findByTitleContainsIgnoreCase(String title);

}
