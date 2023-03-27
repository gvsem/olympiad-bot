package org.gvsem.olympiadbot.repository;

import org.gvsem.olympiadbot.model.Olympiad;
import org.gvsem.olympiadbot.model.Track;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    @Query(value = "SELECT DISTINCT profile FROM tracks t", nativeQuery = true)
    List<String> getDistinctProfiles();

    List<Track> getTracksByProfile(String profile, Pageable pageable);

    List<Track> getTracksByOlympiad(Olympiad olympiad);

}
