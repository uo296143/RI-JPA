package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContract implements Command<ContractDto> {

    private ContractDto dto;
    private ContractRepository contract_repo = Factories.repository
        .forContract();
    private MechanicRepository mechanic_repo = Factories.repository
        .forMechanic();
    private ContractTypeRepository contract_type_repo = Factories.repository
        .forContractType();
    private ProfessionalGroupRepository professional_group_repo = Factories.repository
        .forProfessionalGroup();

    public AddContract(ContractDto dto) {
        ArgumentChecks.isNotNull(dto);
        ArgumentChecks.isNotEmpty(dto.mechanic.nif);
        ArgumentChecks.isNotBlank(dto.mechanic.nif);
        ArgumentChecks.isNotEmpty(dto.contractType.name);
        ArgumentChecks.isNotBlank(dto.contractType.name);
        ArgumentChecks.isNotEmpty(dto.professionalGroup.name);
        ArgumentChecks.isNotBlank(dto.professionalGroup.name);
        ArgumentChecks.isNotNull(dto.annualBaseSalary);
        ArgumentChecks.isTrue(dto.annualBaseSalary > 0,
                "Annual base salary must be greater than zero");
        ArgumentChecks.isFalse(dto.contractType.name.equals("FIXED_TERM")
                && dto.endDate == null);

        // Se establece la fecha de inicio del contrato la cuál será el primer
        // día del próximo mes
        dto.startDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        this.dto = dto;
    }

    @Override
    public ContractDto execute() throws BusinessException {

        Optional<ContractType> optionalContractType = contract_type_repo
            .findByName(dto.contractType.name);

        Optional<ProfessionalGroup> optionalProfessionalGroup = professional_group_repo
            .findByName(dto.professionalGroup.name);

        Optional<Mechanic> optionalMechanic = mechanic_repo
            .findByNif(dto.mechanic.nif);

        BusinessChecks.exists(optionalContractType,
                "The contract type doesn´t exist");
        BusinessChecks.exists(optionalProfessionalGroup,
                "The professional group doesn´t exist");
        BusinessChecks.exists(optionalMechanic, "The mechanic doesn´t exist");

        // Comprobación de fechas si es contrato es FIXED_TERM
        if (dto.endDate != null) {
            BusinessChecks.isTrue(dto.startDate.isBefore(dto.endDate),
                    "End date can´t be earlier start date");
        }

        ContractType contractType = optionalContractType.get();
        ProfessionalGroup professionalGroup = optionalProfessionalGroup.get();
        Mechanic mechanic = optionalMechanic.get();

        // Compruebo si ya hay un contrato en vigor para este mecánico
        Optional<Contract> optional_contract = mechanic.getContractInForce();

        if (optional_contract.isPresent()) {
            Contract past_contract = optional_contract.get();
            past_contract.terminate(LocalDate.now());
        }

        Contract contract = new Contract(mechanic, contractType,
                professionalGroup, dto.startDate, dto.endDate,
                dto.annualBaseSalary);

        contract_repo.add(contract);

        return DtoAssembler.toDto(contract);

    }

}
