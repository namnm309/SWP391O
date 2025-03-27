"use client";

import { useState, useEffect, useCallback } from "react";
import Link from "next/link";
import { useParams } from "next/navigation";
import { format } from "date-fns";
import { vi } from "date-fns/locale";
import {
  ArrowLeft,
  Clock,
  FileText,
  CheckCircle,
  AlertCircle,
  Truck,
  Package,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { Steps, Step } from "@/components/ui/steps";
import { OrderHistory } from "@/lib/histor";
import { getOrderById } from "@/api/order";
 
export default function OrderDetailPage() {
  const params = useParams();
  const orderId = params.orderId as string;
  const [order, setOrder] = useState<OrderHistory | null>(null);
  const [loading, setLoading] = useState(true);
  const fetchProducts = useCallback(async () => {
    try {
      const response = await getOrderById(orderId);
      setOrder(response.result);
      setLoading(false);
    } catch (error) {
      console.error(error);
    }
  }, [orderId]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  if (loading) {
    return (
      <div className="container mx-auto flex min-h-[400px] items-center justify-center px-4 py-8">
        <div className="h-16 w-16 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
      </div>
    );
  }

  if (!order) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="mb-6">
          <Button variant="ghost" asChild>
            <Link href="/lich-su">
              <ArrowLeft className="mr-2 h-4 w-4" />
              Quay lại danh sách đơn hàng
            </Link>
          </Button>
        </div>
        <Card>
          <CardContent className="flex min-h-[300px] flex-col items-center justify-center p-6">
            <AlertCircle className="mb-4 h-12 w-12 text-red-500" />
            <h2 className="mb-2 text-xl font-bold">Không tìm thấy đơn hàng</h2>
            <p className="text-center text-gray-500">
              Đơn hàng với mã {orderId} không tồn tại hoặc đã bị xóa.
            </p>
          </CardContent>
        </Card>
      </div>
    );
  }

  // Format currency to VND
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  // Get status badge
  const getStatusBadge = (status: string) => {
    switch (status) {
      case "Order Received":
        return <Badge variant="secondary">Đã nhận đơn</Badge>;
      case "Processing":
        return <Badge variant="secondary">Đang xử lý</Badge>;
      case "Success":
        return <Badge variant="secondary">Hoàn thành</Badge>;
      case "Cancelled":
        return <Badge variant="destructive">Đã hủy</Badge>;
      default:
        return <Badge>{status}</Badge>;
    }
  };

  // Translate payment type
  const translatePaymentType = (type: string) => {
    switch (type) {
      case "VNPAY":
        return "VNPAY";
      case "Cash":
        return "Tiền mặt";
      default:
        return type;
    }
  };

  // Get current step based on status
  const getCurrentStep = (status: string) => {
    switch (status) {
      case "Order Received":
        return 0;
      case "Processing":
        return 1;
      case "Success":
        return 3;
      case "Cancelled":
        return -1;
      default:
        return 0;
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-6">
        <Button variant="ghost" asChild>
          <Link href="/lich-su/">
            <ArrowLeft className="mr-2 h-4 w-4" />
            Quay lại danh sách đơn hàng
          </Link>
        </Button>
      </div>

      <div className="grid gap-6 md:grid-cols-3">
        <div className="md:col-span-2">
          <Card>
            <CardHeader>
              <div className="flex flex-col justify-between gap-2 sm:flex-row sm:items-center">
                <div>
                  <CardTitle className="flex items-center gap-2">
                    Đơn hàng #{order.orderId}
                    {getStatusBadge(order.status)}
                  </CardTitle>
                  <CardDescription>
                    Ngày đặt:{" "}
                    {format(new Date(order.orderDate), "dd/MM/yyyy", {
                      locale: vi,
                    })}
                  </CardDescription>
                </div>
                <div>
                  <span className="font-medium">
                    {formatCurrency(order.totalPrice)}
                  </span>
                  <span className="ml-2 text-sm text-gray-500">
                    ({translatePaymentType(order.paymentType)})
                  </span>
                </div>
              </div>
            </CardHeader>

            <CardContent>
              {order.status !== "Cancelled" && (
                <div className="mb-8">
                  <Steps currentStep={getCurrentStep(order.status)}>
                    <Step
                      icon={Package}
                      title="Đã nhận đơn"
                      description="Đơn hàng đã được tiếp nhận"
                    />
                    <Step
                      icon={Truck}
                      title="Đang xử lý"
                      description="Đơn hàng đang được xử lý"
                    />
                    <Step
                      icon={Clock}
                      title="Chờ tiêm"
                      description="Đang chờ đến ngày tiêm"
                    />
                    <Step
                      icon={CheckCircle}
                      title="Hoàn thành"
                      description="Đơn hàng đã hoàn thành"
                    />
                  </Steps>
                </div>
              )}

              <h3 className="mb-4 text-lg font-medium">Chi tiết đơn hàng</h3>
              <div className="space-y-4">
                {order.orderDetails.map((detail, index) => (
                  <div key={index} className="rounded-lg border p-4">
                    <div className="flex flex-col justify-between gap-2 sm:flex-row sm:items-start">
                      <div>
                        <h4 className="font-medium">{detail.productName}</h4>
                        <p className="text-sm text-gray-500">
                          Số lượng: {detail.quantity}
                        </p>
                        {detail.vaccinationDate && (
                          <p className="text-sm text-gray-500 flex items-center">
                            <Clock className="mr-1 h-3 w-3" />
                            Ngày tiêm:{" "}
                            {format(
                              new Date(detail.vaccinationDate),
                              "dd/MM/yyyy",
                              { locale: vi }
                            )}
                          </p>
                        )}
                      </div>
                      <div className="flex items-center gap-2">
                        {order.status === "Success" && (
                          <Button variant="outline" size="sm" asChild>
                            <Link href={`/lich-su/${order.orderId}/bao-cao`}>
                              <FileText className="mr-2 h-4 w-4" />
                              Báo cáo phản ứng
                            </Link>
                          </Button>
                        )}
                      </div>
                    </div>
                    <Separator className="my-3" />
                    <div>
                      <h5 className="mb-2 text-sm font-medium">
                        Thông tin người tiêm
                      </h5>
                      <div className="grid gap-2 text-sm md:grid-cols-2">
                        <div>
                          <span className="text-gray-500">Họ tên: </span>
                          <span>
                            {detail.firstName} {detail.lastName}
                          </span>
                        </div>
                        <div>
                          <span className="text-gray-500">Email: </span>
                          <span>{detail.email}</span>
                        </div>
                        <div>
                          <span className="text-gray-500">Số điện thoại: </span>
                          <span>{detail.mobileNo}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>

        <div>
          <Card>
            <CardHeader>
              <CardTitle>Tóm tắt đơn hàng</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span className="text-gray-500">Tổng tiền hàng</span>
                  <span>{formatCurrency(order.totalPrice)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Phí vận chuyển</span>
                  <span>Miễn phí</span>
                </div>
                <Separator />
                <div className="flex justify-between font-medium">
                  <span>Tổng thanh toán</span>
                  <span>{formatCurrency(order.totalPrice)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Phương thức thanh toán</span>
                  <span>{translatePaymentType(order.paymentType)}</span>
                </div>
              </div>
            </CardContent>
            <CardFooter className="flex-col gap-2">
              <Button className="w-full" variant="outline" asChild>
                <Link href="/lich-su">Quay lại danh sách đơn hàng</Link>
              </Button>
              {order.status === "Success" && (
                <Button className="w-full" asChild>
                  <Link href={`/lich-su/${order.orderId}/bao-cao`}>
                    <FileText className="mr-2 h-4 w-4" />
                    Báo cáo phản ứng sau tiêm
                  </Link>
                </Button>
              )}
            </CardFooter>
          </Card>
        </div>
      </div>
    </div>
  );
}
