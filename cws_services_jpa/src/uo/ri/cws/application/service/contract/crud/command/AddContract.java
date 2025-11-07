package uo.ri.cws.application.service.contract.crud.command;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.application.repository.ContractTypeRepository;
import uo.ri.cws.application.repository.MechanicRepository;
import uo.ri.cws.application.repository.PayrollRepository;
import uo.ri.cws.application.repository.ProfessionalGroupRepository;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.ContractType;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.ProfessionalGroup;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.date.Dates;
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
    private PayrollRepository payroll_group_repo = Factories.repository
        .forPayroll();

    public AddContract(ContractDto dto) {
        ArgumentChecks.isNotNull(dto);
        ArgumentChecks.isNotEmpty(dto.mechanic.nif);
        ArgumentChecks.isNotBlank(dto.mechanic.nif);
        ArgumentChecks.isNotEmpty(dto.contractType.name);
        ArgumentChecks.isNotBlank(dto.contractType.name);
        ArgumentChecks.isNotEmpty(dto.professionalGroup.name);
        ArgumentChecks.isNotBlank(dto.professionalGroup.name);
        ArgumentChecks.isNotNull(dto.annualBaseSalary);
        ArgumentChecks.isTrue(dto.annualBaseSalary > 0);
        ArgumentChecks.isFalse(dto.contractType.name.equals("FIXED_TERM")
                && dto.endDate == null);

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

        BusinessChecks.exists(optionalContractType);
        BusinessChecks.exists(optionalProfessionalGroup);
        BusinessChecks.exists(optionalMechanic);

        // Comprobación de fechas si es contrato es FIXED_TERM
        if (dto.endDate != null) {
            BusinessChecks.isTrue(dto.startDate.isBefore(dto.endDate));
        }

        ContractType contractType = optionalContractType.get();
        ProfessionalGroup professionalGroup = optionalProfessionalGroup.get();
        Mechanic mechanic = optionalMechanic.get();

        // 1 - Compruebo si ya hay un contrato en vigor para este mecánico
        Optional<Contract> optional_contract = contract_repo
            .findContractInForceByMechanicId(mechanic.getId());
        if (optional_contract.isPresent()) {
            Contract past_contract = optional_contract.get();

            contract_repo.terminateContract(past_contract.getId());

            // 2 - Compruebo si han pasado más de 365 días para calcular el
            // finiquito
            // PROBLEMA -> endDate es null ya que se inserta en esta misma
            // transacción por lo tanto el valor aún no es visible

            long days_between_star_and_end = ChronoUnit.DAYS.between(
                    past_contract.getStartDate(),
                    Dates.lastDayOfCurrentMonth());

            if (days_between_star_and_end >= 365) {

                double settlement = computeSettlement(past_contract,
                        days_between_star_and_end);
                contract_repo.addSettlement(settlement, past_contract.getId());

            }
        }

        Contract contract = new Contract(mechanic, contractType,
                professionalGroup, dto.annualBaseSalary);

        contract_repo.add(contract);

        return DtoAssembler.toDto(contract);

    }

    /*
     * Calcula el finiquito para los mecánicos cuyo contrato se ha acabado y ha
     * estado en vigor un año o más
     */
    private double computeSettlement(Contract past_contract,
            long days_between_star_and_end) {

        // Fecha desde la que se inicia a contar la media del salario medio
        // bruto
        LocalDate dateOneYearAgo = past_contract.getEndDate()
            .minus(12, ChronoUnit.MONTHS);

        double gross_salary = payroll_group_repo.grossSalaryOfTheLastYear(
                past_contract.getId(), dateOneYearAgo,
                past_contract.getEndDate());
        double compensationDaysPerYear = past_contract.getContractType()
            .getCompensationDaysPerYear();
        int full_years_of_contract = (int) (days_between_star_and_end / 365);
        double settlement = gross_salary * compensationDaysPerYear
                * full_years_of_contract;
        return settlement;
    }

}
