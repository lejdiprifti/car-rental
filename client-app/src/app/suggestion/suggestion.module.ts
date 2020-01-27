import { CommonsModule } from '@ikubinfo/commons/commons.module';

import { NgModule } from '@angular/core';

import { PostComponent } from '@ikubinfo/suggestion/post/post.component';
import { PostsComponent } from '@ikubinfo/suggestion/posts/posts.component';
import { DashboardComponent } from '@ikubinfo/suggestion/dashboard/dashboard.component';
import { SuggestionRoutingModule } from '@ikubinfo/suggestion/suggestion-routing.module';
import { LayoutModule } from '@ikubinfo/layout/layout.module';
import { FormsModule } from '@angular/forms';

@NgModule({
    imports: [CommonsModule, SuggestionRoutingModule, LayoutModule, FormsModule],
    exports: [],
    declarations: [DashboardComponent, PostComponent, PostsComponent],
    providers: []
})
export class SuggestionModule { }
