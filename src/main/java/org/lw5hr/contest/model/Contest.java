package org.lw5hr.contest.model;


import javafx.scene.control.Button;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Diego - LW5HR
 */
@Entity
@Table(name = "CONTEST")
public class Contest implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    
    @Column(name = "contestName")
    private String contestName;

    @Column(name = "contestDescription")
    private String contestDescription;

    @OneToMany (mappedBy = "contest", cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<Qso> qsos;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getContestDescription() {
        return contestDescription;
    }

    public void setContestDescription(String contestDescription) {
        this.contestDescription = contestDescription;
    }

    public List<Qso> getQsos() {
        return qsos;
    }

    public void setQsos(List<Qso> qsos) {
        this.qsos = qsos;
    }
}
