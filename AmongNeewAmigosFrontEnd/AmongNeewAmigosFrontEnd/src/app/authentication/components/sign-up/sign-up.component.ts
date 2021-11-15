import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service'
import { FormGroup, FormBuilder, Validators } from '@angular/forms'
import { SignupRequestAmong } from '../../dtos/SignupRequestAmong'
import { ResponseErrorDto } from '../../dtos/ResponseErrorDto'
import { Router } from '@angular/router';


@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

  signUpForm: FormGroup;
  signUpRequest : SignupRequestAmong;
  responseErrorDto: ResponseErrorDto;

  successSignUp: boolean;



  constructor(private route:Router, private authenticationService : AuthenticationService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.successSignUp = false;
    this.responseErrorDto = new ResponseErrorDto();
    this.responseErrorDto.message = '';
    
    this.signUpForm = this.fb.group({
      email : ['',[
        Validators.required,
        Validators.email
      ]],
      password : ['',[
        Validators.required,
        Validators.minLength(8)
      ]],
      nickName : ['',[
        Validators.required
      ]]
    });

  }

  redirect(path : string) {
    this.route.navigate([path]);
  }

  register() {
    this.clean();
    this.signUpRequest = new SignupRequestAmong (this.signUpForm.get("email")?.value,this.signUpForm.get("password")?.value,
    this.signUpForm.get("nickName")?.value);


    this.authenticationService.signUp(this.signUpRequest).subscribe((data : SignupRequestAmong) => {
       this.successSignUp = true;
        console.log('Sucesso Signup');
      },
      (err) => {
        this.responseErrorDto.assign(err.error);
      }
    );
  }

  clean() {
    this.successSignUp = false;
    this.responseErrorDto = new ResponseErrorDto();
    this.responseErrorDto.message = '';
  }

  
  get nickName() {
    return this.signUpForm.get('nickName');
  }

  get email() {
    return this.signUpForm.get('email');
  }
  get password() {
    return this.signUpForm.get('password');
  }
  

}
