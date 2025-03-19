"use client";
import React, { useEffect, useState } from "react";

import OrderCard from "@/components/OrderCard";
 
import { useStorage } from "@/hooks/useLocalStorage";
import { UserLogin } from "@/lib/users";
export interface Order {
  id: number;
  accountDetail: AccountDetail;
  order_details: OrderDetail[];
  amount: string;
  status: string;
  description: string;
  content: string;
  notes: string;
  address: string;
  method: string;
  payment_url: string;
  redirect_url: string;
  order_uuid: string;
  accountId: number;
}

export interface AccountDetail {
  id: number;
  email: string;
}

export interface OrderDetail {
  id: number;
  productId: ProductId;
  quantity: number;
  price: string;
  orderId: number;
}

export interface ProductId {
  id: number;
  images: Image[];
  is_deleted: boolean;
  name: string;
  price: string;
  description: string;
  quantity: number;
  slug: string;
  created_at: string;
  is_active: boolean;
  category: Category;
}

export interface Image {
  id: number;
  image: string;
  created_at: string;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  is_deleted: boolean;
}

export default function ListOrder() {
  const [data, setData] = useState<Order[]>([]);
  const [user] = useStorage<UserLogin>("user", {
    refresh: "",
    access: "",
    userid: 0,
    username: "",
    email: "",
    phone: "",
    address: "",
    status: "",
    accountid: 0,
  });
  const [loading, setLoading] = useState(true);

  const reloadData = () => {
  
  };
  useEffect(() => {
    setLoading(true);
    reloadData();
  }, []);

  return (
    <div className="list-order-container p-10">
      {loading ? (
        <div className="flex justify-center">Loading</div>
      ) : data.length === 0 ? (
        <div className="flex justify-center">No orders available</div>
      ) : (
        data.map((order, index) => (
          <div key={order.id || index} className="mt-4">
            <OrderCard order={order} />
          </div>
        ))
      )}
    </div>
  );
}
