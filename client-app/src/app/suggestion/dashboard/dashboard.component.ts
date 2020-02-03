import { Component, OnInit } from '@angular/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { SelectItem } from 'primeng/components/common/selectitem';
import { Car } from '@ikubinfo/core/models/car';
import { CarService } from '@ikubinfo/core/services/car.service';
import { User } from '@ikubinfo/core/models/user';
import { AuthService } from '@ikubinfo/core/services/auth.service';
import { ReservationService } from '@ikubinfo/core/services/reservation.service';
import { Reservation } from '@ikubinfo/core/models/reservation';
import { LoggerService } from '@ikubinfo/core/utilities/logger.service';
@Component({
  selector: 'ikubinfo-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  events: any[];
  options: any;
  reservations: Array<Reservation>;
  calendarPlugins = [dayGridPlugin,timeGridPlugin, interactionPlugin];
  cars: Car[];

    cols: any[];

    brands: SelectItem[];

    colors: SelectItem[];

    yearFilter: number;
    user: User;

    yearTimeout: any;
  constructor(private carService: CarService, private authService: AuthService, 
    private reservationService: ReservationService, private logger: LoggerService) { }

  ngOnInit() {
this.user = this.authService.user;

    this.loadCars();
    this.events = [];
    this.loadEvents();
  
    this.options = {
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      defaultDate: '2017-02-01',
      hveader: {
          left: 'pre,next',
          center: 'title',
          right: 'month,agendaWeek,agendaDay'
      },
      editable: false
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
            { label: 'Diesel', value: 'Diesel' },
            { label: 'Petrol', value: 'Petrol' }, 
            { label: 'Kerosene', value: 'Kerosene' },
            { label: 'Hydrogen', value: 'Hydrogen' },
            { label: 'Biogas', value: 'Biogas' }
        ];

        this.cols = [
            { field: 'user', header: 'User' },
            { field: 'car', header: 'Car' },
            { field: 'car.type', header: 'Brand' },
            { field: 'startDate', header: 'Start Date' },
            {field: 'endDate', header: 'End Date'}
        ];
    }

    onDateChange(event, dt) {
        if (this.yearTimeout) {
            clearTimeout(this.yearTimeout);
        }

        this.yearTimeout = setTimeout(() => {
            dt.filter(event.value, 'startDate', 'gt');
        }, 250);
    }

    loadCars(): void {
      this.carService.getAll().subscribe(res => {
        this.cars = res;
      }, err => {
        this.logger.error("Error", "Cars could not be found.");
      })
    }

    loadEvents():void {
      this.reservationService.getAll().subscribe(res=>{
        this.reservations = res;
        this.reservations.forEach(el => {
          this.events.push({
            'title': el.user.firstName + ' '+ el.user.lastName + ' - ' + el.car.name + ' '+ el.car.type,
            'start': el.startDate,
            'end': el.endDate
          })
        })
      }, err=> {
        this.logger.error('Error', 'Reservations could not be found.');
      })
    }

}
