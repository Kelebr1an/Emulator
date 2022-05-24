package ru.tk.ms.fts.emul.customer.reg.service;

import ru.tk.ms.fts.emul.customer.reg.model.ResultInfo;

import java.util.List;
import java.util.UUID;

public interface ResultInfoService {

    ResultInfo insertResultInfo(ResultInfo resultInfo);

    List<ResultInfo> getAllResultInfo();

    ResultInfo getResultInfoByID(UUID id);

    void updateResultInfo(UUID id, ResultInfo resultInfo);

    void deleteResultInfo(UUID id);

}
