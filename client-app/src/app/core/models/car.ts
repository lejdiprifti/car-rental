import {Category} from './category';
import { Reservation } from './reservation';
import { ReservedDates } from './reservedDates';
import { Status } from './status.enum';

export interface Car {
    id?: number;
    name?: string;
    type?: string;
    year?: number;
    plate?: string;
    price?: number;
    diesel?: string;
    availability?: Status;
    categoryId?: number;
    category?: Category;
    description?: string;
    photo?: File;
    reservedDates?: Array<ReservedDates>;

}