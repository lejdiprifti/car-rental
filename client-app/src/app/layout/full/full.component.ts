import { Component } from '@angular/core';

@Component({
  selector: 'ikubinfo-full',
  templateUrl: './full.component.html',
  styleUrls: ['./full.component.css']
})
export class FullComponent {

  collapedSideBar: boolean;

  constructor() { }

  ngOnInit() { }

  receiveCollapsed($event) {
    this.collapedSideBar = $event;
  }
}