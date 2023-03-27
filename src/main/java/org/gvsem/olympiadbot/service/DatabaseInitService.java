package org.gvsem.olympiadbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.gvsem.olympiadbot.model.Olympiad;
import org.gvsem.olympiadbot.model.Track;
import org.gvsem.olympiadbot.repository.OlympiadRepository;
import org.gvsem.olympiadbot.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


@Component
public class DatabaseInitService implements ApplicationRunner {

    protected final OlympiadRepository olympiadRepository;
    protected final TrackRepository trackRepository;

    @Autowired
    public DatabaseInitService(OlympiadRepository olympiadRepository, TrackRepository trackRepository) {
        this.olympiadRepository = olympiadRepository;
        this.trackRepository = trackRepository;
    }

    public synchronized void fillIfEmpty() {
        if (olympiadRepository.findAll(PageRequest.of(0, 10, Sort.by("title"))).toList().isEmpty()) {

            ObjectMapper mapper = new ObjectMapper();

            try {
                JsonNode root = mapper.readTree(new File("./src/main/resources/2023.json"));
                assert(root instanceof ArrayNode);
                for (var olympiad : root.get("olympiads")) {
                    Olympiad o = new Olympiad();
                    o.setId(olympiad.get("no").asLong());
                    o.setTitle(olympiad.get("name").asText());
                    o.setPageUrl(olympiad.get("site").asText());
                    assert(olympiad.get("tracks") instanceof ArrayNode);
                    for (var track : olympiad.get("tracks")) {
                        Track t = new Track();
                        t.setProfile(track.get("profile").asText());
                        t.setLevel(track.get("level").asInt());
                        t.setOlympiad(o);
                        assert(track.get("subjects") instanceof ArrayNode);
                        for (var subject : track.get("subjects")) {
                            t.getSubjects().add(subject.asText());
                        }
                        o.getTracks().add(t);
                    }
                    olympiadRepository.saveAndFlush(o);
                }
            } catch (IOException | AssertionError e) {
               throw new RuntimeException(e);
            }

        }
    }

    public void run(ApplicationArguments args) {
        fillIfEmpty();
    }

}
