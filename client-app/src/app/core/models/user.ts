import { Role } from '@ikubinfo/core/models/role';


export interface User {
    username?: string;
    password?: string;
    firstName?: string;
    lastName?: string;
    id?: number;
    role?: Role;
    address?:string;
    phone?: number;
}