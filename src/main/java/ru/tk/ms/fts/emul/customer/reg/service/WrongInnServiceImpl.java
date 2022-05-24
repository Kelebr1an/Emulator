package ru.tk.ms.fts.emul.customer.reg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tk.ms.fts.emul.customer.reg.exception.EntityNotFoundException;
import ru.tk.ms.fts.emul.customer.reg.model.WrongInn;
import ru.tk.ms.fts.emul.customer.reg.repository.WrongInnRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WrongInnServiceImpl implements WrongInnService {

    private final WrongInnRepository wrongInnRepository;

    @Override
    public WrongInn insertWrongInn(WrongInn wrongInn) {
        return wrongInnRepository.save(wrongInn);
    }

    @Override
    public List<WrongInn> getAllWrongInn() {
        return new ArrayList<>(wrongInnRepository.findAll());
    }

    @Override
    public WrongInn getWrongInnByID(UUID id) {
        return wrongInnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }


    @Override
    public WrongInn getWrongInnByInn(String inn) {
        return wrongInnRepository.findWrongInnByInn(inn);
    }

    @Override
    public void updateWrongInn(UUID id, WrongInn wrongInn) {
        var wrongInnFromDB = wrongInnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        wrongInnFromDB.setInn(wrongInn.getInn());

        wrongInnRepository.save(wrongInnFromDB);
    }

    @Override
    public void deleteWrongInn(UUID id) {
        wrongInnRepository.deleteById(id);
    }

}
