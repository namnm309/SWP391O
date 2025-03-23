export interface Vaccine {
  category: string
  id: number
  title: string
  description?: string
  schedule: string
  vaccine_age?: string
  price: number
  stock: number
  discount: number
  discountPrice: number
  date_of_manufacture: string
  vaccine_expiry_date?: string
  manufacturer?: string
  targetGroup?: string
  sideEffects?: string
  isActive?: boolean
  available?: boolean
  image_url?: string
  createdAt: string
}