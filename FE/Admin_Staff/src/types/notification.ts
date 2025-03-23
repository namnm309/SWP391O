import { User } from "./user"

export interface Notification {
  id: number
  user: User
  message: string
  createdAt: string
  readStatus: boolean
}