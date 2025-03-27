"use client"
import { useState } from "react"
import { ModalWrapper } from "@/components/ui/modal-wrapper"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Input } from "@/components/ui/input"
import { useToast } from "@/hooks/use-toast"
import { useStore } from "@/store"
import axios from "@/utils/axiosConfig"

interface CreateNotificationModalProps {
  onClose: () => void
}

export function CreateNotificationModal({ onClose }: CreateNotificationModalProps) {
  const { toast } = useToast()
  const { user } = useStore.getState().profile

  const [message, setMessage] = useState("")
  const [targetUserId, setTargetUserId] = useState("")
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsSubmitting(true)
    try {
      const token = localStorage.getItem("token")
      if (user?.role === "ADMIN") {
        await axios.post(
          "/notification/notifications/staff",
          { message },
          { headers: { Authorization: `Bearer ${token}` } }
        )
      } else if (user?.role === "STAFF") {
        if (targetUserId.trim() !== "") {
          await axios.post(
            "/notification/notifications",
            { userId: Number(targetUserId), message },
            { headers: { Authorization: `Bearer ${token}` } }
          )
        } else {
          await axios.post(
            "/notification/all",
            { message },
            { headers: { Authorization: `Bearer ${token}` } }
          )
        }
      }
      toast({
        title: "Success",
        description: "Notification sent successfully",
      })
      onClose()
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to send notification",
        variant: "destructive",
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <ModalWrapper
      title="Create Notification"
      description="Fill in the details to send a notification"
      isOpen={true}
      onClose={onClose}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="space-y-2">
          <Label htmlFor="message">Message</Label>
          <Textarea
            id="message"
            name="message"
            rows={4}
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            required
          />
        </div>
        {user?.role === "STAFF" && (
          <div className="space-y-2">
            <Label htmlFor="targetUserId">
              User ID (leave blank for all customers)
            </Label>
            <Input
              id="targetUserId"
              name="targetUserId"
              type="number"
              value={targetUserId}
              onChange={(e) => setTargetUserId(e.target.value)}
            />
          </div>
        )}
        <div className="flex justify-end space-x-2 pt-4">
          <Button type="button" variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Sending..." : "Send Notification"}
          </Button>
        </div>
      </form>
    </ModalWrapper>
  )
}
