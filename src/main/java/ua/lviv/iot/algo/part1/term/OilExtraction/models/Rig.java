package ua.lviv.iot.algo.part1.term.OilExtraction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Rig implements Entity {

    private static final String HEADERS = "id;longitude;latitude";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private double longitude;
    private double latitude;

    @JsonIgnoreProperties("rig")
    @OneToMany(mappedBy = "rig", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Tanker> tankers = new HashSet<>();

    @JsonIgnore
    public final String getHeaders() {
        return HEADERS;
    }

    public final String toCSV() {
        return id + ";" + longitude + ";" + latitude;
    }
}
