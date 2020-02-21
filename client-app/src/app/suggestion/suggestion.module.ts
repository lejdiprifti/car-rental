import { CommonsModule } from "@ikubinfo/commons/commons.module";
import { FormsModule } from "@angular/forms";
import { NgModule } from "@angular/core";

import { FullCalendarModule } from "@fullcalendar/angular";

import { DashboardComponent } from "@ikubinfo/suggestion/dashboard/dashboard.component";
import { SuggestionRoutingModule } from "@ikubinfo/suggestion/suggestion-routing.module";
import { LayoutModule } from "@ikubinfo/layout/layout.module";
import { UserComponent } from "@ikubinfo/suggestion/user/user.component";
import { CategoriesComponent } from "@ikubinfo/suggestion/categories/categories.component";
import { CarsComponent } from "@ikubinfo/suggestion/cars/cars.component";
import { CarComponent } from "@ikubinfo/suggestion/car/car.component";
import { BookingComponent } from "@ikubinfo/suggestion/booking/booking.component";
import { BookingsComponent } from "@ikubinfo/suggestion/bookings/bookings.component";
import { ClientsComponent } from "@ikubinfo/suggestion/clients/clients.component";
import { ReservationCancellanceComponent } from '@ikubinfo/suggestion/reservation-cancellance/reservation-cancellance.component';

import { DataViewModule } from "primeng/dataview";
import {
  PasswordModule,
  FileUploadModule,
  CardModule,
  ToggleButtonModule,
  SelectButtonModule,
  ProgressSpinnerModule,
  DialogModule,
  SplitButtonModule,
  PanelModule,
  InplaceModule,
  KeyFilterModule,
  DropdownModule
} from "primeng/primeng";

@NgModule({
  imports: [
    CommonsModule,
    DialogModule,
    SelectButtonModule,
    SplitButtonModule,
    FullCalendarModule,
    ProgressSpinnerModule,
    DropdownModule,
    ToggleButtonModule,
    KeyFilterModule,
    InplaceModule,
    PanelModule,
    ToggleButtonModule,
    SuggestionRoutingModule,
    DataViewModule,
    LayoutModule,
    CardModule,
    FormsModule,
    PasswordModule,
    FileUploadModule
  ],
  exports: [],
  declarations: [
    DashboardComponent,
    BookingsComponent,
    BookingComponent,
    CarComponent,
    ClientsComponent,
    CarsComponent,
    UserComponent,
    CategoriesComponent,
    ReservationCancellanceComponent
  ],
  providers: []
})
export class SuggestionModule {}
