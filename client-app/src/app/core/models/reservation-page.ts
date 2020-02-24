import { Reservation } from './reservation';

export interface ReservationPage {
    totalRecords: number,
    reservationList: Array<Reservation>
}