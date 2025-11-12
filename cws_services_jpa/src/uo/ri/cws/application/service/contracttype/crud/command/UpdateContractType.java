package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateContractType implements Command<Void> {

    private ContractTypeRepository contract_type_repo = Factories.repository
        .forContractType();
    private ContractTypeDto dto;

    public UpdateContractType(ContractTypeDto dto) {
        ArgumentChecks.isNotNull(dto);
        ArgumentChecks.isNotEmpty(dto.name);
        ArgumentChecks.isNotBlank(dto.name);
        ArgumentChecks.isTrue(dto.compensationDays > 0);
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {

        Optional<ContractType> optionalContractType = contract_type_repo
            .findByName(dto.name);
        BusinessChecks.exists(optionalContractType);
        ContractType contractType = optionalContractType.get();

        BusinessChecks.hasVersion(dto.version, contractType.getVersion());
        contractType.setCompensationDaysPerYear(dto.compensationDays);

        return null;
    }

}
