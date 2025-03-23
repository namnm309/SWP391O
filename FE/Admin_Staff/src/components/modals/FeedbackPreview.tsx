"use client"
import { useState } from "react"
import { MessageSquare, Star, Calendar, User } from "lucide-react"
import { PreviewCard } from "@/components/ui/preview-card"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { Button } from "@/components/ui/button"
import axios from "@/utils/axiosConfig"
import type { Feedback } from "@/types/feedback"

interface FeedbackPreviewProps {
  feedback: Feedback
  onClose: () => void
}

export function FeedbackPreview({ feedback, onClose }: FeedbackPreviewProps) {
  const [replyText, setReplyText] = useState("")
  const [submitting, setSubmitting] = useState(false)

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    })
  }

  const handleSubmitReply = async () => {
    if (!replyText.trim()) return

    setSubmitting(true)
    try {
      // Use PUT and pass reply as a query parameter.
      await axios.put(`/feedback/feedback/${feedback.id}/reply`, null, {
        params: { reply: replyText },
      })
      // Optionally, you could update local feedback state to reflect the reply.
    } catch (error) {
      console.error("Error submitting reply:", error)
    } finally {
      setSubmitting(false)
    }
  }

  const renderStars = (rating: number) => {
    return (
      <div className="flex">
        {[...Array(5)].map((_, i) => (
          <Star
            key={i}
            className={`h-4 w-4 ${i < rating ? "fill-yellow-400 text-yellow-400" : "text-gray-300"}`}
          />
        ))}
      </div>
    )
  }

  return (
    <PreviewCard title="Feedback Details" onClose={onClose}>
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <Badge variant="outline" className="px-3 py-1">
            ID: {feedback.id}
          </Badge>
          <div className="flex items-center gap-1">
            <span className="font-medium">{feedback.rating}</span>
            {renderStars(feedback.rating)}
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-1">
            <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
              <User className="h-4 w-4" /> User
            </h3>
            <p className="text-sm">{feedback.user.fullname || `User #${feedback.user.id}`}</p>
          </div>

          <div className="space-y-1">
            <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
              <Calendar className="h-4 w-4" /> Date Submitted
            </h3>
            <p className="text-sm">{formatDate(feedback.updatedAt)}</p>
          </div>
        </div>

        <Separator />

        <div className="space-y-1">
          <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
            <MessageSquare className="h-4 w-4" /> Comment
          </h3>
          <div className="rounded-md border border-gray-200 p-3">
            <p className="text-sm">{feedback.comment || "No comment provided"}</p>
          </div>
        </div>

        {feedback.replied ? (
          <div className="space-y-1">
            <h3 className="text-sm font-medium text-gray-500">Reply</h3>
            <div className="rounded-md border border-gray-200 p-3">
              <p className="text-sm">{feedback.staffReply}</p>
            </div>
          </div>
        ) : (
          <div className="space-y-1">
            <h3 className="text-sm font-medium text-gray-500">Reply</h3>
            <textarea
              value={replyText}
              onChange={(e) => setReplyText(e.target.value)}
              className="w-full rounded-md border border-gray-300 p-2"
              placeholder="Type your reply here..."
              rows={4}
            />
            <Button onClick={handleSubmitReply} disabled={submitting || !replyText.trim()}>
              {submitting ? "Submitting..." : "Submit Reply"}
            </Button>
          </div>
        )}
      </div>
    </PreviewCard>
  )
}
