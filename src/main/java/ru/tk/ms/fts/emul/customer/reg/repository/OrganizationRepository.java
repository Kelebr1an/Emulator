package ru.tk.ms.fts.emul.customer.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tk.ms.fts.emul.customer.reg.model.Organization;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    List<Organization> findOrganizationsByInn(String inn);

}
