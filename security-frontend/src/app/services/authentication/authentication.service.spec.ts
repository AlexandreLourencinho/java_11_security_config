import {TestBed} from '@angular/core/testing';

import {AuthenticationService} from './authentication.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {SignupService} from "@services/authentication/signup.service";
import {LoginService} from "@services/authentication/login.service";
import {HttpClient} from "@angular/common/http";

describe('AuthenticationService', () => {
  let service: AuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: HttpClient, useClass: HttpClientTestingModule},
        SignupService,
        LoginService
      ]
    });
    service = TestBed.inject(AuthenticationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get login service', () => {
    expect(service.getLoginService()).toBeDefined();
  });

  it('should get signup service', () => {
    expect(service.getSignupService()).toBeDefined();
  });
});
