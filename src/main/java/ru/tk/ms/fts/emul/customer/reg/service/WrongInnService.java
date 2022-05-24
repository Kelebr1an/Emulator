package ru.tk.ms.fts.emul.customer.reg.service;

import ru.tk.ms.fts.emul.customer.reg.model.WrongInn;

import java.util.List;
import java.util.UUID;

public interface WrongInnService {

    WrongInn insertWrongInn(WrongInn wrongInn);

    List<WrongInn> getAllWrongInn();

    WrongInn getWrongInnByID(UUID id);

    WrongInn getWrongInnByInn(String inn);

    void updateWrongInn(UUID id, WrongInn wrongInn);

    void deleteWrongInn(UUID id);

}
