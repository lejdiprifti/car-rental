export interface CarFilter {
    startIndex?: number,
    pageSize?: number,
    selectedCategoryIds?: number[],
    startDate?: string,
    endDate?: string,
    brand?: string
}