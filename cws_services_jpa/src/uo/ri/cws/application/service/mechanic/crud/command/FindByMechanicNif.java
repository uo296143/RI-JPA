package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindByMechanicNif implements Command<List<ContractSummaryDto>> {

    private String nif;
    private MechanicRepository mechanic_repo = Factories.repository
        .forMechanic();
    private ContractRepository contract_repo = Factories.repository
        .forContract();

    public FindByMechanicNif(String nif) {
        ArgumentChecks.isNotBlank(nif);
        ArgumentChecks.isNotEmpty(nif);
        this.nif = nif;
    }

    @Override
    public List<ContractSummaryDto> execute() throws BusinessException {

        Optional<Mechanic> optional_mechanic = mechanic_repo.findByNif(nif);
        if (optional_mechanic.isEmpty()) {
            return new ArrayList<ContractSummaryDto>();
        }
        Mechanic mechanic = optional_mechanic.get();
        List<Contract> contracts = contract_repo
            .findByMechanicId(mechanic.getId());

        return DtoAssembler.toContractSummaryDtoList(contracts);

    }

}
