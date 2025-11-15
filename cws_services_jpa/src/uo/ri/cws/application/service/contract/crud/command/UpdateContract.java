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

    /**
     * Update the information of the contract
     * 
     * About end date : If the contract type is FIXED_TERM we have to check that
     * the new end date is not earlier than the start date, if the contract type
     * is another one we don´t check the new end date is later star date. Anyway
     * when we finish a contract that is when we will use end date, the field
     * end date will be generated on the final day of the month when we
     * terminate the contract.
     */
    @Override
    public Void execute() throws BusinessException {

        Optional<Contract> optional_contract = contract_repo.findById(dto.id);
        BusinessChecks.exists(optional_contract, "The contract doesn´t exist");
        Contract contract = optional_contract.get();

        BusinessChecks.isTrue(
                contract.getState().equals(ContractState.IN_FORCE),
                "The contract is not in force");

        // If not we take the old one
        checkIfUserIntroduceAnEndDate(contract);

        if (contract.isFixedTerm()) {
            BusinessChecks.isTrue(dto.endDate.isAfter(contract.getStartDate()),
                    "End date can´t be earlier than start date");
        }

        BusinessChecks.hasVersion(dto.version, contract.getVersion(),
                "Problem with version");

        checkIfUserIntroduceAnEndDate(contract);

        contract.setEndDate(dto.endDate);

        contract.setAnnualBaseSalary(dto.annualBaseSalary);

        contract.updatedNow();

        return null;

    }

    /*
     * If user doesn´t introduce a new end date we will take the old one because
     * the contract could have already one
     * 
     * @param contract is the "old" information of the contract before the
     * update will ocurre
     */
    private void checkIfUserIntroduceAnEndDate(Contract contract) {
        if (dto.endDate == null) {
            dto.endDate = contract.getEndDate();
        }

    }

}
