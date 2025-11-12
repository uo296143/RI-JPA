package uo.ri.cws.application.service.contracttype.crud.command;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.cws.application.service.contracttype.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContractType implements Command<ContractTypeDto> {

    private ContractTypeDto dto;
    private ContractTypeRepository contract_type_repo = Factories.repository
        .forContractType();

    public AddContractType(ContractTypeDto dto) {
        ArgumentChecks.isNotNull(dto);
        ArgumentChecks.isNotEmpty(dto.name);
        ArgumentChecks.isNotBlank(dto.name);
        ArgumentChecks.isTrue(dto.compensationDays > 0);
        this.dto = dto;
    }

    @Override
    public ContractTypeDto execute() throws BusinessException {

        BusinessChecks.doesNotExist(contract_type_repo.findByName(dto.name));
        ContractType contractType = new ContractType(dto.name,
                dto.compensationDays);
        contract_type_repo.add(contractType);
        return DtoAssembler.toDto(contractType);

    }

}
