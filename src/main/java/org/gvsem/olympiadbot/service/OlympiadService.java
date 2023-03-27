package org.gvsem.olympiadbot.service;

import org.gvsem.olympiadbot.model.Olympiad;
import org.gvsem.olympiadbot.model.Track;
import org.gvsem.olympiadbot.repository.OlympiadRepository;
import org.gvsem.olympiadbot.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OlympiadService {

    protected final OlympiadRepository olympiadRepository;

    protected final TrackRepository trackRepository;

    public OlympiadService(@Autowired OlympiadRepository olympiadRepository, @Autowired TrackRepository trackRepository) {
        this.olympiadRepository = olympiadRepository;
        this.trackRepository = trackRepository;
    }

    public Optional<Olympiad> getOlympiad(Long id) {
        return this.olympiadRepository.findById(id);
    }

    public List<Olympiad> getOlympiads(Integer page) {
        return olympiadRepository.findAll(PageRequest.of(page, 10, Sort.by("id"))).toList();
    }

    public List<Olympiad> getOlympiads(String profile, Integer page) {
        return trackRepository.getTracksByProfile(profile, PageRequest.of(page, 15, Sort.by("olympiad")))
                .stream().map(Track::getOlympiad).toList();
    }

    public List<Track> getTracks(Olympiad olympiad) {
        return trackRepository.getTracksByOlympiad(olympiad);
    }

}
