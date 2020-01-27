import { AuthService } from '@ikubinfo/core/services/auth.service';
import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { menuItems } from './menu-items.constants';
import { MenuItem } from './menu-item';

@Component({
  selector: 'ikubinfo-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  isActive: boolean;
  collapsed: boolean;
  showMenu: string;
  pushRightClass: string;
  items: MenuItem[] = [];

  @Output() collapsedEvent = new EventEmitter<boolean>();

  constructor(public router: Router, private authService: AuthService) {
    this.router.events.subscribe(val => {
      if (
        val instanceof NavigationEnd &&
        window.innerWidth <= 992 &&
        this.isToggled()
      ) {
        this.toggleSidebar();
      }
    });
  }

  loadMenu(): void {
    if (this.authService.isLoggedIn) {
      this.items = menuItems.filter(item => item.allowedRoles.includes(this.authService.user.role.id));
    }
  }

  ngOnInit() {
    this.isActive = false;
    this.collapsed = false;
    this.showMenu = '';
    this.pushRightClass = 'push-right';
    this.loadMenu();
  }


  eventCalled() {
    this.isActive = !this.isActive;
  }

  addExpandClass(element: any) {
    if (element === this.showMenu) {
      this.showMenu = '0';
    } else {
      this.showMenu = element;
    }
  }

  toggleCollapsed() {
    this.collapsed = !this.collapsed;
    this.collapsedEvent.emit(this.collapsed);
  }

  isToggled(): boolean {
    const dom: Element = document.querySelector('body');
    return dom.classList.contains(this.pushRightClass);
  }

  toggleSidebar() {
    const dom: any = document.querySelector('body');
    dom.classList.toggle(this.pushRightClass);
  }

  rltAndLtr() {
    const dom: any = document.querySelector('body');
    dom.classList.toggle('rtl');
  }

  onLoggedout() {
    localStorage.removeItem('isLoggedin');
  }
}