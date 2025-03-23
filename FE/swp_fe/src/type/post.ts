import { Category } from "./vaccine"

export interface Post {
  id: number
  title: string
  content: string,
  excerpt: string
  date: string
  image: string
  authorId: 0,
  authorName: string,
  createdAt: string,
  updatedAt: string,
  imageList: [
    string
  ]
}

    