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
  relatives: Relative[],
  avatarUrl: string
}

export interface Relative {
  relativeId: number,
  fullname: string,
  relationshipType: string
}