import { RelationshipType } from "./enums";

export interface User {
  id: number;
  parentid: number;
  username: string;
  fullname: string;
  email: string;
  phone: string;
  bod: string;
  gender: string;
  avatarUrl: string;
}

export interface Child {
  childId: number;
  fullname: string,
  birthDate: string,
  gender: string,
  height: number,
  weight: number,
  relatives: RelationshipType,
  avatarUrl: string
  parentId?: number
}