import { User } from "@/type/user"

export interface Feedback {
  id: number
  user: User
  rating: number
  comment: string
  staffReply: string
  replied: boolean
  createdAt: string
  updatedAt: string
}