import { GenderUser } from "@/types/enums";
import { UserRole } from "@/types/menu";

export interface User {
  id: number;
  name: string;
  username: string;
  fullname: string;
  email?: string;
  token: string;
  role: UserRole;
}

export interface UserProfile {
  avatar?: string;
  id: number;
  username: string;
  fullname: string;
  email: string;
  phone: string;
  address: string;
  dob: string;
  role: UserRole;
  gender: string;
}

export interface ChildProfile {
  id?: number;
  fullname: string;
  dob: string;
  gender: string;
}

export interface Patient {
  address: string;
  dob: string;
  gender: GenderUser;
  email: string;
  id: number
  username: string
  fullname: string
  dateOfBirth: string
  phone: string
  role: UserRole
  avatar: string
  vaccineStatus: string
  parent?: Patient
  children?: Patient[]
}