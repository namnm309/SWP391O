import { Order } from "@/types/order";
import { useEffect, useState } from "react";

interface OrderStatusSelectProps {
  order: Order;
  updateStatus: (newStatus: string) => void;
}

const OrderStatusSelect = ({ order, updateStatus}: OrderStatusSelectProps) => {
  const [status, setStatus] = useState(order.status)

  useEffect(() => {
    setStatus(order.status)
  }, [order.status])

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newStatus = e.target.value
    setStatus(newStatus)
    updateStatus(newStatus)
  }

  const statusOptions: { [key: string]: string } = {
    IN_PROGRESS: "In Progress",
    ORDER_RECEIVED: "Order Received",
    OUT_FOR_DELIVERY: "Out For Delivery",
    CANCEL: "Cancel",
    SUCCESS: "Success",
  }

  return (
    <select
      value={status}
      onChange={handleChange}
      className="rounded border border-gray-300 p-1"
    >
      {Object.keys(statusOptions).map((key) => (
        <option key={key} value={key}>
          {statusOptions[key]}
        </option>
      ))}
    </select>
  )
}

export default OrderStatusSelect;