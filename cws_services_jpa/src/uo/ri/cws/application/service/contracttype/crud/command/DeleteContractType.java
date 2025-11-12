package uo.ri.cws.application.service.contracttype.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.ContractType;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteContractType implements Command<Void> {

    private String name;
    private ContractTypeRepository contract_type_repo = Factories.repository
        .forContractType();

    public DeleteContractType(String name) {
        ArgumentChecks.isNotBlank(name);
        ArgumentChecks.isNotEmpty(name);
        this.name = name;
    }

    @Override
    public Void execute() throws BusinessException {

        Optional<ContractType> optionalContractType = contract_type_repo
            .findByName(name);
        BusinessChecks.exists(optionalContractType);
        ContractType contractType = optionalContractType.get();
        BusinessChecks.isTrue(contractType.getContracts().isEmpty());
        contract_type_repo.remove(contractType);

        return null;
    }

}
