import { Role } from '@ikubinfo/core/models/role';


export interface User {
    id?: number;
    username?: string;
    password?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    role?: Role;
    address?:string;
    phone?: number;
}