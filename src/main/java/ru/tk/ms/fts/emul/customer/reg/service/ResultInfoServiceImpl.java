package ru.tk.ms.fts.emul.customer.reg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tk.ms.fts.emul.customer.reg.exception.EntityNotFoundException;
import ru.tk.ms.fts.emul.customer.reg.model.ResultInfo;
import ru.tk.ms.fts.emul.customer.reg.repository.ResultInfoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultInfoServiceImpl implements ResultInfoService {

    private final ResultInfoRepository resultInfoRepository;

    @Override
    public ResultInfo insertResultInfo(ResultInfo resultInfo) {
        return resultInfoRepository.save(resultInfo);
    }

    @Override
    public List<ResultInfo> getAllResultInfo() {
        return new ArrayList<>(resultInfoRepository.findAll());
    }

    @Override
    public ResultInfo getResultInfoByID(UUID id) {
        return resultInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public void updateResultInfo(UUID id, ResultInfo resultInfo) {
        var resultInfoFromDB = resultInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        resultInfoFromDB.setDescription(resultInfo.getDescription());

        resultInfoRepository.save(resultInfoFromDB);
    }

    @Override
    public void deleteResultInfo(UUID id) {
        resultInfoRepository.deleteById(id);
    }

}
