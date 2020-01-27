
import { NgModule } from '@angular/core';

import { AuthentificationRoutingModule } from '@ikubinfo/authentification/authentification-routing.module';
import { CommonsModule } from '@ikubinfo/commons/commons.module';
import { LoginComponent } from '@ikubinfo/authentification/login/login.component';
import { RegisterComponent } from '@ikubinfo/authentification/register/register.component';
import { LayoutModule } from '@ikubinfo/layout/layout.module';
import { CommonModule } from '@angular/common';


@NgModule({
    imports: [CommonModule, CommonsModule, LayoutModule, AuthentificationRoutingModule],
    exports: [],
    declarations: [LoginComponent, RegisterComponent],
    providers: []
})
export class AuthentificationModule { }
