import { VaccineOrder } from "./vaccine"

export interface OrderDetail {
  childId: number
  childName: string
  vaccines: VaccineOrder[]
  orderdetialid: number
  productName: string
  quantity: number
  orderId: string
  vaccinationDate: string
  price: number
  firstName: string
  lastName: string
  email: string
  mobileNo: string
}

export interface Order {
  email: string
  firstName: string
  lastName: string
  mobileNo: string
  orderDate: string
  orderId: string
  paymentType: string
  status: string
  totalPrice: number
  orderDetails: OrderDetail[]
}

export interface OrdersResponse {
  code: number
  message: string
  result: Order[]
}
