import { CommonsModule } from '@ikubinfo/commons/commons.module';
import { Router } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { CoreModule } from '@ikubinfo/core/core.module';
import { AppRoutingModule } from '@ikubinfo/app-routing.module';
import { AppComponent } from '@ikubinfo/app.component';
import { AuthentificationModule } from '@ikubinfo/authentification/authentification.module';
import { DatePipe } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    CoreModule,
    AuthentificationModule,
    CommonsModule
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule {   // Diagnostic only: inspect router configuration
  constructor(router: Router) {
    // Use a custom replacer to display function names in the route configs
    // const replacer = (key, value) => (typeof value === 'function') ? value.name : value;

    // console.log('Routes: ', JSON.stringify(router.config, replacer, 2));
  }
}
