import { Component, OnInit } from '@angular/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { SelectItem } from 'primeng/components/common/selectitem';
import { Car } from '@ikubinfo/core/models/car';
import { CarService } from '@ikubinfo/core/services/car.service';
@Component({
  selector: 'ikubinfo-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  events: any[];
  options: any;
  calendarPlugins = [dayGridPlugin,timeGridPlugin, interactionPlugin];
  cars: Car[];

    cols: any[];

    brands: SelectItem[];

    colors: SelectItem[];

    yearFilter: number;

    yearTimeout: any;
  constructor(private carService: CarService) { }

  ngOnInit() {
    this.loadCars();
    this.events = [
      {
          "title": "All Day Event",
          "start": "2016-01-01"
      },
      {
          "title": "Long Event",
          "start": "2020-02-02",
          "end": "2020-02-05"
      },{
        "title": "Long Event 2",
        "start": "2020-02-02",
        "end": "2020-02-05"
    },{
      "title": "Long Event 3",
      "start": "2020-02-02",
      "end": "2020-02-05"
  },{
    "title": "Long Event 4",
    "start": "2020-02-09",
    "end": "2020-02-12"
},{
  "title": "Long Event 3",
  "start": "2020-02-05",
  "end": "2020-02-09"
},{
  "title": "Long Event 3",
  "start": "2020-02-26",
  "end": "2020-02-28"
},
      {
          "title": "Repeating Event",
          "start": "2016-01-09T16:00:00"
      },
      {
          "title": "Repeating Event",
          "start": "2016-01-16T16:00:00"
      },
      {
          "title": "Conference",
          "start": "2016-01-11",
          "end": "2016-01-13"
      }
  ];

  
    this.options = {
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      defaultDate: '2017-02-01',
      hveader: {
          left: 'pre,next',
          center: 'title',
          right: 'month,agendaWeek,agendaDay'
      },
      editable: true
  };

    this.brands = [
            { label: 'All Brands', value: null },
            { label: 'Audi', value: 'Audi' },
            { label: 'BMW', value: 'BMW' },
            { label: 'Fiat', value: 'Fiat' },
            { label: 'Honda', value: 'Honda' },
            { label: 'Jaguar', value: 'Jaguar' },
            { label: 'Mercedes', value: 'Mercedes' },
            { label: 'Renault', value: 'Renault' },
            { label: 'VW', value: 'VW' },
            { label: 'Volvo', value: 'Volvo' }
        ];

        this.colors = [
            { label: 'White', value: 'White' },
            { label: 'Green', value: 'Green' },
            { label: 'Silver', value: 'Silver' },
            { label: 'Black', value: 'Black' },
            { label: 'Red', value: 'Red' },
            { label: 'Maroon', value: 'Maroon' },
            { label: 'Brown', value: 'Brown' },
            { label: 'Orange', value: 'Orange' },
            { label: 'Blue', value: 'Blue' }
        ];

        this.cols = [
            { field: 'plate', header: 'Plate' },
            { field: 'year', header: 'Year' },
            { field: 'type', header: 'Brand' },
            { field: 'color', header: 'Color' }
        ];
    }

    onYearChange(event, dt) {
        if (this.yearTimeout) {
            clearTimeout(this.yearTimeout);
        }

        this.yearTimeout = setTimeout(() => {
            dt.filter(event.value, 'year', 'gt');
        }, 250);
    }

    loadCars(): void {
      this.carService.getAll().subscribe(res => {
        this.cars = res;
      }, err => {
        
      })
    }

}
