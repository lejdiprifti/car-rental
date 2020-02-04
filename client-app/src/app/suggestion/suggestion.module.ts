import { CommonsModule } from '@ikubinfo/commons/commons.module';

import { NgModule } from '@angular/core';

import { PostComponent } from '@ikubinfo/suggestion/post/post.component';
import { PostsComponent } from '@ikubinfo/suggestion/posts/posts.component';
import { DashboardComponent } from '@ikubinfo/suggestion/dashboard/dashboard.component';
import { SuggestionRoutingModule } from '@ikubinfo/suggestion/suggestion-routing.module';
import { LayoutModule } from '@ikubinfo/layout/layout.module';
import { FormsModule } from '@angular/forms';
import { UserComponent } from './user/user.component';
import { CategoryComponent } from './category/category.component';
import { PasswordModule, FileUploadModule, CardModule, ToggleButtonModule, ProgressSpinnerModule,DialogModule, SplitButtonModule,PanelModule, InplaceModule, KeyFilterModule, DropdownModule } from 'primeng/primeng';
import { CategoriesComponent } from './categories/categories.component';
import {DataViewModule} from 'primeng/dataview';
import { CarsComponent } from './cars/cars.component';
import { CarComponent } from './car/car.component';
import {FullCalendarModule} from '@fullcalendar/angular';
import { BookingComponent } from './booking/booking.component';



@NgModule({
    imports: [CommonsModule,DialogModule, SplitButtonModule,  FullCalendarModule, ProgressSpinnerModule,DropdownModule,ToggleButtonModule,KeyFilterModule,InplaceModule,PanelModule, ToggleButtonModule, SuggestionRoutingModule,DataViewModule, LayoutModule,CardModule, FormsModule, PasswordModule, FileUploadModule],
    exports: [],
    declarations: [DashboardComponent, BookingComponent,CarComponent, PostComponent, PostsComponent, CarsComponent, UserComponent, CategoryComponent, CategoriesComponent],
    providers: []
})
export class SuggestionModule { }
