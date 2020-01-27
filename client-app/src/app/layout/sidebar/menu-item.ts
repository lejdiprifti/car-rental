import { RoleEnum } from '@ikubinfo/core/models/role.enum';

export interface MenuItem {
    url?: string;
    icon?: string;
    label?: string;
    allowedRoles?: RoleEnum[];
}