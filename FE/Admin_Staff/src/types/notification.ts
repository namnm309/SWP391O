import { User } from "./user"

export interface Notification {
  id: number
  sender: string
  user: User
  message: string
  createdAt: string
  readStatus: boolean
}