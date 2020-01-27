import { Role } from '@ikubinfo/core/models/role.enum';


export interface User {
    username?: string;
    email?: string;
    id?: number;
    role?: Role
}