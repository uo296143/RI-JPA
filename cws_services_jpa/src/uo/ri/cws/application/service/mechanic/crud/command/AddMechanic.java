package uo.ri.cws.application.service.mechanic.crud.command;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Mechanic;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

    private MechanicDto dto;
    private MechanicRepository repo = Factories.repository.forMechanic();

    public AddMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Cannot add null mechanic");
        ArgumentChecks.isNotBlank(dto.nif,
                "Cannot add mechanic with null or blank nif");
        ArgumentChecks.isNotBlank(dto.name,
                "Cannot add mechanic with null or blank name");
        ArgumentChecks.isNotBlank(dto.surname,
                "Cannot add mechanic with null or blank surname");

        this.dto = dto;
    }

    @Override
    public MechanicDto execute() throws BusinessException {

        Mechanic m = new Mechanic(dto.nif, dto.name, dto.surname);
        BusinessChecks.doesNotExist(repo.findByNif(dto.nif));
        repo.add(m);
        dto.id = m.getId();
        dto.version = m.getVersion();

        return dto;
    }

}
