package ru.tk.ms.fts.emul.customer.reg.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "result_info")
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultInfo {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

}
