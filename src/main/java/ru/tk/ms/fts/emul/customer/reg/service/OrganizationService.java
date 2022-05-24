package ru.tk.ms.fts.emul.customer.reg.service;

import org.w3c.dom.Document;
import ru.tk.ms.fts.emul.customer.reg.model.Organization;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    Organization insertOrganization(Organization organization);

    Organization insertOrganization(Document document);

    List<Organization> getAllOrganization();

    Organization getOrganizationByID(UUID id);

    Organization getOrganizationByInn(String inn);

    void updateOrganization(UUID id, Organization organization);

    void deleteOrganization(UUID id);

}
