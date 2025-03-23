export interface Feedback {
  id: number;
  user: User;
  rating: number;
  comment: string;
  staffReply: string;
  replied: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface User {
  id: number;
  parentid: string;
  username: string;
  fullname: string;
  password: string;
  email: string;
  phone: string;
  bod: string;
  gender: string;
  height: string;
  weight: string;
  enabled: boolean;
  verificationcode: string;
  verficationexpiration: string;
  roles: Role[];
  accountNonLocked: string;
  resetToken: string;
  avatarUrl: string;
}

export interface Role {
  name: string;
  description: string;
  permissions: [];
}
