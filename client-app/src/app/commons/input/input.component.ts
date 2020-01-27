import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'ikubinfo-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css']
})
export class InputComponent implements OnInit {

  @Input() errorMsg: string;
  @Input() showErrorMsg: boolean;
  @Input() label: string;

  constructor() { }

  ngOnInit() {
  }

}
