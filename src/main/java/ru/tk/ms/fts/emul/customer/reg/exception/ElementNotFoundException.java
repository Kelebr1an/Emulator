package ru.tk.ms.fts.emul.customer.reg.exception;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(String elementName) {
        super(String.format("Элемент с названием '%s' не найден", elementName));
    }

}
