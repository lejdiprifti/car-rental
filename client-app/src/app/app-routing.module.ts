import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthGuard } from '@ikubinfo/core/guards/auth-guard';

const routes: Routes = [
  {
    path: 'suggestion',
    loadChildren: () => import('./suggestion/suggestion.module').then(module => module.SuggestionModule),
    canLoad: [AuthGuard]
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      {
        enableTracing: false, // <-- debugging purposes only
      }
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
