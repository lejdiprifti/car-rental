import { Component, OnInit } from "@angular/core";

import { UserService } from "@ikubinfo/core/services/user.service";
import { User } from "@ikubinfo/core/models/user";
import { LoggerService } from "@ikubinfo/core/utilities/logger.service";
import { cols } from "@ikubinfo/suggestion/clients/clients.constants";

@Component({
  selector: "ikubinfo-clients",
  templateUrl: "./clients.component.html",
  styleUrls: ["./clients.component.css"]
})
export class ClientsComponent implements OnInit {
  clients: Array<User>;
  cols: any[];
  totalRecords: number;
  first: number = 0;
  name: string;
  constructor(
    private userService: UserService,
    private logger: LoggerService
  ) {}

  ngOnInit() {
    this.loadClients(0,10);
    this.cols = cols;
  }

  loadClients(startIndex: number, pageSize:number, name?: string): void {
    this.userService.getAll(startIndex, pageSize, name).subscribe(
      res => {
        this.clients = res.userList;
        this.totalRecords = res.totalRecords;
      },
      err => {
        this.logger.error("Error", err.error.message);
      }
    );
  }

  paginate(event): void {
    this.first = event.first;
    this.loadClients(this.first, 10, this.name);
  }

  search(name: string): void {
    this.name = name;
    console.log(name);
    this.loadClients(this.first, 10, name);
  }
}
