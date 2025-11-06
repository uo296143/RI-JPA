package uo.ri.cws.application.service.invoice.create.command;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.workorder.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrdersByClientNif
        implements Command<List<InvoicingWorkOrderDto>> {

    private String nif;
    private WorkOrderRepository work_order_repo = Factories.repository
        .forWorkOrder();

    public FindNotInvoicedWorkOrdersByClientNif(String nif) {
        ArgumentChecks.isNotNull(nif);
        this.nif = nif;
    }

    @Override
    public List<InvoicingWorkOrderDto> execute() throws BusinessException {

        return work_order_repo.findNotInvoicedByClientNif(nif)
            .stream()
            .map(w -> DtoAssembler.toInvoicingWorkOrderDto(w))
            .collect(Collectors.toList());

    }

}
