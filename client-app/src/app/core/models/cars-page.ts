import { Car } from './car';

export interface CarsPage {
    totalRecords: number;
    carsList: Array<Car>;
}