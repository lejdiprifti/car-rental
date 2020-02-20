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
  constructor(
    private userService: UserService,
    private logger: LoggerService
  ) {}

  ngOnInit() {
    this.loadClients();
    this.cols = cols;
  }

  loadClients(): void {
    this.userService.getAll().subscribe(
      res => {
        this.clients = res;
      },
      err => {
        this.logger.error("Error", err.error.message);
      }
    );
  }
}
