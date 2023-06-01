package ua.lviv.iot.algo.part1.term.OilExtraction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Tanker implements Entity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int capacityInBarrels;
    private int currentLoadInBarrels;
    private List<String> datesOfDeparture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("tankers")
    private Rig rig;

    @JsonIgnore
    private Integer rigId;

    @Transient
    public int getRigId() {
        return (rig != null) ? rig.getId() : 0;
    }
    @JsonIgnore
    public String getHeaders() {
        return "id;capacityInBarrels;currentLoadInBarrels;datesOfDeparture;rigId";
    }

    public String toCSV() {
        return id + ";"
                + capacityInBarrels + ";"
                + currentLoadInBarrels + ";"
                + datesOfDeparture + ";"
                + rigId;
    }

}
