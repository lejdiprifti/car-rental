import { Car } from './car';
import { User } from './user';

export interface Reservation {
    id?: number,
    carId?: number,
    car?: Car,
    user?: User,
    startDate?: Date,
    endDate?: Date,
    created_at?: Date,
    active?: boolean
}