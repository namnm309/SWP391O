export interface OrderDetail {
  id: number
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
  orderId: number
  orderDate: string
  status: string
  paymentType: string
  totalPrice: number
  orderDetails: OrderDetail[]
}

export interface OrdersResponse {
  code: number
  message: string
  result: Order[]
}
