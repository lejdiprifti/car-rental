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
import { PasswordModule, FileUploadModule, CardModule } from 'primeng/primeng';
import { CategoriesComponent } from './categories/categories.component';

@NgModule({
    imports: [CommonsModule, SuggestionRoutingModule, LayoutModule,CardModule, FormsModule, PasswordModule, FileUploadModule],
    exports: [],
    declarations: [DashboardComponent, PostComponent, PostsComponent, UserComponent, CategoryComponent, CategoriesComponent],
    providers: []
})
export class SuggestionModule { }
