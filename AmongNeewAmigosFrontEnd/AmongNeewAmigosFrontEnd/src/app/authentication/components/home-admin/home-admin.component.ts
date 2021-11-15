import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.scss']
})
export class HomeAdminComponent implements OnInit {

  constructor(private route:Router, private authenticationService : AuthenticationService) { }

  ngOnInit(): void {
  }

  redirect(path : string) {
    this.route.navigate([path]);
  }

  get isUserLogged()  {
    return this.authenticationService.isUserLogged();
  }

  loggout() {
    localStorage.setItem("isLogged","false");
    localStorage.setItem("nickName","");
    localStorage.setItem("idToken","");
    localStorage.setItem("expiresIn","");
    localStorage.setItem("isAdmin","");
    localStorage.setItem("isCommonUser","false");
    this.route.navigate(['/home']);
  }

}
