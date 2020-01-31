import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { RegisterService } from '@ikubinfo/core/services/register.service';
import { Role } from '@ikubinfo/core/models/role';
import { Router } from '@angular/router';
import { User } from '@ikubinfo/core/models/user';

@Component({
  selector: 'ikubinfo-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  passwordForm: FormGroup;
  role: Role;
  registerUser: User;

  static isOldEnough(control: AbstractControl): any {
    const birthDatePlus18 = new Date(control.value);
    birthDatePlus18.setFullYear(birthDatePlus18.getFullYear() + 18);
    return birthDatePlus18 < new Date() ? null : { tooYoung: true };
  }

static passwordMatch(group: FormGroup):any{
  const password= group.get('password').value;
  const repeatPassword= group.get('repeatPassword').value;
  return password === repeatPassword ? null : { matchingError: true};
}
  constructor(
    private router: Router,
    private fb: FormBuilder,
    private  registerService: RegisterService,
    private logger: LoggerService
  ) 
  {
    this.registerUser = {};
  }
  ngOnInit() {
 
    this.passwordForm=this.fb.group({
    password: [
      "", 
      [
        Validators.required,
        Validators.pattern("(?=.*d)(?=.*[a-z])(?=.*[A-Z]).{8,}")
      ]
    ],
    repeatPassword: [
      "",
      [
        Validators.required,
      ]
    ] 
  },
  {validators: RegisterComponent.passwordMatch});

  this.registerForm = this.fb.group({
    username: [
      "",
      [
        Validators.required,
        Validators.minLength(4)
      ],
     
    ],
    birthdate: ["", [Validators.required, RegisterComponent.isOldEnough]],
    firstName: ['', [Validators.required]],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    email: [
      "",
      [
        Validators.required,
        Validators.pattern("[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,}$")
      ]
    ],
    address: ["", Validators.required],
    passwordForm: this.passwordForm
   
  });


  }

  register(): void {
    this.registerUser.username = this.registerForm.value.username;
    this.registerUser.password = this.passwordForm.value.password;
    this.registerUser.address = this.registerForm.value.address;
    this.registerUser.firstName = this.registerForm.value.firstName;
    this.registerUser.lastName = this.registerForm.value.lastName;
    this.registerUser.email = this.registerForm.value.email;
    this.registerUser.phone = this.registerForm.value.phone;
    this.registerUser.birthdate = this.registerForm.value.birthdate;
    this.registerService.register(this.registerUser).subscribe(res=> {
      this.logger.success("Success", "You registered successfully.")
      this.router.navigate(["/login"]);
    }, err=>{
      this.logger.error("Error","Username is already taken.");
      this.router.navigate(['/register']);
    });
  }

}
