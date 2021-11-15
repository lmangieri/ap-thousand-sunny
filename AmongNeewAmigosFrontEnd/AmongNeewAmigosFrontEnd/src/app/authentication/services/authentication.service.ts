import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { SignupRequestAmong } from '../dtos/SignupRequestAmong'
import { LoginRequest } from '../dtos/LoginRequest'
import { environment } from 'src/environments/environment';
import * as angular from 'angular';
import { map } from 'rxjs/operators'
import { Observable } from 'rxjs';
import { LoginResponse } from '../dtos/LoginResponse';
import { ResponseErrorDto } from '../dtos/ResponseErrorDto';
import { Router } from '@angular/router';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  httpBase = environment.apiBaseUrl;


  constructor(private http: HttpClient, private route:Router) { }

  signUp(signUpRequest : SignupRequestAmong) {
    return this.http.post<SignupRequestAmong>(this.httpBase + 'authentication/signUp',signUpRequest, httpOptions);
  }

  anonymousLogin(apelido : string, responseErrorDto : ResponseErrorDto) {
    this.executeLoginAnonymous(apelido).subscribe(
      (loginResponse : LoginResponse) => {
        this.afterLogin(loginResponse,responseErrorDto);
        /* Anonymous login da refresh na página pois ele já está na home */
        location.reload();
      },
      (err) => {
        //console.info("login didn't work");
        if(err.status == 0) {
          responseErrorDto.message = "Um erro inesperado aconteceu na tentativa de login.";
        }
      }
    );
  }

  afterLogin(loginResponse : LoginResponse,responseErrorDto : ResponseErrorDto) {
       // Condição abaixo indica que o horário do sistema do usuário está desconfigurada para o futuro
       if(!(Number.parseInt(loginResponse.expiresIn,10) - Date.now()/1000 > 0)) { 
        responseErrorDto.message = "Login foi realizado, porém aparentemente o seu computador ou dispositivo móvel está com o horário desconfigurado.";
        return;
      }

      console.log('inside subscribe 555');
      localStorage.setItem("isLogged","true");
      localStorage.setItem("nickName",loginResponse.nickName);
      localStorage.setItem("idToken",loginResponse.idToken);
      localStorage.setItem("expiresIn",loginResponse.expiresIn);
      if(loginResponse.roles.includes("ROLE_ADMIN_P")) {
        localStorage.setItem("isAdmin","true");
      } else {
        localStorage.setItem("isAdmin","false");
      }

      if(loginResponse.roles.includes("ROLE_COMMON_P")) {
        localStorage.setItem("isCommonUser","true");
      } else {
        localStorage.setItem("isCommonUser","false");
      }

  }

  login(loginRequest : LoginRequest, responseErrorDto : ResponseErrorDto) {
    this.executeLogin(loginRequest).subscribe(
      (loginResponse : LoginResponse) => {
        this.afterLogin(loginResponse,responseErrorDto);
        /* Este é o fluxo de login de acc adm */
        this.route.navigate(['/home']);
      },
      (err) => {
        //console.info("login didn't work");
        if(err.status == 0) {
          responseErrorDto.message = "Um erro inesperado aconteceu na tentativa de login.";
        }
        if(err.status == 401) {
          responseErrorDto.message = "E-mail ou Senha inválidos.";
        }
      }
    );

  }

  executeLoginAnonymous(apelido: string) : Observable<LoginResponse> { 
    return this.http.post<LoginResponse>(this.httpBase + 'authentication/anonymousLogin?nickName='+apelido,null, httpOptions)
    .pipe(map(data => {
      return data;
    }));
  }

  executeLogin(loginRequest : LoginRequest) : Observable<LoginResponse> {
      return this.http.post<LoginResponse>(this.httpBase + 'authentication/login',loginRequest, httpOptions)
      .pipe(map(data => {
        return data;
      }));
  }

  isUserLogged() : boolean {
    var idToken = localStorage.getItem("idToken");
    var expiresIn = localStorage.getItem("expiresIn");
    //console.info('expires in => '+expiresIn);
    //console.info('now is => '+Date.now()/1000);
    if(idToken != null) {
      if(expiresIn != null) {
        //console.info("Token is still valid");
        return Number.parseInt(expiresIn,10) - Date.now()/1000 > 0
      }
    }
    console.info("idToken é nulo ou inválido");
    return false;
  }

  isAdmin() : boolean {
    var isAdmin = localStorage.getItem("isAdmin");
    if(isAdmin == "true") {
      return true;
    } else {
      return false;
    }
  }

  getNick() : string {
    var nick = localStorage.getItem("nickName");
    if(nick != null) {
      return nick;
    } else {
      return 'Anônimo';
    }
  }
}
