import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'ikubinfo-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {

  @Input() header: string;

  constructor() { }

  ngOnInit() {
  }

}
