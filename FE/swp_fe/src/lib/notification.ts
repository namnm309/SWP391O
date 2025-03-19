export interface Notification {
  id: number;
  user: User;
  message: string;
  createdAt: string;
  readStatus: boolean;
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
  permissions: string[];
}
