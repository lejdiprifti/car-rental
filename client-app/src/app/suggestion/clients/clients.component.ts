import { Component, OnInit } from '@angular/core';
import { UserService } from '@ikubinfo/core/services/user.service';
import { User } from '@ikubinfo/core/models/user';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';

@Component({
  selector: 'ikubinfo-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {

  clients: Array<User>;
  cols: any[];
  constructor(private userService: UserService, private logger: LoggerService) { }

  ngOnInit() {
    this.loadClients();
    this.cols = [
      { field: 'firstName', header: 'First Name' },
      { field: 'lastName', header: 'Last Name' },
      { field: 'email', header: 'E-mail' },
      { field: 'address', header: 'Address' },
      {field: 'phone', header: 'Phone no.'},
      {field: 'mailto', header: 'Send mail'}
    ]
  }


  loadClients(): void {
    this.userService.getAll().subscribe(res => {
      this.clients = res;
    }, err=> {
      this.logger.error('Error', err.error.message);
    })
  }

}
