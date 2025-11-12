package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateContract implements Command<Void> {

    private ContractDto dto;
    private ContractRepository contract_repo = Factories.repository
        .forContract();

    public UpdateContract(ContractDto dto) {
        ArgumentChecks.isNotNull(dto);
        ArgumentChecks.isNotEmpty(dto.id);
        ArgumentChecks.isNotBlank(dto.id);
        ArgumentChecks.isTrue(dto.annualBaseSalary > 0);

        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {

        Optional<Contract> optional_contract = contract_repo.findById(dto.id);
        BusinessChecks.exists(optional_contract);
        Contract contract = optional_contract.get();
        boolean isFixedTerm = contract.getContractType()
            .getName()
            .equals("FIXED_TERM");
        BusinessChecks
            .isTrue(contract.getState().equals(ContractState.IN_FORCE));
        BusinessChecks.isTrue(isFixedTerm
                && contract.getEndDate().isAfter(contract.getStartDate()));

        BusinessChecks.hasVersion(dto.version, contract.getVersion());

        if (isFixedTerm) {
            BusinessChecks
                .isFalse(dto.endDate.isBefore(contract.getStartDate()));
            contract.setEndDate(dto.endDate);
        }
        contract.setAnnualBaseSalary(dto.annualBaseSalary);

        return null;

    }

}
