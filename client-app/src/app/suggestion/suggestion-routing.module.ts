import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminGuard } from '@ikubinfo/core/guards/admin-guard';
import { DashboardComponent } from '@ikubinfo/suggestion/dashboard/dashboard.component';
import { FullComponent } from '@ikubinfo/layout/full/full.component';
import { UserComponent } from './user/user.component';
import { UserGuard } from '@ikubinfo/core/guards/user-guard';
import { CategoryComponent } from './category/category.component';
import { CategoriesComponent } from './categories/categories.component';
import { CarsComponent } from './cars/cars.component';
import { CarComponent } from './car/car.component';
import { BookingComponent } from './booking/booking.component';
import { BookingsComponent } from './bookings/bookings.component';
import { ClientsComponent } from './clients/clients.component';

const suggestionRoutes: Routes = [
    {
        path: '',
        component: FullComponent,
        children: [
            { path: 'dashboard', component: DashboardComponent, canActivate: [AdminGuard] },
            {path: 'user', component: UserComponent, canActivate: [UserGuard]},
            {path: 'categories', component: CategoriesComponent, canActivate: [AdminGuard]},
            {path: 'category', component: CategoryComponent, canActivate: [AdminGuard]},
            {path: 'category/:id', component: CategoryComponent, canActivate: [AdminGuard]},
            {path: 'cars', component: CarsComponent},
            {path: 'car/:id', component: CarComponent, canActivate: [AdminGuard]},
            {path: 'car', component: CarComponent, canActivate: [AdminGuard]},
            {path: 'reservations', component: BookingsComponent},
            {path: 'clients', component: ClientsComponent, canActivate: [AdminGuard]},
            {path: 'reservation/:carId', component: BookingComponent, canActivate: [UserGuard]}
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(suggestionRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class SuggestionRoutingModule { }
