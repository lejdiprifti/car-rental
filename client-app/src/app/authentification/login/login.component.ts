
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AuthService } from '@ikubinfo/core/services/auth.service';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';

@Component({
  selector: 'ikubinfo-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private logger: LoggerService
  ) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  login(): void {
    this.authService.login(this.loginForm.value).subscribe((data:any) => {
      this.authService.setData(data);
      this.router.navigate(['/rental']);
      this.logger.success('Success', 'You logged in successfully!');
    }, err=>{
      this.logger.error('Error', 'Username or password is incorrect!');
    });
  }
}