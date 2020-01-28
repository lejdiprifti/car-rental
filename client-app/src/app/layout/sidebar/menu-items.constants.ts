import { RoleEnum as Role } from './../../core/models/role.enum';
import { MenuItem } from './menu-item'

export const menuItems: Array<MenuItem> = [{
    url: '/rental/dashboard',
    icon: 'fa-dashboard',
    label: 'Dashboard',
    allowedRoles: [Role.ADMIN, Role.USER]
}, {
    url: '/rental/user',
    icon: 'fa fa-user',
    label: 'User',
    allowedRoles: [Role.USER]
},
{
    url: '/rental/categories',
    icon: 'fa fa-list',
    label: 'Categories',
    allowedRoles: [Role.ADMIN]
},
{
    url: '/rental/posts',
    icon: 'fa-table',
    label: 'Manage posts',
    allowedRoles: [Role.ADMIN]

}]