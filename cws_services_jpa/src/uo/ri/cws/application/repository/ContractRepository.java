package uo.ri.cws.application.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.domain.Contract;

public interface ContractRepository extends Repository<Contract> {

    /**
     * @return a list with all contracts (might be empty)
     */
    List<Contract> findAll();

    /**
     * @return a list with all contracts in force (might be empty)
     */
    List<Contract> findAllInForce();

    List<Contract> findByMechanicId(String id);

    List<Contract> findByProfessionalGroupId(String id);

    List<Contract> findByContractTypeId(String id2Del);

    List<Contract> findAllInForceThisMonth(LocalDate present);

    List<Contract> findInforceContracts();

    /**
     * @return the contract in force of the mechanic if it exists
     */
    Optional<Contract> findContractInForceByMechanicId(String id);

    /**
     * Change the state and the endDate of the contract at the moment in force
     */
    void terminateContract(String id);

    /**
     * Update the settlement value when the contract is finished
     * 
     * @param settlement
     * @param id
     */
    void addSettlement(double settlement, String id);

}
