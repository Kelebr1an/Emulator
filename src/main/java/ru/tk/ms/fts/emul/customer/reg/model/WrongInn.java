package ru.tk.ms.fts.emul.customer.reg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "WRONG_INN")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrongInn {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "INN")
    private String inn;

}
