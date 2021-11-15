import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { HomeDetailsDto } from '../dtos/HomeDetailsDto';
import {map} from 'rxjs/operators';
import { VoteResponse } from '../dtos/VoteResponse';
import { PollAnalyseDetails } from '../dtos/PollAnalyseDetails';
import { PollDto } from '../dtos/PollDto';
import { GenericResponse } from '../dtos/GenericResponse';

const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };
  
  @Injectable({
    providedIn: 'root'
  })
  export class ApplicationService {
    httpBase = environment.apiBaseUrl;

    constructor(private http: HttpClient, private route:Router) { }

    callHome() : Observable<HomeDetailsDto> {
        var config = {headers:  {
            'Authorization': "Bearer "+localStorage.getItem("idToken")+"",
            'Content-Type':  'application/json'
            }
        };

        return this.http.get<HomeDetailsDto>(this.httpBase + 'app/home',config)
        .pipe(map(data => {
            return data;
        }));
    }

    getPollAnalyseDetails() : Observable<PollAnalyseDetails> {
        var config = {headers:  {
            'Authorization': "Bearer "+localStorage.getItem("idToken")+"",
            'Content-Type':  'application/json'
            }
        };

        return this.http.get<PollAnalyseDetails>(this.httpBase + 'app/home/analysePoll',config)
        .pipe(map(data => {
            return data;
        }));
    }

    createPoll(pollDto : PollDto) : Observable<GenericResponse> {
        var config = {headers:  {
            'Authorization': "Bearer "+localStorage.getItem("idToken")+"",
            'Content-Type':  'application/json'
            }
        };

        return this.http.post<GenericResponse>(this.httpBase + 'app/poll',pollDto, config)
        .pipe(map(data => {
            return data;
        }));
    }

    vote(optionId : number) : Observable<VoteResponse> {
        var config = {headers:  {
            'Authorization': "Bearer "+localStorage.getItem("idToken")+"",
            'Content-Type':  'application/json'
            }
        };

        console.log(config);
        return this.http.post<VoteResponse>(this.httpBase + 'app/vote?option='+optionId,null, config)
        .pipe(map(data => {
            return data;
        }));
    }

    analysePoll() : Observable<string> {
        var config = {headers:  {
            'Authorization': "Bearer "+localStorage.getItem("idToken")+"",
            'Content-Type':  'application/json'
            }
        };
        return this.http.post<string>(this.httpBase +'app/analysePoll',null,config)
        .pipe(map(data => {
            return data;
        }));
    }

    returnPollToOpenState() : Observable<GenericResponse> {
        var config = {headers:  {
            'Authorization': "Bearer "+localStorage.getItem("idToken")+"",
            'Content-Type':  'application/json'
            }
        };
        return this.http.post<GenericResponse>(this.httpBase +'app/returnPollToOpenState',null,config)
        .pipe(map(data => {
            return data;
        }));
    }



    endPoll(optionId : number) : Observable<string> {
        var config = {headers:  {
            'Authorization': "Bearer "+localStorage.getItem("idToken")+"",
            'Content-Type':  'application/json'
            }
        };
        return this.http.post<string>(this.httpBase +'app/endPoll?option='+optionId,null,config)
        .pipe(map(data => {
            return data;
        }));
    }
  }