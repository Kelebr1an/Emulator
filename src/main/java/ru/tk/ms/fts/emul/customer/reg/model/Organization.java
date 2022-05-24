package ru.tk.ms.fts.emul.customer.reg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ORGANIZATION")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "INN")
    private String inn;

    @Column(name = "OGRN")
    private String ogrn;

    @Column(name = "ADDRESS")
    private String address;

    @OneToOne(mappedBy = "organization")
    private Participant participant;

    @Column(name = "DATE_OF_CREATE")
    private LocalDate date;

}
