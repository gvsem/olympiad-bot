package org.gvsem.olympiadbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "olympiads")
public class Olympiad {

    @Id
    @Getter @Setter
    private Long id;

    @Getter @Setter
    @NotNull @NotBlank
    @Column(name="title")
    private String title;

    @Getter @Setter
    @NotNull
    @Column(name="page_url")
    private String pageUrl;

    @Getter @Setter
    @OneToMany(mappedBy = "olympiad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Track> tracks = new ArrayList<>();

}
