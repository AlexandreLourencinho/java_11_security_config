import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./components/authentication/login/login.module').then(m => m.LoginModule)
  },
  {
    path: '',
    loadChildren: () => import('./components/home/home.module').then(m => m.HomeModule)
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
