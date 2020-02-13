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
import { StatisticsService } from '@ikubinfo/core/services/statistics.service';
import { Statistics } from '@ikubinfo/core/models/statistics';
@Component({
  selector: 'ikubinfo-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  events: {
    events: any[], 
    textColor: string
  };
  options: any;
  reservations: Array<Reservation>;
  filteredBookings: Array<any>;
  calendarPlugins = [dayGridPlugin,timeGridPlugin, interactionPlugin];
  cars: Car[];

    cols: any[];

    brands: SelectItem[];

    colors: SelectItem[];

    yearFilter: number;
    user: User;
    date: Date = null;
    endDate: Date = null;
    yearTimeout: any;

    stats: Statistics;
  constructor(private statisticsService: StatisticsService, private authService: AuthService, 
    private reservationService: ReservationService, private logger: LoggerService) { }

  ngOnInit() {
this.user = this.authService.user;
this.events =  {
  events: [],  
  textColor: 'white'
};
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
  this.stats = {};
  this.getStatistics();
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
            { field: 'startDate', header: 'Start Date' },
            {field: 'endDate', header: 'End Date'},
            {field: 'price', header: 'Total Fee'}
        ];
    }

   

    loadEvents():void {
      this.reservationService.getAll().subscribe(res=>{
        this.reservations = res;
        this.filteredBookings = this.reservations;
        this.reservations.forEach(el => {
          this.events.events.push({
            'title': el.user.firstName + ' '+ el.user.lastName + ' - ' + el.car.name + ' '+ el.car.type,
            'start': el.startDate,
            'end': el.endDate
          })
        })
      }, err=> {
        this.logger.error('Error', 'Reservations could not be found.');
      })
    }

    calculateFee(car: Car, startDate: Date, endDate: Date): number {
      return Number((car.price * (new Date(endDate).getTime() - new Date(startDate).getTime())/(24*60*60*1000)).toFixed(2));
    }

    filterBookings(): void {
      if (this.date && !this.endDate){
      this.reservations = this.filteredBookings;
      this.reservations = this.reservations.filter(el => this.date.getTime() < new Date(el.startDate).getTime())
      } else if (this.endDate && !this.date){
        this.reservations = this.filteredBookings;
        this.reservations = this.reservations.filter(el => this.endDate.getTime() > new Date(el.endDate).getTime())
      } else if (this.date && this.endDate){
        this.reservations = this.filteredBookings;
        this.reservations = this.reservations.filter(el => this.date.getTime() < new Date(el.startDate).getTime())
        this.reservations = this.reservations.filter(el => this.endDate.getTime() > new Date(el.endDate).getTime())
      } else {
        this.reservations = this.filteredBookings;
      }
    }

    filterBookingsByPrice(event) {
        this.reservations = this.filteredBookings;
        this.reservations = this.reservations.filter(el => this.calculateFee(el.car, el.startDate, el.endDate) > event.target.value);
    }

    getStatistics(): void {
      this.statisticsService.getStatistics().subscribe(res => {
        this.stats = res;
      }, err => {
        this.logger.error("Error", "Could not retrieve statistics.");
      })
    }
}


