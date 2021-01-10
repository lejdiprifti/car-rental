import { User } from './user';

export interface UserPage {
    totalRecords?: number,
    userList?: Array<User>
}