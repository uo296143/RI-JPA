package uo.ri.cws.application.service.invoice.create.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.WorkOrder.WorkOrderState;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto> {

    private List<String> workOrderIds;
    private InvoiceRepository invoice_repo = Factories.repository.forInvoice();
    private WorkOrderRepository work_order_repo = Factories.repository
        .forWorkOrder();

    public CreateInvoiceFor(List<String> workOrderIds) {
        ArgumentChecks.isNotNull(workOrderIds);
        ArgumentChecks.isFalse(workOrderIds.isEmpty());
        ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null));

        this.workOrderIds = workOrderIds;
    }

    @Override
    public InvoiceDto execute() throws BusinessException {

        List<WorkOrder> workOrders = checkWorkOrdersAreCorrect(workOrderIds);
        long number = invoice_repo.getNextInvoiceNumber();
        Invoice invoice = new Invoice(number, workOrders);
        invoice_repo.add(invoice);
        return DtoAssembler.toDto(invoice);

    }

    /*
     * Metodo que comprueba que existan todas las workOrders, que estén en
     * estado FINISHED y además a partir de su id recupera el objeto para
     * añadirlo a la lista worOrders de Invoice.
     */
    private List<WorkOrder> checkWorkOrdersAreCorrect(List<String> workOrderIds)
            throws BusinessException {

        List<WorkOrder> work_orders = new ArrayList<WorkOrder>();
        for (String id : workOrderIds) {
            Optional<WorkOrder> w = work_order_repo.findById(id);
            BusinessChecks.isFalse(w.isEmpty(), "A workorder doesn´t exist");
            BusinessChecks.isTrue(
                    w.get().getState().equals(WorkOrderState.FINISHED),
                    "A wokorder is not in FINISHED state");
            work_orders.add(w.get());
        }

        return work_orders;
    }

}
