"use client"
import { useState } from "react"
import { Syringe, Calendar, Clock } from "lucide-react"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { PreviewCard } from "@/components/ui/preview-card"
import { Vaccine } from "@/types/vaccine"

interface VaccinePreviewProps {
  vaccine: Vaccine
  onClose: () => void
}

export function VaccinePreview({ vaccine, onClose }: VaccinePreviewProps) {
  const [showFullDescription, setShowFullDescription] = useState(false)

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

  return (
    <PreviewCard title={vaccine.title} onClose={onClose}>
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <Badge variant="outline" className="px-3 py-1">
            ID: {vaccine.id}
          </Badge>
          <Badge className="bg-blue-100 text-blue-800 hover:bg-blue-100">
            {formatPrice(vaccine.price)}
          </Badge>
        </div>

        {vaccine.description && (
          <div className="space-y-1">
            <h3 className="text-sm font-medium text-gray-500">Description</h3>
            <p className="text-sm">
              {showFullDescription || vaccine.description.length <= 150
                ? vaccine.description
                : `${vaccine.description.substring(0, 150)}...`}
              {vaccine.description.length > 150 && (
                <Button
                  variant="link"
                  className="h-auto p-0 text-blue-600"
                  onClick={() => setShowFullDescription(!showFullDescription)}
                >
                  {showFullDescription ? "Show less" : "Show more"}
                </Button>
              )}
            </p>
          </div>
        )}

        <Separator />

        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-1">
            <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
              <Calendar className="h-4 w-4" /> Created At
            </h3>
            <p className="text-sm">{formatDate(vaccine.createdAt)}</p>
          </div>

          {vaccine.manufacturer && (
            <div className="space-y-1">
              <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
                Manufacturer
              </h3>
              <p className="text-sm">{vaccine.manufacturer}</p>
            </div>
          )}
        </div>

        {vaccine.schedule && (
          <div className="space-y-1">
            <h3 className="flex items-center gap-1 text-sm font-medium text-gray-500">
              Schedule
            </h3>
            <p className="text-sm">{vaccine.schedule}</p>
          </div>
        )}
      </div>
    </PreviewCard>
  )
}
