import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { RegisterService } from '@ikubinfo/core/services/register.service';

@Component({
  selector: 'ikubinfo-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private registerService: RegisterService,
    private logger: LoggerService) { }

  ngOnInit() {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      password: ['', Validators.required],
      username: ['', Validators.required],
      address: ['', Validators.required],
      phone: ['', Validators.required],
      repeatPassword: ['', Validators.required]
    });
  }

  register(): void {
    this.registerService.register(this.registerForm.value).subscribe(res => {
      this.logger.success('Success', 'You registered succesfully');
    }, err=>{
      this.logger.error('Error', 'Username is already taken.');
    })

  }

}
