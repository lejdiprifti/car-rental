import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminGuard } from '@ikubinfo/core/guards/admin-guard';
import { DashboardComponent } from '@ikubinfo/suggestion/dashboard/dashboard.component';
import { PostComponent } from '@ikubinfo/suggestion/post/post.component';
import { PostsComponent } from '@ikubinfo/suggestion/posts/posts.component';
import { FullComponent } from '@ikubinfo/layout/full/full.component';
import { UserComponent } from './user/user.component';
import { UserGuard } from '@ikubinfo/core/guards/user-guard';
import { CategoryComponent } from './category/category.component';
import { CategoriesComponent } from './categories/categories.component';
import { CarsComponent } from './cars/cars.component';
import { CarComponent } from './car/car.component';

const suggestionRoutes: Routes = [
    {
        path: '',
        component: FullComponent,
        children: [
            { path: 'dashboard', component: DashboardComponent, canActivate: [AdminGuard] },
            { path: 'posts', component: PostsComponent, canActivate: [AdminGuard] },
            { path: 'post', component: PostComponent, canActivate: [AdminGuard] },
            { path: 'post/:id', component: PostComponent, canActivate: [AdminGuard] },
            {path: 'user', component: UserComponent, canActivate: [UserGuard]},
            {path: 'categories', component: CategoriesComponent, canActivate: [AdminGuard]},
            {path: 'category', component: CategoryComponent, canActivate: [AdminGuard]},
            {path: 'category/:id', component: CategoryComponent, canActivate: [AdminGuard]},
            {path: 'cars', component: CarsComponent},
            {path: 'car/:id', component: CarComponent, canActivate: [AdminGuard]},
            {path: 'car', component: CarComponent, canActivate: [AdminGuard]}
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
