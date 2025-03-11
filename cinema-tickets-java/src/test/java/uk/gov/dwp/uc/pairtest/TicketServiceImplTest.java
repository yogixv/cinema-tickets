package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.MockitoAnnotations;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImplTest {

    @Mock
    private TicketPaymentService mockTicketPaymentService;

    @Mock
    private SeatReservationService mockSeatReservationService;

    private TicketService ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketServiceImpl(mockTicketPaymentService, mockSeatReservationService);
    }

    @Test
    public void testValidPurchase_AdultChildInfant() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);
        ticketService.purchaseTickets(1L, adult, child, infant);

        // Payment = (2 * 25) + (1 * 15) = 50 + 15 = 65
        // Seats reserved = adult + child = 2 + 1 = 3
        verify(mockTicketPaymentService, times(1)).makePayment(1L, 65);
        verify(mockSeatReservationService, times(1)).reserveSeat(1L, 3);
    }

    @Test
    public void testValidPurchase_OnlyAdultTickets() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 3);
        ticketService.purchaseTickets(1L, adult);

        // Payment = 3 * 25 = 75, Seats reserved = 3
        verify(mockTicketPaymentService, times(1)).makePayment(1L, 75);
        verify(mockSeatReservationService, times(1)).reserveSeat(1L, 3);
    }

    @Test
    public void testValidPurchase_AdultAndChildTicketsOnly() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 2);
        ticketService.purchaseTickets(1L, adult, child);

        // Payment = (1 * 25) + (2 * 15) = 25 + 30 = 55, Seats reserved = 1 + 2 = 3
        verify(mockTicketPaymentService, times(1)).makePayment(1L, 55);
        verify(mockSeatReservationService, times(1)).reserveSeat(1L, 3);
    }

    @Test
    public void testValidPurchase_AdultChildAndInfant() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 2);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);
        ticketService.purchaseTickets(1L, adult, child, infant);

        // Payment = (1 * 25) + (2 * 15) = 25 + 30 = 55, Seats reserved = 1 + 2 = 3
        verify(mockTicketPaymentService, times(1)).makePayment(1L, 55);
        verify(mockSeatReservationService, times(1)).reserveSeat(1L, 3);
    }

    @Test
    public void testNoTicketsRequested() {
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L);
        });
    }

    @Test
    public void testExceedingMaxTickets() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 26);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adult);
        });
    }

    @Test
    public void testChildWithoutAdult() {
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, child);
        });
    }

    @Test
    public void testInfantWithoutAdult() {
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, infant);
        });
    }

    @Test
    public void testMoreInfantsThanAdults() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adult, infant);
        });
    }

    @Test
    public void testInvalidTicketTypeRequest_Null() {
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, (TicketTypeRequest) null);
        });
    }

    @Test
    public void testInvalidTicketTypeRequest_NullType() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest infant = new TicketTypeRequest(null, 2);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adult, infant);
        });
    }

    @Test
    public void testInvalidTicketTypeRequest_NegativeQuantity() {
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, -4);
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adult, child);
        });
    }

}