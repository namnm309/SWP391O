import { User } from "@/models/user"

export interface Feedback {
  staffReply: string
  id: number
  user: User
  rating: number
  comment?: string
  replied: boolean
  updatedAt: string
}