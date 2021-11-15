import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './authentication/services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'AmongNeewAmigosFrontEnd';

  constructor(private authenticationService : AuthenticationService, private route:Router) { }

  logout() {
    localStorage.setItem("isLogged","false");
    localStorage.setItem("nickName","");
    localStorage.setItem("idToken","");
    localStorage.setItem("expiresIn","");
    localStorage.setItem("isAdmin","");
    localStorage.setItem("isCommonUser","false");
    this.route.navigate(['/home']);
  }

  get isUserLogged()  {
    return this.authenticationService.isUserLogged();
  }
}
