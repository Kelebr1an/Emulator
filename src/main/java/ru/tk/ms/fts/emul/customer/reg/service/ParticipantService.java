package ru.tk.ms.fts.emul.customer.reg.service;

import ru.tk.ms.fts.emul.customer.reg.model.Organization;
import ru.tk.ms.fts.emul.customer.reg.model.Participant;

import java.util.List;
import java.util.UUID;

public interface ParticipantService {

    Participant insertParticipant(Participant participant);

    Participant insertParticipant(Organization organization);

    List<Participant> getAllParticipant();

    Participant getParticipantByID(UUID id);

    void updateParticipant(UUID id, Participant participant);

    void deleteParticipant(UUID id);

}
