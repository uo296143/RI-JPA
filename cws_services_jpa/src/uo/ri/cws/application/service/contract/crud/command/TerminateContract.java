package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class TerminateContract implements Command<Void> {

    private String id;
    private ContractRepository contract_repo = Factories.repository
        .forContract();

    public TerminateContract(String id) {
        ArgumentChecks.isNotEmpty(id);
        ArgumentChecks.isNotBlank(id);
        this.id = id;
    }

    @Override
    public Void execute() throws BusinessException {

        Optional<Contract> optionalContract = contract_repo.findById(id);
        BusinessChecks.exists(optionalContract, "The contract doesn´t exist");
        Contract contract = optionalContract.get();
        BusinessChecks.isTrue(
                contract.getState().equals(ContractState.IN_FORCE),
                "The contract is already terminated");

        contract.terminate(LocalDate.now());

        return null;
    }

}
