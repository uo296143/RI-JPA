package uo.ri.cws.infrastructure.persistence.jpa.repository;

import java.util.List;

import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.WorkOrder.WorkOrderState;
import uo.ri.cws.infrastructure.persistence.jpa.util.BaseJpaRepository;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class WorkOrderJpaRepository extends BaseJpaRepository<WorkOrder>
        implements WorkOrderRepository {

    @Override
    public List<WorkOrder> findByIds(List<String> idsAveria) {
        return Jpa.getManager()
            .createNamedQuery("WorkOrder.findByIds", WorkOrder.class)
            .setParameter(1, idsAveria)
            .getResultList();
    }

    @Override
    public List<WorkOrder> findByClientNif(String nif) {

        return Jpa.getManager()
            .createNamedQuery("WorkOrder.findByClientNif", WorkOrder.class)
            .setParameter(1, nif)
            .getResultList();

    }

    @Override
    public List<WorkOrder> findNotInvoicedByClientNif(String nif) {

        return Jpa.getManager()
            .createNamedQuery("WorkOrder.findNotInvoicedWorkOrdersByClientNif",
                    WorkOrder.class)
            .setParameter(1, nif)
            .setParameter(2, WorkOrderState.FINISHED)
            .getResultList();

    }

    @Override
    public List<WorkOrder> findByPlateNumber(String plate) {

        return Jpa.getManager()
            .createNamedQuery("WorkOrder.findByPlate", WorkOrder.class)
            .setParameter(1, plate)
            .getResultList();

    }

}
