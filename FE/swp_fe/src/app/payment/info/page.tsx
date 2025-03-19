"use client";

import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import { CheckCircle, XCircle, Home } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

export default function PaymentResponse() {
  const searchParams = useSearchParams();
  const [isSuccess, setIsSuccess] = useState<boolean | null>(null);
  const [responseCode, setResponseCode] = useState<string | null>(null);
//   const [orderInfo, setOrderInfo] = useState<string | null>(null);
  const [amount, setAmount] = useState<string | null>(null);

  useEffect(() => {
    // Get the response code from URL parameters
    const code = searchParams.get("vnp_ResponseCode");
    // const info = searchParams.get("vnp_OrderInfo");
    const paymentAmount = searchParams.get("vnp_Amount");

    setResponseCode(code);
    // setOrderInfo(info);
    setAmount(paymentAmount);

    // Check if payment was successful
    setIsSuccess(code === "00");
  }, [searchParams]);

  // Format currency
  const formatCurrency = (amount: string | null) => {
    if (!amount) return "0 VNĐ";
    // VNPay returns amount with extra 00, so divide by 100
    const value = Number.parseInt(amount) / 100;
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(value);
  };

  if (isSuccess === null) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="h-16 w-16 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
      </div>
    );
  }

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 p-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader
          className={`${isSuccess ? "bg-green-50" : "bg-red-50"} rounded-t-lg`}
        >
          <CardTitle className="flex items-center justify-center gap-2 text-xl">
            {isSuccess ? (
              <>
                <CheckCircle className="h-8 w-8 text-green-500" />
                <span className="text-green-700">Thanh toán thành công</span>
              </>
            ) : (
              <>
                <XCircle className="h-8 w-8 text-red-500" />
                <span className="text-red-700">Thanh toán thất bại</span>
              </>
            )}
          </CardTitle>
        </CardHeader>

        <CardContent className="mt-4 space-y-4">
          <div className="rounded-lg bg-gray-50 p-4">
            <h3 className="mb-2 font-medium text-gray-700">
              Thông tin giao dịch
            </h3>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-gray-500">Mã phản hồi:</span>
                <span className="font-medium">{responseCode}</span>
              </div>
              {/* {orderInfo && (
                <div className="flex justify-between">
                  <span className="text-gray-500">Thông tin đơn hàng:</span>
                  <span className="font-medium">{orderInfo}</span>
                </div>
              )} */}
              {amount && (
                <div className="flex justify-between">
                  <span className="text-gray-500">Số tiền:</span>
                  <span className="font-medium">{formatCurrency(amount)}</span>
                </div>
              )}
              <div className="flex justify-between">
                <span className="text-gray-500">Trạng thái:</span>
                <span
                  className={`font-medium ${
                    isSuccess ? "text-green-600" : "text-red-600"
                  }`}
                >
                  {isSuccess ? "Thành công" : "Thất bại"}
                </span>
              </div>
            </div>
          </div>

          {isSuccess ? (
            <div className="rounded-lg bg-green-50 p-4 text-center text-green-700">
              <p>
                Cảm ơn bạn đã thanh toán. Đơn hàng của bạn đã được xác nhận.
              </p>
            </div>
          ) : (
            <div className="rounded-lg bg-red-50 p-4 text-center text-red-700">
              <p>
                Rất tiếc, giao dịch không thành công. Vui lòng thử lại hoặc liên
                hệ hỗ trợ.
              </p>
            </div>
          )}
        </CardContent>

        <CardFooter className="flex flex-col gap-3 sm:flex-row">
          <Button variant="outline" className="w-full" asChild>
            <Link href="/">
              <Home className="mr-2 h-4 w-4" />
              Trang chủ
            </Link>
          </Button>
          {/* <Button
            className={`w-full ${
              isSuccess
                ? "bg-green-600 hover:bg-green-700"
                : "bg-blue-600 hover:bg-blue-700"
            }`}
            asChild
          >
            <Link href={isSuccess ? "/account/orders" : "/checkout"}>
              {isSuccess ? "Xem đơn hàng" : "Thử lại"}
            </Link>
          </Button> */}
        </CardFooter>
      </Card>
    </div>
  );
}
