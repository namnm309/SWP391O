export interface Category {
  subCategories: Category[]
  id: number
  name: string
  imageName: string
  isActive: boolean
}

export interface CategoriesResponse {
  code: number
  message: string
  result: Category[]
}

