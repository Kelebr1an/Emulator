package ru.tk.ms.fts.emul.customer.reg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import ru.tk.ms.fts.emul.customer.reg.exception.ElementNotFoundException;
import ru.tk.ms.fts.emul.customer.reg.exception.EntityNotFoundException;
import ru.tk.ms.fts.emul.customer.reg.model.MQMessage;
import ru.tk.ms.fts.emul.customer.reg.model.Organization;
import ru.tk.ms.fts.emul.customer.reg.repository.OrganizationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final MQMessageService mqMessageService;

    @Override
    public Organization insertOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization insertOrganization(Document document) {
        var organization = new Organization();

        organization.setInn(
                Optional.ofNullable(
                        mqMessageService.getMessageElement(
                                document,
                                MQMessage.MessageElements.INN.getElement()
                        )
                ).orElseThrow(() ->
                        new ElementNotFoundException(MQMessage.MessageElements.INN.getElement())));

        organization.setTitle(
                Optional.ofNullable(
                        mqMessageService.getMessageElement(
                                document,
                                MQMessage.MessageElements.TITLE.getElement()
                        )
                ).orElseThrow(() ->
                        new ElementNotFoundException(MQMessage.MessageElements.TITLE.getElement())));

        organization.setAddress(
                mqMessageService.getMessageElement(
                        document,
                        MQMessage.MessageElements.ADDRESS.getElement()));

        organization.setOgrn(
                mqMessageService.getMessageElement(
                        document,
                        MQMessage.MessageElements.OGRN.getElement()));

        organization.setDate(java.time.LocalDate.now());

        return organizationRepository.save(organization);
    }

    @Override

    public List<Organization> getAllOrganization() {
        return new ArrayList<>(organizationRepository.findAll());
    }

    @Override
    public Organization getOrganizationByID(UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public Organization getOrganizationByInn(String inn) {
        var organizations = organizationRepository.findOrganizationsByInn(inn);
        return organizations.stream().findFirst().orElse(null);
    }

    @Override
    public void updateOrganization(UUID id, Organization organization) {
        var organizationFromDB = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        organizationFromDB.setInn(organization.getInn());
        organizationFromDB.setTitle(organization.getTitle());
        organizationFromDB.setAddress(organization.getAddress());
        organizationFromDB.setOgrn(organization.getOgrn());
        organizationFromDB.setDate(organization.getDate());

        organizationRepository.save(organizationFromDB);
    }

    @Override
    public void deleteOrganization(UUID id) {
        organizationRepository.deleteById(id);
    }

}
