import {Category} from './category';
import { Reservation } from './reservation';
import { ReservedDates } from './reservedDates';

export interface Car {
    id?: number;
    name?: string;
    type?: string;
    year?: number;
    plate?: string;
    price?: number;
    diesel?: string;
    availability?: boolean;
    categoryId?: number;
    category?: Category;
    description?: string;
    photo?: File;
    reservedDates?: Array<ReservedDates>;

}