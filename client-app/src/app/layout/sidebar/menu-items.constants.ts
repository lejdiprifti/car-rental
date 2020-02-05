import { RoleEnum as Role } from './../../core/models/role.enum';
import { MenuItem } from './menu-item'

export const menuItems: Array<MenuItem> = [{
    url: '/rental/dashboard',
    icon: 'fa-dashboard',
    label: 'Dashboard',
    allowedRoles: [Role.ADMIN]
}, { url: '/rental/cars',
     icon: 'fas fa-car',
     label: 'Dashboard',
     allowedRoles: [Role.USER]
},{
    url: '/rental/reservations',
    icon: 'fa fa-list',
    label: 'Reservations',
    allowedRoles: [Role.USER, Role.ADMIN]
},
{
    url: '/rental/user',
    icon: 'fa fa-user',
    label: 'User',
    allowedRoles: [Role.USER]
},{
    url: '/rental/cars',
    icon: 'fas fa-car',
    label: 'Cars',
    allowedRoles: [Role.ADMIN]
},
{
    url: '/rental/categories',
    icon: 'fas fa-sitemap',
    label: 'Categories',
    allowedRoles: [Role.ADMIN]
}]