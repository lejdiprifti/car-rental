import { RoleEnum as Role } from './../../core/models/role.enum';
import { MenuItem } from './menu-item'

export const menuItems: Array<MenuItem> = [{
    url: '/suggestion/dashboard',
    icon: 'fa-dashboard',
    label: 'Dashboard',
    allowedRoles: [Role.ADMIN, Role.USER]
}, {
    url: '/suggestion/posts',
    icon: 'fa-table',
    label: 'Manage posts',
    allowedRoles: [Role.ADMIN]

}]