import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SignUpComponent } from './authentication/components/sign-up/sign-up.component';
import { LoginComponent } from './authentication/components/login/login.component';
import { HomeAdminComponent } from './authentication/components/home-admin/home-admin.component';

import { SidebarComponent } from './sidebar/sidebar.component';
import { HttpClientModule } from '@angular/common/http';

import { ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './pages/components/home/home.component'
import { Router } from '@angular/router';
import { PollCreationComponent } from './pages/components/poll-creation/poll-creation.component';


@NgModule({
  declarations: [
    AppComponent,
    SignUpComponent,
    LoginComponent,
    SidebarComponent,
    HomeComponent,
    PollCreationComponent,
    HomeAdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {

 }
