package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    private static final int MAX_TICKETS = 25;
    private static final int ADULT_TICKET_PRICE = 25;
    private static final int CHILD_TICKET_PRICE = 15;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {
        if (ticketPaymentService == null || seatReservationService == null) {
            throw new IllegalArgumentException("Services cannot be null");
        }
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {

        validateAccountId(accountId);
        validateTicketTypeRequests(ticketTypeRequests);

    }

    private void validateAccountId(long accountId) {
        if (accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID: " + accountId);
        }
    }

    private void validateTicketTypeRequests(TicketTypeRequest... ticketTypeRequests) {
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("At least one ticket type request must be provided");
        }
        for (TicketTypeRequest request : ticketTypeRequests) {
            if (request == null) {
                throw new InvalidPurchaseException("Invalid ticket type request");
            }
        }
    }

}
