package uo.ri.cws.application.service.contract.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteContract implements Command<Void> {

    private String id;
    private ContractRepository contract_repo = Factories.repository
        .forContract();

    public DeleteContract(String id) {
        ArgumentChecks.isNotEmpty(id);
        ArgumentChecks.isNotBlank(id);
        this.id = id;
    }

    @Override
    public Void execute() throws BusinessException {

        Optional<Contract> optionalContract = contract_repo.findById(id);
        BusinessChecks.exists(optionalContract);
        Contract contract = optionalContract.get();
        BusinessChecks.isTrue(contract.getMechanic().getAssigned().isEmpty());
        BusinessChecks
            .isTrue(contract.getMechanic().getInterventions().isEmpty());
        BusinessChecks.isTrue(contract.getPayrolls().isEmpty());
        contract_repo.remove(optionalContract.get());

        return null;
    }

}
