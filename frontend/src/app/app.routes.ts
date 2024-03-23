import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {RegistrationComponent} from "./registration/registration.component";
import {NgModule} from "@angular/core";
import {LoginComponent} from "./login/login.component";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'home', component: HomeComponent},

  {path: 'login', component: LoginComponent},
];

export class AppRoutingModule {
}
