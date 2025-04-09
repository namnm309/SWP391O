"use client"
import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { useToast } from "@/hooks/use-toast"
import axios from "@/utils/axiosConfig"

const reasons = [
  "Customer requested cancellation",
  "Out of stock",
  "Payment issue",
  "Other",
]

interface CancelOrderModalProps {
  orderId: string
  onClose: () => void
}

export const CancelOrderModal: React.FC<CancelOrderModalProps> = ({ orderId, onClose }) => {
  const { toast } = useToast()
  const [selectedReason, setSelectedReason] = useState<string>(reasons[0])
  const [customReason, setCustomReason] = useState<string>("")

  const handleSubmit = async () => {
    const reasonToSend = selectedReason === "Other" ? customReason : selectedReason
    try {
      const token = localStorage.getItem("token")
      await axios.put(`/order/cancel-order-by-staff/${orderId}`, {
        orderId: orderId,
        reason: reasonToSend
      }, {
        headers: { Authorization: `Bearer ${token}` },
      })
      toast({
        title: "Order canceled",
        description: "The order has been canceled successfully.",
      })
      onClose()
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to cancel order",
        variant: "destructive",
      })
    }
  }

  return (
    <Dialog open={true} onOpenChange={(open) => { if (!open) onClose() }}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Cancel Order</DialogTitle>
          <DialogDescription>
            Choose a reason for canceling the order.
          </DialogDescription>
        </DialogHeader>
        <div className="space-y-4">
          <div>
            <label htmlFor="reason" className="block text-sm font-medium text-gray-700">
              Reason
            </label>
            <select
              id="reason"
              value={selectedReason}
              onChange={(e) => setSelectedReason(e.target.value)}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm"
            >
              {reasons.map((reason) => (
                <option key={reason} value={reason}>
                  {reason}
                </option>
              ))}
            </select>
          </div>
          {selectedReason === "Other" && (
            <div>
              <label htmlFor="customReason" className="block text-sm font-medium text-gray-700">
                Custom Reason
              </label>
              <input
                id="customReason"
                type="text"
                value={customReason}
                onChange={(e) => setCustomReason(e.target.value)}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm"
                placeholder="Enter custom reason"
              />
            </div>
          )}
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose}>Cancel</Button>
          <Button onClick={handleSubmit}>Submit</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
