package ru.tk.ms.fts.emul.customer.reg.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(UUID id) {
        super(String.format("Entity with ID %s not found", id.toString()));
    }

}
