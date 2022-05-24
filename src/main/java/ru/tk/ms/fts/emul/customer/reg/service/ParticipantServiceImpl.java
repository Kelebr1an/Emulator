package ru.tk.ms.fts.emul.customer.reg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tk.ms.fts.emul.customer.reg.exception.EntityNotFoundException;
import ru.tk.ms.fts.emul.customer.reg.model.Organization;
import ru.tk.ms.fts.emul.customer.reg.model.Participant;
import ru.tk.ms.fts.emul.customer.reg.repository.ParticipantRepository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Override
    public Participant insertParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    public Participant insertParticipant(Organization organization) {
        var participant = new Participant();
        var secureRandom = new SecureRandom();

        String random = IntStream.generate(() -> secureRandom.nextInt(10))
                .limit(15)
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append)
                .toString();

        participant.setParticipantId(random);

        participant.setOrganization(organization);

        return participantRepository.save(participant);
    }

    @Override
    public List<Participant> getAllParticipant() {
        return new ArrayList<>(participantRepository.findAll());
    }

    @Override
    public Participant getParticipantByID(UUID id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public void updateParticipant(UUID id, Participant participant) {
        var participantFromDB = participantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        participantFromDB.setParticipantId(participant.getParticipantId());
        participantFromDB.setOrganization(participant.getOrganization());

        participantRepository.save(participantFromDB);
    }

    @Override
    public void deleteParticipant(UUID id) {
        participantRepository.deleteById(id);
    }

}
