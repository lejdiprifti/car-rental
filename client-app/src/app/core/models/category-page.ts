import { Category } from './category';

export interface CategoryPage {
    totalRecords?: number,
    categoryList?: Array<Category>;
}