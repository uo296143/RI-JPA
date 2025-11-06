package uo.ri.cws.application.service.workorder.crud;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.cws.domain.WorkOrder;

public class DtoAssembler {

    public static WorkOrderDto toDto(WorkOrder w) {
        WorkOrderDto dto = new WorkOrderDto();
        dto.id = w.getId();
        dto.version = w.getVersion();

        dto.vehicleId = w.getVehicle().getId();
        dto.description = w.getDescription();
        dto.date = w.getDate();
        dto.amount = w.getAmount();
        dto.state = w.getState().toString();

        // might be null
        dto.mechanicId = w.getMechanic().getId();
        dto.invoiceId = w.getInvoice().getId();
        return dto;
    }

    public static InvoicingWorkOrderDto toInvoicingWorkOrderDto(WorkOrder w) {

        InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
        dto.id = w.getId();

        dto.description = w.getDescription();
        dto.date = w.getDate();
        dto.amount = w.getAmount();
        dto.state = w.getState().toString();

        return dto;

    }

//    public static List<MechanicDto> toMechanicDtoList(List<Mechanic> list) {
//        List<MechanicDto> res = new ArrayList<>();
//        for (Mechanic m : list) {
//            res.add(toDto(m));
//        }
//        return res;
//    }

}
