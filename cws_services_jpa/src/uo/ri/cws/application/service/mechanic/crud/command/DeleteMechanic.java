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
        BusinessChecks.exists(optional_m,
                "The mechanic doesn´t exist so you can´t delete it");
        Mechanic m = optional_m.get();
        BusinessChecks.isTrue(m.getInterventions().isEmpty(),
                "You can´t delete the mechanic because it has interventions");
        BusinessChecks.isTrue(m.getAssigned().isEmpty(),
                "You can´t delete the mechanic because it has work orders");
        BusinessChecks.isTrue(m.getContracts().isEmpty(),
                "You can´t delete the mechanic because it has contracts");

        mechanic_repo.remove(m);
        return null;

    }

}
