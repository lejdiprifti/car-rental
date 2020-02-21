import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AdminGuard } from "@ikubinfo/core/guards/admin-guard";
import { DashboardComponent } from "@ikubinfo/suggestion/dashboard/dashboard.component";
import { FullComponent } from "@ikubinfo/layout/full/full.component";
import { UserComponent } from "@ikubinfo/suggestion/user/user.component";
import { UserGuard } from "@ikubinfo/core/guards/user-guard";
import { CategoriesComponent } from "@ikubinfo/suggestion/categories/categories.component";
import { CarsComponent } from "@ikubinfo/suggestion/cars/cars.component";
import { CarComponent } from "@ikubinfo/suggestion/car/car.component";
import { BookingComponent } from "@ikubinfo/suggestion/booking/booking.component";
import { BookingsComponent } from "@ikubinfo/suggestion/bookings/bookings.component";
import { ClientsComponent } from "@ikubinfo/suggestion/clients/clients.component";
import { ReservationCancellanceComponent } from '@ikubinfo/suggestion/reservation-cancellance/reservation-cancellance.component';

const suggestionRoutes: Routes = [
  {
    path: "",
    component: FullComponent,
    children: [
      {
        path: "dashboard",
        component: DashboardComponent,
        canActivate: [AdminGuard]
      },
      { path: "user", component: UserComponent, canActivate: [UserGuard] },
      {
        path: "categories",
        component: CategoriesComponent,
        canActivate: [AdminGuard]
      },
      { path: "cars", component: CarsComponent },
      { path: "car/:id", component: CarComponent, canActivate: [AdminGuard] },
      { path: "car", component: CarComponent, canActivate: [AdminGuard] },
      { path: "reservations", component: BookingsComponent },
      {
        path: "clients",
        component: ClientsComponent,
        canActivate: [AdminGuard]
      },
      {
        path: "car/:carId/reservation",
        component: BookingComponent,
        canActivate: [UserGuard]
      },
      {
        path: "car/:carId/reservations/cancel",
        component: ReservationCancellanceComponent,
        canActivate: [AdminGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(suggestionRoutes)],
  exports: [RouterModule]
})
export class SuggestionRoutingModule {}
