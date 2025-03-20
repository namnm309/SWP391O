"use client";
import React from "react";
import Link from "next/link";
import { Order, OrderDetail } from "./../lib/order";

const CartItem = ({ item }: { item: OrderDetail }) => {
  return (
    <>
      <div className="flex items-center py-4 border-b">
        <Link href={`/product/${item?.productId?.id}`}>
          <img
            src={item?.productId?.images && item?.productId?.images[0]?.image}
            alt={item?.productId?.name}
            className="w-20 h-20 object-cover mr-4"
          />
        </Link>
        <div className="flex-grow">
          <Link
            href={`/product/${item?.productId?.id}`}
            className="font-medium"
          >
            {item?.productId?.name}
          </Link>
          {/* <p className="text-sm text-gray-500">{item.quantity}</p> */}
          <p className="text-sm text-gray-500 max-w-50 truncate">
            {item?.productId?.price}*{item?.quantity}
          </p>
        </div>
        <div className="text-right">
          <>
            <p className="font-bold text-blue-600">
              {item.price.toLocaleString()}đ
            </p>
          </>
        </div>
      </div>
    </>
  );
};

export default function OrderCard({ order }: { order: Order }) {
  return (
    <div className=" mx-auto bg-white rounded-lg shadow-md overflow-hidden border border-[#7F7F7F]">
      <div className="p-4 bg-white flex  justify-between items-center border-b border-black">
        <div className="flex items-center ">
          <h2 className="text-lg font-bold mr-4"> </h2>
        </div>
        <div className="flex gap-1">
          <span className=" font-medium">|</span>
          <span className="text-blue-800 font-medium text-center">
            {order.status}
          </span>
        </div>
      </div>
      <div className="p-4">
        {order?.order_details &&
          order?.order_details.map((item) => (
            <CartItem key={item.id} item={item} />
          ))}
      </div>
      <div className="p-4 bg-white  border-t border-[#7F7F7F]">
        <div className="flex justify-end  gap-2 items-center mb-4 ">
            <span className="font-medium">Total:</span>
          <span className="font-bold text-[#002278] text-xl">
            {order?.amount?.toLocaleString()}đ
          </span>
        </div>
      </div>
    </div>
  );
}
