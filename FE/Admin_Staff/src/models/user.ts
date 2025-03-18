export enum UserRole {
  ADMIN = 'ROLE_ADMIN',
  STAFF = 'ROLE_STAFF',
  CUSTOMER = 'ROLE_CUSTOMER',
  CHILD = "ROLE_CHILD"
}

export interface User {
  id: number;
  username: string;
  fullName: string;
  email?: string;
  token: string;
  role: UserRole;
}

export interface UserProfile {
  avatar?: string;
  id: number | string;
  username: string;
  fullName: string;
  email: string;
  phone: string;
  address: string;
  dob: string;
  role: UserRole;
  gender: string;
}

export interface ChildProfile {
  id?: string;
  fullName: string;
  dob: string;
  gender: string;
}

export interface Patient {
  email: any;
  id: number
  username: string
  fullName: string
  dateOfBirth: string
  phone: string
  role: UserRole
  avatar: string
  vaccineStatus: string
  parent?: Patient
  children?: Patient[]
}