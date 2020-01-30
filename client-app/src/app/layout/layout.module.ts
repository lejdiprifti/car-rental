
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SidebarModule } from 'primeng/sidebar';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

import { FullComponent } from '@ikubinfo/layout/full/full.component';
import { CommonsModule } from '@ikubinfo/commons/commons.module';
import { SimpleComponent } from '@ikubinfo/layout/simple/simple.component';
import { SidebarComponent } from '@ikubinfo/layout//sidebar/sidebar.component';
import { NavbarComponent } from '@ikubinfo/layout//navbar/navbar.component';
import { FooterComponent } from '@ikubinfo/layout/footer/footer.component';

@NgModule({
    imports: [CommonModule, RouterModule, SidebarModule, ButtonModule, CommonsModule],
    exports: [FullComponent, SimpleComponent],
    declarations: [FooterComponent, NavbarComponent, SidebarComponent, FullComponent, SimpleComponent],
    providers: [],
})
export class LayoutModule { }
