
import { NgModule, Optional, SkipSelf } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ConfirmationService, MessageService } from 'primeng/primeng';


import { AuthService } from '@ikubinfo/core/services/auth.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { ApiService } from '@ikubinfo/core/utilities/api.service';
import { AuthGuard } from '@ikubinfo/core/guards/auth-guard';
import { TokenInterceptor } from '@ikubinfo/core/interceptors/token-interceptor';
import { UserService } from './services/user.service';
import { CategoryService } from './services/category.service';
import { CarService } from './services/car.service';


@NgModule({
  imports: [
    HttpClientModule
  ],
  declarations: [],
  providers: [AuthService, LoggerService, ApiService, AuthGuard,UserService,CategoryService,CarService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }, ConfirmationService,
    MessageService]

})
export class CoreModule {
  /*Prevent reimport of the core module */
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(
        'CoreModule is already loaded. Import it in the AppModule only');
    }
  }
}
