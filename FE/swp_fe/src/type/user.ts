export interface User {
  id: number
  parentid: number
  username: string
  fullname: string
  password: string
  email: string
  phone: string
  bod: string
  gender: string
  height: number
  weight: number
  enabled: boolean
  verificationcode: string
  verficationexpiration: string
  roles: Role[]
  accountNonLocked: boolean
  resetToken: string
  avatarUrl: string
  createAt: string
  permissions: Permission[]
}

export interface Permission {
  name: string
  description: string
}

export interface Role {
  name: string
  description: string
  permissions: Permission[]
}