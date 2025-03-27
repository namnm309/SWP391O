"use client"
import { Mail, Phone, User, CreditCard, Clock } from "lucide-react"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import type { Order } from "@/types/order"
import { useEffect, useState } from "react"
import axios from "@/utils/axiosConfig"
import { toast } from "@/hooks/use-toast"
import { DateTimePicker } from "@/components/DateTimePicker"
import { format } from "date-fns"

interface OrderDetailsModalProps {
  order: Order | null
  onClose: () => void
}

export function OrderDetailsModal({ order, onClose }: OrderDetailsModalProps) {
  if (!order) return null
  const [orderDetail, setOrderDetail] = useState<Order>()

  useEffect(() => {
    const loadOrders = async () => {
      try {
        const token = localStorage.getItem("token")
        const response = await axios.get(`/order/order/${order.orderId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
        const data: Order = response.data.result
        setOrderDetail(data)
      } catch (error) {
        toast({
          title: "Error",
          description: "Failed to load orders",
          variant: "destructive",
        })
      }
    }

    loadOrders()
  }, [toast, order.orderId])

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat("vn-VN", {
      style: "currency",
      currency: "vnd",
    }).format(price)
  }

  const getStatusBadge = (status: string) => {
    switch (status.toLowerCase()) {
      case "success":
        return <Badge className="bg-green-100 text-green-800">Completed</Badge>
      case "pending":
        return <Badge className="bg-yellow-100 text-yellow-800">Pending</Badge>
      case "in progress":
        return <Badge className="bg-blue-100 text-blue-800">Processing</Badge>
      case "cancelled":
        return <Badge className="bg-red-100 text-red-800">Cancelled</Badge>
      default:
        return <Badge className="bg-gray-100 text-gray-800">{status}</Badge>
    }
  }

  const displayDateTime = (dateString: string) => {
    const date = new Date(dateString)
    return format(date, "yyyy-MM-dd'T'HH:mm:ss")
  }
  
  const VaccinationDateCell = ({ item }: { item: any }) => {
    const [editing, setEditing] = useState(false)
    const [tempDate, setTempDate] = useState<Date | undefined>(
      item.vaccinationDate ? new Date(item.vaccinationDate) : undefined
    )
  
    const handleSetDate = async (newDate: Date | undefined) => {
      if (!newDate) return
      try {
        const token = localStorage.getItem("token")
  
        const formattedDate = format(newDate, "yyyy-MM-dd'T'HH:mm:ss")
  
        await axios.put(
          "/order/update-vaccination-date-mail",
          null,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              orderDetailId: item.orderdetialid,
              vaccinationDate: formattedDate,
            },
          }
        )
  
        item.vaccinationDate = formattedDate
  
        toast({
          title: "Success",
          description: "Vaccination date updated successfully",
        })
      } catch (error) {
        toast({
          title: "Error",
          description: "Failed to update vaccination date",
          variant: "destructive",
        })
      }
    }
  
    if (!item.vaccinationDate && !editing) {
      return (
        <div className="flex items-center gap-2">
          <span>-</span>
          <button className="text-blue-500 underline" onClick={() => setEditing(true)}>
            Set Date
          </button>
        </div>
      )
    }
  
    if (!editing) {
      return (
        <div className="flex items-center gap-2">
          <span>{displayDateTime(item.vaccinationDate)}</span>
          <button className="text-blue-500 underline" onClick={() => setEditing(true)}>
            Edit
          </button>
        </div>
      )
    }
  
    return (
      <DateTimePicker
        date={tempDate}
        setDate={async (pickedDate) => {
          await handleSetDate(pickedDate)
        }}
        onClose={() => {
          setEditing(false)
        }}
      />
    )
  }

  return (
    <Dialog open onOpenChange={onClose}>
      <DialogContent className="max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{`Order Details - ${order.orderId}`}</DialogTitle>
          <DialogDescription>
            View detailed information about this order.
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          <div className="flex flex-wrap items-center justify-between gap-2">
            <div className="flex items-center gap-2">
              <Clock className="h-4 w-4 text-gray-500" />
              <span className="text-sm text-gray-500">Ordered on {formatDate(order.orderDate)}</span>
            </div>
            {getStatusBadge(order.status)}
          </div>

          <Separator />

          <div className="space-y-2">
            <h3 className="text-sm font-medium">Order Summary</h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-1">
                <p className="text-xs text-gray-500">Payment Method</p>
                <div className="flex items-center gap-1">
                  <CreditCard className="h-4 w-4 text-gray-500" />
                  <span className="text-sm">{order.paymentType}</span>
                </div>
              </div>
              <div className="space-y-1">
                <p className="text-xs text-gray-500">Total Amount</p>
                <p className="text-sm font-medium">{formatPrice(order.totalPrice)}</p>
              </div>
            </div>
          </div>

          <Separator />

          <div className="space-y-2">
            <h3 className="text-sm font-medium">Order Items</h3>
            <div className="rounded-md border">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Product</TableHead>
                    <TableHead>Quantity</TableHead>
                    <TableHead>Price</TableHead>
                    <TableHead>Vaccination Date</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {orderDetail && orderDetail.orderDetails.map((item) => (
                    <TableRow key={item.orderdetialid || item.orderId}>
                      <TableCell className="font-medium">{item.productName}</TableCell>
                      <TableCell>{item.quantity}</TableCell>
                      <TableCell>{formatPrice(item.price)}</TableCell>
                      <TableCell>
                        <VaccinationDateCell item={item} />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </div>

          <Separator />

          <div className="space-y-2">
            <h3 className="text-sm font-medium">Customer Information</h3>
            {orderDetail && orderDetail.orderDetails.length > 0 && (
              <div className="space-y-2 rounded-md border p-3">
                <div className="flex items-center gap-2">
                  <User className="h-4 w-4 text-gray-500" />
                  <span className="text-sm">
                    {orderDetail.orderDetails[0].firstName} {orderDetail.orderDetails[0].lastName}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <Mail className="h-4 w-4 text-gray-500" />
                  <span className="text-sm">{orderDetail.orderDetails[0].email}</span>
                </div>
                <div className="flex items-center gap-2">
                  <Phone className="h-4 w-4 text-gray-500" />
                  <span className="text-sm">{orderDetail.orderDetails[0].mobileNo}</span>
                </div>
              </div>
            )}
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}
