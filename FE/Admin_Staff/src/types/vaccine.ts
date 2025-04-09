export interface Vaccine {
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

export interface VaccineOrder {
  date: string 
  id: number
  name: string
  price: number
  status: string
}

export enum VaccineStatus {
  DA_LEN_LICH = "Đã lên lịch",
  DA_TIEM = "Đã tiêm",
  CHUA_TIEM = "Chưa tiêm",
  QUA_HAN = "Quá hạn",
  DA_HUY = "Đã huỷ",
}