import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '@ikubinfo/core/services/auth.service';
import { User } from '@ikubinfo/core/models/user';
import { ConfirmationService } from 'primeng/primeng';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
import { Router } from '@angular/router';
import { RegisterService } from '@ikubinfo/core/services/register.service';
import { DatePipe } from '@angular/common';
import { RegisterComponent } from '@ikubinfo/authentification/register/register.component';
import { UserService } from '@ikubinfo/core/services/user.service';

@Component({
  selector: 'ikubinfo-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  settingsForm: FormGroup;
  updateUser: User;
  passwordForm: FormGroup;
  user: User;
   constructor(private confirmationService: ConfirmationService,
               private logger: LoggerService,private router: Router ,
               private fb:FormBuilder,
               private userService: UserService,
               private authService: AuthService,
               private datePipe: DatePipe) { 
     this.updateUser={
 
     }
     
   }
 
   ngOnInit() {
    this.settingsForm=this.fb.group({
      birthdate: ['',
    [
      RegisterComponent.isOldEnough,Validators.required
    ]
  ],
    email: [
      "",
      [
        Validators.pattern("[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,}$"),Validators.required
      ]
    ],
    address: [
      "", Validators.required
    ],
    firstName: [
      "", Validators.required
    ],
    lastName: [
      "", Validators.required
    ],
    phone: [
      "", [
        Validators.required, Validators.minLength(7)
      ]
    ]
    });
    this.loadData();
     this.passwordForm=this.fb.group({
       password: [
         "",
         [
           Validators.pattern("(?=.*d)(?=.*[a-z])(?=.*[A-Z]).{8,}")
         ]
       ],
       repeatPassword: [
         "",
         [
           Validators.required
         ]
       ]
     },
     {validators: RegisterComponent.passwordMatch});
 
    
   }
 
   loadData(): void {
    this.userService.getUserById(this.authService.user.id).subscribe(
      result => {
        this.user = result;
        const dateString = this.user.birthdate;
        const newDate= new Date(dateString);
        const convertedDate = this.datePipe.transform(newDate, "yyyy-MM-dd");
        this.settingsForm.get('birthdate').setValue(convertedDate);
        this.settingsForm.get('address').setValue(this.user.address);
        this.settingsForm.get('email').setValue(this.user.email);
        this.settingsForm.get('firstName').setValue(this.user.firstName);
        this.settingsForm.get('lastName').setValue(this.user.lastName);
        this.settingsForm.get('phone').setValue(this.user.phone);
      }
    );
   }
 
 updatePassword(): void {
     if (this.passwordForm.value.password !== ""){
       this.updateUser.password=this.passwordForm.value.password;
     }
     this.confirmationService.confirm({
       message: 'Do you want to save your data?',
       header: 'Save Confirmation',
       icon: 'pi pi-info-circle',
       accept: () => {
         this.userService.edit(this.updateUser).subscribe(res=>{
           this.logger.success("Success", "User was updated successfully!");
       },
       err=>{
         this.logger.error("Error",err.error.message);
         this.loadData();
       });
       }
     });
 
     
 }
 updateData(): void {
     this.updateUser.birthdate=new Date(this.settingsForm.value.birthdate);
     this.updateUser.email=this.settingsForm.value.email;
     this.updateUser.address=this.settingsForm.value.address;
    this.updateUser.firstName = this.settingsForm.value.firstName;
     this.updateUser.lastName = this.settingsForm.value.lastName;
     this.updateUser.phone = this.settingsForm.value.phone;
   this.confirmationService.confirm({
     message: 'Do you want to save your data?',
     header: 'Save Confirmation',
     icon: 'pi pi-info-circle',
     accept: () => {
       this.userService.edit(this.updateUser).subscribe(res=>{
         this.router.navigate(['rental/user']);
         this.logger.success("Success", "Data saved successfully!");
     },
     err=>{
       this.logger.error("Error", err.error.message);
       this.loadData();
     });
     }
   });
   
 }
 delete(): void{
   this.confirmationService.confirm({
     message: 'Do you want to delete your account?',
     header: 'Delete Confirmation',
     icon: 'pi pi-info-circle',
     accept: () => {
       this.userService.delete().subscribe(res=>{
         this.logger.info("Info", "Account was deleted.");
         this.router.navigate(["/login"]);
       },
       err => {
         this.logger.error('Error',  err.error.message);
       });
     }
   });
 
 }
}
