"use client"
import { useState, useEffect, useCallback } from "react"
import type { ColumnDef } from "@tanstack/react-table"
import { Eye, ShoppingCart, CreditCard, Calendar, Plus, Ban } from "lucide-react"
import { DataTable } from "@/components/ui/data-table"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast";
import { Badge } from "@/components/ui/badge"
import type { Order } from "@/types/order"
import { OrderDetailsModal } from "@/components/modals/OrderDetail"
import axios from "@/utils/axiosConfig"
import { RegisterVaccinationModal } from "@/components/modals/RegisterVaccinationModal"
import { CancelOrderModal } from "@/components/modals/CancelOrderModal"

export default function OrdersPage() {
  const { toast } = useToast()

  const [orders, setOrders] = useState<Order[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null)
  const [showCreateOrderModal, setShowCreateOrderModal] = useState(false)
  const [orderToCancel, setOrderToCancel] = useState<string | null>(null)

  const loadOrders = useCallback(async () => {
    try {
      setLoading(true)
      const token = localStorage.getItem("token")
      const response = await axios.get("/order/all-orders", {
        headers: { Authorization: `Bearer ${token}` },
      })
      const data: Order[] = response.data.result || []
      setOrders(data)
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load orders",
        variant: "destructive",
      })
    } finally {
      setLoading(false)
    }
  }, [toast])

  useEffect(() => {
    loadOrders()
  }, [loadOrders])

  const handleViewOrder = (orderId: string) => {
    const order = orders.find((o) => o.orderId === orderId)
    if (order) {
      setSelectedOrder(order)
    }
  }

  const getStatusBadge = (status: string) => {
    switch (status.toLowerCase()) {
      case "success":
        return <Badge className="bg-green-100 text-green-800">Completed</Badge>
      case "paid":
        return <Badge className="bg-green-100 text-green-800">Completed</Badge>
      case "canceled_partial":
        return <Badge className="bg-red-100 text-red-800">Canceled Partial</Badge>
      case "cancel":
        return <Badge className="bg-red-100 text-red-800">Cancelled</Badge>
      case "cancelled":
        return <Badge className="bg-red-100 text-red-800">Cancelled</Badge>
      default:
        return <Badge className="bg-gray-100 text-gray-800">{status}</Badge>
    }
  }

  const columns: ColumnDef<Order>[] = [
    {
      accessorKey: "orderId",
      header: "Order ID",
    },
    {
      accessorKey: "paymentType",
      header: "Payment Method",
    },
    {
      accessorKey: "totalPrice",
      header: "Total",
      cell: ({ row }) => {
        const price = row.getValue("totalPrice") as number
        return new Intl.NumberFormat("vn-VN", {
          style: "currency",
          currency: "vnd",
        }).format(price)
      },
    },
    {
      accessorKey: "orderDate",
      header: "Order Date",
      cell: ({ row }) => {
        const date = row.getValue("orderDate") as string
        return new Date(date).toLocaleDateString()
      },
    },
    {
      accessorKey: "status",
      header: "Status",
      cell: ({ row }) => {
        const status = row.getValue("status") as string
        return <span className="capitalize">{getStatusBadge(status)}</span>
      },
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const orderId = row.getValue("orderId") as string

        return (
          <div className="flex gap-2">
            <Button variant="outline" size="sm" className="hover:cursor-pointer" onClick={() => handleViewOrder(orderId)}>
              <Eye className="mr-2 h-4 w-4" />
              View
            </Button>

            <Button variant="outline" size="sm" className="text-red-500 hover:text-red-700 hover:cursor-pointer" onClick={() => setOrderToCancel(orderId)}>
              <Ban className="mr-2 h-4 w-4" />
              Cancel
            </Button>
          </div>
        )
      },
    },
  ]

  const totalOrders = orders.length
  const totalRevenue = orders.reduce((sum, order) => sum + order.totalPrice, 0)
  const pendingOrders = orders.filter((order) => order.status.toLowerCase() === "pending").length

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Orders Management</h1>
        <Button onClick={() => setShowCreateOrderModal(true)}>
          <Plus className="mr-2 h-4 w-4" />
          Create Order
        </Button>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Orders</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center">
              <ShoppingCart className="mr-2 h-5 w-5 text-blue-600" />
              <div className="text-2xl font-bold">{totalOrders}</div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Revenue</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center">
              <CreditCard className="mr-2 h-5 w-5 text-green-600" />
              <div className="text-2xl font-bold">
                {new Intl.NumberFormat("vn-VN", {
                  style: "currency",
                  currency: "vnd",
                }).format(totalRevenue)}
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Pending Orders</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center">
              <Calendar className="mr-2 h-5 w-5 text-yellow-600" />
              <div className="text-2xl font-bold">{pendingOrders}</div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>All Orders</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p>Loading orders...</p>
            </div>
          ) : (
            <DataTable
              columns={columns}
              data={orders}
              searchColumn="orderId"
              searchPlaceholder="Search by order ID..."
            />
          )}
        </CardContent>
      </Card>

      {selectedOrder && (
        <OrderDetailsModal
          order={selectedOrder}
          onClose={() => setSelectedOrder(null)}
        />
      )}

      {showCreateOrderModal && (
        <RegisterVaccinationModal
          open={showCreateOrderModal}
          onClose={() => setShowCreateOrderModal(false)}
        />
      )}

      {orderToCancel && (
        <CancelOrderModal
          orderId={orderToCancel}
          onClose={() => {
            setOrderToCancel(null)
            loadOrders()
          }}
        />
      )}
    </div>
  )
}

