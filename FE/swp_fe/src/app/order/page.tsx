"use client";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

const OrderPage: React.FC = () => {
  const searchParams = useSearchParams();
  const order_id = searchParams.get("order_id");
  const success = searchParams.get("success");
  const [message, setMessage] = useState<string>("");
  const router = useRouter();

  useEffect(() => {
    if (order_id && success !== undefined) {
      if (success === "true") {
        setMessage(`Order ${order_id} has been successfully processed.`);
      } else {
        setMessage(`Order ${order_id} has failed. Please try again.`);
      }
    }
  }, [order_id, success]);

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h1 className="text-2xl font-bold mb-4 text-center">Order Status</h1>
        <p
          className={`text-lg ${
            success === "true" ? "text-green-600" : "text-red-600"
          } text-center`}
        >
          {message}
        </p>
        {success === "true" && (
          <button
            onClick={() => router.push(`/`)}
            className="mt-4 bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-700"
          >
            Continue Shopping
          </button>
        )}
      </div>
    </div>
  );
};

export default OrderPage;
