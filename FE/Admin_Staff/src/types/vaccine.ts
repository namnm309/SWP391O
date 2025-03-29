import { Category } from "./category";

export interface Vaccine {
  category: Category
  id: number,
  title: string,
  description: string,
  price: number,
  discount: number,
  discountPrice: number,
  quantity: number,
  isActive: true,
  isPriority: true,
  numberOfDoses: number,
  minAgeMonths: number,
  maxAgeMonths: number,
  minDaysBetweenDoses: number,
  manufacturer: string,
  targetGroup: string,
  schedule: string,
  sideEffects: string,
  image: string,
  categoryId: number,
  categoryName: string,
}