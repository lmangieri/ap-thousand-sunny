import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoginRequest } from '../../dtos/LoginRequest';
import { ResponseErrorDto } from '../../dtos/ResponseErrorDto'
import { Router } from '@angular/router';


export class LoginDto {
  name : string;
  leagueTypeId : number;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  responseErrorDto: ResponseErrorDto;

  constructor(private route:Router, private authenticationService : AuthenticationService, private fb: FormBuilder) { }

  ngOnInit(): void {

    this.responseErrorDto = new ResponseErrorDto();
    this.responseErrorDto.message = '';
    this.loginForm = this.fb.group({
      email : ['',[
        Validators.required,
        Validators.email
      ]],
      password : ['',[
        Validators.required,
        Validators.minLength(8)
      ]]
    });
  }

  redirect(path : string) {
    this.route.navigate([path]);
  }

  login() {
    this.clean();
    var loginRequest = new LoginRequest(this.loginForm.get("email")?.value,this.loginForm.get("password")?.value);


    this.authenticationService.login(loginRequest, this.responseErrorDto);
  }

  clean() {
    this.responseErrorDto = new ResponseErrorDto();
    this.responseErrorDto.message = '';
  }

  get email() {
    return this.loginForm.get('email');
  }
  get password() {
    return this.loginForm.get('password');
  }

}
