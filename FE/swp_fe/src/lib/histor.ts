export interface OrderHistory {
  orderId: string;
  orderDate: string;
  status: string;
  paymentType: string;
  totalPrice: number;
  orderDetails: OrderDetail[];
}

export interface OrderDetail {
  id: string;
  productName: string;
  quantity: number;
  orderId: string;
  vaccinationDate: string;
  price: number;
  firstName: string;
  lastName: string;
  email: string;
  mobileNo: string;
}
