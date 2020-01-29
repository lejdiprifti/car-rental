import {Category} from './category';

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

}