package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractById implements Command<Optional<ContractDto>> {

    private String id;
    private ContractRepository contract_repo = Factories.repository
        .forContract();

    public FindContractById(String id) {
        ArgumentChecks.isNotBlank(id);
        ArgumentChecks.isNotEmpty(id);
        this.id = id;
    }

    @Override
    public Optional<ContractDto> execute() throws BusinessException {

        Optional<Contract> optional_contract = contract_repo.findById(id);
        if (optional_contract.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(DtoAssembler.toDto(optional_contract.get()));

    }

}
