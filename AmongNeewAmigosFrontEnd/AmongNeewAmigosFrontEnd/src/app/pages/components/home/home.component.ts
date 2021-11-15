import { AfterViewInit, Component, ElementRef, OnInit } from '@angular/core';
import { AuthenticationService } from '../../../authentication/services/authentication.service';
import { Router } from '@angular/router';
import { ApplicationService } from '../../services/application.service';
import { HomeDetailsDto } from '../../dtos/HomeDetailsDto';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { PollAnalyseDetails } from '../../dtos/PollAnalyseDetails';
import {Chart} from 'node_modules/chart.js'
import { registerables} from 'chart.js';
import * as angular from 'angular';
import { ResponseErrorDto } from 'src/app/authentication/dtos/ResponseErrorDto';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterViewInit {
  responseErrorDto : ResponseErrorDto;
  anonymousLoginForm: FormGroup;
  homeDetailsDto : HomeDetailsDto;
  pollAnalyseDetails : PollAnalyseDetails;
  loading : boolean;
  successVote: number;
  voteForm: FormGroup;


  aguardeVoto: boolean;


  constructor(private authenticationService : AuthenticationService, private route:Router, private applicationService: ApplicationService, private fb: FormBuilder, private elementRef: ElementRef) { 

  }


  ngAfterViewInit(): void {
    console.log("ngAfterViewInit....");
  }

  ngOnInit(): void {
    console.log("homeInit....");
    this.anonymousLoginForm = this.fb.group({
      nickName : ['',[
        Validators.required
      ]]
    });



    this.homeDetailsDto = new HomeDetailsDto();
    this.homeDetailsDto.pollControlStatus = 0;
    
    this.loading = true;
    this.successVote = 0; // significa que não votou ainda
    this.aguardeVoto = false;



    if(this.isUserLogged) {
      console.log("homeInit....inside userLogged");
      this.reloadHome();
      this.voteForm = this.fb.group({
        optionId :  [null,[
          Validators.required
        ]]
      });      
    }    
  }

  anonymousLogin() {
    var apelido = this.anonymousLoginForm.get("nickName")?.value;

    this.authenticationService.anonymousLogin(apelido,this.responseErrorDto);
  }

  /* Este método na verdade está relacionado a mudar o status da votação para status de análise */
  encerrar() {
    this.applicationService.analysePoll().subscribe(
      (analysePollResponse) => {
        console.log(analysePollResponse);
        this.reloadHome();
      },
      (err) => {
        console.log(err);
        this.reloadHome();
      }
    )
  }

  loadChart() {

    var keyss: string[] = [];
    var values: number[] = [];
    for (const [key, value] of Object.entries( this.homeDetailsDto.resultsLastPoll)) { 
      keyss.push(key);
      values.push(value);
      console.log(key, value);
  }
    console.log(keyss);
    console.log(values);

    let htmlRef = this.elementRef.nativeElement.querySelector(`#myChart`);
    Chart.register(...registerables);
    var myChart = new Chart(htmlRef, {
      type: 'pie',
      data: {
          datasets: [{
              label: '# of Votes',

              data: values,
              backgroundColor: [
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                  'rgba(75, 192, 192, 0.2)',
                  'rgba(153, 102, 255, 0.2)',
                  'rgba(255, 159, 64, 0.2)'
              ],
              borderColor: [
                  'rgba(255, 99, 132, 1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)',
                  'rgba(75, 192, 192, 1)',
                  'rgba(153, 102, 255, 1)',
                  'rgba(255, 159, 64, 1)'
              ],
              borderWidth: 1
          }],
          labels: keyss
      },
      options: {
        plugins: {
          legend: {
            position: 'bottom',
          },
          title: {
            display: true,
            text: 'Resultado votação'
          }
        }
      }
  });
}


  /* Este método está relacionado a mudar o status da votação de "Em Análise" para Encerrado*/
  endPoll(val : number) {
    this.applicationService.endPoll(val).subscribe(
      (endPollResponse) => {
        console.log(endPollResponse);
        this.reloadHome();
      },
      (err) => {
        console.log(err);
        this.reloadHome();
      }
    )
  }


  returnPollToOpenState() {
    this.applicationService.returnPollToOpenState().subscribe(
      (success) => {
        this.loading = true;
        console.log(success);
        this.reloadHome();
      },
      (err) => {
        console.log(err);
      }
    )
  }

  reloadHome() {
    this.applicationService.callHome().subscribe(
      (HomeDetailsDto : HomeDetailsDto) => {
        console.log(HomeDetailsDto);
        this.homeDetailsDto = HomeDetailsDto;
        if(this.homeDetailsDto.pollControlStatus == 2 && this.isAdmin) {
          this.loading = true;
          this.getPollAnalyseDetails();
        } else if (this.homeDetailsDto.pollControlStatus == 3) {
          this.loadChart();
          this.loading = false;
        }
        else {
          this.loading = false;
        }
      }
    );
  }

  redirect(path : string) {
    this.route.navigate([path]);
  }

  getPollAnalyseDetails() {
    this.applicationService.getPollAnalyseDetails().subscribe(
      (PollAnalyseDetails : PollAnalyseDetails) => {
        this.pollAnalyseDetails = PollAnalyseDetails;
        this.loading = false;
      }
    );
  }

  votar(event: any) {
    this.aguardeVoto = true;
    event.target.disabled = true;

    var optionId = this.voteForm.get("optionId")?.value;
    console.log('optionId => '+optionId);
    this.applicationService.vote(optionId).subscribe(
      (voteResponse) => {
        console.log(voteResponse);
        this.successVote = 1;
        this.aguardeVoto = false;
        this.reloadHome();
      },
      (err) => {
        console.log(err);
        this.successVote = 2;
        this.aguardeVoto = false;
        this.reloadHome();
      }
    );
  }


  get getNick() {
    return this.authenticationService.getNick();
  }

  get isUserLogged()  {
    return this.authenticationService.isUserLogged();
  }

  get isAdmin() {
    return this.authenticationService.isAdmin();
  }

  get isLoading() {
    return this.loading;
  }



}
