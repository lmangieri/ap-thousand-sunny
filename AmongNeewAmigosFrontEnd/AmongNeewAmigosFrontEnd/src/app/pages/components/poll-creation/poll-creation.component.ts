import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms'
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/authentication/services/authentication.service';
import { PollDto } from '../../dtos/PollDto';
import { ApplicationService } from '../../services/application.service';

@Component({
  selector: 'app-poll-creation',
  templateUrl: './poll-creation.component.html',
  styleUrls: ['./poll-creation.component.scss']
})
export class PollCreationComponent implements OnInit {
  createPollForm: FormGroup;
  listOptions: string[];
  optionErrorMessage: string;

  formErrorMessage: string;
  waitPollCreation: boolean;

  constructor(private fb: FormBuilder,private authenticationService : AuthenticationService, private applicationService: ApplicationService, private route:Router) { }

  ngOnInit(): void {
    this.waitPollCreation = false;
    this.listOptions = [];
    this.createPollForm = this.fb.group({
      pollTitle : '',
      option: ''
    });
  }

  addOption() {

    if(this.createPollForm.value.option == '') {
      this.optionErrorMessage = 'Opção está vazia';
      return 0;
    }
    if(!this.listOptions.includes(this.createPollForm.value.option)) {
      this.listOptions.push(this.createPollForm.value.option);
      this.clean();
    } else {
      this.optionErrorMessage = 'Opção já foi inserida';
      return 0;
    }
    return 1;
  }

  clean() {
    this.optionErrorMessage = '';
    this.formErrorMessage = '';
    this.createPollForm.value.option = '';
    this.createPollForm.get('option')?.setValue('');
  }

  cleanForm() {
    this.listOptions = [];
  }

  criarVotacao() {

    var pollTitle = this.createPollForm.get('pollTitle')?.value;

    /* Validação de formulário - inicio */
    if(pollTitle == '') {
      this.formErrorMessage = 'Título da votação não pode estar em branco';
      return 0;
    }

    if(!(this.listOptions.length >= 2)) {
      this.formErrorMessage = 'É necessário ter pelo menos 2 opções de votação'
      return 0;
    } 
    /* Validação de formulário - fim */
    this.waitPollCreation = true;
    
    var pollDto = new PollDto(pollTitle,this.listOptions);
    this.applicationService.createPoll(pollDto).subscribe(
      (createPollResponse) => {
        console.log(createPollResponse);
        this.waitPollCreation = false;
        this.route.navigateByUrl('/home');
      },
      (err) => {
        console.log(err);
        this.waitPollCreation = false;
      }
    );
    return 1;
  }

  get isUserLogged()  {
    return this.authenticationService.isUserLogged();
  }

  get isAdmin() {
    return this.authenticationService.isAdmin();
  }

  get getNick() {
    return this.authenticationService.getNick();
  }

  get pollTitle() {
    return this.createPollForm.get('pollTitle');
  }
}
