package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {

    private String mechanicId;
    private MechanicRepository mechanic_repo = Factories.repository
        .forMechanic();

    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotNull(mechanicId);
        this.mechanicId = mechanicId;
    }

    public Void execute() throws BusinessException {

        Optional<Mechanic> optional_m = mechanic_repo.findById(mechanicId);
        BusinessChecks.exists(optional_m);
        Mechanic m = optional_m.get();
        BusinessChecks.isTrue(m.getInterventions().isEmpty());
        BusinessChecks.isTrue(m.getAssigned().isEmpty());
        BusinessChecks.isTrue(m.getContracts().isEmpty());

        mechanic_repo.remove(m);
        return null;

    }

}
