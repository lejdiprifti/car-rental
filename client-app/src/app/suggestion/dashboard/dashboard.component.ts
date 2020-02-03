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
import {formatDate } from '@angular/common';
@Component({
  selector: 'ikubinfo-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  events: any[];
  options: any;
  reservations: Array<Reservation>;
  bookings: Array<any>;
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
    this.events = [];
    this.bookings= [];
    this.reservations = [];
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
            { field: 'name', header: 'Car' },
            { field: 'startDate', header: 'Start Date' },
            {field: 'endDate', header: 'End Date'},
            {field: 'price', header: 'Total Fee'}
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

    loadEvents():void {
      this.reservationService.getAll().subscribe(res=>{
        this.reservations = res;
        this.reservations.forEach(el => {
          this.events.push({
            'title': el.user.firstName + ' '+ el.user.lastName + ' - ' + el.car.name + ' '+ el.car.type,
            'start': el.startDate,
            'end': el.endDate
          })
          this.organizeRezervations();
        })
      }, err=> {
        this.logger.error('Error', 'Reservations could not be found.');
      })
    }

    organizeRezervations(): void {
      console.log(new Date('2020-01-01T00:00:00.000Z').getTime());
      console.log(new Date('2021-01-01T00:00:00.000Z').getTime());
      this.reservations.forEach(el => {
        this.bookings.push({
          'user': el.user.firstName + ' ' + el.user.lastName,
          'name': el.car.name + ' - ' + el.car.type,
          'startDate':new Date(el.startDate).getTime(),
          'endDate': new Date(el.endDate).getTime(),
          'price': (el.car.price * Math.round((Math.abs((new Date(el.endDate).getTime() - new Date(el.startDate).getTime())/(24*60*60*1000)))))
        })
      })
    }

}
