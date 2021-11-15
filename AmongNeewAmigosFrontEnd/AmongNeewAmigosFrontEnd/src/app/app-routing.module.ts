import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeAdminComponent } from './authentication/components/home-admin/home-admin.component';
import { LoginComponent } from './authentication/components/login/login.component';
import { SignUpComponent } from './authentication/components/sign-up/sign-up.component';
import { HomeComponent } from './pages/components/home/home.component';
import { PollCreationComponent } from './pages/components/poll-creation/poll-creation.component';

const routes: Routes = [
  { path: "", component: HomeComponent},
  { path: "home", component: HomeComponent},
  { path: "homeafterlogged", component: HomeComponent},
  { path: "login", component: LoginComponent},
  { path: "sign-up", component: SignUpComponent},
  { path: "poll-creation", component: PollCreationComponent},
  { path: "home-admin", component: HomeAdminComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes,{ useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
