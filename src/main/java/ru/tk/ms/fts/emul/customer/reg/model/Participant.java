package ru.tk.ms.fts.emul.customer.reg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PARTICIPANT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "PARTICIPANT_ID")
    private String participantId;

    @OneToOne
    @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID")
    private Organization organization;

}
