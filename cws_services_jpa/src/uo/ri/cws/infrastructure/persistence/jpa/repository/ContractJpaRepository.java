package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.repository.ContractRepository;
import uo.ri.cws.domain.Contract;
import uo.ri.cws.domain.Contract.ContractState;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;
import uo.ri.util.date.Dates;

public class ContractJpaRepository extends BaseJpaRepository<Contract>
        implements ContractRepository {

//    @Override
//    public List<Contract> findAll() {
//
//        return Jpa.getManager()
//            .createNamedQuery("Contract.findAll", Contract.class)
//            .getResultList();
//
//    }

    @Override
    public List<Contract> findAllInForce() {

        return Jpa.getManager()
            .createNamedQuery("Contract.findAllInForce", Contract.class)
            .getResultList();

    }

    @Override
    public List<Contract> findByMechanicId(String id) {

        return Jpa.getManager()
            .createNamedQuery("Contract.findByMechanicId", Contract.class)
            .setParameter(1, id)
            .getResultList();

    }

    @Override
    public List<Contract> findByProfessionalGroupId(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Contract> findByContractTypeId(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Contract> findAllInForceThisMonth(LocalDate present) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Contract> findInforceContracts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Contract> findContractInForceByMechanicId(String id) {

        return Jpa.getManager()
            .createNamedQuery("Contract.findContractInForceByMechanicId",
                    Contract.class)
            .setParameter(1, id)
            .getResultStream()
            .findFirst();
    }

    @Override
    public void terminateContract(String id) {

        Jpa.getManager()
            .createNamedQuery("Contract.terminateContract", Contract.class)
            .setParameter(1, id)
            .setParameter(2, Dates.lastDayOfCurrentMonth())
            .setParameter(3, ContractState.TERMINATED)
            .executeUpdate();

    }

    @Override
    public void addSettlement(double settlement, String id) {

        Jpa.getManager()
            .createNamedQuery("Contract.addSettlement", Contract.class)
            .setParameter(1, id)
            .setParameter(2, settlement)
            .executeUpdate();
    }

}
