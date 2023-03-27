package org.gvsem.olympiadbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue
    @Getter @Setter
    private Long id;

    @Getter @Setter
    @Column(name="profile")
    private String profile;

    @Getter @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> subjects = new ArrayList<>();

    @Getter @Setter
    @Column(name="level")
    private Integer level;

    @ManyToOne(optional = false)
    @Getter @Setter
    private Olympiad olympiad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return id.equals(track.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
