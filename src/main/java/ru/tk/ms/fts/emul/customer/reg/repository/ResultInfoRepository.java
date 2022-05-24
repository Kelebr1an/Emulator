package ru.tk.ms.fts.emul.customer.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tk.ms.fts.emul.customer.reg.model.ResultInfo;

import java.util.UUID;

@Repository
public interface ResultInfoRepository extends JpaRepository<ResultInfo, UUID> {
}
