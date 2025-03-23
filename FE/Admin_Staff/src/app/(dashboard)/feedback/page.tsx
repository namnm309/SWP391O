"use client"

import { useState, useEffect } from "react"
import type { ColumnDef } from "@tanstack/react-table"
import { Eye, Star } from "lucide-react"
import type { Feedback } from "@/types/feedback"
import { DataTable } from "@/components/ui/data-table"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useToast } from "@/hooks/use-toast";
import { FeedbackPreview } from "@/components/modals/FeedbackPreview"
import axios from "@/utils/axiosConfig";
import { Badge } from "@/components/ui/badge"

export default function FeedbackPage() {
  const { toast } = useToast()

  const [feedback, setFeedback] = useState<Feedback[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedFeedback, setSelectedFeedback] = useState<Feedback | null>(null)

  useEffect(() => {
    const loadFeedback = async () => {
      try {
        const response = await axios.get('/feedback/sorted/desc')
        setFeedback(response.data.result)
      } catch (error: unknown) {
        const msg = error?.response?.data?.message || error?.message;
        toast({
          title: "Error",
          description: msg ||"Failed to delete category",
          variant: "destructive",
        })
      } finally {
        setLoading(false)
      }
    }
  
    loadFeedback()
  }, [toast])

  const handleViewFeedback = (feedbackId: number) => {
    const selected = feedback.find((f) => f.id === feedbackId)
    if (selected) {
      setSelectedFeedback(selected)
    }
  }

  const renderStars = (rating: number) => {
    return (
      <div className="flex">
        {[...Array(5)].map((_, i) => (
          <Star key={i} className={`h-4 w-4 ${i < rating ? "fill-yellow-400 text-yellow-400" : "text-gray-300"}`} />
        ))}
      </div>
    )
  }

  const columns: ColumnDef<Feedback>[] = [
    {
      accessorKey: "id",
      header: "ID",
    },
    {
      accessorKey: "user.fullname",
      header: "User ID",
    },
    {
      accessorKey: "comment",
      header: "Feedback",
    },
    {
      accessorKey: "rating",
      header: "Rating",
      cell: ({ row }) => {
        const rating = row.getValue("rating") as number
        return renderStars(rating)
      },
    },
    {
      accessorKey: "updatedAt",
      header: "Date",
      cell: ({ row }) => {
        const date = row.getValue("updatedAt") as string
        return new Date(date).toLocaleDateString()
      },
    },
    {
      accessorKey: "replied",
      header: "Status",
      cell: ({ row }) => {
        const replied = row.getValue("replied") as boolean
        return replied ? <Badge>Replied</Badge> : <Badge variant="destructive">Not Answered</Badge>
      },
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => {
        const feedbackId = row.getValue("id") as number

        return (
          <Button variant="outline" size="sm" onClick={() => handleViewFeedback(feedbackId)}>
            <Eye className="mr-2 h-4 w-4" />
            View
          </Button>
        )
      },
    },
  ]

  const averageRating = feedback.length > 0 ? feedback.reduce((sum, item) => sum + item.rating, 0) / feedback.length : 0

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Feedback Management</h1>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Feedback</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{feedback.length}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Average Rating</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center">
              <div className="text-2xl font-bold mr-2">{averageRating.toFixed(1)}</div>
              {renderStars(Math.round(averageRating))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">5-Star Ratings</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{feedback.filter((f) => f.rating === 5).length}</div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>All Feedback</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p>Loading feedback...</p>
            </div>
          ) : (
            <DataTable
              columns={columns}
              data={feedback}
            />
          )}
        </CardContent>
      </Card>

      {/* Preview Popup */}
      {selectedFeedback && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
          <FeedbackPreview feedback={selectedFeedback} onClose={() => setSelectedFeedback(null)} />
        </div>
      )}
    </div>
  )
}

