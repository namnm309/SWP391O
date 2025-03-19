"use client";

import { useCallback, useEffect, useState } from "react";
import Link from "next/link";
import { format } from "date-fns";
import { vi } from "date-fns/locale";
import { Search, ChevronRight, FileText, Clock } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { getOrders } from "@/api/order";
import { OrderHistory } from "@/lib/histor";
 
 
export default function OrdersPage() {
  const [orders, setOrders] = useState<OrderHistory[]>([]);
  const [filterStatus, setFilterStatus] = useState("all");
  const [searchTerm, setSearchTerm] = useState("");
 
    const fetchProducts = useCallback(async () => {
      try {
        const response = await getOrders();
        setOrders(response.result);
      } catch (error) {
        console.error(error);
      }
    }, []);
  
    useEffect(() => {
      fetchProducts();
    }, [fetchProducts]);  

  
  // Format currency to VND
  const formatCurrency = (amount:number) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  // Get status badge
  const getStatusBadge = (status:string) => {
    switch (status) {
      case "Order Received":
        return <Badge variant="secondary">Đã nhận đơn</Badge>;
      case "Processing":
        return <Badge variant="outline">Đang xử lý</Badge>;
      case "Completed":
        return <Badge variant="secondary">Hoàn thành</Badge>;
      case "Cancelled":
        return <Badge variant="destructive">Đã hủy</Badge>;
      default:
        return <Badge>{status}</Badge>;
    }
  };

  // Translate payment type
  const translatePaymentType = (type:string) => {
    switch (type) {
      case "VNPAY":
        return "VNPAY";
      case "Cash":
        return "Tiền mặt";
      default:
        return type;
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-2xl font-bold">Đơn hàng của tôi</h1>
        <p className="text-muted-foreground">
          Quản lý và theo dõi đơn hàng của bạn
        </p>
      </div>

      {/* Filters */}
      <div className="mb-6 flex flex-col gap-4 rounded-lg border bg-white p-4 sm:flex-row sm:items-center sm:justify-between">
        <div className="relative w-full sm:w-64">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 transform text-gray-400" />
          <Input
            placeholder="Tìm kiếm đơn hàng..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>

        <div className="flex flex-wrap gap-2">
          <Select value={filterStatus} onValueChange={setFilterStatus}>
            <SelectTrigger className="w-[180px]">
              <SelectValue placeholder="Trạng thái" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">Tất cả trạng thái</SelectItem>
              <SelectItem value="Order Received">Đã nhận đơn</SelectItem>
              <SelectItem value="Processing">Đang xử lý</SelectItem>
              <SelectItem value="Completed">Hoàn thành</SelectItem>
              <SelectItem value="Cancelled">Đã hủy</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      {/* Orders List */}
      <div className="space-y-4">
        {orders.length > 0 ? (
          orders.map((order) => (
            <Card key={order.orderId} className="overflow-hidden">
              <CardContent className="p-0">
                <div className="border-b bg-gray-50 p-4">
                  <div className="flex flex-col justify-between gap-2 sm:flex-row sm:items-center">
                    <div>
                      <div className="flex items-center gap-2">
                        <h3 className="font-medium">
                          Đơn hàng #{order.orderId}
                        </h3>
                        {getStatusBadge(order.status)}
                      </div>
                      <p className="text-sm text-gray-500">
                        Ngày đặt:{" "}
                        {format(new Date(order.orderDate), "dd/MM/yyyy", {
                          locale: vi,
                        })}
                      </p>
                    </div>
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-medium">
                        {formatCurrency(order.totalPrice)}
                      </span>
                      <span className="text-sm text-gray-500">
                        ({translatePaymentType(order.paymentType)})
                      </span>
                    </div>
                  </div>
                </div>

                <div className="p-4">
                  <div className="space-y-3">
                    {order.orderDetails.map((detail, index) => (
                      <div
                        key={index}
                        className="flex flex-col justify-between gap-2 sm:flex-row sm:items-center"
                      >
                        <div>
                          <p className="font-medium">{detail.productName}</p>
                          <p className="text-sm text-gray-500">
                            Số lượng: {detail.quantity} | Người tiêm:{" "}
                            {detail.firstName} {detail.lastName}
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
                          {order.status === "Completed" && (
                            <Button variant="outline" size="sm" asChild>
                              <Link href={`/lich-su/${order.orderId}/bao-cao`}>
                                <FileText className="mr-2 h-4 w-4" />
                                Báo cáo phản ứng
                              </Link>
                            </Button>
                          )}
                          <Button variant="outline" size="sm" asChild>
                            <Link href={`/lich-su/${order.orderId}`}>
                              Chi tiết
                              <ChevronRight className="ml-1 h-4 w-4" />
                            </Link>
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </CardContent>
            </Card>
          ))
        ) : (
          <div className="rounded-lg border bg-white p-8 text-center">
            <p className="text-gray-500">
              Không tìm thấy đơn hàng nào.
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
