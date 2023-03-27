package org.gvsem.olympiadbot.utils;

import org.gvsem.olympiadbot.model.Track;

import java.util.Comparator;
import java.util.Objects;

public class TrackLevelComparator implements Comparator<Track> {
    @Override
    public int compare(Track o1, Track o2) {
        if (Objects.equals(o1.getLevel(), o2.getLevel())) {
            return o1.getProfile().compareTo(o2.getProfile());
        }
        return o1.getLevel().compareTo(o2.getLevel());
    }
}
