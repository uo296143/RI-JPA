package uo.ri.cws.application.service.mechanic.crud.command;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

    private MechanicDto dto;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto);
        ArgumentChecks.isNotBlank(dto.id);
        ArgumentChecks.isNotBlank(dto.name);
        ArgumentChecks.isNotBlank(dto.surname);
        ArgumentChecks.isNotNull(dto.nif);
        this.dto = dto;
    }

    public Void execute() throws BusinessException {

        Optional<Mechanic> optional_m = repo.findById(dto.id);
        BusinessChecks.exists(optional_m);
        Mechanic m = optional_m.get();
        BusinessChecks.hasVersion(dto.version, m.getVersion());
        m.setName(dto.name);
        m.setSurname(dto.surname);
        m.updatedNow();

        return null;
    }

}
